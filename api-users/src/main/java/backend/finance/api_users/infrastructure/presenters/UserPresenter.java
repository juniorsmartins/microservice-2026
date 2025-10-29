package backend.finance.api_users.infrastructure.presenters;

import backend.finance.api_users.application.dtos.output.UserResponse;
import backend.finance.api_users.domain.entities.Usuario;
import backend.finance.api_users.infrastructure.jpas.UserJpa;

public interface UserPresenter {

    UserResponse toUserResponse(Usuario usuario);

    UserJpa toUserJpa(Usuario usuario);

    Usuario toEntity(UserJpa jpa);
}
