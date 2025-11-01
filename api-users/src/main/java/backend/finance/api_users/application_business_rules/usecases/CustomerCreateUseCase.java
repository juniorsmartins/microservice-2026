package backend.finance.api_users.application_business_rules.usecases;

import backend.finance.api_users.application_business_rules.dtos.input.CustomerRequest;
import backend.finance.api_users.application_business_rules.ports.input.CustomerCreateInputPort;
import backend.finance.api_users.application_business_rules.ports.output.CustomerSaveOutputPort;
import backend.finance.api_users.enterprise_business_rules.entities.Customer;
import backend.finance.api_users.enterprise_business_rules.entities.Permissao;
import backend.finance.api_users.enterprise_business_rules.entities.Usuario;
import backend.finance.api_users.enterprise_business_rules.validation.CustomerValidation;
import backend.finance.api_users.enterprise_business_rules.validation.RoleValidation;
import backend.finance.api_users.enterprise_business_rules.validation.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCreateUseCase implements CustomerCreateInputPort {

    private final CustomerSaveOutputPort customerSaveOutputPort;

    private final CustomerValidation customerValidation;

    private final UserValidation userValidation;

    private final RoleValidation roleValidation;

    @Override
    public Customer create(CustomerRequest request) {

        customerValidation.checkDuplicateEmail(null, request.email());
        userValidation.checkDuplicateUsername(null, request.user().username());

        var roleDto = roleValidation.getOrCreateRole(request.user().role());
        var permissao = Permissao.create(roleDto.id(), roleDto.name());
        var usuario = Usuario.create(null, request.user().username(), request.user().password(), permissao, true);
        var customer = Customer.create(null, request.name(), request.email(), usuario, true);

        return customerSaveOutputPort.save(customer);
    }
}
