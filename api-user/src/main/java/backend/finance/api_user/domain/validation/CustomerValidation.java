package backend.finance.api_user.domain.validation;

import backend.finance.api_user.application.dtos.input.CustomerRequest;

import java.util.UUID;

public interface CustomerValidation {

    void checkDuplicateEmail(UUID customerId, CustomerRequest request);

    void checkDuplicateUsername(UUID customerId, CustomerRequest request);

    void checkRoleExists(CustomerRequest request);
}
