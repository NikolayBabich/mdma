package ru.filit.mdma.dm.mapping;

import java.math.RoundingMode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.filit.mdma.dm.model.Operation;
import ru.filit.mdma.dm.util.DateTimeUtil;
import ru.filit.mdma.dm.web.dto.OperationDto;

@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, RoundingMode.class})
public interface OperationMapper {

  @Mapping(target = "operDate",
      expression = "java(DateTimeUtil.getDateTimeText(operation.getOperDate()))")
  @Mapping(target = "amount",
      expression = "java(operation.getAmount().setScale(2, RoundingMode.HALF_UP).toString())")
  OperationDto toDto(Operation operation);

  @Mapping(target = "operDate",
      expression = "java(DateTimeUtil.getSecondsOfDateTime(dto.getOperDate()))")
  Operation fromDto(OperationDto dto);

}
