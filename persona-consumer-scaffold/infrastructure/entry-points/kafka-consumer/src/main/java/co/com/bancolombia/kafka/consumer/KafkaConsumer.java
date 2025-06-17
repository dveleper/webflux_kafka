package co.com.bancolombia.kafka.consumer;

import co.com.bancolombia.kafka.consumer.dto.PersonaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    //private final SomeUseCase useCase;

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
