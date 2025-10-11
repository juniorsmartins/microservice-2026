package backend.finance.api_user.domain.validation;

import java.util.UUID;

public interface UserValidation {

    void checkDuplicateUsername(UUID customerId, String username);
}
