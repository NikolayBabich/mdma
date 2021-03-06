package ru.filit.mdma.dms.web.controller;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.dms.service.ClientService;
import ru.filit.mdma.dms.model.RequestDetails;
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
    return ResponseEntity.ok(clientService.findClients(clientSearchDto,
        RequestDetails.create(userRole, userName, "/dm/client"))
    );
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
    return ResponseEntity.ok(clientService.findContacts(clientIdDto,
        RequestDetails.create(userRole, userName, "/dm/client/contact"))
    );
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
    return ResponseEntity.ok(clientService.findAccounts(clientIdDto,
        RequestDetails.create(userRole, userName, "/dm/client/account"))
    );
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
    return ResponseEntity.ok(clientService.getBalance(accountNumberDto,
        RequestDetails.create(userRole, userName, "/dm/client/account/balance"))
    );
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
    return ResponseEntity.ok(clientService.findOperations(operationSearchDto,
        RequestDetails.create(userRole, userName, "/dm/client/account/operation"))
    );
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
    return ResponseEntity.ok(clientService.saveContact(contactDto,
        RequestDetails.create(userRole, userName, "/dm/client/contact/save"))
    );
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
    return ResponseEntity.ok(clientService.getLevel(clientIdDto,
        RequestDetails.create(userRole, userName, "/dm/client/level"))
    );
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
    return ResponseEntity.ok(clientService.getLoanPayment(accountNumberDto,
        RequestDetails.create(userRole, userName, "/dm/client/account/loan-payment"))
    );
  }

}
