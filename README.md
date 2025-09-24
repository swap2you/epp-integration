# RUC EPP Integration API

A clean, production-ready Spring Boot application for integrating with Pennsylvania's Electronic Payment Platform (EPP) for Road User Charge (RUC) payments.

## 🎯 Overview

This application provides a simple REST API that connects your existing systems to Pennsylvania's EPP payment gateway. It handles all the complex EPP integration details, JSON formatting, and transaction management automatically.

**Key Features:**
- ✅ **EPP Hosted Checkout** - Redirect users directly to EPP's secure payment page
- ✅ **Clean Architecture** - Modular, maintainable code with proper separation of concerns  
- ✅ **Automatic Validation** - Input validation and EPP format compliance
- ✅ **Transaction Management** - Idempotent operations and proper error handling
- ✅ **Multiple Environments** - Test (H2) and Production (Oracle) profiles
- ✅ **Simple Testing UI** - Built-in form for testing EPP integration
- ✅ **Maven Compiler Fixed** - Parameter resolution issue resolved with `-parameters` flag
- ✅ **Comprehensive Debugging** - Detailed logging throughout request flow
- ✅ **Form Submission Working** - Test UI successfully submits to EPP portal
- ✅ **Error Handling Enhanced** - Detailed error messages and stack traces

**Tech Stack:** Java 17, Spring Boot 3.2.5, Maven, H2/Oracle, Flyway

---

## Features

- **EPP Hosted Checkout**: `/payments/epp/start` auto-posts SaleDetails+Items to EPP.
- **Callback**: `/payments/epp/result` receives EPPResponse, returns ApplicationResponse.
- **Idempotency**: Handles EPP retries (upsert by orderKey+applicationUniqueId).
- **Statuses**: `APP`, `COM`, `CAN`, `DEC`, `PEN`, `SEN`.
- **Feature flag**: `ruc.payments.provider = payeasy|epp`.
- **Oracle 12c**: CLOB for JSON, HikariCP pooling (local profile).
- **H2 Database**: In-memory database for testing.
- **Flyway**: Separate migrations for Oracle and H2.
- **Tests**: Unit, slice, integration (H2).
- **Postman**: `/start` and `/result` flows.

---

## Architecture

### Layered Design
- **Controller Layer**: Thin REST controllers with minimal logic
- **Service Layer**: Business logic, validation, and transaction management  
- **Repository Layer**: Data access and persistence
- **Utility Layer**: Reusable components and helpers

### Key Components
- `PaymentController`: REST endpoints for payment operations
- `PaymentService`: Core business logic for payment processing
- `TransactionService`: Transaction management and data operations
- `EppClient`: External EPP gateway integration
- `ModelMapper`: Entity/DTO conversion utilities
- `ValidationUtils`: Input validation and sanitization

### Clean Architecture Benefits
- **Separation of Concerns**: Each layer has specific responsibilities
- **Testability**: Easy to unit test with mocked dependencies
- **Maintainability**: Changes isolated to appropriate layers
- **Reusability**: Utility classes shared across components

---

## Prerequisites

- **Java 17+** (JDK installed at `C:\Program Files\Java\jdk-17.0.12`)
- **Maven 3.8+**
- **Oracle 12c+** (for local/production)
- **Oracle JDBC driver** (`ojdbc11`)

---

## Quick Start Guide

### 1. Check Versions First (Same Terminal)

**PowerShell Commands:**
```powershell
# Check Maven and Java versions
mvn --version
java -version
```

### 2. Configure and Run (Same Terminal - Do Not Open New Terminal)

```powershell
# Set Java 17 environment
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"

# Set Spring profile to test (uses H2 in-memory database)
$env:SPRING_PROFILES_ACTIVE="test"

# Clean build and run the application
mvn clean spring-boot:run
```

**Expected Maven Output:**
```
Apache Maven 3.9.11
Java version: 17.0.12, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-17.0.12
```

### 3. Verify Application is Running

**Test the health endpoint:**
```
GET http://localhost:8080/payments/epp/ping
```
**Expected Response:** `pong`

---

## Recent Fixes & Debugging Features

### 🔧 Maven Compiler Parameter Resolution Fix

**Issue Fixed:** `java.lang.IllegalArgumentException: Name for argument of type [java.lang.String] not specified`

**Solution Applied:**
1. **Added Maven Compiler Plugin** with `-parameters` flag in `pom.xml`:
   ```xml
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-compiler-plugin</artifactId>
       <version>3.13.0</version>
       <configuration>
           <source>17</source>
           <target>17</target>
           <parameters>true</parameters>  <!-- Key fix -->
       </configuration>
   </plugin>
   ```

