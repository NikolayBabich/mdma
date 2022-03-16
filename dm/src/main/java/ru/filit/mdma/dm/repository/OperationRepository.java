package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.Operation;

@Repository
public class OperationRepository extends AbstractYamlRepository<Operation> {

  private static final String YAML_PATH = "datafiles/operations.yml";

  public OperationRepository(@Qualifier("yamlObjectMapper") ObjectMapper yamlMapper) {
    super(yamlMapper, Operation.class, YAML_PATH);
  }

}
