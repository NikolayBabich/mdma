package ru.filit.mdma.dm.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.mapping.ContactMapper;
import ru.filit.mdma.dm.repository.ContactRepository;
import ru.filit.mdma.dm.web.dto.ClientIdDto;
import ru.filit.mdma.dm.web.dto.ContactDto;

@Service
public class ContactService {

  private final ContactRepository repository;
  private final ContactMapper mapper;

  public ContactService(ContactRepository repository, ContactMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public List<ContactDto> findContacts(ClientIdDto clientIdDto) {
    String clientId = clientIdDto.getId();
    return repository.getAll().stream()
        .filter(contact -> clientId.equals(contact.getClientId()))
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

}
