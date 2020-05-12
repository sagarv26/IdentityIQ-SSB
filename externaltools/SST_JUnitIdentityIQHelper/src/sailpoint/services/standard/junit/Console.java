package sailpoint.services.standard.junit;

import java.io.File;import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import bsh.EvalError;
import bsh.Interpreter;
import sailpoint.api.SailPointContext;
import sailpoint.api.TaskManager;
import sailpoint.api.Terminator;
import sailpoint.api.Workflower;
import sailpoint.object.ApprovalItem;
import sailpoint.object.ApprovalSet;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.QueryOptions;
import sailpoint.object.Rule;
import sailpoint.object.SailPointObject;
import sailpoint.object.TaskDefinition;
import sailpoint.object.TaskResult;
import sailpoint.object.WorkItem;
import sailpoint.server.Exporter.Cleaner;
import sailpoint.server.Importer;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

/**
 * Console class is to re-implement some of the IdentityIQ methods to be used by the Unit Tester
 * 
 * @author christian.cairney
 *
 */
public class Console {

	public static final String RULE_RETURN_VALUE = "beanshell_rule_return_value";
	
	private SailPointContext context;
	
	private static Logger log = Logger.getLogger(Console.class);
	
	/**
	 * Closable iterator interface which allows for next() with the view of a Hibernate decache.
	 * 
	 * @author christian.cairney
	 *
	 * @param <T>
	 */
	public interface ClosableDecachableIterator<T> extends Iterator<T> {
		public void close();
		public T nextWithDeache();
	}
	
	/**
	 * ObjectIterator allows for SailPointObjects to be iterated over without creating
	 * Hibernate cache and memory scaling issues the context.getObjetcs and context.search(class, QueryOptions) 
	 * method presents.
	 *  
	 * This iterator can be safely closed with any resource leaks.
	 *  
	 * @author christian.cairney
	 *
	 */
	public class ObjectIterator<T extends SailPointObject> implements ClosableDecachableIterator<T> {

		private Iterator<Object[]> iterator = null;
		private SailPointObject lastObject = null;
		private Class<? extends SailPointObject> clazz;
		
		/**
		 * Constructor to create the 
		 * @param clazz
		 * @param f
		 * @param uid
		 * @throws GeneralException
		 */
		public ObjectIterator(Class<? extends SailPointObject> clazz, Filter f, String uid) throws GeneralException {
			
			QueryOptions qo = new QueryOptions(f);
			log.debug("Creating object iterator  based on '" + f.getExpression() + "' and return field '" + uid + "'");
			Iterator<Object[]> it = context.search(clazz, qo, uid);
			this.clazz = clazz;
			this.iterator = it;
			
		}
		
		/**
		 * Check to see if any more objects are left to iterate over
		 */
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		/**
		 * Fetch the next object in the iterator
		 */
		@Override
		public T next() {
			String id = (String) iterator.next()[0];
			SailPointObject spo = null;
			lastObject = null;
			try {
				spo = context.getObject(this.clazz, id);
				lastObject = spo;
			} catch (GeneralException e) {
				log.error(e);
			}
			return (T) spo;
		}

		/**
		 * Fetch the next object in the iterator and automatically decache the
		 * previous object
		 * 
		 */
		public T nextWithDeache() {
			
			if (lastObject != null) {
				try {
					context.decache(lastObject);
				} catch (GeneralException e) {
					log.warn("Unable to decvache object " + lastObject.getId());
				}
			}
			return next();
		
		}
		
		/**
		 * Flush any remaining objects in the iterator and close
		 */
		public void close() {
			
			sailpoint.tools.Util.flushIterator(iterator);
			
			
		}
		
		/**
		 * Remove the current object (and any dependent objects)
		 * from the data store.
		 */
		@Override
		public void remove() {
			
			try {
				deleteObject(lastObject.getClass(), lastObject.getId());
			} catch (GeneralException e) {
				log.error("Could not remove object " + lastObject.getId());
			}
		}
		
	}
	
