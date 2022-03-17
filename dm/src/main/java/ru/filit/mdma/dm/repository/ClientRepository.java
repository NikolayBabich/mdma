package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.Client;

@Repository
public class ClientRepository extends AbstractYamlRepository<Client> {

  private static final String YAML_PATH = "datafiles/clients.yml";

  public ClientRepository(@Qualifier("yamlObjectMapper") ObjectMapper yamlMapper) {
    super(yamlMapper, Client.class, YAML_PATH);
  }

  public Optional<Client> getById(String id) {
    return getAll().stream()
        .filter(client -> id.equals(client.getId()))
        .findAny();
  }

}
