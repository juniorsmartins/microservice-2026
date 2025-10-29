package backend.finance.api_users.infrastructure.ports.output;

import backend.finance.api_users.domain.entities.Customer;

public interface CustomerUpdateOutputPort {

    Customer update(Customer customer);
}