	/**
	 * Console constructor, SailPointContext required
	 * 
	 * @param console
	 */
	public Console(SailPointContext console) {
		this.context = console;
		log.setLevel(Level.DEBUG);
	}
	

	/**
	 * Run a SailPoint rule with an arguments map
	 * 
	 * @param rule			SailPointRule object
	 * @param arguments		Arguments map to pass to the rule
	 * @return				If the rule returns an object, it will be passed to this
	 * 						methods return variable
	 * @throws GeneralException
	 */
	public Object runRule(Rule rule, Map<String,Object> arguments) throws GeneralException {
		if (rule == null) throw new GeneralException("runRuleMethod must be supplied a rule object");
		return context.runRule(rule, arguments);	
	}
	
	/**
	 * Run a SailPoint rule without any arguments
	 * 
	 * @param rule
	 * @return
	 * @throws GeneralException
	 */
	public Object runRule(Rule rule) throws GeneralException {
		return runRule(rule, null);
	}
	
	
	/**
	 * Execute a method in a rule, such as a method in a rule library
	 * 
	 * The method parameter is the beanshell code used to evaluate the method call
	 * 
	 * The fetchVariablesList are the beanshell variables to be retrieved from the beanshell
	 * environment.  These variables along with the method call return variable will be passed
	 * to this methods returning Map.  The beanshell method return variable is stored in the
	 * map with the key from this classes constant: RULE_RETURN_VALUE
	 * 
	 * 
	 * @param rule				 SailPointRule object
	 * @param method			 Method is the beanshell which will be evaluated to execute the method.
	 * @param arguments			 Arguments map to pass to the rule
	 * @param fetchVariablesList List of variables which will be fetched from the beanshell environment
	 * @return					 Returns the list of variables fetched from the beanshell environment
	 * 							 along with any return variables from the method call.
	 * @throws GeneralException
	 */
	public Map<String,Object> runRuleMethod(Rule rule, String method, Map<String,Object> arguments, List<String> fetchVariablesList) throws GeneralException {
		
		if (rule == null) throw new GeneralException("runRuleMethod must be supplied a rule object");
		
		Interpreter beanshell = new Interpreter();
		Map<String,Object> returnVariablesMap = new HashMap<String,Object>();
		
		initBeanShell(beanshell, arguments);
		
		try {			
			// First off, evaluate the code so it's parsed, any side effects of running
			// code will be ignored
			evaluateRule(beanshell, rule, arguments);
		} catch (EvalError e) {
			throw new GeneralException("Rule source did not evaluate as expected",e);
		}
		try {
			// Now attempt to execute the method
			Object returnObject = beanshell.eval(method);
			returnVariablesMap.put(RULE_RETURN_VALUE, returnObject);
			
			if (fetchVariablesList != null) {
				for (String variableName : fetchVariablesList) {
					returnVariablesMap.put(variableName, beanshell.get(variableName));
				}
			}
			
		} catch (EvalError e) {
			throw new GeneralException("Rule method did not evaluate as expected",e);
		}
		return returnVariablesMap;
	}

	/**
	 * Execute a method in a rule, such as a method in a rule library
	 * 
	 * @param rule				 SailPointRule object
	 * @param method			 Method is the beanshell which will be evaluated to execute the method.
	 * @param arguments			 Arguments map to pass to the rule
	 * @return
	 * @throws GeneralException
	 */
	public Object runRuleMethod(Rule rule, String method, Map<String,Object> arguments) throws GeneralException {
		
		if (rule == null) throw new GeneralException("runRuleMethod must be supplied a rule object");
		
		Interpreter beanshell = new Interpreter();
		initBeanShell(beanshell, arguments);
		Object returnObject = null;
		
		try {			
			// First off, evaluate the code so it's parsed, any side effects of running
			// code will be ignored
			evaluateRule(beanshell, rule, arguments);
		} catch (EvalError e) {
			throw new GeneralException("Rule source did not evaluate as expected",e);
		}
		try {
			// Now attempt to execute the method
			returnObject = beanshell.eval(method);
			
		} catch (EvalError e) {
			throw new GeneralException("Rule method did not evaluate as expected",e);
		}
		return returnObject;
	}
	
