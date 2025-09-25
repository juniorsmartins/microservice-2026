package backend.finance.api_user.infrastructure.ports.input;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;

import java.util.UUID;

public interface CustomerInputPort {

    CustomerDto create(CustomerRequest customerRequest);

    CustomerDto update(UUID customerId, CustomerRequest customerRequest);

    void deleteById(UUID id);
}
