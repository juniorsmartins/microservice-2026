package backend.finance.application.usecases;

import backend.finance.application.dtos.response.CustomerResponse;
import backend.finance.application.exceptions.http404.CustomerNotFoundCustomException;
import backend.finance.application.mappers.CustomerMapper;
import backend.finance.application.ports.input.CustomerQueryInputPort;
import backend.finance.application.ports.output.CustomerQueryOutputPort;

import java.util.UUID;

public class CustomerQueryUseCase implements CustomerQueryInputPort {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    private final CustomerMapper customerMapper;

    public CustomerQueryUseCase(CustomerQueryOutputPort customerQueryOutputPort, CustomerMapper customerMapper) {
        this.customerQueryOutputPort = customerQueryOutputPort;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerResponse findByIdAndActiveTrue(UUID id) {
        return customerQueryOutputPort.findActiveById(id)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new CustomerNotFoundCustomException(id));
    }
}
