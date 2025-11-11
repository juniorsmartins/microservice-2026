package backend.finance.application.validation;

import backend.finance.application.dtos.RoleDto;

public interface RoleValidation {

    RoleDto getOrCreateRole(String name);
}
