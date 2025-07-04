# Subscription Service API Documentation

This document provides examples of how to use the Subscription Service API endpoints.

## Base URL
```
http://localhost:8083
```

## Endpoints

### 1. Create a New Subscription
**POST** `/api/subscriptions`

Creates a new subscription for a user to a course.

**Request Body:**
```json
{
  "userId": 1,
  "courseId": 101
}
```

**Response:**
```json
{
  "id": 1,
  "userId": 1,
  "courseId": 101,
  "isPaid": false,
  "isCompleted": false,
  "enrolledAt": "2025-07-01T02:00:00"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8083/api/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "courseId": 101
  }'
```

### 2. Get All Subscriptions for a User
**GET** `/api/subscriptions/user/{userId}`

Retrieves all subscriptions for a specific user.

**Response:**
```json
[
  {
    "id": 1,
    "userId": 1,
    "courseId": 101,
    "isPaid": true,
    "isCompleted": false,
    "enrolledAt": "2025-06-01T10:00:00"
  },
  {
    "id": 2,
    "userId": 1,
    "courseId": 102,
    "isPaid": true,
    "isCompleted": true,
    "enrolledAt": "2025-05-15T14:30:00"
  }
]
```

**cURL Example:**
```bash
curl -X GET http://localhost:8083/api/subscriptions/user/1
```

### 3. Mark Subscription as Paid
**PUT** `/api/subscriptions/{id}/pay`

Marks a subscription as paid.

**Response:**
```json
{
  "id": 1,
  "userId": 1,
  "courseId": 101,
  "isPaid": true,
  "isCompleted": false,
  "enrolledAt": "2025-07-01T02:00:00"
}
```

**cURL Example:**
```bash
curl -X PUT http://localhost:8083/api/subscriptions/1/pay
```

### 4. Mark Subscription as Completed
**PUT** `/api/subscriptions/{id}/complete`

Marks a subscription as completed.

**Response:**
```json
{
  "id": 1,
  "userId": 1,
  "courseId": 101,
  "isPaid": true,
  "isCompleted": true,
  "enrolledAt": "2025-07-01T02:00:00"
}
```

**cURL Example:**
```bash
curl -X PUT http://localhost:8083/api/subscriptions/1/complete
```

## Error Responses

### 400 Bad Request
Returned when the request is invalid (e.g., user already subscribed to the course).

```json
{
  "error": "User is already subscribed to this course"
}
```

### 404 Not Found
Returned when the subscription ID doesn't exist.

```json
{
  "error": "Subscription not found with id: 999"
}
```

## Sample Data

The application initializes with the following sample subscriptions:

| ID | User ID | Course ID | Is Paid | Is Completed | Enrolled At |
|----|---------|-----------|---------|--------------|-------------|
| 1  | 1       | 1         | true    | false        | 30 days ago |
| 2  | 1       | 2         | true    | true         | 60 days ago |
| 3  | 2       | 1         | false   | false        | 15 days ago |
| 4  | 3       | 3         | true    | false        | 20 days ago |
| 5  | 2       | 4         | false   | false        | 5 days ago  |

## Entity Structure

### Subscription
```json
{
  "id": "Long - Unique identifier",
  "userId": "Long - User identifier", 
  "courseId": "Long - Course identifier",
  "isPaid": "Boolean - Payment status",
  "isCompleted": "Boolean - Completion status",
  "enrolledAt": "LocalDateTime - Enrollment timestamp"
}
```

## Business Rules

1. **Unique Subscription**: A user cannot subscribe to the same course twice
2. **Default Status**: New subscriptions are created with `isPaid=false` and `isCompleted=false`
3. **Enrollment Time**: The `enrolledAt` field is automatically set to the current timestamp when creating a subscription
4. **Status Updates**: Payment and completion status can be updated independently 