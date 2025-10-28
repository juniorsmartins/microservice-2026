package backend.finance.api_user.domain.validation;

import java.util.UUID;

public interface CustomerValidation {

    void checkDuplicateEmail(UUID customerId, String email);
}
