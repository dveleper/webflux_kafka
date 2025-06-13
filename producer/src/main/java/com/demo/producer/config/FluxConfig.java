package com.demo.producer.config;

import com.demo.producer.dto.PersonaDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Configuración para el manejo de flujos de datos reactivos (Flux).
 * <p>
 * Define los beans {@link Sinks.Many} y {@link Flux} que actúan como un bus de eventos
 * en memoria. Esto permite que diferentes componentes de la aplicación se comuniquen
 * de forma desacoplada y reactiva.
 */
@Configuration
public class FluxConfig {

    /**
     * Crea un Sink "many-to-many" (multicast).
     * <p>
     * Un Sink permite emitir elementos de forma programática a un Flux.
     * Este sink en particular permite múltiples suscriptores y se asegura de que
     * todos reciban los mismos elementos emitidos después de su suscripción.
     * Es el componente que "publica" los eventos.
     *
     * @return un Sink para emitir objetos PersonaDTO.
     */
    @Bean
    public Sinks.Many<PersonaDTO> personaSink() {
        // Usamos un sink multicast que permite múltiples suscriptores.
        // onBackpressureBuffer() maneja la contrapresión por si los suscriptores son más lentos que los publicadores.
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    /**
     * Crea un Flux a partir del Sink de personas.
     * <p>
     * Este Flux representa el stream de eventos. Cualquier componente puede inyectar
     * este Flux para "escuchar" los eventos de creación de personas que se emiten
     * a través del {@link #personaSink()}.
     * Es el componente que los suscriptores "escuchan".
     *
     * @param personaSink el Sink que actúa como fuente del Flux.
     * @return un Flux de eventos de PersonaDTO.
     */
    @Bean
    public Flux<PersonaDTO> personaStream(Sinks.Many<PersonaDTO> personaSink) {
        // Exponemos el sink como un Flux para que los componentes puedan suscribirse a él.
        // El Flux es inmutable, previniendo que los consumidores emitan eventos.
        return personaSink.asFlux();
    }
} 