2. **Explicit @RequestParam names** in `TestUIController`:
   ```java
   @PostMapping("/submit")
   public String submitTest(
           @RequestParam("applicationCode") String applicationCode,
           @RequestParam("orderKey") String orderKey,
           // ... etc
   ```

### 🔍 Comprehensive Debugging Features

The application now includes extensive logging for troubleshooting:

**Controller Initialization:**
```
🏗️ TestUIController CONSTRUCTOR called!
✅ TestUIController initialized with RestTemplate and ObjectMapper
```

**Form Page Access (`/test`):**
```
📋 /test form page requested - showTestForm() called
🔑 Generated Order Key: TEST-ORDER-[timestamp]
✅ Form pre-populated, returning test-form template
```

**Form Submission (`/test/submit`):**
```
🚀 SUBMIT ENDPOINT HIT! /test/submit
✅ Form Data Received: [all parameters logged]
🔄 Building SaleDetails object...
🌐 Preparing HTTP call to /payments/epp/start...
📤 JSON Request Body: [full JSON payload]
✅ HTTP Response received!
📄 EPP Form HTML length: [size]
```

### 🧪 Test Form Features

- **Pre-populated Test Data** - Ready to test immediately
- **Error Display** - Shows validation errors and exceptions
- **Ping Tests** - Built-in connectivity testing buttons
- **Auto-Submit to EPP** - Redirects directly to Pennsylvania EPP portal

---

## API Testing

### Using Postman

1. **Import Collection**: `./postman/RUC_EPP.postman_collection.json`

2. **Test Health Check**:
   - **GET** `http://localhost:8080/payments/epp/ping`
   - Response: `pong`

3. **Test Payment Start** (✅ FIXED - Updated validation constraints):
   - **POST** `http://localhost:8080/payments/epp/start`
   - **Body:**
     ```json
     {
       "saleDetailId": 1,
       "applicationCode": "3256d54a-9e63-4c7d-b2f9-a2897ec82aab",
       "orderKey": "TEST-ORDER-12345",
       "firstName": "John",
       "lastName": "Doe",
       "address1": "123 Main Street",
       "address2": "Apt 4B",
       "city": "Philadelphia",
       "stateCode": "PA",
       "zipCode": "19101",
       "totalAmount": 150.75,
       "applicationUniqueId": "UNIQUE-123456",
       "paymentAccountType": "CC",
       "email": "john.doe@example.com",
       "items": [
         {
           "saleItemId": 1,
           "count": 2,
           "description": "RUC Registration Fee",
           "amount": 75.375,
           "itemKey": "REG-FEE-001"
         }
       ]
     }
     ```
   - **Response:** HTML form that auto-posts to EPP

4. **Test EPP Callback (OnEPPResult)** - Primary Endpoint:
   - **POST** `http://localhost:8080/payments/epp/OnEPPResult`
   - **Body:**
     ```json
     {
       "orderKey": "TEST-ORDER-12345",
       "applicationUniqueId": "UNIQUE-123456",
       "applicationCode": "3256d54a-9e63-4c7d-b2f9-a2897ec82aab",
       "status": "COM",
       "errorMessage": null,
       "cardHolderName": "John Doe",
       "address": "123 Main Street",
       "city": "Philadelphia",
       "stateCode": "PA",
       "zipCode": "19101",
       "totalAmount": 150.75,
       "emailId": "john.doe@example.com",
       "referenceNumber": "AUTH123456",
       "paymentAccountType": "Visa"
     }
     ```
   - **Response:** ApplicationResponse JSON

5. **Test Legacy Callback (Backward Compatibility)**:
   - **POST** `http://localhost:8080/payments/epp/result`
   - **Body:** Same as OnEPPResult above
   - **Response:** ApplicationResponse JSON

---

## 🔥 Commerce Hub Integration

### Quick Commerce Hub Testing (Copy & Paste)

**✅ Commerce Hub Compliant Payload** (Fixed validation - no more "required" errors):

```json
POST http://localhost:8080/payments/epp/start
Content-Type: application/json

{
  "saleDetailId": null,
  "orderKey": "CH-ORDER-12345",
  "applicationCode": "3256d54a-9e63-4c7d-b2f9-a2897ec82aab",
  "firstName": "John",
  "lastName": "Doe",
  "address1": "123 Commerce Street",
  "city": "Philadelphia",
  "stateCode": "PA",
  "zipCode": "19101",
  "totalAmount": 25.00,
  "applicationUniqueId": null,
  "email": "commerce@example.com",
  "items": [
    {
      "saleItemId": null,
      "count": 1,
      "description": "RUC Road User Charge",
      "amount": 25.00,
      "itemKey": ""
    }
  ]
}
```

