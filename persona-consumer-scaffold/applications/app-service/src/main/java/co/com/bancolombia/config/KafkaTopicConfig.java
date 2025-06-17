package co.com.bancolombia.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuración de Spring para la creación programática de tópicos de Kafka.
 * <p>
 * Esta clase define los beans necesarios para que Spring cree automáticamente
 * los tópicos en el bróker de Kafka al iniciar la aplicación, si no existen.
 */
@Configuration
public class KafkaTopicConfig {

    /**
     * Define y crea el tópico de Dead Letter Topic (DLT) para el tópico principal de personas.
     * <p>
     * Un DLT se utiliza para enviar mensajes que no pudieron ser procesados
     * correctamente después de varios intentos, evitando que se pierdan.
     *
     * @return Un objeto {@link NewTopic} que representa la configuración del tópico DLT.
     */
    @Bean
    public NewTopic dlt() {
        return TopicBuilder.name("personas-topic.DLT")
                .partitions(1)
                .replicas(1)
                .build();
    }
} 