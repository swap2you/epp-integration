# RUC EPP Integration - Troubleshooting Guide

Comprehensive troubleshooting guide for common issues with the RUC EPP Integration API.

## 🚨 CRITICAL ISSUES (FIXED)

### ❌ Parameter Resolution Error (RESOLVED)

**Error Message:**
```
java.lang.IllegalArgumentException: Name for argument of type [java.lang.String] not specified, and parameter name information not available via reflection. Ensure that the compiler uses the '-parameters' flag.
```

**Root Cause:** Maven compiler not preserving parameter names for Spring `@RequestParam` annotations.

**✅ SOLUTION APPLIED:**

1. **Maven Compiler Plugin Added** (in `pom.xml`):
   ```xml
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-compiler-plugin</artifactId>
       <version>3.13.0</version>
       <configuration>
           <source>17</source>
           <target>17</target>
           <parameters>true</parameters>  <!-- CRITICAL FIX -->
       </configuration>
   </plugin>
   ```

2. **Explicit Parameter Names** (in `TestUIController.java`):
   ```java
   @PostMapping("/submit")
   public String submitTest(
       @RequestParam("applicationCode") String applicationCode,  // Explicit names
       @RequestParam("orderKey") String orderKey,
       // ... etc
   ```

**Status:** ✅ RESOLVED - This error should no longer occur.

---

## 🔍 DEBUGGING WORKFLOW

### Step 1: Check Application Startup

**Look for these success messages:**

```
🏗️ TestUIController CONSTRUCTOR called!
✅ TestUIController initialized with RestTemplate and ObjectMapper
```

**If missing:**
- Controller not loading → Check Spring Boot configuration
- Maven build issues → Run `mvn clean compile`

### Step 2: Test Health Endpoints

**EPP Service:**
```bash
curl http://localhost:8080/payments/epp/ping
# Expected: "pong"
```

**UI Controller:**
```bash
curl http://localhost:8080/test/ping
# Expected: "UI Controller is working!"
```

**If endpoints fail:**
- Check application is running on port 8080
- Verify no other applications using the port
- Check firewall/antivirus blocking the port

### Step 3: Test Form Submission

**Navigate to:** `http://localhost:8080/test`

**Expected logs on form submission:**
```
🚀 SUBMIT ENDPOINT HIT! /test/submit
✅ Form Data Received:
  - Application Code: 3256d54a-9e63-4c7d-b2f9-a2897ec82aab
  - Order Key: TEST-ORDER-[timestamp]
  [... all form parameters ...]

🔄 Building SaleDetails object...
💰 Total Amount parsed: 100.00
📦 Sale Items created: 1 items

🌐 Preparing HTTP call to /payments/epp/start...
📤 JSON Request Body: [complete JSON]
🔗 Calling URL: http://localhost:8080/payments/epp/start

⏳ Making HTTP POST request...
✅ HTTP Response received!
📊 Response Status: 200 OK
📄 EPP Form HTML length: [size]
🎯 EPP Form Preview: <html><body onload='document.forms[0].submit()'>...
```

---

## 🐛 COMMON ISSUES & SOLUTIONS

### Issue: Form Submission Does Nothing

**Symptoms:**
- Form submits but no redirect happens
- No console logs appear
- Page stays on test form

**Debugging Steps:**

1. **Check Browser Developer Console:**
   - Open F12 Developer Tools
   - Look for JavaScript errors
   - Check Network tab for failed requests

2. **Check Application Console:**
   - Look for "🚀 SUBMIT ENDPOINT HIT!" message
   - If missing → Form not reaching controller

3. **Check Form HTML:**
   ```html
   <form action="/test/submit" method="post">
   ```
   - Verify action URL is correct
   - Ensure method is POST

**Solutions:**
- **Missing Logs:** Form not reaching endpoint → Check form action URL
- **JavaScript Errors:** Clear browser cache, disable extensions
- **Network Errors:** Check if application is actually running

### Issue: Application Won't Start

**Error Messages:**
```
Error: Could not find or load main class
class file has wrong version
Port 8080 already in use
```

**Solutions:**

1. **Wrong Java Version:**
   ```powershell
   $env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"
   java -version  # Should show Java 17
   ```

2. **Port 8080 Busy:**
   ```powershell
   # Find what's using port 8080
   netstat -ano | findstr 8080
   
   # Stop Java processes
   Get-Process -Name "java" | Stop-Process -Force
   ```

3. **Maven Build Issues:**
   ```powershell
   mvn clean compile
   mvn dependency:resolve
   ```

### Issue: EPP Redirect Not Working

**Symptoms:**
- Form submits successfully (logs show)
- HTTP response received
- But no redirect to EPP portal

