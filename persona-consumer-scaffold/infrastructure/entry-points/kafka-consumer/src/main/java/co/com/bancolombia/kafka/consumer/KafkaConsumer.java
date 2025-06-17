package co.com.bancolombia.kafka.consumer;

import co.com.bancolombia.kafka.consumer.dto.PersonaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Punto de entrada (Entry Point) que actúa como consumidor de eventos de Kafka.
 * <p>
 * Esta clase es responsable de escuchar un tópico específico de Kafka y procesar
 * los mensajes recibidos.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    //private final SomeUseCase useCase;

    /**
     * Método listener que se activa automáticamente cuando llega un mensaje al tópico 'personas-topic'.
     * <p>
     * La anotación {@code @KafkaListener} configura la suscripción al tópico, el grupo de consumidor
     * y la fábrica de contenedores a utilizar. Spring se encarga de la deserialización del mensaje
     * JSON a un objeto {@link PersonaDTO} antes de invocar este método.
     *
     * @param personaDTO El objeto deserializado desde el mensaje de Kafka, que contiene los datos de la persona.
     */
    @KafkaListener(
            topics = "personas-topic",
            groupId = "personas-group",
            containerFactory = "kafkaListenerContainerFactory" // Coincide con el Bean en KafkaConfig
    )
    public void escucharMensajesDePersonas(PersonaDTO personaDTO) {
        log.info("Mensaje de persona recibido en el grupo 'personas-group': {}", personaDTO);
        // Aquí iría la lógica para procesar el DTO, por ejemplo:
        // useCase.procesarPersona(personaDTO);
    }
}
