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

                .route("api-users", p -> p // Define uma rota com o ID "api-users".
                        .path("/api/{version}/customers/**") // Define o predicado de caminho para a rota. O padrão "/api-users/**" indica que qualquer solicitação que comece com "/api-users/" será roteada por esta rota.
                        .uri("lb://api-users")) // Define o URI de destino para a rota. "lb://" indica que o Gateway deve usar o balanceamento de carga para rotear as solicitações para o serviço registrado com o nome "api-users".

                .route("api-notifications", p -> p
                        .path("/api/{version}/notifications/**")
                        .uri("lb://api-notifications"))

                .route("api-news", p -> p
                        .path("/api/{version}/news/**")
                        .uri("lb://api-news"))

                .build();
    }
}
