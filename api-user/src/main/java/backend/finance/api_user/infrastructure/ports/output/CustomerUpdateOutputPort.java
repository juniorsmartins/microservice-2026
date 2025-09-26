package backend.finance.api_user.infrastructure.ports.output;

import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.entities.Customer;

import java.util.UUID;

public interface CustomerUpdateOutputPort {

    CustomerDto update(UUID customerId, Customer customer);
}
