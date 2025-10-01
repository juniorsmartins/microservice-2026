package backend.finance.api_user.application.configs.kafka;

import backend.finance.api_user.application.dtos.output.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public final class KafkaConsumer {

    @KafkaListener(topics = "${spring.kafka.topic.evento-create-customer}", groupId = "grupo-mensagem-kafka", containerFactory = "kafkaListenerContainerFactory")
    public void consumirEventoConsulta(CustomerResponse customerResponse, Acknowledgment ack) {

        try {
            log.info("\n\n KafkaConsumer - Mensagem recebida no tópico de eventos de consulta: {}. \n\n", customerResponse);
            ack.acknowledge(); // Confirmar o processamento da mensagem

        } catch (Exception e) {
            log.error("\n\n KafkaConsumer - Erro ao processar a mensagem: {}.\n\n", e.getMessage());
        }
    }
}
