package ru.filit.mdma.dm.service;

import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.exception.NotFoundException;
import ru.filit.mdma.dm.mapping.AccountMapper;
import ru.filit.mdma.dm.model.Account;
import ru.filit.mdma.dm.model.Account.TypeEnum;
import ru.filit.mdma.dm.repository.AccountRepository;
import ru.filit.mdma.dm.util.DateTimeUtil;
import ru.filit.mdma.dm.web.dto.AccountDto;
import ru.filit.mdma.dm.web.dto.AccountNumberDto;
import ru.filit.mdma.dm.web.dto.ClientIdDto;
import ru.filit.mdma.dm.web.dto.LoanPaymentDto;

@Service
public class AccountService {

  private static final int DAYS_IN_HALF_YEAR = 182;
  private static final BigDecimal LOAN_MULTIPLIER = BigDecimal.valueOf(0.0007);

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

  private long getBalanceInPennies(Account account, Long timestamp) {
    BigDecimal balanceInPennies = balanceService.getBalance(account.getNumber(), timestamp)
        .multiply(BigDecimal.valueOf(100));
    return balanceInPennies.longValue();
  }

  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto) {
    String accountNumber = accountNumberDto.getAccountNumber();
    Optional<Account> account = accountRepository.getByNumber(accountNumber);
    if (account.isEmpty() || account.get().getType() != TypeEnum.OVERDRAFT) {
      throw new NotFoundException("No overdraft Account number:" + accountNumber);
    }

    List<BigDecimal> negativeBalances =
        DateTimeUtil.getEndsOfLastWorkdays(DAYS_IN_HALF_YEAR).stream()
            .map(timestamp -> balanceService.getBalance(accountNumber, timestamp))
            .takeWhile(balance -> balance.compareTo(BigDecimal.ZERO) < 0)
            .collect(Collectors.toList());

    BigDecimal overpaySum = Lists.reverse(negativeBalances).stream()
        .skip(account.get().getDeferment())
        .map(balance -> balance.multiply(LOAN_MULTIPLIER).setScale(2, RoundingMode.HALF_UP))
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .negate();

    return new LoanPaymentDto().amount(overpaySum.toString());
  }

}
