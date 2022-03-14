package ru.filit.mdma.dm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Банковские операции по счету.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Operation implements Serializable {

  private static final long serialVersionUID = 1L;

  private OperationType type;
  private String accountNumber;
  private Long operDate;
  private BigDecimal amount;
  private String description;

}
