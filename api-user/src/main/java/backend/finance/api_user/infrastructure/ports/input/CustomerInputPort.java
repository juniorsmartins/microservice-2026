package backend.finance.api_user.infrastructure.ports.input;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.infrastructure.ports.output.CustomerOutputPort;
import backend.finance.api_user.infrastructure.ports.output.RoleOutputPort;
import backend.finance.api_user.infrastructure.ports.output.UserOutputPort;

public interface CustomerInputPort {

    CustomerDto create(CustomerRequest customerRequest, CustomerOutputPort customerOutputPort, UserOutputPort userOutputPort, RoleOutputPort roleOutputPort);
}
