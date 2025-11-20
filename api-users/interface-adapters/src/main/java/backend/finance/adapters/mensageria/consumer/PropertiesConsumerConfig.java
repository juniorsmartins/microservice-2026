package backend.finance.adapters.mensageria.consumer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class PropertiesConsumerConfig {

    @Value("${spring.kafka.consumer.key-deserializer}")
    public String keyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    public String valueDeserializer;

    @Value("${spring.kafka.consumer.group-id}")
    public String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    public String autoOffsetReset;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    public boolean enableAutoCommit;

    @Value("${spring.kafka.consumer.isolation-level}")
    public String isolationLevel;

    @Value("${spring.kafka.consumer.max-poll-records}")
    public String maxPollRecords;

    @Value("${spring.kafka.consumer.request-timeout-ms}")
    public String requestTimeoutMs;

    @Value("${spring.kafka.consumer.partition.assignment.strategy}")
    public String partitionAssignmentStrategy;

    @Value("${spring.kafka.consumer.properties.schema.registry.url}")
    public String schemaRegistryUrl;

    @Value("${spring.kafka.consumer.properties.specific.avro.reader}")
    public boolean specificAvroReader;
}
