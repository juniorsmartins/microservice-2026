package backend.core.api_news.configs;

import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class NoCacheTestConfig { // Importar nas classes que precisar desativar o Cache @Import(NoCacheTestConfig.class)

    @Bean
    @Primary
    public CacheManager noOpCacheManager() {
        return new NoOpCacheManager();
    }
}
