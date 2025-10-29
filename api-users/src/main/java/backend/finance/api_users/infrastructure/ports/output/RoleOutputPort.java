package backend.finance.api_users.infrastructure.ports.output;

import backend.finance.api_users.application.dtos.internal.RoleDto;
import backend.finance.api_users.domain.enums.RoleEnum;

import java.util.Optional;

public interface RoleOutputPort {

    Optional<RoleDto> findByName(RoleEnum name);

    RoleDto save(RoleDto roleDto);
}
