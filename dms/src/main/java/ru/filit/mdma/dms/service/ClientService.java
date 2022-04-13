package ru.filit.mdma.dms.service;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static ru.filit.mdma.dms.model.EventType.REQUEST;
import static ru.filit.mdma.dms.model.EventType.RESPONSE;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.filit.mdma.dms.util.MaskingService;
import ru.filit.mdma.dms.web.RequestDetails;
import ru.filit.mdma.dms.web.dto.AccessDto;
import ru.filit.mdma.dms.web.dto.AccessRequestDto;
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

@Service
public class ClientService {

  private static final String VERSION = "3";
  private static final int TIMEOUT_IN_SECONDS = 5;
  private static final int ID_LOWER_BOUND = 10_000_000;
  private static final int ID_UPPER_BOUND = 100_000_000;
  private static final String AUDITOR = "AUDITOR";

  private final WebClient webClient;
  private final MaskingService maskingService;
  private final AuditService auditService;

  public ClientService(
      WebClient webClient,
      MaskingService maskingService,
      AuditService auditService
  ) {
    this.webClient = webClient;
    this.maskingService = maskingService;
    this.auditService = auditService;
  }

  public List<AccessDto> getAccess(String role) {
    return sendRequestForList("/access", new AccessRequestDto(role, VERSION),
        new ParameterizedTypeReference<>() {});
  }

  public List<ClientDto> findClients(ClientSearchDto clientSearchDto, RequestDetails details) {
    int requestId = generateRequestId();
    ClientSearchDto unmasked = unmask(clientSearchDto);
    auditService.audit(requestId, details.getUserName(), REQUEST, details.getUri(), unmasked);

    List<ClientDto> clients = sendRequestForList("/client", unmasked,
        new ParameterizedTypeReference<>() {});

    auditService.audit(requestId, details.getUserName(), RESPONSE, details.getUri(),
        mask(clients, getAccess(AUDITOR)));

    return mask(clients, getAccess(details.getUserRole()));
  }

  public List<ContactDto> findContacts(ClientIdDto clientIdDto, RequestDetails details) {
    int requestId = generateRequestId();
    ClientIdDto unmasked = unmask(clientIdDto);
    auditService.audit(requestId, details.getUserName(), REQUEST, details.getUri(), unmasked);

    List<ContactDto> contacts = sendRequestForList("/client/contact", unmask(clientIdDto),
        new ParameterizedTypeReference<>() {});

    auditService.audit(requestId, details.getUserName(), RESPONSE, details.getUri(),
        mask(contacts, getAccess(AUDITOR)));

    return mask(contacts, getAccess(details.getUserRole()));
  }

  public List<AccountDto> findAccounts(ClientIdDto clientIdDto, RequestDetails details) {
    int requestId = generateRequestId();
    ClientIdDto unmasked = unmask(clientIdDto);
    auditService.audit(requestId, details.getUserName(), REQUEST, details.getUri(), unmasked);

    List<AccountDto> accounts = sendRequestForList("/client/account", unmask(clientIdDto),
        new ParameterizedTypeReference<>() {});

    auditService.audit(requestId, details.getUserName(), RESPONSE, details.getUri(),
        mask(accounts, getAccess(AUDITOR)));

    return mask(accounts, getAccess(details.getUserRole()));
  }

  public CurrentBalanceDto getBalance(AccountNumberDto accountNumberDto, RequestDetails details) {
    int requestId = generateRequestId();
    AccountNumberDto unmasked = unmask(accountNumberDto);
    auditService.audit(requestId, details.getUserName(), REQUEST, details.getUri(), unmasked);

    CurrentBalanceDto balance = sendRequestForObject("/client/account/balance",
        unmask(accountNumberDto), CurrentBalanceDto.class);

    auditService.audit(requestId, details.getUserName(), RESPONSE, details.getUri(),
        mask(balance, getAccess(AUDITOR)));

    return mask(balance, getAccess(details.getUserRole()));
  }

  public List<OperationDto> findOperations(OperationSearchDto operationSearchDto,
      RequestDetails details
  ) {
    int requestId = generateRequestId();
    OperationSearchDto unmasked = unmask(operationSearchDto);
    auditService.audit(requestId, details.getUserName(), REQUEST, details.getUri(), unmasked);

    List<OperationDto> operations = sendRequestForList("/client/account/operation",
        unmask(operationSearchDto), new ParameterizedTypeReference<>() {});

    auditService.audit(requestId, details.getUserName(), RESPONSE, details.getUri(),
        mask(operations, getAccess(AUDITOR)));

    return mask(operations, getAccess(details.getUserRole()));
  }

