package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.Access;

@Repository
public class AccessYamlRepository extends AbstractYamlRepository<Access> {

  private static final Resource ACCESS_V2 = new ClassPathResource("datafiles/access2.yml");
  private static final Resource ACCESS_V3 = new ClassPathResource("datafiles/access3.yml");

  private Resource activeResource = ACCESS_V2;

  public AccessYamlRepository(YAMLMapper yamlMapper) {
    super(yamlMapper, Access.class);
  }

  public void setVersion(String version) {
    if ("2".equals(version)) {
      activeResource = ACCESS_V2;
    } else if ("3".equals(version)) {
      activeResource = ACCESS_V3;
    } else {
      throw new IllegalArgumentException("Access version is unknown");
    }
  }

  public List<Access> readAll() {
    try {
      return super.readAll(activeResource.getInputStream());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public List<Access> getByRole(String role) {
    return readAll().stream()
        .filter(access -> role.equals(access.getRole()))
        .collect(Collectors.toList());
  }

}
