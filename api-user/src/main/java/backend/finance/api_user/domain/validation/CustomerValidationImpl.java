package backend.finance.api_user.domain.validation;

import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_user.application.configs.exception.http409.UsernameConflictRulesCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.ports.output.CustomerQueryOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerValidationImpl implements CustomerValidation {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    @Override
    public void checkDuplicateEmail(UUID customerId, CustomerRequest request) {
        var email = request.email();
        customerQueryOutputPort.findByEmail(request.email())
                .ifPresent(customerDto -> {
                    if (customerId == null || !customerId.equals(customerDto.id())) {
                        throw new EmailConflictRulesCustomException(email);
                    }
                });
    }

    @Override
    public void checkDuplicateUsername(UUID customerId, CustomerRequest request) {
        var username = request.user().username();
        customerQueryOutputPort.findByUsername(username)
                .ifPresent(customerDto -> {
                    if (customerId == null || !customerId.equals(customerDto.id())) {
                        throw new UsernameConflictRulesCustomException(username);
                    }
                });
    }

    @Override
    public void checkRoleExists(CustomerRequest request) {
        var roleName = request.user().role();
        try {
            RoleEnum.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundCustomException(roleName);
        }
    }
}
