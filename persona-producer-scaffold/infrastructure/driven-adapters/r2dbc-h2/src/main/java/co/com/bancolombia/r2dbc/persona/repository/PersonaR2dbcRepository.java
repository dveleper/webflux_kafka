package co.com.bancolombia.r2dbc.persona.repository;

import co.com.bancolombia.r2dbc.persona.PersonaData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PersonaR2dbcRepository extends ReactiveCrudRepository<PersonaData, Integer> {
    @Query("SELECT * FROM personas WHERE persona_id = :personaId")
    Mono<PersonaData> findByPersonaId(@Param("personaId") String personaId);
} 