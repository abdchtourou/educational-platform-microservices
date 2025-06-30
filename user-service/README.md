# User Service

A Spring Boot microservice for user management in the e-learning system.

## Features

- User registration and authentication
- Role-based access control (STUDENT, INSTRUCTOR, ADMIN)
- JWT token generation and validation
- RESTful API for user management
- Spring Security integration
- H2 in-memory database for development
- Eureka service discovery integration
- Spring Boot Actuator for monitoring

## Technologies Used

- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Web** - REST API development
- **Spring Data JPA** - Database operations
- **Spring Security** - Authentication and authorization
- **Lombok** - Reducing boilerplate code
- **H2 Database** - In-memory database for development
- **Spring Boot DevTools** - Development productivity
- **Eureka Discovery Client** - Service registration
- **Spring Boot Actuator** - Application monitoring
- **JJWT** - JWT token handling

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to the user-service directory
3. Run the application:

```bash
./mvnw spring-boot:run
```

The service will start on port `8081`.

### API Endpoints

#### Public Endpoints
- `POST /api/users` - Register a new user
- `GET /api/users/health` - Health check

#### Protected Endpoints
- `GET /api/users` - Get all users (ADMIN/INSTRUCTOR only)
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users/email/{email}` - Get user by email
- `GET /api/users/role/{role}` - Get users by role (ADMIN/INSTRUCTOR only)
- `GET /api/users/active` - Get active users (ADMIN/INSTRUCTOR only)
- `GET /api/users/search?term={searchTerm}` - Search users (ADMIN/INSTRUCTOR only)
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user (ADMIN only)
- `PATCH /api/users/{id}/enable` - Enable user (ADMIN only)
- `PATCH /api/users/{id}/disable` - Disable user (ADMIN only)
- `PATCH /api/users/{id}/change-password` - Change user password

### Configuration

The application can be configured through `application.yml`:

- **Server Port**: 8081
- **Database**: H2 in-memory database
- **Eureka**: Registers with Eureka server at `http://localhost:8761/eureka/`
- **JWT**: Token expiration set to 24 hours

### H2 Console

For development, you can access the H2 console at:
`http://localhost:8081/h2-console`

- **JDBC URL**: `jdbc:h2:mem:userdb`
- **Username**: `sa`
- **Password**: `password`

### Testing

Run tests with:

```bash
./mvnw test
```

### Monitoring

Actuator endpoints are available at:
- `http://localhost:8081/actuator/health`
- `http://localhost:8081/actuator/info`
- `http://localhost:8081/actuator/metrics`

## Project Structure

```
src/
├── main/
│   ├── java/com/elearningsystem/userservice/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── model/          # Entity classes
│   │   ├── repository/     # Data repositories
│   │   ├── service/        # Business logic
│   │   └── UserServiceApplication.java
│   └── resources/
│       ├── application.yml
│       └── application-test.yml
└── test/
    └── java/com/elearningsystem/userservice/
        └── UserServiceApplicationTests.java
```

## User Roles

- **STUDENT**: Basic user role for learners
- **INSTRUCTOR**: Can view user lists and manage course content
- **ADMIN**: Full access to user management functions 