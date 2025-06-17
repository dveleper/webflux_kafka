package co.com.bancolombia.usecase.personausecase;

import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.model.persona.gateways.PersonaEventsGateway;
import co.com.bancolombia.model.persona.gateways.PersonaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Caso de uso que encapsula la lógica de negocio para las operaciones con Personas.
 * <p>
 * Esta clase orquesta el flujo de las operaciones, interactuando con los gateways
 * del dominio (repositorios, publicadores de eventos) para ejecutar las acciones.
 * No conoce los detalles de la infraestructura, solo las interfaces del dominio.
 */
@RequiredArgsConstructor
public class PersonaUseCase {

    private final PersonaRepository personaRepository;
    private final PersonaEventsGateway personaEventsGateway;

    /**
     * Crea una nueva persona, la persiste y emite un evento de dominio.
     * <p>
     * El flujo es reactivo: primero se guarda la persona y, si la operación es exitosa
     * ({@code doOnSuccess}), se emite un evento a través del {@link PersonaEventsGateway}.
     *
     * @param persona la entidad {@link Persona} a crear.
     * @return un {@link Mono} que emite la persona creada.
     */
    public Mono<Persona> crearPersona(Persona persona) {
        return personaRepository.save(persona)
                .doOnSuccess(personaEventsGateway::emitirEvento);
    }

    /**
     * Obtiene un flujo de todas las personas existentes.
     *
     * @return un {@link Flux} que emite todas las entidades {@link Persona} encontradas.
     */
    public Flux<Persona> obtenerTodasLasPersonas() {
        return personaRepository.findAll();
    }

    /**
     * Obtiene una persona específica por su identificador.
     *
     * @param id el identificador único de la persona.
     * @return un {@link Mono} que emite la {@link Persona} encontrada, o vacío si no existe.
     */
    public Mono<Persona> obtenerPersonaPorId(String id) {
        return personaRepository.findById(id);
    }
} 