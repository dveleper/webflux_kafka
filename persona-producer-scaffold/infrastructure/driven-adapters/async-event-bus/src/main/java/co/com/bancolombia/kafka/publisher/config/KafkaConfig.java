package co.com.bancolombia.kafka.publisher.config;

import co.com.bancolombia.kafka.publisher.KafkaEventPublisher;
import co.com.bancolombia.model.persona.gateways.PersonaEventsGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaConfig {

    @Bean
    public PersonaEventsGateway personaEventsGateway(KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaEventPublisher(kafkaTemplate);
    }
} 