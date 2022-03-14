package ru.filit.mdma.common.web.dto;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Начисленные платежи по кредиту.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LoanPaymentDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String amount;

}
