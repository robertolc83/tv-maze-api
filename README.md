# tv-maze-api

API middleware que consume los servicios públicos de [TVMaze](https://www.tvmaze.com/api) para búsqueda y consulta de shows, con persistencia en MongoDB Atlas para cache de shows y almacenamiento de comentarios/calificaciones.

## Descripción general

El API expone tres endpoints principales:

1. **Búsqueda de shows** — busca shows en TVMaze por texto libre, devolviendo un resumen enriquecido con los comentarios guardados de cada uno.
2. **Consulta de show por ID** — devuelve el objeto completo del show, usando un patrón de **cache-aside** contra MongoDB (si el show ya fue consultado antes, se sirve desde la base de datos sin volver a llamar a TVMaze), enriquecido con sus comentarios guardados.
3. **Registro de comentarios** — permite guardar una calificación (0-5) y un comentario de texto asociados a un show por su ID.

## Arquitectura

El proyecto sigue una arquitectura por capas típica de Spring Boot:

```
Cliente HTTP
    │
    ▼
Controller        → expone los endpoints REST, valida el request (@Valid)
    │
    ▼
Service           → lógica de negocio: mapeo de DTOs, orquestación de cache,
    │                resolución de channel (network/webChannel), enriquecido con comentarios
    ▼
Repository/Client → acceso a datos
    ├── TvMazeClient        → llamadas HTTP al API externo de TVMaze (RestClient)
    ├── ShowCacheRepository → cache de shows en Mongo (MongoTemplate + org.bson.Document)
    └── CommentRepository   → CRUD de comentarios en Mongo (MongoRepository)
```

**Manejo de errores**: centralizado en `GlobalExceptionHandler` (`@RestControllerAdvice`), que traduce excepciones técnicas (404 de TVMaze, errores de validación de Bean Validation) a respuestas JSON consistentes, y loguea cualquier error inesperado sin exponer detalles internos al cliente.

**Estructura de paquetes**:
```
com.pinwox.tvmazeapi
├── config          # Beans de configuración (RestClient)
├── controller      # Endpoints REST
├── exception       # Excepciones custom + manejador global
├── model
│   ├── dto         # Objetos de transferencia (request/response)
│   └── external    # Modelos que mapean la respuesta cruda de TVMaze
├── repository      # Acceso a datos (Mongo)
└── service         # Lógica de negocio
```

## Stack tecnológico

| Componente | Versión / detalle |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.1 |
| Spring Framework | 7.0.9 |
| Build tool | Maven |
| Base de datos | MongoDB Atlas (free tier M0) |
| Cliente HTTP | `RestClient` (Spring Boot 4) |
| Validación | Jakarta Bean Validation (`spring-boot-starter-validation`) |
| Boilerplate | Lombok |

## Endpoints

### `GET /api/search?search_query={texto}`

Busca shows en TVMaze por texto y devuelve un resumen de cada uno, incluyendo sus comentarios guardados.

**Respuesta:**
```json
[
  {
    "id": 139,
    "name": "Girls",
    "channel": "HBO",
    "summary": "<p>...</p>",
    "genres": ["Drama", "Romance"],
    "comments": [
      { "comment": "Excelente serie", "rating": 5 }
    ]
  }
]
```

### `GET /api/shows/{show_id}`

Devuelve el objeto completo del show (tal como lo entrega TVMaze), agregando su arreglo de comentarios guardados.

- Si el show ya fue consultado antes, se sirve desde el cache de Mongo (colección `shows`).
- Si no, se consulta a TVMaze, se guarda en cache, y se devuelve.
- Los comentarios **nunca** se persisten dentro del cache — se consultan y agregan en cada respuesta, para que comentarios nuevos aparezcan de inmediato aunque el show ya esté cacheado.
- Si el `show_id` no existe en TVMaze, devuelve `404` con un mensaje descriptivo.

### `POST /api/comments`

Guarda un comentario y calificación asociados a un show.

**Request body:**
```json
{
  "showId": 139,
  "comment": "Excelente serie",
  "rating": 5
}
```

**Validaciones:**
- `showId`: requerido.
- `comment`: no puede estar vacío.
- `rating`: requerido, entre 0 y 5.

**Respuesta exitosa:** `201 Created`
```json
{ "status": 201, "message": "Comentario guardado correctamente" }
```

**Respuesta de validación fallida:** `400 Bad Request`
```json
{
  "status": 400,
  "error": "Bad Request",
  "errors": { "rating": "rating debe ser entre 0 y 5" }
}
```

## Configuración y requisitos previos

### 1. MongoDB Atlas

Se requiere un cluster de MongoDB Atlas (puede ser el free tier M0) con:
- Un usuario de base de datos con permisos de lectura/escritura.
- **Network Access sin restricción de IP** (`0.0.0.0/0`), para permitir conexión desde cualquier entorno de evaluación.

### 2. Variable de entorno

El proyecto requiere la variable `MONGODB_URI` con la connection string de Atlas:

```
MONGODB_URI=mongodb+srv://<usuario>:<password>@<cluster>.mongodb.net/tvmaze?retryWrites=true&w=majority&appName=Cluster0
```

Si no se define, la aplicación intentará conectarse a una instancia local (`mongodb://localhost:27017/tvmaze`) — útil para desarrollo con Mongo local, pero no recomendado para evaluación.

**Configuración recomendada (desarrollo local):**

Crea un archivo `.env` en la raíz del proyecto (ya excluido en `.gitignore`, nunca se debe subir al repositorio):
```
MONGODB_URI=mongodb+srv://usuario:password@cluster0.xxxxx.mongodb.net/tvmaze?retryWrites=true&w=majority&appName=Cluster0
```

Y cárgalo antes de levantar la aplicación:
```bash
export $(cat .env | xargs)
```

> ⚠️ **Nota sobre Spring Boot 4**: la propiedad de conexión a Mongo cambió de `spring.data.mongodb.uri` (Boot 2.x/3.x) a **`spring.mongodb.uri`** en Boot 4.x. Este proyecto ya usa la sintaxis correcta en `application.yml`.

### 3. Variables opcionales

| Variable | Default | Descripción |
|---|---|---|
| `SERVER_PORT` | `8080` | Puerto en el que corre la aplicación |

## Cómo correr el proyecto

**Compilar:**
```bash
./mvnw clean install
```

**Correr en modo desarrollo:**
```bash
export $(cat .env | xargs)
./mvnw spring-boot:run
```

**Correr el JAR empaquetado:**
```bash
./mvnw clean package -DskipTests
MONGODB_URI="mongodb+srv://..." java -jar target/tv-maze-api-0.0.1-SNAPSHOT.jar
```

La aplicación queda disponible en `http://localhost:8080`.

## Ejemplos de prueba (curl)

```bash
# Buscar shows
curl "http://localhost:8080/api/search?search_query=girls" | jq

# Obtener un show por ID
curl "http://localhost:8080/api/shows/139" | jq

# Guardar un comentario
curl -X POST http://localhost:8080/api/comments \
  -H "Content-Type: application/json" \
  -d '{"showId": 139, "comment": "Excelente serie", "rating": 5}'
```

## Decisiones de diseño

- **`Map<String, Object>` para el show completo (Endpoint B)**: como TVMaze devuelve un objeto con muchos campos anidados y variables, y el requerimiento es persistir/devolver el objeto tal cual (sin transformación), se optó por un documento dinámico en vez de un modelo Java estricto. Esto evita acoplar el proyecto a la forma exacta del esquema de TVMaze, que puede cambiar.
- **DTOs estrictos para Search y Comments**: en estos casos sí se conoce y transforma la forma exacta de los datos (resolución de `channel`, validación de `rating`), por lo que se usan clases tipadas (`ShowSummaryDTO`, `CommentRequestDTO`).
- **Exception translation**: las excepciones técnicas de librerías externas (ej. `HttpClientErrorException.NotFound` de Spring) se traducen a excepciones de dominio propias (`ShowNotFoundException`) antes de llegar al controller, para desacoplar la capa de negocio del cliente HTTP específico usado.
- **Comentarios nunca se cachean junto al show**: se consultan en cada request para reflejar comentarios nuevos sin depender del estado del cache.
