package backend.finance.api_users.application_business_rules.ports.input;

import backend.finance.api_users.application_business_rules.dtos.input.CustomerRequest;
import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;

public interface CustomerCreateInputPort {

    CustomerResponse create(CustomerRequest customerRequest);
}
