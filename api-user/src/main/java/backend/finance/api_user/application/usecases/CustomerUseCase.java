package backend.finance.api_user.application.usecases;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.validation.CustomerValidation;
import backend.finance.api_user.infrastructure.ports.input.CustomerInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerPersistenceOutputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerQueryOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerUseCase implements CustomerInputPort {

    private final CustomerPersistenceOutputPort customerPersistenceOutputPort;

    private final CustomerQueryOutputPort customerQueryOutputPort;

    private final CustomerValidation customerValidation;

    @Override
    public CustomerDto create(CustomerRequest customerRequest) {

        customerValidation.checkDuplicateEmail(null, customerRequest);
        customerValidation.checkDuplicateUsername(null, customerRequest);
        customerValidation.checkRoleExists(customerRequest);

        return customerPersistenceOutputPort.save(customerRequest);
    }

    @Override
    public CustomerDto update(UUID customerId, CustomerRequest customerRequest) {

        customerValidation.checkDuplicateEmail(customerId, customerRequest);
        customerValidation.checkDuplicateUsername(customerId, customerRequest);
        customerValidation.checkRoleExists(customerRequest);

        return customerPersistenceOutputPort.update(customerId, customerRequest);
    }

    @Override
    public void deleteById(UUID id) {
        customerQueryOutputPort.findById(id)
                .ifPresentOrElse(customerDto -> customerPersistenceOutputPort.deleteById(customerDto.id()),
                        () -> {
                            throw new CustomerNotFoundCustomException(id);
                        });
    }
}
