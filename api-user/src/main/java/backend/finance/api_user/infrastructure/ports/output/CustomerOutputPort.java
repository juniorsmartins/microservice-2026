package backend.finance.api_user.infrastructure.ports.output;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;

import java.util.Optional;

public interface CustomerOutputPort {

    CustomerDto save(CustomerRequest customerRequest);

    Optional<CustomerDto> findByEmail(String email);
}
