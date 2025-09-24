# EPP Integration Guide

A comprehensive guide for integrating with the RUC EPP Payment API.

## 🎯 Quick Integration

### 1. Add Dependencies

Add these to your existing Spring Boot `pom.xml`:

```xml
<dependencies>
    <!-- Core Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- For testing (H2) -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- For production (Oracle) -->
    <dependency>
        <groupId>com.oracle.database.jdbc</groupId>
        <artifactId>ojdbc11</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

### 1.1. CRITICAL: Add Maven Compiler Plugin

**⚠️ Required Fix:** Add this plugin to avoid parameter resolution errors:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version>
            <configuration>
                <source>17</source>
                <target>17</target>
                <parameters>true</parameters>  <!-- CRITICAL: Preserves parameter names -->
            </configuration>
        </plugin>
        <!-- Other plugins... -->
    </plugins>
</build>
```

**Why This is Needed:** Without the `-parameters` flag, Spring Boot cannot resolve `@RequestParam` parameter names, causing:
```
java.lang.IllegalArgumentException: Name for argument of type [java.lang.String] not specified
```

### 2. Copy Source Files

Copy these packages from this project to yours:

```
src/main/java/com/ruk/payments/
├── controller/PaymentController.java    # REST endpoints
├── service/PaymentService.java          # Business logic
├── service/EppClient.java               # EPP integration
├── dto/SaleDetails.java                 # Request DTOs
├── dto/SaleItems.java
├── dto/EppResponse.java                 # Response DTOs
├── entity/EppTransaction.java           # Database entity
├── repo/EppTransactionRepository.java   # Data access
├── config/EppProperties.java            # Configuration
└── util/CommonUtils.java                # Utilities
```

### 3. Update Configuration

Add to your `application.yml`:

```yaml
ruc:
  payments:
    provider: epp
    applicationCode: "3256d54a-9e63-4c7d-b2f9-a2897ec82aab"
    paymentGatewayUrl: "https://epp.beta.pa.gov/Payment/Create"
    returnUrl: "https://your-domain.com/payments/epp/result"
    merchantId: "235188073995"
    environment: "UAT"

spring:
  profiles:
    active: test
  datasource:
    # H2 for testing
    url: jdbc:h2:mem:ruc
    username: sa
    password: 
    driver-class-name: org.h2.Driver
    
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    
  flyway:
    enabled: true
    locations: classpath:db/migration/h2
```

### 4. Database Migration

Copy the database migration file:
- `src/main/resources/db/migration/h2/V1__epp_schema.sql`

### 5. Test Integration

```bash
# Start your application
mvn spring-boot:run

# Test health endpoint
curl http://localhost:8080/payments/epp/ping

# Test payment initiation
curl -X POST http://localhost:8080/payments/epp/start \
  -H "Content-Type: application/json" \
  -d '{
    "applicationCode": "3256d54a-9e63-4c7d-b2f9-a2897ec82aab",
    "orderKey": "TEST-12345",
    "firstName": "John",
    "lastName": "Smith",
    "address1": "123 Main St",
    "city": "Harrisburg",
    "stateCode": "PA",
    "zipCode": "17111",
    "email": "test@example.com",
    "totalAmount": 100.00,
    "items": [{
      "count": 1,
      "description": "RUC Fee",
      "amount": 100.00,
      "itemKey": "TEST-12345"
    }]
  }'
```

## 🔌 API Integration Patterns

### Pattern 1: Direct Integration

```java
@RestController
public class YourPaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/your-payment-endpoint")
    public ResponseEntity<String> processPayment(@RequestBody YourPaymentRequest request) {
        // Convert your request to SaleDetails
        SaleDetails saleDetails = convertToSaleDetails(request);
        
        // Process through EPP
        String eppForm = paymentService.startPayment(saleDetails);
        
        return ResponseEntity.ok(eppForm);
    }
}
```

### Pattern 2: Service Layer Integration

