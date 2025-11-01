package backend.finance.api_users.application_business_rules.validation;

import java.util.UUID;

public interface UserValidation {

    void checkDuplicateUsername(UUID customerId, String username);
}
