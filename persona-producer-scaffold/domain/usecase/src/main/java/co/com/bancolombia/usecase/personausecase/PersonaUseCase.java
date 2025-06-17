package co.com.bancolombia.usecase.personausecase;

import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.model.persona.gateways.PersonaEventsGateway;
import co.com.bancolombia.model.persona.gateways.PersonaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PersonaUseCase {

    private final PersonaRepository personaRepository;
    private final PersonaEventsGateway personaEventsGateway;

    public Mono<Persona> crearPersona(Persona persona) {
        return personaRepository.save(persona)
                .doOnSuccess(personaEventsGateway::emitirEvento);
    }

    public Flux<Persona> obtenerTodasLasPersonas() {
        return personaRepository.findAll();
    }

    public Mono<Persona> obtenerPersonaPorId(String id) {
        return personaRepository.findById(id);
    }
} 