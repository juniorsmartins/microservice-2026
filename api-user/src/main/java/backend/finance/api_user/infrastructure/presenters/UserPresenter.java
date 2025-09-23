package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.internal.UserDto;
import backend.finance.api_user.application.dtos.output.UserResponse;

public final class UserPresenter {

    public static UserResponse toUserResponse(UserDto dto) {
        return new UserResponse(dto.id(), dto.username());
    }
}
