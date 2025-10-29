package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.output.UserResponse;
import backend.finance.api_user.domain.entities.Usuario;
import backend.finance.api_user.infrastructure.jpas.UserJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class UserPresenterImpl implements UserPresenter {

    private final RolePresenter rolePresenter;

    @Override
    public UserResponse toUserResponse(Usuario usuario) {
        return new UserResponse(usuario.getId(), usuario.getUsername(), usuario.isActive());
    }

    @Override
    public UserJpa toUserJpa(Usuario usuario) {
        var roleJpa = rolePresenter.toRoleJpa(usuario.getRole());
        return new UserJpa(usuario.getId(), usuario.getUsername(), usuario.getPassword(), roleJpa, usuario.isActive());
    }

    @Override
    public Usuario toEntity(UserJpa jpa) {
        var permissao = rolePresenter.toEntity(jpa.getRole());
        return Usuario.create(jpa.getId(), jpa.getUsername(), jpa.getPassword(), permissao, jpa.isActive());
    }
}
