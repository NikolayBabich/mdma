package ru.filit.mdma.crm.security;

import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authEx) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(new JSONObject()
        .put("timestamp", new Date().getTime())
        .put("status", HttpStatus.UNAUTHORIZED.value())
        .put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase())
        .put("message", authEx.getMessage())
        .put("path", request.getRequestURI())
        .toString());
  }

  @Override
  public void afterPropertiesSet() {
    this.setRealmName("Default");
    super.afterPropertiesSet();
  }

}
