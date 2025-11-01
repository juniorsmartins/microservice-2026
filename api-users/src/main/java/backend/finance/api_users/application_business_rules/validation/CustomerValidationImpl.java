package backend.finance.api_users.application_business_rules.validation;

import backend.finance.api_users.application_business_rules.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_users.application_business_rules.ports.output.CustomerQueryOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerValidationImpl implements CustomerValidation {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    @Override
    public void checkDuplicateEmail(UUID customerId, String email) {
        customerQueryOutputPort.findByEmail(email)
                .ifPresent(customer -> {
                    if (customerId == null || !customerId.equals(customer.getId())) {
                        throw new EmailConflictRulesCustomException(email);
                    }
                });
    }
}
