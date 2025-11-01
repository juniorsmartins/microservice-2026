package backend.finance.api_users.application_business_rules.usecases;

import backend.finance.api_users.application_business_rules.dtos.input.CustomerRequest;
import backend.finance.api_users.application_business_rules.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.application_business_rules.ports.input.CustomerUpdateInputPort;
import backend.finance.api_users.application_business_rules.ports.output.CustomerQueryOutputPort;
import backend.finance.api_users.application_business_rules.ports.output.CustomerSaveOutputPort;
import backend.finance.api_users.application_business_rules.validation.CustomerValidation;
import backend.finance.api_users.application_business_rules.validation.RoleValidation;
import backend.finance.api_users.application_business_rules.validation.UserValidation;
import backend.finance.api_users.enterprise_business_rules.entities.Customer;
import backend.finance.api_users.enterprise_business_rules.entities.Permissao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerUpdateUseCase implements CustomerUpdateInputPort {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    private final CustomerSaveOutputPort customerSaveOutputPort;

    private final CustomerValidation customerValidation;

    private final UserValidation userValidation;

    private final RoleValidation roleValidation;

    @Override
    public Customer update(UUID customerId, CustomerRequest request) {

        var saved = customerQueryOutputPort.findByIdAndActiveTrue(customerId)
                .orElseThrow(() -> new CustomerNotFoundCustomException(customerId));

        customerValidation.checkDuplicateEmail(customerId, request.email());
        userValidation.checkDuplicateUsername(customerId, request.user().username());
        var roleDto = roleValidation.getOrCreateRole(request.user().role());

        saved.setName(request.name());
        saved.setEmail(request.email());
        saved.getUser().setUsername(request.user().username());
        saved.getUser().setPassword(request.user().password());
        saved.getUser().setRole(Permissao.create(roleDto.id(), roleDto.name()));

        return customerSaveOutputPort.save(saved);
    }
}
