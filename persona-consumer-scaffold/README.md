# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función "public static void main(String[] args)").

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**

---

## Estructura Detallada: `persona-consumer-scaffold`

Este proyecto es un microservicio reactivo diseñado para actuar como un consumidor de eventos de Apache Kafka. Su única responsabilidad es escuchar un tópico específico (`personas-topic`), procesar los mensajes recibidos que representan a una "Persona", y ejecutar una acción con ellos (en este caso, registrarlos en el log).

La arquitectura se organiza en los siguientes módulos:

### `domain` (El Núcleo del Negocio)
Aunque este servicio no tiene una lógica de negocio compleja, la estructura del dominio se mantiene para la consistencia de la Arquitectura Limpia.
- **`model`**: Podría contener las entidades y gateways si el consumidor necesitara interactuar con otros sistemas o bases de datos. En la implementación actual, el modelo de datos se maneja directamente en el punto de entrada a través de un DTO.
- **`usecase`**: Contendría la lógica de aplicación si el procesamiento del mensaje fuera más allá de un simple registro. Por ejemplo, un `ProcesarPersonaUseCase` podría ser invocado desde el listener de Kafka.

### `infrastructure` (Los Detalles de Implementación)
Esta capa contiene la implementación concreta para conectarse y recibir mensajes de Kafka.

#### `entry-points` (Puntos de Entrada a la Aplicación)
- **`kafka-consumer`**: Es el punto de entrada principal del servicio.
    - **`KafkaConsumer.java`**: Contiene el método de escucha. Utiliza la anotación `@KafkaListener` de Spring para suscribirse al tópico `personas-topic` con un `group-id` específico. Este método es el que se activa cada vez que llega un nuevo mensaje.
    - **`dto/PersonaDTO.java`**: Objeto de Transferencia de Datos que representa la estructura del mensaje JSON que se espera recibir de Kafka. El deserializador de Kafka lo usa como clase de destino.
    - **`config/KafkaConfig.java`**: Configura los beans de Spring Kafka necesarios para el funcionamiento del listener. Define el `ConsumerFactory` (que especifica cómo conectarse a Kafka y cómo deserializar los mensajes) y el `ConcurrentKafkaListenerContainerFactory` (que gestiona el ciclo de vida del listener).

#### `driven-adapters` (Adaptadores para Servicios Externos)
En esta implementación, no hay `driven-adapters`, ya que el servicio no se comunica con ningún sistema externo (como una base de datos) después de recibir el mensaje. Si necesitara guardar los datos, aquí se implementaría el `Repository`.

### `applications` (El Ensamblador)
- **`app-service`**: Es el módulo que une todas las piezas y arranca la aplicación.
    - **`MainApplication.java`**: Punto de arranque de la aplicación Spring Boot.
    - **`config`**: Contiene la configuración de beans globales, como `UseCasesConfig.java` (aunque no se use activamente) y `KafkaTopicConfig.java` para la creación programática de tópicos (como el DLT).
    - **`resources/application.yaml`**: Archivo de configuración central. Aquí se definen propiedades cruciales como la dirección del servidor de Kafka (`bootstrap-servers`), el identificador del grupo de consumidores (`group-id`), y la estrategia de deserialización.
