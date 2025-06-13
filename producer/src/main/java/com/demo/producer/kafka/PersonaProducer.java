package com.demo.producer.kafka;

import com.demo.producer.dto.PersonaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersonaProducer {

    private final KafkaTemplate<String, PersonaDTO> kafkaTemplate;
    private static final String TOPIC = "personas-topic";

    public void enviarEvento(PersonaDTO personaDTO) {
        try {
            // La clave del mensaje será el ID de la persona, para asegurar que los mensajes
            // de una misma persona vayan a la misma partición y se procesen en orden.
            String key = personaDTO.getId();
            kafkaTemplate.send(TOPIC, key, personaDTO);
            log.info("Evento de persona {} enviado correctamente al topic {}", key, TOPIC);
        } catch (Exception e) {
            log.error("Error al enviar evento de persona {} al topic {}", personaDTO.getId(), TOPIC, e);
        }
    }
}