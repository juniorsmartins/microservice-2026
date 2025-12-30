package backend.finance.adapters.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RegisterBeanRegistrar.class)
public class ModernWebConfig {
}
