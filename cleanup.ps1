# Cleanup Script - Remove unnecessary files
# Keep only: README.md, INTEGRATION_GUIDE.md, QUICK_START.md, and RUC_EPP_Complete.postman_collection.json

Write-Host "Starting cleanup..." -ForegroundColor Green

# Delete temporary documentation files
$filesToDelete = @(
    "COMMIT_READY.md",
    "COMPLETE_TRANSFORMATION_SUMMARY.md",
    "TEST_CLEANUP_GUIDE.md",
    "TRANSFORMATION_PLAN.md",
    "PACKAGE_RENAME_GUIDE.md",
    "PACKAGE_VERIFICATION.md",
    "POSTMAN_GUIDE.md",
    "README_OLD.md",
    "git-commit-push.ps1",
    "rename-package.ps1",
    "rename-to-ruc.bat",
    "update-package-names.ps1",
    "postman\RUC_EPP.postman_collection.json",
    "postman\RUK_EPP.postman_collection.json"
)

foreach ($file in $filesToDelete) {
    if (Test-Path $file) {
        Remove-Item $file -Force
        Write-Host "✅ Deleted: $file" -ForegroundColor Yellow
    } else {
        Write-Host "⚠️  Not found: $file" -ForegroundColor Gray
    }
}

# Rename QUICK_REFERENCE.md to QUICK_START.md
if (Test-Path "QUICK_REFERENCE.md") {
    Rename-Item "QUICK_REFERENCE.md" -NewName "QUICK_START.md" -Force
    Write-Host "✅ Renamed: QUICK_REFERENCE.md → QUICK_START.md" -ForegroundColor Green
}

Write-Host "`n✅ Cleanup complete!" -ForegroundColor Green
Write-Host "`n📁 Remaining files:" -ForegroundColor Cyan
Write-Host "  ✅ README.md - Main project documentation"
Write-Host "  ✅ INTEGRATION_GUIDE.md - Step-by-step integration"
Write-Host "  ✅ QUICK_START.md - Quick start guide"
Write-Host "  ✅ postman\RUC_EPP_Complete.postman_collection.json - Postman collection"
Write-Host "  ✅ All source code files (src/)"
Write-Host "  ✅ pom.xml and configuration files"
Write-Host "`nPress any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
