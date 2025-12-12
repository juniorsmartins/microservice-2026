package backend.finance.adapters.utils;

import backend.finance.adapters.repositories.CustomerRepository;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@EmbeddedKafka(partitions = 3, // Configuração do Kafka embutido para testes
        topics = {"${spring.kafka.topic.events.customer-created}"},
        brokerProperties = {
        "auto.create.topics.enable=true"
        }
)
@DirtiesContext // Para garantir que o contexto seja reiniciado entre os testes, evitando interferências
public abstract class KafkaAvroIntegrationTest {

    @Autowired
    protected CustomerRepository customerRepository;

    @BeforeEach
    void cleanDatabaseBefore() {
        customerRepository.deleteAll();
    }

    @AfterEach
    void cleanDatabaseAfter() {
        customerRepository.deleteAll();
    }

    @Bean
    public SchemaRegistryClient schemaRegistryClient() { // Necessário para fornecer mock de Schema Registry para os testes
        return new MockSchemaRegistryClient(); // Mock para testes
    }
}
