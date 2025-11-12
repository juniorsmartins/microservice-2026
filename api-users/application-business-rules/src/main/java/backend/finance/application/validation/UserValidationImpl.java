package backend.finance.application.validation;

import backend.finance.application.exceptions.http409.UsernameConflictRulesCustomException;
import backend.finance.application.ports.output.CustomerQueryOutputPort;

import java.util.UUID;

public final class UserValidationImpl implements UserValidation {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    public UserValidationImpl(CustomerQueryOutputPort customerQueryOutputPort) {
        this.customerQueryOutputPort = customerQueryOutputPort;
    }

    @Override
    public void checkDuplicateUsername(UUID customerId, String username) {
        customerQueryOutputPort.findByUsername(username)
                .ifPresent(dto -> {
                    if (customerId == null || !customerId.equals(dto.id())) {
                        throw new UsernameConflictRulesCustomException(username);
                    }
                });
    }
}
