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

    @Value("${spring.kafka.producer.compression-type}")
    public String compressionType;

    //#region Exactly-once
    @Value("${spring.kafka.producer.acks}")
    public String acks;

    @Value("${spring.kafka.producer.enable.idempotence}")
    public boolean enableIdempotence;

    @Value("${spring.kafka.producer.max.in.flight.requests.per.connection}")
    public int maxInFlightRequestsPerConnection;
    //#endregion Exactly-once

    @Value("${spring.kafka.producer.batch-size}")
    public int batchSize;

    @Value("${spring.kafka.producer.linger-ms}")
    public String lingerMs;

    @Value("${spring.kafka.producer.delivery.timeout.ms}")
    public String deliveryTimeoutMs;

    @Value("${spring.kafka.producer.request.timeout.ms}")
    public String requestTimeoutMs;

    @Value("${spring.kafka.producer.properties.schema.registry.url}")
    public String schemaRegistryUrl;

    @Value("${spring.kafka.producer.properties.auto.register.schemas}")
    public boolean autoRegisterSchemas;
}
