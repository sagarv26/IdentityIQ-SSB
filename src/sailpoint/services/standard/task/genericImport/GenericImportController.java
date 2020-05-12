package sailpoint.services.standard.task.genericImport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.proxy.HibernateProxy;

import sailpoint.api.ObjectUtil;
import sailpoint.api.PersistenceManager;
import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.Rule;
import sailpoint.object.SailPointObject;
import sailpoint.object.TaskResult;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;
import bsh.EvalError;
import bsh.Interpreter;

public class GenericImportController {

	public static final String VERSION = "2.0.1";
	
	public static final String IMPORT_TRANSFORM_RULE = "importTransformRule";
	public static final String IMPORT_FINALIZE_RULE = "importFinalizeRule";
	public static final String IMPORT_ROW_RULE = "importRowRule";
	public static final String IMPORT_INIT_RULE = "importInitRule";
	public static final String IMPORT_GROUP_BY = "importGroupBy";
	public static final String IMPORT_MULTI_VALUE_FIELDS = "importMultiValueFields";
	public static final String IMPORT_LOGGER_NAME = "importLoggerName";
	public static final String IMPORT_LOGGER_LEVEL = "importLoggerLevel";
	public static final String IMPORT_MANUAL_HEADER = "importManualHeader";
	
	private static final String FIELD_DELIMITER = ",";
	
	private GenericImport genericImport;
	private Interpreter beanshell = null;
	private SailPointContext context = null;
	private Iterator<List<String>> genericImportIterator;
	private TaskResult taskResult = null;
	private Attributes<String, Object> attributes = null;
	

	// A cache for the row just in case we need to read ahead.
	private List<String> cachedRow = null;

	private final static Logger log = Logger.getLogger(GenericImportController.class);
	private String ruleLogContext = null;
	private Level ruleLogLevel = null;
	
	private Rule	ruleTransform;
	private Rule	ruleRow;
	private Rule	ruleInit;
	private Rule	ruleFinalize;

	private List<String> groupBy = new ArrayList<String>();		// Group the fields up per row
	private List<String> mvFields = new ArrayList<String>();
	private String	manualHeader = null;	

	public GenericImportController(GenericImport gi) throws GeneralException {

		if (log.isDebugEnabled()) log.debug("**Instantiating GenericImportController version: " + VERSION);
		
		genericImport = gi;
		ruleLogContext = GenericImportController.class.getName();
		
		if (log.isDebugEnabled()) log.debug("Rule logger context initialised as " + ruleLogContext);

		try {
			context = sailpoint.api.SailPointFactory.getCurrentContext();
		} catch (GeneralException e) {
			// Cannot get SailPoint's current context.
			throw new GeneralException(
					"Could not get current context in GenericImporter", e);
		}

		beanshell = new Interpreter();
		try {
			beanshell.eval("import sailpoint.services.task.genericImport.*;");
		} catch (EvalError e) {
			throw new GeneralException(
					"Somewhat unexpected, but there is a EvalError in AbstractGenericImport constructor",
					e);
		}

	}

	public TaskResult getTaskResult() {
		return taskResult;
	}


	/**
	 * Set a bespoke logger name and log out rules
	 * log output to the chosen logger
	 * 
	 * @param log
	 */
	public void setRuleLoggerContext(String log) {
		// SSDBUGS-23
		// Setting log to null created a NULL logger which is bad for
		// readability, fixed with NULL check as ruleLogCOntext is defaulted
		// to this class name in the Constructor.
		if (log != null) {
			this.ruleLogContext = log;
		}
	}
	
	public String getRuleLoggerContext() {
		return this.ruleLogContext;
	}
	
	
	public Level getRuleLoggerLevel() {
		return ruleLogLevel;
	}

	// Set the rule log level here
	public void setRuleLoggerLevel(Level ruleLogLevel) {
		this.ruleLogLevel = ruleLogLevel;
	}

	public void setTaskResult(TaskResult taskResult) {
		this.taskResult = taskResult;
	}

