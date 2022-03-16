package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.Account;

@Repository
public class AccountRepository extends AbstractYamlRepository<Account> {

  private static final String YAML_PATH = "datafiles/accounts.yml";

  public AccountRepository(@Qualifier("yamlObjectMapper") ObjectMapper yamlMapper) {
    super(yamlMapper, Account.class, YAML_PATH);
  }

}
