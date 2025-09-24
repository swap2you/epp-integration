# RUC EPP Integration API - Startup Guide# RUC EPP Integration API - Startup Guide



✅ **UPDATED GUIDE** - This guide provides step-by-step instructions for starting the RUC EPP Integration API successfully with **ALL RECENT FIXES APPLIED**.✅ **UPDATED GUIDE** - This guide provides step-by-step instructions for starting the RUC EPP Integration API successfully with **FIXED VALIDATION CONSTRAINTS**.



## 🎯 Current Status## 🎯 Current Status

- ✅ **Application Code**: `3256d54a-9e63-4c7d-b2f9-a2897ec82aab` (UUID format supported)- ✅ **Application Code**: `3256d54a-9e63-4c7d-b2f9-a2897ec82aab` (UUID format supported)

- ✅ **Validation Fixed**: ApplicationCode field now accepts up to 50 characters  - ✅ **Validation Fixed**: ApplicationCode field now accepts up to 50 characters  

- ✅ **Commerce Hub Validation Fixed**: applicationUniqueId is now optional (no more "required" errors)- ✅ **Commerce Hub Validation Fixed**: applicationUniqueId is now optional (no more "required" errors)

- ✅ **EPP Compliant**: All Pennsylvania EPP specification requirements implemented- ✅ **EPP Compliant**: All Pennsylvania EPP specification requirements implemented

- ✅ **Testing Ready**: Application running on port 8080 with H2 database- ✅ **Testing Ready**: Application running on port 8080 with H2 database

- ✅ **Commerce Hub Ready**: Nullable fields and flexible validation implemented- ✅ **Commerce Hub Ready**: Nullable fields and flexible validation implemented

- ✅ **Maven Compiler Fixed**: Parameter resolution issue resolved with `-parameters` flag

- ✅ **Comprehensive Debugging**: Detailed logging throughout request flow## Prerequisites Check

- ✅ **Form Submission Working**: Test UI successfully submits to EPP portal

Before starting, ensure you have:

## Prerequisites Check- Java 17 JDK installed at `C:\Program Files\Java\jdk-17.0.12`

- Maven 3.8+ installed

Before starting, ensure you have:- PowerShell terminal access

- Java 17 JDK installed at `C:\Program Files\Java\jdk-17.0.12`

- Maven 3.8+ installed## Quick Start (Step-by-Step)

- PowerShell terminal access

### Step 1: Open PowerShell Terminal

## ⚠️ CRITICAL FIX APPLIED1. Open PowerShell in the project root directory: `c:\Dev Resources\Workspace\EPPTest`

2. Verify you're in the correct directory by running: `Get-Location`

**Parameter Resolution Issue Fixed:** The project now includes the Maven compiler plugin with `-parameters` flag to prevent this error:

```### Step 2: Check Maven and Java Versions

java.lang.IllegalArgumentException: Name for argument of type [java.lang.String] not specified**First verify your environment in the same PowerShell terminal:**

```

```powershell

**What was fixed in `pom.xml`:**# Check Maven version

```xmlmvn --version

<plugin>

    <groupId>org.apache.maven.plugins</groupId># Check Java version

    <artifactId>maven-compiler-plugin</artifactId>java -version

    <version>3.13.0</version>```

    <configuration>

        <source>17</source>### Step 3: Configure Environment (REQUIRED for every new session)

        <target>17</target>**Run these commands in the SAME PowerShell terminal (do not open a new terminal):**

        <parameters>true</parameters>  <!-- This preserves parameter names -->

    </configuration>```powershell

</plugin># Set Java 17 environment

```$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"



This fix ensures that Spring Boot can properly resolve `@RequestParam` parameter names in the test form controller.# Set Spring profile for H2 testing

$env:SPRING_PROFILES_ACTIVE="test"

## Quick Start (Step-by-Step)

# Clean build and start the application

### Step 1: Open PowerShell Terminalmvn clean spring-boot:run

1. Open PowerShell in the project root directory: `c:\Dev Resources\Workspace\EPPTest````

2. Verify you're in the correct directory by running: `Get-Location`

**Expected Maven Output:**

### Step 2: Check Maven and Java Versions```

**First verify your environment in the same PowerShell terminal:**Apache Maven 3.9.11

Java version: 17.0.12, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-17.0.12

```powershell```

# Check Maven version

mvn --version### Step 4: Verify Application Started Successfully



# Check Java version**Look for these log messages:**

java -version```

```Started EppIntegrationApplication in X.XXX seconds

