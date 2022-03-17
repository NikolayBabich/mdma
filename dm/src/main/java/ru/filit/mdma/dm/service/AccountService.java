package ru.filit.mdma.dm.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.mapping.AccountMapper;
import ru.filit.mdma.dm.model.Account;
import ru.filit.mdma.dm.repository.AccountRepository;
import ru.filit.mdma.dm.web.dto.AccountDto;
import ru.filit.mdma.dm.web.dto.AccountNumberDto;
import ru.filit.mdma.dm.web.dto.ClientIdDto;
import ru.filit.mdma.dm.web.dto.LoanPaymentDto;

@Service
public class AccountService {

  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;
  private final BalanceService balanceService;

  public AccountService(
      AccountRepository accountRepository,
      AccountMapper accountMapper,
      BalanceService balanceService
  ) {
    this.accountRepository = accountRepository;
    this.accountMapper = accountMapper;
    this.balanceService = balanceService;
  }

  public List<AccountDto> findAccounts(ClientIdDto clientIdDto) {
    String clientId = clientIdDto.getId();
    return accountRepository.getAll().stream()
        .filter(account -> clientId.equals(account.getClientId()))
        .map(accountMapper::toDto)
        .collect(Collectors.toList());
  }

  public BigDecimal getAverageBalance(Account account, List<Long> endsOfDays) {
    double averageInPennies = endsOfDays.stream()
        .mapToLong(timestamp -> getBalanceInPennies(account, timestamp))
        .average()
        .orElse(0.0);
    return BigDecimal.valueOf(averageInPennies / 100.0).setScale(2, RoundingMode.HALF_UP);
  }

  private long getBalanceInPennies(Account account, Long endOfDay) {
    BigDecimal balanceInPennies = balanceService.getBalance(account.getNumber(), endOfDay)
        .multiply(BigDecimal.valueOf(100));
    return balanceInPennies.longValue();
  }

  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto) {
    return null;
  }

}
