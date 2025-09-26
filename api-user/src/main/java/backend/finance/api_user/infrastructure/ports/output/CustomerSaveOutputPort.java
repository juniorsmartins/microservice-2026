package backend.finance.api_user.infrastructure.ports.output;

import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.entities.Customer;

public interface CustomerSaveOutputPort {

    CustomerDto save(Customer customer);
}
