# API Reference

Quick reference for the RUC EPP Integration API endpoints and data formats.

## � Quick Start

```powershell
# Set Java 17 and run
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"
$env:SPRING_PROFILES_ACTIVE="test"
mvn clean spring-boot:run
```

## API Endpoints

### Health Check Endpoints
```
GET http://localhost:8080/payments/epp/ping
Response: "pong"

GET http://localhost:8080/test/ping
Response: "UI Controller is working!"
```

### Test Form UI (NEW)
```
GET http://localhost:8080/test
- Pre-populated test form for immediate EPP testing
- Built-in ping test buttons
- Error display and debugging information
- Auto-submit functionality to EPP portal
```

### Debug JSON Endpoint
```
POST http://localhost:8080/payments/epp/debug-json
Content-Type: application/json
- Returns the JSON that would be sent to EPP
- Useful for payload validation without triggering EPP
```

### Start Payment - Commerce Hub Compliant (✅ CURRENT)
```
POST http://localhost:8080/payments/epp/start
Content-Type: application/json

{
  "saleDetailId": null,
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
  "applicationUniqueId": null,
  "paymentAccountType": "CC",
  "email": "john.doe@example.com",
  "items": [
    {
      "saleItemId": null,
      "count": 2,
      "description": "RUC Registration Fee",
      "amount": 75.375,
      "itemKey": ""
    }
  ]
}

Response: HTML form (auto-posts to EPP)
Notes: 
- itemKey will be automatically set to orderKey value by service layer
- applicationUniqueId can be null (Commerce Hub compliant)
- No validation errors for null applicationUniqueId (validation fix applied)
```

### Start Payment - Legacy Format (Backward Compatible)
```
POST http://localhost:8080/payments/epp/start
Content-Type: application/json

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

Response: HTML form (auto-posts to EPP)
```

### EPP Callback (OnEPPResult) - Primary Endpoint
```
POST http://localhost:8080/payments/epp/OnEPPResult
Content-Type: application/json

{
  "orderKey": "TEST-ORDER-12345",
  "applicationUniqueId": null,
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

Response: ApplicationResponse JSON
Notes: 
- applicationUniqueId is optional and can be null per Commerce Hub requirements
- No validation errors for null applicationUniqueId in callbacks (validation fix applied)
```

### Legacy Callback (Backward Compatibility)
```
POST http://localhost:8080/payments/epp/result
Content-Type: application/json

Body: Same as OnEPPResult above
Response: ApplicationResponse JSON
```

## Payment Statuses
- `APP` - Approved
- `COM` - Complete
- `CAN` - Cancelled
- `DEC` - Declined
- `PEN` - Pending
- `SEN` - Sent

## Debugging & Logging Features

### Comprehensive Request Tracing
When using the test form (`/test`), you'll see detailed logs:

```
🚀 SUBMIT ENDPOINT HIT! /test/submit
✅ Form Data Received:
  - Application Code: 3256d54a-9e63-4c7d-b2f9-a2897ec82aab
  - Order Key: [value]
  - First Name: [value]
  [... all form fields logged ...]

🔄 Building SaleDetails object...
💰 Total Amount parsed: [amount]
📦 Sale Items created: 1 items

🌐 Preparing HTTP call to /payments/epp/start...
📤 JSON Request Body: [complete JSON payload]
🔗 Calling URL: http://localhost:8080/payments/epp/start

⏳ Making HTTP POST request...
✅ HTTP Response received!
📊 Response Status: 200 OK
📄 EPP Form HTML length: [size]
🎯 EPP Form Preview: <html><body onload='document.forms[0].submit()'>[...]
```

### Error Diagnosis
- **Parameter Resolution Errors**: Fixed with Maven `-parameters` flag
- **Form Submission Issues**: Detailed endpoint hit logging
- **HTTP Call Tracing**: Complete request/response logging
- **EPP Integration**: Auto-submit form preview and debugging

### Quick Testing
- **Test Form**: `http://localhost:8080/test` - Immediate EPP testing
- **Ping Tests**: Built-in connectivity testing buttons
- **Error Display**: Real-time error messages in UI

## Configuration Profiles
- **test**: H2 in-memory database (for development)
- **local**: Oracle 12c database (for local dev with Oracle)
- **prod**: Production Oracle configuration

## Database
- **Test**: H2 in-memory (`jdbc:h2:mem:ruc`)
- **Tables**: `epp_transaction`, `epp_feature_flag`
- **Migrations**: Flyway auto-applies on startup

## Troubleshooting Quick Fixes
1. **Java Error**: Set `$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"`
2. **Oracle Dialect Error**: Ensure `SPRING_PROFILES_ACTIVE=test`
3. **Port Error**: Stop existing process or set `SERVER_PORT=8081`
4. **Build Error**: Run `mvn clean` first

## Tools
- **Postman**: Import `postman/RUC_EPP.postman_collection.json`
- **Logs**: Check console output for startup messages
- **Health**: Always test `/ping` endpoint first
