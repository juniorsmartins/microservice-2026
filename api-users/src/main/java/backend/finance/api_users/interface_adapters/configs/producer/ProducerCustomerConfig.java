package backend.finance.api_users.interface_adapters.configs.producer;

import backend.finance.api_users.CustomerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class ProducerCustomerConfig {

    private final ProducerBaseConfig producerBaseConfig;

    @Bean
    public ProducerFactory<String, CustomerMessage> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerBaseConfig.producerConfigs()); // Criar a fábrica de produtores
    }

    @Bean
    public KafkaTemplate<String, CustomerMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory()); // Criar o KafkaTemplate
    }
}
