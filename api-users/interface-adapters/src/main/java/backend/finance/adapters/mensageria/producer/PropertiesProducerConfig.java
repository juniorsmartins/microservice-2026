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

    @Value("${spring.kafka.producer.enable.idempotence}")
    public boolean enableIdempotence;

    @Value("${spring.kafka.producer.max.in.flight.requests.per.connection}")
    public int maxInFlightRequestsPerConnection;
}
