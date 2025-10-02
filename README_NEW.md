# RUC EPP Integration API

**Production-Ready Spring Boot 3.2.5 + Java 17** application for Electronic Payment Platform (EPP) integration with Pennsylvania's Road User Charge (RUC) system.

---

## 🎯 Overview

This is a **generic, reusable payment integration component** designed for Pennsylvania's EPP Commerce Hub. It can be integrated into any Spring Boot application following Clean Architecture principles.

**Key Features:**
- ✅ EPP payment initiation and processing
- ✅ **5 Integration Methods** (Direct, Template, ModelAndView, AJAX, REST API)
- ✅ **Ultra-Modern Animated UI** with glassmorphism design
- ✅ Transaction persistence and auditing
- ✅ H2 (testing) and Oracle (production) database support
- ✅ Professional test UI with card-based method selection
- ✅ Comprehensive error handling and validation
- ✅ **Back Button Support** with automatic loading state reset

---

## 🚀 Quick Start

### Prerequisites
- **Java 17+** (Required)
- **Maven 3.6+**
- **Oracle Database** (production) or **H2** (testing)

### Running the Application

```powershell
# Set environment and run (use same terminal session)
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"
$env:SPRING_PROFILES_ACTIVE="test"
mvn clean spring-boot:run
```

**Application URL**: http://localhost:8080

### Test the Application
- **Health Check**: http://localhost:8080/payments/epp/ping → Returns "pong"
- **Test UI**: http://localhost:8080/test/form → **Modern Payment Form with 5 Integration Methods**
- **API Endpoint**: POST http://localhost:8080/payments/epp/start

For detailed startup instructions, see **QUICK_START.md**

---

## 📋 API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/payments/epp/ping` | GET | Health check - Returns "pong" |
| `/payments/epp/start` | POST | Initiate EPP payment |
| `/payments/epp/OnEPPResult` | POST | EPP callback handler (receives payment result) |
| `/payments/epp/debug-json` | POST | JSON payload debugging tool |
| `/test/form` | GET | **Modern UI with 5 integration methods** |
| `/test/method1-direct` | POST | Direct Response integration |
| `/test/method2-template` | POST | Template Engine integration |
| `/test/method3-modelview` | POST | ModelAndView integration |
| `/test/method4-ajax` | POST | **AJAX/JSON Response integration (NEW)** |
| `/test/method5-rest` | POST | **REST API integration (NEW)** |
| `/test/ruc-invoke` | GET | RUC payment invoke page (ModelAndView) |

---

## � Integration Methods

The application now supports **5 distinct integration patterns** for maximum flexibility:

### Method 1: Direct Response (⚡ Purple)
- **Backend**: Writes HTML directly to `HttpServletResponse`
- **Frontend**: Browser receives and auto-submits form
- **Use Case**: .NET-style integration, legacy systems
- **Redirect**: Automatic via embedded JavaScript

### Method 2: Template Engine (🎨 Pink)
- **Backend**: Thymeleaf template rendering (`epp-redirect.html`)
- **Frontend**: Server-rendered HTML with auto-submit
- **Use Case**: Spring Boot applications, server-side rendering
- **Redirect**: Automatic via template script

### Method 3: ModelAndView (📱 Cyan)
- **Backend**: Traditional MVC with `ModelAndView` object
- **Frontend**: Template-based rendering (`RucInvoke.html`)
- **Use Case**: Classic Spring MVC applications
- **Redirect**: Automatic via template script

### Method 4: AJAX Response (🔄 Orange) - **NEW**
- **Backend**: Returns JSON with `formHtml` field
- **Frontend**: JavaScript fetches JSON, injects HTML, auto-submits
- **Use Case**: Single Page Applications (SPAs), modern web apps
- **Redirect**: Client-side via JavaScript fetch and DOM injection
- **Response Format**:
  ```json
  {
    "success": true,
    "orderKey": "TEST-ORDER-xxx",
    "gatewayUrl": "https://epp.beta.pa.gov/Payment/Index",
    "formHtml": "<form>...</form><script>...</script>",
    "message": "Payment form generated successfully"
  }
  ```

