package backend.finance.application.usecases;

import backend.finance.application.dtos.request.CustomerRequest;
import backend.finance.application.dtos.response.CustomerResponse;
import backend.finance.application.exceptions.http400.EnterpriseValidationException;
import backend.finance.application.mappers.CustomerMapper;
import backend.finance.application.ports.input.CustomerCreateInputPort;
import backend.finance.application.ports.output.CustomerEventPublisherOutputPort;
import backend.finance.application.ports.output.CustomerSaveOutputPort;
import backend.finance.application.validation.CustomerValidation;
import backend.finance.application.validation.RoleValidation;
import backend.finance.application.validation.UserValidation;
import backend.finance.enterprise.entities.Customer;
import backend.finance.enterprise.entities.Permissao;
import backend.finance.enterprise.entities.Usuario;
import backend.finance.enterprise.enums.RoleEnum;
import backend.finance.enterprise.exceptions.BadRequestCustomException;

public class CustomerCreateUseCase implements CustomerCreateInputPort {

    private final CustomerSaveOutputPort customerSaveOutputPort;

    private final CustomerValidation customerValidation;

    private final UserValidation userValidation;

    private final RoleValidation roleValidation;

    private final CustomerEventPublisherOutputPort eventPublisher;

    private final CustomerMapper customerMapper;

    public CustomerCreateUseCase(CustomerSaveOutputPort customerSaveOutputPort, CustomerValidation customerValidation,
                                 UserValidation userValidation, RoleValidation roleValidation,
                                 CustomerEventPublisherOutputPort eventPublisher, CustomerMapper customerMapper) {
        this.customerSaveOutputPort = customerSaveOutputPort;
        this.customerValidation = customerValidation;
        this.userValidation = userValidation;
        this.roleValidation = roleValidation;
        this.eventPublisher = eventPublisher;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {

        customerValidation.checkDuplicateEmail(null, request.email());
        userValidation.checkDuplicateUsername(null, request.user().username());

        var customer = runDataValidationRules(request);

        var customerDto = customerMapper.toDto(customer);
        var dtoSaved = customerSaveOutputPort.save(customerDto);
        var response = customerMapper.toResponse(dtoSaved);
        eventPublisher.sendEventCreateCustomer(response);

        return response;
    }

    private Customer runDataValidationRules(CustomerRequest request) {

        try {
            var roleDto = roleValidation.getOrCreateRole(request.user().role());
            var permissao = Permissao.create(roleDto.id(), RoleEnum.valueOf(roleDto.name()));
            var usuario = Usuario.create(null, request.user().username(), request.user().password(), permissao, true);
            return Customer.create(null, request.name(), request.email(), usuario);

        } catch (BadRequestCustomException e) {
            throw new EnterpriseValidationException(e.getMessageKey(), e.getValue0(), e.getValue1());
        }
    }
}
