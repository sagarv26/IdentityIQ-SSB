package sailpoint.services.standard.junit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.After;
import org.junit.Before;

import bsh.EvalError;
import bsh.Interpreter;
import sailpoint.api.SailPointContext;
import sailpoint.api.TaskManager;
import sailpoint.api.Terminator;
import sailpoint.object.Filter;
import sailpoint.object.QueryOptions;
import sailpoint.object.Rule;
import sailpoint.object.SailPointObject;
import sailpoint.object.TaskDefinition;
import sailpoint.object.TaskResult;
import sailpoint.tools.GeneralException;

/**
 * JUnit helper, must reference the correct identityiq.jar file AND 
 * the class path is set to the WEB-INF\class folder in the same version of IdentityIQ.
 * 
 * Class will expose the following variables to the extending class
 * 
 *  	context 	- 	SailPointContext
 *  	console		-	Console helper class
 *  
 * @author christian.cairney
 *
 */
public class SailPointJUnitTestHelper {

	private SailPointExternalContext ctx = null;
	private Logger log;
	protected SailPointContext context;
	protected Console console;
	
	/**
	 * Constructor to be called from your JUnit class
	 * 
	 * 		super(username, password);
	 * 
	 * 
	 * @param username		SailPoint IdentityIQ username for instance
	 * @param password		SailPoint IdentityIQ password for instance
	 * @throws Exception
	 */
	public SailPointJUnitTestHelper(String username, String password) throws Exception {
		
		ctx = new SailPointExternalContext(username,password);
		
		// Default logger config
		//Logger.getRootLogger().getLoggerRepository().resetConfiguration();
		ConsoleAppender consoleAppender = new ConsoleAppender(); //create appender
		//configure the appender
		String loggerPattern = "%p|%C{1}|%M] %m%n";
		consoleAppender.setLayout(new PatternLayout(loggerPattern)); 
		consoleAppender.setThreshold(Level.DEBUG);
		consoleAppender.activateOptions();
		//add appender to any Logger (here is root)
		Logger.getRootLogger().removeAllAppenders();
		Logger.getRootLogger().addAppender(consoleAppender);
		
		log = Logger.getLogger(this.getClass().getPackage().getName());;
		log.setLevel(Level.TRACE);
		  
	}
		
	/*
	 * Before method will startup IdentityIQ here.
	 */
	@Before
	public void before() throws Exception {
		
		ctx.start();
		context = ctx.getSailPointContext();
		console = new Console(context);

	}
	
	/**
	 * Return a folder path to the class package test data.
	 * 
	 * @return
	 */
	public String getTestDataFolder() {
		
		String unitTest = "unittest/" +  this.getClass().getPackage().getName().replace(".", "/") + "/testdata/";
		return unitTest;
	}
	
	/**
	 * Set the class log level
	 * 
	 * @param clazz		Class object to set level
	 * @param level		Log4j LEVEL
	 */
	public void setClassLogLevel(Class clazz, Level level) {
		
		log = Logger.getLogger(clazz);
		log.setLevel(level);
		
	}
	
	/**
	 * Set the class log level
	 * 
	 * @param clazz		String representation of the class
	 * @param level		Log4j LEVEL
	 */
	public void setClassLogLevel(String clazz, Level level) {
		
		log = Logger.getLogger(clazz);
		log.setLevel(level);
		
	}
	
	/**
	 * Get the SailPointContext, already exposed as the context
	 * variable to classes extending this class.
	 * 
	 * @return
	 * @throws GeneralException
	 */
	public SailPointContext getContext() throws GeneralException {
		return ctx.getSailPointContext();
	}

	/*
	 * Shutdown IdentityIQ
	 */
	@After
	public void after() {
			
		
		log.debug("**** Clearing up. ****");
		if (ctx != null) ctx.close();
	}
	
	
	/*
	 * Output message to indicate the start of a test
	 * @param message
	 */
	private void logStartEndTest(String message) {
		
		if (log.isDebugEnabled()) {
			log.debug("**"  + message + " **");
		}
		
	}
	
	/**
	 * Method used at a start of the test, used to 
	 * output to the log in friendly format
	 * 
	 */
	public void startTest() {
		
		logStartEndTest("Starting test " + getTestName());

	}
	

	/**
	 * Method used at a end of the test, used to 
	 * output to the log in friendly format
	 * 
	 */
	public void endTest() {

		logStartEndTest("Finished test " + getTestName());

	}
	
	/**
	 * Derive the test name from the class method being executed.
	 * 
	 * @return
	 */
	public String getTestName() {
		
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String testName = "*Unknown*";
		
		boolean foundThisClassInTrace = false;
		for (int i=0; i < stackTraceElements.length; i++) {
	
			StackTraceElement ste = stackTraceElements[i];
			
			log.trace(i + " " + ste.getClassName() + " " + ste.getMethodName() );
			
			if (ste.getClassName().equals(SailPointJUnitTestHelper.class.getCanonicalName())) {
				foundThisClassInTrace = true;
			} else if (foundThisClassInTrace) {
			
				StackTraceElement steDebug = stackTraceElements[i];
				testName = steDebug.getMethodName() + " (" + steDebug.getClassName() + ")";
				break;
				
			}
		}
		
		return testName;
	}
	
}
