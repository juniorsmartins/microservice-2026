package backend.finance.api_users.interface_adapters.mensageria.producer;

import backend.finance.api_users.CustomerMessage;
import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;
import backend.finance.api_users.interface_adapters.mensageria.PropertiesConfig;
import backend.finance.api_users.interface_adapters.presenters.CustomerPresenter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public final class KafkaCustomerEventPublisher implements CustomerEventPublisher {

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
