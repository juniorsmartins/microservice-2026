package backend.finance.api_user.infrastructure.ports.input;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.domain.entities.Customer;

import java.util.UUID;

public interface CustomerUpdateInputPort {

    Customer update(UUID customerId, CustomerRequest customerRequest);
}
