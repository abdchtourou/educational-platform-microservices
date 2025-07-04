# Subscription Service

A Spring Boot microservice for managing course subscriptions in the e-learning system.

## Features

- **Subscription Management**: Subscribe users to courses, track progress, and manage subscription status
- **Progress Tracking**: Update and monitor course completion progress
- **Status Management**: Handle subscription states (ACTIVE, COMPLETED, CANCELLED, EXPIRED)
- **RESTful API**: Comprehensive REST endpoints for all subscription operations
- **Data Validation**: Input validation using Bean Validation
- **Service Discovery**: Eureka client integration for microservice architecture
- **H2 Database**: In-memory database for development and testing

## Technology Stack

- **Java 21**
- **Spring Boot 3.4.1**
- **Spring Data JPA**
- **Spring Web**
- **Spring Cloud (Eureka Client)**
- **H2 Database**
- **Lombok**
- **Maven**

## Project Structure

```
src/
├── main/
│   ├── java/com/elearningsystem/subscriptionservice/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── model/          # JPA entities
│   │   ├── repository/     # Data repositories
│   │   ├── service/        # Business logic
│   │   └── SubscriptionServiceApplication.java
│   └── resources/
│       └── application.yml  # Application configuration
└── test/
    ├── java/               # Test classes
    └── resources/
        └── application-test.yml  # Test configuration
```

## Configuration

The service runs on port **8083** and registers with Eureka as `subscription-service`.

### Application Properties

- **Port**: 8083
- **Database**: H2 in-memory database
- **H2 Console**: Available at `/h2-console`
- **Eureka**: Registers with Eureka server at `http://localhost:8761/eureka`

## Running the Application

### Prerequisites

- Java 21
- Maven 3.6+
- Eureka Server (optional, for service discovery)

### Build and Run

```bash
# Build the project
./mvnw clean compile

# Run tests
./mvnw test

# Start the application
./mvnw spring-boot:run
```

The service will be available at `http://localhost:8083`

## API Endpoints

### Core Subscription Operations

- `POST /api/subscriptions` - Create a new subscription
- `GET /api/subscriptions/user/{userId}` - Get all subscriptions for a user
- `PUT /api/subscriptions/{id}/pay` - Mark subscription as paid
- `PUT /api/subscriptions/{id}/complete` - Mark subscription as completed

## Data Model

### Subscription Entity

- `id` - Unique identifier (Long)
- `userId` - User identifier (Long)
- `courseId` - Course identifier (Long)
- `isPaid` - Payment status (Boolean)
- `isCompleted` - Completion status (Boolean)
- `enrolledAt` - Enrollment timestamp (LocalDateTime)

## Sample Data

The application initializes with sample subscription data for testing:

- User subscriptions with different payment and completion statuses
- Multiple users and courses
- Various enrollment dates

## Testing

Run the test suite with:

```bash
./mvnw test
```

Tests use a separate H2 database and disable Eureka client for isolated testing.

## Development

### Database Console

Access the H2 console at `http://localhost:8083/h2-console` with:
- **JDBC URL**: `jdbc:h2:mem:subscriptiondb`
- **Username**: `sa`
- **Password**: (empty)

### Logging

Debug logging is enabled for the application package. Adjust logging levels in `application.yml`.

## Integration

This service is designed to work with:
- **Course Service** - For course information
- **User Service** - For user authentication and management
- **Eureka Server** - For service discovery
- **API Gateway** - For routing and load balancing 