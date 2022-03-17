package ru.filit.mdma.dm.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.exception.NotFoundException;
import ru.filit.mdma.dm.mapping.ContactMapper;
import ru.filit.mdma.dm.model.Contact;
import ru.filit.mdma.dm.repository.ClientRepository;
import ru.filit.mdma.dm.repository.ContactRepository;
import ru.filit.mdma.dm.web.dto.ClientIdDto;
import ru.filit.mdma.dm.web.dto.ContactDto;

@Service
public class ContactService {

  private final ContactRepository repository;
  private final ClientRepository clientRepository;
  private final ContactMapper mapper;

  public ContactService(ContactRepository repository,
      ClientRepository clientRepository, ContactMapper mapper) {
    this.repository = repository;
    this.clientRepository = clientRepository;
    this.mapper = mapper;
  }

  public List<ContactDto> findContacts(ClientIdDto clientIdDto) {
    String clientId = clientIdDto.getId();
    return repository.getAll().stream()
        .filter(contact -> clientId.equals(contact.getClientId()))
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

  public ContactDto saveContact(ContactDto contactDto) {
    String clientId = contactDto.getClientId();
    if (clientRepository.getById(clientId).isEmpty()) {
      throw new NotFoundException("Not found Client id=" + clientId);
    }

    Contact newContact = mapper.fromDto(contactDto);
    ContactDto retVal = null;

    List<Contact> allContacts = repository.getAll();
    String contactId = contactDto.getId();
    if (contactId == null) {
      Set<Integer> contactIds = allContacts.stream()
          .map(contact -> Integer.parseInt(contact.getId()))
          .collect(Collectors.toSet());
      newContact.setId(generateNotExistingId(contactIds));
      allContacts.add(newContact);
      retVal = mapper.toDto(newContact);
    } else {
      boolean notFound = true;
      for (Contact contact : allContacts) {
        if (clientId.equals(contact.getClientId()) && contactId.equals(contact.getId())) {
          notFound = false;
          contact.setType(newContact.getType());
          contact.setValue(newContact.getValue());
          retVal = mapper.toDto(contact);
          break;
        }
      }
      if (notFound) {
        throw new NotFoundException("No Contact id:" + contactId + " for Client id:" + clientId);
      }
    }
    repository.saveAll(allContacts);
    return retVal;
  }

  private String generateNotExistingId(Set<Integer> ids) {
    int randomId;
    do {
      randomId = RandomUtils.nextInt(10_000, 100_000);
    } while (ids.contains(randomId));
    return String.valueOf(randomId);
  }

}
