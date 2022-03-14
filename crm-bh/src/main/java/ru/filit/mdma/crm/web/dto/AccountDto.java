package ru.filit.mdma.crm.web.dto;

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
public class AccountDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String number;
  private final String clientId;
  private final String type;
  private final String currency;
  private final String status;
  private final String openDate;
  private String closeDate;
  private String deferment;
  private String shortcut;
  private String balanceAmount;

}
