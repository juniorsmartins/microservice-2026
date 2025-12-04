package backend.communication.infraestrutura.configs;

import backend.communication.aplicacao.mappers.NotificationMapper;
import backend.communication.aplicacao.mappers.NotificationMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public NotificationMapper notificationMapper() {
        return new NotificationMapperImpl();
    }
}
