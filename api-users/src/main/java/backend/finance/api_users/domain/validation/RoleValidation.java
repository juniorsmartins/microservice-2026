package backend.finance.api_users.domain.validation;

import backend.finance.api_users.application.dtos.internal.RoleDto;

public interface RoleValidation {

    RoleDto getOrCreateRole(String name);
}
