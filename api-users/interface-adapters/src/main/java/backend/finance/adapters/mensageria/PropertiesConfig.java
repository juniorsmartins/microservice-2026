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

    @Value(value = "${spring.kafka.topic.events.customer-created}")
    public String topicEventsCustomerCreated;

    @Value("${spring.kafka.topic.min.insync.replicas}")
    public int minInsyncReplicas;
}
