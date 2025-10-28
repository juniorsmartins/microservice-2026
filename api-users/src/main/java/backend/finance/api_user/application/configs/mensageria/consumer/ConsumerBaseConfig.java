package backend.finance.api_user.application.configs.mensageria.consumer;

import backend.finance.api_user.application.configs.mensageria.PropertiesConfig;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class ConsumerBaseConfig {

    private final PropertiesConfig propertiesConfig; // Injetar a configuração de propriedades do Kafka

    @Bean // A anotação Bean indica que o métdo retorna um objeto que será gerenciado pelo container Spring
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(); // Um HashMap simples para armazenar as configurações no formato chave → valor.
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.bootstrapServers); // Servidor Kafka - Define onde está rodando
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // A chave da mensagem será desserializada como String
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class); // Indica que o produtor envia dados serializados com Avro e registrados no Schema Registry.
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, propertiesConfig.consumerGroupId); // Define o grupo de consumidores
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertiesConfig.consumerAutoOffsetReset); // Define o comportamento: "earliest" → lê desde o início do tópico; "latest" → lê apenas novas mensagens; "none" → lança exceção se não houver offset.
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, propertiesConfig.schemaRegistryUrl); // Aponta o Schema Registry. Necessário para o KafkaAvroDeserializer baixar o schema e desserializar Avro corretamente.
        props.put("specific.avro.reader", propertiesConfig.specificAvroReader); // Habilita o Specific Record do Avro (em vez de GenericRecord). Se true, espera que você use classes geradas pelo Avro (ex: User.java gerado a partir de .avsc). Muito útil para tipagem forte.
        return props;
    }
}
