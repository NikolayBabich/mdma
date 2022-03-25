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
import ru.filit.mdma.dms.util.Utils;
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

  private static final String VERSION = "2";
  private static final int TIMEOUT_IN_SECONDS = 5;

  private final WebClient webClient;

  public ClientService(WebClient webClient) {
    this.webClient = webClient;
  }

  public List<AccessDto> getAccess(String role) {
    return sendRequestForList("/access", new AccessRequestDto(role, VERSION),
        new ParameterizedTypeReference<>() {});
  }

  public List<ClientDto> findClients(ClientSearchDto clientSearchDto, String role, String name) {
    List<ClientDto> clients = sendRequestForList("/client", clientSearchDto,
        new ParameterizedTypeReference<>() {});
    return maskResponse(clients, getAccess(role));
  }

  public List<ContactDto> findContacts(ClientIdDto clientIdDto, String role, String name) {
    List<ContactDto> contacts = sendRequestForList("/client/contact", clientIdDto,
        new ParameterizedTypeReference<>() {});
    return maskResponse(contacts, getAccess(role));
  }

  public List<AccountDto> findAccounts(ClientIdDto clientIdDto, String role, String name) {
    List<AccountDto> accounts = sendRequestForList("/client/account", clientIdDto,
        new ParameterizedTypeReference<>() {});
    return maskResponse(accounts, getAccess(role));
  }

  public CurrentBalanceDto getBalance(AccountNumberDto accountNumberDto, String role, String name) {
    CurrentBalanceDto balance = sendRequestForObject("/client/account/balance", accountNumberDto,
        CurrentBalanceDto.class);
    return maskResponse(balance, getAccess(role));
  }

  public List<OperationDto> findOperations(OperationSearchDto operationSearchDto,
      String role, String name
  ) {
    List<OperationDto> operations = sendRequestForList("/client/account/operation",
        operationSearchDto, new ParameterizedTypeReference<>() {});
    return maskResponse(operations, getAccess(role));
  }

  public ContactDto saveContact(ContactDto contactDto, String role, String name) {
    ContactDto contact = sendRequestForObject("/client/contact/save", contactDto, ContactDto.class);
    return maskResponse(contact, getAccess(role));
  }

  public ClientLevelDto getLevel(ClientIdDto clientIdDto, String role, String name) {
    ClientLevelDto clientLevel = sendRequestForObject("/client/level", clientIdDto,
        ClientLevelDto.class);
    return maskResponse(clientLevel, getAccess(role));
  }

  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto,
      String role, String name
  ) {
    LoanPaymentDto loanPayment = sendRequestForObject("/client/account/loan-payment",
        accountNumberDto, LoanPaymentDto.class);
    return maskResponse(loanPayment, getAccess(role));
  }

  public <T> T maskResponse(T original, List<AccessDto> access) {
    return Utils.getMasked(original, access);
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
