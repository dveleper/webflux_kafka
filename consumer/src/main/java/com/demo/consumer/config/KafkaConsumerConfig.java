package com.demo.consumer.config;

import com.demo.consumer.dto.PersonaDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuración para el consumidor de Kafka.
 * <p>
 * Define los beans necesarios para crear y configurar un listener de Kafka
 * que pueda recibir y deserializar mensajes de tipo {@link PersonaDTO}.
 */
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, PersonaDTO> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "personas-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Configuración del deserializador JSON
        JsonDeserializer<PersonaDTO> deserializer = new JsonDeserializer<>(PersonaDTO.class);
        deserializer.addTrustedPackages("*"); // Confiar en todos los paquetes

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PersonaDTO> kafkaListenerContainerFactory(
            ConsumerFactory<String, PersonaDTO> consumerFactory,
            DefaultErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, PersonaDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        // Habilitar la confirmación manual de mensajes
        factory.getContainerProperties().setAckMode(org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaOperations<String, ?> kafkaOperations) {
        // En caso de error, publica el mensaje en un DLT (Dead Letter Topic)
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaOperations,
                (rec, ex) -> new TopicPartition(rec.topic() + ".DLT", -1));

        // Define una política de reintentos: 2 intentos con 1 segundo de espera entre ellos.
        // Después de los 2 reintentos, se ejecuta el 'recoverer' (enviar a DLT).
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 2));
    }

    /**
     * Define el bean para el Dead Letter Topic (DLT).
     * <p>
     * Esto asegura que el tópico 'personas-topic.DLT' exista en el broker de Kafka.
     * Se crea con 1 partición y un factor de replicación de 1, adecuado para un entorno local.
     *
     * @return un objeto NewTopic que será usado por Spring para crear el tópico al iniciar.
     */
    @Bean
    public NewTopic dlt() {
        return TopicBuilder.name("personas-topic.DLT")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
