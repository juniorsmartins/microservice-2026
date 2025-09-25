package backend.finance.api_user.infrastructure.ports.input;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.infrastructure.ports.output.CustomerOutputPort;

import java.util.UUID;

public interface CustomerInputPort {

    CustomerDto create(CustomerRequest customerRequest, CustomerOutputPort customerOutputPort);

    CustomerDto update(UUID customerId, CustomerRequest customerRequest, CustomerOutputPort customerOutputPort);

    void deleteById(UUID id, CustomerOutputPort customerOutputPort);
}
