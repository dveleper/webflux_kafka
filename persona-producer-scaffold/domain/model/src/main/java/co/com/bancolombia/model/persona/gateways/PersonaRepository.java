package co.com.bancolombia.model.persona.gateways;

import co.com.bancolombia.model.persona.Persona;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonaRepository {

    Mono<Persona> save(Persona persona);

    Flux<Persona> findAll();

    Mono<Persona> findById(String id);
} 