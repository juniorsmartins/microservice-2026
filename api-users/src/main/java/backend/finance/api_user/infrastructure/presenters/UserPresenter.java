package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.output.UserResponse;
import backend.finance.api_user.domain.entities.Usuario;
import backend.finance.api_user.infrastructure.jpas.UserJpa;

public interface UserPresenter {

    UserResponse toUserResponse(Usuario usuario);

    UserJpa toUserJpa(Usuario usuario);

    Usuario toEntity(UserJpa jpa);
}
