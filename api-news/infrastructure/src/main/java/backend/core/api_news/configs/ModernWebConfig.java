package backend.core.api_news.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RegisterBeanRegistrar.class)
public class ModernWebConfig {
}
