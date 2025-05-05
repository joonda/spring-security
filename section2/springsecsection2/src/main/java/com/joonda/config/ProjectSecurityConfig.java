package com.joonda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
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
}
