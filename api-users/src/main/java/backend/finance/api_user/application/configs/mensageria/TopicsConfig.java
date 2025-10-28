package backend.finance.api_user.application.configs.mensageria;

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
    public NewTopic criarTopicoEventoCreateCustomer() {
        return montarTopico(propertiesConfig.topicoEventoCreateCustomer, 1, (short) 1);
    }

    private NewTopic montarTopico(String nome, int numReplicas, short numParticoes) {
        return TopicBuilder
                .name(nome)
                .replicas(numReplicas)
                .partitions(numParticoes)
                .build();
    }
}
