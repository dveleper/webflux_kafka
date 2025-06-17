# Comandos Gradle para el Microservicio Consumidor (persona-consumer-scaffold)

Este archivo contiene los comandos de Gradle más comunes para gestionar, construir y ejecutar el microservicio consumidor.

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

Por ejemplo, para construir solo el módulo `kafka-consumer`:

```bash
./gradlew :kafka-consumer:build
```
> **Nota:** Reemplace `:kafka-consumer` con el nombre del módulo que desea gestionar (ej. `:app-service`). Los nombres de los módulos están definidos en `settings.gradle`.

---

## Comandos del Scaffold de Bancolombia

Estos son los comandos del plugin de Gradle de Arquitectura Limpia que se utilizaron para generar los diferentes módulos de este proyecto.

### 1. Creación de la Estructura Base

El primer paso, ejecutado en un directorio vacío (`persona-consumer-scaffold`), fue crear la estructura inicial del proyecto.

```bash
# Se crea un build.gradle temporal solo para poder invocar al wrapper
echo "plugins { id 'co.com.bancolombia.cleanArchitecture' version '3.6.4' }" > build.gradle

# Se invoca al wrapper de gradle para que cree la estructura
gradle ca -Ppackage=co.com.bancolombia -Ptype=reactive -Pname=persona-consumer
```
*   **Acción:** Genera la estructura de directorios base, incluyendo `applications`, `domain` e `infrastructure`, junto con los archivos de configuración principales como `settings.gradle`, `main.gradle`, etc.

### 2. Generación de Puntos de Entrada (Entry Points)

Para procesar mensajes desde un tópico de Kafka, se generó un punto de entrada de tipo `kafka`.

```bash
./gradlew generateEntryPoint --type kafka
```
*   **Acción:** Crea el módulo `infrastructure/entry-points/kafka-consumer` con la clase `KafkaConsumer` para recibir y manejar mensajes de forma reactiva.

### 3. Generación del Dominio

El núcleo del negocio reside en la capa de Dominio. El scaffold proporciona comandos para generar la estructura básica de los modelos y los casos de uso.

#### Generar Modelo de Dominio

Este comando crea la entidad de negocio. Para el consumidor, el modelo es el mismo que en el productor.

```bash
./gradlew generateModel --name Persona
```

*   **Acción:**
    *   Crea la clase de la entidad en `domain/model/src/main/java/{packagePath}/model/persona/Persona.java`.
    *   Crea la interfaz del repositorio en `domain/model/src/main/java/{packagePath}/model/persona/gateways/PersonaRepository.java`.
*   **Ajuste Manual:** Dado que este microservicio solo consume y procesa el evento pero no lo persiste, la interfaz `PersonaRepository.java` generada no fue necesaria y podría ser eliminada.

#### Generar Caso de Uso

Este comando crea la clase que contendrá la lógica de negocio para procesar los mensajes entrantes.

```bash
./gradlew generateUseCase --name ProcesarPersona
```

*   **Acción:**
    *   Crea la clase del caso de uso en `domain/usecase/src/main/java/{packagePath}/usecase/procesarpersona/ProcesarPersonaUseCase.java`.
*   **Implementación Manual:** Después de la generación, esta clase se implementó para manejar la lógica de procesamiento del objeto `Persona` recibido desde el punto de entrada de Kafka. 