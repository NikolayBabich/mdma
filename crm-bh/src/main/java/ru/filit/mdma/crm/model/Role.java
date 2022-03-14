package ru.filit.mdma.crm.model;

import com.google.common.base.MoreObjects;

/**
 * Основная роль пользователя.
 */
public enum Role {

  MANAGER("Офис-менеджер банка"),
  SUPERVISOR("Старший смены"),
  AUDITOR("Служебная роль");

  private final String value;

  Role(String value) {
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
