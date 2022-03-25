package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.Contact;

@Repository
public class ContactYamlRepository extends AbstractWritableYamlRepository<Contact> {

  private static final String YAML_PATH = "datafiles/contacts.yml";

  public ContactYamlRepository(YAMLMapper yamlMapper) {
    super(yamlMapper, Contact.class, YAML_PATH);
  }

}
