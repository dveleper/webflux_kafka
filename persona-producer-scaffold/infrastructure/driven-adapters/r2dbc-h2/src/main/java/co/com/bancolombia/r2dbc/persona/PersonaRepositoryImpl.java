package co.com.bancolombia.r2dbc.persona;

import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.model.persona.gateways.PersonaRepository;
import co.com.bancolombia.r2dbc.persona.mappers.PersonaDataMapper;
import co.com.bancolombia.r2dbc.persona.repository.PersonaR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PersonaRepositoryImpl implements PersonaRepository {

    private final PersonaR2dbcRepository personaR2dbcRepository;
    private final PersonaDataMapper mapper;

    @Override
    public Mono<Persona> save(Persona persona) {
        return personaR2dbcRepository.save(mapper.toData(persona))
                .map(mapper::toEntity);
    }

    @Override
    public Flux<Persona> findAll() {
        return personaR2dbcRepository.findAll()
                .map(mapper::toEntity);
    }

    @Override
    public Mono<Persona> findById(String id) {
        return personaR2dbcRepository.findByPersonaId(id)
                .map(mapper::toEntity);
    }
} 