**Debugging:**

1. **Check HTML Response:**
   - Look in logs for "🎯 EPP Form Preview"
   - Should show: `<html><body onload='document.forms[0].submit()'>`

2. **Check Browser:**
   - Is JavaScript enabled?
   - Are popups blocked?
   - Check for Content Security Policy errors

3. **Check EPP URL:**
   - Should be: `https://epp.beta.pa.gov/Payment/Index`
   - Verify URL is accessible

**Solutions:**
- **Invalid HTML:** Check PaymentService and EppClient configuration
- **JavaScript Disabled:** Enable JavaScript in browser
- **Popup Blocked:** Allow popups for localhost

### Issue: Database Connection Errors

**Error Messages:**
```
Failed to configure a DataSource
Connection is not available
```

**Solutions:**

1. **Use Test Profile (H2):**
   ```powershell
   $env:SPRING_PROFILES_ACTIVE="test"
   ```

2. **Check H2 Console:**
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:ruc`
   - Username: `sa`
   - Password: (empty)

3. **Oracle Connection Issues:**
   - Verify Oracle database is running
   - Check connection strings in `application-local.yml`
   - Ensure Oracle JDBC driver is available

### Issue: Validation Errors

**Error Messages:**
```
Application unique ID is required
Validation failed for object
```

**Status:** ✅ RESOLVED - These validation issues have been fixed:

- `applicationUniqueId` is now optional (can be null)
- `saleDetailId` and `saleItemId` can be null
- ApplicationCode supports UUID format (50 characters)

**If still seeing validation errors:**
- Check you're using the latest code version
- Verify `@Valid` annotations in controllers
- Check SaleDetails and SaleItems field constraints

---

## 🔧 DIAGNOSTIC COMMANDS

### Environment Check
```powershell
# Check Java version
java -version

# Check Maven version
mvn --version

# Check environment variables
$env:JAVA_HOME
$env:SPRING_PROFILES_ACTIVE

# Check running processes
Get-Process -Name "java"

# Check port usage
netstat -ano | findstr 8080
```

### Application Health Check
```bash
# EPP service health
curl http://localhost:8080/payments/epp/ping

# UI controller health
curl http://localhost:8080/test/ping

# Test form UI
# Open: http://localhost:8080/test
```

### Log Analysis
**Look for these key log patterns:**

✅ **SUCCESS PATTERNS:**
```
🏗️ TestUIController CONSTRUCTOR called!
🚀 SUBMIT ENDPOINT HIT! /test/submit
✅ HTTP Response received!
📄 EPP Form HTML length: [size > 0]
```

❌ **ERROR PATTERNS:**
```
java.lang.IllegalArgumentException: Name for argument
Failed to configure a DataSource
Port 8080 already in use
Connection refused
```

---

## 📞 SUPPORT CHECKLIST

Before seeking support, verify:

### ✅ Environment Setup
- [ ] Java 17 installed and JAVA_HOME set
- [ ] Maven 3.8+ available
- [ ] PowerShell terminal used (not CMD)
- [ ] Correct project directory

### ✅ Application Status
- [ ] Application starts without errors
- [ ] Health endpoints return expected responses
- [ ] Test form loads at http://localhost:8080/test
- [ ] Maven compiler plugin with `-parameters` flag present

### ✅ Form Testing
- [ ] Form displays pre-populated data
- [ ] Test ping buttons work
- [ ] Form submission shows detailed logs
- [ ] EPP redirect HTML is generated

### ✅ Common Resolutions Tried
- [ ] Cleaned and rebuilt: `mvn clean compile`
- [ ] Stopped Java processes: `Get-Process -Name "java" | Stop-Process -Force`
- [ ] Used test profile: `$env:SPRING_PROFILES_ACTIVE="test"`
- [ ] Checked browser developer console

---

## 🎯 SUCCESS CRITERIA

**Application is working correctly when:**

1. **Startup:** Application starts with debugging messages
2. **Health:** Both ping endpoints return expected responses
3. **Form:** Test form loads with pre-populated data
4. **Submission:** Form submission shows comprehensive logs
5. **EPP:** Browser redirects to Pennsylvania EPP portal
6. **Integration:** Ready for production API integration

**Total resolution time:** Most issues resolve within 5-10 minutes following this guide.

---

**🔗 Related Documentation:**
- [README.md](./README.md) - Project overview and quick start
- [STARTUP_GUIDE.md](./STARTUP_GUIDE.md) - Detailed startup instructions
- [INTEGRATION_GUIDE.md](./INTEGRATION_GUIDE.md) - Integration into existing projects
- [API_REFERENCE.md](./API_REFERENCE.md) - API endpoints and examples