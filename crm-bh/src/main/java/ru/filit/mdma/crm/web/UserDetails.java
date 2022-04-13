package ru.filit.mdma.crm.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserDetails {

  private static final String ROLE_PREFIX = "ROLE_";

  private final String userRole;
  private final String userName;

  private UserDetails(String userRole, String userName) {
    this.userRole = userRole;
    this.userName = userName;
  }

  public static UserDetails ofAuthUser() {
    return new UserDetails(getAuthUserRole(), getAuthUserName());
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
