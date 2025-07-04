Write-Host "Starting E-Learning System Services" -ForegroundColor Green
Write-Host "====================================" -ForegroundColor Green

# Start Discovery Server first
Write-Host "`nStarting Discovery Server..." -ForegroundColor Yellow
Start-Process -FilePath "powershell" -ArgumentList "-Command", "cd discovery-server; ./mvnw spring-boot:run" -WindowStyle Normal
Write-Host "Discovery Server starting on port 8761..." -ForegroundColor Cyan

# Wait for Discovery Server to start
Write-Host "Waiting for Discovery Server to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 20

# Start all other services
Write-Host "`nStarting User Service..." -ForegroundColor Yellow
Start-Process -FilePath "powershell" -ArgumentList "-Command", "cd user-service; ./mvnw spring-boot:run" -WindowStyle Normal

Write-Host "Starting Course Service..." -ForegroundColor Yellow
Start-Process -FilePath "powershell" -ArgumentList "-Command", "cd course-service; ./mvnw spring-boot:run" -WindowStyle Normal

Write-Host "Starting Subscription Service..." -ForegroundColor Yellow
Start-Process -FilePath "powershell" -ArgumentList "-Command", "cd subscription-service; ./mvnw spring-boot:run" -WindowStyle Normal

Write-Host "Starting Exam Service..." -ForegroundColor Yellow
Start-Process -FilePath "powershell" -ArgumentList "-Command", "cd exam-service; ./mvnw spring-boot:run" -WindowStyle Normal

# Wait a bit for services to register
Write-Host "Waiting for services to register with Eureka..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Start API Gateway last
Write-Host "`nStarting API Gateway..." -ForegroundColor Yellow
Start-Process -FilePath "powershell" -ArgumentList "-Command", "cd api-gateway; ./mvnw spring-boot:run" -WindowStyle Normal

Write-Host "`nAll services are starting..." -ForegroundColor Green
Write-Host "Please wait about 2-3 minutes for all services to be ready." -ForegroundColor Yellow
Write-Host "`nService URLs:" -ForegroundColor Cyan
Write-Host "- Discovery Server: http://localhost:8761" -ForegroundColor White
Write-Host "- API Gateway: http://localhost:8080" -ForegroundColor White
Write-Host "- User Service: http://localhost:8081" -ForegroundColor White
Write-Host "- Course Service: http://localhost:8082" -ForegroundColor White
Write-Host "- Subscription Service: http://localhost:8083" -ForegroundColor White
Write-Host "- Exam Service: http://localhost:8084" -ForegroundColor White

Write-Host "`nRun './test-main-flow.ps1' to test the system once all services are ready." -ForegroundColor Green 