Tomcat started on port(s): 8080 (http)

**Expected Output:**```

```

Apache Maven 3.9.11**Test the health endpoint:**

Java version: 17.0.12, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-17.0.12```

```GET http://localhost:8080/payments/epp/ping

```

### Step 3: Configure Environment (REQUIRED for every new session)**Expected Response:** `pong`

**Run these commands in the SAME PowerShell terminal (do not open a new terminal):**

---

```powershell

# Set Java 17 environment## Complete Command Sequence (Copy & Paste)

$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"

For convenience, here's the complete startup sequence in one block:

# Set Spring profile for H2 testing

$env:SPRING_PROFILES_ACTIVE="test"```powershell

# Navigate to project directory

# Clean build and start the applicationcd "c:\Dev Resources\Workspace\EPPTest"

mvn clean spring-boot:run

```# Set Java 17 environment

$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"

### Step 4: Wait for Application Startup$env:Path="$env:JAVA_HOME\bin;" + $env:Path

**Watch for these success messages:**

# Verify Java version

```java -version

  .   ____          _            __ _ _

 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \# Set Spring profile for H2 testing

( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \set SPRING_PROFILES_ACTIVE=test

 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )

  '  |____| .__|_| |_|_| |_\__, | / / / /# Clean build and run

 =========|_|==============|___/=/_/_/_/mvn clean spring-boot:run

 :: Spring Boot ::                (v3.2.5)```



2025-09-23T15:XX:XX.XXX-04:00  INFO 17212 --- [rucApi] [           main] c.r.payments.EppIntegrationApplication   : Started EppIntegrationApplication in X.XXX seconds (process running for X.XXX)---

```

## Testing the API

**Look for debugging initialization messages:**

```### Using Browser

🏗️ TestUIController CONSTRUCTOR called!- Health Check: `http://localhost:8080/payments/epp/ping`

✅ TestUIController initialized with RestTemplate and ObjectMapper

```### Using Postman (✅ UPDATED WITH FIXED PAYLOADS)



### Step 5: Verify Application is Running1. **Health Check**:

   - **GET** `http://localhost:8080/payments/epp/ping`

**Test the health endpoints:**   - **Expected**: `pong`



1. **EPP Service Health Check:**2. **Start EPP Payment** (Fixed validation):

   ```   - **POST** `http://localhost:8080/payments/epp/start`

   GET http://localhost:8080/payments/epp/ping   - **Headers**: `Content-Type: application/json`

   Expected Response: "pong"   - **Body**:

   ```     ```json

     {

2. **UI Controller Health Check:**       "saleDetailId": 1,

   ```       "applicationCode": "3256d54a-9e63-4c7d-b2f9-a2897ec82aab",

   GET http://localhost:8080/test/ping       "orderKey": "TEST-ORDER-12345",

   Expected Response: "UI Controller is working!"       "firstName": "John",

   ```       "lastName": "Doe",

       "address1": "123 Main Street",

3. **Test Form UI:**       "address2": "Apt 4B",

   ```       "city": "Philadelphia",

   http://localhost:8080/test       "stateCode": "PA",

   Expected: Pre-populated test form with EPP integration       "zipCode": "19101",

   ```       "totalAmount": 150.75,

       "applicationUniqueId": "UNIQUE-123456",

## Testing the Complete Flow       "paymentAccountType": "CC",

       "email": "john.doe@example.com",

### Step 1: Open Test Form       "items": [

Navigate to: `http://localhost:8080/test`         {

           "saleItemId": 1,

**You should see:**           "count": 2,

- Pre-populated form with test data           "description": "RUC Registration Fee",

- Application Code: `3256d54a-9e63-4c7d-b2f9-a2897ec82aab`           "amount": 75.375,

- Test ping buttons           "itemKey": "REG-FEE-001"

- Error display area         }

       ]

### Step 2: Test Connectivity     }

Click the built-in test buttons:     ```

- "Test Ping" - Tests EPP service connectivity

- "Test UI Controller" - Tests form controller3. **EPP Callback (OnEPPResult)**:

   - **POST** `http://localhost:8080/payments/epp/OnEPPResult`

### Step 3: Submit Payment Form   - **Headers**: `Content-Type: application/json`

1. **Review Form Data** - Modify if needed   - **Body**:

2. **Click "Submit Payment & Go to EPP"**     ```json

3. **Watch Console Logs** for detailed debugging:     {

       "orderKey": "TEST-ORDER-12345",

```       "applicationUniqueId": "UNIQUE-123456",

