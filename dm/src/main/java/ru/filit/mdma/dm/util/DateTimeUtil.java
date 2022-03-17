package ru.filit.mdma.dm.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public final class DateTimeUtil {

  private static final long MILLIS_IN_SECOND = 1_000L;
  private static final long SECONDS_IN_DAY = 60 * 60 * 24;
  public static final int MAX_HOUR = 23;
  public static final int MAX_MINUTE = 59;
  public static final int MAX_SECOND = 59;

  private DateTimeUtil() {
    throw new UnsupportedOperationException("Utility class is not instantiable");
  }

  public static String getDateTimeText(Long seconds) {
    if (seconds == null) {
      return null;
    }
    LocalDateTime ldt = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);
    return ldt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  public static String getDateText(Long seconds) {
    if (seconds == null) {
      return null;
    }
    LocalDateTime ldt = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);
    return ldt.format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public static Long getSecondsOfDateTime(String dateTimeText) {
    if (dateTimeText == null) {
      return null;
    }
    LocalDateTime ldt = LocalDateTime.parse(dateTimeText, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    return ldt.toEpochSecond(ZoneOffset.UTC);
  }

  public static Long getSecondsOfDate(String dateText) {
    if (dateText == null) {
      return null;
    }
    LocalDateTime ldt = LocalDate.parse(dateText, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
    return ldt.toEpochSecond(ZoneOffset.UTC);
  }

  public static Long getBeginOfMonth(Long seconds) {
    LocalDateTime beginOfMonth = LocalDate.ofInstant(Instant.ofEpochSecond(seconds), ZoneOffset.UTC)
        .atStartOfDay().withDayOfMonth(1);
    return beginOfMonth.toEpochSecond(ZoneOffset.UTC);
  }

  public static List<Long> getTimestampOfLastDays(int daysLimit) {
    long endOfCurrentDay = getEndOfCurrentDay();
    return LongStream.iterate(0, n -> n + SECONDS_IN_DAY)
        .limit(daysLimit)
        .map(n -> endOfCurrentDay - n)
        .boxed()
        .collect(Collectors.toList());
  }

  public static Long getEndOfCurrentDay() {
    return getEndOfDay(getCurrentDate());
  }

  public static Long getEndOfDay(LocalDate ld) {
    return ld.atTime(MAX_HOUR, MAX_MINUTE, MAX_SECOND).toEpochSecond(ZoneOffset.UTC);
  }

  private static LocalDate getCurrentDate() {
    return LocalDate.now();
    //return LocalDate.of(2021, 12, 13);
  }

}
