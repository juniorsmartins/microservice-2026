package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.input.UserRequest;
import backend.finance.api_user.application.dtos.internal.UserDto;
import backend.finance.api_user.application.dtos.output.UserResponse;
import backend.finance.api_user.infrastructure.jpas.RoleJpa;
import backend.finance.api_user.infrastructure.jpas.UserJpa;
import org.springframework.stereotype.Component;

@Component
public final class UserPresenterImpl implements UserPresenter {

    @Override
    public UserResponse toUserResponse(UserDto dto) {
        return new UserResponse(dto.id(), dto.username());
    }

    @Override
    public UserDto toUserDto(UserJpa jpa) {
        return new UserDto(jpa.getId(), jpa.getUsername());
    }

    @Override
    public UserJpa toUserJpa(UserRequest request, RoleJpa role) {
        return new UserJpa(null, request.username(), request.password(), role);
    }
}
