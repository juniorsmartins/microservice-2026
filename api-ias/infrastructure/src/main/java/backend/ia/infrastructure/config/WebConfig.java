package backend.ia.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI openApi() { // OpenAPI - Swagger
        return new OpenAPI()
                .components(new Components())
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Microsserviço API-Ias")
                        .version("1.0")
                        .description("Microsserviço responsável por fornecer inteligência artificial.")
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
