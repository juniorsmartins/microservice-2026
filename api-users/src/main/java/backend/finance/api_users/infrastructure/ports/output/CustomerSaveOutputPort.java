package backend.finance.api_users.infrastructure.ports.output;

import backend.finance.api_users.domain.entities.Customer;

public interface CustomerSaveOutputPort {

    Customer save(Customer customer);
}