  public ContactDto saveContact(ContactDto contactDto, RequestDetails details) {
    int requestId = generateRequestId();
    ContactDto unmasked = unmask(contactDto);
    auditService.audit(requestId, details.getUserName(), REQUEST, details.getUri(), unmasked);

    ContactDto contact = sendRequestForObject("/client/contact/save", unmask(contactDto),
        ContactDto.class);

    auditService.audit(requestId, details.getUserName(), RESPONSE, details.getUri(),
        mask(contact, getAccess(AUDITOR)));

    return mask(contact, getAccess(details.getUserRole()));
  }

  public ClientLevelDto getLevel(ClientIdDto clientIdDto, RequestDetails details) {
    int requestId = generateRequestId();
    ClientIdDto unmasked = unmask(clientIdDto);
    auditService.audit(requestId, details.getUserName(), REQUEST, details.getUri(), unmasked);

    ClientLevelDto clientLevel = sendRequestForObject("/client/level", unmask(clientIdDto),
        ClientLevelDto.class);

    auditService.audit(requestId, details.getUserName(), RESPONSE, details.getUri(),
        mask(clientLevel, getAccess(AUDITOR)));

    return mask(clientLevel, getAccess(details.getUserRole()));
  }

  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto, RequestDetails details) {
    int requestId = generateRequestId();
    AccountNumberDto unmasked = unmask(accountNumberDto);
    auditService.audit(requestId, details.getUserName(), REQUEST, details.getUri(), unmasked);

    LoanPaymentDto loanPayment = sendRequestForObject("/client/account/loan-payment",
        unmask(accountNumberDto), LoanPaymentDto.class);

    auditService.audit(requestId, details.getUserName(), RESPONSE, details.getUri(),
        mask(loanPayment, getAccess(AUDITOR)));

    return mask(loanPayment, getAccess(details.getUserRole()));
  }

  private int generateRequestId() {
    return RandomUtils.nextInt(ID_LOWER_BOUND, ID_UPPER_BOUND);
  }

  public <T> T mask(T originalDto, List<AccessDto> access) {
    return maskingService.getMasked(originalDto, access);
  }

  public <T> T unmask(T maskedDto) {
    return maskingService.getUnmasked(maskedDto);
  }

  private <T, R> List<R> sendRequestForList(String requestUri, T requestBody,
      ParameterizedTypeReference<List<R>> responseType
  ) {
    Mono<List<R>> mono = getResponseSpec(requestUri, requestBody)
        .bodyToMono(responseType);
    return getSyncResponse(mono);
  }

  private <T, R> R sendRequestForObject(String requestUri, T requestBody, Class<R> responseType) {
    Mono<R> mono = getResponseSpec(requestUri, requestBody)
        .bodyToMono(responseType);
    return getSyncResponse(mono);
  }

  private <T> ResponseSpec getResponseSpec(String requestUri, T requestBody) {
    return webClient.post()
        .uri(requestUri)
        .bodyValue(requestBody)
        .retrieve()
        .onStatus(HttpStatus::isError,
            response -> response.bodyToMono(ErrorResponse.class).map(
                error -> new ResponseStatusException(error.getStatus(), error.getMessage())
            )
        );
  }

  private <R> R getSyncResponse(Mono<R> mono) {
    return mono.timeout(Duration.ofSeconds(TIMEOUT_IN_SECONDS))
        .onErrorMap(WebClientRequestException.class,
            ex -> new ResponseStatusException(SERVICE_UNAVAILABLE, "Server DM: connection refused"))
        .onErrorMap(TimeoutException.class,
            ex -> new ResponseStatusException(SERVICE_UNAVAILABLE, "Server DM: response timeout"))
        .block();
  }

  private static final class ErrorResponse {

    private final HttpStatus status;
    private final String message;

    private ErrorResponse(
        @JsonProperty("status") int status,
        @JsonProperty("message") String message
    ) {
      this.status = HttpStatus.valueOf(status);
      this.message = message;
    }

    private HttpStatus getStatus() {
      return status;
    }

    private String getMessage() {
      return message;
    }

  }

}
