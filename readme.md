# API de Votaciones

API RESTful para gestionar un sistema de votaciones: registro de votantes y candidatos, emisión de un único voto por votante y estadísticas de resultados.

## Tecnologías

- Java 17
- Spring Boot 3.5 (Web, Data JPA, Validation, Security)
- PostgreSQL 16
- JWT (jjwt)
- Swagger / OpenAPI (springdoc)
- Maven (con Maven Wrapper)
- Docker y Docker Compose

## Estructura

```
votaciones-api/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── database/
│   └── init.sql
├── docs/
│   ├── estadisticas-swagger.png
│   └── estadisticas-postman.png
├── src/
│   └── main/
│       ├── java/com/votaciones/
│       │   ├── VotacionesApplication.java
│       │   ├── config/
│       │   │   ├── OpenApiConfig.java
│       │   │   └── SecurityConfig.java
│       │   ├── security/
│       │   │   ├── JwtAuthFilter.java
│       │   │   └── JwtService.java
│       │   ├── controller/
│       │   │   ├── AuthController.java
│       │   │   ├── VoterController.java
│       │   │   ├── CandidateController.java
│       │   │   └── VoteController.java
│       │   ├── service/
│       │   │   ├── AuthService.java
│       │   │   ├── VoterService.java
│       │   │   ├── CandidateService.java
│       │   │   └── VoteService.java
│       │   ├── repository/
│       │   │   ├── AppUserRepository.java
│       │   │   ├── VoterRepository.java
│       │   │   ├── CandidateRepository.java
│       │   │   └── VoteRepository.java
│       │   ├── entity/
│       │   │   ├── AppUser.java
│       │   │   ├── Voter.java
│       │   │   ├── Candidate.java
│       │   │   └── Vote.java
│       │   ├── dto/
│       │   │   ├── AuthRequest.java
│       │   │   ├── TokenResponse.java
│       │   │   ├── VoterRequest.java
│       │   │   ├── VoterResponse.java
│       │   │   ├── CandidateRequest.java
│       │   │   ├── CandidateResponse.java
│       │   │   ├── CandidateStatistics.java
│       │   │   ├── VoteRequest.java
│       │   │   ├── VoteResponse.java
│       │   │   └── VoteStatistics.java
│       │   └── exception/
│       │       ├── ApiException.java
│       │       ├── ErrorResponse.java
│       │       └── GlobalExceptionHandler.java
│       └── resources/
│           └── application.yml
├── .dockerignore
├── .gitattributes
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
├── pom.xml
├── run-local.sh
└── README.md
```

## Requisitos

- Docker (opción 1)
- JDK 17+ y PostgreSQL en ejecución (opciones 2 y 3)
- No necesitas instalar Maven: el proyecto incluye Maven Wrapper (`mvnw` / `mvnw.cmd`), que lo descarga automáticamente la primera vez.

## Ejecución rápida

### Opción 1: Docker (un solo comando)

```bash
docker compose up --build
```

Levanta PostgreSQL ejecutando `database/init.sql`, espera a que esté listo, construye la API y la inicia en el puerto 8080.

```bash
docker compose down        # detener
docker compose down -v     # detener y borrar los datos
```

### Opción 2: Local (un solo comando)

Requiere JDK 17+ y PostgreSQL instalado y en ejecución.

```bash
chmod +x run-local.sh
./run-local.sh
```

El script ejecuta `database/init.sql` (crea la base `votaciones` y todas las tablas) y arranca la API con Maven Wrapper.

En Windows se ejecuta desde Git Bash o WSL.

### Opción 3: Manual

Base de datos con Docker:

```bash
docker compose up -d db
./mvnw spring-boot:run
```

Base de datos local, con un solo script:

```bash
psql -U postgres -f database/init.sql
./mvnw spring-boot:run
```

En Windows (PowerShell o CMD) usa `.\mvnw.cmd spring-boot:run`.

El script `init.sql` usa comandos de `psql`, por lo que debe ejecutarse con `psql` y no desde un cliente gráfico.

## Configuración

Valores por defecto en `src/main/resources/application.yml`, sobrescribibles con variables de entorno:

| Variable | Por defecto |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/votaciones` |
| `DB_USER` | `postgres` |
| `DB_PASSWORD` | `postgres` |
| `JWT_SECRET` | clave de desarrollo (mínimo 32 caracteres) |

`run-local.sh` también acepta `DB_HOST` y `DB_PORT`.

La API queda en `http://localhost:8080` y la documentación Swagger en `http://localhost:8080/swagger-ui.html`.

## Autenticación

Todos los endpoints, excepto `/auth/**` y Swagger, requieren un token JWT.

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Respuesta:

