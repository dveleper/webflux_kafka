package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ApiRouter {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(ApiHandler handler) {
        return RouterFunctions.route(GET("/api/personas"), handler::obtenerTodasLasPersonas)
                .andRoute(GET("/api/personas/{id}"), handler::obtenerPersonaPorId)
                .andRoute(POST("/api/personas"), handler::crearPersona)
                .andRoute(GET("/api/event-stream").and(accept(MediaType.TEXT_EVENT_STREAM)), handler::streamPersonas);
    }
} 