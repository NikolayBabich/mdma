package ru.filit.mdma.dms.service;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.filit.mdma.dms.util.MaskingService;
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

  private final WebClient webClient;
  private final MaskingService maskingService;

  public ClientService(WebClient webClient, MaskingService maskingService) {
    this.webClient = webClient;
    this.maskingService = maskingService;
  }

  public List<AccessDto> getAccess(String role) {
    return sendRequestForList("/access", new AccessRequestDto(role, VERSION),
        new ParameterizedTypeReference<>() {});
  }

  public List<ClientDto> findClients(ClientSearchDto clientSearchDto, String role, String name) {

    List<ClientDto> clients = sendRequestForList("/client", unmask(clientSearchDto),
        new ParameterizedTypeReference<>() {});
    return mask(clients, getAccess(role));
  }

  public List<ContactDto> findContacts(ClientIdDto clientIdDto, String role, String name) {
    List<ContactDto> contacts = sendRequestForList("/client/contact", unmask(clientIdDto),
        new ParameterizedTypeReference<>() {});
    return mask(contacts, getAccess(role));
  }

  public List<AccountDto> findAccounts(ClientIdDto clientIdDto, String role, String name) {
    List<AccountDto> accounts = sendRequestForList("/client/account", unmask(clientIdDto),
        new ParameterizedTypeReference<>() {});
    return mask(accounts, getAccess(role));
  }

  public CurrentBalanceDto getBalance(AccountNumberDto accountNumberDto, String role, String name) {
    CurrentBalanceDto balance = sendRequestForObject("/client/account/balance",
        unmask(accountNumberDto), CurrentBalanceDto.class);
    return mask(balance, getAccess(role));
  }

  public List<OperationDto> findOperations(OperationSearchDto operationSearchDto,
      String role, String name
  ) {
    List<OperationDto> operations = sendRequestForList("/client/account/operation",
        unmask(operationSearchDto), new ParameterizedTypeReference<>() {});
    return mask(operations, getAccess(role));
  }

  public ContactDto saveContact(ContactDto contactDto, String role, String name) {
    ContactDto contact = sendRequestForObject("/client/contact/save", unmask(contactDto),
        ContactDto.class);
    return mask(contact, getAccess(role));
  }

  public ClientLevelDto getLevel(ClientIdDto clientIdDto, String role, String name) {
    ClientLevelDto clientLevel = sendRequestForObject("/client/level", unmask(clientIdDto),
        ClientLevelDto.class);
    return mask(clientLevel, getAccess(role));
  }

  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto,
      String role, String name
  ) {
    LoanPaymentDto loanPayment = sendRequestForObject("/client/account/loan-payment",
        unmask(accountNumberDto), LoanPaymentDto.class);
    return mask(loanPayment, getAccess(role));
  }

  public <T> T mask(T original, List<AccessDto> access) {
    return maskingService.getMasked(original, access);
  }

  private <T> T unmask(T dto) {
    return maskingService.getUnmasked(dto);
  }

  private <T, R> List<R> sendRequestForList(String requestUri, T requestBody,
      ParameterizedTypeReference<List<R>> responseType
  ) {
    Mono<List<R>> mono = getResponseSpec(requestUri, requestBody)
        .bodyToMono(responseType);
    return getResponse(mono);
  }

  private <T, R> R sendRequestForObject(String requestUri, T requestBody, Class<R> responseType) {
    Mono<R> mono = getResponseSpec(requestUri, requestBody)
        .bodyToMono(responseType);
    return getResponse(mono);
  }

  private <T> ResponseSpec getResponseSpec(String requestUri, T requestBody) {
    return webClient.post()
        .uri(requestUri)
        .bodyValue(requestBody)
        .retrieve()
        .onStatus(HttpStatus::isError,
            response -> response.bodyToMono(ErrorResponseHelper.class).map(
                error -> new ResponseStatusException(error.getStatus(), error.getMessage())
            )
        );
  }

  private <R> R getResponse(Mono<R> mono) {
    return mono.timeout(Duration.ofSeconds(TIMEOUT_IN_SECONDS))
        .onErrorMap(WebClientRequestException.class,
            ex -> new ResponseStatusException(SERVICE_UNAVAILABLE, "Server DM: connection refused"))
        .onErrorMap(TimeoutException.class,
            ex -> new ResponseStatusException(SERVICE_UNAVAILABLE, "Server DM: response timeout"))
        .block();
  }

  private static class ErrorResponseHelper {

    private final HttpStatus status;
    private final String message;

    public ErrorResponseHelper(
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
