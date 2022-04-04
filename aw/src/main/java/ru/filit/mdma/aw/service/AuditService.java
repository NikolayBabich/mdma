package ru.filit.mdma.aw.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.filit.mdma.aw.Application;
import ru.filit.mdma.aw.config.KafkaConfig;

@Service
public class AuditService {

  private static final String AUDIT_FILENAME = "auditfiles/dm-audit.txt";
  private static final File AUDIT_FILE = FileUtils.getFile(
      new ApplicationHome(Application.class).getDir(), AUDIT_FILENAME);

  @KafkaListener(topics = KafkaConfig.TOPIC_NAME, groupId = KafkaConfig.GROUP_ID)
  public void listen(String message) {
    try {
      FileUtils.writeStringToFile(AUDIT_FILE, message + System.lineSeparator(), UTF_8, true);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
