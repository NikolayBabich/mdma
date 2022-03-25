package ru.filit.mdma.dms.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Права доступа к полям сущностей.
 */
public class AccessDto {

  private final String entity;
  private final String property;

  public AccessDto(
      @JsonProperty("entity") String entity,
      @JsonProperty("property") String property
  ) {
    this.entity = entity;
    this.property = property;
  }

  public String getEntity() {
    return entity;
  }

  public String getProperty() {
    return property;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccessDto accessDto = (AccessDto) o;
    return Objects.equals(entity, accessDto.entity) && Objects.equals(property, accessDto.property);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entity, property);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("AccessDto{");
    sb.append("entity='").append(entity).append('\'');
    sb.append(", property='").append(property).append('\'');
    sb.append('}');
    return sb.toString();
  }

}
