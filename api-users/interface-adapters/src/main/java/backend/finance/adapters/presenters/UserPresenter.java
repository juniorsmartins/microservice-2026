package backend.finance.adapters.presenters;

import backend.finance.adapters.jpas.UserJpa;
import backend.finance.application.dtos.UserDto;
import backend.finance.application.dtos.response.UserAllResponse;
import backend.finance.application.dtos.response.UserResponse;

public interface UserPresenter {

    UserResponse toResponse(UserJpa jpa);

    UserDto toDto(UserJpa jpa);

    UserJpa toJpa(UserDto dto);

    UserAllResponse toAllResponse(UserJpa jpa);
}
