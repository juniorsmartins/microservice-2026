package backend.finance.adapters.mensageria;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class TopicsConfig {

    private final PropertiesConfig propertiesConfig; // Injetar a configuração de propriedades do Kafka

    @Bean
    public NewTopic upTopicEventCreateCustomer() {
        return buildTopic(propertiesConfig.topicEventCreateCustomer, 1, (short) 1);
    }

    private NewTopic buildTopic(String nome, int numReplicas, short numParticoes) {
        return TopicBuilder
                .name(nome)
                .replicas(numReplicas)
                .partitions(numParticoes)
                .build();
    }
}
