package backend.finance.adapters.configs;

import backend.finance.application.usecases.CustomerCreateUseCase;
import backend.finance.application.usecases.CustomerDisableUseCase;
import backend.finance.application.usecases.CustomerQueryUseCase;
import backend.finance.application.usecases.CustomerUpdateUseCase;
import backend.finance.application.validation.CustomerValidationImpl;
import backend.finance.application.validation.RoleValidationImpl;
import backend.finance.application.validation.UserValidationImpl;
import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.core.env.Environment;

public class RegisterBeanRegistrar implements BeanRegistrar {

    @Override
    public void register(BeanRegistry registry, Environment env) {

        // UseCase
        registry.registerBean("customerCreateUseCase", CustomerCreateUseCase.class,
                spec -> spec.description("Serviço de criar Customer."));

        registry.registerBean("customerUpdateUseCase", CustomerUpdateUseCase.class,
                spec -> spec.description("Serviço de atualizar Customer."));

        registry.registerBean("customerDisableUseCase", CustomerDisableUseCase.class,
                spec -> spec.description("Serviço de desativar Customer."));

        registry.registerBean("customerQueryUseCase", CustomerQueryUseCase.class,
                spec -> spec.description("Serviços de consultar Customer."));

        // Validation
        registry.registerBean("customerValidation", CustomerValidationImpl.class,
                spec -> spec.description("Serviço de validar Customer."));

        registry.registerBean("userValidation", UserValidationImpl.class,
                spec -> spec.description("Serviço de validar User."));

        registry.registerBean("roleValidation", RoleValidationImpl.class,
                spec -> spec.description("Serviço de validar Role."));
    }
}
