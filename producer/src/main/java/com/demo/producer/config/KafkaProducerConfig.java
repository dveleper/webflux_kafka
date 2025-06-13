package com.demo.producer.config;

import com.demo.producer.dto.PersonaDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuración para el productor de Kafka.
 * <p>
 * Define los beans necesarios para crear y configurar un KafkaTemplate que puede
 * enviar mensajes de tipo {@link PersonaDTO} a un tópico de Kafka.
 */
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Crea un mapa de propiedades de configuración para el productor de Kafka.
     * <p>
     * Especifica el servidor de arranque, así como los serializadores para la clave (String)
     * y el valor (JSON para objetos PersonaDTO).
     *
     * @return un mapa con las propiedades del productor.
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return props;
    }

    /**
     * Crea la fábrica de productores de Kafka.
     * <p>
     * Utiliza las propiedades definidas en {@link #producerConfigs()} para construir
     * una fábrica que crea instancias de productores.
     *
     * @return una instancia de {@link ProducerFactory}.
     */
    @Bean
    public ProducerFactory<String, PersonaDTO> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Crea el bean KafkaTemplate.
     * <p>
     * El KafkaTemplate es una abstracción de alto nivel que simplifica el envío de mensajes a Kafka.
     *
     * @return una instancia de {@link KafkaTemplate} configurada para enviar PersonaDTO.
     */
    @Bean
    public KafkaTemplate<String, PersonaDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
