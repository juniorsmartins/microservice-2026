package backend.finance.api_users.application_business_rules.validation;

import backend.finance.api_users.application_business_rules.exception.http409.UsernameConflictRulesCustomException;
import backend.finance.api_users.application_business_rules.ports.output.CustomerQueryOutputPort;
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
