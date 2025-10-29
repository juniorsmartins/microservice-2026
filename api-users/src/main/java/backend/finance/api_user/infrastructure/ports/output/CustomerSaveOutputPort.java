package backend.finance.api_user.infrastructure.ports.output;

import backend.finance.api_user.domain.entities.Customer;

public interface CustomerSaveOutputPort {

    Customer save(Customer customer);
}
