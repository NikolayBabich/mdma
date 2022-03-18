package ru.filit.mdma.dm.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.model.AccountBalance;
import ru.filit.mdma.dm.model.Operation.TypeEnum;
import ru.filit.mdma.dm.repository.BalanceRepository;
import ru.filit.mdma.dm.util.DateTimeUtil;
import ru.filit.mdma.dm.web.dto.AccountNumberDto;
import ru.filit.mdma.dm.web.dto.CurrentBalanceDto;

@Service
public class BalanceService {

  private final BalanceRepository balanceRepository;
  private final OperationService operationService;

  public BalanceService(
      BalanceRepository balanceRepository,
      OperationService operationService
  ) {
    this.balanceRepository = balanceRepository;
    this.operationService = operationService;
  }

  public CurrentBalanceDto getBalance(AccountNumberDto accountNumberDto) {
    BigDecimal balance = getBalance(accountNumberDto.getAccountNumber(),
        DateTimeUtil.getEndOfCurrentDay());
    return new CurrentBalanceDto().balanceAmount(balance.toString());
  }

  public BigDecimal getBalance(String accountNumber, Long targetDate) {
    Long beginOfMonth = DateTimeUtil.getBeginOfMonth(targetDate);

    BigDecimal latestBalanceAmount = balanceRepository.getAll().stream()
        .filter(balance -> accountNumber.equals(balance.getAccountNumber())
            && beginOfMonth.equals(balance.getBalanceDate()))
        .findAny()
        .map(AccountBalance::getAmount)
        .orElse(BigDecimal.ZERO);

    BigDecimal operationSum =
        operationService.getOperationsBetween(accountNumber, beginOfMonth, targetDate).stream()
            .map(operation -> operation.getType() == TypeEnum.EXPENSE
                ? operation.getAmount().negate() : operation.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return latestBalanceAmount.add(operationSum).setScale(2, RoundingMode.HALF_UP);
  }

}
