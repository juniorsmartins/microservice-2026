package backend.finance.application.ports.output;

import backend.finance.application.dtos.RoleDto;

import java.util.Optional;

public interface RoleOutputPort {

    Optional<RoleDto> findByName(String name);

    RoleDto save(RoleDto roleDto);
}
