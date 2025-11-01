package backend.finance.api_users.enterprise_business_rules.validation;

import backend.finance.api_users.application_business_rules.dtos.internal.RoleDto;

public interface RoleValidation {

    RoleDto getOrCreateRole(String name);
}
