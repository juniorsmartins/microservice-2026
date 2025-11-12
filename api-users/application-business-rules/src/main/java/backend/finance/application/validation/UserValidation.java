package backend.finance.application.validation;

import java.util.UUID;

public interface UserValidation {

    void checkDuplicateUsername(UUID customerId, String username);
}
