package backend.finance.api_user.application.configs.kafka;

import backend.finance.api.user.CustomerKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public final class KafkaConsumer {

    @KafkaListener(topics = "${spring.kafka.topic.evento-create-customer}", groupId = "group-api-user-customer", containerFactory = "kafkaListenerContainerFactory")
    public void consumirEventoConsulta(CustomerKafka customerKafka, Acknowledgment ack) {

        try {
            log.info("\n\n KafkaConsumer - Mensagem recebida no tópico de eventos: {}. \n\n", customerKafka);
            ack.acknowledge(); // Confirmar o processamento da mensagem

        } catch (Exception e) {
            log.error("\n\n KafkaConsumer - Erro ao processar a mensagem: {}.\n\n", e.getMessage());
        }
    }
}
