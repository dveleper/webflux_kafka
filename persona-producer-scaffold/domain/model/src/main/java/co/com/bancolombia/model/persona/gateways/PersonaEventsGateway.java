package co.com.bancolombia.model.persona.gateways;

import co.com.bancolombia.model.persona.Persona;

/**
 * Define el contrato (puerto) para un publicador de eventos de dominio relacionados con Personas.
 * <p>
 * Esta interfaz pertenece a la capa de dominio y abstrae la tecnología de mensajería subyacente.
 * El caso de uso dependerá de esta interfaz, no de una implementación concreta.
 */
public interface PersonaEventsGateway {
    /**
     * Emite un evento de dominio para una entidad Persona.
     * <p>
     * La implementación de este método se encargará de serializar y enviar el objeto
     * a un sistema de mensajería como Kafka, RabbitMQ, etc.
     *
     * @param persona La entidad Persona que se publicará como evento.
     */
    void emitirEvento(Persona persona);
} 