### Method 5: REST API (🌐 Teal) - **NEW**
- **Backend**: Returns pure JSON with structured payment data
- **Frontend**: JavaScript builds form manually from JSON and submits
- **Use Case**: Microservices, mobile apps, API integrations
- **Redirect**: Client-side via JavaScript form building
- **Response Format**:
  ```json
  {
    "success": true,
    "orderKey": "TEST-ORDER-xxx",
    "applicationCode": "3256d54a-9e63-4c7d-b2f9-a2897ec82aab",
    "amount": 100.00,
    "gatewayUrl": "https://epp.beta.pa.gov/Payment/Index",
    "paymentData": "{\"SaleDetailId\":0,...}",
    "timestamp": "2025-10-02T14:30:00",
    "instructions": "Submit paymentData to gatewayUrl as 'saleDetail' parameter"
  }
  ```

---

## �🏗️ Project Architecture

This application follows **Clean Architecture** with clear separation of concerns:

```
src/main/java/com/ruc/payments/
├── EppIntegrationApplication.java    # Main Spring Boot application
├── controller/
│   ├── PaymentController.java        # EPP payment REST endpoints
│   └── TestUIController.java         # Test UI with 5 integration methods
├── service/
│   ├── PaymentService.java           # Payment business logic interface
│   ├── EppClient.java                # EPP system integration client
│   └── impl/
│       └── PaymentServiceImpl.java   # Payment service implementation
├── repo/
│   └── EppTransactionRepository.java # Spring Data JPA repository
├── dto/
│   ├── SaleDetails.java              # Payment request DTO
│   ├── SaleItems.java                # Payment items DTO
│   ├── EppResponse.java              # EPP callback response DTO
│   └── ApplicationResponse.java      # Generic API response DTO
├── entity/
│   └── EppTransaction.java           # JPA entity for transaction persistence
├── config/
│   ├── EppProperties.java            # EPP configuration properties
│   ├── AppConfig.java                # General application configuration
│   └── CorsConfig.java               # CORS configuration
├── exception/
│   └── PaymentProcessingException.java # Custom exception
└── util/
    └── ModelMapper.java              # Entity/DTO mapping utility

src/main/resources/templates/
├── test-form-modern.html             # Modern UI with 5 methods (animated)
├── epp-redirect.html                 # Method 2 template
├── RucInvoke.html                    # Method 3 template
└── EpgInvoke.html                    # Alternative template

src/test/java/com/ruc/payments/
├── PaymentControllerTest.java        # Controller layer tests
├── EppClientTest.java                # EPP client unit tests
└── EppTransactionRepositoryTest.java # Repository layer tests
```

### Key Components

**Controllers (Thin Layer)**
- Minimal logic, delegate all work to services
- Handle HTTP requests/responses only
- Input validation with `@Valid`

**Services (Business Logic)**
- `PaymentService`: Orchestrates payment workflow
- `EppClient`: EPP form generation and integration
- Transaction management with `@Transactional`

**Repository (Data Layer)**
- Spring Data JPA for database operations
- Clean data access boundaries

**Configuration**
- `EppProperties`: EPP-specific settings (Application Code, Gateway URL, Return URL)
- Profile-based configuration (test/local)

---

## ⚙️ Configuration

### Database Profiles

**Test Profile (H2 In-Memory)**
```powershell
$env:SPRING_PROFILES_ACTIVE="test"
```
- No database setup required
- Migrations: `src/main/resources/db/migration/h2/`
- Perfect for development and testing

**Local Profile (Oracle)**
```powershell
$env:SPRING_PROFILES_ACTIVE="local"
```
- Requires Oracle 12c database
- Migrations: `src/main/resources/db/migration/`
- Production-ready configuration

### EPP Integration Methods

Configure in `application.yml`:

```yaml
epp:
  test:
    use-direct-response: false  # false=Spring template, true=.NET-style
```

**Three Integration Approaches:**

1. **Direct Response** (`use-direct-response: true`)
   - .NET-style HTML form written directly to HttpServletResponse
   - Auto-submits to EPP gateway

