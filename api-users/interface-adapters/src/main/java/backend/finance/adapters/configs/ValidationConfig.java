package backend.finance.adapters.configs;

import backend.finance.application.ports.output.CustomerQueryOutputPort;
import backend.finance.application.ports.output.RoleOutputPort;
import backend.finance.application.validation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {

    @Bean
    public CustomerValidation customerValidation(CustomerQueryOutputPort customerQueryOutputPort) {
        return new CustomerValidationImpl(customerQueryOutputPort);
    }

    @Bean
    public UserValidation userValidation(CustomerQueryOutputPort customerQueryOutputPort) {
        return new UserValidationImpl(customerQueryOutputPort);
    }

    @Bean
    public RoleValidation roleValidation(RoleOutputPort roleOutputPort) {
        return new RoleValidationImpl(roleOutputPort);
    }
}
