package co.com.bancolombia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Clase principal que inicia la aplicación Spring Boot para el servicio productor de personas.
 * <p>
 * {@code @SpringBootApplication}: Anotación principal que habilita la autoconfiguración de Spring,
 * el escaneo de componentes y la configuración de la aplicación.
 * {@code @ConfigurationPropertiesScan}: Escanea en busca de beans anotados con {@code @ConfigurationProperties}.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class MainApplication {
    /**
     * Punto de entrada principal para la ejecución de la aplicación.
     * @param args Argumentos de línea de comandos pasados al iniciar la aplicación.
     */
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
