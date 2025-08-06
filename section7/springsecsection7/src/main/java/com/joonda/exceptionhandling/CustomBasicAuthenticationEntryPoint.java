package com.joonda.exceptionhandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

  /**
   * @param request       that resulted in an <code>AuthenticationException</code>
   * @param response      so that the user agent can begin authentication
   * @param authException that caused the invocation
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

    /*
    * 에러 시간, 메시지, 요청 경로를 지정
    * */
    LocalDateTime currentTimeStamp = LocalDateTime.now();
    String message = (authException != null && authException.getMessage() != null) ? authException.getMessage() : "Unauthorized";
    String path = request.getRequestURI();

    /*
    * 응답 헤더와 상태 코드를 설정
    * */
    response.setHeader("joonda-error-reason", "Authentication failed");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json;charset=UTF-8");

    /*
    * JSON 형태로 에러 메시지를 만들어 응답 본문으로 보낸다.
    * */
    String jsonResponse =
      String.format("{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
        currentTimeStamp, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), message, path);

    response.getWriter().write(jsonResponse);
  }
}
