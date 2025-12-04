package backend.communication.infraestrutura.mensageria.consumer;

import backend.CustomerMessage;
import backend.communication.aplicacao.ports.input.EmailEventCustomerCreatedInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public final class EventSubscriber {

    private final EmailEventCustomerCreatedInputPort emailEventCustomerCreatedInputPort;

    @KafkaListener(topics = "${spring.kafka.topic.events.customer-created}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "listenerContainerFactoryCustomerMessage")
    public void consumerEventCustomerCreated(CustomerMessage message, Acknowledgment ack) {

        try {
            log.info("\n\n consumerEventCustomerCreated - Mensagem recebida: {}. \n\n", message);
            ack.acknowledge(); // Confirmar o processamento da mensagem - faz o commit
            emailEventCustomerCreatedInputPort.create(message.getId(), message.getName(), message.getEmail());

        } catch (Exception e) {
            log.error("\n\n consumerEventCustomerCreated - Erro ao processar a mensagem: {}.\n\n", e.getMessage());
        }
    }
}
