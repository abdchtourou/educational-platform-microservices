# E-Learning System - Microservices Architecture

A comprehensive e-learning platform built with Spring Boot microservices architecture, featuring user management, course creation, subscriptions, and examination systems.

## 🏗️ Architecture Overview

The system consists of 6 microservices:

- **Discovery Server** (Port 8761) - Eureka service registry
- **API Gateway** (Port 8080) - Unified entry point
- **User Service** (Port 8081) - User management and authentication
- **Course Service** (Port 8082) - Course management and approval
- **Subscription Service** (Port 8083) - Subscription and payment handling
- **Exam Service** (Port 8084) - Exam creation and evaluation

## 🚀 Getting Started

### Prerequisites

- Java 21
- Maven 3.6+
- PowerShell (for Windows)

### Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd project
   ```

2. **Start all services automatically**
   ```powershell
   ./start-all-services.ps1
   ```

3. **Wait for services to start** (approximately 2-3 minutes)

4. **Test the system**
   ```powershell
   ./test-main-flow.ps1
   ```

### Manual Service Startup

If you prefer to start services manually:

1. **Start Discovery Server first**
   ```powershell
   cd discovery-server
   ./mvnw spring-boot:run
   ```

2. **Wait 30 seconds, then start other services**
   ```powershell
   # In separate terminal windows:
   cd user-service && ./mvnw spring-boot:run
   cd course-service && ./mvnw spring-boot:run
   cd subscription-service && ./mvnw spring-boot:run
   cd exam-service && ./mvnw spring-boot:run
   ```

3. **Start API Gateway last**
   ```powershell
   cd api-gateway
   ./mvnw spring-boot:run
   ```

## 🌐 Service URLs

| Service | URL | Health Check |
|---------|-----|--------------|
| Discovery Server | http://localhost:8761 | http://localhost:8761/actuator/health |
| API Gateway | http://localhost:8080 | http://localhost:8080/actuator/health |
| User Service | http://localhost:8081 | http://localhost:8081/actuator/health |
| Course Service | http://localhost:8082 | http://localhost:8082/actuator/health |
| Subscription Service | http://localhost:8083 | http://localhost:8083/actuator/health |
| Exam Service | http://localhost:8084 | http://localhost:8084/actuator/health |

## 📊 Monitoring Dashboards

- **Eureka Dashboard**: http://localhost:8761
- **H2 Database Consoles**:
  - User Service: http://localhost:8081/h2-console
  - Course Service: http://localhost:8082/h2-console
  - Subscription Service: http://localhost:8083/h2-console
  - Exam Service: http://localhost:8084/h2-console

## 🔧 API Documentation for Postman

All APIs should be accessed through the **API Gateway** at `http://localhost:8080`

### 1. User Service APIs

#### Register User
```http
POST http://localhost:8080/api/users
Content-Type: application/json

{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "role": "STUDENT"
}
```

#### Get User by ID
```http
GET http://localhost:8080/api/users/{userId}
Authorization: Bearer {jwt_token}
```

#### Get User by Username
```http
GET http://localhost:8080/api/users/username/{username}
```

#### Get User by Email
```http
GET http://localhost:8080/api/users/email/{email}
```

#### Get All Users (Admin/Instructor only)
```http
GET http://localhost:8080/api/users
Authorization: Bearer {jwt_token}
```

#### Update User
```http
PUT http://localhost:8080/api/users/{userId}
Content-Type: application/json
Authorization: Bearer {jwt_token}

{
    "firstName": "Updated Name",
    "lastName": "Updated Last Name",
    "email": "updated@example.com"
}
```

### 2. Course Service APIs

#### Create Course
```http
POST http://localhost:8080/api/courses
Content-Type: application/json

{
    "title": "Introduction to Spring Boot",
    "description": "Learn the fundamentals of Spring Boot framework",
    "trainer": "john_instructor",
    "price": 99.99
}
```

#### Get All Courses
```http
GET http://localhost:8080/api/courses
```

#### Get Course by ID
```http
GET http://localhost:8080/api/courses/{courseId}
```

#### Approve Course (Admin only)
```http
PUT http://localhost:8080/api/courses/{courseId}/approve
```

#### Reject Course (Admin only)
```http
PUT http://localhost:8080/api/courses/{courseId}/reject
```

#### Get Courses by Status
```http
GET http://localhost:8080/api/courses/status/{status}
# status: PENDING, APPROVED, REJECTED
```

#### Get Courses by Trainer
```http
GET http://localhost:8080/api/courses/trainer/{trainerName}
```

### 3. Subscription Service APIs

#### Create Subscription
```http
POST http://localhost:8080/api/subscriptions
Content-Type: application/json

{
    "userId": 1,
    "courseId": 1
}
```

#### Get All Subscriptions
```http
GET http://localhost:8080/api/subscriptions
```

#### Get Subscription by ID
```http
GET http://localhost:8080/api/subscriptions/{subscriptionId}
```

