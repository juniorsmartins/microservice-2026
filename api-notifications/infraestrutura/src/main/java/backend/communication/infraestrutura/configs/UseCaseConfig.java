package backend.communication.infraestrutura.configs;

import backend.communication.aplicacao.mappers.NotificationMapper;
import backend.communication.aplicacao.ports.output.EmailOutputPort;
import backend.communication.aplicacao.ports.output.NotificationFindOutputPort;
import backend.communication.aplicacao.ports.output.NotificationSaveOutputPort;
import backend.communication.aplicacao.usecases.EmailEventCustomerCreatedUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public EmailEventCustomerCreatedUseCase emailEventCustomerCreatedUseCase(
            NotificationSaveOutputPort notificationSaveOutputPort, NotificationFindOutputPort notificationFindOutputPort,
            NotificationMapper notificationMapper, EmailOutputPort emailOutputPort) {
        return new EmailEventCustomerCreatedUseCase(notificationSaveOutputPort, notificationFindOutputPort,
                notificationMapper, emailOutputPort);
    }
}
