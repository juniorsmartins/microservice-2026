package backend.finance.api_user.infrastructure.ports.input;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;

public interface CustomerCreateInputPort {

    CustomerDto create(CustomerRequest customerRequest);
}
