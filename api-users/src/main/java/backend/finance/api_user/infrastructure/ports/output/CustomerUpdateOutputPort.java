package backend.finance.api_user.infrastructure.ports.output;

import backend.finance.api_user.domain.entities.Customer;

public interface CustomerUpdateOutputPort {

    Customer update(Customer customer);
}
