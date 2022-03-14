package ru.filit.mdma.common.web.dto;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Уровни клиентов.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ClientLevelDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String level;
  private final String accountNumber;
  private final String avgBalance;

}
