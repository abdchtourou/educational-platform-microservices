# API Gateway

The API Gateway serves as the single entry point for all microservices in the e-learning system. It handles request routing, load balancing, and cross-cutting concerns like CORS.

## Features

- **Service Discovery Integration**: Automatically discovers and routes to services registered with Eureka
- **Load Balancing**: Built-in client-side load balancing with Spring Cloud LoadBalancer
- **CORS Support**: Configured to handle cross-origin requests
- **Request Logging**: Custom filter to log all requests passing through the gateway
- **Health Monitoring**: Actuator endpoints for monitoring gateway health

## Configuration

### Port Configuration
- **API Gateway**: `8080`

### Service Routes
The gateway automatically routes requests based on service names discovered from Eureka:

| Path Pattern | Target Service | Description |
|-------------|----------------|-------------|
| `/api/users/**` | user-service | User management and authentication |
| `/api/auth/**` | user-service | Authentication endpoints |
| `/api/courses/**` | course-service | Course management |
| `/api/exams/**` | exam-service | Exam management |
| `/api/results/**` | exam-service | Exam results |
| `/eureka/**` | discovery-server | Eureka dashboard |

### Eureka Integration
- **Discovery Server URL**: `http://localhost:8761/eureka`
- **Service Name**: `api-gateway`
- **Auto-discovery**: Enabled with lower-case service IDs

## Running the Gateway

### Prerequisites
1. Java 21 installed
2. Discovery Server running on port 8761

### Start the Gateway
```bash
./mvnw spring-boot:run
```

### Verify Gateway is Running
```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{"status":"UP"}
```

## API Usage Examples

### Through Gateway (Recommended)
```bash
# User service through gateway
curl http://localhost:8080/api/users

# Course service through gateway  
curl http://localhost:8080/api/courses

# Exam service through gateway
curl http://localhost:8080/api/exams
```

### Direct Service Access (Not Recommended)
```bash
# Direct access bypasses gateway features
curl http://localhost:8083/api/users  # user-service
curl http://localhost:8082/api/courses # course-service
curl http://localhost:8084/api/exams   # exam-service
```

## Monitoring

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Available Actuator Endpoints
```bash
curl http://localhost:8080/actuator
```

## Architecture Benefits

1. **Single Entry Point**: Clients only need to know the gateway URL
2. **Service Abstraction**: Internal service locations are hidden from clients
3. **Cross-Cutting Concerns**: Authentication, logging, rate limiting can be handled centrally
4. **Load Balancing**: Automatic distribution of requests across service instances
5. **Fault Tolerance**: Gateway can handle service failures gracefully

## Security Considerations

- CORS is configured to allow all origins (suitable for development)
- For production, configure specific allowed origins
- Consider adding authentication filters for secured endpoints
- Implement rate limiting to prevent abuse

## Troubleshooting

### Gateway Not Starting
1. Check if port 8080 is available
2. Ensure Discovery Server is running on port 8761
3. Verify Java 21 is installed

### Service Not Found
1. Check if target service is registered with Eureka
2. Verify service names match the configured routes
3. Check Eureka dashboard: http://localhost:8761

### Request Timeout
1. Check if target service is running and healthy
2. Verify network connectivity between services
3. Check service logs for errors 