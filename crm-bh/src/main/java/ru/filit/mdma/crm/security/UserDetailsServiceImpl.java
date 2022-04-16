package ru.filit.mdma.crm.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.filit.mdma.crm.model.User;
import ru.filit.mdma.crm.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository repository;

  public UserDetailsServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.readAllUsers().stream()
        .filter(user -> username.equals(user.getUsername()))
        .findAny()
        .map(UserDetailsServiceImpl::userDetailsOf)
        .orElseThrow(() -> new UsernameNotFoundException("No User with username:" + username));
  }

  private static UserDetails userDetailsOf(User user) {
    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .roles(user.getRole().name())
        .build();
  }

}
