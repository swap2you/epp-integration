# EPP Integration - Quick Runner Guide

## Prerequisites
- Java 17+ installed
- Maven 3.8+ installed
- Git configured with your credentials

## Quick Start Commands

### 1. Environment Setup (PowerShell)
```powershell
# Set Java Home (if needed)
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"

# Verify versions
java -version
mvn --version
```

### 2. Build & Run Project
```powershell
# Clean build
mvn clean compile

# Run with test profile (H2 database)
$env:SPRING_PROFILES_ACTIVE="test"
mvn spring-boot:run

# Run with Oracle profile (local database)
$env:SPRING_PROFILES_ACTIVE="local"
mvn spring-boot:run
```

### 3. Health Check
- Application URL: http://localhost:8080
- Ping Endpoint: http://localhost:8080/payments/epp/ping
- Test Form: http://localhost:8080/test/form

### 4. Common Build Tasks
```powershell
# Full clean build
mvn clean install

# Skip tests during build
mvn clean install -DskipTests

# Run only tests
mvn test

# Package application
mvn package

# Run specific test class
mvn test -Dtest=YourTestClass
```

## Profile Configuration

### Test Profile (H2 Database)
```powershell
$env:SPRING_PROFILES_ACTIVE="test"
```
- Uses in-memory H2 database
- Perfect for development and testing
- No external database required

### Local Profile (Oracle)
```powershell
$env:SPRING_PROFILES_ACTIVE="local"
```
- Requires Oracle 12c+ database
- Configure connection in `application-local.yml`

## Troubleshooting

### Common Issues
1. **Port 8080 in use**: Change port in application.yml or kill process
2. **Java version**: Ensure Java 17+ is set in JAVA_HOME
3. **Maven not found**: Add Maven to PATH environment variable
4. **Database connection**: Check Oracle service status for local profile

### Debug Mode
```powershell
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

## Production Deployment
```powershell
# Create production JAR
mvn clean package -Pprod

# Run production JAR
java -jar target/epp-integration-1.0.0.jar --spring.profiles.active=prod
```