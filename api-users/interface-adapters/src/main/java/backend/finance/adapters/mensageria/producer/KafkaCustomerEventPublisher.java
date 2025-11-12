package backend.finance.adapters.mensageria.producer;

import backend.finance.adapters.CustomerMessage;
import backend.finance.adapters.mensageria.PropertiesConfig;
import backend.finance.adapters.presenters.CustomerPresenter;
import backend.finance.application.dtos.response.CustomerResponse;
import backend.finance.application.ports.output.CustomerEventPublisherOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public final class KafkaCustomerEventPublisher implements CustomerEventPublisherOutputPort {

    private final KafkaTemplate<String, CustomerMessage> kafkaTemplate;

    private final PropertiesConfig propertiesConfig;

    private final CustomerPresenter customerPresenter;

    @Override
    public void sendEventCreateCustomer(CustomerResponse response) {

        var message = customerPresenter.toMessage(response);
        kafkaTemplate.send(propertiesConfig.topicEventCreateCustomer, UUID.randomUUID().toString(), message);
        log.info("\n\n KafkaCustomerEventPublisher - Mensagem enviada: {}. \n\n", message);
    }
}
