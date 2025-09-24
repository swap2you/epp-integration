# Postman Collection for EPP Integration - /start Endpoint

## Endpoint Details
- **Method**: POST
- **URL**: `http://localhost:8080/payments/epp/start`
- **Content-Type**: `application/json` OR `text/html; charset=UTF-8`
- **Accept**: `text/html`

## Headers
```
Content-Type: application/json
Accept: text/html
```

**Alternative Headers (Rahul's requirement):**
```
Content-Type: text/html; charset=UTF-8
Accept: text/html
```

## Request Body (JSON)
```json
{
  "SaleDetailId": 0,
  "ApplicationCode": "3256d54a-9e63-4c7d-b2f9-a2897ec82aab",
  "OrderKey": "TEST-ORDER-1758661854234",
  "FirstName": "John",
  "LastName": "Smith",
  "Address1": "400 Market Street",
  "Address2": "test",
  "City": "Harrisburg",
  "StateCode": "PA",
  "ZipCode": "17111",
  "TotalAmount": 75.00,
  "Items": [
    {
      "SaleItemId": 0,
      "Count": 1,
      "Description": "RUC Payment Test",
      "Amount": 75.00,
      "ItemKey": "TEST-ORDER-1758661854234"
    }
  ],
  "ApplicationUniqueId": null,
  "PaymentAccountType": "CC",
  "EmailId": "testuser@gmail.com"
}
```

## Expected Response
- **Status Code**: 200 OK
- **Content-Type**: text/html
- **Body**: HTML form that auto-redirects to Pennsylvania EPP

### Sample Response Body:
```html
<html><body onload='document.forms[0].submit()'>
<form method='POST' action='https://epp.beta.pa.gov/'>
<input type='hidden' name='payload' value='"{\"SaleDetailId\":0,\"ApplicationCode\":\"3256d54a-9e63-4c7d-b2f9-a2897ec82aab\",\"OrderKey\":\"TEST-ORDER-1758661854234\",\"FirstName\":\"John\",\"LastName\":\"Smith\",\"Address1\":\"400 Market Street\",\"Address2\":\"test\",\"City\":\"Harrisburg\",\"StateCode\":\"PA\",\"ZipCode\":\"17111\",\"TotalAmount\":75.00,\"Items\":[{\"SaleItemId\":0,\"Count\":1,\"Description\":\"RUC Payment Test\",\"Amount\":75.00,\"ItemKey\":\"TEST-ORDER-1758661854234\"}],\"ApplicationUniqueId\":null,\"PaymentAccountType\":\"CC\",\"EmailId\":\"testuser@gmail.com\"}"'/>
</form></body></html>
```

## Test Scenarios

### 1. Basic Valid Request
- Use the JSON payload above
- Should return HTML form
- Status: 200 OK

### 2. With Rahul's Content-Type
- Same JSON payload
- Headers: `Content-Type: text/html; charset=UTF-8`
- Should work with custom message converter
- Status: 200 OK

### 3. Invalid ApplicationCode
- Change ApplicationCode to invalid format
- Should return validation error
- Status: 400 Bad Request

### 4. Missing Required Fields
- Remove required fields like OrderKey
- Should return validation error
- Status: 400 Bad Request

## Postman Collection JSON

```json
{
  "info": {
    "name": "EPP Integration API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "EPP Start Payment - JSON",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          },
          {
            "key": "Accept",
            "value": "text/html"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"SaleDetailId\": 0,\n  \"ApplicationCode\": \"3256d54a-9e63-4c7d-b2f9-a2897ec82aab\",\n  \"OrderKey\": \"TEST-ORDER-{{$timestamp}}\",\n  \"FirstName\": \"John\",\n  \"LastName\": \"Smith\",\n  \"Address1\": \"400 Market Street\",\n  \"Address2\": \"test\",\n  \"City\": \"Harrisburg\",\n  \"StateCode\": \"PA\",\n  \"ZipCode\": \"17111\",\n  \"TotalAmount\": 75.00,\n  \"Items\": [\n    {\n      \"SaleItemId\": 0,\n      \"Count\": 1,\n      \"Description\": \"RUC Payment Test\",\n      \"Amount\": 75.00,\n      \"ItemKey\": \"TEST-ORDER-{{$timestamp}}\"\n    }\n  ],\n  \"ApplicationUniqueId\": null,\n  \"PaymentAccountType\": \"CC\",\n  \"EmailId\": \"testuser@gmail.com\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/payments/epp/start",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["payments", "epp", "start"]
        }
      }
    },
    {
      "name": "EPP Start Payment - HTML Content Type",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "text/html; charset=UTF-8"
          },
          {
            "key": "Accept",
            "value": "text/html"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"SaleDetailId\": 0,\n  \"ApplicationCode\": \"3256d54a-9e63-4c7d-b2f9-a2897ec82aab\",\n  \"OrderKey\": \"TEST-ORDER-{{$timestamp}}\",\n  \"FirstName\": \"John\",\n  \"LastName\": \"Smith\",\n  \"Address1\": \"400 Market Street\",\n  \"Address2\": \"test\",\n  \"City\": \"Harrisburg\",\n  \"StateCode\": \"PA\",\n  \"ZipCode\": \"17111\",\n  \"TotalAmount\": 75.00,\n  \"Items\": [\n    {\n      \"SaleItemId\": 0,\n      \"Count\": 1,\n      \"Description\": \"RUC Payment Test\",\n      \"Amount\": 75.00,\n      \"ItemKey\": \"TEST-ORDER-{{$timestamp}}\"\n    }\n  ],\n  \"ApplicationUniqueId\": null,\n  \"PaymentAccountType\": \"CC\",\n  \"EmailId\": \"testuser@gmail.com\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/payments/epp/start",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["payments", "epp", "start"]
        }
      }
    },
    {
      "name": "EPP Ping - Health Check",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/payments/epp/ping",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["payments", "epp", "ping"]
        }
      }
    }
  ]
}
```

## Quick Setup Instructions

1. **Import Collection**: Copy the JSON above and import into Postman
2. **Set Environment**: Create environment with `baseUrl = http://localhost:8080`
3. **Start Server**: Make sure your Spring Boot app is running
4. **Test Health**: First test `/ping` endpoint
5. **Test Payment**: Use either content type variant

## Common Issues & Solutions

### 415 Unsupported Media Type
- **Problem**: Content-Type not supported
- **Solution**: Use custom message converter (implemented)
- **Test**: Try both `application/json` and `text/html; charset=UTF-8`

### 400 Validation Error
- **Problem**: Required fields missing or invalid
- **Solution**: Check ApplicationCode format (UUID), required fields
- **Fields**: ApplicationCode, OrderKey, FirstName, LastName, etc.

### 500 Internal Server Error
- **Problem**: Server configuration or database issue
- **Solution**: Check server logs, database connection

## Notes
- OrderKey should be unique for each request
- Use `{{$timestamp}}` in Postman for dynamic OrderKey generation
- ApplicationCode must be valid UUID format
- TotalAmount should match sum of Items amounts