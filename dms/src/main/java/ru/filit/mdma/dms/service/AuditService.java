package ru.filit.mdma.dms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dms.model.EventType;

@Service
public class AuditService {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
  private static final String TOPIC_NAME = "dm-audit";

  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;

  public AuditService(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
    this.objectMapper = objectMapper;
    this.kafkaTemplate = kafkaTemplate;
  }

  public void audit(int requestId, String name, EventType eventType, String uri, Object payload) {
    try {
      sendMessage(String.format(
              "%s %8d %-15s %s%-34s %s",
              LocalDateTime.now().format(DATE_TIME_FORMATTER),
              requestId,
              name,
              eventType.getDirection(),
              uri,
              objectMapper.writeValueAsString(payload)
          )
      );
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private void sendMessage(String message) {
    kafkaTemplate.send(TOPIC_NAME, message);
  }

}
