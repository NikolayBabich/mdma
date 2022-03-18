package ru.filit.mdma.dm.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

  private static final int ID_LOWER_BOUND = 10_000;
  private static final int ID_UPPER_BOUND = 100_000;

  private final ContactRepository contactRepository;
  private final ContactMapper contactMapper;
  private final ClientRepository clientRepository;

  public ContactService(
      ContactRepository contactRepository,
      ContactMapper contactMapper,
      ClientRepository clientRepository
  ) {
    this.contactRepository = contactRepository;
    this.contactMapper = contactMapper;
    this.clientRepository = clientRepository;
  }

  public List<ContactDto> findContacts(ClientIdDto clientIdDto) {
    String clientId = clientIdDto.getId();
    return contactRepository.getAll().stream()
        .filter(contact -> clientId.equals(contact.getClientId()))
        .map(contactMapper::toDto)
        .collect(Collectors.toList());
  }

  public ContactDto saveContact(ContactDto contactDto) {
    String clientId = contactDto.getClientId();
    if (clientRepository.getById(clientId).isEmpty()) {
      throw new NotFoundException("No Client id:" + clientId);
    }

    Contact newContact = contactMapper.fromDto(contactDto);
    ContactDto retVal;
    List<Contact> allContacts = contactRepository.getAll();
    String contactId = contactDto.getId();
    if (contactId == null) {
      Set<Integer> contactIds = allContacts.stream()
          .map(contact -> Integer.parseInt(contact.getId()))
          .collect(Collectors.toSet());
      newContact.id(generateNotExistingId(contactIds));
      allContacts.add(newContact);
      retVal = contactMapper.toDto(newContact);
    } else {
      Contact existedContact = allContacts.stream()
          .filter(contact -> clientId.equals(contact.getClientId())
              && contactId.equals(contact.getId()))
          .findAny()
          .orElseThrow(() -> new NotFoundException(
              "No Contact id:" + contactId + " for Client id:" + clientId));
      retVal = contactMapper.toDto(
          existedContact.type(newContact.getType()).value(newContact.getValue()));
    }
    contactRepository.saveAll(allContacts);
    return retVal;
  }

  private String generateNotExistingId(Set<Integer> ids) {
    return IntStream.generate(() -> RandomUtils.nextInt(ID_LOWER_BOUND, ID_UPPER_BOUND))
        .filter(id -> !ids.contains(id))
        .mapToObj(String::valueOf)
        .findFirst()
        // shouldn't get here
        .orElse(null);
  }

}