2. **Spring Template** (`use-direct-response: false`)
   - Uses Thymeleaf template `epp-redirect.html`
   - Recommended for Spring applications

3. **ModelAndView Pattern**
   - Separate endpoint using `EpgInvoke.html`
   - Follows Rahul's Java implementation pattern

### Environment Variables

```yaml
# EPP Configuration (required)
epp:
  application-code: ${EPP_APPLICATION_CODE}  # UUID format (50 chars max)
  payment-gateway-url: ${EPP_GATEWAY_URL}
  return-url: ${EPP_RETURN_URL}
```

---

## 🧪 Testing

### Run Tests
```powershell
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=PaymentControllerTest
```

### Test Coverage
- ✅ **PaymentControllerTest** - REST controller integration tests
- ✅ **EppClientTest** - EPP client unit tests
- ✅ **EppTransactionRepositoryTest** - Repository layer tests

### Using the Test UI
1. Start application: `mvn spring-boot:run`
2. Open browser: http://localhost:8080/test/form
3. Fill payment details:
   - Sale Detail ID (optional)
   - Application Unique ID (optional)
   - Order Key (auto-generated)
   - Email address
   - Item description
   - Amount (USD)
4. Click "Submit Payment"
5. Review generated EPP form (redirects to EPP gateway)

### Using Postman

Import the collection from `postman/RUC_EPP_Complete.postman_collection.json`:

**Available Requests:**
- Health Check - Ping
- Start Payment (single item)
- Start Payment (multiple items)
- EPP Result Callback - Complete
- EPP Result Callback - Cancelled
- EPP Result Callback - Declined
- Debug JSON
- Error scenarios (invalid data)

**Features:**
- ✅ Complete headers and JSON bodies
- ✅ Pre-request scripts (auto-generate orderKey, timestamps)
- ✅ Test assertions (status codes, response validation)
- ✅ Environment variables support

---

## 💡 EPP Integration Details

### Payment Flow

1. **Initiate Payment**
   ```
   POST /payments/epp/start
   Body: SaleDetails (items, email, amounts)
   ```

2. **Generate EPP Form**
   - `EppClient` builds HTML form with encrypted payload
   - Auto-submits to Pennsylvania EPP Commerce Hub

3. **EPP Processing**
   - User completes payment on EPP hosted page
   - EPP validates and processes transaction

4. **Callback Result**
   ```
   POST /payments/epp/OnEPPResult
   Body: EppResponse (status, orderKey, transactionId)
   ```

5. **Status Handling**
   - `COM` - Payment completed successfully
   - `CAN` - User cancelled payment
   - `DEC` - Payment declined
   - `APP` - Payment approved (pending settlement)
   - `PEN` - Payment pending

### EPP Payload Format

```json
{
  "applicationCode": "uuid-format-50-chars-max",
  "orderKey": "unique-transaction-identifier",
  "applicationUniqueId": "optional-application-reference",
  "items": [
    {
      "itemKey": "matches-orderKey",
      "description": "RUC Payment - Road Usage Charge",
      "amount": 25.00
    }
  ],
  "billEmail": "user@example.com",
  "enteredDate": "2025-10-02T10:30:00",
  "transactionDate": "2025-10-02T10:30:00"
}
```

**Key Requirements:**
- `applicationCode`: Must be UUID format
- `orderKey`: Unique per transaction (auto-generated)
- `items`: Required array (even for single item)
- `itemKey`: Automatically set to match `orderKey`
- Timestamps: Auto-populated in ISO 8601 format

---

## 🗄️ Database Schema

### Transaction Table

```sql
CREATE TABLE epp_transactions (
    id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    order_key VARCHAR2(100) NOT NULL UNIQUE,
    application_unique_id VARCHAR2(100),
    application_code VARCHAR2(50),
    status VARCHAR2(20),
    raw_request CLOB,
    raw_response CLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);
```

**Columns:**
- `order_key`: Unique transaction identifier
- `application_unique_id`: Optional external reference
- `status`: Payment status (COM, CAN, DEC, APP, PEN)
- `raw_request`: Complete EPP request JSON
- `raw_response`: Complete EPP callback JSON

### Flyway Migrations

