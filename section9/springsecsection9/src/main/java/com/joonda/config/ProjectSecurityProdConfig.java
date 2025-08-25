package com.joonda.config;

import com.joonda.exceptionhandling.CustomAccessDeniedHandler;
import com.joonda.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.joonda.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@Profile("prod")
public class ProjectSecurityProdConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

        http.securityContext(contextConfig -> contextConfig.requireExplicitSave(false))
          .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
          .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
              CorsConfiguration config = new CorsConfiguration();
              config.setAllowedOrigins(Collections.singletonList("https://localhost:4200"));
              config.setAllowedMethods(Collections.singletonList("*"));
              config.setAllowCredentials(true);
              config.setAllowedHeaders(Collections.singletonList("*"));
              config.setMaxAge(3600L);
              return config;
            }
          }))
          .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
            .ignoringRequestMatchers("/contact", "/register")
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
          .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
          .requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) // Only HTTPS
          .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/myAccount").hasRole("USER")
            .requestMatchers("/myBalance").hasAnyRole("ADMIN", "USER")
            .requestMatchers("/myLoans").hasRole("USER")
            .requestMatchers("/myCards").hasRole("USER")
            .requestMatchers("/user").authenticated()
            .requestMatchers("/notices", "/contact", "/error", "/register", "/invalidSession").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
      return http.build();
    }

    /*
    * 기존의 InMemoryUserDetailManager 형식으로 유저를 메모리 기반으로 만들어 줬다면
    * 이제는 Database에 유저 정보를 직접 저장한 상태이기 때문에,
    * JdbcUserDetailsManager에 DataSource를 직접 파라미터로 전달해준다.
    * */
//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);
//    }

    /*
    * PasswordEncoderFactories 활용
    * BCryptPasswordEncoder가 기본값으로 지정이 되어있다. (Spring Security 에서 권장)
    * return new BCryptPasswordEncoder(); 를 사용할 수 있찌만, Factories를 쓰는 이유는 유연성을 제공한다.
    * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*
    * Spring Security 6.3 버전부터 도입
    * 비밀번호가 유출되었는지 확인하는 데 도움을 주는 interface
    * 사용자가 강력한 비밀번호를 사용할 수 있도록 강제한다.
    * */
    @Bean
    public CompromisedPasswordChecker passwordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
