package com.demo.producer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuración de Spring Security para el microservicio Productor.
 * <p>
 * Habilita la seguridad web, pero personaliza las reglas para permitir el acceso
 * a los endpoints públicos como la API de personas y la consola H2.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad que se aplica a todas las peticiones HTTP.
     *
     * @param http el objeto HttpSecurity para configurar las reglas.
     * @return la cadena de filtros de seguridad construida.
     * @throws Exception si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Define las reglas de autorización para las peticiones.
            .authorizeHttpRequests(authz -> authz
                // Permite el acceso sin autenticación a la consola H2 y a la API de personas.
                .requestMatchers("/h2-console/**", "/personas/**").permitAll()
                // Requiere autenticación para cualquier otra petición.
                .anyRequest().authenticated()
            )
            // Configura la protección contra Cross-Site Request Forgery (CSRF).
            .csrf(csrf -> csrf
                // Deshabilita CSRF para las rutas de la consola y la API, ya que son sin estado.
                .ignoringRequestMatchers("/h2-console/**", "/personas/**")
            )
            // Configura las cabeceras de seguridad.
            .headers(headers -> headers
                // Permite que la consola H2 sea mostrada dentro de un <frame> o <iframe> desde el mismo origen.
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            )
            // Habilita la autenticación básica HTTP con la configuración por defecto.
            .httpBasic(withDefaults());
        return http.build();
    }
} 