# URL Shortening Service

A URL shortener REST API built with Spring Boot and PostgreSQL. Shortens links, tracks visit counts, and handles concurrent requests safely using atomic inserts and database-level uniqueness constraints.

> Project from [roadmap.sh](https://roadmap.sh/projects/url-shortening-service)

---

## Screenshot

<img width="762" height="736" alt="image" src="https://github.com/user-attachments/assets/02cf34bb-5a44-4ed2-b8d2-7668c4f9a06f" />

---

## Stack

- Java + Spring Boot
- PostgreSQL
- Thymeleaf
- JPA / Hibernate
- Docker + Docker Compose

---

## Design Decisions

**Short code generation** uses `SecureRandom` + Base62 `[0-9A-Za-z]` with 7 characters, giving ~3.5 billion possible combinations. `SecureRandom` ensures codes are cryptographically unpredictable, preventing enumeration of private URLs.

**Collision handling** delegates uniqueness to the database via a `UNIQUE` constraint on `short_code`. On collision, `DataIntegrityViolationException` is caught and the generation is retried up to 5 times — eliminating the race condition that a SELECT + INSERT approach would have.

**Visit tracking** uses an atomic `UPDATE SET access_count = access_count + 1` query inside a `@Transactional` method, preventing lost increments under concurrent requests.

---

## API Endpoints

| Method   | Endpoint                        | Status | Description               |
|----------|---------------------------------|--------|---------------------------|
| `GET`    | `/`                             | 200    | Frontend                  |
| `POST`   | `/short-urls`                   | 201    | Create a short URL        |
| `GET`    | `/short-urls`                   | 200    | List all URLs (paginated) |
| `GET`    | `/short-urls/{shortCode}`       | 200    | Get a short URL by code   |
| `PUT`    | `/short-urls/{shortCode}`       | 200    | Update a short URL        |
| `DELETE` | `/short-urls/{shortCode}`       | 204    | Delete a short URL        |
| `GET`    | `/short-urls/{shortCode}/stats` | 200    | Get visit statistics      |
| `GET`    | `/{shortCode}`                  | 302    | Redirect to original URL  |

### POST `/short-urls`
```json
// Request
{ "url": "https://www.example.com/some/long/url" }

// Response 201
{
  "id": 1,
  "url": "https://www.example.com/some/long/url",
  "shortCode": "aB3kZ9x",
  "createdAt": "2021-09-01T12:00:00Z",
  "updatedAt": "2021-09-01T12:00:00Z"
}
```

### GET `/short-urls/{shortCode}`
```json
// Response 200
{
  "id": 1,
  "url": "https://www.example.com/some/long/url",
  "shortCode": "aB3kZ9x",
  "createdAt": "2021-09-01T12:00:00Z",
  "updatedAt": "2021-09-01T12:00:00Z"
}
```

### PUT `/short-urls/{shortCode}`
```json
// Request
{ "url": "https://www.example.com/some/updated/url" }

// Response 200
{
  "id": 1,
  "url": "https://www.example.com/some/updated/url",
  "shortCode": "aB3kZ9x",
  "createdAt": "2021-09-01T12:00:00Z",
  "updatedAt": "2021-09-01T12:30:00Z"
}
```

### GET `/short-urls/{shortCode}/stats`
```json
// Response 200
{
  "id": 1,
  "url": "https://www.example.com/some/long/url",
  "shortCode": "aB3kZ9x",
  "createdAt": "2021-09-01T12:00:00Z",
  "updatedAt": "2021-09-01T12:00:00Z",
  "accessCount": 10
}
```

### GET `/short-urls?page=0&size=10`
```json
// Response 200
{
  "content": [...],
  "number": 0,
  "totalPages": 5,
  "totalElements": 47
}
```

---

## Run locally

**Prerequisites:** Docker + Docker Compose.

```bash
git clone https://github.com/BRYANR1910/UrlShortener.git
cd UrlShortener
docker compose up --build
```

App available at `http://localhost:8080`
