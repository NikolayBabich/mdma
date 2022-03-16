package ru.filit.mdma.dm.mapping;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.filit.mdma.dm.model.Contact;
import ru.filit.mdma.dm.util.Utils;
import ru.filit.mdma.dm.web.dto.ContactDto;

@Mapper(componentModel = "spring", imports = {StringUtils.class, Utils.class})
public interface ContactMapper {

  @Mapping(target = "shortcut",
      expression = "java((contact.getType() == TypeEnum.PHONE)"
          + " ? StringUtils.right(contact.getValue(), 4) : Utils.cutEmail(contact.getValue()))")
  ContactDto toDto(Contact contact);

  Contact fromDto(ContactDto dto);

}
