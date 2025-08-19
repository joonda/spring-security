package com.joonda.config;

import com.joonda.model.Customer;
import com.joonda.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EazyBankUserDetailsService implements UserDetailsService {

  private final CustomerRepository customerRepository;

  /**
   * @param username the username identifying the user whose data is required.
   * @return
   * @throws UsernameNotFoundException
   */
  /*
  * 사용자가 로그인 폼에 정보 입력 후 제출
  * DaoAuthenticationProvider를 통해 인증 시도
  * DaoAuthenticationProvider는 UserDetailsService 구현체의 loadUserByUsername() 을 호출
  * DB 에서 유저 정보 조회
  * loadUserByUsername 에서는 DB에서 email로 Customer Entity를 찾고, Spring Security에서 사용하는 User 객체로 반환
  * 반환된 User 객체의 비밀번호와 입력된 비밀번호를 DaoAuthenticationProvider가 비교
  * 일치하면 로그인 성공
  * */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new
      UsernameNotFoundException("User not found: " + username));

    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
    return new User(customer.getEmail(), customer.getPwd(), authorities);
  }
}
