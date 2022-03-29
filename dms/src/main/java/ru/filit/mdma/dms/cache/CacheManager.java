package ru.filit.mdma.dms.cache;

import java.util.UUID;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.stereotype.Component;
import ru.filit.mdma.dms.config.CacheConfig;

@Component
public class CacheManager {

  private final Ignite ignite;

  public CacheManager(Ignite ignite) {
    this.ignite = ignite;
  }

  public String getToken(String value) {
    String token = generateToken();
    getTokenCache().put(token, value);
    return token;
  }

  public String getActualValue(String token) {
    String value = getTokenCache().get(token);
    if (value == null) {
      throw new IllegalArgumentException("Invalid or expired token");
    }
    return value;
  }

  private String generateToken() {
    String token;
    do {
      String guid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
      token = String.format("#%s#", guid);
    } while (getTokenCache().containsKey(token));
    return token;
  }

  private IgniteCache<String, String> getTokenCache() {
    return ignite.cache(CacheConfig.TOKEN_CACHE);
  }

}
