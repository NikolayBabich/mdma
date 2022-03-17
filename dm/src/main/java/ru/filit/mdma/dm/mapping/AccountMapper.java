package ru.filit.mdma.dm.mapping;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.filit.mdma.dm.model.Account;
import ru.filit.mdma.dm.util.DateTimeUtil;
import ru.filit.mdma.dm.web.dto.AccountDto;

@Mapper(componentModel = "spring", imports = {StringUtils.class, DateTimeUtil.class})
public interface AccountMapper {

  @Mapping(target = "openDate",
      expression = "java(DateTimeUtil.getDateText(account.getOpenDate()))")
  @Mapping(target = "closeDate",
      expression = "java(DateTimeUtil.getDateText(account.getCloseDate()))")
  @Mapping(target = "shortcut",
      expression = "java(StringUtils.right(account.getNumber(), 4))")
  AccountDto toDto(Account account);

  @Mapping(target = "openDate",
      expression = "java(DateTimeUtil.getSecondsOfDate(dto.getOpenDate()))")
  @Mapping(target = "closeDate",
      expression = "java(DateTimeUtil.getSecondsOfDate(dto.getCloseDate()))")
  Account fromDto(AccountDto dto);

}
