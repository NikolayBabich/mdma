package ru.filit.mdma.dm.web.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

  /**
   * Запрос клиентов.
   *
   * @param clientSearchDto параметры поиска клиентов
   * @return список клиентов
   */
  @PostMapping
  public ResponseEntity<List<ClientDto>> getClient(
      @Valid @RequestBody ClientSearchDto clientSearchDto) {
    return null;
  }

  /**
   * Запрос контактов клиента.
   *
   * @param clientIdDto id клиента
   * @return список контактов клиента
   */
  @PostMapping("/contact")
  public ResponseEntity<List<ContactDto>> getContact(@Valid @RequestBody ClientIdDto clientIdDto) {
    return null;
  }

  /**
   * Запрос счетов клиента.
   *
   * @param clientIdDto id клиента
   * @return список счетов клиента
   */
  @PostMapping("/account")
  public ResponseEntity<List<AccountDto>> getAccount(@Valid @RequestBody ClientIdDto clientIdDto) {
    return null;
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
    return null;
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
    return null;
  }

  /**
   * Сохранение контакта клиента.
   *
   * @param contactDto параметры контакта клиента
   * @return сохраненный контакт клиента
   */
  @PostMapping("/contact/save")
  public ResponseEntity<ContactDto> saveContact(@Valid @RequestBody ContactDto contactDto) {
    return null;
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
    return null;
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
    return null;
  }

}
