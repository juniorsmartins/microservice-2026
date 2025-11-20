package backend.finance.adapters.mensageria.consumer;

import backend.finance.adapters.CustomerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class ConsumerCustomerConfig {

    private final ConsumerBaseConfig consumerBaseConfig;

    @Bean
    public ConsumerFactory<String, CustomerMessage> consumerFactoryCustomerMessage() {
        return new DefaultKafkaConsumerFactory<>(consumerBaseConfig.consumerConfigs()); // Criar a fábrica de consumidores
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomerMessage> listenerContainerFactoryCustomerMessage() {
        ConcurrentKafkaListenerContainerFactory<String, CustomerMessage> factory = new ConcurrentKafkaListenerContainerFactory<>(); // Criar a fábrica de listeners
        factory.setConsumerFactory(consumerFactoryCustomerMessage()); // Configurar a fábrica de consumidores
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE); // Usar confirmação manual - mais controle - pode ser útil para garantir que a mensagem foi processada antes de confirmar
        return factory;
    }
}
