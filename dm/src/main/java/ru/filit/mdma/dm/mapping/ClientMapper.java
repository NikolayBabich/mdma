package ru.filit.mdma.dm.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.filit.mdma.dm.model.Client;
import ru.filit.mdma.dm.util.DateTimeUtils;
import ru.filit.mdma.dm.web.dto.ClientDto;
import ru.filit.mdma.dm.web.dto.ClientSearchDto;

@Mapper(componentModel = "spring", imports = DateTimeUtils.class)
public interface ClientMapper {

  @Mapping(target = "birthDate",
      expression = "java(DateTimeUtils.getDateText(client.getBirthDate()))")
  ClientDto toDto(Client client);

  @Mapping(target = "birthDate",
      expression = "java(DateTimeUtils.getSecondsOfDate(dto.getBirthDate()))")
  Client fromDto(ClientDto dto);

  @Mapping(target = "birthDate",
      expression = "java(DateTimeUtils.getSecondsOfDate(dto.getBirthDate()))")
  @Mapping(target = "passportSeries", expression = "java((dto.getPassport() != null)"
      + " ? dto.getPassport().replaceAll(\"\\\\s.+\", \"\") : null)")
  @Mapping(target = "passportNumber", expression = "java((dto.getPassport() != null)"
      + " ? dto.getPassport().replaceAll(\"^.+\\\\s\", \"\") : null)")
  Client fromSearchDto(ClientSearchDto dto);

}
