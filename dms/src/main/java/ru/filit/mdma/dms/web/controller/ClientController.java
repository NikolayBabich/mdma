package ru.filit.mdma.dms.web.controller;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.dms.service.ClientService;
import ru.filit.mdma.dms.web.dto.AccountDto;
import ru.filit.mdma.dms.web.dto.AccountNumberDto;
import ru.filit.mdma.dms.web.dto.ClientDto;
import ru.filit.mdma.dms.web.dto.ClientIdDto;
import ru.filit.mdma.dms.web.dto.ClientLevelDto;
import ru.filit.mdma.dms.web.dto.ClientSearchDto;
import ru.filit.mdma.dms.web.dto.ContactDto;
import ru.filit.mdma.dms.web.dto.CurrentBalanceDto;
import ru.filit.mdma.dms.web.dto.LoanPaymentDto;
import ru.filit.mdma.dms.web.dto.OperationDto;
import ru.filit.mdma.dms.web.dto.OperationSearchDto;

/**
 * Контроллер запросов информации о клиенте.
 */
@RestController
@RequestMapping(
    path = "/client",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
public class ClientController implements ClientApi {

  private final ClientService clientService;

  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  /**
   * Запрос клиентов.
   *
   * @param clientSearchDto параметры поиска клиентов
   * @param userRole        роль пользователя
   * @param userName        имя пользователя
   * @return список клиентов
   */
  @PostMapping
  public ResponseEntity<List<ClientDto>> getClient(ClientSearchDto clientSearchDto,
      String userRole, String userName
  ) {
    String uri = "/dm/client";
    return ResponseEntity.ok(clientService.findClients(clientSearchDto, userRole, userName, uri));
  }

  /**
   * Запрос контактов клиента.
   *
   * @param clientIdDto id клиента
   * @param userRole    роль пользователя
   * @param userName    имя пользователя
   * @return список контактов клиента
   */
  @PostMapping("/contact")
  public ResponseEntity<List<ContactDto>> getContact(ClientIdDto clientIdDto,
      String userRole, String userName
  ) {
    String uri = "/dm/client/contact";
    return ResponseEntity.ok(clientService.findContacts(clientIdDto, userRole, userName, uri));
  }

  /**
   * Запрос счетов клиента.
   *
   * @param clientIdDto id клиента
   * @param userRole    роль пользователя
   * @param userName    имя пользователя
   * @return список счетов клиента
   */
  @PostMapping("/account")
  public ResponseEntity<List<AccountDto>> getAccount(ClientIdDto clientIdDto,
      String userRole, String userName
  ) {
    String uri = "/dm/client/account";
    return ResponseEntity.ok(clientService.findAccounts(clientIdDto, userRole, userName, uri));
  }

  /**
   * Запрос баланса по счету.
   *
   * @param accountNumberDto номер счета
   * @param userRole         роль пользователя
   * @param userName         имя пользователя
   * @return текущий баланс по счету
   */
  @PostMapping("/account/balance")
  public ResponseEntity<CurrentBalanceDto> getAccountBalance(AccountNumberDto accountNumberDto,
      String userRole, String userName
  ) {
    String uri = "/dm/client/account/balance";
    return ResponseEntity.ok(clientService.getBalance(accountNumberDto, userRole, userName, uri));
  }

  /**
   * Запрос операций по счету.
   *
   * @param operationSearchDto параметры поиска операций
   * @param userRole           роль пользователя
   * @param userName           имя пользователя
   * @return список операций
   */
  @PostMapping("/account/operation")
  public ResponseEntity<List<OperationDto>> getAccountOperations(
      OperationSearchDto operationSearchDto, String userRole, String userName
  ) {
    String uri = "/dm/client/account/operation";
    return ResponseEntity.ok(clientService.findOperations(operationSearchDto, userRole,
        userName, uri));
  }

  /**
   * Сохранение контакта клиента.
   *
   * @param contactDto параметры контакта клиента
   * @param userRole   роль пользователя
   * @param userName   имя пользователя
   * @return сохраненный контакт клиента
   */
  @PostMapping("/contact/save")
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto,
      String userRole, String userName
  ) {
    String uri = "/dm/client/contact/save";
    return ResponseEntity.ok(clientService.saveContact(contactDto, userRole, userName, uri));
  }

  /**
   * Получение уровня клиента.
   *
   * @param clientIdDto id клиента
   * @param userRole    роль пользователя
   * @param userName    имя пользователя
   * @return уровень клиента
   */
  @PostMapping("/level")
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto,
      String userRole, String userName
  ) {
    String uri = "/dm/client/level";
    return ResponseEntity.ok(clientService.getLevel(clientIdDto, userRole, userName, uri));
  }

  /**
   * Получение суммы процентных платежей по счету Овердрафт.
   *
   * @param accountNumberDto номер счета
   * @param userRole         роль пользователя
   * @param userName         имя пользователя
   * @return сумма начисленных платежей
   */
  @PostMapping("/account/loan-payment")
  public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto,
      String userRole, String userName
  ) {
    String uri = "/dm/client/account/loan-payment";
    return ResponseEntity.ok(clientService.getLoanPayment(accountNumberDto, userRole,
        userName, uri));
  }

}
