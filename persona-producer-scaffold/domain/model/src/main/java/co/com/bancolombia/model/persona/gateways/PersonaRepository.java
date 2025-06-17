package co.com.bancolombia.model.persona.gateways;

import co.com.bancolombia.model.persona.Persona;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Define el contrato (puerto) para la persistencia de entidades {@link Persona}.
 * <p>
 * Esta interfaz es la abstracción del repositorio en la capa de dominio.
 * Define las operaciones CRUD que se pueden realizar, utilizando programación reactiva (Mono y Flux)
 * para manejar las operaciones de forma asíncrona y no bloqueante.
 */
public interface PersonaRepository {

    /**
     * Guarda o actualiza una entidad Persona en el repositorio.
     *
     * @param persona la entidad a guardar.
     * @return un {@link Mono} que emite la entidad guardada.
     */
    Mono<Persona> save(Persona persona);

    /**
     * Recupera todas las entidades Persona del repositorio.
     *
     * @return un {@link Flux} que emite todas las entidades encontradas.
     */
    Flux<Persona> findAll();

    /**
     * Busca una entidad Persona por su identificador único.
     *
     * @param id el identificador de la persona a buscar.
     * @return un {@link Mono} que emite la entidad encontrada, o un mono vacío si no se encuentra.
     */
    Mono<Persona> findById(String id);
} 