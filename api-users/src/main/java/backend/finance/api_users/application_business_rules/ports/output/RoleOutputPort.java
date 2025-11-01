package backend.finance.api_users.application_business_rules.ports.output;

import backend.finance.api_users.application_business_rules.dtos.internal.RoleDto;
import backend.finance.api_users.enterprise_business_rules.enums.RoleEnum;

import java.util.Optional;

public interface RoleOutputPort {

    Optional<RoleDto> findByName(RoleEnum name);

    RoleDto save(RoleDto roleDto);
}
