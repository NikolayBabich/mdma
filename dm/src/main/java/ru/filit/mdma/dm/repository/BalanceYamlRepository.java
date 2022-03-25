package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.AccountBalance;

@Repository
public class BalanceYamlRepository extends AbstractWritableYamlRepository<AccountBalance> {

  private static final String YAML_PATH = "datafiles/balances.yml";

  public BalanceYamlRepository(YAMLMapper yamlMapper) {
    super(yamlMapper, AccountBalance.class, YAML_PATH);
  }

}