#### Get User Subscriptions
```http
GET http://localhost:8080/api/subscriptions/user/{userId}
```

#### Mark Subscription as Paid
```http
PUT http://localhost:8080/api/subscriptions/{subscriptionId}/pay
```

#### Mark Subscription as Completed
```http
PUT http://localhost:8080/api/subscriptions/{subscriptionId}/complete
```

#### Get Subscriptions by Payment Status
```http
GET http://localhost:8080/api/subscriptions/paid/{isPaid}
# isPaid: true or false
```

#### Check if User is Subscribed to Course
```http
GET http://localhost:8080/api/subscriptions/user/{userId}/course/{courseId}
```

### 4. Exam Service APIs

#### Create Exam
```http
POST http://localhost:8080/api/exams
Content-Type: application/json

{
    "courseId": 1,
    "title": "Spring Boot Fundamentals Quiz",
    "questions": "{\"q1\": \"What is Spring Boot?\", \"q2\": \"What is auto-configuration?\"}",
    "duration": 60
}
```

#### Get All Exams
```http
GET http://localhost:8080/api/exams
```

#### Get Exam by Course ID
```http
GET http://localhost:8080/api/exams/course/{courseId}
```

#### Submit Exam
```http
POST http://localhost:8080/api/exams/submit
Content-Type: application/json

{
    "courseId": 1,
    "userId": 1,
    "answers": "{\"q1\": \"A Java framework\", \"q2\": \"Automatic configuration\"}"
}
```

#### Get User Results
```http
GET http://localhost:8080/api/results/user/{userId}
```

#### Get Result by User and Course
```http
GET http://localhost:8080/api/results/user/{userId}/course/{courseId}
```

## 🧪 Testing Workflow

### Complete E-Learning Flow Test

1. **Register a Student**
   ```http
   POST http://localhost:8080/api/users
   {
       "username": "student1",
       "email": "student1@example.com", 
       "password": "password123",
       "firstName": "John",
       "lastName": "Student",
       "role": "STUDENT"
   }
   ```

2. **Register an Instructor**
   ```http
   POST http://localhost:8080/api/users
   {
       "username": "instructor1",
       "email": "instructor1@example.com",
       "password": "password123", 
       "firstName": "Jane",
       "lastName": "Instructor",
       "role": "INSTRUCTOR"
   }
   ```

3. **Create a Course**
   ```http
   POST http://localhost:8080/api/courses
   {
       "title": "Java Programming Basics",
       "description": "Learn Java from scratch",
       "trainer": "instructor1",
       "price": 149.99
   }
   ```

4. **Approve the Course (as Admin)**
   ```http
   PUT http://localhost:8080/api/courses/1/approve
   ```

5. **Subscribe to Course**
   ```http
   POST http://localhost:8080/api/subscriptions
   {
       "userId": 1,
       "courseId": 1
   }
   ```

6. **Mark Subscription as Paid**
   ```http
   PUT http://localhost:8080/api/subscriptions/1/pay
   ```

7. **Create Exam for Course**
   ```http
   POST http://localhost:8080/api/exams
   {
       "courseId": 1,
       "title": "Java Basics Final Exam",
       "questions": "{\"q1\": \"What is Java?\", \"q2\": \"What is OOP?\"}",
       "duration": 90
   }
   ```

8. **Submit Exam**
   ```http
   POST http://localhost:8080/api/exams/submit
   {
       "courseId": 1,
       "userId": 1,
       "answers": "{\"q1\": \"Programming language\", \"q2\": \"Object-oriented programming\"}"
   }
   ```

## 🔒 Authentication

The system uses JWT-based authentication. After user registration, you can authenticate using the login endpoint (when implemented) or use the user creation response for testing.

## 🛠️ Configuration

### Database Configuration
All services use H2 in-memory databases for development:
- **JDBC URL**: `jdbc:h2:mem:{servicename}db`
- **Username**: `sa`
- **Password**: (empty)

### Service Ports
- Discovery Server: 8761
- API Gateway: 8080
- User Service: 8081
- Course Service: 8082
- Subscription Service: 8083
- Exam Service: 8084

## 🚨 Troubleshooting

### Services Not Starting
1. Ensure Java 21 is installed
2. Check if ports are available
3. Start Discovery Server first, wait 30 seconds
4. Start other services
5. Start API Gateway last

### Service Communication Issues
1. Check Eureka dashboard for registered services
2. Verify service health endpoints
3. Check application logs for errors

### Database Issues
1. Access H2 console for each service
2. Check JPA entity mappings
3. Verify data initialization

## 📁 Project Structure

```
project/
├── discovery-server/          # Eureka service registry
├── api-gateway/              # Spring Cloud Gateway
├── user-service/             # User management & auth
├── course-service/           # Course management
├── subscription-service/     # Subscription handling
├── exam-service/             # Exam & evaluation
├── start-all-services.ps1    # Service startup script
├── test-main-flow.ps1        # Testing script
└── README.md                 # This file
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details. 