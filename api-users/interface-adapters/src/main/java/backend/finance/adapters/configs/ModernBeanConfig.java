package backend.finance.adapters.configs;

import backend.finance.application.mappers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RegisterBeanRegistrar.class) // Maioria dos beans registrados pelo BeanRegistrar
public class ModernBeanConfig {

    @Bean
    public CustomerMapper customerMapper(UserMapper userMapper) {
        return new CustomerMapperImpl(userMapper);
    }

    @Bean
    public UserMapper userMapper(RoleMapper roleMapper) {
        return new UserMapperImpl(roleMapper);
    }

    @Bean
    public RoleMapper roleMapper() {
        return new RoleMapperImpl();
    }
}
