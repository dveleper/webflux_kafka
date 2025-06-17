package co.com.bancolombia.r2dbc.persona.mappers;

import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.r2dbc.persona.PersonaData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonaDataMapper {

    @Mapping(target = "dbId", ignore = true) // dbId es autogenerado
    @Mapping(source = "id", target = "personaId")
    PersonaData toData(Persona persona);

    @Mapping(source = "personaId", target = "id")
    Persona toEntity(PersonaData data);
} 