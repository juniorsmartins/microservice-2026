package backend.finance.api_user.domain.validation;

import backend.finance.api_user.application.dtos.internal.RoleDto;

public interface RoleValidation {

    RoleDto getOrCreateRole(String name);
}
