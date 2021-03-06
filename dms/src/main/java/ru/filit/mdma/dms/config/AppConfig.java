package ru.filit.mdma.dms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

  private static final String URL_FORMAT = "http://%s:%s/%s";
  private static final String API_BASE_URL = "dm";
  private static final String USER_AGENT = "DMS/1.0";

  private final String serverDmHost;
  private final String serverDmPort;

  public AppConfig(
      @Value("${system.element.dm.host}") String serverDmHost,
      @Value("${system.element.dm.port}") String serverDmPort
  ) {
    this.serverDmHost = serverDmHost;
    this.serverDmPort = serverDmPort;
  }

  @Bean
  public WebClient webClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder
        .baseUrl(String.format(URL_FORMAT, serverDmHost, serverDmPort, API_BASE_URL))
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
        .build();
  }

}
