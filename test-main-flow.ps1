#!/usr/bin/env pwsh

Write-Host "E-Learning System - Main Flow Testing Script" -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green

# Configuration
$GATEWAY_URL = "http://localhost:8080"
$DISCOVERY_URL = "http://localhost:8761"

# Test Discovery Server
Write-Host "`nTesting Discovery Server..." -ForegroundColor Yellow
try {
    $discovery = Invoke-RestMethod -Uri "$DISCOVERY_URL/actuator/health" -Method Get -TimeoutSec 10
    Write-Host "✅ Discovery Server is healthy" -ForegroundColor Green
} catch {
    Write-Host "❌ Discovery Server is not responding" -ForegroundColor Red
    exit 1
}

# Test API Gateway
Write-Host "`nTesting API Gateway..." -ForegroundColor Yellow
try {
    $gateway = Invoke-RestMethod -Uri "$GATEWAY_URL/actuator/health" -Method Get -TimeoutSec 10
    Write-Host "✅ API Gateway is healthy" -ForegroundColor Green
} catch {
    Write-Host "❌ API Gateway is not responding" -ForegroundColor Red
    exit 1
}

# Test User Service - Register User
Write-Host "`nTesting User Registration..." -ForegroundColor Yellow
$registerPayload = @{
    username = "testuser"
    email = "test@example.com"
    password = "password123"
    firstName = "Test"
    lastName = "User"
    role = "STUDENT"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "$GATEWAY_URL/api/auth/register" -Method Post -Body $registerPayload -ContentType "application/json"
    Write-Host "✅ User registered successfully - ID: $($registerResponse.id)" -ForegroundColor Green
    $global:userId = $registerResponse.id
} catch {
    Write-Host "❌ User registration failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test User Service - Login
Write-Host "`nTesting User Login..." -ForegroundColor Yellow
$loginPayload = @{
    username = "testuser"
    password = "password123"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$GATEWAY_URL/api/auth/login" -Method Post -Body $loginPayload -ContentType "application/json"
    Write-Host "✅ User login successful" -ForegroundColor Green
    $global:token = $loginResponse.token
} catch {
    Write-Host "❌ User login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test Course Service - Create Course
Write-Host "`nTesting Course Creation..." -ForegroundColor Yellow
$coursePayload = @{
    title = "Introduction to Spring Boot"
    description = "Learn the fundamentals of Spring Boot"
    trainer = "testuser"
    price = 99.99
} | ConvertTo-Json

try {
    $courseResponse = Invoke-RestMethod -Uri "$GATEWAY_URL/api/courses" -Method Post -Body $coursePayload -ContentType "application/json"
    Write-Host "✅ Course created successfully - ID: $($courseResponse.id)" -ForegroundColor Green
    $global:courseId = $courseResponse.id
} catch {
    Write-Host "❌ Course creation failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test Course Service - Approve Course
Write-Host "`nTesting Course Approval..." -ForegroundColor Yellow
try {
    $approveResponse = Invoke-RestMethod -Uri "$GATEWAY_URL/api/courses/$global:courseId/approve" -Method Put
    Write-Host "✅ Course approved successfully" -ForegroundColor Green
} catch {
    Write-Host "❌ Course approval failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test Subscription Service - Create Subscription
Write-Host "`nTesting Subscription Creation..." -ForegroundColor Yellow
$subscriptionPayload = @{
    userId = $global:userId
    courseId = $global:courseId
} | ConvertTo-Json

try {
    $subscriptionResponse = Invoke-RestMethod -Uri "$GATEWAY_URL/api/subscriptions" -Method Post -Body $subscriptionPayload -ContentType "application/json"
    Write-Host "✅ Subscription created successfully - ID: $($subscriptionResponse.id)" -ForegroundColor Green
    $global:subscriptionId = $subscriptionResponse.id
} catch {
    Write-Host "❌ Subscription creation failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test Subscription Service - Mark as Paid
Write-Host "`nTesting Mark Subscription as Paid..." -ForegroundColor Yellow
try {
    $paidResponse = Invoke-RestMethod -Uri "$GATEWAY_URL/api/subscriptions/$global:subscriptionId/pay" -Method Put
    Write-Host "✅ Subscription marked as paid successfully" -ForegroundColor Green
} catch {
    Write-Host "❌ Mark subscription as paid failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test Exam Service - Create Exam
Write-Host "`nTesting Exam Creation..." -ForegroundColor Yellow
$examPayload = @{
    courseId = $global:courseId
    title = "Spring Boot Basics Quiz"
    questions = '{"q1": "What is Spring Boot?", "q2": "What is auto-configuration?"}'
    duration = 60
} | ConvertTo-Json

try {
    $examResponse = Invoke-RestMethod -Uri "$GATEWAY_URL/api/exams" -Method Post -Body $examPayload -ContentType "application/json"
    Write-Host "✅ Exam created successfully - ID: $($examResponse.id)" -ForegroundColor Green
    $global:examId = $examResponse.id
} catch {
    Write-Host "❌ Exam creation failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test Exam Service - Submit Exam
Write-Host "`nTesting Exam Submission..." -ForegroundColor Yellow
$submissionPayload = @{
    courseId = $global:courseId
    userId = $global:userId
    answers = '{"q1": "A framework", "q2": "Automatic configuration"}'
} | ConvertTo-Json

try {
    $submissionResponse = Invoke-RestMethod -Uri "$GATEWAY_URL/api/exams/submit" -Method Post -Body $submissionPayload -ContentType "application/json"
    Write-Host "✅ Exam submitted successfully - Score: $($submissionResponse.score)" -ForegroundColor Green
} catch {
    Write-Host "❌ Exam submission failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host "`n🎉 ALL TESTS PASSED! E-Learning System is working correctly." -ForegroundColor Green
Write-Host "`nService URLs:" -ForegroundColor Cyan
Write-Host "- Eureka Dashboard: http://localhost:8761" -ForegroundColor White
Write-Host "- API Gateway: http://localhost:8080" -ForegroundColor White
Write-Host "- User Service H2 Console: http://localhost:8081/h2-console" -ForegroundColor White
Write-Host "- Course Service H2 Console: http://localhost:8082/h2-console" -ForegroundColor White
Write-Host "- Subscription Service H2 Console: http://localhost:8083/h2-console" -ForegroundColor White 