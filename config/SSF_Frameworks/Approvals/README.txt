
APPROVAL FRAMEWORK README

Quick Deployment Steps:
1. Copy SPCONF_ApprovalObjectMappings.xml to /config/Custom folder 
2. Update approval object mappings with appropriate configurations and rule names.  Samples are provided.
3. Update calling workflow to call SP Dynamic Approval Sub **.  Input vars are:
	- identityName
	- project
	- approvalSet
	- requestor
	- identityRequestId
	- emailArgList
	- approvedTo
	- rejectedTo
	- approvedTemplate
	- rejectedTemplate
	
** Alternative is to call the SP Provision Processor Sub with same input variables as
this framework automatically calls the approval framework


Update these.  Once in place, inside of the custom mapping object, a method in the library can be called directly by 
prefixing the name of the method with "method:".  For example, "method:cstPreApprovalDefaultSplitterRule".  The prefix "rule:" 
can be used to denote that the it is still an actual rule call.  However, no prefix will default to a rule.  

