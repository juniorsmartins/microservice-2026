package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.internal.UserDto;
import backend.finance.api_user.application.dtos.output.UserResponse;
import backend.finance.api_user.domain.entities.Usuario;
import backend.finance.api_user.infrastructure.jpas.RoleJpa;
import backend.finance.api_user.infrastructure.jpas.UserJpa;

public interface UserPresenter {

    UserResponse toUserResponse(UserDto dto);

    UserDto toUserDto(UserJpa jpa);

    UserJpa toUserJpa(Usuario usuario);
}
