package ru.filit.mdma.dm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Подбитые балансы на счетах на начало месяца.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AccountBalance implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String accountNumber;
  private final Long balanceDate;
  private final BigDecimal amount;

}
