# Git/GitLab Management Guide

## Initial Setup

### First Time Repository Setup
```powershell
# Initialize repository
git init

# Add all files
git add .

# Initial commit
git commit -m "Initial commit: EPP Integration project"

# Add remote origin (replace with your GitLab URL)
git remote add origin https://gitlab.com/yourusername/epp-integration.git

# Push to main branch
git push -u origin main
```

## Daily Git Operations

### 1. Check Status & View Changes
```powershell
# Check current status
git status

# View changes
git diff

# View staged changes
git diff --cached

# View commit history
git log --oneline -10
```

### 2. Stage & Commit Changes
```powershell
# Stage specific files
git add filename.java

# Stage all changes
git add .

# Stage all modified files (not new files)
git add -u

# Commit with message
git commit -m "Your commit message"

# Add and commit in one step
git commit -am "Your commit message"
```

### 3. Push Changes
```powershell
# Push to current branch
git push

# Push to specific branch
git push origin branch-name

# Force push (use carefully)
git push --force
```

### 4. Pull Latest Changes
```powershell
# Pull from current branch
git pull

# Pull from specific branch
git pull origin main

# Fetch without merging
git fetch origin
```

## Branch Management

### Create & Switch Branches
```powershell
# Create new branch
git branch feature/new-feature

# Create and switch to new branch
git checkout -b feature/new-feature

# Switch to existing branch
git checkout main

# Switch to branch (newer syntax)
git switch main

# List all branches
git branch -a
```

### Merge Branches
```powershell
# Switch to main
git checkout main

# Merge feature branch
git merge feature/new-feature

# Delete merged branch
git branch -d feature/new-feature

# Force delete branch
git branch -D feature/new-feature
```

## GitLab Specific Operations

### Remote Management
```powershell
# View remote URLs
git remote -v

# Change remote URL
git remote set-url origin https://gitlab.com/yourusername/epp-integration.git

# Add additional remote
git remote add gitlab https://gitlab.com/yourusername/epp-integration.git
```

### GitLab CI/CD Integration
```powershell
# Push and trigger pipeline
git push origin main

# Tag for release
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

## Emergency Commands

### Undo Operations
```powershell
# Undo last commit (keep changes)
git reset --soft HEAD~1

# Undo last commit (discard changes)
git reset --hard HEAD~1

# Undo changes to specific file
git checkout -- filename.java

# Undo all local changes
git reset --hard HEAD
```

### Stash Changes
```powershell
# Stash current changes
git stash

# Apply stashed changes
git stash pop

# List stashes
git stash list

# Apply specific stash
git stash apply stash@{0}
```

## Best Practices

### Commit Message Format
```
feat: add new payment validation logic
fix: resolve null pointer exception in controller
docs: update API documentation
refactor: improve service layer architecture
test: add unit tests for payment service
```

### Branch Naming Convention
```
feature/feature-name
bugfix/issue-description
hotfix/critical-fix
release/version-number
```