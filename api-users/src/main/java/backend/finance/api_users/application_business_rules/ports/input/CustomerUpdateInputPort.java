package backend.finance.api_users.application_business_rules.ports.input;

import backend.finance.api_users.application_business_rules.dtos.input.CustomerRequest;
import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;

import java.util.UUID;

public interface CustomerUpdateInputPort {

    CustomerResponse update(UUID customerId, CustomerRequest customerRequest);
}
