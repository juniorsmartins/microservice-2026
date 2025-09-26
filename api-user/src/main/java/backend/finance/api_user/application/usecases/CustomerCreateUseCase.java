package backend.finance.api_user.application.usecases;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.validation.CustomerValidation;
import backend.finance.api_user.infrastructure.ports.input.CustomerCreateInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerSaveOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCreateUseCase implements CustomerCreateInputPort {

    private final CustomerSaveOutputPort customerSaveOutputPort;

    private final CustomerValidation customerValidation;

    @Override
    public CustomerDto create(CustomerRequest customerRequest) {

        customerValidation.checkDuplicateEmail(null, customerRequest);
        customerValidation.checkDuplicateUsername(null, customerRequest);
        customerValidation.checkRoleExists(customerRequest);

        return customerSaveOutputPort.save(customerRequest);
    }
}
