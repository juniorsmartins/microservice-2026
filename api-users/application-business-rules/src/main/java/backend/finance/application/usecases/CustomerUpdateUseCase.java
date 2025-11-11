package backend.finance.application.usecases;

import backend.finance.application.dtos.request.CustomerRequest;
import backend.finance.application.dtos.response.CustomerResponse;
import backend.finance.application.exceptions.http404.CustomerNotFoundCustomException;
import backend.finance.application.mappers.CustomerMapper;
import backend.finance.application.ports.input.CustomerUpdateInputPort;
import backend.finance.application.ports.output.CustomerQueryOutputPort;
import backend.finance.application.ports.output.CustomerSaveOutputPort;
import backend.finance.application.validation.CustomerValidation;
import backend.finance.application.validation.RoleValidation;
import backend.finance.application.validation.UserValidation;
import backend.finance.enterprise.entities.Permissao;
import backend.finance.enterprise.enums.RoleEnum;

import java.util.UUID;

public class CustomerUpdateUseCase implements CustomerUpdateInputPort {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    private final CustomerSaveOutputPort customerSaveOutputPort;

    private final CustomerValidation customerValidation;

    private final UserValidation userValidation;

    private final RoleValidation roleValidation;

    private final CustomerMapper customerMapper;

    public CustomerUpdateUseCase(CustomerQueryOutputPort customerQueryOutputPort,
                                 CustomerSaveOutputPort customerSaveOutputPort, CustomerValidation customerValidation,
                                 UserValidation userValidation, RoleValidation roleValidation,
                                 CustomerMapper customerMapper) {
        this.customerQueryOutputPort = customerQueryOutputPort;
        this.customerSaveOutputPort = customerSaveOutputPort;
        this.customerValidation = customerValidation;
        this.userValidation = userValidation;
        this.roleValidation = roleValidation;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerResponse update(UUID customerId, CustomerRequest request) {

        var saved = customerQueryOutputPort.findActiveById(customerId)
                .map(customerMapper::toEntity)
                .orElseThrow(() -> new CustomerNotFoundCustomException(customerId));

        customerValidation.checkDuplicateEmail(customerId, request.email());
        userValidation.checkDuplicateUsername(customerId, request.user().username());

        var roleDto = roleValidation.getOrCreateRole(request.user().role());
        var permissao = Permissao.create(roleDto.id(), RoleEnum.valueOf(roleDto.name()));

        saved.setName(request.name());
        saved.setEmail(request.email());
        saved.getUser().setUsername(request.user().username());
        saved.getUser().setPassword(request.user().password());
        saved.getUser().setRole(permissao);

        var customerDto = customerMapper.toDto(saved);
        var customerDtoSaved = customerSaveOutputPort.save(customerDto);
        return customerMapper.toResponse(customerDtoSaved);
    }
}

//var dtoSaved = customerQueryOutputPort.findByIdAndActiveTrue(customerId)
//        .orElseThrow(() -> new CustomerNotFoundCustomException(customerId));
//
//        customerValidation.checkDuplicateEmail(customerId, request.email());
//        userValidation.checkDuplicateUsername(customerId, request.user().username());
//
//var roleDto = roleValidation.getOrCreateRole(request.user().role());
//var permissao = Permissao.create(roleDto.id(), roleDto.name());
//var usuario = Usuario.create(dtoSaved.user().id(), request.user().username(), request.user().password(), permissao, dtoSaved.user().active());
//var customer = Customer.create(dtoSaved.id(), request.name(), request.email(), usuario, dtoSaved.active());
//
//var customerDto = customerMapper.toDto(customer);
//var customerSaved = customerSaveOutputPort.save(customerDto);
//        return customerMapper.toResponse(customerSaved);