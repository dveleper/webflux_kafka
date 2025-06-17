package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.PersonaDTO;
import co.com.bancolombia.api.mappers.PersonaMapper;
import co.com.bancolombia.usecase.personausecase.PersonaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
@RequiredArgsConstructor
public class ApiHandler {

    private final PersonaUseCase personaUseCase;
    private final PersonaMapper personaMapper;
    private final Sinks.Many<PersonaDTO> personaSink = Sinks.many().multicast().onBackpressureBuffer();

    public Mono<ServerResponse> crearPersona(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PersonaDTO.class)
                .map(personaMapper::toEntity)
                .flatMap(personaUseCase::crearPersona)
                .map(personaMapper::toDTO)
                .doOnNext(personaSink::tryEmitNext) // Emitir al sink para el stream SSE
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).bodyValue(dto));
    }

    public Mono<ServerResponse> obtenerTodasLasPersonas(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(personaUseCase.obtenerTodasLasPersonas().map(personaMapper::toDTO), PersonaDTO.class);
    }

    public Mono<ServerResponse> obtenerPersonaPorId(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return personaUseCase.obtenerPersonaPorId(id)
                .map(personaMapper::toDTO)
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> streamPersonas(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(personaSink.asFlux(), PersonaDTO.class);
    }
} 