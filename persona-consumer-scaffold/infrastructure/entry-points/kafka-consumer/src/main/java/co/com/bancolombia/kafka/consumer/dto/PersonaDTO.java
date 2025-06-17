package co.com.bancolombia.kafka.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) para representar una Persona.
 * <p>
 * Esta clase se utiliza para la deserialización de los mensajes JSON consumidos de Kafka.
 * Sus campos deben coincidir con la estructura del JSON producido por el servicio emisor.
 * <p>
 * Las anotaciones de Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor) generan
 * automáticamente los getters, setters, constructores, y otros métodos boilerplate.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDTO {
    /** El identificador único de la persona. */
    private String id;
    /** El nombre de la persona. */
    private String nombre;
    /** La edad de la persona. */
    private int edad;
} 