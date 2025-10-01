package backend.finance.api_user.utils;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@EmbeddedKafka(partitions = 1,
        topics = {"evento-create-customer"},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}) // Configuração do Kafka embutido para testes
@DirtiesContext // Para garantir que o contexto seja reiniciado entre os testes, evitando interferências
public abstract class BaseIntegrationTest {

//    @Autowired
//    private EmbeddedKafkaBroker embeddedKafka; // Necessário para inicializar o contexto Kafka
}
