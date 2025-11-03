package backend.finance.api_users.application_business_rules.usecases;

import backend.finance.api_users.application_business_rules.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.application_business_rules.ports.input.CustomerQueryInputPort;
import backend.finance.api_users.application_business_rules.ports.output.CustomerQueryOutputPort;
import backend.finance.api_users.enterprise_business_rules.entities.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerQueryUseCase implements CustomerQueryInputPort {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    @Override
    public Customer findByIdAndActiveTrue(UUID id) {
        return customerQueryOutputPort.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new CustomerNotFoundCustomException(id));
    }
}
