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

    private final PropertiesConfig propertiesConfig; // Injetar a configuração de propriedades do Kafka

    private final PropertiesConsumerConfig propertiesConsumerConfig;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(); // Um HashMap simples para armazenar as configurações no formato chave → valor.
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.bootstrapServers); // Servidor Kafka - Define onde está rodando

        // Deserializers
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, propertiesConsumerConfig.keyDeserializer); // A chave da mensagem será desserializada como String
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, propertiesConsumerConfig.valueDeserializer); // Indica que o produtor envia dados serializados com Avro e registrados no Schema Registry.

        // === TUNING 2025 ===
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, propertiesConsumerConfig.requestTimeoutMs); // Evita timeout falso com rede ruim (padrão é 30000)
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, propertiesConsumerConfig.maxPollRecords); //

        // Grupo e comportamento
        props.put(ConsumerConfig.GROUP_ID_CONFIG, propertiesConsumerConfig.groupId); // Identificador único do grupo de consumidores. Consumers com o mesmo group.id formam um grupo que divide o trabalho (uma partição → um consumer por grupo). O coordinator usa isso para atribuir partições e armazenar offsets no tópico interno __consumer_offsets.
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertiesConsumerConfig.autoOffsetReset); // Define o comportamento: "earliest" → lê desde o início do tópico; "latest" → lê apenas novas mensagens; "none" → lança exceção se não houver offset.
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, propertiesConsumerConfig.enableAutoCommit); //
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, propertiesConsumerConfig.isolationLevel); //

        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, propertiesConsumerConfig.partitionAssignmentStrategy); // Estratégia de atribuição de partição. É um protocolo de reequilíbrio de partição dentro do grupo de consumidores.

        // Schema Registry
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, propertiesConsumerConfig.schemaRegistryUrl); // Aponta o Schema Registry. Necessário para o KafkaAvroDeserializer baixar o schema e desserializar Avro corretamente.
        props.put("specific.avro.reader", propertiesConsumerConfig.specificAvroReader); // Habilita o Specific Record do Avro (em vez de GenericRecord). Se true, espera que você use classes geradas pelo Avro (ex: User.java gerado a partir de .avsc). Muito útil para tipagem forte.

        return props;
    }
}
