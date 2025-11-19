package backend.finance.adapters.mensageria.producer;

import backend.finance.adapters.mensageria.PropertiesConfig;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class ProducerBaseConfig {

    private final PropertiesConfig propertiesConfig; // Injetar a configuração de propriedades do Kafka

    private final PropertiesProducerConfig propertiesProducerConfig;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.bootstrapServers); // Servidor Kafka (obrigatório)
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, propertiesProducerConfig.schemaRegistryUrl);
        props.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, propertiesProducerConfig.autoRegisterSchemas);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, propertiesProducerConfig.keySerializer); // Para serializar chaves (obrigatório)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, propertiesProducerConfig.valueSerializer); // Para serializar mensagens - pode ser string, json, avro (obrigatório)
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, propertiesProducerConfig.compressionType);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, propertiesProducerConfig.enableIdempotence); // Habilita idempotência (necessário para evitar duplicatas em retries)
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, propertiesProducerConfig.maxInFlightRequestsPerConnection); // Permitir múltiplas requisições em voo para maior throughput (padrão 5)
        props.put(ProducerConfig.ACKS_CONFIG, propertiesProducerConfig.acks);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, propertiesProducerConfig.batchSize); // Tamanho máximo (em bytes) que o produtor tenta juntar em um único batch antes de enviar pro broker.
        props.put(ProducerConfig.LINGER_MS_CONFIG, propertiesProducerConfig.lingerMs); // Espera até 5ms pra encher o batch - obrigatório usar com batch-size.
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, propertiesProducerConfig.deliveryTimeoutMs); // É o tempo máximo que o produtor vai ficar tentando entregar UMA mensagem antes de desistir e jogar exceção (Mesmo que tenha milhões de retries, depois de X tempo no total o produtor desiste e joga exceção). Ele controla o ciclo de vida da mensagem, incluindo: Todas as tentativas de retry; Espera no batch (linger.ms); Tempo de compressão; Tempo de envio pela rede; Espera pelo ack do broker.
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, propertiesProducerConfig.requestTimeoutMs); // Especifica quanto tempo máximo por cada tentativa de enviar a mensagem.
        return props;
    }
}
