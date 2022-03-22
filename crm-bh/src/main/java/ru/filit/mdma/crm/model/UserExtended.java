package ru.filit.mdma.crm.model;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class UserExtended extends User {

  public Collection<GrantedAuthority> getAuthorities() {
    return Collections.unmodifiableList(
        AuthorityUtils.createAuthorityList("ROLE_" + this.getRole().getValue()));
  }

}
