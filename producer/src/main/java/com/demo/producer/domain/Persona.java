package com.demo.producer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("personas")
public class Persona {

    @Id
    private Integer dbId; // Clave primaria de la base de datos (autoincremental)

    private String personaId; // ID de negocio (el que viene en el DTO)
    private String nombre;
    private int edad;
}
