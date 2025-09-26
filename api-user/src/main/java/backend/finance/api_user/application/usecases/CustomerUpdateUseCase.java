package backend.finance.api_user.application.usecases;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.validation.CustomerValidation;
import backend.finance.api_user.infrastructure.ports.input.CustomerUpdateInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerUpdateOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerUpdateUseCase implements CustomerUpdateInputPort {

    private final CustomerUpdateOutputPort customerUpdateOutputPort;

    private final CustomerValidation customerValidation;

    @Override
    public CustomerDto update(UUID customerId, CustomerRequest customerRequest) {

        customerValidation.checkDuplicateEmail(customerId, customerRequest);
        customerValidation.checkDuplicateUsername(customerId, customerRequest);
        customerValidation.checkRoleExists(customerRequest);

        return customerUpdateOutputPort.update(customerId, customerRequest);
    }
}
