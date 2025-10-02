# Maven Commands Reference

## Quick Reference

### Development Commands (Fast)

| Command | What It Does | When to Use |
|---------|-------------|-------------|
| `mvn clean compile` | Deletes target folder + compiles source code only | **Daily development** - Fast iteration |
| `mvn spring-boot:run` | Runs Spring Boot app without packaging | **Running locally** - Quick testing |
| `mvn clean spring-boot:run` | Clean + compile + run in one command | **Most common** - Fresh start each time |

### Testing & Packaging Commands (Slower)

| Command | What It Does | When to Use |
|---------|-------------|-------------|
| `mvn clean install` | Clean + compile + test + package JAR + install to local Maven repo | **Before commit** - Full verification |
| `mvn test` | Runs unit tests only | **Testing changes** - Verify tests pass |
| `mvn clean test` | Clean + compile + run tests | **Clean test run** - After major changes |
| `mvn package` | Compile + test + create JAR in target/ | **Creating deployable JAR** |
| `mvn clean package` | Clean + compile + test + create JAR | **Production build** |

---

## Detailed Explanation

### `mvn clean compile` (Current in build-and-run.ps1)

**What happens**:
1. **clean**: Deletes the `target/` directory (removes old compiled files)
2. **compile**: Compiles `src/main/java` → `target/classes`

**Time**: ~10-15 seconds  
**Use for**: Daily development, quick iterations  
**Pros**: 
- ✅ Fast
- ✅ Catches compilation errors quickly
- ✅ Good for UI/config changes

**Cons**: 
- ❌ Doesn't run tests
- ❌ Doesn't create JAR file
- ❌ Can't detect test failures

**Example Output**:
```
[INFO] --- clean:3.2.0:clean (default-clean) @ epp-integration ---
[INFO] Deleting C:\Dev Resources\Workspace\EPPTest\target
[INFO] --- resources:3.3.1:resources (default-resources) @ epp-integration ---
[INFO] --- compiler:3.13.0:compile (default-compile) @ epp-integration ---
[INFO] Compiling 21 source files with javac
[INFO] BUILD SUCCESS
```

---

### `mvn clean install` (Commented out in build-and-run.ps1)

**What happens**:
1. **clean**: Deletes the `target/` directory
2. **compile**: Compiles `src/main/java` → `target/classes`
3. **test-compile**: Compiles `src/test/java` → `target/test-classes`
4. **test**: Runs all unit tests
5. **package**: Creates JAR file in `target/`
6. **install**: Copies JAR to local Maven repository (`~/.m2/repository`)

**Time**: ~30-60 seconds (depending on test count)  
**Use for**: Before commits, CI/CD pipelines, production builds  
**Pros**: 
- ✅ Complete verification
- ✅ Runs all tests
- ✅ Creates deployable JAR
- ✅ Installs to local Maven repo for other projects

**Cons**: 
- ❌ Slower (runs all tests)
- ❌ Overkill for quick development iterations

**Example Output**:
```
[INFO] --- clean:3.2.0:clean (default-clean) ---
[INFO] --- resources:3.3.1:resources (default-resources) ---
[INFO] --- compiler:3.13.0:compile (default-compile) ---
[INFO] --- resources:3.3.1:testResources (default-testResources) ---
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) ---
[INFO] --- surefire:3.0.0:test (default-test) ---
[INFO] Running com.ruc.payments.PaymentControllerTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] --- jar:3.3.0:jar (default-jar) ---
[INFO] Building jar: target/epp-integration-1.0.0.jar
[INFO] --- install:3.1.1:install (default-install) ---
[INFO] Installing target/epp-integration-1.0.0.jar to ~/.m2/repository
[INFO] BUILD SUCCESS
```

---

## Maven Lifecycle Phases (What Runs When)

Maven has a **sequential lifecycle**. When you run a phase, all previous phases run automatically:

```
clean
  ↓
compile          ← mvn clean compile stops here (FAST)
  ↓
test-compile
  ↓
test
  ↓
package          ← mvn clean package stops here (MEDIUM)
  ↓
install          ← mvn clean install stops here (FULL)
  ↓
deploy
```

---

## Recommendation by Scenario

### 🏃 Daily Development (Fast Iteration)
```powershell
mvn clean spring-boot:run
# OR
mvn clean compile
mvn spring-boot:run
```
**Why**: Fastest way to see changes

---

### 🧪 Before Committing Code
```powershell
mvn clean install
```
**Why**: Ensures all tests pass and build works

---

