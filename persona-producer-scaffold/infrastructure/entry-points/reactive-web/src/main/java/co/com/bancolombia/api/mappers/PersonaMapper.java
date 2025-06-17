package co.com.bancolombia.api.mappers;

import co.com.bancolombia.api.dto.PersonaDTO;
import co.com.bancolombia.model.persona.Persona;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonaMapper {
    Persona toEntity(PersonaDTO dto);
    PersonaDTO toDTO(Persona persona);
} 