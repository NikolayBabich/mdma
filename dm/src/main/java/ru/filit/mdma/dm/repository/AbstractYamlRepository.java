package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class AbstractYamlRepository<T> implements ReadableRepository<T> {

  private final YAMLMapper yamlMapper;
  private final CollectionType collectionType;

  protected AbstractYamlRepository(YAMLMapper mapper, Class<? extends T> clazz) {
    this.yamlMapper = mapper;
    this.collectionType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
  }

  protected YAMLMapper getYamlMapper() {
    return yamlMapper;
  }

  @Override
  public List<T> readAll(InputStream is) {
    try {
      return yamlMapper.readValue(is, collectionType);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
