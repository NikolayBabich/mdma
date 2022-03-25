package ru.filit.mdma.dm.mapping;

import org.mapstruct.Mapper;
import ru.filit.mdma.dm.model.Access;
import ru.filit.mdma.dm.web.dto.AccessDto;

@Mapper(componentModel = "spring")
public interface AccessMapper {

  AccessDto toDto(Access account);

}
