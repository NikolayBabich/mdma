package ru.filit.mdma.crm.web.controller;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.crm.service.ClientService;
import ru.filit.mdma.crm.web.dto.AccountNumberDto;
import ru.filit.mdma.crm.web.dto.ClientDto;
import ru.filit.mdma.crm.web.dto.ClientIdDto;
import ru.filit.mdma.crm.web.dto.ClientLevelDto;
import ru.filit.mdma.crm.web.dto.ClientSearchDto;
import ru.filit.mdma.crm.web.dto.ContactDto;
import ru.filit.mdma.crm.web.dto.LoanPaymentDto;
import ru.filit.mdma.crm.web.dto.OperationDto;


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

  private static final String ROLE_PREFIX = "ROLE_";

  private final ClientService clientService;

  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  /**
   * Поиск клиентов.
   *
   * @param clientSearchDto параметры поиска клиентов
   * @return список клиентов
   */
  @PostMapping("/find")
  public ResponseEntity<List<ClientDto>> findClient(ClientSearchDto clientSearchDto) {
    return ResponseEntity.ok(clientService.findClients(
        clientSearchDto, getAuthUserRole(), getAuthUserName()));
  }

  /**
   * Получение информации о клиенте.
   *
   * @param clientIdDto id клиента
   * @return информация о клиенте
   */
  @PostMapping
  public ResponseEntity<ClientDto> getClient(ClientIdDto clientIdDto) {
    return ResponseEntity.ok(clientService.getClient(
        clientIdDto, getAuthUserRole(), getAuthUserName()));
  }

  /**
   * Получение информации о последних операциях.
   *
   * @param accountNumberDto номер счета
   * @return информация о последних операциях
   */
  @PostMapping("/account/last-operations")
  public ResponseEntity<List<OperationDto>> getLastOperations(AccountNumberDto accountNumberDto) {
    return ResponseEntity.ok(clientService.getLastOperations(
        accountNumberDto, getAuthUserRole(), getAuthUserName()));
  }

  /**
   * Сохранение контакта клиента.
   *
   * @param contactDto параметры контакта клиента
   * @return сохраненный контакт клиента
   */
  @PostMapping("/contact/save")
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto) {
    return ResponseEntity.ok(clientService.saveContact(
        contactDto, getAuthUserRole(), getAuthUserName()));
  }

  /**
   * Получение уровня клиента.
   *
   * @param clientIdDto id клиента
   * @return информация об уровне клиента
   */
  @PostMapping("/level")
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto) {
    return ResponseEntity.ok(clientService.getClientLevel(
        clientIdDto, getAuthUserRole(), getAuthUserName()));
  }

  /**
   * Получение суммы процентных платежей по счету Овердрафт.
   *
   * @param accountNumberDto номер счета
   * @return сумма начисленных платежей
   */
  @PostMapping("/account/loan-payment")
  public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto) {
    return ResponseEntity.ok(clientService.getLoanPayment(
        accountNumberDto, getAuthUserRole(), getAuthUserName()));
  }

  private String getAuthUserRole() {
    return getAuthentication().getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .filter(authority -> authority.startsWith(ROLE_PREFIX))
        .findFirst()
        .map(role -> role.replace(ROLE_PREFIX, ""))
        .orElseThrow(() -> new IllegalStateException("Authenticated user has no role"));
  }

  private String getAuthUserName() {
    return getAuthentication().getName();
  }

  private Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

}
