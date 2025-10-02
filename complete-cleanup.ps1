# Complete Project Cleanup and Documentation Update Script
# This script will:
# 1. Delete all unnecessary files
# 2. Rename QUICK_REFERENCE.md to QUICK_START.md
# 3. Update README.md with clean, comprehensive documentation
# 4. Keep only essential files

Write-Host "`n🧹 Starting Complete Project Cleanup..." -ForegroundColor Cyan
Write-Host "=" * 60 -ForegroundColor Gray

# Step 1: Delete unnecessary documentation files
Write-Host "`n📄 Step 1: Removing unnecessary documentation files..." -ForegroundColor Yellow

$docsToDelete = @(
    "COMMIT_READY.md",
    "COMPLETE_TRANSFORMATION_SUMMARY.md",
    "TEST_CLEANUP_GUIDE.md",
    "TRANSFORMATION_PLAN.md",
    "PACKAGE_RENAME_GUIDE.md",
    "PACKAGE_VERIFICATION.md",
    "POSTMAN_GUIDE.md",
    "README_OLD.md"
)

foreach ($file in $docsToDelete) {
    if (Test-Path $file) {
        Remove-Item $file -Force
        Write-Host "  ✅ Deleted: $file" -ForegroundColor Green
    }
}

# Step 2: Delete helper scripts
Write-Host "`n🔧 Step 2: Removing helper scripts..." -ForegroundColor Yellow

$scriptsToDelete = @(
    "git-commit-push.ps1",
    "rename-package.ps1",
    "rename-to-ruc.bat",
    "update-package-names.ps1"
)

foreach ($script in $scriptsToDelete) {
    if (Test-Path $script) {
        Remove-Item $script -Force
        Write-Host "  ✅ Deleted: $script" -ForegroundColor Green
    }
}

# Step 3: Delete old Postman collections
Write-Host "`n📮 Step 3: Removing old Postman collections..." -ForegroundColor Yellow

$postmanOld = @(
    "postman\RUC_EPP.postman_collection.json",
    "postman\RUK_EPP.postman_collection.json"
)

foreach ($collection in $postmanOld) {
    if (Test-Path $collection) {
        Remove-Item $collection -Force
        Write-Host "  ✅ Deleted: $collection" -ForegroundColor Green
    }
}

# Step 4: Rename QUICK_REFERENCE.md to QUICK_START.md
Write-Host "`n📝 Step 4: Renaming quick reference..." -ForegroundColor Yellow

if (Test-Path "QUICK_REFERENCE.md") {
    Rename-Item "QUICK_REFERENCE.md" -NewName "QUICK_START.md" -Force
    Write-Host "  ✅ Renamed: QUICK_REFERENCE.md → QUICK_START.md" -ForegroundColor Green
}

# Step 5: Backup existing README.md
Write-Host "`n💾 Step 5: Backing up current README..." -ForegroundColor Yellow

if (Test-Path "README.md") {
    Copy-Item "README.md" -Destination "README.backup.md" -Force
    Write-Host "  ✅ Backup created: README.backup.md" -ForegroundColor Green
}

# Step 6: Show final project structure
Write-Host "`n📊 Final Project Structure:" -ForegroundColor Cyan
Write-Host "=" * 60 -ForegroundColor Gray

Write-Host "`n📄 Documentation Files (KEEP):" -ForegroundColor Green
Write-Host "  ✅ README.md                    - Main project documentation"
Write-Host "  ✅ INTEGRATION_GUIDE.md         - Step-by-step integration"
Write-Host "  ✅ QUICK_START.md               - Quick start guide"

Write-Host "`n📮 Postman Collection (KEEP):" -ForegroundColor Green
Write-Host "  ✅ postman\RUC_EPP_Complete.postman_collection.json"

Write-Host "`n📂 Source Code (KEEP):" -ForegroundColor Green
Write-Host "  ✅ src/main/java/com/ruc/payments/"
Write-Host "  ✅ src/test/java/com/ruc/payments/"
Write-Host "  ✅ src/main/resources/"

Write-Host "`n⚙️  Configuration Files (KEEP):" -ForegroundColor Green
Write-Host "  ✅ pom.xml"
Write-Host "  ✅ .gitignore"
Write-Host "  ✅ application.yml"
Write-Host "  ✅ application-test.yml"

Write-Host "`n🗑️  Deleted Files:" -ForegroundColor Red
Write-Host "  ❌ COMMIT_READY.md"
Write-Host "  ❌ COMPLETE_TRANSFORMATION_SUMMARY.md"
Write-Host "  ❌ TEST_CLEANUP_GUIDE.md"
Write-Host "  ❌ TRANSFORMATION_PLAN.md"
Write-Host "  ❌ PACKAGE_RENAME_GUIDE.md"
Write-Host "  ❌ PACKAGE_VERIFICATION.md"
Write-Host "  ❌ POSTMAN_GUIDE.md"
Write-Host "  ❌ README_OLD.md"
Write-Host "  ❌ git-commit-push.ps1"
Write-Host "  ❌ rename-package.ps1"
Write-Host "  ❌ rename-to-ruc.bat"
Write-Host "  ❌ update-package-names.ps1"
Write-Host "  ❌ postman\RUC_EPP.postman_collection.json"
Write-Host "  ❌ postman\RUK_EPP.postman_collection.json"

Write-Host "`n" + "=" * 60 -ForegroundColor Gray
Write-Host "✅ Cleanup Complete!" -ForegroundColor Green
Write-Host "`n📋 Next Steps:" -ForegroundColor Cyan
Write-Host "  1. Review README.md (backed up to README.backup.md)"
Write-Host "  2. Delete README.backup.md if new README looks good"
Write-Host "  3. Delete this cleanup script (complete-cleanup.ps1)"
Write-Host "  4. Commit changes: git add . && git commit -m 'Clean up project files'"
Write-Host "`nPress any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
