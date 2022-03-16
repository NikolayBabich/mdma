package ru.filit.mdma.dm.util;

import java.util.Map;
import java.util.Objects;
import org.apache.commons.beanutils.PropertyUtils;

public final class Utils {

  private Utils() {
    throw new UnsupportedOperationException("Utility class is not instantiable");
  }

  public static boolean hasNonNullProperty(Object object) {
    try {
      return PropertyUtils.describe(object)
          .values().stream()
          .anyMatch(Objects::nonNull);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public static <T> boolean equalsByNonNull(T sample, T target) {
    try {
      Map<String, Object> targetProperties = PropertyUtils.describe(target);
      return PropertyUtils.describe(sample)
          .entrySet().stream()
          .filter(property -> Objects.nonNull(property.getValue()))
          .allMatch(property -> Objects.equals(
              property.getValue(), targetProperties.get(property.getKey()))
          );
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public static String cutEmail(String email) {
    int indexBeforeAt = email.indexOf('@') - 1;
    return email.substring(indexBeforeAt);
  }

}
