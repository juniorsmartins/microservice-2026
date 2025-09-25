package backend.finance.api_user.application.usecases;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_user.application.configs.exception.http409.UsernameConflictRulesCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.ports.input.CustomerInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerOutputPort;
import backend.finance.api_user.infrastructure.ports.output.UserOutputPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerUseCase implements CustomerInputPort {

    @Override
    public CustomerDto create(CustomerRequest customerRequest, CustomerOutputPort customerOutputPort, UserOutputPort userOutputPort) {

        checkDuplicateEmail(customerRequest, customerOutputPort);
        checkDuplicateUsername(customerRequest, userOutputPort);
        checkRoleExists(customerRequest);

        return customerOutputPort.save(customerRequest);
    }

    @Override
    public void deleteById(UUID id, CustomerOutputPort customerOutputPort) {
        customerOutputPort.findById(id)
                .ifPresentOrElse(customerDto -> customerOutputPort.deleteById(customerDto.id()),
                        () -> {throw new CustomerNotFoundCustomException(id);});
    }

    private void checkDuplicateEmail(CustomerRequest dto, CustomerOutputPort customerOutputPort) {
        var email = dto.email();
        customerOutputPort.findByEmail(dto.email())
                .ifPresent(customerDto -> {
                    throw new EmailConflictRulesCustomException(email);
                });
    }

    private void checkDuplicateUsername(CustomerRequest dto, UserOutputPort userOutputPort) {
        var username = dto.user().username();
        userOutputPort.findByUsername(username)
                .ifPresent(user -> {
                    throw new UsernameConflictRulesCustomException(username);
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
