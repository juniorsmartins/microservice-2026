package backend.finance.api_users.infrastructure.ports.input;

import backend.finance.api_users.application.dtos.input.CustomerRequest;
import backend.finance.api_users.domain.entities.Customer;

import java.util.UUID;

public interface CustomerUpdateInputPort {

    Customer update(UUID customerId, CustomerRequest customerRequest);
}