	/**
	 * Initialise the beanshell interpreter
	 * 
	 * @param bsh
	 * @param arguments
	 * @throws GeneralException
	 */
	private void initBeanShell(Interpreter bsh, Map<String,Object> arguments) throws GeneralException {
		
		try {
			bsh.set("log", log );
			bsh.set("context", context );
			for (String key : arguments.keySet()) {
				bsh.set(key, arguments.get(key) );
			}
			
		} catch (EvalError e) {
			throw new GeneralException("Could not initialise the beanshell interpreter in initBeanShell", e);
		}
		
	}
	
	/**
	 * Evaluate the beanshell rule outside of the SailPointContext helper methods
	 * 
	 * @param bsh
	 * @param rule
	 * @param arguments
	 * @return
	 * @throws EvalError
	 */
	private Object evaluateRule(Interpreter bsh, Rule rule, Map<String,Object> arguments) throws EvalError {
		
		Object returnObject = null;
		List<Rule> referencedRules = rule.getReferencedRules();
        if (referencedRules != null) {
            for (Rule refRule : referencedRules) {
            	evaluateRule(bsh, refRule, arguments);
            }
        }
        returnObject = bsh.eval(rule.getSource());
        
        return returnObject;	
	}
	
	/**
	 * Get the single object from IdentityIQ, similar to the context.getObjectByName method except can handle wild 
	 * cards
	 * 
	 * @param className
	 * @param objectName
	 * @return
	 * @throws GeneralException
	 */
	public <T extends SailPointObject> T getObjectByName(Class<T> className, String objectName) throws GeneralException {
		
		Filter f = this.getFilterByName(objectName);
		if (f == null) throw new GeneralException("Object name '" + objectName + "' produced null filter");
		
		int count = context.countObjects(className,  new QueryOptions(f));
		if (log.isDebugEnabled()) log.debug("getObjectByName using filter '" + f.getExpression() + "', Found " + count + " objects.");
		if (count > 1) throw new GeneralException("Get object by name returned more than one object");
		
		ClosableDecachableIterator<T> it = new ObjectIterator<T>(className, f, "id");
		
		T spo = null;
		if (it.hasNext()) spo = it.next();;
		it.close();
		return spo;
		
	}
	
	/**
	 * Similar to the SailPointContext.getObjects method except it does not bloat the hibernate cache
	 * 
	 * @param clazz
	 * @param f
	 * @return
	 * @throws GeneralException
	 */
	public Iterator<SailPointObject> getObjects(Class<? extends SailPointObject> clazz, Filter f) throws GeneralException {
		
		return new ObjectIterator<SailPointObject>(clazz, f, "id");
		
	}
	
	/**
	 * Uses the terminator class to delete the objects and any associated objects
	 * 
	 * @param clazz
	 * @param objectUId
	 * @return
	 * @throws GeneralException
	 */
	public int deleteObject(Class<? extends SailPointObject> clazz, String objectUId) throws GeneralException {
		
		Filter f = getFilterByName(objectUId);
		int count = context.countObjects(clazz, new QueryOptions(f));
		if (count == 0) {
			f = getFilterById(objectUId);
			count = context.countObjects(clazz, new QueryOptions(f));
		}
		return deleteObject(clazz, f);
		
	}
	
	/**
	 * Uses the terminator class to delete the objects and any associated objects
	 * 
	 * @param clazz
	 * @param filter
	 * @return
	 * @throws GeneralException
	 */
	public int deleteObject(Class<? extends SailPointObject> clazz, Filter filter) throws GeneralException {
		
		Terminator terminator = new Terminator(context);
		QueryOptions qo = new QueryOptions(filter);
		
		int countObjects = context.countObjects(clazz, qo);
		terminator.deleteObjects(clazz, qo);
		return countObjects;
		
	}
	
