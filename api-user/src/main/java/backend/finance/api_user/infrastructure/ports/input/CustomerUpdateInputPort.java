package backend.finance.api_user.infrastructure.ports.input;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;

import java.util.UUID;

public interface CustomerUpdateInputPort {

    CustomerDto update(UUID customerId, CustomerRequest customerRequest);
}
