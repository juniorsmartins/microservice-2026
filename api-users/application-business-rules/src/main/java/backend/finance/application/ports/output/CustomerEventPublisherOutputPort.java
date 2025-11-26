package backend.finance.application.ports.output;

import backend.finance.application.dtos.response.CustomerResponse;

public interface CustomerEventPublisherOutputPort {

    void sendEventCustomerCreated(CustomerResponse response);
}
