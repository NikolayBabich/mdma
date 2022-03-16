package ru.filit.mdma.dm.service;

import java.math.RoundingMode;
import java.util.Comparator;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.exception.NotFoundException;
import ru.filit.mdma.dm.model.AccountBalance;
import ru.filit.mdma.dm.repository.BalanceRepository;
import ru.filit.mdma.dm.web.dto.AccountNumberDto;
import ru.filit.mdma.dm.web.dto.CurrentBalanceDto;

@Service
public class BalanceService {

  private final BalanceRepository repository;

  public BalanceService(BalanceRepository repository) {
    this.repository = repository;
  }

  public CurrentBalanceDto getBalance(AccountNumberDto accountNumberDto) {
    String accountNumber = accountNumberDto.getAccountNumber();
    AccountBalance latestBalance = repository.getAll().stream()
        .filter(balance -> accountNumber.equals(balance.getAccountNumber()))
        .max(Comparator.comparing(AccountBalance::getBalanceDate))
        .orElseThrow(() -> new NotFoundException("No balances for account № " + accountNumber));

    return new CurrentBalanceDto()
        .balanceAmount(latestBalance.getAmount().setScale(2, RoundingMode.HALF_UP).toString());
  }

}
