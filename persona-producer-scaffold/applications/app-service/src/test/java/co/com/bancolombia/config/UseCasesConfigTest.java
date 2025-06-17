package co.com.bancolombia.config;

import co.com.bancolombia.model.persona.gateways.PersonaEventsGateway;
import co.com.bancolombia.model.persona.gateways.PersonaRepository;
import co.com.bancolombia.usecase.personausecase.PersonaUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UseCasesConfig.class)
class UseCasesConfigTest {

    @Autowired
    private PersonaUseCase personaUseCase;

    @MockBean
    private PersonaRepository personaRepository;

    @MockBean
    private PersonaEventsGateway personaEventsGateway;

    @Test
    void personaUseCaseBeanShouldBeCreated() {
        assertNotNull(personaUseCase, "El bean de PersonaUseCase no debería ser nulo");
    }
}