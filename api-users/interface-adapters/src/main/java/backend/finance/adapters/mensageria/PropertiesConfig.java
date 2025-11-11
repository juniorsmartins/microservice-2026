package backend.finance.adapters.mensageria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class PropertiesConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    public String bootstrapServers;

    @Value(value = "${spring.kafka.topic.event-create-customer}")
    public String topicEventCreateCustomer;

    @Value("${spring.kafka.properties.schema.registry.url}")
    public String schemaRegistryUrl;

    @Value("${spring.kafka.properties.specific.avro.reader}")
    public boolean specificAvroReader;

    @Value("${spring.kafka.properties.auto.register.schemas}")
    public boolean autoRegisterSchemas;

    @Value("${spring.kafka.consumer.group-id}")
    public String consumerGroupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    public String consumerAutoOffsetReset;

    @Value("${spring.kafka.properties.enable.idempotence}")
    public boolean enableIdempotence;

    @Value("${spring.kafka.properties.max.in.flight.requests.per.connection}")
    public int maxInFlightRequestsPerConnection;
}
