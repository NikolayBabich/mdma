package ru.filit.mdma.dm.model;

import com.google.common.base.MoreObjects;

/**
 * Состояние счета.
 */
public enum AccountStatus {

  INACTIVE("Неактивный"),
  ACTIVE("Активный"),
  LOCKED("Заблокированный"),
  CLOSED("Закрытый");

  private final String value;

  AccountStatus(String value) {
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