```java
@Service
public class YourPaymentService {
    
    @Autowired
    private PaymentService eppPaymentService;
    
    public PaymentResult processPayment(CustomerOrder order) {
        // Your business logic
        validateOrder(order);
        
        // Convert to EPP format
        SaleDetails saleDetails = mapToEppFormat(order);
        
        // Process through EPP
        String eppForm = eppPaymentService.startPayment(saleDetails);
        
        // Handle response
        return new PaymentResult(eppForm, order.getId());
    }
}
```

## 🛠️ Data Mapping

### Convert Your Data to EPP Format

```java
public SaleDetails convertToEppFormat(YourOrderData order) {
    SaleDetails saleDetails = new SaleDetails();
    
    // Required fields
    saleDetails.setApplicationCode("3256d54a-9e63-4c7d-b2f9-a2897ec82aab");
    saleDetails.setOrderKey(order.getOrderId());
    saleDetails.setFirstName(order.getCustomer().getFirstName());
    saleDetails.setLastName(order.getCustomer().getLastName());
    saleDetails.setAddress1(order.getBillingAddress().getStreet());
    saleDetails.setCity(order.getBillingAddress().getCity());
    saleDetails.setStateCode(order.getBillingAddress().getState());
    saleDetails.setZipCode(order.getBillingAddress().getZip());
    saleDetails.setEmail(order.getCustomer().getEmail());
    saleDetails.setTotalAmount(order.getTotalAmount());
    
    // Map line items
    List<SaleItems> items = order.getLineItems().stream()
        .map(this::mapToSaleItem)
        .collect(Collectors.toList());
    saleDetails.setItems(items);
    
    return saleDetails;
}

private SaleItems mapToSaleItem(YourLineItem lineItem) {
    SaleItems item = new SaleItems();
    item.setCount(lineItem.getQuantity());
    item.setDescription(lineItem.getDescription());
    item.setAmount(lineItem.getAmount());
    item.setItemKey(lineItem.getSku());
    return item;
}
```

## 🔄 Handling EPP Callbacks

EPP will send callbacks to your configured `returnUrl`. Set up an endpoint to handle them:

```java
@PostMapping("/payments/epp/result")
public ResponseEntity<ApplicationResponse> handleEppCallback(
    @RequestParam Map<String, String> params,
    HttpServletRequest request) {
    
    // Let the EPP service handle the callback
    ApplicationResponse response = paymentService.handleCallback(params, request);
    
    // Add your business logic here
    if (response.isSuccess()) {
        // Update your order status
        updateOrderStatus(params.get("orderKey"), "PAID");
        
        // Send confirmation email
        sendConfirmationEmail(params.get("orderKey"));
    }
    
    return ResponseEntity.ok(response);
}
```

## 🎯 Production Configuration

### Oracle Database Setup

```yaml
# application-prod.yml
spring:
  profiles:
    active: local
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:XE
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: oracle.jdbc.OracleDriver
    
  flyway:
    locations: classpath:db/migration/oracle

ruc:
  payments:
    paymentGatewayUrl: "https://epp.pa.gov/Payment/Create"  # Production URL
    returnUrl: "https://your-production-domain.com/payments/epp/result"
    environment: "PROD"
```

### Environment Variables

```bash
# Production environment variables
export DB_USERNAME=your_db_user
export DB_PASSWORD=your_db_password
export EPP_APPLICATION_CODE=3256d54a-9e63-4c7d-b2f9-a2897ec82aab
export EPP_MERCHANT_ID=235188073995
export EPP_RETURN_URL=https://your-domain.com/payments/epp/result
```

## 🧪 Testing Your Integration

### 1. Unit Tests

```java
@SpringBootTest
@TestPropertySource(properties = {
    "spring.profiles.active=test"
})
class PaymentIntegrationTest {
    
    @Autowired
    private PaymentService paymentService;
    
    @Test
    void testPaymentInitiation() {
        SaleDetails saleDetails = createTestSaleDetails();
        String eppForm = paymentService.startPayment(saleDetails);
        
        assertThat(eppForm).contains("https://epp.beta.pa.gov");
        assertThat(eppForm).contains("3256d54a-9e63-4c7d-b2f9-a2897ec82aab");
    }
}
```

