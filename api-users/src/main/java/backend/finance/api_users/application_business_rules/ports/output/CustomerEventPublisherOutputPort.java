package backend.finance.api_users.application_business_rules.ports.output;

import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;

public interface CustomerEventPublisherOutputPort {

    void sendEventCreateCustomer(CustomerResponse response);
}