	/**
	 * Run a task in this IDEs' instance of IdentityIQ
	 * 
	 * @param taskDefinition		TaskDefinition object to execute
	 * @return
	 * @throws GeneralException
	 */
	public TaskResult runTask(TaskDefinition taskDefinition) throws GeneralException {
		
		return runTask(taskDefinition, null);

	}
	
	/**
	 * Run a task in this IDEs' instance of IdentityIQ
	 * 
	 * @param taskDefinition		TaskDefinition object to execute
	 * @param args					Map of the arguments for this task
	 * @return						TaskResult
	 * @throws GeneralException
	 */
	public TaskResult runTask(TaskDefinition taskDefinition, Map<String,Object> args) throws GeneralException {
		
		TaskManager taskManager = new TaskManager(context);
        TaskResult result = taskManager.runSync(taskDefinition, args);
        

        return result;
        
  
	}
	
	/**
	 * Set a workitem state
	 * 
	 * state = State.Finished == Approved
	 * state = State.Rejected == Rejected
	 * 
	 * @param workitemName
	 * @param comment
	 * @throws Exception
	 */
	public void setWorkitemState(String workitemUid, String comment, WorkItem.State state, String approverName) throws GeneralException {
		
		   
		WorkItem item = this.context.getObject(WorkItem.class, workitemUid);
		Identity approver =  context.getObject(Identity.class, approverName);
		
		if (approver == null) throw new GeneralException("COuld not find approver " + approverName);
		if (item == null) throw new GeneralException("Could not find workitem id/name: " + workitemUid);
		if (item.getState() != null) throw new GeneralException("Work item is already completed for id/name: " + workitemUid);
		if (state == null) throw new GeneralException("Must supply a workitem state");
		
		item.setState(WorkItem.State.Finished);
		if (comment != null && comment.length() > 0) item.setCompletionComments(comment);
	
		ApprovalSet aset = item.getApprovalSet();
		if (aset != null) {
			 List<ApprovalItem> items = aset.getItems();
		     if (items != null) {
		    	 for (ApprovalItem appitem : items) {
		    		 if (appitem.getState() == null) {
						appitem.setState(state);
						appitem.setApprover( approver.getName() );
		             }
		    	 }
		     }
		}

		Workflower flower = new Workflower(context);
		flower.process(item, true);
	}
   
	/**
	 * Read a file as a single string
	 * 
	 * @param filename			File reference passed as a string.
	 * @return
	 * @throws GeneralException
	 */
	public String readFile(String filename) throws GeneralException {
		
		return readFile(new File(filename));
		
	}
	
	/**
	 * Read a file as a single string
	 * 
	 * @param file				File reference passed as a File object
	 * @return
	 * @throws GeneralException
	 */
	public String readFile(File file) throws GeneralException {
		
		if (file.exists()) {
			String fileContents = Util.readFile(file);
			return fileContents;
		} else {
			throw new GeneralException("File not found");
		}
	}
	
	/**
	 * Write a string out to file.
	 * 
	 * @param filename			File reference passed as a string.
	 * @param text				Text to write out
	 * @throws GeneralException
	 */
	public void writeFile(String filename, String text) throws GeneralException {
		
		writeFile(new File(filename), text);
		
	}
	
	/**
	 * Write a string out to file.
	 * 
	 * @param file`				File reference passed as a File object
	 * @param text				Text to write out
	 * @throws GeneralException
	 */
	public void  writeFile(File file, String text) throws GeneralException {
		Util.writeFile(file.getAbsolutePath(), text);
	}

	/**
	 * Import an XML string into IdentityIQ
	 * 
	 * @param xml					 String based XML object to import
	 * @param removeIds				 Boolean to scribe the ID's before importing.
	 * @param enableRoleChangeEvents Enable/Disable role change events
	 * @param autoCreate			 Auto create auxiliary objects
	 * @param strictReferences		 Reference objects must exist before import
	 * @throws Exception
	 */
	public void importObject(String xml, boolean removeIds, boolean enableRoleChangeEvents, boolean autoCreate, boolean strictReferences) throws Exception {

		Importer importer = new Importer(context);
		importer.setScrubIds(removeIds);
		importer.setRolePropEnabled(enableRoleChangeEvents);
		importer.setAutoCreate(autoCreate);
		importer.setStrictReferences(strictReferences);
		importer.importXml(xml);
			
	}
	
