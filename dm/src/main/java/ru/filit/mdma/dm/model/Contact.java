package ru.filit.mdma.dm.model;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Контактные данные клиента.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Builder
public class Contact  implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String id;
  private final String clientId;
  private final ContactType type;
  private final String value;

}
