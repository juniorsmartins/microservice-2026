package backend.finance.api_user.application.usecases;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.entities.Customer;
import backend.finance.api_user.domain.entities.Permissao;
import backend.finance.api_user.domain.entities.Usuario;
import backend.finance.api_user.domain.validation.CustomerValidation;
import backend.finance.api_user.domain.validation.UserValidation;
import backend.finance.api_user.infrastructure.ports.input.CustomerCreateInputPort;
import backend.finance.api_user.domain.validation.RoleValidation;
import backend.finance.api_user.infrastructure.ports.output.CustomerSaveOutputPort;
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
    public CustomerDto create(CustomerRequest request) {

        customerValidation.checkDuplicateEmail(null, request.email());
        userValidation.checkDuplicateUsername(null, request.user().username());

        var roleDto = roleValidation.getOrCreateRole(request.user().role());
        var permissao = Permissao.create(roleDto.id(), roleDto.name());
        var usuario = Usuario.create(request.user().username(), request.user().password(), permissao);
        var customer = Customer.create(null, request.name(), request.email(), usuario);

        return customerSaveOutputPort.save(customer);
    }
}