	/**
	 * Export a SailPointObject from the repository.  This method will return a String of the fully
	 * re-hydrated object from hibernate.
	 * 
	 * Pass the SailPointObject to return the xml of the object as a string.
	 * 
	 * @param spObject			SailPointObject to serialise as XML
	 * @return					XML of SailPointObject
	 * @throws GeneralException
	 */
	public String exportObject(SailPointObject spObject) throws GeneralException {
		return exportObject(spObject, false, (String[]) null);
	}
	
	/**
	 * Export a SailPointObject from the repository.  This method will return a String of the fully
	 * re-hydrated object from hibernate.
	 * 
	 * Pass the SailPointObject to return the xml of the object as a string.
	 * 
	 * @param spObject			SailPointObject to serialise as XML
	 * @param clean				Set to "true" to clean the object before rendering as XML
	 * @return					XML of SailPointObject
	 * @throws GeneralException
	 */
	public String exportObject(SailPointObject spObject, boolean clean) throws GeneralException {
		return exportObject(spObject, clean, (String[]) null);
	}
	
	/**
	 * Export a SailPointObject from the repository.  This method will return a String of the fully
	 * re-hydrated object from hibernate.
	 * 
	 * Pass the SailPointObject to return the xml of the object as a string.
	 * 
	 * @param spObject			SailPointObject to serialise as XML
	 * @param clean				Set to "true" to clean the object before rendering as XML
	 * @param cleanArgs			Variable set of options to remove properties of the object before rendering
	 * 							as XML
	 * @return
	 * @throws GeneralException XML of SailPointObject
	 */
	public String exportObject(SailPointObject spObject, boolean clean, String... cleanArgs) throws GeneralException {

		
		List<String> cleanArgsList = null;
		if (clean && cleanArgs == null) {
			cleanArgsList = new ArrayList<String>(Arrays.asList("id", "created", "modified", "targetId", "assignedScopePath", "policyId", "assignmentId", "roleId", "identityId"));
		} else if (clean) {
			cleanArgsList = Arrays.asList(cleanArgs);
		}
		
		String xml = spObject.toXml();
        if (clean) {
            Cleaner cleaner = new Cleaner(cleanArgsList);
            xml = cleaner.clean(xml);
        }
        return xml;
	}
	
	
	/**
	 * Return an object filter by name.  Wild cards are allowed, and will
	 * create a Like MatchMode.START type filter.
	 * 
	 * 		getFilterByName("nameOfObject");
	 * 		getFilterByName("nameOf*");
	 * 
	 * @param name	Name of object to produce filter on, wild cards allowed
	 * @return
	 */
	public Filter getFilterByName(String name) {
		
		Filter f = null;
		if (name.endsWith("*")) {
			if (name.length() == 1) { 
				f = null;
			} else {
				f = Filter.like("name", name.substring(0, name.length()-1), Filter.MatchMode.START);
			}
		} else {
			f = Filter.eq("name", name);
		}
		return f;
	}
	
	/**
	 * Return a filter by ID
	 * 
	 * @param id
	 * @return
	 */
	public Filter getFilterById(String id) {
		return Filter.eq("id", id);
	}
	
	/**
	 * Return a filter to fetch a Managed Attribute
	 * 
	 * @param applicationName
	 * @param attribute
	 * @param value
	 * @return
	 */
	public Filter getFilterManagedAttribute(String applicationName, String attribute, String value) {
		List<Filter> and = new ArrayList<Filter>();
		and.add(Filter.eq("application.name", applicationName));
		and.add(Filter.eq("attribute", attribute));
		and.add(Filter.eq("value", value));
		return Filter.and(and);
	}

}
