package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.model.Contact;

@Repository
public class ContactRepository extends AbstractYamlRepository<Contact> {

  private static final String YAML_PATH = "datafiles/contacts.yml";

  public ContactRepository(@Qualifier("yamlObjectMapper") ObjectMapper yamlMapper) {
    super(yamlMapper, Contact.class, YAML_PATH);
  }

  public Contact save(Contact contact) {
    return null;
  }

}
