package ru.filit.mdma.dms.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dms.cache.CacheManager;
import ru.filit.mdma.dms.web.dto.AccessDto;

@Service
public class MaskingService {

  private final CacheManager cacheManager;

  public MaskingService(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @SuppressWarnings("unchecked")
  public <T> T getMasked(T original, List<AccessDto> accesses) {
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

  public <T> T getUnmasked(T sourceDto) {
    try {
      PropertyUtils.describe(sourceDto)
          .forEach((propertyName, propertyValue) -> {
            if (propertyValue instanceof String) {
              try {
                if (((String) propertyValue).matches("#[0-9A-F]{32}#")) {
                  PropertyUtils.setProperty(sourceDto, propertyName, getActualValue(propertyValue));
                }
              } catch (Exception e) {
                throw new IllegalStateException(e);
              }
            }
          });
      return sourceDto;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private String getEntityName(Object bean) {
    return bean.getClass().getSimpleName().toLowerCase().replace("dto", "");
  }

  private static Set<String> getAllowedProperties(List<AccessDto> accesses, String entityName) {
    return accesses.stream()
        .filter(access -> entityName.equalsIgnoreCase(access.getEntity()))
        .map(AccessDto::getProperty)
        .collect(Collectors.toSet());
  }

  private <T> T getMaskedItem(T bean, Set<String> allowedProperties) {
    try {
      PropertyUtils.describe(bean)
          .forEach((propertyName, propertyValue) -> {
            if (propertyValue instanceof String) {
              try {
                if (!allowedProperties.contains(propertyName)) {
                  PropertyUtils.setProperty(bean, propertyName, getTokenized(propertyValue));
                }
              } catch (Exception e) {
                throw new IllegalStateException(e);
              }
            }
          });
      return bean;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private String getTokenized(Object propertyValue) {
    return cacheManager.getToken((String) propertyValue);
  }

  private String getActualValue(Object propertyValue) {
    return cacheManager.getActualValue((String) propertyValue);
  }

}
