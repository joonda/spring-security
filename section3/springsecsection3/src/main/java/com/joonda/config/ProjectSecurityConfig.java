package com.joonda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

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

    @Bean
    public UserDetailsService userDetailsService() {
        // 기본의 application.properties에 저장한 user 정보 대신 직접 여기서 유저를 만든다
        // User 메서드를 활용, withUsername, password, authorities를 지정
        // InmemoryUserDetailsManager에 만든 user를 전달
        // 현재 password는 임시로 {noop}으로 지정, 비밀번호 인코더를 사용하지 않고, 평문으로 처리하겠다는 뜻
        // spring security에서는 권장하지 않는 방법이기에, 이후 수정 필요.
        UserDetails user = User.withUsername("user").password("{noop}EazyBytes@12345").authorities("read").build();
        UserDetails admin = User.withUsername("admin")
                /*
                * https://bcrypt-generator.com/ 에서 평문 비밀번호에 대해 Hash 값으로 생성해서 넣을 수 있다.
                * Bean에 등록되어있는 PasswordEncoder 메서드 덕분에 접두사를 기반으로 인코더를 인식할 수 있다.
                * 기존에 선언했던 접두사인 {noop}을 없애고 현재 PasswordEncoder의 기본 값인 bcrypt를 사용.
                * 가독성을 높이기 위해 {bcrypt}를 명시
                * */
                .password("{bcrypt}$2a$12$3BLaFiNenhfoxCo2Gevp5OdGpXyJpJuGyxmPgjbYTpb81v8Y1DxaW")
                .authorities("admin")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

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
