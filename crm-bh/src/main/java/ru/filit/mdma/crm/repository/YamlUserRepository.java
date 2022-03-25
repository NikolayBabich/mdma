package ru.filit.mdma.crm.repository;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.crm.model.UserExtended;

@Repository
public class YamlUserRepository implements UserRepository {

  private static final String YAML_RESOURCE_PATH = "datafiles/users.yml";

  private final YAMLMapper yamlMapper;
  private final CollectionType collectionType;
  private final Resource yamlResource = new ClassPathResource(YAML_RESOURCE_PATH);

  public YamlUserRepository(YAMLMapper mapper) {
    this.yamlMapper = mapper;
    this.collectionType =
        mapper.getTypeFactory().constructCollectionType(List.class, UserExtended.class);
  }

  @Override
  public List<UserExtended> readAllUsers() {
    try {
      return yamlMapper.readValue(yamlResource.getInputStream(), collectionType);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
