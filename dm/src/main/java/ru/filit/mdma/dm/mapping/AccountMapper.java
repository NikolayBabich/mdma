package ru.filit.mdma.dm.mapping;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.filit.mdma.dm.model.Account;
import ru.filit.mdma.dm.util.DateTimeUtils;
import ru.filit.mdma.dm.web.dto.AccountDto;

@Mapper(componentModel = "spring", imports = {StringUtils.class, DateTimeUtils.class})
public interface AccountMapper {

  @Mapping(target = "openDate",
      expression = "java(DateTimeUtils.getDateText(account.getOpenDate()))")
  @Mapping(target = "closeDate",
      expression = "java(DateTimeUtils.getDateText(account.getCloseDate()))")
  @Mapping(target = "shortcut",
      expression = "java(StringUtils.right(account.getNumber(), 4))")
  AccountDto toDto(Account account);

  @Mapping(target = "openDate",
      expression = "java(DateTimeUtils.getSecondsOfDate(dto.getOpenDate()))")
  @Mapping(target = "closeDate",
      expression = "java(DateTimeUtils.getSecondsOfDate(dto.getCloseDate()))")
  Account fromDto(AccountDto dto);

}
