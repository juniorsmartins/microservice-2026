package backend.finance.api_users.interface_adapters.presenters;

import backend.finance.api_users.application_business_rules.dtos.output.UserResponse;
import backend.finance.api_users.enterprise_business_rules.entities.Usuario;
import backend.finance.api_users.frameworks_drivers.jpas.UserJpa;

public interface UserPresenter {

    UserResponse toUserResponse(Usuario usuario);

    UserJpa toUserJpa(Usuario usuario);

    Usuario toEntity(UserJpa jpa);
}
