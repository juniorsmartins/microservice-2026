package backend.finance.api_user.application.usecases;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_user.application.configs.exception.http409.UsernameConflictRulesCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.ports.input.CustomerInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerCommandOutputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerQueryOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerUseCase implements CustomerInputPort {

    private final CustomerCommandOutputPort customerCommandOutputPort;

    private final CustomerQueryOutputPort customerQueryOutputPort;

    @Override
    public CustomerDto create(CustomerRequest customerRequest) {

        checkDuplicateEmail(null, customerRequest);
        checkDuplicateUsername(null, customerRequest);
        checkRoleExists(customerRequest);

        return customerCommandOutputPort.save(customerRequest);
    }

    @Override
    public CustomerDto update(UUID customerId, CustomerRequest customerRequest) {

        checkDuplicateEmail(customerId, customerRequest);
        checkDuplicateUsername(customerId, customerRequest);
        checkRoleExists(customerRequest);

        return customerCommandOutputPort.update(customerId, customerRequest);
    }

    @Override
    public void deleteById(UUID id) {
        customerQueryOutputPort.findById(id)
                .ifPresentOrElse(customerDto -> customerCommandOutputPort.deleteById(customerDto.id()),
                        () -> {
                            throw new CustomerNotFoundCustomException(id);
                        });
    }

    private void checkDuplicateEmail(UUID customerId, CustomerRequest dto) {
        var email = dto.email();
        customerQueryOutputPort.findByEmail(dto.email())
                .ifPresent(customerDto -> {
                    if (customerId == null || !customerId.equals(customerDto.id())) {
                        throw new EmailConflictRulesCustomException(email);
                    }
                });
    }

    private void checkDuplicateUsername(UUID customerId, CustomerRequest dto) {
        var username = dto.user().username();
        customerQueryOutputPort.findByUsername(username)
                .ifPresent(customerDto -> {
                    if (customerId == null || !customerId.equals(customerDto.id())) {
                        throw new UsernameConflictRulesCustomException(username);
                    }
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
