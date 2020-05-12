package sailpoint.services.standard.task.genericImport;

import org.apache.log4j.Logger;

import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.TaskResult;
import sailpoint.object.TaskSchedule;
import sailpoint.task.AbstractTaskExecutor;
import sailpoint.tools.GeneralException;

public class GenericImportTaskExecutor extends AbstractTaskExecutor {

	private static final Logger log = Logger
			.getLogger(GenericImportTaskExecutor.class);

	public static final String IMPORT_CLASS_NAME = "genericImportDriverClass";
	public static final String IMPORT_MANUAL_HEADER = "importManualHeader";
	
	private TaskResult result = null;
	private SailPointContext context;
	private boolean terminate;

	@Override
	public void execute(SailPointContext context, TaskSchedule taskSchedule,
			TaskResult taskResult, Attributes<String, Object> attributes) throws GeneralException {

		log.debug("Starting execute");
		
		this.terminate = false;
		
		String importClassName = attributes.getString(IMPORT_CLASS_NAME);
		String importManualHeader = attributes.getString(IMPORT_MANUAL_HEADER);
		
		// If shorthand class name was used, prepend with the package name
		if (!importClassName.contains(".")) importClassName = this.getClass().getPackage().getName() + "." + importClassName;
		
		if (log.isDebugEnabled()) log.debug("Import class name: " + importClassName);
		GenericImport genericImport = null;

		try {
			genericImport = (GenericImport) Class.forName(importClassName)
					.newInstance();
			
			log.debug("Instantiated class");
			
		} catch (InstantiationException e) {
			throw new GeneralException("Error when instantiating class '"
					+ importClassName + "'", e);
		} catch (IllegalAccessException e) {
			throw new GeneralException("Error when instantiating class '"
					+ importClassName + "'", e);
		} catch (ClassNotFoundException e) {
			throw new GeneralException("Error when instantiating class '"
					+ importClassName + "'", e);
		}

		genericImport.setAttributes(attributes);

		this.result = taskResult;
		this.context = context;

		int progressInterval = 1; //taskSchedule.getDefinition().getEffectiveProgressInterval();

		// If the progressInterval in the task definition
		// is set less than 1, then set the progress interval to 1 by
		// default.
		if (progressInterval < 1) {
			progressInterval = 1;
		}

		if (log.isDebugEnabled()) {

			log.debug("Starting Generic Import");
			log.debug("  Progress interval: "
					+ String.valueOf(progressInterval));
		}

		// Update the progress bar
		updateProgress(context, this.result, "Initializing import", 0);

		GenericImportController genericImporter = new GenericImportController(genericImport);
		genericImporter.setTaskResult(result);
		genericImporter.setAttributes(attributes);
		
		int recordNo = 0;

		try {
			if (log.isDebugEnabled()) log.debug("Opening iterator");
			genericImporter.open();
			
			if (log.isDebugEnabled()) log.debug("Opened.");
			
			// Should update the display every 2 seconds
			long interval = System.currentTimeMillis()
					+ (progressInterval * 1000);

			while (genericImporter.hasNext()) {

				if (terminate) {
					result.setTerminated(this.terminate);
					break;
				}
				recordNo++;
				
				log.debug("Getting next from iterator");
				genericImporter.next();
				log.debug("Got next from iterator");
				if (System.currentTimeMillis() > interval) {
					
					interval = System.currentTimeMillis()
							+ (progressInterval * 1000);
					this.updateProgress(
							context,
							this.result,
							"Importing record number: "
									+ String.valueOf(recordNo));
				}

			}
			
			// Finalise here, only if there are no errors! :)
			
			this.updateProgress(
							context,
							this.result,
							"Finalizing import...");
			genericImporter.processFinalize();
			
		} catch (GeneralException e) {

			throw new GeneralException("GenericImporter has thrown an unexpected error: " + e.getMessage(),e);
			
		} finally {

			log.debug("Closing iterator");
			genericImporter.close();
			log.debug("Iterator is close");
			
		}

		updateProgress(context, this.result, "Completed import", 100);

		result.setAttribute("processed", recordNo);

		if (log.isDebugEnabled())
			log.debug("Finished object import task.");

	}

	@Override
	public boolean terminate() {
		this.terminate = true;
		return true;
	}

}
