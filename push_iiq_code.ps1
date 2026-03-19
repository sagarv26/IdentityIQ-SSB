# Powershell script to push changes to repo
# Default repository link
$repoLink = "https://github.com/sagarv26/IdentityIQ-SSB.git"

# Ask only commit message
$commitMessage = Read-Host "Enter commit message"

# Navigate to your project folder
Set-Location "E:\IIQ-SSD\"

# Enable credential manager
git config --global credential.helper manager

# Initialize repo if not exists
if (!(Test-Path ".git")) {
    git init
}

# Ensure branch is main (safe for old Git too)
git branch -M main

# Set remote
$remoteExists = git remote

if ($remoteExists -notcontains "origin") {
    git remote add origin $repoLink
} else {
    git remote set-url origin $repoLink
}

# Add files
git add .

# Check for changes
$status = git status --porcelain

if (-not $status) {
    Write-Host "No new changes to commit. Attempting push..." -ForegroundColor Yellow
} else {
    git commit -m "$commitMessage"
}


# Push (first push will create branch)
git push -u origin main