package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.internal.UserDto;
import backend.finance.api_user.application.dtos.output.UserResponse;
import backend.finance.api_user.domain.entities.Usuario;
import backend.finance.api_user.infrastructure.jpas.RoleJpa;
import backend.finance.api_user.infrastructure.jpas.UserJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class UserPresenterImpl implements UserPresenter {

    private final RolePresenter rolePresenter;

    @Override
    public UserResponse toUserResponse(UserDto dto) {
        return new UserResponse(dto.id(), dto.username());
    }

    @Override
    public UserDto toUserDto(UserJpa jpa) {
        return new UserDto(jpa.getId(), jpa.getUsername());
    }

    @Override
    public UserJpa toUserJpa(Usuario usuario) {
        var roleJpa = rolePresenter.toRoleJpa(usuario.getRole());
        return new UserJpa(usuario.getId(), usuario.getUsername(), usuario.getPassword(), roleJpa);
    }
}
