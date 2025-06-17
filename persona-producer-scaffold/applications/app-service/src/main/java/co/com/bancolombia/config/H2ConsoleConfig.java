package co.com.bancolombia.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * Configuración para levantar la consola web de la base de datos H2.
 * <p>
 * Debido a que la autoconfiguración de la consola H2 puede ser problemática en aplicaciones WebFlux,
 * esta clase define un bean para iniciar y detener el servidor de la consola manualmente.
 */
@Configuration
public class H2ConsoleConfig {

    /**
     * Crea y gestiona el ciclo de vida del servidor de la consola H2.
     * <p>
     * El servidor se inicia en el puerto 8082 y se permite el acceso desde otras máquinas.
     * Spring gestiona automáticamente la llamada a los métodos {@code start()} y {@code stop()}
     * gracias a los atributos {@code initMethod} y {@code destroyMethod}.
     *
     * @return Una instancia del servidor H2.
     * @throws SQLException si ocurre un error durante la creación del servidor.
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
    }
} 