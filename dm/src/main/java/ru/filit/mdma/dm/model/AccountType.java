package ru.filit.mdma.dm.model;

import com.google.common.base.MoreObjects;

/**
 * Тип счета.
 */
public enum AccountType {

  PAYMENT("Расчётный"),
  BUDGET("Бюджетный"),
  TRANSIT("Транзитный"),
  OVERDRAFT("Овердрафт");

  private final String value;

  AccountType(String value) {
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
