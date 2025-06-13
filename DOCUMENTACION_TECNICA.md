# Documentación Técnica: Sistema de Personas (Productor-Consumidor con Kafka)

## 1. Arquitectura General

Este sistema implementa un patrón de arquitectura de Productor-Consumidor utilizando Apache Kafka como bus de mensajería. La arquitectura se compone de dos microservicios principales, un clúster de Kafka y está diseñada para ser ejecutada en contenedores Docker.

### Pila Tecnológica
- **Lenguaje**: Java 17
- **Framework**: Spring Boot 3
- **Programación Reactiva**: Spring WebFlux y Project Reactor para operaciones asíncronas y no bloqueantes.
- **Persistencia (Productor)**: Spring Data R2DBC con un driver para la base de datos en memoria H2.
- **Mensajería**: Spring for Apache Kafka.
- **Contenerización**: Docker y Docker Compose.
- **Build Tool**: Apache Maven.

---

## 2. Diagramas de Arquitectura y Diseño

### 2.1. Diagrama de Casos de Uso
Este diagrama muestra las acciones que un usuario (o sistema externo) puede realizar sobre el sistema.

![Diagrama de Casos de Uso](Diagrama%20de%20Casos%20de%20Uso.PNG)

### 2.2. Diagrama de Componentes
Este diagrama muestra los principales componentes de software y las relaciones entre ellos.

![Diagrama de Componentes](Diagrama%20de%20Componentes.PNG)

### 2.3. Diagrama de Despliegue
Este diagrama ilustra cómo se despliegan los componentes en la infraestructura de Docker.

![Diagrama de Despliegue](Diagrama%20de%20Despliegue.PNG)

### 2.4. Diagrama de Arquitectura de Solución
Este diagrama ofrece una vista de alto nivel de los componentes internos de cada microservicio y las tecnologías que utilizan para interactuar.

![Diagrama de Arquitectura de Solución](Diagrama%20de%20Arquitectura.PNG)

---

## 3. Flujo Detallado y Rol de Componentes

A continuación, se describe el flujo completo de datos cuando se crea una nueva persona, detallando la responsabilidad de cada componente.

**Escenario: `POST /personas`**

1.  **Cliente Externo**: Un usuario o sistema envía una petición `HTTP POST` a `http://localhost:8080/personas` con los datos de una persona en formato JSON en el cuerpo de la solicitud.

2.  **`Producer Service` - `PersonaController`**:
    *   **Rol**: Es la puerta de entrada a la API. Se encarga de recibir las peticiones HTTP, validar que están bien formadas y deserializar el cuerpo JSON a un objeto `PersonaDTO`.
    *   **Acción**: Invoca al método correspondiente en la capa de servicio (`PersonaService`) pasándole el `PersonaDTO`.

3.  **`Producer Service` - `PersonaService`**:
    *   **Rol**: Orquesta la lógica de negocio. Es el cerebro de la operación. No sabe de HTTP ni de Kafka directamente, solo ejecuta las acciones de negocio.
    *   **Acción**:
        a. Recibe el `PersonaDTO` del controlador.
        b. Mapea el `PersonaDTO` a una entidad de dominio `Persona`.
        c. Llama al `PersonaRepository` para persistir la entidad `Persona` en la base de datos H2.
        d. Una vez guardada, llama al servicio productor de Kafka (`KafkaService`) para enviar el `PersonaDTO` original al tópico `personas-topic`.
        e. Emite el `PersonaDTO` a un flujo reactivo (`Flux`) interno para alimentar el stream SSE.
        f. Devuelve el resultado al controlador.

4.  **`Producer Service` - `PersonaRepository`**:
    *   **Rol**: Capa de acceso a datos. Su única responsabilidad es interactuar con la base de datos (en este caso, H2).
    *   **Acción**: Recibe la entidad `Persona` y ejecuta la operación de guardado en la tabla `personas`.

5.  **`Apache Kafka`**:
    *   **Rol**: Actúa como un bus de mensajería duradero y desacoplado. Garantiza que los mensajes se entreguen de forma fiable.
    *   **Acción**: Recibe el mensaje del productor en el tópico `personas-topic` y lo almacena, poniéndolo a disposición de cualquier consumidor suscrito.

