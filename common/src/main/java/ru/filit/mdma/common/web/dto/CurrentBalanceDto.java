package ru.filit.mdma.common.web.dto;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Значение текущего баланса счета.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CurrentBalanceDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String balanceAmount;

}
