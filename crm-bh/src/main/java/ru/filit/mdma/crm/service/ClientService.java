package ru.filit.mdma.crm.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
import ru.filit.mdma.crm.web.dto.AccountDto;
import ru.filit.mdma.crm.web.dto.AccountNumberDto;
import ru.filit.mdma.crm.web.dto.ClientDto;
import ru.filit.mdma.crm.web.dto.ClientIdDto;
import ru.filit.mdma.crm.web.dto.ClientLevelDto;
import ru.filit.mdma.crm.web.dto.ClientSearchDto;
import ru.filit.mdma.crm.web.dto.ContactDto;
import ru.filit.mdma.crm.web.dto.CurrentBalanceDto;
import ru.filit.mdma.crm.web.dto.LoanPaymentDto;
import ru.filit.mdma.crm.web.dto.OperationDto;
import ru.filit.mdma.crm.web.dto.OperationSearchDto;

@Service
public class ClientService {

  private static final int TIMEOUT_IN_SECONDS = 5;
  private static final int LAST_OPERATIONS_QUANTITY = 3;

  private final WebClient webClient;

  public ClientService(WebClient webClient) {
    this.webClient = webClient;
  }

  public List<ClientDto> findClients(ClientSearchDto clientSearchDto, String role, String name) {
    return sendRequestForList("/", clientSearchDto,
        new ParameterizedTypeReference<>() {}, role, name);
  }

  public ClientDto getClient(ClientIdDto clientIdDto, String role, String name) {
    String clientId = clientIdDto.getId();
    List<ClientDto> clients = findClients(new ClientSearchDto().id(clientId), role, name);

    if (clients.isEmpty()) {
      throw new ResponseStatusException(BAD_REQUEST, "No Client id:" + clientId);
    } else if (clients.size() > 1) {
      throw new IllegalStateException("Search by clientId:" + clientId + " returned multi-result");
    }
    ClientDto client = clients.get(0);

    List<ContactDto> contacts = sendRequestForList("/contact", clientIdDto,
        new ParameterizedTypeReference<>() {}, role, name);
    contacts.forEach(client::addContactsItem);
    List<AccountDto> accounts = sendRequestForList("/account", clientIdDto,
        new ParameterizedTypeReference<>() {}, role, name);
    accounts.forEach(client::addAccountsItem);

    accounts.forEach(account -> {
      CurrentBalanceDto balance = sendRequestForObject(
          "/account/balance", accountNumberDto(account), CurrentBalanceDto.class, role, name);
      account.balanceAmount(balance.getBalanceAmount());
    });

    return client;
  }

  private AccountNumberDto accountNumberDto(AccountDto accountDto) {
    return new AccountNumberDto().accountNumber(accountDto.getNumber());
  }

  public List<OperationDto> getLastOperations(AccountNumberDto accountNumberDto,
      String role, String name
  ) {
    OperationSearchDto search = new OperationSearchDto()
        .accountNumber(accountNumberDto.getAccountNumber())
        .quantity(String.valueOf(LAST_OPERATIONS_QUANTITY));
    return sendRequestForList("/account/operation", search,
        new ParameterizedTypeReference<>() {}, role, name);
  }

  public ContactDto saveContact(ContactDto contactDto, String role, String name) {
    return sendRequestForObject("/contact/save", contactDto, ContactDto.class, role, name);
  }

  public ClientLevelDto getClientLevel(ClientIdDto clientIdDto, String role, String name) {
    return sendRequestForObject("/level", clientIdDto, ClientLevelDto.class, role, name);
  }

  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto,
      String role, String name
  ) {
    return sendRequestForObject(
        "/account/loan-payment", accountNumberDto, LoanPaymentDto.class, role, name);
  }

  private <T, R> List<R> sendRequestForList(String requestUri, T requestBody,
      ParameterizedTypeReference<List<R>> responseType, String role, String name
  ) {
    Mono<List<R>> mono = getResponseSpec(requestUri, requestBody, role, name)
        .bodyToMono(responseType);
    return getResponse(mono);
  }

  private <T, R> R sendRequestForObject(String requestUri, T requestBody, Class<R> responseType,
      String role, String name
  ) {
    Mono<R> mono = getResponseSpec(requestUri, requestBody, role, name)
        .bodyToMono(responseType);
    return getResponse(mono);
  }

  private <T> ResponseSpec getResponseSpec(String requestUri, T requestBody,
      String role, String name
  ) {
    return webClient.post()
        .uri(requestUri)
        .header("CRM-User-Role", role)
        .header("CRM-User-Name", name)
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
