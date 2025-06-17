# Comandos Gradle para el Microservicio Productor (persona-producer-scaffold)

Este archivo contiene los comandos de Gradle más comunes para gestionar, construir y ejecutar el microservicio productor.

## Comandos Principales

### Construir el Proyecto

Este comando compila el código fuente, ejecuta las pruebas y empaqueta la aplicación en un archivo JAR.

```bash
./gradlew build
```

Después de una construcción exitosa, el artefacto principal se encontrará en `applications/app-service/build/libs/app-service.jar`.

### Limpiar el Proyecto

Este comando elimina el directorio `build` de todos los módulos, limpiando los artefactos de compilaciones anteriores.

```bash
./gradlew clean
```

### Ejecutar la Aplicación

Hay dos formas principales de ejecutar la aplicación:

1.  **Usando el plugin de Gradle (para desarrollo):**
    Este comando compila y ejecuta la aplicación directamente. Es ideal para el desarrollo y pruebas rápidas.

    ```bash
    ./gradlew bootRun
    ```

2.  **Ejecutando el JAR (para producción):**
    Este método es el preferido para entornos de producción. Primero, asegúrate de haber construido el proyecto.

    ```bash
    java -jar applications/app-service/build/libs/app-service.jar
    ```

### Ejecutar las Pruebas

Este comando ejecuta todas las pruebas unitarias y de integración del proyecto.

```bash
./gradlew test
```

## Comandos por Módulo

La arquitectura limpia divide el proyecto en módulos. Aunque generalmente se gestiona el proyecto como un todo, es posible ejecutar tareas en módulos específicos.

Por ejemplo, para construir solo el módulo `reactive-web`:

```bash
./gradlew :reactive-web:build
```
> **Nota:** Reemplace `:reactive-web` con el nombre del módulo que desea gestionar (ej. `:app-service`, `:r2dbc-h2`, `:async-event-bus`). Los nombres de los módulos están definidos en `settings.gradle`.

---

## Comandos del Scaffold de Bancolombia

Estos son los comandos del plugin de Gradle de Arquitectura Limpia que se utilizaron para generar los diferentes módulos de este proyecto.

### 1. Creación de la Estructura Base

El primer paso, ejecutado en un directorio vacío (`persona-producer-scaffold`), fue crear la estructura inicial del proyecto.

```bash
# Se crea un build.gradle temporal solo para poder invocar al wrapper
echo "plugins { id 'co.com.bancolombia.cleanArchitecture' version '3.6.4' }" > build.gradle

# Se invoca al wrapper de gradle para que cree la estructura
gradle ca -Ppackage=co.com.bancolombia -Ptype=reactive -Pname=persona-producer
```
*   **Acción:** Genera la estructura de directorios base, incluyendo `applications`, `domain` e `infrastructure`, junto con los archivos de configuración principales como `settings.gradle`, `main.gradle`, etc.

### 2. Generación de Puntos de Entrada (Entry Points)

Para exponer la funcionalidad del caso de uso a través de una API REST reactiva, se generó un módulo `reactive-web`.

```bash
./gradlew generateEntryPoint --type reactive-web
```
*   **Acción:** Crea el módulo `infrastructure/entry-points/reactive-web` con las clases `ApiRouter` y `ApiHandler` para gestionar las peticiones HTTP.

### 3. Generación de Adaptadores Dirigidos (Driven Adapters)

Se generaron dos adaptadores para conectar el dominio con tecnologías externas:

#### Adaptador de Base de Datos (R2DBC - H2)

Inicialmente, se genera un adaptador para bases de datos reactivas (el default es PostgreSQL).

```bash
./gradlew generateDrivenAdapter --type r2dbc
```
*   **Acción:** Crea el módulo `infrastructure/driven-adapters/r2dbc-postgresql`.
*   **Adaptación manual a H2:**
    1.  Se renombró la carpeta a `r2dbc-h2`.
    2.  Se actualizó `settings.gradle` para apuntar al nuevo nombre del directorio.
    3.  Se modificó el `build.gradle` del módulo para reemplazar la dependencia de `postgresql` por la de `h2`.

#### Adaptador de Eventos (Kafka)

Para publicar mensajes en un tópico de Kafka, se generó un adaptador de bus de eventos asíncrono.

```bash
./gradlew generateDrivenAdapter --type asynceventbus --tech kafka
```
*   **Acción:** Crea el módulo `infrastructure/driven-adapters/async-event-bus` con la clase `KafkaEventPublisher` para enviar eventos.

### 4. Generación del Dominio

El núcleo del negocio reside en la capa de Dominio. El scaffold proporciona comandos para generar la estructura básica de los modelos y los casos de uso.

#### Generar Modelo de Dominio

Este comando crea la entidad de negocio y su puerto (interfaz de repositorio) principal.

```bash
./gradlew generateModel --name Persona
```

*   **Acción:**
    *   Crea la clase de la entidad en `domain/model/src/main/java/{packagePath}/model/persona/Persona.java`.
    *   Crea la interfaz del repositorio en `domain/model/src/main/java/{packagePath}/model/persona/gateways/PersonaRepository.java`.
*   **Ajuste Manual:** En este proyecto, se necesitaba un puerto adicional para la publicación de eventos. Se creó manualmente la interfaz `PersonaEventsGateway.java` dentro del mismo paquete `gateways`.

#### Generar Caso de Uso

Este comando crea la clase que contendrá la lógica de negocio principal.

```bash
./gradlew generateUseCase --name Persona
```

*   **Acción:**
    *   Crea la clase del caso de uso en `domain/usecase/src/main/java/{packagePath}/usecase/persona/PersonaUseCase.java`.
*   **Implementación Manual:** Después de la generación, esta clase se implementó manualmente para orquestar la lógica de guardar la persona y publicar el evento, utilizando los puertos definidos en el modelo. 