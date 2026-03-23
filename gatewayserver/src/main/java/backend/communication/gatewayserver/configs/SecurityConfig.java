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

    @Bean // O metodo securityWebFilterChain é responsável por configurar a cadeia de filtros de segurança para o aplicativo. Ele define as regras de autorização para diferentes endpoints da API, especificando quais papéis (roles) são necessários para acessar determinados recursos. Além disso, ele configura o OAuth2 Resource Server para usar JWT (JSON Web Tokens) para autenticação e autorização, e desabilita a proteção CSRF (Cross-Site Request Forgery) para simplificar as requisições entre serviços.
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET).permitAll() // Permite acesso a todas as requisições GET sem autenticação
                .pathMatchers("/api/*/customers/**").hasAnyRole("ADMIN", "USERS")
                .pathMatchers("/api/*/notifications/**").hasAnyRole("ADMIN", "NOTIFICATIONS")
                .pathMatchers("/api/*/ias/**").hasAnyRole("ADMIN", "IAS")
                .pathMatchers("/api/*/news/**").hasAnyRole("ADMIN", "NEWS"))

                // Configura o aplicativo como um Resource Server OAuth2, o que significa que ele irá validar os tokens JWT recebidos nas requisições para autenticar e autorizar os usuários. O metodo jwt é usado para configurar a autenticação baseada em JWT, e o jwtAuthenticationConverter é configurado para extrair as autoridades (roles) do token JWT usando o grantedAuthoritiesExtractor(), que é responsável por converter os papéis do Keycloak em GrantedAuthority para o Spring Security.
                .oauth2ResourceServer(spec -> spec
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));

        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);

        return serverHttpSecurity.build();
    }

    // É responsável por configurar a extração de autoridades (roles) do token JWT. Ele utiliza o JwtAuthenticationConverter para converter o token JWT em um objeto de autenticação que o Spring Security pode usar para autorizar o acesso aos recursos protegidos. O KeycloakRoleConverter é usado para extrair os papéis do token JWT e convertê-los em GrantedAuthority, que são usados pelo Spring Security para determinar as permissões do usuário.
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
