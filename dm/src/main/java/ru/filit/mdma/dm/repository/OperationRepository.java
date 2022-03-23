package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.Operation;

@Repository
public class OperationRepository extends AbstractYamlRepository<Operation> {

  private static final String YAML_PATH = "datafiles/operations.yml";

  public OperationRepository(YAMLMapper yamlMapper) {
    super(yamlMapper, Operation.class, YAML_PATH);
  }

}
