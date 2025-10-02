# 🚀 Quick Start Guide

## Prerequisites Check

✅ **Java 17+** installed  
✅ **Maven 3.6+** installed  
✅ Terminal/PowerShell access

---

## 1️⃣ Start the Application (2 minutes)

### Option A: Test Profile (H2 Database - Recommended)

```powershell
# Navigate to project
cd "C:\Dev Resources\Workspace\EPPTest"

# Set environment (use same terminal session)
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.12"
$env:SPRING_PROFILES_ACTIVE="test"

# Run application
mvn clean spring-boot:run
```

**Application starts on**: http://localhost:8080

### Option B: Local Profile (Oracle Database)

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
mvn clean spring-boot:run
```

⚠️ Requires Oracle database configured in `application.yml`

---

## 2️⃣ Verify Installation (1 minute)

### Health Check
Open browser or use curl:

```powershell
# Browser
http://localhost:8080/payments/epp/ping

# PowerShell
Invoke-WebRequest -Uri "http://localhost:8080/payments/epp/ping"
```

**Expected Response**: `"pong"`

### Test UI
```
http://localhost:8080/test/form
```

You should see a modern payment form with gradient design.

---

## 3️⃣ Test Payment Flow (3 minutes)

### Using Test UI

1. **Open Form**: http://localhost:8080/test/form
2. **Fill Required Fields**:
   - Order Key: (auto-generated or custom)
   - Email: your-email@example.com
   - Item Description: Test Payment
   - Amount: 25.00
3. **Submit**: Click "Submit Payment"
4. **Review**: See generated EPP form HTML

### Using Postman

1. **Import Collection**:
   - Open Postman
   - Import → File
   - Select: `postman/RUC_EPP_Complete.postman_collection.json`

2. **Run Requests**:
   - "Health Check - Ping" → Should return "pong"
   - "Start Payment - Initiate RUC Payment" → Returns HTML form

---

## 4️⃣ Build & Test (5 minutes)

### Run Tests
```powershell
mvn test
```

**Expected**: All 3 tests pass
- PaymentControllerTest
- EppClientTest  
- EppTransactionRepositoryTest

### Build JAR
```powershell
mvn clean package
```

**Output**: `target/epp-integration-1.0.0.jar`

### Run JAR
```powershell
java -jar target/epp-integration-1.0.0.jar --spring.profiles.active=test
```

---

## 🎯 Key Endpoints

| URL | Method | Purpose |
|-----|--------|---------|
| http://localhost:8080/payments/epp/ping | GET | Health check |
| http://localhost:8080/test/form | GET | Test payment UI |
| http://localhost:8080/payments/epp/start | POST | Start payment (API) |
| http://localhost:8080/payments/epp/OnEPPResult | POST | EPP callback |

---

## 🔧 Common Commands

### Build & Run
```powershell
# Clean build
mvn clean compile

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=test

# Package without tests
mvn clean package -DskipTests
```

### Database
```powershell
# H2 Console (if enabled)
http://localhost:8080/h2-console

# Oracle connection test
Test-Connection -ComputerName localhost -Port 1521
```

### Troubleshooting
```powershell
# Kill Java processes
taskkill /f /im java.exe

# Find port usage
netstat -ano | findstr :8080

# Check Java version
java -version

# Check Maven version
mvn --version
```

---

## 📋 Integration Checklist

Before integrating into your project:

- [ ] Application runs successfully
- [ ] Health check returns "pong"
- [ ] Test form loads and works
- [ ] All tests pass (`mvn test`)
- [ ] Postman collection imported
- [ ] Sample payment tested
- [ ] Read INTEGRATION_GUIDE.md

---

## 📚 Next Steps

1. **Explore API**: Use Postman collection to test all endpoints
2. **Read Documentation**: 
   - `README.md` - Complete overview
   - `INTEGRATION_GUIDE.md` - Integration instructions
3. **Configure EPP**: Get Application Code from Pennsylvania EPP team
4. **Test Payment**: Use test form to generate EPP payment

---

## 🐛 Quick Troubleshooting

### Port Already in Use
```powershell
taskkill /f /im java.exe
```

### Build Fails
```powershell
mvn clean compile
```

### Tests Fail
```powershell
# Run specific test
mvn test -Dtest=PaymentControllerTest

# Skip tests
mvn package -DskipTests
```

### Database Issues
- **H2**: Works automatically (no configuration needed)
- **Oracle**: Check `application.yml` connection settings

---

## ✅ Success Indicators

You're ready when:
- ✅ Application starts without errors
- ✅ `/ping` returns "pong"
- ✅ Test form loads correctly
- ✅ All tests pass
- ✅ Postman collection works

---

## 🤝 Need Help?

1. Check **README.md** for detailed documentation
2. Review **INTEGRATION_GUIDE.md** for step-by-step instructions
3. Check application logs in terminal
4. Test with debug endpoint: POST `/payments/epp/debug-json`

---

**Estimated Setup Time**: 10-15 minutes  
**Status**: ✅ Production Ready  
**Version**: 1.0.0