```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

Guarda el token y envíalo en cada petición:

```bash
export TOKEN="eyJhbGciOiJIUzI1NiJ9..."
```

En Swagger, usa el botón **Authorize** y pega el token.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/auth/register` | Registrar usuario y obtener token |
| POST | `/auth/login` | Iniciar sesión y obtener token |
| POST | `/voters` | Registrar votante |
| GET | `/voters` | Listar votantes (filtros y paginación) |
| GET | `/voters/{id}` | Detalle de un votante |
| DELETE | `/voters/{id}` | Eliminar votante |
| POST | `/candidates` | Registrar candidato |
| GET | `/candidates` | Listar candidatos (filtros y paginación) |
| GET | `/candidates/{id}` | Detalle de un candidato |
| DELETE | `/candidates/{id}` | Eliminar candidato |
| POST | `/votes` | Emitir un voto |
| GET | `/votes` | Listar todos los votos |
| GET | `/votes/statistics` | Estadísticas de la votación |

### Filtros y paginación

| Recurso | Parámetros |
|---|---|
| `/voters` | `name`, `email`, `has_voted`, `page`, `size`, `sort` |
| `/candidates` | `name`, `party`, `page`, `size`, `sort` |

Por defecto: `page=0`, `size=10`, orden por `id`.

## Ejemplos de uso

### Registrar votantes

```bash
curl -X POST http://localhost:8080/voters \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Carlos Perez","email":"carlos@example.com"}'
```

```bash
curl -X POST http://localhost:8080/voters \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Maria Lopez","email":"maria@example.com"}'
```

```bash
curl -X POST http://localhost:8080/voters \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Juan Rojas","email":"juan@example.com"}'
```

Respuesta:

```json
{
  "id": 1,
  "name": "Carlos Perez",
  "email": "carlos@example.com",
  "has_voted": false
}
```

### Registrar candidatos

```bash
curl -X POST http://localhost:8080/candidates \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Ana Torres","party":"Partido Verde"}'
```

```bash
curl -X POST http://localhost:8080/candidates \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Luis Gomez","party":"Partido Azul"}'
```

### Listar con filtros y paginación

```bash
curl "http://localhost:8080/voters?has_voted=false&name=car&page=0&size=5&sort=name,asc" \
  -H "Authorization: Bearer $TOKEN"
```

```json
{
  "content": [
    { "id": 1, "name": "Carlos Perez", "email": "carlos@example.com", "has_voted": false }
  ],
  "page": { "size": 5, "number": 0, "total_elements": 1, "total_pages": 1 }
}
```

```bash
curl "http://localhost:8080/candidates?party=verde" \
  -H "Authorization: Bearer $TOKEN"
```

### Emitir votos

```bash
curl -X POST http://localhost:8080/votes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"voter_id":1,"candidate_id":1}'
```

```bash
curl -X POST http://localhost:8080/votes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"voter_id":2,"candidate_id":1}'
```

```bash
curl -X POST http://localhost:8080/votes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"voter_id":3,"candidate_id":2}'
```

Respuesta:

```json
{ "id": 1, "voter_id": 1, "candidate_id": 1 }
```

### Listar votos

```bash
curl http://localhost:8080/votes -H "Authorization: Bearer $TOKEN"
```

### Estadísticas

```bash
curl http://localhost:8080/votes/statistics -H "Authorization: Bearer $TOKEN"
```

```json
{
  "total_votes": 3,
  "voters_who_voted": 3,
  "candidates": [
    { "candidate_id": 1, "name": "Ana Torres", "party": "Partido Verde", "votes": 2, "percentage": 66.67 },
    { "candidate_id": 2, "name": "Luis Gomez", "party": "Partido Azul", "votes": 1, "percentage": 33.33 }
  ]
}
```

### Capturas de las estadísticas

![Estadísticas en Swagger](docs/estadisticas-swagger.png)

![Estadísticas en Postman](docs/estadisticas-postman.png)

## Validaciones y reglas de negocio

- Un votante no puede registrarse como candidato y viceversa (se compara el nombre sin distinguir mayúsculas).
- El correo del votante es único.
- Cada votante puede votar una sola vez; el votante se bloquea durante la operación para evitar votos duplicados concurrentes, y `voter_id` es único en la tabla `votes`.
- El `candidate_id` debe existir.
- Al votar, `has_voted` pasa a `true` y los votos del candidato se incrementan en la misma transacción.
- No se puede eliminar un votante que ya votó ni un candidato que tiene votos.

## Manejo de errores

Formato de respuesta:

```json
{
  "timestamp": "2026-10-08T15:30:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "El votante ya emitió su voto"
}
```

| Código | Caso |
|---|---|
| 400 | Datos inválidos o parámetros mal formados |
| 401 | Token ausente, inválido o credenciales incorrectas |
| 404 | Votante o candidato inexistente |
| 409 | Duplicados, voto repetido o conflicto de integridad |