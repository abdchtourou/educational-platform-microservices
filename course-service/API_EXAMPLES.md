# Course Service API Documentation

## Overview
The Course Service provides REST APIs for managing courses with approval workflow.

## Base URL
```
http://localhost:8082/api/courses
```

## Endpoints

### 1. Add a New Course (by trainer)
**POST** `/api/courses`

**Request Body:**
```json
{
    "title": "Introduction to Python",
    "description": "Learn Python programming from basics to advanced",
    "trainer": "john.doe",
    "price": 99.99,
    "duration": 40
}
```

**Response:**
```json
{
    "id": 1,
    "title": "Introduction to Python",
    "description": "Learn Python programming from basics to advanced",
    "trainer": "john.doe",
    "price": 99.99,
    "duration": 40,
    "status": "PENDING"
}
```

### 2. Approve a Course (by admin)
**PUT** `/api/courses/{id}/approve`

**Response:**
```json
{
    "courseId": 1,
    "title": "Introduction to Python",
    "previousStatus": "PENDING",
    "newStatus": "APPROVED",
    "message": "Course status updated successfully"
}
```

### 3. Reject a Course
**PUT** `/api/courses/{id}/reject`

**Response:**
```json
{
    "courseId": 1,
    "title": "Introduction to Python",
    "previousStatus": "PENDING",
    "newStatus": "REJECTED",
    "message": "Course status updated successfully"
}
```

### 4. Get All Approved Courses (for users)
**GET** `/api/courses`

**Response:**
```json
[
    {
        "id": 1,
        "title": "Introduction to Spring Boot",
        "description": "Learn the fundamentals of Spring Boot framework",
        "trainer": "john.doe",
        "price": 99.99,
        "duration": 40,
        "status": "APPROVED"
    },
    {
        "id": 2,
        "title": "Advanced Java Programming",
        "description": "Master advanced Java concepts and best practices",
        "trainer": "jane.smith",
        "price": 149.99,
        "duration": 60,
        "status": "APPROVED"
    }
]
```

### 5. Get All Courses by Trainer
**GET** `/api/courses/trainer/{trainerId}`

**Response:**
```json
[
    {
        "id": 1,
        "title": "Introduction to Spring Boot",
        "description": "Learn the fundamentals of Spring Boot framework",
        "trainer": "john.doe",
        "price": 99.99,
        "duration": 40,
        "status": "APPROVED"
    }
]
```

## Additional Endpoints

### Get All Courses (Admin View)
**GET** `/api/courses/all`

### Get Course by ID
**GET** `/api/courses/{id}`

### Get Courses by Status
**GET** `/api/courses/status/{status}`
- Status values: `PENDING`, `APPROVED`, `REJECTED`

## Testing with curl

### Add a new course:
```bash
curl -X POST http://localhost:8082/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "title": "React Fundamentals",
    "description": "Learn React.js from scratch",
    "trainer": "alice.brown",
    "price": 89.99,
    "duration": 35
  }'
```

### Approve a course:
```bash
curl -X PUT http://localhost:8082/api/courses/1/approve
```

### Get all approved courses:
```bash
curl http://localhost:8082/api/courses
```

### Get courses by trainer:
```bash
curl http://localhost:8082/api/courses/trainer/john.doe
```

## Error Responses

### 404 Not Found (Course not exists)
```json
{
    "timestamp": "2025-07-01T01:40:39.461Z",
    "status": 404,
    "error": "Not Found",
    "path": "/api/courses/999/approve"
}
```

### 400 Bad Request (Validation Error)
```json
{
    "timestamp": "2025-07-01T01:40:39.461Z",
    "status": 400,
    "error": "Bad Request",
    "message": "Title is required"
}
```

## Course Status Flow
1. **PENDING** - Initial status when a trainer creates a course
2. **APPROVED** - Course approved by admin and visible to users
3. **REJECTED** - Course rejected and not visible to users 