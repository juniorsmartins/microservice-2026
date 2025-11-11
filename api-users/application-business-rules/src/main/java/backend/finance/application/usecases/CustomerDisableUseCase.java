package backend.finance.application.usecases;

import backend.finance.application.exceptions.http404.CustomerNotFoundCustomException;
import backend.finance.application.mappers.CustomerMapper;
import backend.finance.application.ports.input.CustomerDisableInputPort;
import backend.finance.application.ports.output.CustomerQueryOutputPort;
import backend.finance.application.ports.output.CustomerSaveOutputPort;

import java.util.UUID;

public class CustomerDisableUseCase implements CustomerDisableInputPort {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    private final CustomerSaveOutputPort customerSaveOutputPort;

    private final CustomerMapper customerMapper;

    public CustomerDisableUseCase(CustomerQueryOutputPort customerQueryOutputPort,
                                  CustomerSaveOutputPort customerSaveOutputPort, CustomerMapper customerMapper) {
        this.customerQueryOutputPort = customerQueryOutputPort;
        this.customerSaveOutputPort = customerSaveOutputPort;
        this.customerMapper = customerMapper;
    }

    @Override
    public void disableById(UUID id) {
        customerQueryOutputPort.findByIdAndActiveTrue(id)
                .ifPresentOrElse(dto -> {
                            var customer = customerMapper.toEntity(dto);
                            customer.disable();
                            customer.getUser().disable();
                            var dtoDisable = customerMapper.toDto(customer);
                            customerSaveOutputPort.save(dtoDisable);
                        },
                        () -> {
                            throw new CustomerNotFoundCustomException(id);
                        });
    }
}
