# Default repository link
$repoLink = "https://github.com/sagarv26/IdentityIQ-SSB.git"

# Ask only commit message
$commitMessage = Read-Host "Enter commit message"

# Navigate to your project folder
Set-Location "E:\IIQ-SSD\"

# Ensure Git credential manager is enabled
git config --global credential.helper manager

# Initialize repo if not already initialized
if (!(Test-Path ".git")) {
    git init
}

# Check if remote exists
$remoteExists = git remote

if ($remoteExists -notcontains "origin") {
    git remote add origin $repoLink
} else {
    git remote set-url origin $repoLink
}

# Add files
git add .

# Check if there are changes to commit
$status = git status --porcelain

if (-not $status) {
    Write-Host "No changes to commit." -ForegroundColor Yellow
    exit
}

# Commit changes
git commit -m "$commitMessage"

# Detect current branch
$branch = git branch --show-current

if (-not $branch) {
    $branch = "main"
}

# Push to repo
git push -u origin $branch