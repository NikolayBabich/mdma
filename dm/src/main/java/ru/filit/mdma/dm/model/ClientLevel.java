package ru.filit.mdma.dm.model;

import com.google.common.base.MoreObjects;

/**
 * Уровни клиентов Банка.
 */
public enum ClientLevel {

  LOW("Low"),
  MIDDLE("Middle"),
  SILVER("Silver"),
  GOLD("Gold");

  private final String value;

  ClientLevel(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("value", value()).toString();
  }

  public String value() {
    return value;
  }

}
