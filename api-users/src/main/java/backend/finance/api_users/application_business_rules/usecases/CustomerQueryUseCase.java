package backend.finance.api_users.application_business_rules.usecases;

import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;
import backend.finance.api_users.application_business_rules.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.application_business_rules.ports.input.CustomerQueryInputPort;
import backend.finance.api_users.application_business_rules.ports.output.CustomerQueryOutputPort;
import backend.finance.api_users.interface_adapters.presenters.CustomerPresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerQueryUseCase implements CustomerQueryInputPort {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    private final CustomerPresenter customerPresenter;

    @Override
    public CustomerResponse findByIdAndActiveTrue(UUID id) {
        return customerQueryOutputPort.findByIdAndActiveTrue(id)
                .map(customerPresenter::toResponse)
                .orElseThrow(() -> new CustomerNotFoundCustomException(id));
    }
}
