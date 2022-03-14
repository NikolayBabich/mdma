package ru.filit.mdma.dm.web.dto;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Права доступа к полям сущностей.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AccessDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String entity;
  private final String property;

}
