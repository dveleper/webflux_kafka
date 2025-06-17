package co.com.bancolombia.r2dbc.persona.mappers;

import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.r2dbc.persona.PersonaData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre la entidad de dominio {@link Persona} y la entidad de datos {@link PersonaData}.
 * <p>
 * Utiliza MapStruct para generar automáticamente el código de mapeo en tiempo de compilación,
 * asegurando un alto rendimiento y evitando la escritura de código boilerplate.
 * {@code componentModel = "spring"} hace que MapStruct genere un bean de Spring para este mapper.
 */
@Mapper(componentModel = "spring")
public interface PersonaDataMapper {

    /**
     * Convierte una entidad de dominio {@link Persona} a una entidad de datos {@link PersonaData}.
     *
     * @param persona la entidad de dominio.
     * @return la entidad de datos correspondiente.
     */
    @Mapping(target = "dbId", ignore = true) // dbId es autogenerado por la BD
    @Mapping(source = "id", target = "personaId")
    PersonaData toData(Persona persona);

    /**
     * Convierte una entidad de datos {@link PersonaData} a una entidad de dominio {@link Persona}.
     *
     * @param data la entidad de datos.
     * @return la entidad de dominio correspondiente.
     */
    @Mapping(source = "personaId", target = "id")
    Persona toEntity(PersonaData data);
} 