6.  **`Consumer Service` - `PersonaListener`**:
    *   **Rol**: Es el punto de entrada en el servicio consumidor. Está constantemente escuchando el tópico `personas-topic`.
    *   **Acción**:
        a. Cuando Kafka le entrega un nuevo mensaje, Spring Kafka lo deserializa automáticamente de JSON a un `PersonaDTO`.
        b. El listener recibe el `PersonaDTO` y un objeto `Acknowledgment` para control manual.
        c. Invoca a la lógica de negocio del consumidor.

7.  **`Consumer Service` - Lógica de Negocio (`Business Logic`)**:
    *   **Rol**: Ejecuta la acción que el consumidor debe realizar con los datos. En este proyecto, la lógica es simple.
    *   **Acción**:
        a. Registra en el log que ha recibido y procesado el mensaje.
        b. Realiza una comprobación para simular errores (si el nombre es "error").
        c. Si todo es correcto, utiliza el objeto `Acknowledgment` para notificar a Kafka que el mensaje ha sido procesado exitosamente y puede ser eliminado de la cola para este grupo de consumidores. Si hay un error, no lo notifica, permitiendo reintentos o el envío a una DLT.

Este flujo desacoplado asegura que el productor puede seguir aceptando peticiones y publicando mensajes incluso si el consumidor está caído o procesando lentamente, lo que aumenta la resiliencia y escalabilidad del sistema.

---

## 4. Proyecto `producer`

### Propósito
El proyecto `producer` es una aplicación web reactiva que expone una API REST para la gestión de entidades `Persona`. Sus responsabilidades son:
1.  Recibir peticiones HTTP para crear y consultar personas.
2.  Persistir la información de las personas en una base de datos.
3.  Producir y enviar un mensaje a un tópico de Kafka cada vez que se crea una nueva persona.
4.  Exponer un stream de Server-Sent Events (SSE) para notificar en tiempo real las nuevas personas creadas.

### API Endpoints
La API se encuentra bajo el path base `/personas`.

| Método | Ruta              | Descripción                                                                 | Body (Request) | Body (Response) |
|--------|-------------------|-----------------------------------------------------------------------------|----------------|-----------------|
| `POST` | `/`               | Crea una nueva persona, la guarda en BD y la envía a Kafka.                 | `PersonaDTO`   | `PersonaDTO`    |
| `GET`  | `/`               | Obtiene todas las personas almacenadas en la base de datos.                 | N/A            | `Flux<PersonaDTO>` |
| `GET`  | `/{dbId}`         | Busca una persona por su ID de base de datos (clave primaria).              | N/A            | `Mono<PersonaDTO>` |
| `GET`  | `/stream`         | Stream de Server-Sent Events (SSE) con las personas creadas en tiempo real. | N/A            | `Flux<PersonaDTO>` (stream) |

### Modelos de Datos

#### `PersonaDTO` (Data Transfer Object)
Utilizado para la comunicación a través de la API REST y como cuerpo del mensaje de Kafka.

-   `id` (String): Un identificador de negocio para la persona.
-   `nombre` (String): Nombre de la persona.
-   `edad` (int): Edad de la persona.

#### `Persona` (Entidad de Dominio)
Representa la tabla `personas` en la base de datos.

-   `dbId` (Integer): Clave primaria autoincremental (`@Id`).
-   `personaId` (String): El ID de negocio (mapeado desde el DTO).
-   `nombre` (String): Nombre de la persona.
-   `edad` (int): Edad de la persona.

### Flujo de Datos (Creación de Persona)
1.  Un cliente envía una petición `POST /personas` con un `PersonaDTO` en el cuerpo.
2.  `PersonaController` recibe la petición.
3.  Llama a `PersonaService`, que orquesta la lógica de negocio.
4.  El servicio guarda la entidad `Persona` en la base de datos H2 a través de un repositorio R2DBC.
5.  El servicio envía el `PersonaDTO` original al tópico `personas-topic` en Kafka.
6.  El servicio también emite el `PersonaDTO` a un `Flux` interno que alimenta el stream SSE.
7.  El controlador devuelve el `PersonaDTO` de la persona creada al cliente.

---

