package backend.finance.application.ports.input;

import backend.finance.application.dtos.request.CustomerRequest;
import backend.finance.application.dtos.response.CustomerResponse;

public interface CustomerCreateInputPort {

    CustomerResponse create(CustomerRequest request);
}
