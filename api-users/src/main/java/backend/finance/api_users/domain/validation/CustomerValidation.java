package backend.finance.api_users.domain.validation;

import java.util.UUID;

public interface CustomerValidation {

    void checkDuplicateEmail(UUID customerId, String email);
}
