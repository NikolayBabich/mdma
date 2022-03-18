package ru.filit.mdma.dm.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.exception.NotFoundException;
import ru.filit.mdma.dm.mapping.AccountMapper;
import ru.filit.mdma.dm.mapping.ClientMapper;
import ru.filit.mdma.dm.model.Account.StatusEnum;
import ru.filit.mdma.dm.model.Client;
import ru.filit.mdma.dm.model.ClientLevel;
import ru.filit.mdma.dm.repository.ClientRepository;
import ru.filit.mdma.dm.util.DateTimeUtil;
import ru.filit.mdma.dm.util.Utils;
import ru.filit.mdma.dm.web.dto.ClientDto;
import ru.filit.mdma.dm.web.dto.ClientIdDto;
import ru.filit.mdma.dm.web.dto.ClientLevelDto;
import ru.filit.mdma.dm.web.dto.ClientSearchDto;

@Service
public class ClientService {

  private static final int DAYS_FOR_AVG_BALANCE = 30;

  private final ClientRepository clientRepository;
  private final ClientMapper clientMapper;
  private final AccountService accountService;
  private final AccountMapper accountMapper;

  public ClientService(
      ClientRepository clientRepository,
      ClientMapper clientMapper,
      AccountService accountService,
      AccountMapper accountMapper
  ) {
    this.clientRepository = clientRepository;
    this.clientMapper = clientMapper;
    this.accountService = accountService;
    this.accountMapper = accountMapper;
  }

  public List<ClientDto> findClients(ClientSearchDto clientSearchDto) {
    Client searchSample = clientMapper.fromSearchDto(clientSearchDto);
    return clientRepository.getAll().stream()
        .filter(client -> Utils.equalsByNonNull(searchSample, client))
        .map(clientMapper::toDto)
        .collect(Collectors.toList());
  }

  public ClientLevelDto getLevel(ClientIdDto clientIdDto) {
    String clientId = clientIdDto.getId();
    if (clientRepository.getById(clientId).isEmpty()) {
      throw new NotFoundException("No Client id:" + clientId);
    }

    ClientLevelHelper helper = accountService.findAccounts(clientIdDto).stream()
        .map(accountMapper::fromDto)
        .filter(account -> account.getStatus() == StatusEnum.ACTIVE)
        .map(account -> new ClientLevelHelper(
            account.getNumber(),
            accountService.getAverageBalance(
                account, DateTimeUtil.getEndsOfLastDays(DAYS_FOR_AVG_BALANCE)
            ))
        )
        .max(Comparator.comparing(ClientLevelHelper::getAvgBalance))
        .orElseThrow(() -> new NotFoundException("No active Account for Client id:" + clientId));

    return new ClientLevelDto()
        .level(helper.getLevel().getValue())
        .accountNumber(helper.getAccountNumber())
        .avgBalance(helper.getAvgBalance().toString());
  }

  private static class ClientLevelHelper {

    private static final BigDecimal MIDDLE_LEVEL_LIMIT = BigDecimal.valueOf(30_000);
    private static final BigDecimal SILVER_LEVEL_LIMIT = BigDecimal.valueOf(300_000);
    private static final BigDecimal GOLD_LEVEL_LIMIT = BigDecimal.valueOf(1_000_000);

    private final String accountNumber;
    private final BigDecimal avgBalance;
    private final ClientLevel level;

    public ClientLevelHelper(String accountNumber, BigDecimal avgBalance) {
      this.accountNumber = accountNumber;
      this.avgBalance = avgBalance;
      this.level = computeLevel(avgBalance);
    }

    private String getAccountNumber() {
      return accountNumber;
    }

    private BigDecimal getAvgBalance() {
      return avgBalance;
    }

    private ClientLevel getLevel() {
      return level;
    }

    private ClientLevel computeLevel(BigDecimal avgBalance) {
      if (avgBalance.compareTo(MIDDLE_LEVEL_LIMIT) < 0) {
        return ClientLevel.Low;
      } else if (avgBalance.compareTo(SILVER_LEVEL_LIMIT) < 0) {
        return ClientLevel.Middle;
      } else if (avgBalance.compareTo(GOLD_LEVEL_LIMIT) < 0) {
        return ClientLevel.Silver;
      } else {
        return ClientLevel.Gold;
      }
    }

  }

}
