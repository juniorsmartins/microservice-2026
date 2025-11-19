package backend.finance.adapters.mensageria.consumer;

import backend.finance.adapters.CustomerMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public final class Consumer {

    @KafkaListener(topics = "${spring.kafka.topic.event-create-customer}", groupId = "group-api-users-customer", containerFactory = "listenerContainerFactoryCustomerMessage")
    public void consumerEventCreateCustomer(CustomerMessage customerMessage, Acknowledgment ack) {

        try {
            log.info("\n\n Consumer - Mensagem recebida: {}. \n\n", customerMessage);
            ack.acknowledge(); // Confirmar o processamento da mensagem

        } catch (Exception e) {
            log.error("\n\n Consumer - Erro ao processar a mensagem: {}.\n\n", e.getMessage());
        }
    }
}
