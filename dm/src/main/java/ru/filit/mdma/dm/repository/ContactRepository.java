package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.Contact;

@Repository
public class ContactRepository extends AbstractYamlRepository<Contact> {

  private static final String YAML_PATH = "datafiles/contacts.yml";

  public ContactRepository(@Qualifier("yamlObjectMapper") ObjectMapper yamlMapper) {
    super(yamlMapper, Contact.class, YAML_PATH);
  }

  public void saveAll(List<Contact> contacts) {
    try {
      yamlMapper.writeValue(yamlFile, contacts);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
