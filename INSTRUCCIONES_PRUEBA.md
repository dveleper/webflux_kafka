# Guía de Pruebas: Interacción del Sistema Productor-Consumidor

Este documento proporciona las instrucciones paso a paso para ejecutar y probar la interacción entre todos los componentes del sistema: el servicio `productor`, el servicio `consumidor`, `Kafka` y la base de datos `H2`.

## Requisitos Previos
Asegúrate de tener instalado lo siguiente:
-   Java 17 o superior
-   Apache Maven
-   Docker y Docker Compose

---

## Estructura de Terminales
Para realizar la prueba completa, te recomendamos usar 4 terminales distintas para mantener todo claro y organizado.

-   **Terminal 1**: Ejecutará el entorno de Docker (Kafka, Zookeeper, Consumidor).
-   **Terminal 2**: Ejecutará la aplicación `productor`.
-   **Terminal 3**: Monitorizará el tópico de Kafka para ver los mensajes llegar.
-   **Terminal 4**: Enviará las peticiones `curl` a la API del `productor`.

---

## Paso 1: Iniciar el Entorno Base (Kafka y Consumidor)

En la **Terminal 1**, navega a la raíz del proyecto y luego al directorio `consumer`. Desde allí, levanta los contenedores de Docker.

```bash
# Navegar al directorio del consumidor
cd consumer

# Construir y levantar los servicios de Docker Compose
docker-compose up --build
```
Verás muchos logs. Espera a que los servicios se estabilicen. Al final, deberías ver logs del `consumer` indicando que está listo y escuchando.

---

## Paso 2: Iniciar la Aplicación Productora

En la **Terminal 2**, navega al directorio `producer` y ejecuta la aplicación con Maven.

```bash
# Navegar al directorio del productor
cd producer

# Ejecutar la aplicación Spring Boot
./mvnw spring-boot:run
```
Espera a que la aplicación Spring Boot se inicie completamente.

---

## Paso 3: Monitorizar el Tópico de Kafka

En la **Terminal 3**, vamos a abrir una consola dentro del contenedor de Kafka para ver los mensajes del tópico `personas-topic` en tiempo real.

```bash
docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic personas-topic --from-beginning
```
Esta terminal se quedará esperando. Aquí aparecerá cualquier mensaje que se envíe al tópico.

---

## Paso 4: Realizar la Prueba de Interacción

Ahora que todo está en marcha, vamos a enviar una petición para crear una persona y verificar que todo el flujo funciona.

### 4.1. Enviar una Petición a la API

En la **Terminal 4**, ejecuta el siguiente comando `curl` para crear una nueva persona.

<details>
<summary>Comando para <b>bash (Git Bash, WSL, macOS, Linux)</b></summary>

```bash
curl -X POST http://localhost:8080/personas \
-H "Content-Type: application/json" \
-d '{
    "id": "TEST-001",
    "nombre": "Carlos Ruiz",
    "edad": 35
}'
```
</details>

<details>
<summary>Comando para <b>Windows PowerShell</b></summary>

```powershell
Invoke-WebRequest -Uri http://localhost:8080/personas -Method POST -ContentType "application/json" -Body '{"id": "TEST-001", "nombre": "Carlos Ruiz", "edad": 35}'
```
</details>

Si todo va bien, recibirás una respuesta JSON `200 OK` con los datos que enviaste.

### 4.2. Verificar el Mensaje en Kafka

Inmediatamente después de ejecutar el `curl`, mira la **Terminal 3** (la del `kafka-console-consumer`). Deberías ver aparecer el mensaje en formato JSON:

```json
{"id":"TEST-001","nombre":"Carlos Ruiz","edad":35}
```
Esto confirma que el `productor` envió el mensaje correctamente y Kafka lo recibió.

### 4.3. Verificar los Datos en la Base de Datos H2

Finalmente, vamos a comprobar que el `productor` también guardó los datos en su base de datos interna.

1.  Abre tu navegador web y ve a la siguiente URL:
    [**http://localhost:8080/h2-console**](http://localhost:8080/h2-console)

2.  En la página de inicio de sesión de H2, asegúrate de que los datos sean correctos y haz clic en **Connect**:
    -   **JDBC URL**: `jdbc:h2:mem:testdb`
    -   **User Name**: `sa`
    -   **Password**: (déjalo en blanco)

3.  Una vez dentro, escribe la siguiente consulta SQL y haz clic en **Run**:
    ```sql
    SELECT * FROM personas;
    ```

4.  Deberías ver una tabla con una fila que contiene los datos de "Carlos Ruiz" que insertaste. Anota el valor de la columna `DB_ID` (probablemente `1`) para usarlo en el siguiente paso.

---

## Paso 5: Probar las Operaciones de Lectura de la API

Ahora vamos a probar los endpoints `GET`. Puedes usar la **Terminal 4** para estos comandos.

### 5.1. Obtener Todas las Personas

Este comando te devolverá un array JSON con todas las personas que has creado.

<details>
<summary>Comando para <b>bash (Git Bash, WSL, macOS, Linux)</b></summary>

```bash
curl http://localhost:8080/personas
```
</details>

<details>
<summary>Comando para <b>Windows PowerShell</b></summary>

```powershell
Invoke-WebRequest -Uri http://localhost:8080/personas -Method GET
```
</details>

### 5.2. Obtener una Persona por su ID

Este comando devuelve una única persona. Reemplaza el `1` al final de la URL con el `DB_ID` que viste en la consola H2.

<details>
<summary>Comando para <b>bash (Git Bash, WSL, macOS, Linux)</b></summary>

```bash
curl http://localhost:8080/personas/1
```
</details>

<details>
<summary>Comando para <b>Windows PowerShell</b></summary>

```powershell
Invoke-WebRequest -Uri http://localhost:8080/personas/1 -Method GET
```
</details>

---

## Paso 6: Probar el Stream de Eventos en Tiempo Real (SSE)

Esta prueba te permite ver cómo el servidor notifica a los clientes en tiempo real cuando se crea una nueva persona.

1.  **Abre el Stream**: La forma más sencilla de probar esto es abrir la siguiente URL en tu **navegador web** (Chrome, Firefox, Edge):
    [**http://localhost:8080/personas/stream**](http://localhost:8080/personas/stream)
    La página parecerá que está cargando indefinidamente. Esto es normal, está esperando eventos.

2.  **Crea una Nueva Persona**: Vuelve a la **Terminal 4** y envía otra petición `POST` para crear una persona diferente. Puedes usar los comandos del Paso 4.1, pero cambiando los datos.

    <details>
    <summary>Comando para <b>bash</b></summary>

    ```bash
    curl -X POST http://localhost:8080/personas \
    -H "Content-Type: application/json" \
    -d '{"id": "TEST-002", "nombre": "Laura Gomez", "edad": 28}'
    ```
    </details>

    <details>
    <summary>Comando para <b>PowerShell</b></summary>

    ```powershell
    Invoke-WebRequest -Uri http://localhost:8080/personas -Method POST -ContentType "application/json" -Body '{"id": "TEST-002", "nombre": "Laura Gomez", "edad": 28}'
    ```
    </details>

3.  **Observa el Resultado**: Inmediatamente después de enviar la petición, vuelve a la ventana de tu navegador. Verás que un nuevo evento ha aparecido en la página, mostrando los datos de "Laura Gomez" en formato JSON.

¡Felicidades! Si has completado todos estos pasos, has verificado con éxito el flujo completo del sistema. 