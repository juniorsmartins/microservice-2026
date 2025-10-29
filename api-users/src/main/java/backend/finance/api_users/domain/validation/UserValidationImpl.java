package backend.finance.api_users.domain.validation;

import backend.finance.api_users.application.configs.exception.http409.UsernameConflictRulesCustomException;
import backend.finance.api_users.infrastructure.ports.output.CustomerQueryOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserValidationImpl implements UserValidation {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    @Override
    public void checkDuplicateUsername(UUID customerId, String username) {
        customerQueryOutputPort.findByUsername(username)
                .ifPresent(customer -> {
                    if (customerId == null || !customerId.equals(customer.getId())) {
                        throw new UsernameConflictRulesCustomException(username);
                    }
                });
    }
}
