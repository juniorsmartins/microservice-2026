package backend.finance.application.validation;

import backend.finance.application.dtos.RoleDto;
import backend.finance.application.exceptions.http404.RoleNotFoundCustomException;
import backend.finance.application.ports.output.RoleOutputPort;
import backend.finance.enterprise.enums.RoleEnum;

public final class RoleValidationImpl implements RoleValidation {

    private final RoleOutputPort roleOutputPort;

    public RoleValidationImpl(RoleOutputPort roleOutputPort) {
        this.roleOutputPort = roleOutputPort;
    }

    @Override
    public RoleDto getOrCreateRole(String name) {
        try {
            var roleEnum = RoleEnum.valueOf(name);
            var roleDto = roleOutputPort.findByName(name);
            return roleDto.orElseGet(() -> roleOutputPort.save(new RoleDto(null, roleEnum.name())));
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundCustomException(name);
        }
    }
}
