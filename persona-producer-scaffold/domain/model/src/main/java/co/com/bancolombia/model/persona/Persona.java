package co.com.bancolombia.model.persona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la entidad de negocio principal: Persona.
 * <p>
 * Esta clase es el modelo central del dominio. Contiene los atributos que definen a una persona
 * y es utilizada por los casos de uso para aplicar la lógica de negocio.
 * Es una clase agnóstica a la tecnología de persistencia o de transporte.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Persona {

    /** El identificador único de la persona. Puede ser asignado por la base de datos. */
    private String id;
    /** El nombre de pila de la persona. */
    private String nombre;
    /** El apellido de la persona. */
    private String apellido;
    /** La edad de la persona en años. */
    private int edad;
} 