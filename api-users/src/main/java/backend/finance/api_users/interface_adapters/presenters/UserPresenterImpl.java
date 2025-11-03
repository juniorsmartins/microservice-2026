package backend.finance.api_users.interface_adapters.presenters;

import backend.finance.api_users.application_business_rules.dtos.output.UserResponse;
import backend.finance.api_users.enterprise_business_rules.entities.Usuario;
import backend.finance.api_users.interface_adapters.jpas.UserJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class UserPresenterImpl implements UserPresenter {

    private final RolePresenter rolePresenter;

    @Override
    public UserResponse toResponse(Usuario usuario) {
        return new UserResponse(usuario.getId(), usuario.getUsername(), usuario.isActive());
    }

    @Override
    public UserResponse toResponse(UserJpa jpa) {
        return new UserResponse(jpa.getId(), jpa.getUsername(), jpa.isActive());
    }

    @Override
    public UserJpa toJpa(Usuario usuario) {
        var roleJpa = rolePresenter.toRoleJpa(usuario.getRole());
        return new UserJpa(usuario.getId(), usuario.getUsername(), usuario.getPassword(), roleJpa, usuario.isActive());
    }

    @Override
    public Usuario toEntity(UserJpa jpa) {
        var permissao = rolePresenter.toEntity(jpa.getRole());
        return Usuario.create(jpa.getId(), jpa.getUsername(), jpa.getPassword(), permissao, jpa.isActive());
    }
}
