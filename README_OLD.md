# EPP Integration API

**Simple, Production-Ready Spring Boot 3.2.5 + Java 17** application for Electronic Payment Platform (EPP) integration with Pennsylvania's Road User Charge (RUC) system.

## 🚀 Quick Start

### Prerequisites
- **Java 17+** (Required)
- **Maven 3.6+**

### Running the Application

```powershell
# Set environment and run (use same terminal session)
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"
$env:SPRING_PROFILES_ACTIVE="test"
mvn spring-boot:run
```

**Application URL**: http://localhost:8081

## 📋 API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/payments/epp/ping` | GET | Health check → Returns "pong" |
| `/test/form` | GET | Test payment form UI |
| `/payments/epp/start` | POST | Initiate EPP payment |
| `/payments/epp/OnEPPResult` | POST | EPP callback handler |
| `/payments/epp/debug-json` | POST | JSON debugging |

## 🏗️ Project Structure

```
src/main/java/com/ruk/payments/
├── EppIntegrationApplication.java    # Main Spring Boot app
├── controller/
│   ├── PaymentController.java       # EPP payment endpoints
│   └── TestUIController.java        # Test UI endpoints
├── service/
│   ├── PaymentService.java          # Payment business logic interface
│   ├── TransactionService.java      # Transaction operations interface
│   ├── EppClient.java               # EPP system integration
│   └── impl/
│       ├── PaymentServiceImpl.java  # Payment implementation
│       └── TransactionServiceImpl.java
├── repo/
│   └── EppTransactionRepository.java # JPA repository
├── dto/
│   ├── SaleDetails.java             # EPP sale request DTO
│   ├── SaleItems.java               # EPP items DTO
│   ├── EppResponse.java             # EPP callback response
│   └── ApplicationResponse.java     # API response DTO
├── entity/
│   └── EppTransaction.java          # JPA entity
├── config/
│   ├── EppProperties.java           # EPP configuration
│   ├── AppConfig.java               # Application config
│   ├── WebConfig.java               # Web MVC config
│   └── CorsConfig.java              # CORS configuration
├── exception/
│   └── PaymentProcessingException.java
└── util/
    └── ModelMapper.java             # Entity/DTO mapping

src/test/java/com/ruk/payments/
├── PaymentControllerTest.java       # Controller tests
├── EppTransactionRepositoryTest.java # Repository tests
└── EppClientTest.java               # EPP client tests
```

## ⚙️ Configuration

### Database Profiles
- **`test`** profile: H2 in-memory (development) - Migrations: `db/migration/h2/`
- **`local`** profile: Oracle 12c (production) - Migrations: `db/migration/`

### EPP Integration Toggle
Choose integration method in `application-test.yml`:
```yaml
epp:
  test:
    use-direct-response: true  # false=Spring template, true=.NET-style
```

**Three Integration Methods:**
1. **Direct Response** - .NET-style HTML form (auto-submit)
2. **Spring Template** - Thymeleaf `epp-redirect.html`
3. **ModelAndView** - Spring MVC with `EpgInvoke.html`

## 🧪 Testing

```powershell
# Run all tests
mvn test

# Build and package
mvn clean package

# Run JAR
java -jar target/epp-integration-1.0.0.jar --spring.profiles.active=test
```

**Test Coverage**: 3 core test suites with MockMvc and @MockBean

## 🔒 Security Features

- ✅ **HTML XSS Protection**: Proper escaping in EPP JSON embedding
- ✅ **Input Validation**: Bean validation (@Valid) on all DTOs
- ✅ **Transaction Security**: Idempotent EPP operations
- ✅ **Error Handling**: Comprehensive exception management

## 💡 EPP Integration Details

**Pennsylvania EPP Commerce Hub:**
- **Application Code**: UUID format (50 chars max)
- **Order Key**: Unique per transaction (auto-generated)
- **Items Array**: Required (even for single items)
- **Auto-population**: `itemKey = orderKey`, timestamps

**Payment Status Codes:**
- `COM` - Completed successfully
- `CAN` - Cancelled by user  
- `DEC` - Declined
- `APP` - Approved
- `PEN` - Pending

