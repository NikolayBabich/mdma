package ru.filit.mdma.dm.web.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.dm.service.AccountService;
import ru.filit.mdma.dm.service.BalanceService;
import ru.filit.mdma.dm.service.ClientService;
import ru.filit.mdma.dm.service.ContactService;
import ru.filit.mdma.dm.service.OperationService;
import ru.filit.mdma.dm.util.Utils;
import ru.filit.mdma.dm.web.dto.AccountDto;
import ru.filit.mdma.dm.web.dto.AccountNumberDto;
import ru.filit.mdma.dm.web.dto.ClientDto;
import ru.filit.mdma.dm.web.dto.ClientIdDto;
import ru.filit.mdma.dm.web.dto.ClientLevelDto;
import ru.filit.mdma.dm.web.dto.ClientSearchDto;
import ru.filit.mdma.dm.web.dto.ContactDto;
import ru.filit.mdma.dm.web.dto.CurrentBalanceDto;
import ru.filit.mdma.dm.web.dto.LoanPaymentDto;
import ru.filit.mdma.dm.web.dto.OperationDto;
import ru.filit.mdma.dm.web.dto.OperationSearchDto;

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
  private final ContactService contactService;
  private final AccountService accountService;
  private final BalanceService balanceService;
  private final OperationService operationService;

  public ClientController(
      ClientService clientService,
      ContactService contactService,
      AccountService accountService,
      BalanceService balanceService,
      OperationService operationService
  ) {
    this.clientService = clientService;
    this.contactService = contactService;
    this.accountService = accountService;
    this.balanceService = balanceService;
    this.operationService = operationService;
  }

  /**
   * Запрос клиентов.
   *
   * @param clientSearchDto параметры поиска клиентов
   * @return список клиентов
   */
  @PostMapping
  public ResponseEntity<List<ClientDto>> getClient(
      @Valid @RequestBody ClientSearchDto clientSearchDto) {
    if (!Utils.hasNonNullProperty(clientSearchDto)) {
      throw new IllegalArgumentException("clientSearchDto has no non-null property");
    }
    List<ClientDto> clients = clientService.findClients(clientSearchDto);
    return ResponseEntity.ok(clients);
  }

  /**
   * Запрос контактов клиента.
   *
   * @param clientIdDto id клиента
   * @return список контактов клиента
   */
  @PostMapping("/contact")
  public ResponseEntity<List<ContactDto>> getContact(@Valid @RequestBody ClientIdDto clientIdDto) {
    List<ContactDto> contacts = contactService.findContacts(clientIdDto);
    return ResponseEntity.ok(contacts);
  }

  /**
   * Запрос счетов клиента.
   *
   * @param clientIdDto id клиента
   * @return список счетов клиента
   */
  @PostMapping("/account")
  public ResponseEntity<List<AccountDto>> getAccount(@Valid @RequestBody ClientIdDto clientIdDto) {
    List<AccountDto> accounts = accountService.findAccounts(clientIdDto);
    return ResponseEntity.ok(accounts);
  }

  /**
   * Запрос баланса по счету.
   *
   * @param accountNumberDto номер счета
   * @return текущий баланс по счету
   */
  @PostMapping("/account/balance")
  public ResponseEntity<CurrentBalanceDto> getAccountBalance(
      @Valid @RequestBody AccountNumberDto accountNumberDto) {
    CurrentBalanceDto balance = balanceService.getBalance(accountNumberDto);
    return ResponseEntity.ok(balance);
  }

  /**
   * Запрос операций по счету.
   *
   * @param operationSearchDto параметры поиска операций
   * @return список операций
   */
  @PostMapping("/account/operation")
  public ResponseEntity<List<OperationDto>> getAccountOperations(
      @Valid @RequestBody OperationSearchDto operationSearchDto) {
    List<OperationDto> operations = operationService.findOperations(operationSearchDto);
    return ResponseEntity.ok(operations);
  }

  /**
   * Сохранение контакта клиента.
   *
   * @param contactDto параметры контакта клиента
   * @return сохраненный контакт клиента
   */
  @PostMapping("/contact/save")
  public ResponseEntity<ContactDto> saveContact(@Valid @RequestBody ContactDto contactDto) {
    ContactDto savedContact = contactService.saveContact(contactDto);
    return ResponseEntity.ok(savedContact);
  }

  /**
   * Получение уровня клиента.
   *
   * @param clientIdDto id клиента
   * @return уровень клиента
   */
  @PostMapping("/level")
  public ResponseEntity<ClientLevelDto> getClientLevel(
      @Valid @RequestBody ClientIdDto clientIdDto) {
    ClientLevelDto level = clientService.getLevel(clientIdDto);
    return ResponseEntity.ok(level);
  }

  /**
   * Получение суммы процентных платежей по счету Овердрафт.
   *
   * @param accountNumberDto номер счета
   * @return сумма начисленных платежей
   */
  @PostMapping("/account/loan-payment")
  public ResponseEntity<LoanPaymentDto> getLoanPayment(
      @Valid @RequestBody AccountNumberDto accountNumberDto) {
    LoanPaymentDto loanPayment = accountService.getLoanPayment(accountNumberDto);
    return ResponseEntity.ok(loanPayment);
  }

}
