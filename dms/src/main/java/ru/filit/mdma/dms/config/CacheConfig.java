package ru.filit.mdma.dms.config;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

  public static final String TOKEN_CACHE = "TokenCache";

  private static final String TEMP_DIR_PROPERTY = "java.io.tmpdir";

  @Bean
  public IgniteSpringBean ignite() {
    IgniteSpringBean bean = new IgniteSpringBean();
    bean.setConfiguration(igniteConfiguration());
    return bean;
  }

  @Bean
  public IgniteConfiguration igniteConfiguration() {
    return new IgniteConfiguration()
        .setClientMode(false)
        .setDataStorageConfiguration(dataStorageConfiguration())
        .setCacheConfiguration(cacheConfiguration())
        .setWorkDirectory(System.getProperty(TEMP_DIR_PROPERTY));
  }

  private DataStorageConfiguration dataStorageConfiguration() {
    return new DataStorageConfiguration()
        .setDefaultDataRegionConfiguration(dataRegionConfiguration());
  }

  private DataRegionConfiguration dataRegionConfiguration() {
    return new DataRegionConfiguration()
        .setName("dataDefault")
        .setInitialSize(50L * 1024 * 1024)
        .setMaxSize(150L * 1024 * 1024);
  }

  private CacheConfiguration<String, String> cacheConfiguration() {
    return new CacheConfiguration<String, String>()
        .setName(TOKEN_CACHE)
        .setCacheMode(CacheMode.REPLICATED)
        .setAtomicityMode(CacheAtomicityMode.ATOMIC)
        .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.THIRTY_MINUTES))
        .setEagerTtl(true);
  }

}
