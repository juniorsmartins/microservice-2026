package backend.finance.application.ports.input;

import backend.finance.application.dtos.request.CustomerRequest;
import backend.finance.application.dtos.response.CustomerResponse;

import java.util.UUID;

public interface CustomerUpdateInputPort {

    CustomerResponse update(UUID customerId, CustomerRequest customerRequest);
}
