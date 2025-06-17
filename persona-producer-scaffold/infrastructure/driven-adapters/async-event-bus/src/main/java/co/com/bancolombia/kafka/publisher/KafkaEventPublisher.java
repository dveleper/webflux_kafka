package co.com.bancolombia.kafka.publisher;

import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.model.persona.gateways.PersonaEventsGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
public class KafkaEventPublisher implements PersonaEventsGateway {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "personas-topic";

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