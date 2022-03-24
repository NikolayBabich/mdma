package ru.filit.mdma.dms.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
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

  private static final int TIMEOUT_IN_SECONDS = 5;

  private final WebClient webClient;

  public ClientService(WebClient webClient) {
    this.webClient = webClient;
  }

  public List<ClientDto> findClients(ClientSearchDto clientSearchDto, String role, String name) {
    List<ClientDto> clients = sendRequestForList("/", clientSearchDto,
        new ParameterizedTypeReference<>() {});
    return clients;
  }

  public List<ContactDto> findContacts(ClientIdDto clientIdDto, String role, String name) {
    List<ContactDto> contacts = sendRequestForList("/contact", clientIdDto,
        new ParameterizedTypeReference<>() {});
    return contacts;
  }

  public List<AccountDto> findAccounts(ClientIdDto clientIdDto, String role, String name) {
    List<AccountDto> accounts = sendRequestForList("/account", clientIdDto,
        new ParameterizedTypeReference<>() {});
    return accounts;
  }

  public CurrentBalanceDto getBalance(AccountNumberDto accountNumberDto, String role, String name) {
    CurrentBalanceDto balance = sendRequestForObject("/account/balance", accountNumberDto,
        CurrentBalanceDto.class);
    return balance;
  }

  public List<OperationDto> findOperations(OperationSearchDto operationSearchDto,
      String role, String name
  ) {
    List<OperationDto> operations = sendRequestForList("/account/operation", operationSearchDto,
        new ParameterizedTypeReference<>() {});
    return operations;
  }

  public ContactDto saveContact(ContactDto contactDto, String role, String name) {
    ContactDto contact = sendRequestForObject("/contact/save", contactDto, ContactDto.class);
    return contact;
  }

  public ClientLevelDto getLevel(ClientIdDto clientIdDto, String role, String name) {
    ClientLevelDto clientLevel = sendRequestForObject("/level", clientIdDto, ClientLevelDto.class);
    return clientLevel;
  }

  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto,
      String role, String name
  ) {
    LoanPaymentDto loanPayment = sendRequestForObject("/level", accountNumberDto,
        LoanPaymentDto.class);
    return loanPayment;
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
        .onStatus(BAD_REQUEST::equals,
            response -> response.bodyToMono(ErrorResponseHelper.class).map(
                error -> new ResponseStatusException(BAD_REQUEST, error.getMessage())
            ))
        .onStatus(INTERNAL_SERVER_ERROR::equals,
            response -> response.bodyToMono(ErrorResponseHelper.class).map(
                error -> new ResponseStatusException(INTERNAL_SERVER_ERROR, error.getMessage())
            ));
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

    private final String message;

    public ErrorResponseHelper(@JsonProperty("message") String message) {
      this.message = message;
    }

    private String getMessage() {
      return message;
    }

  }

}
