package ru.filit.mdma.dm.model;

import com.google.common.base.MoreObjects;

/**
 * Валюта счета.
 */
public enum CurrencyCode {

  RUR("RUR");

  private final String value;

  CurrencyCode(String value) {
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
