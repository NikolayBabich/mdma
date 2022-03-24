package ru.filit.mdma.crm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  public AuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authEx) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    Map<String, Object> responseBody = new LinkedHashMap<>();
    responseBody.put("timestamp", new Date());
    responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
    responseBody.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
    responseBody.put("message", authEx.getMessage());
    responseBody.put("path", request.getRequestURI());
    objectMapper.writeValue(response.getWriter(), responseBody);
  }

  @Override
  public void afterPropertiesSet() {
    this.setRealmName("Default");
    super.afterPropertiesSet();
  }

}
