package backend.finance.api_users.application_business_rules.validation;

import java.util.UUID;

public interface CustomerValidation {

    void checkDuplicateEmail(UUID customerId, String email);
}
