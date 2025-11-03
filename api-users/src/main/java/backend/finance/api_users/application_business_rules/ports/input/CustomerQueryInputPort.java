package backend.finance.api_users.application_business_rules.ports.input;

import backend.finance.api_users.enterprise_business_rules.entities.Customer;

import java.util.UUID;

public interface CustomerQueryInputPort {

    Customer findByIdAndActiveTrue(UUID id);
}
