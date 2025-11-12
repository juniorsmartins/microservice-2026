package backend.finance.application.validation;

import backend.finance.application.exceptions.http409.EmailConflictRulesCustomException;
import backend.finance.application.ports.output.CustomerQueryOutputPort;

import java.util.UUID;

public final class CustomerValidationImpl implements CustomerValidation {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    public CustomerValidationImpl(CustomerQueryOutputPort customerQueryOutputPort) {
        this.customerQueryOutputPort = customerQueryOutputPort;
    }

    @Override
    public void checkDuplicateEmail(UUID customerId, String email) {
        customerQueryOutputPort.findByEmail(email)
                .ifPresent(dto -> {
                    if (customerId == null || !customerId.equals(dto.id())) {
                        throw new EmailConflictRulesCustomException(email);
                    }
                });
    }
}
