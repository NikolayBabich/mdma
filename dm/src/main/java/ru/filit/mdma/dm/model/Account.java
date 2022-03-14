package ru.filit.mdma.dm.model;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Банковские счета клиента.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "number")
@Builder
public class Account implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String number;
  private final String clientId;
  private final AccountType type;
  @Builder.Default
  private CurrencyCode currency = CurrencyCode.RUR;
  private final AccountStatus status;
  private final Long openDate;
  private Long closeDate;
  private Integer deferment;

}