### 2. Integration Testing

Use the provided Postman collection in the `postman/` directory for API testing.

### 3. Test Data

```json
{
  "applicationCode": "3256d54a-9e63-4c7d-b2f9-a2897ec82aab",
  "orderKey": "TEST-ORDER-12345",
  "firstName": "John",
  "lastName": "Smith",
  "address1": "400 Market Street",
  "city": "Harrisburg",
  "stateCode": "PA",
  "zipCode": "17111",
  "email": "john.smith@example.com",
  "totalAmount": 100.00,
  "items": [
    {
      "count": 1,
      "description": "RUC Registration Fee",
      "amount": 100.00,
      "itemKey": "TEST-ORDER-12345"
    }
  ]
}
```

## 🚨 Common Issues & Solutions

### ⚠️ CRITICAL: Parameter Resolution Error
**Error:** `java.lang.IllegalArgumentException: Name for argument of type [java.lang.String] not specified`

**Root Cause:** Maven compiler not preserving parameter names for Spring `@RequestParam` annotations.

**Solution:** Add Maven compiler plugin with `-parameters` flag (see section 1.1 above).

**Alternative Solution:** Use explicit parameter names:
```java
@PostMapping("/submit")
public String submitTest(
    @RequestParam("applicationCode") String applicationCode,  // Explicit names
    @RequestParam("orderKey") String orderKey,
    // ... etc
```

### Issue: Form Submission Not Working
**Symptoms:** Form submits but endpoint not hit, no logs visible

**Debugging Steps:**
1. **Check Console Logs** - Look for these messages:
   ```
   🚀 SUBMIT ENDPOINT HIT! /test/submit
   ✅ Form Data Received: [parameters]
   ```

2. **Verify Form Action** - Ensure form points to correct endpoint:
   ```html
   <form action="/test/submit" method="post">
   ```

3. **Test Ping Endpoints** - Use built-in test buttons on form page

### Issue: "Application unique ID is required"
**Solution:** This validation has been removed. Use `"applicationUniqueId": null` in your payload.

### Issue: "class file has wrong version"
**Solution:** Ensure you're using Java 17:
```bash
export JAVA_HOME=/path/to/jdk-17
java -version  # Should show Java 17
```

### Issue: EPP form not generating
**Solution:** Check that:
- Total amount is > 0
- All required fields are populated
- Application is running on correct profile
- Check detailed logs for HTTP call trace

### Issue: Database connection fails
**Solution:** Verify database settings and use H2 for testing:
```yaml
spring:
  profiles:
    active: test  # Uses H2 in-memory database
```

## 📞 Support & Debugging

### Quick Health Checks
- **EPP Service Health:** `GET /payments/epp/ping` → Should return `pong`
- **UI Controller Health:** `GET /test/ping` → Should return `UI Controller is working!`
- **Test Form UI:** `http://localhost:8080/test` → Pre-populated test form

### Debugging Features
The application includes comprehensive logging for troubleshooting:

**Controller Initialization:**
```
🏗️ TestUIController CONSTRUCTOR called!
✅ TestUIController initialized with RestTemplate and ObjectMapper
```

**Form Submission Debugging:**
```
🚀 SUBMIT ENDPOINT HIT! /test/submit
✅ Form Data Received: [all form parameters logged]
🔄 Building SaleDetails object...
🌐 Preparing HTTP call to /payments/epp/start...
📤 JSON Request Body: [complete JSON payload]
✅ HTTP Response received!
📄 EPP Form HTML length: [response size]
```

### Development Tools
- **H2 Console:** `http://localhost:8080/h2-console` (test profile only)
- **Application Logs:** Watch console for detailed request/response tracing
- **Built-in Test Form:** Use `/test` page for immediate testing

---

This integration should take 30-60 minutes to implement. Focus on getting the basic flow working first, then add your business logic around it.