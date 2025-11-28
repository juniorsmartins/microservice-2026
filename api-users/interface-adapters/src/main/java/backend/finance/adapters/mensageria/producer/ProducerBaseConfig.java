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
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.bootstrapServers); // Endereço do cluster Kafka (obrigatório)
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, propertiesProducerConfig.keySerializer); // Define como serializar chaves (obrigatório)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, propertiesProducerConfig.valueSerializer); // Define como serializar o payload/mensagem - pode ser string, json, avro e etc (obrigatório)

        // === EXACTLY-ONCE (idempotence, acks e retries) ===
        props.put(ProducerConfig.ACKS_CONFIG, propertiesProducerConfig.acks); // # Define se o producer deve esperar confirmação de entrega (0 - Producer nem espera resposta. Envia e esquece / 1 - Producer espera o líder confirmar que gravou no log (padrão). / all ou -1 - Producer espera líder + todas as réplicas ISR confirmarem (ou seja, RF=3 → 3 confirmações).
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, propertiesProducerConfig.enableIdempotence); // Habilita idempotência (necessário para evitar mensagens duplicadas, incluído em retries). Funciona em conjunto com acks e retries.
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, propertiesProducerConfig.maxInFlightRequestsPerConnection); // Permitir múltiplas requisições em voo para maior throughput (padrão 5)
        props.put(ProducerConfig.RETRIES_CONFIG, propertiesProducerConfig.retries);

        // === Resiliência (funciona com Retries) ===
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, propertiesProducerConfig.retryBackoffMs); // Tempo para esperar entre as tentativas (default: 100ms)
        props.put(ProducerConfig.RETRY_BACKOFF_MAX_MS_CONFIG, propertiesProducerConfig.retryBackoffMaxMS); // A quantidade máxima de tempo em milissegundos para esperar ao tentar novamente uma solicitação ao corretor que falhou repetidamente (padrão: 1000).
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, propertiesProducerConfig.requestTimeoutMs); // Especifica o tempo máximo por cada tentativa de enviar um batch. Espera a resposta do broker em cada retry.
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, propertiesProducerConfig.deliveryTimeoutMs); // É o tempo máximo geral que o produtor vai ficar tentando entregar UMA mensagem antes de desistir e jogar exceção (Mesmo que tenha milhões de retries, depois de X tempo no total o produtor desiste e joga exceção). Ele controla o ciclo de vida da mensagem, incluindo: Todas as tentativas de retry; Espera no batch (linger.ms); Tempo de compressão; Tempo de envio pela rede; Espera pelo ack do broker.

        // === THROUGHPUT MÁXIMO ===
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, propertiesProducerConfig.batchSize); // Tamanho máximo (em bytes) que o produtor tenta juntar em um único batch antes de enviar para o broker. Junta várias mensagens num pacote só.
        props.put(ProducerConfig.LINGER_MS_CONFIG, propertiesProducerConfig.lingerMs); // Define quanto temo espera até encher o batch (usar obrigatório com batch-size)
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, propertiesProducerConfig.compressionType); // Define como a mensagem será comprimida antes de enviar (none, gzip, snappy, lz4, zstd). Isso reduz o tamanho.

        // === MENSAGEM AVRO ===
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, propertiesConfig.schemaRegistryUrl); // Endereço do esquema registry (obrigatório se usar Avro)
        props.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, propertiesProducerConfig.autoRegisterSchemas); // Registra o esquema Avro automático

        return props;
    }
}
