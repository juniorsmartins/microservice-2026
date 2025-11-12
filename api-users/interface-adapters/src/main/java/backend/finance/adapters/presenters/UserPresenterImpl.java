package backend.finance.adapters.presenters;

import backend.finance.adapters.jpas.UserJpa;
import backend.finance.application.dtos.UserDto;
import backend.finance.application.dtos.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class UserPresenterImpl implements UserPresenter {

    private final RolePresenter rolePresenter;

    @Override
    public UserResponse toResponse(UserJpa jpa) {
        return new UserResponse(jpa.getId(), jpa.getUsername(), jpa.isActive());
    }

    @Override
    public UserDto toDto(UserJpa jpa) {
        var roleDto = rolePresenter.toRoleDto(jpa.getRole());
        return new UserDto(jpa.getId(), jpa.getUsername(), jpa.getPassword(), roleDto, jpa.isActive());
    }

    @Override
    public UserJpa toJpa(UserDto dto) {
        var roleJpa = rolePresenter.toRoleJpa(dto.role());
        return new UserJpa(dto.id(), dto.username(), dto.password(), roleJpa, dto.active());
    }
}
