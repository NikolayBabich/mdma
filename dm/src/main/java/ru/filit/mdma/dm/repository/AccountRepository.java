package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.Account;

@Repository
public class AccountRepository extends AbstractYamlRepository<Account> {

  private static final String YAML_PATH = "datafiles/accounts.yml";

  public AccountRepository(@Qualifier("yamlObjectMapper") ObjectMapper yamlMapper) {
    super(yamlMapper, Account.class, YAML_PATH);
  }

  public Optional<Account> getByNumber(String accountNumber) {
    return getAll().stream()
        .filter(account -> accountNumber.equals(account.getNumber()))
        .findAny();
  }

}
