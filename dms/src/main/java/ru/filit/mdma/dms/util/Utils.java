package ru.filit.mdma.dms.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.PropertyUtils;
import ru.filit.mdma.dms.web.dto.AccessDto;

public final class Utils {

  private Utils() {
    throw new UnsupportedOperationException("Utility class is not instantiable");
  }

  @SuppressWarnings("unchecked")
  public static <T> T getMasked(T original, List<AccessDto> accesses) {
    if (original instanceof List) {
      List<?> items = (List<?>) original;
      if (items.size() > 0) {
        Set<String> allowedProperties = getAllowedProperties(accesses, getEntityName(items.get(0)));
        return (T) items.stream()
            .map(item -> getMaskedItem(item, allowedProperties))
            .collect(Collectors.toList());
      }
      return original;
    } else {
      Set<String> allowedProperties = getAllowedProperties(accesses, getEntityName(original));
      return getMaskedItem(original, allowedProperties);
    }
  }

  private static String getEntityName(Object bean) {
    return bean.getClass().getSimpleName().toLowerCase().replace("dto", "");
  }

  private static Set<String> getAllowedProperties(List<AccessDto> accesses, String entityName) {
    return accesses.stream()
        .filter(access -> entityName.equalsIgnoreCase(access.getEntity()))
        .map(AccessDto::getProperty)
        .collect(Collectors.toSet());
  }

  private static <T> T getMaskedItem(T bean, Set<String> allowedProperties) {
    try {
      PropertyUtils.describe(bean)
          .forEach((propertyName, propertyValue) -> {
            try {
              if (!allowedProperties.contains(propertyName)) {
                PropertyUtils.setProperty(bean, propertyName, "****");
              }
            } catch (Exception e) {
              throw new IllegalStateException(e);
            }
          });
      return bean;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

}
