package backend.finance.adapters.configs;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(
    info = @Info(title = "API-Users",
            version = "1.0",
            description = "Microsserviços criado para ser currículo vivo.",
            contact = @Contact(name = "Junior Martins", url = "https://www.linkedin.com/in/juniorsmartins/"), //; Dá para acrescentar email
            license = @License(name = "Copyright (c) 2025 Junior Martins. All Rights Reserved.") // Pode adicionar url de site com termos da licença
    ),
    externalDocs = @ExternalDocumentation(description = "Documentação das APIs do Microsserviços.",
            url = "https://github.com/juniorsmartins/microservice-2026/blob/master/README.md"
    )
)
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Microsserviço API-Users")
                        .version("1.1")
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
