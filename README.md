# Test Java Inditex Capitole

This is a Spring Boot application that provides a REST API to query product prices based on application date, product ID, and brand ID.

## Table of Contents

- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [API Documentation](#api-documentation)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [License](#license)

## Technologies Used

- **Java 17** with Records for DTOs.
- **Spring Boot** for application development.
- **Spring Data JPA** for database interaction.
- **H2 In-memory Database** for initial setup.
- **MapStruct** for object mapping between entities and DTOs.
- **Lombok** to reduce boilerplate code.
- **Resilience4j** for circuit breaker patterns.
- **Micrometer/Prometheus** for metrics.
- **Swagger/OpenAPI** for API documentation.

## Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your_username/price-query-service.git
    cd price-query-service
    ```

2. **Run the application**:
   The application can be run using Maven. Make sure you have Maven and Java 17 installed.
    ```bash
    ./mvnw spring-boot:run
    ```

3. **Access API Documentation**:
   Open your browser and go to `http://localhost:8080/swagger-ui.html` to explore the available endpoints.

## API Documentation

The API provides the following endpoint:

### Query Price

**Endpoint**: `/api/prices`

**Method**: `GET`

**Parameters**:
- `applicationDate` (required): The date when the price is applied (in `ISO_LOCAL_DATE_TIME` format).
- `productId` (required): The product identifier.
- `brandId` (required): The brand identifier.

Example:
```bash
curl -X GET "http://localhost:8080/api/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

## Metrics

```bash
http://localhost:8080/actuator/prometheus
```