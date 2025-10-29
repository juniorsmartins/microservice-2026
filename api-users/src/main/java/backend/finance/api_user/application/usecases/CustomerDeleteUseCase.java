package backend.finance.api_user.application.usecases;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.infrastructure.ports.input.CustomerDeleteInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerQueryOutputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerSaveOutputPort;
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
