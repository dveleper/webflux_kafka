package co.com.bancolombia.kafka.publisher.config;

import co.com.bancolombia.kafka.publisher.KafkaEventPublisher;
import co.com.bancolombia.model.persona.gateways.PersonaEventsGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Configuración de Spring para el publicador de eventos de Kafka.
 * <p>
 * Esta clase define el bean para la implementación del {@link PersonaEventsGateway},
 * asegurando que esté disponible para ser inyectado en los casos de uso.
 */
@Configuration
public class KafkaConfig {

    /**
     * Crea el bean para el publicador de eventos de persona.
     * <p>
     * Este método construye una instancia de {@link KafkaEventPublisher} y la expone
     * como un bean de Spring bajo la interfaz {@link PersonaEventsGateway}.
     *
     * @param kafkaTemplate El template de Kafka, autoconfigurado por Spring Boot, que se
     *                      utilizará para enviar los mensajes.
     * @return una implementación del gateway de eventos de persona.
     */
    @Bean
    public PersonaEventsGateway personaEventsGateway(KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaEventPublisher(kafkaTemplate);
    }
} 