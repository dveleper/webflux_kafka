package co.com.bancolombia.model.persona.gateways;

import co.com.bancolombia.model.persona.Persona;

public interface PersonaEventsGateway {
    void emitirEvento(Persona persona);
} 