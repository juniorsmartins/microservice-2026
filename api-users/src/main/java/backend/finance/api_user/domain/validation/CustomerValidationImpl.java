package backend.finance.api_user.domain.validation;

import backend.finance.api_user.application.configs.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.infrastructure.ports.output.CustomerQueryOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerValidationImpl implements CustomerValidation {

    private final CustomerQueryOutputPort customerQueryOutputPort;

    @Override
    public void checkDuplicateEmail(UUID customerId, CustomerRequest request) {
        var email = request.email();
        customerQueryOutputPort.findByEmail(request.email())
                .ifPresent(customerDto -> {
                    if (customerId == null || !customerId.equals(customerDto.id())) {
                        throw new EmailConflictRulesCustomException(email);
                    }
                });
    }
}
