package backend.communication.infraestrutura.configs;

import backend.communication.aplicacao.mappers.NotificationMapperImpl;
import backend.communication.aplicacao.usecases.EmailEventCustomerCreatedUseCase;
import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.core.env.Environment;

public class RegisterBeanRegistrar implements BeanRegistrar {

    @Override
    public void register(BeanRegistry registry, Environment env) {

        registry.registerBean("emailEventCustomerCreatedUseCase", EmailEventCustomerCreatedUseCase.class,
                spec -> spec.description("Serviço para criar email."));

        registry.registerBean("notificationMapper", NotificationMapperImpl.class,
                spec -> spec.description("Serviço para converter notifications."));
    }
}
