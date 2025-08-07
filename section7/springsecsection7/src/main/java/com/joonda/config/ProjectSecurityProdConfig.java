package com.joonda.config;

import com.joonda.exceptionhandling.CustomAccessDeniedHandler;
import com.joonda.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@Profile("prod")
public class ProjectSecurityProdConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // permitAll -> API, MVC 경로를 보안 없이 허용
        // denyAll -> 인증된 사용자든 익명 사용자든 관계 없이 API로 들어오는 모든 request를 거부 (403 에러 반환)
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession"))
          .requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) // Only HTTPS
          .csrf(csrfConfig -> csrfConfig.disable())
          .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
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
