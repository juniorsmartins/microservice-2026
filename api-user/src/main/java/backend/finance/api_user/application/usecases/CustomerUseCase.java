package backend.finance.api_user.application.usecases;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.ports.input.CustomerInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerOutputPort;
import backend.finance.api_user.infrastructure.ports.output.UserOutputPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerUseCase implements CustomerInputPort {

    @Override
    public CustomerDto create(CustomerRequest customerRequest, CustomerOutputPort customerOutputPort, UserOutputPort userOutputPort) {
        return Optional.ofNullable(customerRequest)
                .map(request -> checkDuplicateEmail(request, customerOutputPort))
                .map(request -> checkDuplicateUsername(request, userOutputPort))
                .map(this::checkRoleExists)
                .map(customerOutputPort::save)
                .orElseThrow();
    }

    private CustomerRequest checkDuplicateEmail(CustomerRequest dto, CustomerOutputPort customerOutputPort) {
        customerOutputPort.findByEmail(dto.email())
                .ifPresent(customer -> {
                    // TODO - criar exception customizada
                    throw new RuntimeException("Email already exists");
                });
        return dto;
    }

    private CustomerRequest checkDuplicateUsername(CustomerRequest dto, UserOutputPort userOutputPort) {
        userOutputPort.findByUsername(dto.user().username())
                .ifPresent(user -> {
                    // TODO - criar exception customizada
                    throw new RuntimeException("Username already exists");
                });
        return dto;
    }

    private CustomerRequest checkRoleExists(CustomerRequest request) {
        try {
            RoleEnum.valueOf(request.user().role());
            return request;
        } catch (IllegalArgumentException e) {
            // TODO - Add Custom Exception
            throw new RuntimeException("Role '" + request.user().role() + "' does not exist", e);
        }
    }
}
