package backend.finance.adapters.mensageria.producer;

import backend.finance.adapters.CustomerMessage;
import backend.finance.adapters.mensageria.PropertiesBaseConfig;
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
public final class KafkaCustomerEventPublisher implements CustomerEventPublisherOutputPort {

    private final KafkaTemplate<String, CustomerMessage> kafkaTemplate;

    private final PropertiesBaseConfig propertiesBaseConfig;

    private final CustomerPresenter customerPresenter;

    @Override
    public void sendEventCreateCustomer(CustomerResponse response) {

        var message = customerPresenter.toMessage(response);

        kafkaTemplate.send(propertiesBaseConfig.topicEventCreateCustomer, message.getId(), message)
                .whenComplete((result, exception) -> { // whenComplete é Callback
                    if (exception == null) {
                        log.info("\n\n ----- Metadados ----- \n" +
                                "Topic: " + result.getRecordMetadata().topic() + "\n" +
                                "Partition: " + result.getRecordMetadata().partition() + "\n" +
                                "Offset: " + result.getRecordMetadata().offset() + "\n" +
                                "Timestamp: " + result.getRecordMetadata().timestamp() + "\n" +
                                "sendEventCreateCustomer - Mensagem enviada: " + result.getProducerRecord().value() + "\n\n");
                    } else {
                        log.error("Erro durante a produção: ", exception);
                    }
                });
    }
}
