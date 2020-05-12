
PROVISION PROCESSOR FRAMEWORK README

Quick Deployment Steps:
1. Copy the file SP_EMAILTEXT_Custom.xml to a customer config folder **
2. Rename the custom object and file **
3. Update the properties %%SP_CUSTOM_FV_RULE_LIBRARY_NAME%% and
	 %%SP_CUSTOM_FV_RULE_LIBRARY_PATH%% to 
	reflect the new object name and path **
4. In a provisioning workflow, call the SP Provision Processor Sub

NOTE:  SEE THE SAMPLE WORKFLOW AND ALL OF THE VARIABLES THAT ARE PASSED IN
	THE PROCESSOR ALSO CURRENTLY REQUIRES CONFIGURATION OF THE APPROVAL FRAMEWORK

** By default, the properties already point to the existing custom library location.  
	Steps 1-3 are optional
	but it is recommended that the file and name be changed to reflect the given customer.  

