package co.com.bancolombia.config;

import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Spring para el bean de {@link ObjectMapper}.
 * <p>
 * Provee una instancia de {@code ObjectMapper} que puede ser inyectada en otras partes
 * de la aplicación para la serialización y deserialización de objetos JSON.
 * Utiliza la implementación de {@code reactivecommons}.
 */
@Configuration
public class ObjectMapperConfig {

    /**
     * Define el bean de {@link ObjectMapper}.
     *
     * @return una instancia de {@link ObjectMapperImp}.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperImp();
    }

}