🚀 SUBMIT ENDPOINT HIT! /test/submit       "applicationCode": "3256d54a-9e63-4c7d-b2f9-a2897ec82aab",

✅ Form Data Received:       "status": "COM",

  - Application Code: 3256d54a-9e63-4c7d-b2f9-a2897ec82aab       "cardHolderName": "John Doe",

  - Order Key: TEST-ORDER-[timestamp]       "address": "123 Main Street",

  [... all form fields logged ...]       "city": "Philadelphia",

       "stateCode": "PA",

🔄 Building SaleDetails object...       "zipCode": "19101",

💰 Total Amount parsed: 100.00       "totalAmount": 150.75,

📦 Sale Items created: 1 items       "emailId": "john.doe@example.com",

       "referenceNumber": "AUTH123456",

🌐 Preparing HTTP call to /payments/epp/start...       "paymentAccountType": "Visa"

📤 JSON Request Body: [complete JSON payload]     }

🔗 Calling URL: http://localhost:8080/payments/epp/start     ```



⏳ Making HTTP POST request...### Using curl

✅ HTTP Response received!```bash

📊 Response Status: 200 OK# Health check

📄 EPP Form HTML length: [size]curl http://localhost:8080/payments/epp/ping

🎯 EPP Form Preview: <html><body onload='document.forms[0].submit()'>...

```# Start payment

curl -X POST http://localhost:8080/payments/epp/start \

### Step 4: EPP Redirect  -H "Content-Type: application/json" \

**Expected Result:** Browser should automatically redirect to:  -d '{

```    "orderKey": "25092230001000007",

https://epp.beta.pa.gov/Payment/Index    "applicationUniqueId": "3256d54a-9e63-4c7d-b2f9-a2897ec82aab",

```    "amount": 25.00,

    "email": "driver@example.com",

With your payment data pre-filled in the EPP Commerce Hub.    "items": [

      { "itemCode": "RUC_FEE", "description": "Road User Charge", "amount": 25.00 }

## Troubleshooting    ]

  }'

### Issue: Application Won't Start```

**Solution:** 

1. Stop any running Java processes: `Get-Process -Name "java" | Stop-Process -Force`### ✅ Commerce Hub Testing (Validation Fix Applied)

2. Clean rebuild: `mvn clean compile`

3. Restart with environment variables**Commerce Hub Compliant Payload** (Copy & Paste Ready):



### Issue: Form Submission Fails```json

**Check Console for:**POST http://localhost:8080/payments/epp/start

- ❌ No "🚀 SUBMIT ENDPOINT HIT!" message → Form not reaching controllerContent-Type: application/json

- ❌ Parameter resolution errors → Maven compiler issue (should be fixed)

- ❌ HTTP call failures → Check `/payments/epp/start` endpoint{

  "saleDetailId": null,

### Issue: EPP Redirect Not Working  "orderKey": "CH-ORDER-{{$timestamp}}",

**Verify:**  "applicationCode": "3256d54a-9e63-4c7d-b2f9-a2897ec82aab",

- HTTP response contains HTML with `<form method='POST' action='https://epp.beta.pa.gov/Payment/Index'>`  "firstName": "Commerce",

- Browser auto-submit is enabled  "lastName": "User",

- No JavaScript errors in browser console  "address1": "123 Commerce Street",

  "city": "Philadelphia",

## Stopping the Application  "stateCode": "PA",

  "zipCode": "19101",

To stop the application:  "totalAmount": 25.00,

1. **In the running terminal:** Press `Ctrl+C`  "applicationUniqueId": null,

2. **Or force stop:** `Get-Process -Name "java" | Stop-Process -Force`  "email": "commerce@example.com",

  "items": [

## Key URLs for Testing    {

      "saleItemId": null,

- **Health Check:** http://localhost:8080/payments/epp/ping      "count": 1,

- **UI Health Check:** http://localhost:8080/test/ping      "description": "RUC Road User Charge",

- **Test Form:** http://localhost:8080/test      "amount": 25.00,

- **Direct API:** POST http://localhost:8080/payments/epp/start      "itemKey": ""

- **Debug JSON:** POST http://localhost:8080/payments/epp/debug-json    }

  ]

---}

