package backend.finance.adapters.mensageria.producer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class PropertiesProducerConfig {

    @Value("${spring.kafka.producer.key-serializer}")
    public String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    public String valueSerializer;

    //#region Exactly-once
    @Value("${spring.kafka.producer.acks}")
    public String acks;

    @Value("${spring.kafka.producer.enable.idempotence}")
    public boolean enableIdempotence;

    @Value("${spring.kafka.producer.max.in.flight.requests.per.connection}")
    public int maxInFlightRequestsPerConnection;

    @Value("${spring.kafka.producer.retries}")
    public int retries;
    //#endregion Exactly-once

    //#region Resiliência
    @Value("${spring.kafka.producer.retry.backoff.ms}")
    public int retryBackoffMs;

    @Value("${spring.kafka.producer.request.timeout.ms}")
    public int requestTimeoutMs;

    @Value("${spring.kafka.producer.delivery.timeout.ms}")
    public int deliveryTimeoutMs;
    //#endregion Resiliência

    //#region Throughput Máximo
    @Value("${spring.kafka.producer.batch-size}")
    public int batchSize;

    @Value("${spring.kafka.producer.linger-ms}")
    public String lingerMs;

    @Value("${spring.kafka.producer.compression-type}")
    public String compressionType;
    //#endregion Throughput Máximo

    //#region Schema Registry e Avro
    @Value("${spring.kafka.producer.properties.schema.registry.url}")
    public String schemaRegistryUrl;

    @Value("${spring.kafka.producer.properties.auto.register.schemas}")
    public boolean autoRegisterSchemas;
    //endregion Schema Registry e Avro
}
