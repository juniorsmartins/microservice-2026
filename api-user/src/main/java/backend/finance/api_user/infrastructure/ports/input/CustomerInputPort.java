package backend.finance.api_user.infrastructure.ports.input;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.infrastructure.ports.output.CustomerOutputPort;
import backend.finance.api_user.infrastructure.ports.output.UserOutputPort;

import java.util.UUID;

public interface CustomerInputPort {

    CustomerDto create(CustomerRequest customerRequest, CustomerOutputPort customerOutputPort, UserOutputPort userOutputPort);

    void deleteById(UUID id, CustomerOutputPort customerOutputPort);
}
