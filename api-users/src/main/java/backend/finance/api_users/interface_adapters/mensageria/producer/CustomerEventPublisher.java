package backend.finance.api_users.interface_adapters.mensageria.producer;

import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;

public interface CustomerEventPublisher {

    void sendEventCreateCustomer(CustomerResponse response);
}
