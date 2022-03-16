package ru.filit.mdma.dm.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class DateConverter {

  private static final long MILLIS_IN_SECOND = 1_000L;

  private DateConverter() {
    throw new UnsupportedOperationException("Utility class is not instantiable");
  }

  public static String getDateTimeText(Long seconds) {
    if (seconds == null) {
      return null;
    }
    Instant instance = Instant.ofEpochMilli(seconds * MILLIS_IN_SECOND);
    LocalDateTime ldt = LocalDateTime.ofInstant(instance, ZoneOffset.UTC);
    return ldt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  public static String getDateText(Long seconds) {
    if (seconds == null) {
      return null;
    }
    Instant instance = Instant.ofEpochMilli(seconds * MILLIS_IN_SECOND);
    LocalDate ld = LocalDate.ofInstant(instance, ZoneOffset.UTC);
    return ld.format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public static Long getSecondsOfDateTime(String dateTimeText) {
    if (dateTimeText == null) {
      return null;
    }
    LocalDateTime ldt = LocalDateTime.parse(dateTimeText, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    Instant instance = ldt.atZone(ZoneOffset.UTC).toInstant();
    return instance.toEpochMilli() / MILLIS_IN_SECOND;
  }

  public static Long getSecondsOfDate(String dateText) {
    if (dateText == null) {
      return null;
    }
    LocalDateTime ldt = LocalDate.parse(dateText, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
    Instant instance = ldt.atZone(ZoneOffset.UTC).toInstant();
    return instance.toEpochMilli() / MILLIS_IN_SECOND;
  }

}
