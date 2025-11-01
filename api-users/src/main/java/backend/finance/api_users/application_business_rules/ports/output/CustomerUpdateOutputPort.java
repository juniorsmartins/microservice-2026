package backend.finance.api_users.application_business_rules.ports.output;

import backend.finance.api_users.enterprise_business_rules.entities.Customer;

public interface CustomerUpdateOutputPort {

    Customer update(Customer customer);
}
