package backend.finance.adapters.mensageria.producer;

import backend.finance.adapters.mensageria.PropertiesConfig;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class ProducerBaseConfig {

    private final PropertiesConfig propertiesConfig; // Injetar a configuração de propriedades do Kafka

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.bootstrapServers); // Servidor Kafka
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // Usar StringSerializer para serializar chaves
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class); // Usar JsonSerializer para serializar mensagens JSON
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, propertiesConfig.schemaRegistryUrl);
        props.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, propertiesConfig.autoRegisterSchemas);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, propertiesConfig.enableIdempotence); // Habilita idempotência (necessário para evitar duplicatas em retries)
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, propertiesConfig.maxInFlightRequestsPerConnection); // Permitir múltiplas requisições em voo para maior throughput (padrão 5)
        return props;
    }
}
