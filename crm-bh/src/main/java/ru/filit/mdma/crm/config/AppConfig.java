package ru.filit.mdma.crm.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

  private static final String URL_FORMAT = "http://%s:%s/%s";
  private static final String API_BASE_URL = "dm/client";
  private static final String USER_AGENT = "CRM-BH/1.0";

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

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Bean
  public YAMLMapper yamlMapper() {
    return (YAMLMapper) new YAMLMapper()
        .configure(Feature.WRITE_DOC_START_MARKER, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

}
