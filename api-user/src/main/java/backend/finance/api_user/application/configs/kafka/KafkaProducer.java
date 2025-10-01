package backend.finance.api_user.application.configs.kafka;

import backend.finance.api_user.application.dtos.output.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public final class KafkaProducer {

    private final KafkaTemplate<String, CustomerResponse> kafkaTemplate;

    private final KafkaPropertiesConfig kafkaPropertiesConfig;

    public void enviarEvento(CustomerResponse customerResponse) {
        kafkaTemplate.send(kafkaPropertiesConfig.topicoEventoCreateCustomer, UUID.randomUUID().toString(), customerResponse);
        log.info("\n\n KafkaProducer - Mensagem enviada ao tópico de eventos de consulta: {}. \n\n", customerResponse);
    }
}
