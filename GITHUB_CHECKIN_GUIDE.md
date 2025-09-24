# GitHub Check-in Procedure for EPP Integration Project

## Prerequisites
1. **GitHub Account**: Make sure you have a GitHub account
2. **Git Installed**: Verify Git is installed on your machine
3. **GitHub Personal Access Token**: For authentication (recommended over password)

## Step-by-Step Process

### Step 1: Initialize Git Repository
```powershell
# Navigate to your project directory (if not already there)
cd "C:\Dev Resources\Workspace\EPPTest"

# Initialize Git repository
git init
```

### Step 2: Configure Git (if not done already)
```powershell
# Set your name and email (use your GitHub credentials)
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Optional: Set default branch to main
git config --global init.defaultBranch main
```

### Step 3: Create .gitignore File
```powershell
# Create .gitignore to exclude unnecessary files
# This will be created automatically in next step
```

### Step 4: Add Files to Git
```powershell
# Add all files to staging area
git add .

# Check what files are staged
git status
```

### Step 5: Create Initial Commit
```powershell
# Create your first commit
git commit -m "Initial commit: EPP Integration project with Spring Boot 3.2.5"
```

### Step 6: Create GitHub Repository
1. **Go to GitHub.com**
2. **Click "+" icon** → "New repository"
3. **Repository Details**:
   - **Repository name**: `epp-integration` or `ruc-epp-integration`
   - **Description**: `RUC EPP Integration API with Spring Boot 3.2.5 and Commerce Hub compliance`
   - **Visibility**: Choose Public or Private
   - **DON'T** initialize with README (you already have files)
4. **Click "Create repository"**

### Step 7: Connect Local Repository to GitHub
```powershell
# Add remote origin (replace YOUR_USERNAME and REPO_NAME)
git remote add origin https://github.com/YOUR_USERNAME/epp-integration.git

# Verify remote is added
git remote -v
```

### Step 8: Push to GitHub
```powershell
# Push your main branch to GitHub
git branch -M main
git push -u origin main
```

## Authentication Options

### Option A: Personal Access Token (Recommended)
1. **Go to GitHub Settings** → Developer settings → Personal access tokens → Tokens (classic)
2. **Generate new token** with `repo` permissions
3. **Use token as password** when prompted during push

### Option B: GitHub CLI (Alternative)
```powershell
# Install GitHub CLI if not installed
winget install --id GitHub.cli

# Authenticate
gh auth login

# Push using GitHub CLI
gh repo create epp-integration --source=. --public --push
```

## Complete PowerShell Script

Here's a complete script you can run:

```powershell
# Navigate to project directory
cd "C:\Dev Resources\Workspace\EPPTest"

# Initialize Git
git init

# Configure Git (replace with your details)
git config user.name "Your Name"
git config user.email "your.email@example.com"

# Add all files
git add .

# Create initial commit
git commit -m "Initial commit: EPP Integration project

- Spring Boot 3.2.5 with Java 17
- EPP payment integration with Pennsylvania system
- Commerce Hub compliant with Rahul's requirements
- Custom message converter for text/html content type
- Complete REST API with validation
- H2 test database and Oracle production support
- Comprehensive documentation and testing setup"

# Add remote (replace YOUR_USERNAME with actual username)
git remote add origin https://github.com/YOUR_USERNAME/epp-integration.git

# Push to GitHub
git branch -M main
git push -u origin main
```

## Important Files to Include

### .gitignore File Content:
```
# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml

# IDE
.idea/
*.iws
*.iml
*.ipr
.vscode/

# OS
.DS_Store
Thumbs.db

# Logs
*.log
logs/

# Application specific
application-local.properties
application-dev.properties

# Temporary files
*.tmp
*.swp
*~

# Java
*.class
*.jar
*.war
*.ear
hs_err_pid*
```

## Project Structure That Will Be Uploaded:

```
EPPTest/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ruk/payments/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── repo/
│   │   │       └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/
│   │       └── db/migration/
├── pom.xml
├── README.md
├── INTEGRATION_GUIDE.md
├── API_REFERENCE.md
├── STARTUP_GUIDE.md
├── TROUBLESHOOTING.md
└── POSTMAN_START_ENDPOINT.md
```

## After Upload - GitHub Repository Features

1. **Repository Description**: Set a clear description
2. **Topics/Tags**: Add tags like `spring-boot`, `epp-integration`, `java`, `payment-gateway`
3. **README.md**: Your existing README will be the main page
4. **Releases**: Consider creating a v1.0.0 release
5. **Issues**: Enable for bug tracking
6. **Actions**: Set up CI/CD if needed

## Next Steps After Upload

1. **Verify Upload**: Check all files are present on GitHub
2. **Clone Test**: Clone to a different location to test
3. **Documentation**: Update any URLs in documentation
4. **Team Access**: Add collaborators if needed
5. **Backup**: Your code is now safely backed up!

## Troubleshooting Common Issues

### Authentication Failed
- Use Personal Access Token instead of password
- Check token permissions include `repo`

### Large Files
- Add large files to `.gitignore`
- Use Git LFS for files >100MB

### Line Ending Issues
```powershell
git config --global core.autocrlf true  # For Windows
```

### Push Rejected
```powershell
# If remote has changes you don't have
git pull origin main --rebase
git push origin main
```