package backend.communication.gatewayserver.configs;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Esta classe é responsável por converter os papéis (roles) do Keycloak para um formato que o Spring Security possa entender.
// O Keycloak geralmente retorna os papéis com um prefixo "ROLE_", e esta classe pode ser usada para remover esse prefixo ou ajustar os papéis conforme necessário para a configuração de segurança do Spring.
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) { // É chamado para converter um objeto Jwt (que representa o token JWT recebido) em uma coleção de GrantedAuthority (que representa os papéis do usuário).

        // O getClaim é usado para extrair o valor do claim "realm_access" do token JWT. O claim "realm_access" geralmente contém informações sobre os papéis do usuário no Keycloak.
        Map<String, Object> realmAccess = source.getClaim("realm_access");

        if (realmAccess == null || realmAccess.isEmpty()) {
            return List.of();
        }

        // O get é usado para extrair a lista de papéis do claim "roles" dentro do claim "realm_access". A lista de papéis é então processada usando stream para adicionar o prefixo "ROLE_" a cada papel e convertê-los em objetos SimpleGrantedAuthority, que são usados pelo Spring Security para representar os papéis do usuário.
        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles"))
                .stream().map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return returnValue;
    }
}
