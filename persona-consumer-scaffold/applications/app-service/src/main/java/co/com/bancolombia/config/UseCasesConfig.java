package co.com.bancolombia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Configuración de Spring para la inyección de dependencias de los Casos de Uso.
 * <p>
 * Esta clase habilita el escaneo de componentes en el paquete {@code co.com.bancolombia.usecase},
 * permitiendo que Spring descubra y gestione los beans de los casos de uso como componentes
 * de la aplicación.
 * <p>
 * El filtro {@code FilterType.REGEX} asegura que solo las clases cuyo nombre termine en "UseCase"
 * sean registradas como beans, manteniendo la convención de la arquitectura.
 */
@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {
}
