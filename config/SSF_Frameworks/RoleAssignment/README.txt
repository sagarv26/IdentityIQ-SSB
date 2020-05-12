
ROLE ASSIGNMENT FRAMEWORK README

Quick Deployment Steps:
1. Create an Organizational Role and place birthright roles in this organization 
2. Update target property %%SP_BIRTHRIGHT_ROLES_ORGANIZATION_ROLE%% with name of of organization role in step 1
3. In workflows or rules, add the rule reference for SP Roles Assignment Rule Library
4. In workflows or rules add either line of logic:

	- AccountRequest acctReq = getBirthrightRolesIIQAccountRequest(SailPointContext context, Identity identity); 
		
		Will return an object of type ProvisioningPlan.AccountRequest with:
			App Name = "IIQ"
			Operation = "Modify"
			AttributeRequest "Add" "assignedRoles" [Any birthright roles that should be added]
			
		Ideally used in joiners.
		
	- AccountRequest acctReq = getAddOrRemoveRolesAccountRequest(SailPointContext context, Identity identity);
	
		Will return an object of type ProvisioningPlan.AccountRequest with:
			App Name = "IIQ"
			Operation = "Modify"
			AttributeRequest "Add" "assignedRoles" [Any birthright roles that should be added]
			AttributeRequest "Remove" "assignedRoles" [Any birthright roles that should be removed]
			
		Ideally used in movers.

