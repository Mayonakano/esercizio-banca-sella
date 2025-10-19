# Banca Sella API

A Spring Boot application that exposes REST endpoints to:
- Retrieve the current account balance
- Create a money transfer
- List account transactions in an accounting-date range

The application integrates with the external Fabrick API via OpenFeign, documents APIs with SpringDoc OpenAPI (Swagger UI), persists transfer data to an embedded H2 database, and includes validation and global error handling.


## Tech stack
- Java 17
- Spring Boot 3.2.x
  - Web, Validation, Data JPA
- OpenFeign (Spring Cloud) for HTTP client
- SpringDoc OpenAPI + Swagger UI
- H2 (runtime) for persistence
- Lombok
- Maven


## How it works (high level)
- Controller layer: `OperationsController` exposes the REST endpoints under `/operation/*` and performs validation as well as mapping of common exceptions to HTTP statuses.
- Service layer: `FabrickService` orchestrates calls to the external Fabrick API (through `FabrickClient`) and performs simple transformations/persistence (e.g., saves money transfer data using `BankTransferRepository`).
- Feign client: `FabrickClient` defines the remote Fabrick API surface. `FeignClientAuthInterceptor` adds the required headers (Api-Key, Auth-Schema, X-Time-Zone, X-Request-Id, JSON content headers).
- Persistence: Money transfer responses are mapped to entities (e.g., `BankTransferEntity`, `CreditorEntity`, `AmountEntity`, `FeeEntity`) and saved via Spring Data JPA. H2 is used by default at runtime for easy local usage.
- Error handling: `OperationsController` catches `IllegalArgumentException` (→ 400) and `NullPointerException` (→ 404).
- API documentation: SpringDoc exposes generated OpenAPI docs and Swagger UI.


## Requirements
- JDK 17+
- Maven 3.9+
- Internet access if you want to call the real Fabrick API endpoint

Optional but recommended:
- An IDE with Lombok support enabled


## Configuration
Set the Fabrick API connection and account parameters in `src/main/resources/application.properties` (or via environment variables / JVM system properties). The app also auto-imports optional `./secrets.properties` or `./secrets.env` files from the project root if present.

```
# Fabrick API base URL (defaults to http://localhost:8080 if FABRICK_URL is not set)
fabrick_client.url=${FABRICK_URL:http://localhost:8080}

# The account id used for all operations
fabrick_client.accountId=${FABRICK_ACCOUNT_ID:}

# API authentication
fabrick_client.api_key=${FABRICK_API_KEY:}
fabrick_client.auth_schema=${FABRICK_AUTH_SCHEMA:S2S}
```

You can override via:
- JVM system properties: `-Dfabrick_client.accountId=...`, `-Dfabrick_client.api_key=...`, `-Dfabrick_client.auth_schema=...`, `-Dfabrick_client.url=...`
- Environment variables: `FABRICK_ACCOUNT_ID`, `FABRICK_API_KEY`, `FABRICK_AUTH_SCHEMA`, `FABRICK_URL`
- Root-level `secrets.properties`/`secrets.env` loaded automatically (see `spring.config.import` in application.properties)


## Build and run

1) Run with Maven (dev):
- `create a configuration with all environment required variables and set the correct jdk >= 17`
- `mvn clean install spring-boot:run`

2) Run without Maven after the run
- `create a configuration with all environment required variables on vmArgs and set the correct jdk >= 17`
- `set the path of the main class`

The application starts on port 8080 by default. To change it:
- `-Dserver.port=8080` or set `server.port=8080` in `application.properties`.


## API documentation (Swagger/OpenAPI)
- Swagger UI:
  - http://localhost:8080/swagger-ui.html
  - or http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

APIs are grouped under the tag "Operations" with descriptions and schema details generated from your code and validation annotations.


## REST endpoints
Base path: `/operation`

1) GET `/operation/accountBalance`
- Description: Returns the current balance for the configured account.
- Success: `200 OK` with a numeric body (BigDecimal), e.g., `100.0`.
- Errors:
  - `400 Bad Request` (e.g., invalid configuration)
  - `404 Not Found` (payload not found upstream)

