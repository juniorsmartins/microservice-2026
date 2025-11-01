package backend.finance.api_users.application_business_rules.ports.input;

import java.util.UUID;

public interface CustomerDeleteInputPort {

    void disableById(UUID id);
}