```

**✅ SUCCESS CRITERIA:**

- Application starts without errors**Expected Result:**

- Health checks return expected responses- ✅ **No "Application unique ID is required" error** (validation fix applied)

- Test form loads with pre-populated data- ✅ **HTML EPP form generated** with auto-populated fields

- Form submission shows detailed logs- ✅ **itemKey automatically set** to orderKey value

- EPP redirect works to Pennsylvania EPP portal

**Commerce Hub Validation Test Results:**

**⏱️ Total startup time:** 2-3 minutes including verification- `applicationUniqueId: null` → ✅ **ACCEPTED** (no validation error)

- `saleDetailId: null` → ✅ **ACCEPTED** (nullable Integer)

**🎯 Next Steps:** Use the test form to verify EPP integration, then integrate the API endpoints into your existing application.- `saleItemId: null` → ✅ **ACCEPTED** (nullable Integer)
- `itemKey: ""` → ✅ **AUTO-POPULATED** with orderKey

---

## Troubleshooting

### Issue: Java Version Error
**Symptom:**
```
Error: org.springframework.boot.maven.RunMojo has been compiled by a more recent version
```
**Solution:**
1. Ensure Java 17 is set: `$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"`
2. Verify with: `java -version`

### Issue: Oracle Dialect Error in Test Mode
**Symptom:**
```
ClassNotFoundException: org.hibernate.dialect.Oracle12cDialect
```
**Solution:**
1. Ensure test profile is set: `set SPRING_PROFILES_ACTIVE=test`
2. Clean and rebuild: `mvn clean spring-boot:run`

### Issue: Port Already in Use
**Symptom:**
```
Port 8080 was already in use
```
**Solution:**
1. Stop existing process using port 8080
2. Or change port: `set SERVER_PORT=8081`

### Issue: Flyway Migration Conflicts
**Symptom:**
```
Found more than one migration with version 1
```
**Solution:**
Project already configured correctly. If this occurs, ensure only H2 migrations exist in `db/migration/h2/`.

---

## Next Time You Start the API

**Remember these key steps:**
1. Set Java 17 environment variables (required every PowerShell session)
2. Set Spring profile to `test`
3. Run `mvn clean spring-boot:run`
4. Test with health endpoint

**Bookmark this command sequence:**
```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"; $env:Path="$env:JAVA_HOME\bin;" + $env:Path; set SPRING_PROFILES_ACTIVE=test; mvn clean spring-boot:run
```

---

## Project Structure Summary

### Source Code Organization
- **Controllers**: `src/main/java/com/ruk/payments/controller/` - REST API endpoints
- **Services**: `src/main/java/com/ruk/payments/service/` - Business logic layer
  - **Interfaces**: Service contracts and abstractions
  - **Implementations**: Concrete service implementations with full business logic
- **Repositories**: `src/main/java/com/ruk/payments/repo/` - Data access layer
- **DTOs**: `src/main/java/com/ruk/payments/dto/` - Data transfer objects
- **Entities**: `src/main/java/com/ruk/payments/entity/` - JPA entities
- **Utilities**: `src/main/java/com/ruk/payments/util/` - Helper and utility classes
- **Configuration**: `src/main/java/com/ruk/payments/config/` - Spring configuration
- **Exceptions**: `src/main/java/com/ruk/payments/exception/` - Custom exceptions

### Configuration Files
- **Main Config**: `src/main/resources/application.yml`
- **Test Config**: `src/main/resources/application-test.yml`
- **H2 Migrations**: `src/main/resources/db/migration/h2/`

### Documentation & Tools
- **Postman Collection**: `postman/RUC_EPP.postman_collection.json`
- **Integration Guide**: `INTEGRATION_GUIDE.md` - Complete integration documentation
- **API Reference**: `API_REFERENCE.md` - Quick reference guide

### Architecture Highlights
- **Clean Separation**: Controllers handle HTTP, Services handle business logic
- **Dependency Injection**: All components properly wired through Spring
- **Exception Handling**: Custom exceptions with proper error codes
- **Validation**: Input validation at multiple layers
- **Logging**: Comprehensive logging throughout all layers
- **Testing**: Unit and integration tests for all components

---

## Success Indicators

✅ **Application Started Successfully When You See:**
- No error messages in console
- `Started EppIntegrationApplication` message
- `Tomcat started on port(s): 8080` message
- Health endpoint returns `pong`

❌ **Something Wrong If You See:**
- Java version errors
- Oracle dialect errors
- Flyway migration conflicts
- Port binding errors


<!-- 
  Let me stop any running processes first:

  - Get-Process -Name "java" -ErrorAction SilentlyContinue | Stop-Process -Force
  - mvn clean compile
  
  Let me try a direct approach and run the application:
  - $env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12";
  - $env:SPRING_PROFILES_ACTIVE="test"; 
  - mvn spring-boot:run 
  
  -->