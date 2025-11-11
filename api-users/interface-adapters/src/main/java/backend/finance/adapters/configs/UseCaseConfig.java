package backend.finance.adapters.configs;

import backend.finance.application.mappers.CustomerMapper;
import backend.finance.application.ports.input.CustomerCreateInputPort;
import backend.finance.application.ports.input.CustomerDisableInputPort;
import backend.finance.application.ports.input.CustomerQueryInputPort;
import backend.finance.application.ports.input.CustomerUpdateInputPort;
import backend.finance.application.ports.output.CustomerEventPublisherOutputPort;
import backend.finance.application.ports.output.CustomerQueryOutputPort;
import backend.finance.application.ports.output.CustomerSaveOutputPort;
import backend.finance.application.usecases.CustomerCreateUseCase;
import backend.finance.application.usecases.CustomerDisableUseCase;
import backend.finance.application.usecases.CustomerQueryUseCase;
import backend.finance.application.usecases.CustomerUpdateUseCase;
import backend.finance.application.validation.CustomerValidation;
import backend.finance.application.validation.RoleValidation;
import backend.finance.application.validation.UserValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CustomerCreateInputPort customerCreateUseCase(
            CustomerSaveOutputPort customerSaveOutputPort,
            CustomerValidation customerValidation,
            UserValidation userValidation,
            RoleValidation roleValidation,
            CustomerEventPublisherOutputPort eventPublisher,
            CustomerMapper customerMapper
    ) {
        return new CustomerCreateUseCase(customerSaveOutputPort, customerValidation, userValidation, roleValidation,
                eventPublisher, customerMapper);
    }

    @Bean
    public CustomerUpdateInputPort customerUpdateUseCase(CustomerQueryOutputPort customerQueryOutputPort,
                                                         CustomerSaveOutputPort customerSaveOutputPort,
                                                         CustomerValidation customerValidation,
                                                         UserValidation userValidation,
                                                         RoleValidation roleValidation,
                                                         CustomerMapper customerMapper) {
        return new CustomerUpdateUseCase(customerQueryOutputPort, customerSaveOutputPort, customerValidation,
                userValidation, roleValidation, customerMapper);
    }

    @Bean
    public CustomerDisableInputPort customerDisableUseCase(CustomerQueryOutputPort customerQueryOutputPort,
                                                           CustomerSaveOutputPort customerSaveOutputPort,
                                                           CustomerMapper customerMapper) {
        return new CustomerDisableUseCase(customerQueryOutputPort, customerSaveOutputPort, customerMapper);
    }

    @Bean
    public CustomerQueryInputPort customerQueryUseCase(CustomerQueryOutputPort customerQueryOutputPort,
                                                       CustomerMapper customerMapper) {
        return new CustomerQueryUseCase(customerQueryOutputPort, customerMapper);
    }
}
