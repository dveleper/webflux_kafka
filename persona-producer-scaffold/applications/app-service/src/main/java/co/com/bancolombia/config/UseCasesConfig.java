package co.com.bancolombia.config;

import co.com.bancolombia.kafka.publisher.config.KafkaConfig;
import co.com.bancolombia.model.persona.gateways.PersonaEventsGateway;
import co.com.bancolombia.model.persona.gateways.PersonaRepository;
import co.com.bancolombia.usecase.personausecase.PersonaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuración de Spring para la creación y exposición de los Casos de Uso como beans.
 * <p>
 * Esta clase es el corazón de la inyección de dependencias para la capa de dominio.
 * Se encarga de construir los beans de los casos de uso, inyectando en ellos las
 * implementaciones concretas de los gateways (repositorios, publicadores de eventos, etc.)
 * que provienen de la capa de infraestructura.
 * <p>
 * La anotación {@code @Import(KafkaConfig.class)} asegura que la configuración del publicador
 * de Kafka esté disponible en el contexto de Spring.
 */
@Configuration
@Import(KafkaConfig.class)
public class UseCasesConfig {

    /**
     * Crea el bean para {@link PersonaUseCase}.
     * <p>
     * Este método recibe las implementaciones concretas de los gateways (inyectadas por Spring)
     * y las utiliza para construir la instancia del caso de uso. De esta forma, el caso de uso
     * opera contra abstracciones (interfaces) sin conocer los detalles de la infraestructura.
     *
     * @param personaRepository La implementación del repositorio de personas (ej. R2dbcH2).
     * @param personaEventsGateway La implementación del publicador de eventos (ej. KafkaEventPublisher).
     * @return una instancia de {@link PersonaUseCase} lista para ser utilizada en la aplicación.
     */
    @Bean
    public PersonaUseCase personaUseCase(PersonaRepository personaRepository, PersonaEventsGateway personaEventsGateway) {
        return new PersonaUseCase(personaRepository, personaEventsGateway);
    }
}
