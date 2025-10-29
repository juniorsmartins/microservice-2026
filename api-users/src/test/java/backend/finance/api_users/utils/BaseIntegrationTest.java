package backend.finance.api_users.utils;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@EmbeddedKafka(partitions = 1, // Configuração do Kafka embutido para testes
        topics = {"${spring.kafka.topic.event-create-customer}"},
        brokerProperties = {"listeners=PLAINTEXT://localhost:0",
        "auto.create.topics.enable=true"})
@DirtiesContext // Para garantir que o contexto seja reiniciado entre os testes, evitando interferências
public abstract class BaseIntegrationTest {

//    @Autowired
//    private EmbeddedKafkaBroker embeddedKafka; // Necessário para inicializar o contexto Kafka

    @Bean
    public SchemaRegistryClient schemaRegistryClient() { // Necessário para fornecer mock de Schema Registry para os testes
        return new MockSchemaRegistryClient(); // Mock para testes
    }
}
