package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.Client;

@Repository
public class ClientYamlRepository extends AbstractWritableYamlRepository<Client> {

  private static final String YAML_PATH = "datafiles/clients.yml";

  public ClientYamlRepository(YAMLMapper yamlMapper) {
    super(yamlMapper, Client.class, YAML_PATH);
  }

  public Optional<Client> getById(String id) {
    return readAll().stream()
        .filter(client -> id.equals(client.getId()))
        .findAny();
  }

}
