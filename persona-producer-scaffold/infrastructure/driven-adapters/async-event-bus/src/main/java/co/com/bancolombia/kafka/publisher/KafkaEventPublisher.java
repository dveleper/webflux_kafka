package co.com.bancolombia.kafka.publisher;

import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.model.persona.gateways.PersonaEventsGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Implementación del gateway de eventos de persona utilizando Apache Kafka.
 * <p>
 * Esta clase, un adaptador dirigido (driven adapter), implementa la interfaz
 * {@link PersonaEventsGateway} del dominio, proporcionando una solución concreta
 * para publicar eventos en un bróker de Kafka.
 */
@Slf4j
@RequiredArgsConstructor
public class KafkaEventPublisher implements PersonaEventsGateway {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "personas-topic";

    /**
     * Publica un evento de persona en el tópico de Kafka 'personas-topic'.
     * <p>
     * Utiliza el {@link KafkaTemplate} de Spring para enviar el objeto {@link Persona},
     * que será serializado a JSON. El ID de la persona se utiliza como clave del mensaje
     * de Kafka, lo que puede ayudar a garantizar el orden de los mensajes para una misma persona
     * si el tópico tiene múltiples particiones.
     *
     * @param persona la entidad {@link Persona} a publicar.
     */
    @Override
    public void emitirEvento(Persona persona) {
        try {
            String key = persona.getId();
            this.kafkaTemplate.send(TOPIC, key, persona);
            log.info("Evento de persona {} enviado correctamente al topic {}", key, TOPIC);
        } catch (Exception e) {
            log.error("Error al enviar evento de persona {} al topic {}", persona.getId(), TOPIC, e);
        }
    }
} 