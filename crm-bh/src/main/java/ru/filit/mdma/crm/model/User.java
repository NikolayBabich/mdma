package ru.filit.mdma.crm.model;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Пользователь системы.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String username;
  private final String password;
  private final Role role;

}
