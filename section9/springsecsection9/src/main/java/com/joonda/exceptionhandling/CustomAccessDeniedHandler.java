package com.joonda.exceptionhandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    /*
     * 에러 시간, 메시지, 요청 경로를 지정
     * */
    LocalDateTime currentTimeStamp = LocalDateTime.now();
    String message = (accessDeniedException != null && accessDeniedException.getMessage() != null) ? accessDeniedException.getMessage() : "Authorization failed";
    String path = request.getRequestURI();

    /*
     * 응답 헤더와 상태 코드를 설정
     * */
    response.setHeader("joonda-denied-reason", "Authorization failed");
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType("application/json;charset=UTF-8");

    /*
     * JSON 형태로 에러 메시지를 만들어 응답 본문으로 보낸다.
     * */
    String jsonResponse =
      String.format("{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
        currentTimeStamp, HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), message, path);

    response.getWriter().write(jsonResponse);
  }
}
