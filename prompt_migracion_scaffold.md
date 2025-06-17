# Prompt: Migración de Proyecto Spring Boot WebFlux a Scaffold Clean Architecture de Bancolombia

## 🎯 Objetivo
Migrar un proyecto existente de Spring Boot que utiliza WebFlux, Apache Kafka, base de datos H2, Docker, Docker Compose y Maven hacia una estructura basada en el **Scaffold Clean Architecture de Bancolombia** **usando Gradle como sistema de construcción**.

Esta migración debe preservar la funcionalidad original e implementar correctamente los módulos, capas y convenciones del scaffold, incluyendo los siguientes:

- Modelado de entidades en el dominio
- Capa de lógica de negocio (use cases)
- Endpoints reactivos (handlers/routers)
- Integración con Kafka como Publisher/Subscriber
- Persistencia reactiva con H2 y R2DBC
- Configuración con perfiles y propiedades externalizadas
- Migración de sistema de construcción de Maven (`pom.xml`) a Gradle (`build.gradle.kts`)
- Pruebas unitarias y de integración
- Docker y Docker Compose funcionales
- README con instrucciones de ejecución

---

## 📦 Proyecto Base

Voy a proporcionarte el código fuente completo del proyecto base en Spring Boot (estructura Maven). Léelo completamente y realiza las siguientes tareas:

---

## 🛠️ Tareas de Migración

### 1. 📁 Estructura del Proyecto
- Analiza y comprende la estructura actual del proyecto.
- Crea un nuevo proyecto basado en el [Scaffold Clean Architecture de Bancolombia](https://github.com/bancolombia/scaffold-clean-architecture) utilizando **Gradle (versión Kotlin DSL preferiblemente)**.

### 2. ⚙️ Migración de Maven a Gradle
- Transforma la configuración de dependencias y plugins de `pom.xml` a `build.gradle.kts` o `build.gradle`.
- Elimina los archivos Maven (`pom.xml`, `.mvn`, etc.).
- Verifica compatibilidad con las versiones de los plugins para Kafka, WebFlux, y R2DBC.

### 3. 🧠 Migración de Lógica y Componentes

#### Dominio
- Extrae y define las entidades del negocio.
- Implementa las entidades en el módulo `model`.

#### Casos de uso
- Identifica las reglas de negocio y encapsúlalas en `usecases`.

#### Entrada
- Crea handlers y routers para los endpoints WebFlux actuales en el módulo `entry-points/reactive-web`.

#### Salida
- Implementa adaptadores para la base de datos H2 con R2DBC en `driven-adapters/r2dbc`.
- Crea un adaptador Kafka que implemente publicación y consumo de eventos usando Reactive Kafka en `driven-adapters/kafka`.

### 4. ⚙️ Configuración
- Migra las propiedades de `application.yml` o `application.properties` a la estructura de `resources` de cada módulo.
- Implementa `HelperConfiguration` y clases utilitarias si aplica.

### 5. 🧪 Pruebas
- Migra y adapta pruebas unitarias y de integración.
- Implementa mocks, testcontainers o pruebas con perfiles `test`.

### 6. 🐳 Dockerización
- Crea un `Dockerfile` funcional para el nuevo proyecto.
- Adapta o crea un nuevo `docker-compose.yml` que incluya H2 y Kafka para entorno local.

### 7. 📄 Documentación
- Genera un archivo `README.md` que contenga:
  - Descripción de la arquitectura
  - Requisitos
  - Instrucciones para compilar, probar y ejecutar el sistema localmente con Gradle y Docker

---

## ✅ Resultado Esperado

Entregar como resultado:

- Proyecto completamente migrado bajo Scaffold Clean Architecture
- Funcionalidad equivalente a la original
- Código organizado en módulos: model, usecases, entry-points, driven-adapters, helpers
- Configuración y conexión a Kafka, H2 y WebFlux operativas
- Gradle como sistema de construcción (sin rastro de Maven)
- README completo y actualizado
- Instrucciones de ejecución local vía Docker Compose
- Proyecto empaquetado para descarga o disponible para clonar desde un repositorio

---

## 🔁 Consideraciones

- Asegúrate de usar prácticas reactivas (Mono/Flux) en toda la solución.
- Si se encuentra lógica acoplada o mal estructurada, refactorizar para adaptarla a los principios de Clean Architecture.
- Verifica compatibilidad entre plugins de Gradle y dependencias.

---

## 📂 ¿Cómo lo usarás?

1. Entrega el proyecto Spring Boot WebFlux original como entrada (en Maven).
2. Ejecuta este prompt dentro de Cursor AI u otra herramienta compatible.
3. Revisa los resultados generados, probando la nueva solución.
4. Ajusta detalles de configuración si es necesario.
