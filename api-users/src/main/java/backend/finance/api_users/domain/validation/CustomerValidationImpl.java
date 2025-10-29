package backend.finance.api_users.domain.validation;

import backend.finance.api_users.application.configs.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_users.infrastructure.ports.output.CustomerQueryOutputPort;
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
