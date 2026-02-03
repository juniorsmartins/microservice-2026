package backend.communication.gatewayserver.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET).permitAll()
                .pathMatchers("/api/*/customers/**").authenticated()
                .pathMatchers("/api/*/notifications/**").authenticated()
                .pathMatchers("/api/*/ias/**").authenticated()
                .pathMatchers("/api/*/news/**").authenticated())
                .oauth2ResourceServer(spec -> spec.jwt(Customizer.withDefaults()));

        serverHttpSecurity.csrf(spec -> spec.disable());

        return serverHttpSecurity.build();
    }
}
