package backend.finance.api_user.application.usecases;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.infrastructure.ports.input.CustomerDeleteInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerDeleteOutputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerQueryOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerDeleteUseCase implements CustomerDeleteInputPort {

    private final CustomerDeleteOutputPort customerDeleteOutputPort;

    private final CustomerQueryOutputPort customerQueryOutputPort;

    @Override
    public void deleteById(UUID id) {
        customerQueryOutputPort.findById(id)
                .ifPresentOrElse(customerDto -> customerDeleteOutputPort.deleteById(customerDto.id()),
                        () -> {
                            throw new CustomerNotFoundCustomException(id);
                        });
    }
}
