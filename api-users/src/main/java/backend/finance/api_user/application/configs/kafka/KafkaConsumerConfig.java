package backend.finance.api_user.application.configs.kafka;

import backend.finance.api.user.CustomerKafka;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaPropertiesConfig kafkaPropertiesConfig; // Injetar a configuração de propriedades do Kafka

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPropertiesConfig.bootstrapServers); // Servidor Kafka
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // Usar StringDeserializer para desserializar chaves
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class); // Usar JsonDeserializer para desserializar mensagens JSON
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaPropertiesConfig.consumerGroupId); // Definir um ID de grupo
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaPropertiesConfig.consumerAutoOffsetReset); // Ler desde o início do tópico se não houver offset
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, kafkaPropertiesConfig.schemaRegistryUrl);
        props.put("specific.avro.reader", kafkaPropertiesConfig.specificAvroReader);
        return props;
    }

    @Bean
    public ConsumerFactory<String, CustomerKafka> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs()); // Criar a fábrica de consumidores
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomerKafka> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CustomerKafka> factory = new ConcurrentKafkaListenerContainerFactory<>(); // Criar a fábrica de listeners
        factory.setConsumerFactory(consumerFactory()); // Configurar a fábrica de consumidores
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL); // Usar confirmação manual - mais controle - pode ser útil para garantir que a mensagem foi processada antes de confirmar
        return factory;
    }
}
