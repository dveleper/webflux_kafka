package com.demo.consumer.listener;

import com.demo.consumer.dto.PersonaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PersonaListener {

    @KafkaListener(topics = "personas-topic", groupId = "personas-group")
    public void consume(PersonaDTO personaDTO, Acknowledgment ack) {
        log.info("Mensaje recibido del topic 'personas-topic': {}", personaDTO);

        try {
            // Simulación de una condición de error para probar el DLT.
            // Si el nombre es "error", se lanza una excepción.
            if ("error".equalsIgnoreCase(personaDTO.getNombre())) {
                throw new IllegalStateException("Error de procesamiento forzado para la persona: " + personaDTO.getId());
            }

            // Aquí iría la lógica de negocio para procesar el DTO.
            log.info("Persona {} procesada correctamente.", personaDTO.getId());

            // Confirma que el mensaje ha sido procesado exitosamente.
            ack.acknowledge();

        } catch (Exception e) {
            log.error("Error al procesar el mensaje para la persona {}: {}", personaDTO.getId(), e.getMessage());
            // No se confirma el mensaje (no se llama a ack.acknowledge()),
            // permitiendo que el ErrorHandler se encargue de los reintentos y el DLT.
            throw e;
        }
    }
}
