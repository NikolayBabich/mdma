package ru.filit.mdma.dms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dms.model.EventType;

@Service
public class AuditService {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

  private final ObjectMapper objectMapper;

  public AuditService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
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
    System.out.println(message);
  }

}
