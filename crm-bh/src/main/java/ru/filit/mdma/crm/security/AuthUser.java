package ru.filit.mdma.crm.security;

import ru.filit.mdma.crm.model.UserExtended;

public class AuthUser extends org.springframework.security.core.userdetails.User {

  public AuthUser(UserExtended user) {
    super(user.getUsername(), user.getPassword(), user.getAuthorities());
  }

}
