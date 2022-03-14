package ru.filit.mdma.common.web.dto;

import java.io.Serializable;
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
public class OperationDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String type;
  private final String accountNumber;
  private final String operDate;
  private final String amount;
  private String description;

}
