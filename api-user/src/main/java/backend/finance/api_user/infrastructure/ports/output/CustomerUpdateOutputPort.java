package backend.finance.api_user.infrastructure.ports.output;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;

import java.util.UUID;

public interface CustomerUpdateOutputPort {

    CustomerDto update(UUID customerId, CustomerRequest customerRequest);
}
