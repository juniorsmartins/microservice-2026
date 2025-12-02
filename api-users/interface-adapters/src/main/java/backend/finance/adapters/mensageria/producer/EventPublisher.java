package backend.finance.adapters.mensageria.producer;

import backend.CustomerMessage;
import backend.finance.adapters.mensageria.PropertiesConfig;
import backend.finance.adapters.presenters.CustomerPresenter;
import backend.finance.application.dtos.response.CustomerResponse;
import backend.finance.application.ports.output.CustomerEventPublisherOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public final class EventPublisher implements CustomerEventPublisherOutputPort {

    private final KafkaTemplate<String, CustomerMessage> kafkaTemplateCustomerMessage;

    private final PropertiesConfig propertiesConfig;

    private final CustomerPresenter customerPresenter;

    @Override
    public void sendEventCustomerCreated(CustomerResponse response) {

        var message = customerPresenter.toMessage(response);

        kafkaTemplateCustomerMessage.send(propertiesConfig.topicEventsCustomerCreated, message.getId(), message)
                .whenComplete((result, exception) -> { // whenComplete é Callback - será acionado sempre que uma mensagem for enviada ou lançar exceção
                    if (exception == null) {
                        log.info("\n\n ----- Metadados ----- \n" +
                                "Topic: " + result.getRecordMetadata().topic() + "\n" +
                                "Partition: " + result.getRecordMetadata().partition() + "\n" +
                                "Offset: " + result.getRecordMetadata().offset() + "\n" +
                                "Timestamp: " + result.getRecordMetadata().timestamp() + "\n" +
                                "Key: " + result.getProducerRecord().key() + "\n" +
                                "sendEventCustomerCreated - Mensagem enviada: " + result.getProducerRecord().value() + "\n\n");

                    } else {
                        log.error("sendEventCustomerCreated - Erro durante a produção: ", exception);
                    }
                });
    }
}
