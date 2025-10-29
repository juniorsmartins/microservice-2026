package backend.finance.api_users.infrastructure.ports.input;

import backend.finance.api_users.application.dtos.input.CustomerRequest;
import backend.finance.api_users.domain.entities.Customer;

public interface CustomerCreateInputPort {

    Customer create(CustomerRequest customerRequest);
}
