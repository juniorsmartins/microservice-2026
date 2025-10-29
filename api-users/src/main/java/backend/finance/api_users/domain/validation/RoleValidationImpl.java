package backend.finance.api_users.domain.validation;

import backend.finance.api_users.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_users.application.dtos.internal.RoleDto;
import backend.finance.api_users.domain.enums.RoleEnum;
import backend.finance.api_users.infrastructure.ports.output.RoleOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleValidationImpl implements RoleValidation {

    private final RoleOutputPort roleOutputPort;

    @Override
    public RoleDto getOrCreateRole(String name) {
        try {
            var roleEnum = RoleEnum.valueOf(name);
            var roleDto = roleOutputPort.findByName(roleEnum);
            return roleDto.orElseGet(() -> roleOutputPort.save(new RoleDto(null, roleEnum)));
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundCustomException(name);
        }
    }
}
