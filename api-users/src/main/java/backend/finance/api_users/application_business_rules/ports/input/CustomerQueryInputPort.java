package backend.finance.api_users.application_business_rules.ports.input;

import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;

import java.util.UUID;

public interface CustomerQueryInputPort {

    CustomerResponse findByIdAndActiveTrue(UUID id);
}
