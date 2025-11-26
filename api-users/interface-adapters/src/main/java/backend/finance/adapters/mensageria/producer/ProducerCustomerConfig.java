package backend.finance.adapters.mensageria.producer;

import backend.finance.adapters.CustomerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ProducerCustomerConfig {

    private final Map<String, Object> producerConfigs;

    @Bean
    public ProducerFactory<String, CustomerMessage> producerFactoryCustomerMessage() {
        return new DefaultKafkaProducerFactory<>(producerConfigs); // Criar a fábrica de produtores
    }

    @Bean
    public KafkaTemplate<String, CustomerMessage> kafkaTemplateCustomerMessage(ProducerFactory<String, CustomerMessage> producerFactoryCustomerMessage) {
        return new KafkaTemplate<>(producerFactoryCustomerMessage); // Criar o KafkaTemplate
    }
}
