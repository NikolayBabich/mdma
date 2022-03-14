package ru.filit.mdma.dm.model;

import com.google.common.base.MoreObjects;

/**
 * Тип операции.
 */
public enum OperationType {

  RECEIPT("Приходная"),
  EXPENSE("Расходная");

  private final String value;

  OperationType(String value) {
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
