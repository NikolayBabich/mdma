package ru.filit.mdma.dms.model;

public enum EventType {
  REQUEST(">"),
  RESPONSE("<");

  private final String direction;

  EventType(String direction) {
    this.direction = direction;
  }

  public String getDirection() {
    return direction;
  }

}
