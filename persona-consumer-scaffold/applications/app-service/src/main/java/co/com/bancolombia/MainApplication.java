package co.com.bancolombia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Clase principal que inicia la aplicación Spring Boot.
 * <p>
 * {@code @SpringBootApplication}: Habilita la autoconfiguración de Spring Boot y el escaneo de componentes.
 * {@code @ConfigurationPropertiesScan}: Escanea en busca de beans de configuración anotados con {@code @ConfigurationProperties}.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class MainApplication {
    /**
     * Punto de entrada de la aplicación.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
