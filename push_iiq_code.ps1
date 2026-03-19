# Ask user input
$repoLink = Read-Host "Enter Git repository link"
$commitMessage = Read-Host "Enter commit message"

# Navigate to your project folder
Set-Location "E:\IIQ-SSD\"

# Initialize repo if not already initialized
if (!(Test-Path ".git")) {
    git init
}

# Check if remote already exists
$remoteExists = git remote

if ($remoteExists -notcontains "origin") {
    git remote add origin $repoLink
} else {
    git remote set-url origin $repoLink
}

# Git operations
git add .
git commit -m "$commitMessage"
git push -u origin master