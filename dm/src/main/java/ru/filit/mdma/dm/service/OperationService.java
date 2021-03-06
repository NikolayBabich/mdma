package ru.filit.mdma.dm.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.mapping.OperationMapper;
import ru.filit.mdma.dm.model.Operation;
import ru.filit.mdma.dm.repository.OperationYamlRepository;
import ru.filit.mdma.dm.util.DateTimeUtils;
import ru.filit.mdma.dm.web.dto.OperationDto;
import ru.filit.mdma.dm.web.dto.OperationSearchDto;

@Service
public class OperationService {

  private final OperationYamlRepository repository;
  private final OperationMapper mapper;

  public OperationService(OperationYamlRepository repository, OperationMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public List<OperationDto> findOperations(OperationSearchDto operationSearchDto) {
    List<Operation> operations = getOperationsBetween(
        operationSearchDto.getAccountNumber(), 0L, DateTimeUtils.getEndOfCurrentDay());

    return operations.stream()
        .map(mapper::toDto)
        .sorted(Comparator.comparing(OperationDto::getOperDate).reversed())
        .limit(Long.parseLong(operationSearchDto.getQuantity()))
        .collect(Collectors.toList());
  }

  public List<Operation> getOperationsBetween(String accountNumber, Long from, Long to) {
    return repository.readAll().stream()
        .filter(operation -> accountNumber.equals(operation.getAccountNumber())
            && operation.getOperDate() >= from && operation.getOperDate() <= to)
        .collect(Collectors.toList());
  }

}
