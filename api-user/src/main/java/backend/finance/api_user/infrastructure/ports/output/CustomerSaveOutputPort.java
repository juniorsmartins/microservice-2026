package backend.finance.api_user.infrastructure.ports.output;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;

public interface CustomerSaveOutputPort {

    CustomerDto save(CustomerRequest customerRequest);
}
