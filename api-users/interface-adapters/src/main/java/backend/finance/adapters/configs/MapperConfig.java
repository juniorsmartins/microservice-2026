package backend.finance.adapters.configs;

import backend.finance.application.mappers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

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
