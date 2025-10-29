package backend.finance.api_users.application.usecases;

import backend.finance.api_users.application.dtos.input.CustomerRequest;
import backend.finance.api_users.domain.entities.Customer;
import backend.finance.api_users.domain.entities.Permissao;
import backend.finance.api_users.domain.entities.Usuario;
import backend.finance.api_users.domain.validation.CustomerValidation;
import backend.finance.api_users.domain.validation.RoleValidation;
import backend.finance.api_users.domain.validation.UserValidation;
import backend.finance.api_users.infrastructure.ports.input.CustomerUpdateInputPort;
import backend.finance.api_users.infrastructure.ports.output.CustomerUpdateOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerUpdateUseCase implements CustomerUpdateInputPort {

    private final CustomerUpdateOutputPort customerUpdateOutputPort;

    private final CustomerValidation customerValidation;

    private final UserValidation userValidation;

    private final RoleValidation roleValidation;

    @Override
    public Customer update(UUID customerId, CustomerRequest request) {

        customerValidation.checkDuplicateEmail(customerId, request.email());
        userValidation.checkDuplicateUsername(customerId, request.user().username());

        var roleDto = roleValidation.getOrCreateRole(request.user().role());
        var permissao = Permissao.create(roleDto.id(), roleDto.name());
        var usuario = Usuario.create(null, request.user().username(), request.user().password(), permissao, true);
        var customer = Customer.create(customerId, request.name(), request.email(), usuario, true);

        return customerUpdateOutputPort.update(customer);
    }
}