	public Attributes<String, Object> getTaskAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes<String, Object> attributes) {
		this.attributes = attributes;
		
		try {
			this.setRuleTransform(attributes.getString(IMPORT_TRANSFORM_RULE));
			this.setRuleFinalize(attributes.getString(IMPORT_FINALIZE_RULE));
			this.setRuleRow(attributes.getString(IMPORT_ROW_RULE));
			this.setRuleInit(attributes.getString(IMPORT_INIT_RULE));
		} catch (GeneralException e) {
			log.warn("Could not set a rule for the generic import: Init: " +
					(this.getRuleInit() == null ? "NULL" :this.getRuleInit().getName()) + ", Transform: " +
					(this.getRuleTransform() == null ? "NULL" :this.getRuleTransform().getName()) +", Row: " +
					(this.getRuleRow() == null ? "NULL" :this.getRuleRow().getName()) + ", Finalize: " +
					(this.getRuleFinalize() == null ? "NULL" :this.getRuleFinalize().getName()));
		}
		
		setRuleLoggerContext( attributes.getString(IMPORT_LOGGER_NAME) );
		
		String level = attributes.getString(IMPORT_LOGGER_LEVEL );
		if (level !=  null) {
			setRuleLoggerLevel(Level.toLevel(level.trim().toUpperCase(), Level.FATAL));
		}
				
		String mvFieldsValue = attributes.getString(IMPORT_MULTI_VALUE_FIELDS);
		String groupByValue = attributes.getString(IMPORT_GROUP_BY);
		this.manualHeader  = attributes.getString(IMPORT_MANUAL_HEADER);
		if (groupByValue != null) {
			this.setGroupBy(Arrays.asList(groupByValue.split(FIELD_DELIMITER)));
		}
		
		if (mvFieldsValue != null) {
			this.setMvFields(Arrays.asList(mvFieldsValue.split(FIELD_DELIMITER)));
		}
		
	}

	// Transform the schema and types from one value to another. No business
	// logic
	// should be here, just data type transformations
	private Attributes<String, Object> processTransform(
			Attributes<String, Object> row) throws GeneralException {

		Attributes<String,Object> transform = new Attributes<String,Object>();
		transform.putAll( row.mediumClone() );
		
		if (this.ruleTransform == null) {
			// just return the transform collection
			return transform;

		} else {
			// Pass to beanshell for the transformation
			try {
			
				Logger tmpLog = Logger.getLogger(ruleLogContext + ".Transform" );
				if (ruleLogLevel != null) tmpLog.setLevel(ruleLogLevel);
				
				beanshell.set("log", tmpLog );
				beanshell.set("context", context);
				beanshell.set("row", row);
				beanshell.set("taskResult", this.taskResult);
				beanshell.set("taskAttributes", this.attributes);
				beanshell.set("transform", transform);
			
				transform = (Attributes<String,Object>) beanshell
						.eval(this.ruleTransform.getSource());

				if (log.isDebugEnabled()) {					
					if (transform != null) {
						log.debug("Transformed row is:");
						for (String key : transform.getKeys()) {
							String value = null;
							if (transform.get(key) != null)
								value = transform.get(key).toString();
							log.debug("  Key: '" + key + "' Value: '" + value + "'");
						}
					} else {
						log.debug("Transformed row is NULL");
					}
				}
				
				return transform;
			} catch (EvalError e) {
				throw new GeneralException(beanshellErrorReport(e,"transformRow"), e);
			}
		}
	}

	private void processRow(Attributes<String, Object> row)
			throws GeneralException {

		if (this.ruleRow == null) {
			if (log.isDebugEnabled())
				log.debug("No processRow rule available.");
		} else {

			try {
				Logger tmpLog  = Logger.getLogger(ruleLogContext + ".Row" );
				if (ruleLogLevel != null) tmpLog.setLevel(ruleLogLevel);
				
				beanshell.set("log", tmpLog );
				beanshell.set("context", context);
				beanshell.set("row", row);
				beanshell.set("taskResult", this.taskResult);
				beanshell.set("taskAttributes", this.attributes);
			
				beanshell.eval(this.ruleRow.getSource());

			} catch (EvalError e) {
				throw new GeneralException(beanshellErrorReport(e,"processRow") , e);
			}
		}
	}

	private void processInit() throws GeneralException {


		if (this.ruleInit == null) {
			if (log.isDebugEnabled())
				log.debug("No initImport rule available.");
		} else {

			try {
				
				Logger tmpLog  = Logger.getLogger(ruleLogContext + ".Init" );
				if (ruleLogLevel != null) tmpLog.setLevel(ruleLogLevel);
				
				beanshell.set("log", tmpLog );
				beanshell.set("context", context);
				beanshell.set("taskResult", this.taskResult);
				beanshell.set("taskAttributes", this.attributes);
				beanshell.eval(this.ruleInit.getSource());

			} catch (EvalError e) {
				throw new GeneralException(beanshellErrorReport(e,"initImport"), e);
			}
		}
	}

	public void processFinalize() throws GeneralException {

		if (this.ruleFinalize == null) {
			if (log.isDebugEnabled())
				log.debug("No finalizeImport rule available.");
		} else {

			try {
				
				Logger tmpLog  = Logger.getLogger(ruleLogContext + ".Transform" );
				if (ruleLogLevel != null) tmpLog.setLevel(ruleLogLevel);
				
				beanshell.set("log", tmpLog );
				beanshell.set("context", context);
				beanshell.set("taskResult", this.taskResult);
				beanshell.set("taskAttributes", this.attributes);
				beanshell.eval(this.ruleFinalize.getSource());

			} catch (EvalError e) {
				throw new GeneralException(beanshellErrorReport(e,"finalizeImport"), e);
			}
		}
	}

	/**
	 * 
	 * @param e
	 * @param process
	 * @return
	 */
	private String beanshellErrorReport(EvalError e, String process) {
		
		String errorText = null;
		String errorLine = null;
		
		String message = null;
		
		try {
			errorText = e.getErrorText();
		} catch(Exception e2) {
			errorText = null;
		}
		try {
			errorLine = String.valueOf(e.getErrorLineNumber());
		} catch(Exception e2) {
			errorLine = "0";
		}
		
		if (errorText == null) errorText = "N/A";
		if (e.getCause() != null) {
			message = String.format("Evaluation error in '%s' Rule.  Line number: %s, '%s'.  Exception: %s.  Cause: %s", process,errorLine, errorText, e.getMessage(), e.getCause().getMessage());
		} else {
			message = String.format("Evaluation error in '%s' Rule.  Line number: %s, '%s'.  Exception: %s.", process,errorLine, errorText, e.getMessage());
		}
		
		return message;
	}
	
	/**
	 * 
	 * @throws GeneralException
	 */
	public void open() throws GeneralException {

		log.debug("Entering GenericImport open()");

		processInit();
		genericImport.open();
		genericImportIterator = genericImport.iterator();
		
		// We may override the schema using this statement
		if (manualHeader != null) {
			log.debug("Schema before overide changes:" + genericImport.getSchema().toString() );
			genericImport.getSchema().setSchema(manualHeader);
			log.debug("Schema after overide changes:" + genericImport.getSchema().toString() );
		}

		log.debug("Exiting GenericImport open()");

	}

	public boolean hasNext() {
		
		if (cachedRow != null ) return true;
		return genericImportIterator.hasNext();
	}

	/**
	 * Get the next item in the iterator
	 * This method also needs to group any rows which need to.
	 * 
	 * @return
	 * @throws GeneralException
	 */
	public Attributes<String, Object> next() throws GeneralException {

		log.debug("Entering next()");
		// Grab the current row from the iterator
		List<String> row  = null;
		Attributes<String,Object> rowWithSchema = null;
		
		Schema schema = genericImport.getSchema();
		
		if (this.cachedRow != null) {
			row = this.cachedRow;
			this.cachedRow = null;
		} else {
			row = genericImportIterator.next();
		}
		
		// Transform the row with the current schema
		if (row != null) {
			rowWithSchema = schema.applySchemaToRow(row);
		}
		
		// Should the importer group any fields
		if (groupBy.size() > 0) {
			// We should look to see if the next record is a group candidate
			// We can put the next row in the cachedRow variable
			
			log.debug("Checking groupBy");
			while (genericImportIterator.hasNext()) {
				
				this.cachedRow = (List<String>) genericImportIterator.next();
				Attributes<String,Object> cachedRowWithSchema = schema.applySchemaToRow(this.cachedRow);
				
				if (isGrouped(rowWithSchema, cachedRowWithSchema)) {
					
					rowWithSchema = groupRow(rowWithSchema, cachedRowWithSchema);
					// We don't want to cache the next row
					// because it's part of the present row... so NULL
					// triggers reading the next row on the next call.
					this.cachedRow = null;
				} else {
					break;
				}
			}
		}
		
		// DO we have anything here we need to lock?  The schema may generate objects so
		// anything which is a SailPoint Object which is persisted we will lock,
		if (rowWithSchema != null) {
			for (String key : rowWithSchema.keySet()) {
				
				
				if (rowWithSchema.get(key) != null && rowWithSchema.get(key) instanceof SailPointObject) {
			
					Class spoClass = null;
					
					// Because Hibernate is lazy, sometimes we get a proxy of the
					// class and not really the class.. so we have to find the truth
					// behind the class being dealt with here..
					
					if (rowWithSchema.get(key)  instanceof HibernateProxy){
						// Yep, it's lazy, get the real one... mmmokay?
				         HibernateProxy proxy = (HibernateProxy) rowWithSchema.get(key);
				         spoClass =  proxy.getHibernateLazyInitializer().getImplementation().getClass();
				      } else {
				    	  // real class, get the class object
				    	  spoClass = rowWithSchema.get(key).getClass();
				      }
					
					SailPointObject spo = (SailPointObject) rowWithSchema.get(key);
					
					String spoId = spo.getId();
					// Findout if the object has been persisted
					if (spoId != null && spoId.length() > 0) {
						
						// Yes, the object has been persisted because it has an ID, lets get the locked version.
						log.debug("Getting Lock on SailPoint Object Type=" + spoClass.getName() + ", Id=" + spoId);
						SailPointObject spoLocked = ObjectUtil.lockObject(context, spoClass, spoId, null, PersistenceManager.LOCK_TYPE_TRANSACTION);
						// Now put this reference back into the map
						rowWithSchema.put(key, spoLocked);
					}
				
				}
			}
		}
		
		// Transform and process the row
		Attributes<String, Object> transform = processTransform(rowWithSchema);
		if (transform != null) processRow(transform);
		
		// Need to finish the transaction for this context...
		// so decache() and roll back to make sure the session is now clean.
		
		// Rollback should also release the TRANSACTION locks..
		
		context.decache();
		context.rollbackTransaction();
		
		
		if (log.isDebugEnabled()) {
			log.debug("Exiting next() with " + (transform == null ? "NULL" : transform.toString()));
		}
		return transform;

	}	

	private Attributes<String, Object> groupRow(Attributes<String, Object> currentRow, Attributes<String, Object> newRow) throws GeneralException {

		// Copy the row into the newRow
		for (String fieldname : currentRow.getKeys()) {

			// Check to see if this is a multi valued field
			if (mvFields.contains(fieldname)) {
				List<Object> mvValue = null;
				if (newRow.containsKey(fieldname))
					mvValue = newRow.getList(fieldname);
				if (mvValue == null)
					mvValue = new ArrayList<Object>();
				
				
				List<Object> oldMvValue = currentRow.getList(fieldname);
				if (oldMvValue != null && oldMvValue.size() > 0) {
					mvValue.addAll(oldMvValue);
				}
				newRow.put(fieldname, mvValue);
			} else {
				newRow.put(fieldname, currentRow.get(fieldname));
			}
		}

		return newRow;

	}

	private boolean isGrouped(Attributes<String, Object> currentRow,
			Attributes<String, Object> nextRow) throws GeneralException {

		if (log.isDebugEnabled()) {
			log.debug("Entering isGrouped(");
			log.debug("  " + currentRow.toString());
			log.debug("  " + nextRow.toString() + " )");
		}
		
		// Validate the groupBy
		boolean groupRow = false;

		if (groupBy.size() > 0) {

			// Now find out if this should be grouped by checking
			// to see if the fields listed in the groupBy attribute are the same
			

			// Check to see if this is a grouped row
			
			for (String fieldname : groupBy) {

				if (currentRow.containsKey(fieldname) && nextRow.containsKey(fieldname)) {
									
					Object currentRowValue = currentRow.get(fieldname);
					Object nextRowValue = nextRow.get(fieldname);
	
					if ( (currentRowValue != null && nextRowValue != null && currentRowValue.equals(nextRowValue)) ||
							(currentRowValue == null && nextRowValue == null)) {
						
						groupRow = true;
						if (log.isDebugEnabled()) 
							log.debug("  Current value '" + currentRowValue
									+ "' and last value '" + nextRowValue
									+ "' are the same.");
						break;
					} else {
						
						groupRow = false;
						if (log.isDebugEnabled())
							log.debug("  Current values:' " + currentRowValue
									+ "' and last value '" + nextRowValue
									+ "' are not the same..");
					}
				}
			}

		} else {
			groupRow = false;
		}

		if (log.isDebugEnabled()) log.debug("Exiting isGrouped with " + groupRow);
		return groupRow;
	}

	public void close() throws GeneralException {

		log.debug("Entering GenericImporter close()");

		// Make sure we flush the iterator if the importer is closed
		// before finished iterating through the data set.
		Util.flushIterator(genericImportIterator);
		genericImport.close();

		log.debug("Exiting GenericImporter close()");
		
	}
	
	
	// Rules stuff
	/**
	 * Retrieve the rule object by name, used by
	 * the byName setters
	 * 
	 * @param ruleName
	 * @return	A SailPointObject Rule
	 * @throws GeneralException
	 */
	private Rule getRuleFromName(String ruleName) throws GeneralException {

		Rule rule = context.getObject(Rule.class, ruleName);
		return rule;
		
	}
	
	/** 
	 * Grab the Transform rule if one exists
	 */
	public Rule getRuleTransform() {
		return ruleTransform;
	}
	/**
	 * Set the transform rule by the rule name
	 */
	public void setRuleTransform(String ruleName) throws GeneralException {
		setRuleTransform(getRuleFromName(ruleName));
	}
	/**
	 * Set the transform rule by rule object
	 */
	public void setRuleTransform(Rule rule) {
		ruleTransform = rule;
	}
	/**
	 * Get the row rule is it exists
	 */
	public Rule getRuleRow() {
		return ruleRow;
	}
	/**
	 * Set the row rule by rule object
	 */
	public void setRuleRow(Rule rule) {
		ruleRow = rule;
	}
	/**
	 * Set the row rule by rule name
	 */
	public void setRuleRow(String ruleName) throws GeneralException {
		setRuleRow(getRuleFromName(ruleName));
	}
	/**
	 * get the Initialization rule if one exists
	 */
	public Rule getRuleInit() {
		return ruleInit;
	}
	/**
	 * Set the initialization rule by rule object
	 */
	public void setRuleInit(Rule rule) {
		ruleInit = rule;
	}
	/**
	 * Set the initialization rule by rule name
	 */
	public void setRuleInit(String ruleName) throws GeneralException {
		setRuleInit(getRuleFromName(ruleName));
	}
	/**
	 * Get the finalization rule if one exists
	 */
	public Rule getRuleFinalize() {
		return ruleFinalize;
	}
	/**
	 * Set the finalization rule by rule object
	 */
	public void setRuleFinalize(Rule rule) {
		ruleFinalize = rule;
	}
	/**
	 * Set the finalization rule by rule name
	 */
	public void setRuleFinalize(String ruleName) throws GeneralException {
		setRuleFinalize(getRuleFromName(ruleName));
	}
	
	// Group by and multi value setters
	/**
	 * Set the group by attributes
	 */
	public void setGroupBy(List<String> groupBy) {
		if (groupBy == null) {
			this.groupBy.clear();
		} else {
			this.groupBy = groupBy;
		}
	}
	
	/**
	 * Get the group by attributes.
	 * @return	List<String> object of at least 0 in size
	 */
	public List<String> getGroupBy() {
		return this.groupBy;
	}
	
	/**
	 * Get a list of multi valued fields
	 */
	public List<String> getMvFields() {
		return mvFields;
	}

	/**
	 * Set a list of Multi valued fields
	 */
	public void setMvFields(List<String> mvFields) {
		if (mvFields == null) {
			this.mvFields.clear();
		} else { 
			this.mvFields = mvFields;
		}
	}

}