Curl example:
```
curl -X GET http://localhost:8080/operation/accountBalance
```

2) POST `/operation/transaction`
- Description: Creates a money transfer for the configured account.
- Request body (MoneyTransferRequest):
  - Required fields: `creditor`, `description`, `amount`, `currency (3 letters)`.
  - Optional fields: `executionDate (yyyy-MM-dd)`, `uri`, `isUrgent`, `isInstant`, `feeType`, `feeAccountId`, `taxRelief`.
  - If `taxRelief` is provided, its inner fields `isCondoUpgrade`, `creditorFiscalCode`, and `beneficiaryType` are required.
- Success: `200 OK` with a `MoneyTransferResponse` JSON body.
- Errors:
  - `400 Bad Request` on validation failures or invalid configuration
  - `404 Not Found` if related data missing

Minimal example request:
```
curl -X POST http://localhost:8080/operation/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "creditor": {
      "name": "John Doe",
      "account": { "accountCode": "12345", "iban": "IT60X0542811101000000123456" }
    },
    "executionDate": "2025-10-20",
    "description": "Payment",
    "amount": 100.00,
    "currency": "EUR",
    "taxRelief": {
      "isCondoUpgrade": true,
      "creditorFiscalCode": "RSSMRA85T10A562S",
      "beneficiaryType": "NATURAL_PERSON",
      "naturalPersonBeneficiary": { "fiscalCode1": "RSSMRA85T10A562S" }
    }
  }'
```

3) GET `/operation/allTransaction`
- Description: Returns transactions in an accounting date range (inclusive).
- Query parameters (required):
  - `fromAccountingDate`: `yyyy-MM-dd`
  - `toAccountingDate`: `yyyy-MM-dd`
- Success: `200 OK` with a JSON array of transactions.
- Errors:
  - `400 Bad Request` for invalid dates/config
  - `404 Not Found` if no transactions/payload

Curl example:
```
curl -G http://localhost:8080/operation/allTransaction \
  --data-urlencode "fromAccountingDate=2020-01-01" \
  --data-urlencode "toAccountingDate=2020-01-31"
```


## Authentication towards Fabrick API
Requests sent by the application to Fabrick include the following headers (added by `FeignClientAuthInterceptor`):
- `Api-Key` and `apikey`: your API key value
- `Auth-Schema`: e.g., `S2S`
- `X-Time-Zone`: from the local JVM default time zone
- `X-Request-Id`: random UUID per request
- `Accept: application/json`, `Content-Type: application/json`

Make sure your `fabrick_client.*` properties are configured correctly.


## Error handling
- Controller try/catch maps:
  - `IllegalArgumentException` → `400 Bad Request`
  - `NullPointerException` → `404 Not Found`
- `GlobalHandlerException` (ControllerAdvice):
  - Handles `FabrickApiException` by logging all upstream error objects found in the raw body and returning a concise client message built as `"<code>: <description>"`; if missing, falls back to `"Upstream Fabrick API error"`. The original HTTP status from upstream is preserved.
  - Handles `FeignException` by propagating the upstream response body when present; otherwise returns `"Upstream error"` with the original HTTP status.
- `FabrickErrorDecoder` parses Fabrick error JSON and populates `FabrickApiException` with `httpStatus`, `code`, `description`, and the `rawBody` for observability.


## Database
- Uses H2 (in-memory or file, depending on your `application.properties`) for local development. Entities map money transfer responses and related value objects for persistence.


## Running tests
- `mvn test`

Tests cover controller behavior, service logic, Feign config/interceptor, and error decoding. Lombok-generated boilerplate is marked with `@lombok.Generated` via `lombok.config` so coverage tools can ignore it.


## Notes
- Swagger/OpenAPI reflects validation annotations (e.g., required fields, patterns) for better consumer guidance.
- For production, configure a real database and externalize secrets (API keys) via environment variables or a secrets manager.
