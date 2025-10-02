package backend.finance.api_user.application.configs.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class KafkaPropertiesConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    public String bootstrapServers;

    @Value(value = "${spring.kafka.topic.evento-create-customer}")
    public String topicoEventoCreateCustomer;

    @Value("${spring.kafka.properties.schema.registry.url}")
    public String schemaRegistryUrl;

    @Value("${spring.kafka.properties.specific.avro.reader}")
    public String specificAvroReader;

    @Value("${spring.kafka.properties.auto.register.schemas}")
    public String autoRegisterSchemas;

    @Value("${spring.kafka.consumer.group-id}")
    public String consumerGroupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    public String consumerAutoOffsetReset;
}
