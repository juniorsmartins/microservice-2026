package backend.finance.api_user.application.configs.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {

    private final KafkaPropertiesConfig kafkaPropertiesConfig; // Injetar a configuração de propriedades do Kafka

    @Bean
    public NewTopic criarTopicoEventoCreateCustomer() {
        return montarTopico(kafkaPropertiesConfig.topicoEventoCreateCustomer, 1, (short) 1);
    }

    private NewTopic montarTopico(String nome, int numReplicas, short numParticoes) {
        return TopicBuilder
                .name(nome)
                .replicas(numReplicas)
                .partitions(numParticoes)
                .build();
    }
}
