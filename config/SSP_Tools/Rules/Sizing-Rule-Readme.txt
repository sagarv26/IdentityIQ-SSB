Sizing-Rule-Readme.txt

---------------

Description:
	This sizing rule counts various objects in the IdentityIQ installation. It does not alter or create any objects in the system. The results are printed and saved to two temporary files.

Instructions for use:
	- There are two options for using this rule: through the UI or through iiq console. Use whichever you are more confortable with.

	UI Option-
		1. Importing the file:
			a) Copy Rule-Standalone-Install-Sizing-Rule.xml to a known location.
			b) Using the IdentityIQ UI, navigate to the System Setup page.
			c) Select "Import from File" and browse to the copy of Rule-Standalone-Install-Sizing-Rule.xml. Import the file.
		2. Running the rule
			a) Navigate to the IdentityIQ debug page
			b) There will be a Run button with a dropdown selector next to it. Select "Sailpoint Sizing Rule" in the dropdown and press the run button.
			c) The rule will run and the output will be displayed. At the bottom of the output, it will indicate where it is saving the temporary files.
		3. Retrieve the files and either pass them on to SailPoint or use them for your own edification. 
			a) The json file will be used in a future sizing calculator under development.
			b) The txt file contains the same information that was displayed in the debug page.

	Console Option - 
		1. Run iiq console
		2. Import the file using the following command and substituting your file path:
			import <path to your copy of Rule-Standalone-Install-Sizing-Rule.xml>
		3. Run the rule using the following command in the console:
			rule "Sailpoint Sizing Rule"
		4. The output will be displayed. At the bottom of the output, it will indicate where it is saving the temporary files.
		5. Retrieve the files and either pass them on to SailPoint or use them for your own edification. 
			a) The json file will be used in a future sizing calculator under development.
			b) The txt file contains the same information that was displayed in the debug page.


Removing the rule:
	If desired the rule may be removed from the system using the iiq console with the following command:
		delete Rule "Sailpoint Sizing Rule"