Migrations run automatically on startup:
- **H2**: `src/main/resources/db/migration/h2/V1__epp_schema.sql`
- **Oracle**: `src/main/resources/db/migration/V1__epp_schema_oracle.sql`

---

## 🔒 Security Features

- ✅ **Input Validation**: Bean validation (@Valid) on all DTOs
- ✅ **HTML XSS Protection**: Proper escaping in EPP form generation
- ✅ **Transaction Security**: Idempotent payment operations
- ✅ **Error Handling**: Comprehensive exception management
- ✅ **Audit Trail**: All transactions logged with timestamps
- ✅ **Environment Variables**: Secrets not hardcoded

---

## 🚢 Production Deployment

### Build JAR

```powershell
mvn clean package -DskipTests
```

Output: `target/epp-integration-1.0.0.jar`

### Run Production JAR

```powershell
# With Oracle database (local profile)
java -jar target/epp-integration-1.0.0.jar --spring.profiles.active=local
```

### Environment Variables (Production)

```powershell
$env:EPP_APPLICATION_CODE="your-uuid-from-epp-team"
$env:EPP_GATEWAY_URL="https://epp.pa.gov/payment/gateway"
$env:EPP_RETURN_URL="https://your-domain.com/payments/epp/OnEPPResult"
$env:SPRING_DATASOURCE_URL="jdbc:oracle:thin:@prod-db:1521:ORCL"
$env:SPRING_DATASOURCE_USERNAME="username"
$env:SPRING_DATASOURCE_PASSWORD="password"
```

---

## 🐛 Troubleshooting

### Port Already in Use

```powershell
# Kill existing Java processes
taskkill /f /im java.exe

# Or find specific process
netstat -ano | findstr :8080
taskkill /PID <PROCESS_ID> /F
```

### Build Issues

```powershell
# Clean and rebuild
mvn clean compile

# Skip tests if needed
mvn clean package -DskipTests
```

### Database Connection Issues

- **H2**: Automatic - no configuration needed
- **Oracle**: Check `application.yml` datasource settings
- **Flyway**: Migrations run on startup - check logs

### EPP Integration Issues

- Verify `EPP_APPLICATION_CODE` is valid UUID format
- Check `use-direct-response` setting matches your integration method
- Review logs for JSON serialization errors
- Test with `/payments/epp/debug-json` endpoint

---

## 📚 Additional Documentation

- **INTEGRATION_GUIDE.md** - Step-by-step integration instructions for copying this component into your project
- **QUICK_START.md** - Quick reference guide with common commands and verification checklist
- **postman/RUC_EPP_Complete.postman_collection.json** - Complete Postman collection for API testing

---

## 🛠️ Technology Stack

- **Spring Boot**: 3.2.5
- **Java**: 17
- **Spring Data JPA**: Database abstraction
- **Flyway**: Database migrations
- **Thymeleaf**: Template engine
- **H2**: In-memory database (testing)
- **Oracle**: Production database
- **Maven**: Build tool
- **HikariCP**: Connection pooling

---

## 📝 Project Status

**Version**: 1.0.0  
**Status**: ✅ Production Ready  
**Last Updated**: October 2, 2025

### Code Quality
- ✅ Clean Architecture principles
- ✅ Comprehensive test coverage
- ✅ No code redundancy
- ✅ Best practices followed
- ✅ Well-documented

### Testing Status
- ✅ All unit tests passing
- ✅ Integration tests passing
- ✅ Manual testing with Test UI successful
- ✅ Postman collection verified

---

## 🤝 Integration & Support

**For Integration:**
1. Read **INTEGRATION_GUIDE.md** for step-by-step instructions
2. Copy required files to your project
3. Configure EPP properties
4. Test with included Postman collection

**For Issues:**
1. Check troubleshooting section above
2. Review application logs
3. Test with `/test/form` UI
4. Verify EPP connectivity

---

## 📄 License

Internal use for Pennsylvania Department of Transportation (PennDOT) Road User Charge (RUC) payment processing.

---

**Ready to integrate?** See **INTEGRATION_GUIDE.md** for detailed instructions on copying this component into your Spring Boot application.
