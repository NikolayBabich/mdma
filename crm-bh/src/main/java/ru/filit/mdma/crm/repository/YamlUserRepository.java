package ru.filit.mdma.crm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.crm.model.UserExtended;

@Repository
public class YamlUserRepository implements UserRepository {

  private static final String YAML_RESOURCE_PATH = "datafiles/users.yml";

  private final ObjectMapper yamlMapper;
  private final CollectionType collectionType;
  private final Resource yamlResource = new ClassPathResource(YAML_RESOURCE_PATH);

  public YamlUserRepository(@Qualifier("yamlObjectMapper") ObjectMapper mapper) {
    this.yamlMapper = mapper;
    this.collectionType =
        mapper.getTypeFactory().constructCollectionType(List.class, UserExtended.class);
  }

  @Override
  public List<UserExtended> getAllUsers() {
    try {
      return yamlMapper.readValue(yamlResource.getInputStream(), collectionType);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
