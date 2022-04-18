package ru.filit.mdma.dms.service;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.filit.mdma.dms.model.EventType;
import ru.filit.mdma.dms.util.MaskingService;
import ru.filit.mdma.dms.model.RequestDetails;
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

  private static final String ACCESS_VERSION = "3";
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
    return sendRequestForList("/access", new AccessRequestDto(role, ACCESS_VERSION),
        AccessDto.class);
  }

  public List<ClientDto> findClients(ClientSearchDto clientSearchDto, RequestDetails details) {
    return processForList("/client", clientSearchDto, ClientDto.class, details);
  }

  public List<ContactDto> findContacts(ClientIdDto clientIdDto, RequestDetails details) {
    return processForList("/client/contact", clientIdDto, ContactDto.class, details);
  }

  public List<AccountDto> findAccounts(ClientIdDto clientIdDto, RequestDetails details) {
    return processForList("/client/account", clientIdDto, AccountDto.class, details);
  }

  public CurrentBalanceDto getBalance(AccountNumberDto accountNumberDto, RequestDetails details) {
    return processForObject("/client/account/balance", accountNumberDto, CurrentBalanceDto.class,
        details);
  }

  public List<OperationDto> findOperations(OperationSearchDto operationSearchDto,
      RequestDetails details
  ) {
    return processForList("/client/account/operation", operationSearchDto, OperationDto.class,
        details);
  }

  public ContactDto saveContact(ContactDto contactDto, RequestDetails details) {
    return processForObject("/client/contact/save", contactDto, ContactDto.class, details);
  }

  public ClientLevelDto getLevel(ClientIdDto clientIdDto, RequestDetails details) {
    return processForObject("/client/level", clientIdDto, ClientLevelDto.class, details);
  }

  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto, RequestDetails details) {
    return processForObject("/client/account/loan-payment", accountNumberDto, LoanPaymentDto.class,
        details);
  }

  private <T, R> List<R> processForList(String uri, T requestDto, Class<R> responseClass,
      RequestDetails details
  ) {
    int requestId = generateRequestId();
    T unmasked = unmask(requestDto);
    auditRequest(requestId, unmasked, details);

    List<R> responseDto = sendRequestForList(uri, unmasked, responseClass);
    auditResponse(requestId, responseDto, details);

    return mask(responseDto, getAccess(details.getUserRole()));
  }

  private <T, R> R processForObject(String uri, T requestDto, Class<R> responseClass,
      RequestDetails details
  ) {
    int requestId = generateRequestId();
    T unmasked = unmask(requestDto);
    auditRequest(requestId, unmasked, details);

    R responseDto = sendRequestForObject(uri, unmasked, responseClass);
    auditResponse(requestId, responseDto, details);

    return mask(responseDto, getAccess(details.getUserRole()));
  }

  private int generateRequestId() {
    return RandomUtils.nextInt(ID_LOWER_BOUND, ID_UPPER_BOUND);
  }

  private <T> void auditRequest(int requestId, T dto, RequestDetails details) {
    auditService.audit(requestId, details.getUserName(), EventType.REQUEST, details.getUri(), dto);
  }

  private <T> void auditResponse(int requestId, T dto, RequestDetails details) {
    auditService.audit(requestId, details.getUserName(), EventType.RESPONSE, details.getUri(),
        mask(dto, getAccess(AUDITOR)));
  }

  private <T> T mask(T originalDto, List<AccessDto> access) {
    return maskingService.getMasked(originalDto, access);
  }

  private <T> T unmask(T maskedDto) {
    return maskingService.getUnmasked(maskedDto);
  }

  private <T, R> List<R> sendRequestForList(String requestUri, T requestBody,
      Class<R> responseClass
  ) {
    ParameterizedTypeReference<List<R>> responseTypeRef = ParameterizedTypeReference.forType(
        ResolvableType.forClassWithGenerics(List.class, responseClass).getType());
    Mono<List<R>> mono = getResponseSpec(requestUri, requestBody).bodyToMono(responseTypeRef);
    return getSyncResponse(mono);
  }

  private <T, R> R sendRequestForObject(String requestUri, T requestBody, Class<R> responseClass) {
    Mono<R> mono = getResponseSpec(requestUri, requestBody).bodyToMono(responseClass);
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
