package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.Account;

@Repository
public class AccountYamlRepository extends AbstractWritableYamlRepository<Account> {

  private static final String YAML_PATH = "datafiles/accounts.yml";

  public AccountYamlRepository(YAMLMapper yamlMapper) {
    super(yamlMapper, Account.class, YAML_PATH);
  }

  public Optional<Account> getByNumber(String accountNumber) {
    return readAll().stream()
        .filter(account -> accountNumber.equals(account.getNumber()))
        .findAny();
  }

}
