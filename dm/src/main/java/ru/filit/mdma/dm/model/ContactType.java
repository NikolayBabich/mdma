package ru.filit.mdma.dm.model;

import com.google.common.base.MoreObjects;

/**
 * Тип контакта.
 */
public enum ContactType {

  PHONE("Телефон"),
  EMAIL("E-mail");

  private final String value;

  ContactType(String value) {
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
