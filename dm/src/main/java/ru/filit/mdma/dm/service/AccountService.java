package ru.filit.mdma.dm.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.mapping.AccountMapper;
import ru.filit.mdma.dm.repository.AccountRepository;
import ru.filit.mdma.dm.web.dto.AccountDto;
import ru.filit.mdma.dm.web.dto.ClientIdDto;

@Service
public class AccountService {

  private final AccountRepository repository;
  private final AccountMapper mapper;

  public AccountService(AccountRepository repository, AccountMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public List<AccountDto> findAccounts(ClientIdDto clientIdDto) {
    String clientId = clientIdDto.getId();
    return repository.getAll().stream()
        .filter(account -> clientId.equals(account.getClientId()))
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

}
