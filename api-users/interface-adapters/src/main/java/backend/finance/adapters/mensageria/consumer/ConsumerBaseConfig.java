package backend.finance.adapters.mensageria.consumer;

import backend.finance.adapters.mensageria.PropertiesConfig;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class ConsumerBaseConfig {

    private final PropertiesConfig propertiesConfig;

    private final PropertiesConsumerConfig propertiesConsumerConfig;

    @Bean
    public Map<String, Object> consumerConfigs() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, propertiesConsumerConfig.keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, propertiesConsumerConfig.valueDeserializer);

        // === Estratégia de Rebalance ===
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, propertiesConsumerConfig.partitionAssignmentStrategy);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, propertiesConsumerConfig.groupId);
        props.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, propertiesConsumerConfig.groupInstanceId);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, propertiesConsumerConfig.sessionTimeoutMs);

        // === Forma de leitura e garantia de entrega ===
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, propertiesConsumerConfig.isolationLevel);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertiesConsumerConfig.autoOffsetReset);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, propertiesConsumerConfig.enableAutoCommit);

        // === Resiliência ===
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, propertiesConsumerConfig.requestTimeoutMs);

        // === Performance ===
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, propertiesConsumerConfig.maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, propertiesConsumerConfig.maxPollIntervalMs);

        // === Schema Registry ===
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, propertiesConfig.schemaRegistryUrl); // Aponta o Schema Registry. Necessário para o KafkaAvroDeserializer baixar o schema e desserializar Avro corretamente.
        props.put("specific.avro.reader", propertiesConsumerConfig.specificAvroReader); // Habilita o Specific Record do Avro (em vez de GenericRecord). Se true, espera que você use classes geradas pelo Avro (ex: User.java gerado a partir de .avsc). Muito útil para tipagem forte.

        return props;
    }
}
