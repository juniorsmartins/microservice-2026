package backend.finance.api_users.application_business_rules.validation;

import backend.finance.api_users.application_business_rules.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_users.application_business_rules.dtos.internal.RoleDto;
import backend.finance.api_users.enterprise_business_rules.enums.RoleEnum;
import backend.finance.api_users.application_business_rules.ports.output.RoleOutputPort;
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
