package backend.finance.adapters.configs;

import backend.finance.application.mappers.*;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RegisterBeanRegistrar.class) // Maioria dos beans registrados pelo BeanRegistrar
public class WebConfig {

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

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Microsserviço API-Users")
                        .version("1.0")
                        .description("Microsserviço de gerenciamento de clientes.")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Junior Martins")
                                .url("https://www.linkedin.com/in/juniorsmartins/")
                        )
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Copyright (c) 2025 Junior Martins. All Rights Reserved.")
                        )
                )
                .externalDocs(new io.swagger.v3.oas.models.ExternalDocumentation()
                        .description("Documentação das APIs do Microsserviços.")
                        .url("https://github.com/juniorsmartins/microservice-2026/blob/master/README.md")
                );
    }
}
