package ru.filit.mdma.dm.web.dto;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Запрос прав доступа для роли.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AccessRequestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String role;
  private final String version;

}
