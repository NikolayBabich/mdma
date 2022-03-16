package ru.filit.mdma.dm.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.mapping.ClientMapper;
import ru.filit.mdma.dm.model.Client;
import ru.filit.mdma.dm.repository.ClientRepository;
import ru.filit.mdma.dm.util.Utils;
import ru.filit.mdma.dm.web.dto.ClientDto;
import ru.filit.mdma.dm.web.dto.ClientSearchDto;

@Service
public class ClientService {

  private final ClientRepository repository;
  private final ClientMapper mapper;

  public ClientService(ClientRepository repository, ClientMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public List<ClientDto> findClients(ClientSearchDto clientSearchDto) {
    Client searchSample = mapper.fromSearchDto(clientSearchDto);
    return repository.getAll().stream()
        .filter(client -> Utils.equalsByNonNull(searchSample, client))
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

}
