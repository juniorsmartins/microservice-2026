package backend.finance.api_users.domain.validation;

import java.util.UUID;

public interface UserValidation {

    void checkDuplicateUsername(UUID customerId, String username);
}
