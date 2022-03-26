package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import ru.filit.mdma.dm.util.FileUtils;

public abstract class AbstractWritableYamlRepository<T>
    extends AbstractYamlRepository<T> implements WritableRepository<T> {

  private final File yamlFile;

  protected AbstractWritableYamlRepository(YAMLMapper mapper, Class<? extends T> clazz,
      String yamlPath
  ) {
    super(mapper, clazz);
    this.yamlFile = FileUtils.copyOutsideOfJar(yamlPath);
  }

  public List<T> readAll() {
    try {
      return super.readAll(new FileInputStream(yamlFile));
    } catch (FileNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void writeAll(List<T> entities) {
    try {
      yamlMapper.writeValue(yamlFile, entities);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
