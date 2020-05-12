*********************************************
Report Extractor v1.0
Adam Creaney
adam.creaney@sailpoint.com

Description:
In environments where IdentityIQ reports are generated over
larger data sets, the amount of storage required to house data
contained in these reports will continue to increase, potentially
causing performance issues.

The out of the box way to remove these report "results" is to delete
the TaskResults associated with the report being run.  This tool, when
run, will scan through all TaskResults, isolate those that are 
related to IdentityIQ reports, back them up and then remove them.  
**********************************************

Configuration:

There are 3 files associated with this utility:
ReportExtractor.java
ReportExtractorSettings.java
SP_TaskDefinition_ReportExtractor.xml

1)  Compile and build ReportExtractor.java, and ReportExtractorSettings.java into
the IdentityIQ environment using the SSB process

2)  Import SP_TaskDefinition_ReportExtractor.xml (using the SSB, manual import, etc..)

3)  Find the task "SP Export Reports" under the "generic" section of "Monitor->Tasks"

4)  Adjust the settings in the Task Definition

	a) Backup reports older than (number of days) - REQUIRED, this option will backup
	   only reports older than the number of days specified
	b) Zip the archived reports?  - This will attempt to compress the output (recommended
	   for large data sets)
	c) Path to backup location - REQUIRED, this option specifies where the outputed archive
	   will be located
	d) Delete reports after backup? - OPTIONAL - when selected, after the backup is performed
	   the report associated TaskResults will be pruned, also pruning the entries in the spt_file_bucket
	   and associated tables for the report data.  This is the option that will free up space in the 
	   database.

5)  Run the task, either right away or scheduled like a normal task.

6)  Task result will show the number of reports backed up, and if selected, the number of task results
i.e. reports pruned.

7)  Verify that the backed up reports are in the location specified in the input field.

CAUTION:  It is recommended that this utility is tested first (i.e. don't select "delete") to get a count of
how many reports *would* be deleted.  Also, since pruning is a destructive process, use caustion.

Developement is ongoing, if you see room for improvement, or encounter any bugs, please reach out to me on
compass, or via email.