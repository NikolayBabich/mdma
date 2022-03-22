package ru.filit.mdma.crm.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.filit.mdma.crm.repository.UserRepository;

@Service
public class YamlUserDetailsService implements UserDetailsService {

  private final UserRepository repository;

  public YamlUserDetailsService(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.getAllUsers().stream()
        .filter(u -> username.equals(u.getUsername()))
        .findAny()
        .map(AuthUser::new)
        .orElseThrow(() -> new UsernameNotFoundException("No User with username:" + username));
  }

}
