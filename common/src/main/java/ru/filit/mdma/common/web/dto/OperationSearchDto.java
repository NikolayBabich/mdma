package ru.filit.mdma.common.web.dto;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Запрос операций по счету.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OperationSearchDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String accountNumber;
  private final String quantity;

}
