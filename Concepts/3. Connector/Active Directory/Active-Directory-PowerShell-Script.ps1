<#
New-ADUser - Creates a new Active Directory user
Get-ADUser - Gets one or more Active Directory users so that you can perform some action with them
Set-ADUser - Modifies the properties of an existing Active Directory user
Remove-ADUser - Removes the specified user from Active Directory
#>

#The Import-Module cmdlet adds one or more modules to the current session.
Import-Module ActiveDirectory

#The New-ADUser cmdlet creates a new Active Directory user.
$path = 'OU=test,DC=domain,DC=local'
$name = 'Display Name'
$username = 'username'
$password = 'password'

$password = ConvertTo-SecureString $password -AsPlainText -force
New-ADUser -Path $path -SamAccountName $username -Name $name -AccountPassword $password -Enabled $true


#The Get-ADUser cmdlet gets a user object or performs a search to retrieve multiple user objects
$user = Get-ADUser 'username'

#The Set-ADUser cmdlet modifies the properties of an Active Directory user.<
Set-ADUser -Identity 'username' -Enabled $false

#The Remove-ADUser cmdlet removes an Active Directory user.
Remove-ADUser -Identity 'username'


#The New-ADGroup cmdlet creates a new Active Directory group object.
$path = 'OU=test,DC=domain,DC=local'
New-ADGroup -Path $path -Name 'Group Name' -GroupScope Global


#The Remove-ADGroup cmdlet removes an Active Directory group object.
Remove-ADGroup -Identity 'Group Name'


#The Add-ADGroupMember cmdlet adds one or more users, groups, service accounts, or 
#computers as new members of an Active Directory group.
Add-ADGroupMember -Identity 'Group Name' -Members 'username'


#The Add-ADGroupMember cmdlet adds one or more users, groups, service accounts, 
#or computers as new members of an Active Directory group.
Add-ADGroupMember -Identity 'Group Name' -Members 'username'