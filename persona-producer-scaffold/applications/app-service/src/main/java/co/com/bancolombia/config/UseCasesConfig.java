package co.com.bancolombia.config;

import co.com.bancolombia.kafka.publisher.config.KafkaConfig;
import co.com.bancolombia.model.persona.gateways.PersonaEventsGateway;
import co.com.bancolombia.model.persona.gateways.PersonaRepository;
import co.com.bancolombia.usecase.personausecase.PersonaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(KafkaConfig.class)
public class UseCasesConfig {

    @Bean
    public PersonaUseCase personaUseCase(PersonaRepository personaRepository, PersonaEventsGateway personaEventsGateway) {
        return new PersonaUseCase(personaRepository, personaEventsGateway);
    }
}