**Return URL**: EPP calls `/payments/epp/OnEPPResult` with payment results

## 🛠️ Development

### Key Components
- **PaymentService**: Core business logic and orchestration
- **EppClient**: EPP form generation and integration
- **TransactionService**: Database operations and persistence
- **PaymentController**: REST API endpoints
- **TestUIController**: Development testing interface

### Architecture Principles
- **Clean Architecture**: Proper separation of concerns
- **Dependency Injection**: Constructor-based DI
- **Transaction Management**: @Transactional boundaries
- **Exception Handling**: Custom PaymentProcessingException
- **Database Migration**: Flyway for Oracle and H2
- **Connection Pooling**: HikariCP for production Oracle

## 🐛 Troubleshooting

### Port Already in Use
```powershell
# Kill existing Java processes
taskkill /f /im java.exe

# Or use netstat to find specific process
netstat -ano | findstr :8081
taskkill /PID <PROCESS_ID> /F
```

### Build Issues
```powershell
# Clean and rebuild
mvn clean compile

# Skip tests if needed
mvn clean package -DskipTests
```

### Database Issues
- **H2 Profile**: No setup needed - automatic in-memory database
- **Oracle Profile**: Check connection settings in `application.yml`
- **Flyway**: Migrations run automatically on startup

### EPP Integration Issues
- Check `application-test.yml` for correct `use-direct-response` setting
- Verify EPP application code is valid UUID format
- Review logs for JSON serialization errors

## 🚢 Deployment

### Build Production JAR
```powershell
mvn clean package -DskipTests
```

### Run with Profiles
```powershell
# Test profile (H2)
java -jar target/epp-integration-1.0.0.jar --spring.profiles.active=test

# Production profile (Oracle)
java -jar target/epp-integration-1.0.0.jar --spring.profiles.active=local
```

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/epp-integration-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["--spring.profiles.active=test"]
```

### Environment Variables
```powershell
# Required for production
$env:EPP_APPLICATION_CODE="your-uuid-here"
$env:SPRING_DATASOURCE_URL="jdbc:oracle:thin:@localhost:1521:ORCL"
$env:SPRING_DATASOURCE_USERNAME="username"
$env:SPRING_DATASOURCE_PASSWORD="password"
```

## 📊 Postman Testing

Import collections from `postman/` directory:
- **RUC_EPP.postman_collection.json** - Complete EPP workflow
- Test `/start` endpoint with sample payment data
- Verify `/result` callback handling

## 🔧 Configuration Reference

### application-test.yml (H2 Profile)
```yaml
server:
  port: 8081

spring:
  flyway:
    locations: classpath:db/migration/h2
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect

epp:
  test:
    use-direct-response: true
```

### application.yml (Oracle Profile)
```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:ORCL
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  flyway:
    locations: classpath:db/migration
```

## 📝 Recent Changes

### Simplified Architecture (v1.0.0)
- ✅ Removed 14 redundant documentation files
- ✅ Consolidated all docs into single README.md
- ✅ Removed unused utility classes (ValidationUtils, CommonUtils)
- ✅ Removed duplicate test files (JsonSerializationTest, etc.)
- ✅ Kept only essential ModelMapper utility
- ✅ Maintained 3 core test suites
- ✅ Clean Architecture with minimal complexity

### Code Quality Improvements
- ✅ Reduced cognitive complexity in PaymentServiceImpl
- ✅ Fixed HTML XSS vulnerability in EppClient
- ✅ Enhanced exception handling throughout
- ✅ Proper @MockBean configuration in tests
- ✅ All tests passing with comprehensive coverage

---

## 📄 License

Internal use for Pennsylvania Department of Transportation RUC payment processing.

## 🤝 Support

For issues or questions:
1. Check troubleshooting section above
2. Review application logs in terminal
3. Test with built-in `/test/form` UI
4. Verify EPP endpoint connectivity

---

**Status**: ✅ Production Ready | **Version**: 1.0.0 | **Java**: 17+ | **Port**: 8081
