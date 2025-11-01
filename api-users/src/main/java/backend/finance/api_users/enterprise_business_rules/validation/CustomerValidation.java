package backend.finance.api_users.enterprise_business_rules.validation;

import java.util.UUID;

public interface CustomerValidation {

    void checkDuplicateEmail(UUID customerId, String email);
}
