package co.com.bancolombia.r2dbc.persona;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Representa la entidad de datos para una Persona en la base de datos.
 * <p>
 * Esta clase es una implementación específica para la persistencia y está mapeada
 * a la tabla "personas". Contiene los detalles de la infraestructura de base de datos
 * y no debe ser expuesta a la capa de dominio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("personas")
public class PersonaData {

    /** Llave primaria autoincremental de la base de datos. */
    @Id
    private Integer dbId;

    /** Identificador de negocio de la persona. */
    @Column("persona_id")
    private String personaId;

    /** Nombre de la persona. */
    private String nombre;
    /** Apellido de la persona. */
    private String apellido;
    /** Edad de la persona. */
    private int edad;
} 