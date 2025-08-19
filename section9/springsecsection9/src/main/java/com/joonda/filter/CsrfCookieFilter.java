package com.joonda.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfCookieFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    /*
    * CSRF Token 꺼내기
    * */
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    csrfToken.getToken();

    /*
    * 필터 체인의 다음 필터를 실행
    * */
    filterChain.doFilter(request, response);
  }
}
