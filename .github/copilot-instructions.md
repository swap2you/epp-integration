# EPP Integration - AI Coding Agent Instructions

## Project Architecture Overview

This is a **Spring Boot 3.2.5 + Java 17** EPP (Electronic Payment Platform) integration API with **Clean Architecture** principles, designed for Pennsylvania's Road User Charge (RUC) payment processing.

### Core Architecture Patterns

**Clean Architecture Layers:**
- `controller/` - Thin HTTP controllers, minimal business logic
- `service/` - Business logic and transaction management  
- `repo/` - Data access layer with Spring Data JPA
- `dto/` - Data transfer objects for API boundaries
- `entity/` - JPA entities with proper database mapping
- `exception/` - Custom exceptions with error codes
- `util/` - Reusable utilities and helpers
- `config/` - Spring configuration and properties

**Key Design Decisions:**
- Controllers delegate ALL business logic to services
- Services contain transaction boundaries and orchestration
- EppClient handles direct EPP system integration
- PaymentService orchestrates the payment workflow
- Repository layer uses Spring Data JPA conventions

### Critical Developer Workflows

**Build & Run Commands:**
```powershell
# Same terminal session required - do not switch terminals
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"
$env:SPRING_PROFILES_ACTIVE="test"  # or "local" for Oracle
mvn clean spring-boot:run
```

**Database Profiles:**
- `test` profile: H2 in-memory (migrations: `db/migration/h2/`)  
- `local` profile: Oracle 12c (migrations: `db/migration/`)
- Flyway manages all schema changes - never modify entities for DDL

**Testing Workflow:**
- UI Test Form: http://localhost:8080/test/form
- Health Check: http://localhost:8080/payments/epp/ping → "pong"
- Debug JSON: POST to `/payments/epp/debug-json`

### Project-Specific Conventions

**EPP Integration Pattern:**
1. `TestUIController` → calls `PaymentController.start()` 
2. `PaymentService.initiatePayment()` → orchestrates business logic
3. `EppClient.buildHostedCheckoutForm()` → generates HTML form
4. **Toggle Feature**: Supports both Spring template and .NET-style direct response

**HTML Form Submission Toggle:**
```yaml
# application.yml
epp:
  test:
    use-direct-response: false  # false=Spring template, true=.NET-style
```

**Response Approaches:**
- `useDirectResponse: false` → Uses `epp-redirect.html` Thymeleaf template
- `useDirectResponse: true` → Writes HTML directly to HttpServletResponse (like .NET)

**EPP-Specific Data Flow:**
- `SaleDetails` → JSON → Encrypted payload → HTML form → Auto-submit to EPP
- Return URL: EPP calls `/payments/epp/OnEPPResult` with payment status
- Status handling: "COM" (complete), "CAN" (cancelled), "DEC" (declined)

### Integration Points & Dependencies

**External Systems:**
- Pennsylvania EPP Commerce Hub (payment gateway)
- Oracle Database (production) / H2 (testing)
- Spring Boot DevTools (development)

**Cross-Component Communication:**
- Controllers → Services (dependency injection)
- Services → EppClient (EPP system integration)  
- EppClient → EppTransactionRepository (persistence)
- All HTTP calls use RestTemplate with proper error handling

**Configuration Management:**
- `EppProperties` class manages all EPP-related configuration
- Profile-specific YAML files for environment isolation
- Environment variables for sensitive data (EPP_APPLICATION_CODE, etc.)

### Critical Implementation Details

**EPP Payload Format:**
- ApplicationCode: UUID format (50 chars max)
- OrderKey: Must be unique per transaction
- Items array: Required even for single items
- Auto-populated fields: itemKey = orderKey, timestamps

**Error Handling Pattern:**
- Custom `PaymentProcessingException` with error codes
- Controllers return appropriate HTTP status codes
- All exceptions logged with transaction context

**Security & Validation:**
- Bean Validation (@Valid) on all DTOs
- HTML escaping in EPP form generation  
- Transaction idempotency in EppClient
- CORS configuration for test UI integration

This codebase follows Spring Boot conventions but with EPP-specific adaptations for Pennsylvania's payment system requirements.
- ✅ **PRODUCTION TESTING READY** - Application running successfully on port 8080
- ✅ **COMMERCE HUB COMPLIANT** - Rahul's field requirements implemented
- ✅ **NULLABLE FIELD SUPPORT** - saleDetailId and saleItemId can be null
- ✅ **OPTIONAL FIELD VALIDATION** - applicationUniqueId is optional
- ✅ **AUTO ITEMKEY POPULATION** - itemKey automatically set to orderKey
- ✅ **VALIDATION FIX APPLIED** - No more "Application unique ID is required" errors
- ✅ **COMMERCE HUB TESTING READY** - All validation issues resolved

## Architecture Highlights
- **Controller Layer**: Thin REST controllers with minimal logic
- **Service Layer**: All business logic, validation, transaction management
- **Repository Layer**: Clean data access with proper boundaries
- **Utility Layer**: Reusable components and helpers
- **Exception Layer**: Custom exceptions with error codes
- **Configuration Layer**: Proper Spring configuration management

## Documentation Available
- **INTEGRATION_GUIDE.md**: Complete guide for integrating into existing systems
- **REFACTORING_SUMMARY.md**: Detailed summary of architecture changes
- **STARTUP_GUIDE.md**: Updated with new architecture information
- **API_REFERENCE.md**: Quick reference for API usage

## Quick Start Command Sequence (Same Terminal)
```powershell
# Step 1: Check versions first
mvn --version
java -version

# Step 2: Configure environment and run (same terminal)
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"
$env:SPRING_PROFILES_ACTIVE="test"
mvn clean spring-boot:run
```

**Note**: Always use the same terminal session - do not open new terminals between commands.

## Health Check
GET http://localhost:8080/payments/epp/ping → Should return "pong"

## Ready for Integration
The project now follows clean architecture principles with:
- Separation of concerns
- Testable components  
- Maintainable codebase
- Comprehensive documentation
- Production-ready patterns
