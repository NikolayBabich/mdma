package ru.filit.mdma.dm.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.mapping.OperationMapper;
import ru.filit.mdma.dm.repository.OperationRepository;
import ru.filit.mdma.dm.web.dto.OperationDto;
import ru.filit.mdma.dm.web.dto.OperationSearchDto;

@Service
public class OperationService {

  private final OperationRepository repository;
  private final OperationMapper mapper;

  public OperationService(OperationRepository repository, OperationMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public List<OperationDto> findOperations(OperationSearchDto operationSearchDto) {
    String accountNumber = operationSearchDto.getAccountNumber();
    return repository.getAll().stream()
        .filter(operation -> accountNumber.equals(operation.getAccountNumber()))
        .map(mapper::toDto)
        .sorted(Comparator.comparing(OperationDto::getOperDate).reversed())
        .limit(Long.parseLong(operationSearchDto.getQuantity()))
        .collect(Collectors.toList());
  }

}
