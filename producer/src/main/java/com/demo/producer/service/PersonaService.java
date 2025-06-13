package com.demo.producer.service;

import com.demo.producer.domain.Persona;
import com.demo.producer.dto.PersonaDTO;
import com.demo.producer.kafka.PersonaProducer;
import com.demo.producer.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * Servicio de negocio para la gestión de Personas.
 * <p>
 * Orquesta las operaciones principales como la creación, consulta y notificación de eventos
 * relacionados con las entidades de Persona. Actúa como intermediario entre el controlador
 * y las capas de datos y mensajería.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final PersonaProducer personaProducer;
    private final Sinks.Many<PersonaDTO> personaSink;

    /**
     * Procesa la creación de una nueva persona.
     * <p>
     * Esta operación realiza tres acciones principales de forma reactiva:
     * 1. Mapea el DTO a una entidad de dominio.
     * 2. Persiste la entidad en la base de datos.
     * 3. Tras el guardado exitoso, emite eventos tanto a Kafka como al Sink en memoria para SSE.
     *
     * @param personaDTO el objeto de transferencia de datos con la información de la persona.
     * @return un {@link Mono} que emite la entidad {@link Persona} guardada.
     */
    public Mono<Persona> crearPersona(PersonaDTO personaDTO) {
        Persona personaEntity = toEntity(personaDTO);

        return personaRepository.save(personaEntity)
                .doOnSuccess(savedPersona -> {
                    log.info("Persona guardada en BD: {}", savedPersona);
                    // Una vez guardada, dispara los eventos de forma asíncrona
                    dispararEventos(personaDTO);
                });
    }

    /**
     * Obtiene un flujo de todas las personas existentes en la base de datos.
     *
     * @return un {@link Flux} de {@link PersonaDTO}.
     */
    public Flux<PersonaDTO> obtenerTodasLasPersonas() {
        return personaRepository.findAll()
                .map(this::toDTO);
    }

    /**
     * Busca una persona por su identificador único de base de datos (clave primaria).
     *
     * @param dbId el ID de la base de datos de la persona.
     * @return un {@link Mono} que emite el {@link PersonaDTO} si se encuentra, o un Mono vacío si no.
     */
    public Mono<PersonaDTO> obtenerPersonaPorId(Integer dbId) {
        return personaRepository.findById(dbId)
                .map(this::toDTO);
    }

    private void dispararEventos(PersonaDTO personaDTO) {
        // Enviar a Kafka
        personaProducer.enviarEvento(personaDTO);
        // Enviar a Stream SSE
        personaSink.tryEmitNext(personaDTO);

        log.info("Eventos para la persona {} enviados a Kafka y SSE Stream.", personaDTO.getId());
    }

    private Persona toEntity(PersonaDTO dto) {
        // El dbId es null porque es autogenerado por la base de datos.
        return new Persona(null, dto.getId(), dto.getNombre(), dto.getEdad());
    }

    private PersonaDTO toDTO(Persona entity) {
        // Usamos el personaId (de negocio) para el DTO, no el dbId.
        return new PersonaDTO(entity.getPersonaId(), entity.getNombre(), entity.getEdad());
    }
}