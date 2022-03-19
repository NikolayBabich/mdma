package ru.filit.mdma.crm.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

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

  public List<ClientDto> findClients(ClientSearchDto clientSearchDto) {
    return sendRequestForList("/", clientSearchDto, new ParameterizedTypeReference<>() {});
  }

  public ClientDto getClient(ClientIdDto clientIdDto) {
    String clientId = clientIdDto.getId();
    List<ClientDto> clients = findClients(new ClientSearchDto().id(clientId));

    if (clients.isEmpty()) {
      throw new ResponseStatusException(BAD_REQUEST, "No Client id:" + clientId);
    } else if (clients.size() > 1) {
      throw new IllegalStateException("Search by clientId:" + clientId + " returned multi-result");
    }
    ClientDto client = clients.get(0);

    List<ContactDto> contacts = sendRequestForList("/contact", clientIdDto,
        new ParameterizedTypeReference<>() {});
    contacts.forEach(client::addContactsItem);
    List<AccountDto> accounts = sendRequestForList("/account", clientIdDto,
        new ParameterizedTypeReference<>() {});
    accounts.forEach(client::addAccountsItem);

    accounts.forEach(account -> account.balanceAmount(sendRequestForObject("/account/balance",
        accountNumberDto(account), CurrentBalanceDto.class).getBalanceAmount()));

    return client;
  }

  private AccountNumberDto accountNumberDto(AccountDto accountDto) {
    return new AccountNumberDto().accountNumber(accountDto.getNumber());
  }

  public List<OperationDto> getLastOperations(AccountNumberDto accountNumberDto) {
    OperationSearchDto search = new OperationSearchDto()
        .accountNumber(accountNumberDto.getAccountNumber())
        .quantity(String.valueOf(LAST_OPERATIONS_QUANTITY));
    return sendRequestForList("/account/operation", search, new ParameterizedTypeReference<>() {
    });
  }

  public ContactDto saveContact(ContactDto contactDto) {
    return sendRequestForObject("/contact/save", contactDto, ContactDto.class);
  }

  public ClientLevelDto getClientLevel(ClientIdDto clientIdDto) {
    return sendRequestForObject("/level", clientIdDto, ClientLevelDto.class);
  }

  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto) {
    return sendRequestForObject("/account/loan-payment", accountNumberDto, LoanPaymentDto.class);
  }

  private <T, R> List<R> sendRequestForList(String requestUri, T requestBody,
      ParameterizedTypeReference<List<R>> responseType
  ) {
    Mono<List<R>> mono = getResponseSpec(requestUri, requestBody).bodyToMono(responseType);
    return getResponse(mono);
  }

  private <T, R> R sendRequestForObject(String requestUri, T requestBody, Class<R> responseType) {
    Mono<R> mono = getResponseSpec(requestUri, requestBody).bodyToMono(responseType);
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

    private final long timestamp;
    private final String message;

    private ErrorResponseHelper(long timestamp, String message) {
      this.timestamp = timestamp;
      this.message = message;
    }

    public long getTimestamp() {
      return timestamp;
    }

    private String getMessage() {
      return message;
    }

  }

}
