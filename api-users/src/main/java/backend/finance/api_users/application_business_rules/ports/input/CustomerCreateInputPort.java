package backend.finance.api_users.application_business_rules.ports.input;

import backend.finance.api_users.application_business_rules.dtos.input.CustomerRequest;
import backend.finance.api_users.enterprise_business_rules.entities.Customer;

public interface CustomerCreateInputPort {

    Customer create(CustomerRequest customerRequest);
}
