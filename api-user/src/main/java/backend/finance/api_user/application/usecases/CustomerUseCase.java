package backend.finance.api_user.application.usecases;

import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.ports.input.CustomerInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerOutputPort;
import backend.finance.api_user.infrastructure.ports.output.UserOutputPort;
import org.springframework.stereotype.Service;

@Service
public class CustomerUseCase implements CustomerInputPort {

    @Override
    public CustomerDto create(CustomerRequest customerRequest, CustomerOutputPort customerOutputPort, UserOutputPort userOutputPort) {

        checkDuplicateEmail(customerRequest, customerOutputPort);
        checkDuplicateUsername(customerRequest, userOutputPort);
        checkRoleExists(customerRequest);

        return customerOutputPort.save(customerRequest);
    }

    private void checkDuplicateEmail(CustomerRequest dto, CustomerOutputPort customerOutputPort) {
        customerOutputPort.findByEmail(dto.email())
                .ifPresent(customer -> {
                    // TODO - criar exception customizada
                    throw new RuntimeException("Email already exists");
                });
    }

    private void checkDuplicateUsername(CustomerRequest dto, UserOutputPort userOutputPort) {
        userOutputPort.findByUsername(dto.user().username())
                .ifPresent(user -> {
                    // TODO - criar exception customizada
                    throw new RuntimeException("Username already exists");
                });
    }

    private void checkRoleExists(CustomerRequest request) {
        var roleName = request.user().role();
        try {
            RoleEnum.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundCustomException(roleName);
        }
    }
}
