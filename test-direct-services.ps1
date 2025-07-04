Write-Host "Testing Services Directly (Without Gateway)" -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green

# Test User Service directly
Write-Host "`nTesting User Service directly..." -ForegroundColor Yellow
$registerPayload = @{
    username = "testuser"
    email = "test@example.com"
    password = "password123"
    firstName = "Test"
    lastName = "User"
    role = "STUDENT"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/auth/register" -Method Post -Body $registerPayload -ContentType "application/json"
    Write-Host "✅ User registered successfully - ID: $($registerResponse.id)" -ForegroundColor Green
    $global:userId = $registerResponse.id
} catch {
    Write-Host "❌ User registration failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test User Login directly
Write-Host "`nTesting User Login directly..." -ForegroundColor Yellow
$loginPayload = @{
    username = "testuser"
    password = "password123"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/auth/login" -Method Post -Body $loginPayload -ContentType "application/json"
    Write-Host "✅ User login successful" -ForegroundColor Green
    $global:token = $loginResponse.token
} catch {
    Write-Host "❌ User login failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test Course Service directly
Write-Host "`nTesting Course Service directly..." -ForegroundColor Yellow
$coursePayload = @{
    title = "Introduction to Spring Boot"
    description = "Learn the fundamentals of Spring Boot"
    trainer = "testuser"
    price = 99.99
} | ConvertTo-Json

try {
    $courseResponse = Invoke-RestMethod -Uri "http://localhost:8082/api/courses" -Method Post -Body $coursePayload -ContentType "application/json"
    Write-Host "✅ Course created successfully - ID: $($courseResponse.id)" -ForegroundColor Green
    $global:courseId = $courseResponse.id
} catch {
    Write-Host "❌ Course creation failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test Subscription Service directly
Write-Host "`nTesting Subscription Service directly..." -ForegroundColor Yellow
$subscriptionPayload = @{
    userId = $global:userId
    courseId = $global:courseId
} | ConvertTo-Json

try {
    $subscriptionResponse = Invoke-RestMethod -Uri "http://localhost:8083/api/subscriptions" -Method Post -Body $subscriptionPayload -ContentType "application/json"
    Write-Host "✅ Subscription created successfully - ID: $($subscriptionResponse.id)" -ForegroundColor Green
    $global:subscriptionId = $subscriptionResponse.id
} catch {
    Write-Host "❌ Subscription creation failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test Exam Service directly
Write-Host "`nTesting Exam Service directly..." -ForegroundColor Yellow
$examPayload = @{
    courseId = $global:courseId
    title = "Spring Boot Basics Quiz"
    questions = '{"q1": "What is Spring Boot?", "q2": "What is auto-configuration?"}'
    duration = 60
} | ConvertTo-Json

try {
    $examResponse = Invoke-RestMethod -Uri "http://localhost:8084/api/exams" -Method Post -Body $examPayload -ContentType "application/json"
    Write-Host "✅ Exam created successfully - ID: $($examResponse.id)" -ForegroundColor Green
} catch {
    Write-Host "❌ Exam creation failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n✅ Direct service testing completed!" -ForegroundColor Green 