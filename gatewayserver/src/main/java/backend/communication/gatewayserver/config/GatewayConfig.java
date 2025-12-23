package backend.communication.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes() // O RouteLocatorBuilderele cria rotas e permite adicionar predicados e filtros às rotas, possibilitando o roteamento com base em determinadas condições, bem como a alteração da requisição/resposta conforme necessário.
                .route(p -> p
                        .path("/get")
                        .uri("http://httpbin.org:80"))
                .build();
    }
}
