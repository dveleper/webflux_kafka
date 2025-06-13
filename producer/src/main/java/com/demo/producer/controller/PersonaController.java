package com.demo.producer.controller;

import com.demo.producer.dto.PersonaDTO;
import com.demo.producer.service.PersonaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    private final PersonaService personaService;
    private final Flux<PersonaDTO> personaStream;

    public PersonaController(PersonaService personaService, Flux<PersonaDTO> personaStream) {
        this.personaService = personaService;
        this.personaStream = personaStream;
    }

    /**
     * Crea una nueva persona, la persiste en la base de datos y dispara los eventos
     * a Kafka y al stream SSE. La lógica está encapsulada en PersonaService.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PersonaDTO> crearPersona(@RequestBody PersonaDTO personaDTO) {
        return personaService.crearPersona(personaDTO)
                // Mapeamos la entidad guardada de vuelta a un DTO para la respuesta.
                .map(savedEntity -> new PersonaDTO(savedEntity.getPersonaId(), savedEntity.getNombre(), savedEntity.getEdad()));
    }

    /**
     * Obtiene todas las personas guardadas en la base de datos.
     * @return Un Flux de PersonaDTO.
     */
    @GetMapping
    public Flux<PersonaDTO> obtenerTodasLasPersonas() {
        return personaService.obtenerTodasLasPersonas();
    }

    /**
     * Busca una persona por su ID de base de datos.
     * @return Un Mono que contiene la PersonaDTO si se encuentra, o un 404 Not Found si no.
     */
    @GetMapping("/{dbId}")
    public Mono<ResponseEntity<PersonaDTO>> obtenerPersonaPorId(@PathVariable Integer dbId) {
        return personaService.obtenerPersonaPorId(dbId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Expone un stream de Server-Sent Events (SSE) con las personas que se van creando.
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PersonaDTO> streamPersonas() {
        return this.personaStream;
    }
}
