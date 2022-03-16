package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public abstract class AbstractYamlRepository<T> implements YamlRepository<T> {

  protected final ObjectMapper yamlMapper;
  private final CollectionType collectionType;
  private final URL yamlUrl;

  protected AbstractYamlRepository(ObjectMapper mapper, Class<? extends T> clazz, String yamlPath) {
    this.yamlMapper = mapper;
    this.collectionType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
    this.yamlUrl = Resources.getResource(yamlPath);
  }

  @Override
  public List<T> getAll() {
    try {
      return yamlMapper.readValue(yamlUrl.openStream(), collectionType);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