### 📦 Creating Deployable JAR
```powershell
mvn clean package
# JAR created in: target/epp-integration-1.0.0.jar
```
**Why**: Creates standalone executable JAR

---

### 🔍 Just Testing
```powershell
mvn test
# OR for clean test run:
mvn clean test
```
**Why**: Faster than full install, just runs tests

---

### 🚀 CI/CD Pipeline
```powershell
mvn clean install
# OR with coverage reports:
mvn clean install jacoco:report
```
**Why**: Full verification before deployment

---

## Current build-and-run.ps1 Strategy

The script uses **`mvn clean compile`** because:
1. ✅ **Fast** - Only compiles, doesn't run tests
2. ✅ **Good for development** - Quick iterations
3. ✅ **Sufficient for running** - `mvn spring-boot:run` doesn't need JAR
4. ✅ **Catches syntax errors** - Compilation will fail if code is broken

### When to Switch to `mvn clean install`

Uncomment line 19 and comment line 20 if you want to:
- Run all tests before starting the app
- Ensure complete build integrity
- Create the JAR file in `target/`
- Don't mind waiting 30-60 seconds longer

---

## Quick Decision Tree

```
Do you need to:
├─ Just run the app quickly?
│  └─ Use: mvn clean compile + mvn spring-boot:run ✅ (Current)
│
├─ Run tests before starting?
│  └─ Use: mvn clean install + mvn spring-boot:run
│
├─ Only run tests (no app)?
│  └─ Use: mvn test
│
├─ Create JAR for deployment?
│  └─ Use: mvn clean package
│
└─ Verify everything before commit?
   └─ Use: mvn clean install
```

---

## Performance Comparison

| Command | Time (Approx) | What It Does |
|---------|---------------|--------------|
| `mvn compile` | ~8 sec | Just compile (no clean) |
| `mvn clean compile` | ~12 sec | Clean + compile ✅ **Current** |
| `mvn clean test` | ~25 sec | Clean + compile + test |
| `mvn clean package` | ~30 sec | Clean + compile + test + JAR |
| `mvn clean install` | ~35 sec | Full build + install to repo |

---

## Common Maven Commands Cheatsheet

```bash
# Clean commands
mvn clean                      # Just delete target/
mvn clean compile              # Clean + compile source
mvn clean test                 # Clean + compile + run tests
mvn clean package              # Clean + compile + test + create JAR
mvn clean install              # Clean + full build + install to ~/.m2

# No-clean commands (faster, but uses cached files)
mvn compile                    # Just compile (incremental)
mvn test                       # Just run tests
mvn package                    # Just create JAR (if already compiled)
mvn install                    # Just install (if already built)

# Spring Boot specific
mvn spring-boot:run            # Run app without packaging
mvn clean spring-boot:run      # Clean + compile + run (common)

# Skip tests (faster builds)
mvn clean install -DskipTests  # Build without running tests
mvn clean package -DskipTests  # Package without tests

# Verbose output
mvn clean install -X           # Debug mode (very verbose)
mvn clean install -e           # Show error details

# Offline mode (no internet required)
mvn clean install -o           # Use local Maven cache only
```

---

## build-and-run.ps1 Recommendations

### Current Setup (Fast Development) ✅
```powershell
mvn clean compile              # Line 20 (active)
mvn spring-boot:run            # Line 32
```
**Best for**: Daily development, quick UI changes, config tweaks

### Alternative Setup (Safe Development)
```powershell
mvn clean install              # Line 19 (commented)
mvn spring-boot:run            # Line 32
```
**Best for**: Before commits, after major changes, when tests are critical

### Hybrid Approach (Recommended)
Create two scripts:
- `build-and-run.ps1` - Uses `mvn clean compile` (fast)
- `build-and-run-full.ps1` - Uses `mvn clean install` (thorough)

---

## Summary

| Aspect | `mvn clean compile` | `mvn clean install` |
|--------|---------------------|---------------------|
| **Speed** | ⚡ Fast (~12 sec) | 🐌 Slower (~35 sec) |
| **Runs Tests** | ❌ No | ✅ Yes |
| **Creates JAR** | ❌ No | ✅ Yes |
| **Installs to Repo** | ❌ No | ✅ Yes |
| **Use For** | Daily development | Before commits |
| **Current Choice** | ✅ **Active (Line 20)** | Commented (Line 19) |

**Bottom Line**: Use `mvn clean compile` (current) for fast development. Switch to `mvn clean install` before committing code to ensure tests pass.
