package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import ru.filit.mdma.dm.util.FileUtil;

public abstract class AbstractYamlRepository<T> implements YamlRepository<T> {

  protected final YAMLMapper yamlMapper;
  private final CollectionType collectionType;
  protected final File yamlFile;

  protected AbstractYamlRepository(YAMLMapper mapper, Class<? extends T> clazz, String yamlPath) {
    this.yamlMapper = mapper;
    this.collectionType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
    this.yamlFile = FileUtil.copyOutsideOfJar(yamlPath);
  }

  @Override
  public List<T> getAll() {
    try {
      return yamlMapper.readValue(yamlFile, collectionType);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
