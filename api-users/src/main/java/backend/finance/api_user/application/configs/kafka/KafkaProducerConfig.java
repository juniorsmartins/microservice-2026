package backend.finance.api_user.application.configs.kafka;

import backend.finance.api.users.CustomerKafka;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaPropertiesConfig kafkaPropertiesConfig; // Injetar a configuração de propriedades do Kafka

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPropertiesConfig.bootstrapServers); // Servidor Kafka
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // Usar StringSerializer para serializar chaves
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class); // Usar JsonSerializer para serializar mensagens JSON
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, kafkaPropertiesConfig.schemaRegistryUrl);
        props.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, kafkaPropertiesConfig.autoRegisterSchemas);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1_000); // Permitir múltiplas requisições em voo para maior throughput
        return props;
    }

    @Bean
    public ProducerFactory<String, CustomerKafka> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs()); // Criar a fábrica de produtores
    }

    @Bean
    public KafkaTemplate<String, CustomerKafka> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory()); // Criar o KafkaTemplate
    }
}
