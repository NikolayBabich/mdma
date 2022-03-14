package ru.filit.mdma.common.web.dto;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Параметры поиска клиентов.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Builder
public class ClientSearchDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;
  private String lastname;
  private String firstname;
  private String patronymic;
  private String birthDate;
  private String passport;
  private String inn;

}
