package backend.finance.api_user.application.configs.mensageria.producer;

import backend.finance.api.users.CustomerMessage;
import backend.finance.api_user.application.configs.mensageria.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public final class Producer {

    private final KafkaTemplate<String, CustomerMessage> kafkaTemplate;

    private final PropertiesConfig propertiesConfig;

    public void enviarEvento(CustomerMessage customerMessage) {
        kafkaTemplate.send(propertiesConfig.topicoEventoCreateCustomer, UUID.randomUUID().toString(), customerMessage);
        log.info("\n\n Producer - Mensagem enviada: {}. \n\n", customerMessage);
    }
}
