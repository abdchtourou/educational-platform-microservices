# API Gateway Test Script
# This script tests the API Gateway functionality

Write-Host "🚀 Testing API Gateway" -ForegroundColor Green
Write-Host "===================" -ForegroundColor Green

# Test Gateway Health
Write-Host "`n1. Testing Gateway Health..." -ForegroundColor Yellow
try {
    $healthResponse = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -Method GET
    Write-Host "✅ Gateway Health: $($healthResponse.status)" -ForegroundColor Green
} catch {
    Write-Host "❌ Gateway Health Check Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test Actuator Endpoints
Write-Host "`n2. Testing Actuator Endpoints..." -ForegroundColor Yellow
try {
    $actuatorResponse = Invoke-RestMethod -Uri "http://localhost:8080/actuator" -Method GET
    $endpoints = $actuatorResponse._links.PSObject.Properties.Name
    Write-Host "✅ Available Actuator Endpoints: $($endpoints -join ', ')" -ForegroundColor Green
} catch {
    Write-Host "❌ Actuator Endpoints Test Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test Service Discovery
Write-Host "`n3. Testing Service Discovery..." -ForegroundColor Yellow
try {
    $eurekaResponse = Invoke-WebRequest -Uri "http://localhost:8761/" -Method GET
    if ($eurekaResponse.StatusCode -eq 200) {
        Write-Host "✅ Eureka Discovery Server is accessible" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Eureka Discovery Server Test Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test Gateway Routing (if services are running)
Write-Host "`n4. Testing Gateway Routing..." -ForegroundColor Yellow

# Test routing to user-service
Write-Host "   Testing User Service Route..." -ForegroundColor Cyan
try {
    $userResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/users" -Method GET -ErrorAction Stop
    Write-Host "   ✅ User Service Route: Status $($userResponse.StatusCode)" -ForegroundColor Green
} catch {
    if ($_.Exception.Response.StatusCode -eq 503) {
        Write-Host "   ⚠️  User Service Route: Service Unavailable (503) - Service not running" -ForegroundColor Yellow
    } else {
        Write-Host "   ❌ User Service Route Failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test routing to course-service
Write-Host "   Testing Course Service Route..." -ForegroundColor Cyan
try {
    $courseResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/courses" -Method GET -ErrorAction Stop
    Write-Host "   ✅ Course Service Route: Status $($courseResponse.StatusCode)" -ForegroundColor Green
} catch {
    if ($_.Exception.Response.StatusCode -eq 503) {
        Write-Host "   ⚠️  Course Service Route: Service Unavailable (503) - Service not running" -ForegroundColor Yellow
    } else {
        Write-Host "   ❌ Course Service Route Failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test routing to exam-service
Write-Host "   Testing Exam Service Route..." -ForegroundColor Cyan
try {
    $examResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/exams" -Method GET -ErrorAction Stop
    Write-Host "   ✅ Exam Service Route: Status $($examResponse.StatusCode)" -ForegroundColor Green
} catch {
    if ($_.Exception.Response.StatusCode -eq 503) {
        Write-Host "   ⚠️  Exam Service Route: Service Unavailable (503) - Service not running" -ForegroundColor Yellow
    } else {
        Write-Host "   ❌ Exam Service Route Failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test CORS Headers
Write-Host "`n5. Testing CORS Configuration..." -ForegroundColor Yellow
try {
    $corsResponse = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -Method GET
    $varyHeader = $corsResponse.Headers["Vary"]
    if ($varyHeader -and $varyHeader -contains "Origin") {
        Write-Host "✅ CORS Headers Present: Vary header includes Origin" -ForegroundColor Green
    } else {
        Write-Host "⚠️  CORS Headers: Vary header not found or doesn't include Origin" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ CORS Test Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Summary
Write-Host "`n📊 Test Summary" -ForegroundColor Green
Write-Host "===============" -ForegroundColor Green
Write-Host "Gateway URL: http://localhost:8080" -ForegroundColor White
Write-Host "Discovery Server: http://localhost:8761" -ForegroundColor White
Write-Host "Health Endpoint: http://localhost:8080/actuator/health" -ForegroundColor White

Write-Host "`n🔗 Service Routes:" -ForegroundColor Green
Write-Host "User Service: http://localhost:8080/api/users" -ForegroundColor White
Write-Host "Course Service: http://localhost:8080/api/courses" -ForegroundColor White
Write-Host "Exam Service: http://localhost:8080/api/exams" -ForegroundColor White

Write-Host "`n✨ API Gateway testing completed!" -ForegroundColor Green 