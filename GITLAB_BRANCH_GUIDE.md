# GitLab Branch Management & Check-in Guide

## Creating Feature Branches

### 1. Create and Switch to New Branch
```powershell
# Create feature branch for new work
git checkout -b feature/payment-enhancements

# Or create bugfix branch
git checkout -b bugfix/validation-errors

# Or create hotfix branch
git checkout -b hotfix/critical-security-patch
```

### 2. Work on Your Changes
```powershell
# Check what files have changed
git status

# View your changes
git diff

# Stage specific files
git add src/main/java/com/example/PaymentService.java

# Or stage everything
git add .
```

### 3. Commit Your Changes
```powershell
# Commit with descriptive message
git commit -m "feat: add payment retry logic with exponential backoff"

# Or commit with detailed message
git commit -m "feat: enhance payment validation

- Add input sanitization for all payment fields
- Implement business rule validation
- Add comprehensive error messages
- Update unit tests for new validation logic"
```

### 4. Push Branch to GitLab
```powershell
# Push new branch to GitLab
git push -u origin feature/payment-enhancements

# For subsequent pushes to same branch
git push
```

## GitLab Merge Request Process

### 1. Create Merge Request
1. Go to your GitLab repository
2. Click "Create merge request" button
3. Select source branch (your feature branch) → target branch (main)
4. Add description and assign reviewers
5. Click "Create merge request"

### 2. Merge Request Best Practices
- **Title**: Clear, descriptive title
- **Description**: Explain what changes were made and why
- **Assign**: Add team members for code review
- **Labels**: Use appropriate labels (feature, bugfix, documentation)
- **Milestone**: Link to project milestone if applicable

## Daily GitLab Workflow

### Morning Routine
```powershell
# Switch to main branch
git checkout main

# Pull latest changes
git pull origin main

# Create new feature branch
git checkout -b feature/new-task-name

# Start working on your changes...
```

### Evening Check-in
```powershell
# Check what you've changed
git status
git diff

# Stage and commit changes
git add .
git commit -m "wip: progress on payment integration"

# Push to remote branch
git push origin feature/new-task-name
```

## Managing Multiple Branches

### Switch Between Branches
```powershell
# List all branches
git branch -a

# Switch to existing branch
git checkout feature/other-feature

# Switch back to main
git checkout main

# Create branch from specific commit
git checkout -b hotfix/urgent-fix abc1234
```

### Keep Branch Updated
```powershell
# While on your feature branch
git checkout feature/your-branch

# Fetch latest main changes
git fetch origin main

# Merge main into your branch
git merge origin/main

# Or rebase your branch on main (cleaner history)
git rebase origin/main
```

## Conflict Resolution

### When Merge Conflicts Occur
```powershell
# Pull latest changes and see conflicts
git pull origin main

# View conflicted files
git status

# Edit files to resolve conflicts manually
# Look for <<<<<<< HEAD, =======, >>>>>>> markers

# After resolving conflicts, stage files
git add conflicted-file.java

# Complete the merge
git commit -m "resolve merge conflicts with main"
```

## Release Management

### Creating Release Branch
```powershell
# Create release branch from main
git checkout main
git pull origin main
git checkout -b release/v1.2.0

# Make final adjustments
# Update version numbers, documentation

# Commit release preparations
git commit -m "prepare release v1.2.0"

# Push release branch
git push -u origin release/v1.2.0
```

### Tagging Releases
```powershell
# Create annotated tag
git tag -a v1.2.0 -m "Release version 1.2.0 - Enhanced payment processing"

# Push tags to GitLab
git push origin v1.2.0

# Or push all tags
git push origin --tags
```

## GitLab CI/CD Integration

### Pipeline Triggers
```powershell
# Push to trigger pipeline
git push origin feature/your-branch

# Create merge request to trigger merge pipeline
# Tag commit to trigger release pipeline
git tag -a v1.0.1 -m "Patch release"
git push origin v1.0.1
```

### Pipeline Status
- Check pipeline status in GitLab UI
- View job logs for failed builds
- Retry failed jobs if needed

## Emergency Procedures

### Rollback Changes
```powershell
# Rollback last commit (keep changes)
git reset --soft HEAD~1

# Rollback and discard changes
git reset --hard HEAD~1

# Rollback specific file
git checkout HEAD -- filename.java
```

### Hotfix Process
```powershell
# Create hotfix from main
git checkout main
git pull origin main
git checkout -b hotfix/critical-issue

# Make urgent fix
git add .
git commit -m "hotfix: resolve critical security vulnerability"

# Push hotfix
git push -u origin hotfix/critical-issue

# Create urgent merge request to main
```

## Branch Cleanup

### After Merge Request is Accepted
```powershell
# Switch to main
git checkout main

# Pull merged changes
git pull origin main

# Delete local feature branch
git branch -d feature/completed-feature

# Delete remote branch (if not auto-deleted)
git push origin --delete feature/completed-feature
```

### Cleanup Old Branches
```powershell
# List all branches
git branch -a

# Delete multiple local branches
git branch -D branch1 branch2 branch3

# Prune remote tracking branches
git remote prune origin
```

## GitLab Project Settings

### Repository Settings
1. **Branch Protection**: Protect main branch from direct pushes
2. **Merge Request Settings**: Require approvals, remove source branch after merge
3. **CI/CD Variables**: Set environment-specific variables
4. **Deploy Keys**: Add SSH keys for deployment servers

### Useful GitLab Features
- **Issues**: Track bugs and features
- **Wiki**: Project documentation
- **Snippets**: Code snippets and utilities
- **Container Registry**: Docker images
- **Packages**: Maven artifacts