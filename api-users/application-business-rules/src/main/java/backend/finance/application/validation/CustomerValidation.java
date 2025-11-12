package backend.finance.application.validation;

import java.util.UUID;

public interface CustomerValidation {

    void checkDuplicateEmail(UUID customerId, String email);
}
