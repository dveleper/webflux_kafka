package co.com.bancolombia.kafka.consumer.config;

import co.com.bancolombia.kafka.consumer.dto.PersonaDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

/**
 * Configuración de Spring para el consumidor de Kafka.
 * <p>
 * Define los beans necesarios para crear y configurar la infraestructura
 * que permite a la aplicación consumir mensajes de un tópico de Kafka.
 */
@Configuration
public class KafkaConfig {

    /**
     * Crea y configura la fábrica de consumidores de Kafka.
     * <p>
     * Este bean es responsable de establecer las propiedades de conexión con el bróker de Kafka
     * y de definir los deserializadores para la clave y el valor de los mensajes.
     * Utiliza las propiedades definidas en {@code application.yaml}.
     *
     * @param properties Propiedades de Kafka autoconfiguradas por Spring Boot.
     * @return una {@link ConsumerFactory} configurada para consumir mensajes con clave String y valor {@link PersonaDTO}.
     */
    @Bean
    public ConsumerFactory<String, PersonaDTO> consumerFactory(KafkaProperties properties) {
        Map<String, Object> props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // La propiedad spring.json.trusted.packages se configura en application.yaml

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(PersonaDTO.class, false));
    }

    /**
     * Crea la fábrica de contenedores de listeners de Kafka.
     * <p>
     * Este bean utiliza la {@link ConsumerFactory} para construir el contenedor que gestionará
     * el ciclo de vida del listener (ej. el método anotado con {@code @KafkaListener}).
     * Es el puente entre la configuración del consumidor y el código que procesa el mensaje.
     *
     * @param consumerFactory La fábrica de consumidores a utilizar.
     * @return una {@link ConcurrentKafkaListenerContainerFactory} lista para ser usada por los listeners.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PersonaDTO> kafkaListenerContainerFactory(
            ConsumerFactory<String, PersonaDTO> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, PersonaDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