**Key Commerce Hub Features:**
- ✅ **No Validation Errors**: applicationUniqueId can be null (validation fix applied)
- ✅ **Nullable Fields**: saleDetailId and saleItemId support null values
- ✅ **Auto itemKey**: Empty itemKey automatically populated with orderKey
- ✅ **Flexible Integration**: Minimal required fields for Commerce Hub compatibility

**Expected Success Response:**
```html
<!DOCTYPE html>
<html>
<head><title>EPP Payment Form</title></head>
<body>
<form id="eppForm" method="POST" action="https://epp.example.com/payment">
  <!-- EPP form fields auto-populated -->
</form>
<script>document.getElementById('eppForm').submit();</script>
</body>
</html>
```

### Commerce Hub Integration Troubleshooting

| Issue | Solution | Status |
|-------|----------|---------|
| "Application unique ID is required" | ✅ **FIXED** - Validation removed | Resolved |
| saleDetailId type errors | Use `null` instead of integer | ✅ Ready |
| itemKey not populated | Use empty string `""` | ✅ Auto-fills |

---

## Configuration

### Test Profile (H2 Database)
- **Location**: `src/main/resources/application-test.yml`
- **Database**: H2 in-memory
- **Migrations**: `classpath:db/migration/h2`
- **Auto-configured**: Uses H2Dialect automatically

### Local Profile (Oracle Database)
- **Location**: `src/main/resources/application.yml` (local profile section)
- **Database**: Oracle 12c
- **Migrations**: `classpath:db/migration` (Oracle-specific)
- **Environment Variables**:
  ```powershell
  set ORACLE_PASSWORD=your_password
  ```

### Run with Oracle (Local Development)
```powershell
set SPRING_PROFILES_ACTIVE=local
set ORACLE_PASSWORD=your_password
mvn spring-boot:run
```

---

## Database Schema

### H2 Test Schema
- **Location**: `src/main/resources/db/migration/h2/V1__epp_schema.sql`
- **Tables**: `epp_transaction`, `epp_feature_flag`
- **Features**: Auto-identity columns, unique constraints

### Oracle Production Schema  
- **Location**: `src/main/resources/db/migration/V1__epp_schema_oracle.sql`
- **Tables**: `epp_transaction`, triggers, indexes
- **Features**: CLOB for JSON, Oracle-specific syntax

---

## Build & Test

```powershell
# Run all tests
mvn clean test

# Build and verify
mvn clean verify

# Package application
mvn clean package
```

---

## Troubleshooting

### Common Issues

1. **Java Version Error**
   ```
   Error: org.springframework.boot.maven.RunMojo has been compiled by a more recent version
   ```
   **Solution**: Set Java 17 environment as shown in Quick Start

2. **Oracle Dialect Error with Test Profile**
   ```
   Error: Unable to resolve name [org.hibernate.dialect.Oracle12cDialect]
   ```
   **Solution**: Ensure `SPRING_PROFILES_ACTIVE=test` is set

3. **Flyway Migration Conflicts**
   ```
   Error: Found more than one migration with version 1
   ```
   **Solution**: Only H2 migrations should exist in `db/migration/h2/`

### Reset Steps
```powershell
# Clean everything and start fresh
mvn clean
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"
$env:Path="$env:JAVA_HOME\bin;" + $env:Path
set SPRING_PROFILES_ACTIVE=test
mvn spring-boot:run
```

---

## Development Workflow

### Standard Development Session
1. Open PowerShell in project directory
2. Set Java 17 environment
3. Set test profile
4. Run application
5. Test with Postman
6. Make code changes
7. Restart application to test changes

### Code Changes
- Application automatically reloads on file changes when using Spring Boot DevTools
- Database schema changes require Flyway migration scripts
- Configuration changes may require application restart

---

## Oracle JDBC Driver Setup

Due to Oracle licensing, download `ojdbc11-23.4.0.24.05.jar` from [Oracle](https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html) and install:

```sh
mvn install:install-file -Dfile=ojdbc11-23.4.0.24.05.jar -DgroupId=com.oracle.database.jdbc -DartifactId=ojdbc11 -Dversion=23.4.0.24.05 -Dpackaging=jar
```

---

## Production Deployment

- Set `ruc.payments.provider=epp` in environment
- Configure Oracle connection string and credentials
- Use `local` or `prod` profile (not `test`)
- Ensure Flyway migrations are applied in correct order
- Monitor application health via `/payments/epp/ping`

---
