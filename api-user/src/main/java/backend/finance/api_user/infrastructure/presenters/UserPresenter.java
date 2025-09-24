package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.input.UserRequest;
import backend.finance.api_user.application.dtos.internal.UserDto;
import backend.finance.api_user.application.dtos.output.UserResponse;
import backend.finance.api_user.infrastructure.jpas.RoleJpa;
import backend.finance.api_user.infrastructure.jpas.UserJpa;

public final class UserPresenter {

    public static UserResponse toUserResponse(UserDto dto) {
        return new UserResponse(dto.id(), dto.username());
    }

    public static UserDto toUserDto(UserJpa jpa) {
        return new UserDto(jpa.getId(), jpa.getUsername());
    }

    public static UserJpa toUserJpa(UserRequest request, RoleJpa role) {
        return new UserJpa(null, request.username(), request.password(), role);
    }
}
