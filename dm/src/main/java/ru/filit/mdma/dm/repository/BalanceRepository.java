package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.AccountBalance;

@Repository
public class BalanceRepository extends AbstractYamlRepository<AccountBalance> {

  private static final String YAML_PATH = "datafiles/balances.yml";

  public BalanceRepository(@Qualifier("yamlObjectMapper") ObjectMapper yamlMapper) {
    super(yamlMapper, AccountBalance.class, YAML_PATH);
  }

}
