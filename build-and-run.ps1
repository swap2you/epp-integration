# Clean Build and Run Script for RUC EPP Integration
Write-Host "=== RUC EPP Integration - Clean Build & Run ===" -ForegroundColor Green
Write-Host ""

# Step 1: Set Java Home
Write-Host "Step 1: Setting JAVA_HOME to JDK 17..." -ForegroundColor Yellow
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17.0.12"
Write-Host "JAVA_HOME set to: $env:JAVA_HOME" -ForegroundColor Green
Write-Host ""

# Step 2: Set Spring Profile
Write-Host "Step 2: Setting Spring Profile to test..." -ForegroundColor Yellow
$env:SPRING_PROFILES_ACTIVE = "test"
Write-Host "Spring Profile set to: $env:SPRING_PROFILES_ACTIVE" -ForegroundColor Green
Write-Host ""

# Step 3: Clean Build
Write-Host "Step 3: Running Maven clean compile..." -ForegroundColor Yellow
# mvn clean install   # Full build: compile + run tests + create JAR (slower, for production/testing)
mvn clean compile     # Quick build: just compile source code (faster, for development)

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "Build successful!" -ForegroundColor Green
    Write-Host ""
    
    # Step 4: Run Application
    Write-Host "Step 4: Starting Spring Boot application..." -ForegroundColor Yellow
    Write-Host "Press Ctrl+C to stop the application" -ForegroundColor Cyan
    Write-Host ""
    mvn spring-boot:run
} else {
    Write-Host ""
    Write-Host "Build failed with exit code: $LASTEXITCODE" -ForegroundColor Red
    Write-Host "Please check the error messages above." -ForegroundColor Yellow
    exit $LASTEXITCODE
}
