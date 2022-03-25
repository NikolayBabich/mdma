package ru.filit.mdma.dm.web.controller;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.dm.service.AccessService;
import ru.filit.mdma.dm.web.dto.AccessDto;
import ru.filit.mdma.dm.web.dto.AccessRequestDto;

/**
 * Контроллер запросов прав доступа.
 */
@RestController
@RequestMapping(
    path = "/access",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
public class AccessController implements AccessApi {

  private final AccessService accessService;

  public AccessController(AccessService accessService) {
    this.accessService = accessService;
  }

  /**
   * Запрос прав доступа для роли.
   *
   * @param accessRequestDto параметры запроса доступа
   * @return список прав доступа к полям сущностей
   */
  @PostMapping
  public ResponseEntity<List<AccessDto>> getAccess(AccessRequestDto accessRequestDto) {
    return ResponseEntity.ok(accessService.getAccess(accessRequestDto));
  }

}
