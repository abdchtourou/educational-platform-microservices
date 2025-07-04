Write-Host "=== Testing Exam Service API ===" -ForegroundColor Green

# Test 1: GET all exams (should return empty array initially)
Write-Host "`n1. Testing GET /api/exams (should return empty list)" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8084/api/exams" -Method GET
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Create a new exam
Write-Host "`n2. Testing POST /api/exams (create new exam)" -ForegroundColor Yellow
$examData = @{
    courseId = 1
    questions = '{"q1": {"question": "What is Java?", "options": ["Language", "Coffee", "Island"], "correct": 0}, "q2": {"question": "What is Spring?", "options": ["Season", "Framework", "Water"], "correct": 1}}'
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8084/api/exams" -Method POST -Body $examData -ContentType "application/json"
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Cyan
    $createdExam = $response.Content | ConvertFrom-Json
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Get exam by course ID
Write-Host "`n3. Testing GET /api/exams/course/1 (get exam for course 1)" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8084/api/exams/course/1" -Method GET
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Submit exam answers
Write-Host "`n4. Testing POST /api/exams/1/submit (submit exam answers)" -ForegroundColor Yellow
$submissionData = @{
    userId = 123
    answers = '{"q1": 0, "q2": 1}'
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8084/api/exams/1/submit" -Method POST -Body $submissionData -ContentType "application/json"
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Get results for user
Write-Host "`n5. Testing GET /api/results/user/123 (get results for user 123)" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8084/api/results/user/123" -Method GET
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Create another exam for course 2
Write-Host "`n6. Testing POST /api/exams (create exam for course 2)" -ForegroundColor Yellow
$examData2 = @{
    courseId = 2
    questions = '{"q1": {"question": "What is REST?", "options": ["Sleep", "API Style", "Music"], "correct": 1}}'
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8084/api/exams" -Method POST -Body $examData2 -ContentType "application/json"
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Testing Complete ===" -ForegroundColor Green 