## 5. Proyecto `consumer`

### Propósito
El proyecto `consumer` es una aplicación que se suscribe a un tópico de Kafka para procesar mensajes sobre `Persona`. No expone ninguna API. Su única responsabilidad es consumir y procesar los mensajes.

### Kafka Listener (`PersonaListener`)

-   **Tópico**: `personas-topic`
-   **Group ID**: `personas-group`

El método `consume` en la clase `PersonaListener` se activa cada vez que llega un nuevo mensaje al tópico.

### Lógica de Procesamiento
1.  Spring Kafka deserializa automáticamente el mensaje JSON del tópico a un objeto `PersonaDTO`.
2.  El listener recibe el `PersonaDTO`.
3.  Se loguea la información de la persona recibida.
4.  **Manejo de Errores**:
    -   Se utiliza un mecanismo de **acknowledgment manual**. El mensaje solo se marca como procesado (`ack.acknowledge()`) si la lógica se completa sin errores.
    -   Se incluye una simulación de error: si el campo `nombre` del DTO es `"error"`, se lanza una excepción.
    -   Si se lanza una excepción, el mensaje no se confirma. Esto permite que el `ErrorHandler` de Spring Kafka aplique políticas de reintento y, finalmente, envíe el mensaje a una Dead Letter Topic (DLT) si los reintentos fallan (configuración no explícita en el código pero es el comportamiento por defecto).

---

## 6. Infraestructura y Ejecución

### Docker y Docker Compose
El sistema está completamente containerizado. El archivo `consumer/docker-compose.yml` define los servicios necesarios para ejecutar el entorno:

-   `zookeeper`: Dependencia requerida por Kafka.
-   `kafka`: El broker de mensajería de Apache Kafka.
-   `consumer`: La aplicación consumidora.

El servicio del `producer` no está en el `docker-compose.yml` y debe ser ejecutado por separado.

### Cómo Ejecutar el Sistema

#### 1. Iniciar la Infraestructura y el Consumidor
Desde el directorio raíz del proyecto, navegue a la carpeta `consumer` y ejecute:

```bash
# Desde /consumer
docker-compose up --build
```
Este comando construirá la imagen de la aplicación `consumer` y levantará los tres contenedores (`zookeeper`, `kafka`, `consumer`).

#### 2. Ejecutar el Productor
Abra otra terminal. Hay dos formas de ejecutar el productor:

**Opción A: Desde el IDE**
-   Abra el proyecto `producer` en su IDE (e.g., IntelliJ, VSCode).
-   Asegúrese de que el IDE esté configurado para usar Java 17.
-   Ejecute la clase principal `ProducerApplication.java`.

**Opción B: Usando Maven y Java**
-   Navegue al directorio del productor: `cd ../producer`
-   Compile el proyecto y empaquételo:
    ```bash
    ./mvnw clean install
    ```
-   Ejecute el JAR generado:
    ```bash
    java -jar target/producer-0.0.1-SNAPSHOT.jar
    ```

#### 3. Interactuar con la API
Una vez que el productor y el consumidor estén en ejecución, puede enviar peticiones al productor.

**Ejemplo: Crear una persona (usando `curl`)**

```bash
curl -X POST http://localhost:8080/personas \
-H "Content-Type: application/json" \
-d '{
    "id": "USR-123",
    "nombre": "Juan Perez",
    "edad": 30
}'
```
- **Resultado esperado**: Verá la respuesta JSON en su terminal. En la terminal donde se ejecuta el `consumer`, verá un log indicando que el mensaje fue recibido y procesado.

**Ejemplo: Simular un error**

```bash
curl -X POST http://localhost:8080/personas \
-H "Content-Type: application/json" \
-d '{
    "id": "USR-999",
    "nombre": "error",
    "edad": 99
}'
```
- **Resultado esperado**: En la terminal del `consumer`, verá un log de error y trazas de la excepción. El mensaje no será confirmado.

**Ejemplo: Ver el stream de eventos**

```bash
curl http://localhost:8080/personas/stream
```
- **Resultado esperado**: Esta conexión permanecerá abierta. Cada vez que cree una nueva persona (usando el endpoint POST), un nuevo evento SSE aparecerá en esta terminal. 