package ru.filit.mdma.crm.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAttributes {

  private static final String ROLE_PREFIX = "ROLE_";

  private final String userRole;
  private final String userName;

  private UserAttributes(String userRole, String userName) {
    this.userRole = userRole;
    this.userName = userName;
  }

  public static UserAttributes ofAuthUser() {
    return new UserAttributes(getAuthUserRole(), getAuthUserName());
  }

  public String getUserRole() {
    return userRole;
  }

  public String getUserName() {
    return userName;
  }

  private static String getAuthUserRole() {
    return getAuthentication().getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .filter(authority -> authority.startsWith(ROLE_PREFIX))
        .findFirst()
        .map(role -> role.replace(ROLE_PREFIX, ""))
        .orElseThrow(() -> new IllegalStateException("Authenticated user has no role"));
  }

  private static String getAuthUserName() {
    return getAuthentication().getName();
  }

  private static Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

}
