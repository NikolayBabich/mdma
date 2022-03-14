package ru.filit.mdma.crm.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.filit.mdma.common.web.dto.ContactDto;

/**
 * Клиент банка, Физ. лицо.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Builder
public class ClientDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String id;
  private final String lastname;
  private final String firstname;
  private String patronymic;
  private final String birthDate;
  private final String passportSeries;
  private final String passportNumber;
  private final String inn;
  private String address;

  private final List<ContactDto> contacts = new ArrayList<>();
  private final List<AccountDto> accounts = new ArrayList<>();

}
