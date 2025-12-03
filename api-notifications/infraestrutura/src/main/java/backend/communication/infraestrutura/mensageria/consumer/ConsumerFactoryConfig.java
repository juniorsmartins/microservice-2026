package backend.communication.infraestrutura.mensageria.consumer;

import backend.CustomerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class ConsumerFactoryConfig {

    private final Map<String, Object> consumerConfigs;

    @Bean
    public ConsumerFactory<String, CustomerMessage> consumerFactoryCustomerMessage() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs); // Criar a fábrica de consumidores
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomerMessage> listenerContainerFactoryCustomerMessage(ConsumerFactory<String, CustomerMessage> consumerFactoryCustomerMessage) {
        ConcurrentKafkaListenerContainerFactory<String, CustomerMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryCustomerMessage);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
}
