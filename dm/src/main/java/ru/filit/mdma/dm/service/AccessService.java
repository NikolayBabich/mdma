package ru.filit.mdma.dm.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.filit.mdma.dm.mapping.AccessMapper;
import ru.filit.mdma.dm.model.Access;
import ru.filit.mdma.dm.repository.AccessYamlRepository;
import ru.filit.mdma.dm.web.dto.AccessDto;
import ru.filit.mdma.dm.web.dto.AccessRequestDto;

@Service
public class AccessService {

  private final AccessYamlRepository repository;
  private final AccessMapper mapper;

  public AccessService(AccessYamlRepository repository, AccessMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public List<AccessDto> getAccess(AccessRequestDto accessRequestDto) {
    repository.setVersion(accessRequestDto.getVersion());
    String role = accessRequestDto.getRole();
    List<Access> accessByRole = repository.getByRole(role);
    if (accessByRole.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No Access for Role:" + role);
    }
    return accessByRole.stream()
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

}
