package backend.finance.api_users.interface_adapters.presenters;

import backend.finance.api_users.application_business_rules.dtos.output.UserResponse;
import backend.finance.api_users.enterprise_business_rules.entities.Usuario;
import backend.finance.api_users.interface_adapters.jpas.UserJpa;

public interface UserPresenter {

    UserResponse toResponse(Usuario usuario);

    UserResponse toResponse(UserJpa jpa);

    UserJpa toJpa(Usuario usuario);

    Usuario toEntity(UserJpa jpa);
}
