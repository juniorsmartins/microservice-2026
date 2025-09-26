package backend.finance.api_user.infrastructure.ports.output;

import backend.finance.api_user.application.dtos.internal.RoleDto;
import backend.finance.api_user.domain.enums.RoleEnum;

import java.util.Optional;

public interface RoleOutputPort {

    Optional<RoleDto> findByName(RoleEnum name);

    RoleDto save(RoleDto roleDto);
}
