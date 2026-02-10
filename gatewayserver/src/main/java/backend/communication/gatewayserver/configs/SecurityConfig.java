package backend.communication.gatewayserver.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET).permitAll()
                .pathMatchers("/api/*/customers/**").hasRole("CUSTOMER")
                .pathMatchers("/api/*/notifications/**").hasRole("ADMIN")
                .pathMatchers("/api/*/ias/**").hasRole("CUSTOMER")
                .pathMatchers("/api/*/news/**").hasRole("CUSTOMER"))

                .oauth2ResourceServer(spec -> spec
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));

        serverHttpSecurity.csrf(spec -> spec.disable());

        return serverHttpSecurity.build();
    }

    // É responsável por configurar a extração de autoridades (roles) do token JWT. Ele utiliza o JwtAuthenticationConverter para converter o token JWT em um objeto de autenticação que o Spring Security pode usar para autorizar o acesso aos recursos protegidos. O KeycloakRoleConverter é usado para extrair os papéis do token JWT e convertê-los em GrantedAuthority, que são usados pelo Spring Security para determinar as permissões do usuário.
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
