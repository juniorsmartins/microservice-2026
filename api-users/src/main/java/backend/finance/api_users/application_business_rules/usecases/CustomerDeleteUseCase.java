package backend.finance.api_users.application_business_rules.usecases;

import backend.finance.api_users.enterprise_business_rules.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.application_business_rules.ports.input.CustomerDeleteInputPort;
import backend.finance.api_users.application_business_rules.ports.output.CustomerQueryOutputPort;
import backend.finance.api_users.application_business_rules.ports.output.CustomerSaveOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerDeleteUseCase implements CustomerDeleteInputPort {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    private final CustomerSaveOutputPort customerSaveOutputPort;

    @Override
    public void disableById(UUID id) {
        customerQueryOutputPort.findByIdAndActiveTrue(id)
                .ifPresentOrElse(customer -> {
                            customer.disable();
                            customer.getUser().disable();
                            customerSaveOutputPort.save(customer);
                        },
                        () -> {
                            throw new CustomerNotFoundCustomException(id);
                        });
    }
}
