package ru.filit.mdma.dms.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Запрос прав доступа для роли.
 */
public class AccessRequestDto {

  private final String role;
  private final String version;

  public AccessRequestDto(
      @JsonProperty("role") String role,
      @JsonProperty("version") String version
  ) {
    this.role = role;
    this.version = version;
  }

  public String getRole() {
    return role;
  }

  public String getVersion() {
    return version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccessRequestDto that = (AccessRequestDto) o;
    return Objects.equals(role, that.role) && Objects.equals(version,
        that.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(role, version);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("AccessRequestDto{");
    sb.append("role='").append(role).append('\'');
    sb.append(", version='").append(version).append('\'');
    sb.append('}');
    return sb.toString();
  }

}
