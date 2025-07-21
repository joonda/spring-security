package com.joonda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class ProjectSecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // permitAll -> API, MVC 경로를 보안 없이 허용
        // denyAll -> 인증된 사용자든 익명 사용자든 관계 없이 API로 들어오는 모든 request를 거부 (403 에러 반환)
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        http.authorizeHttpRequests((requests) -> requests
                // 로그인이 필요한 페이지는 보안을 설정하고, 로그인이 필요없는 페이지는 permitAll로 보안을 해지.
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                // 기본적으로 Spring Security가 런타임 에러가 발생하면 error 페이지로 리디렉션한다
                // 해당 error 페이지는 기본적으로 authenticated 상태이기 때문에, permitAll 로 설정해줘야 오류 메시지를 볼 수 있다.
                // 상황에 따라서 error 페이지를 authenticated로 할지, permitAll로 할지는 선택!
                // 어디에도 명시를 하지 않으면, 기본적으로 Spring Security 프레임워크는 항상 403 오류 발생
                .requestMatchers("/notices", "/contact", "/error").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
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
