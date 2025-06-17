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

## Estructura Detallada: `persona-producer-scaffold`

Este proyecto es un microservicio reactivo construido con Spring WebFlux, diseñado para gestionar una entidad de negocio `Persona`. Su propósito principal es exponer una API REST para operaciones CRUD y, de forma asíncrona, notificar la creación o actualización de personas a través de eventos en un tópico de Apache Kafka.

La arquitectura se organiza en los siguientes módulos, siguiendo los principios de la Arquitectura Limpia:

### `domain` (El Núcleo del Negocio)
Esta es la capa más interna y agnóstica a la tecnología. No contiene ninguna dependencia de frameworks externos.
- **`model`**: Define las estructuras de datos y los contratos del negocio.
    - `Persona.java`: Es la entidad principal. Representa a una persona con sus atributos (id, nombre, etc.) y es la única estructura de datos que conocen los casos de uso.
    - `gateways/PersonaRepository.java`: Interfaz que define las operaciones de persistencia (ej. `save`, `findById`). El dominio define *qué* se puede hacer, pero no *cómo*.
    - `gateways/PersonaEventsGateway.java`: Interfaz que define el contrato para la publicación de eventos (ej. `emitirEvento`).
- **`usecase`**: Contiene la lógica de aplicación pura.
    - `PersonaUseCase.java`: Orquesta los flujos de negocio. Por ejemplo, para crear una persona, recibe la entidad, invoca al `PersonaRepository` para guardarla y luego al `PersonaEventsGateway` para notificar el evento. Depende de las interfaces del `model`, no de implementaciones concretas.

### `infrastructure` (Los Detalles de Implementación)
Esta capa implementa los contratos definidos en el `domain` y maneja la interacción con el mundo exterior.

#### `entry-points` (Puntos de Entrada a la Aplicación)
- **`reactive-web`**: Expone la funcionalidad del `usecase` al mundo exterior a través de una API REST reactiva.
    - `ApiRouter.java`: Utiliza el enfoque funcional de Spring WebFlux para definir las rutas (`@Bean RouterFunction`). Mapea rutas como `POST /api/personas` a un método específico en el `ApiHandler`.
    - `ApiHandler.java`: Contiene la lógica para manejar las peticiones HTTP. Deserializa el cuerpo de la petición (JSON) a un `PersonaDTO`, lo mapea a la entidad `Persona` del dominio, invoca al `PersonaUseCase` y devuelve una respuesta HTTP adecuada.
    - `dto/PersonaDTO.java` y `mappers/PersonaMapper.java`: El DTO es un objeto de transferencia para no exponer el modelo de dominio directamente en la API. El Mapper se encarga de la conversión entre `PersonaDTO` y `Persona`.

#### `driven-adapters` (Adaptadores para Servicios Externos)
- **`r2dbc-h2`**: Implementación del `PersonaRepository`. Es el "cómo" se guardan los datos.
    - `PersonaRepositoryImpl.java`: Implementa la interfaz `PersonaRepository` del dominio. Utiliza R2DBC (Reactive Relational Database Connectivity) para comunicarse de forma no bloqueante con una base de datos en memoria H2.
    - `mappers/PersonaDataMapper.java`: Convierte entre la entidad de dominio `Persona` y la entidad de base de datos `PersonaData`.
- **`async-event-bus`**: Implementación del `PersonaEventsGateway`.
    - `KafkaEventPublisher.java`: Implementa la interfaz `PersonaEventsGateway`. Utiliza el `KafkaTemplate` de Spring para enviar la entidad `Persona` (serializada a JSON) al tópico de Kafka `personas-topic`.

### `applications` (El Ensamblador)
- **`app-service`**: Es el módulo raíz que une todas las piezas.
    - `MainApplication.java`: Punto de arranque de la aplicación Spring Boot.
    - `config/UseCasesConfig.java`: Declara los beans de los casos de uso, inyectando en ellos las implementaciones concretas de los gateways (ej. `r2dbc-h2` y `async-event-bus`). Aquí es donde se resuelve la inversión de dependencias.
    - `resources/application.yaml`: Archivo de configuración central para definir propiedades como el puerto del servidor, la configuración de seguridad básica, las credenciales y URL de la base de datos H2, y la dirección del servidor de Kafka.
