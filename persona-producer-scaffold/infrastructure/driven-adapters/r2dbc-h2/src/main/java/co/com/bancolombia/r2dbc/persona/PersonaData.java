package co.com.bancolombia.r2dbc.persona;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("personas")
public class PersonaData {

    @Id
    private Integer dbId;

    @Column("persona_id")
    private String personaId;
    
    private String nombre;
    private int edad;
} 