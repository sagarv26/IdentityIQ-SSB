package sailpoint.services.standard.emailnotifier.enhanced;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.EmailNotifier;
import sailpoint.api.ObjectUtil;
import sailpoint.api.PersistenceManager;
import sailpoint.api.SailPointContext;
import sailpoint.object.Configuration;
import sailpoint.object.Custom;
import sailpoint.object.EmailOptions;
import sailpoint.object.EmailTemplate;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.IdentityRequest;
import sailpoint.object.ObjectConfig;
import sailpoint.object.QueryOptions;
import sailpoint.object.Rule;
import sailpoint.object.SailPointObject;
import sailpoint.object.WorkItem;
import sailpoint.object.WorkflowCase;
import sailpoint.server.SMTPEmailNotifier;
import sailpoint.tools.EmailException;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

/**
 * Enhanced Email Notifier class.
 * 
 * The System Configuration needs to be modified to take advantage of this
 * class:
 * 
 * <code>
 * <Configuration name="SystemConfiguration">
 *  <Attributes>
 * 	 <Map>
 * 	  ...
 * 	  <entry key="emailNotifierClass" value="sailpoint.services.standard.emailnotifier.enhanced.EnhancedNotifier" />
 *    ....
 * </code>
 *
 * @author christian.cairney
 * @version 1.0
 */
public class EnhancedNotifier implements EmailNotifier {

	private boolean sendImmediate = true;
	
	// Email options attribute set to true when the email is being sent
	// from the queue. ignoreTemplateCheck will be raised in the TaskExecutor
	// before passing back to the email handler.
	private static final String ATTR_EMAIL_OPTION_IGNORE_TEMPLATE_CHECK = "ignoreTemplateCheck";
	
	// The original email template name is persisted in the email options, which is referred to
	// in the checks to see if the email should be summarized
	private static final String ATTR_EMAIL_OPTION_ORIGINAL_TEMPLATE_NAME = "originalTemplateName";
	// Summary rule arguments
	private static final String RULE_ARG_EMAIL_TEMPLATE = "emailTemplate";
	private static final String RULE_ARG_EMAIL_ADDRESS = "emailaddress";
	private static final String RULE_ARG_IDENTITY = "identity";
	private static final String RULE_ARG_EMAIL_OPTIONS = "emailOptions";
	private static final String RULE_ARG_CONTEXT = "context";

	// Summary object attributes
	public static final String SUMMARY_MAP_ATTR_SEND_EMAIL_IMMEDIATELY = "sendImmediately";
	public static final String SUMMARY_MAP_ATTR_SUMMARY_TO = "to";
	public static final String SUMMARY_MAP_ATTR_SUMMARIES = "summaries";
	public static final String SUMMARY_MAP_CREATED_DATE = "createdDate";
	public static final String SUMMARY_MAP_ORIGINAL_TEMPLATE_NAME = ATTR_EMAIL_OPTION_ORIGINAL_TEMPLATE_NAME;
	
	// Name of the Enhanced configuration object
	public static final String CONFIG_OBJECT_NAME = "Enhanced Email Notifier";
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Dynamic email template settings
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Rule name to programmatically augment the template name
	public static final String CONFIG_ATTRIBUTE_DYNAMIC_EMAIL_RULE_NAME = "dynamicEmailRuleName";
	// Entry to hold the identity attribute to augment the email template name
	public static final String CONFIG_ATTRIBUTE_DYNAMIC_EMAIL_IDENTITY_ATTRIBUTE = "dynamicEmailIdentityAttribute";
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Summary email template settings
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Summary filter should be represented as a map in the config
	public static final String CONFIG_ATTRIBUTE_SUMMARY_RULE= "summaryFilterRule";
	// List of email templates used to scope the template for summarization
	public static final String CONFIG_ATTRIBUTE_SUMMARY_TEMPLATE_FILTER_MAP = "summaryTemplateMap";
	// Entry to hold the email template used to render the summary information
	public static final String CONFIG_ATTRIBUTE_SUMMARY_EMAIL_TEMPLATE_NAME = "summaryEmailTemplateName";
	// The server base URL
	public static final String CONFIG_ATTRIBUTE_SUMMARY_SERVER_ROOT_PATH = "serverRootPath";
	
	// Default server root path if not available in configs
	public static final String DEFAULT_SUMMARY_SERVER_ROOT_PATH = "http://localhost:8080/identityiq";

	// Summary filter maps
	public static final String SUMMARY_RENDER_HINT_ATTRIBUTE_TITLE = "title";
	public static final String SUMMARY_RENDER_HINT_ATTRIBUTE_COLUMNS = "columns";
	public static final String SUMMARY_RENDER_HINT_ATTRIBUTE_COLUMNS_NAME = "name";
	public static final String SUMMARY_RENDER_HINT_ATTRIBUTE_COLUMNS_LABEL = "label";
	public static final String SUMMARY_RENDER_HINT_ATTRIBUTE_COLUMNS_VALUE = "value";
	// When email summary information is stored, the custom objects prefix is
	// here
	public static final String SUMMARY_NAME_PREFIX = "@EMAIL_SUMMARY_FOR_";

	// Our friend, the logger
	private static final Log log = LogFactory.getLog(EnhancedNotifier.class);
	
	private boolean isDynamicEmailTemplateConfigured = false;
	private String dynamicEmailAttributeName = null;
	private Rule dynamicEmailRule = null;

	// Filter rule returns a null or boolean to determine if the email should be summerized
	private Rule summaryFilterRule = null;
	
	//private Map<String, Object> summaryFilters = null;
	private EmailTemplate summaryEmailTemplate = null;

	/**
	 * Default Constructor
	 */
	public EnhancedNotifier() {

	}

	private Rule getDynamicEmailRule() {
		return dynamicEmailRule;
	}
	
	/**
	 * Will return true of the dynamic attribute configuration
	 * has been setup correctly
	 * 
	 * @return
	 */
	private boolean isDynamicEmailTemplateConfigured() {
		return isDynamicEmailTemplateConfigured;
	}


	/**
	 * Returns the Identity Attribute used to dynamically select
	 * an email template
	 * 
	 * @return
	 */
	private String getDynamicEmailAttributeName() {
		return dynamicEmailAttributeName;
	}
	

	private Rule getSummaryFilterRule() {
		return summaryFilterRule;
	}

	private Map<String, Object> getSummaryFilters(Configuration config) throws GeneralException {
		
		Object obj = config.get(CONFIG_ATTRIBUTE_SUMMARY_TEMPLATE_FILTER_MAP);
		Map<String,Object> summaryFilters = null;
		if (obj instanceof Map) {
			summaryFilters = (Map<String,Object>) obj;
		}
		return summaryFilters;
	}

	private EmailTemplate getSummaryEmailTemplate() {
		return summaryEmailTemplate;
	}

	/**
	 * Send any email notifications, this method will split up the "to" variable
	 * in the main message and send out a single email to all addressee's using
	 * their preferred language settings
	 * 
	 * @param context
	 *            SailPoint Context
	 * @param template
	 *            EmailTemplate
	 * @param options
	 *            EmailOptions
	 * @throws GeneralException
	 * @throws EmailException
	 */
	public void sendEmailNotification(SailPointContext context, EmailTemplate template, EmailOptions options)
			throws GeneralException, EmailException {

		log.debug("Entering sendEmailNotification");
		
		init(context);
		
		if (template == null)
			throw new GeneralException("Template is null, must have a instantiated temnplate");

		// Step 1: Derive email addresses
		if (template.getTo() == null)
			throw new GeneralException("Template must have a 'To' parameter set");
		
		//
		// Split the to filed if there is a delimiter
		//
		List<String> emailAddresses = Arrays.asList(template.getTo().split(","));
		if (log.isDebugEnabled()) log.debug("Parsed email addresses to : " + emailAddresses.toString());

		// Add the original email template name so it can be detected later
		// This can be changed to whatever the dynamic email rule decides.
		options.setVariable(EnhancedNotifier.ATTR_EMAIL_OPTION_ORIGINAL_TEMPLATE_NAME, template.getName());
		
		// Now iterate through all the email addresses
		for (String emailAddress : emailAddresses) {

			EmailTemplate selectedTemplate = template;
			options.setTo(emailAddress);
			if (isDynamicEmailTemplateConfigured()) {
				
				// See if there is a post fix value from the desired identity attribute
				
				Identity identity = null;
				if (getDynamicEmailAttributeName() != null) { 
					identity = EnhancedNotifier.getIdentityByEmailAddress(context, emailAddress);

					// Return an augmented template if available
					selectedTemplate = selectDynamicTemplateTemplate(context, selectedTemplate, options, identity);
					if (identity != null) context.decache(identity);
				}
			}
			
			// Recompile the template
			EmailTemplate email = selectedTemplate.compile(context, context.getConfiguration(), options);

			if (log.isDebugEnabled()) {
				log.debug("  Email template after compiling: " + email.toXml());
				log.debug("  Options are: ");
				log.debug("    To: " + options.getTo());

				java.util.Map<String, Object> vars = options.getVariables();

				if (vars != null)
					log.debug("    Variables:");
				for (String key : vars.keySet()) {
					log.debug("      " + key + " = " + vars.get(key));
				}
			} 

			// Step 5: Send the email
			sendEmail(context, email, options);

		}
	}

	/**
	 * sendEmail method is to encapsulate the different ways this wrapper will
	 * send email's. Currently it supports the OOTB SMTPEmailNotifier
	 * 
	 * @param context
	 * @param selectedTemplate
	 * @param options
	 * @throws EmailException
	 * @throws GeneralException
	 */
	@SuppressWarnings("boxing")
	private void sendEmail(SailPointContext context, EmailTemplate selectedTemplate, EmailOptions options)
			throws EmailException, GeneralException {

		log.debug("Entering sendEmail");


		// Need to figure out if we are going to push the email out
		// or queue it for a task to pick up on

		boolean sendEmail = true;

		// Check for the existence of the ignore template check, as this call may be coming
		// from the EmailQueueProcessor class... don't want to end up going in a loop here
		// and keep on re-sending to the queue. Don't care what the value is, just check
		// for it's existence.

		if (!options.getVariables().containsKey(ATTR_EMAIL_OPTION_IGNORE_TEMPLATE_CHECK)) {

			// If not set to ignore, grab a summary of the email first
			
			Map<String,Object> results = summerizeEmail(context, options);
			if (results == null) {
				sendEmail = true;
			} else {
				Object result =  results.get(EnhancedNotifier.SUMMARY_MAP_ATTR_SEND_EMAIL_IMMEDIATELY);
				if (result != null && (result.getClass().equals(boolean.class) || result instanceof Boolean)) {
					sendEmail = (Boolean) result;
				}
			}
		}

		if (sendEmail) {

			// Render to SMTP
			EmailNotifier smtpEmail = new SMTPEmailNotifier();
			if (log.isDebugEnabled()) log.debug("Sending Template to SMTPEmailNotifier: " + selectedTemplate.toXml());
				
			smtpEmail.sendEmailNotification(context, selectedTemplate, options);
			
		}
		log.debug("Exiting sendEmail");

	}


	/**
	 * Check to see if a new template name should be used, first run a configured rule if any then 
	 * check to see if the identity attribute should also augment it.
	 * 
	 * @param template
	 * @param value
	 * @return
	 * @throws GeneralException
	 */
	private EmailTemplate selectDynamicTemplateTemplate(SailPointContext context, EmailTemplate template, EmailOptions options, Identity identity) throws GeneralException {

		log.debug("Entering selectDynamicTemplate");
		
		if (template == null) throw new GeneralException("EmailTemplate is null");
		if (context == null) throw new GeneralException("SailPointContext is null");
		
		String templateName = template.getName();
		EmailTemplate returnTemplate = template;
		

		// First see if the template needs augmenting via a rule
		Rule rule = getDynamicEmailRule();
		if (rule != null) {
			
			log.debug("Evaluating dynamic rule");
			if (log.isDebugEnabled()) log.debug("Rule name " +  rule.getName());
			
			Map<String,Object> args = new HashMap<String,Object>();
			args.put(RULE_ARG_EMAIL_TEMPLATE, template);
			args.put(RULE_ARG_EMAIL_OPTIONS, options);
			args.put(RULE_ARG_CONTEXT, context);
			args.put(RULE_ARG_EMAIL_ADDRESS, options.getTo());
			args.put(RULE_ARG_IDENTITY, identity);
			
			Object value = context.runRule(rule, args);
			if (value != null && value instanceof String) {
				
				// Check to see if the template exists, if so we'll continue with that name
				if (objectExists(context,EmailTemplate.class, (String) value)) {
					templateName = (String) value;
					// Update the base original email name
					options.setVariable(EnhancedNotifier.ATTR_EMAIL_OPTION_ORIGINAL_TEMPLATE_NAME, templateName);
				}
				
			} else {
				throw new GeneralException("Values return from rule name " + rule.getName() + " is not a string, expecting a template name.");
			}
			
			log.debug("Finished evaluating dynamic rule");
			
		}
		
		// Check to see if we need to grab an identity attribute value to augment the template name
		String value = null;
		if (identity != null) {
	
			log.debug("Attribute name evaluation");
			String attributeName = getDynamicEmailAttributeName();
			if (log.isDebugEnabled()) log.debug("Test for dynamic email name based on identity attribute: " + attributeName);
			
			if (attributeName != null) value = (String) identity.getAttribute(attributeName);
			
			if (value != null) {
				
				// Check to see if a template exists with the augmented name
				String name = templateName.concat("_").concat(value);
				if (objectExists(context, EmailTemplate.class, name)) templateName = name;
			}
			
			if (templateName.equals(template.getName())) {
				returnTemplate = template;
			} else {
				// Query to see if the template exists
				returnTemplate = context.getObjectByName(EmailTemplate.class, templateName);
			}
		}
		log.debug("Exiting selectDynamicTemplate with :" + returnTemplate.getName());
		return returnTemplate;
	}

	/**
	 * Used for Testing
	 */
	public Boolean sendImmediate() {
		return sendImmediate;
	}

	/**
	 * Used for Testing
	 */
	public void setSendImmediate(Boolean value) {
		sendImmediate = value;
		
	}

	/**
	 * 
	 * @param context
	 * @param template
	 * @param options
	 * @return
	 * @throws GeneralException
	 */
	private Map<String,Object> summerizeEmail(SailPointContext context,  EmailOptions options) throws GeneralException {
		
		String originalEmailTemplateName = options.getString(EnhancedNotifier.ATTR_EMAIL_OPTION_ORIGINAL_TEMPLATE_NAME);
		Configuration config = EnhancedNotifier.getConfiguration(context);
		if (log.isDebugEnabled()) log.debug("Entering summerizeEmail with template name: " + originalEmailTemplateName);
		Map<String,Object> summaryEmailTemplates = this.getSummaryFilters(config);
		Rule summaryRule = this.getSummaryFilterRule();
		
		Map<String,Object> summaryMap = null;
		String summaryTemplateSettingsKey = null;
		
		Boolean inSummaryList = false;
		
		if (summaryEmailTemplates != null) {
			for (String summaryEmailTemplate : summaryEmailTemplates.keySet()) {
				
				
				if (log.isTraceEnabled()) log.trace("Checking '" + summaryEmailTemplate + " == " + originalEmailTemplateName);
				
				// We have a template passed, but the signature of this email should be the one
				// specified in the options map which indicates what the original template is
				
				if (originalEmailTemplateName.equals(summaryEmailTemplate)) {
					if (log.isDebugEnabled()) log.debug("Matched " + summaryEmailTemplate + " with " + originalEmailTemplateName );
					inSummaryList= true;
					summaryTemplateSettingsKey = summaryEmailTemplate;
					break;
				}
			}
		}
		
		if ( (summaryRule != null) || (inSummaryList) ) {
			
			// Create a map of the variables which will be summerized
			Map<String,Object> entries = generateSummaryMap(context, options);

			// Next see if we need to augment this
			if (summaryRule != null) {
				
				Map<String,Object> args = new HashMap<String,Object>();
				args.put("emailAddress", options.getTo());
				args.put("summaryMap", entries);
				args.put("inSummaryMap", inSummaryList);
				args.put("identity", EnhancedNotifier.getIdentityFromEmailAddress(context, options.getTo()));
				args.put("log", log);
				args.put("context",context);
				
				
				Object result = context.runRule(summaryRule, args);
				if (result == null) {
					log.debug("Summary Rule returned nothing, ignoring summarizing this email");
				} else if (result instanceof Map) {
					
					summaryMap = (Map<String,Object>) result;
					
					// Check to see if the config has a rendering option for this
					// email class, if not.. create one
					checkConfigForRenderHints(context, summaryTemplateSettingsKey, summaryMap);
					
					storeSummerizeEmail(context, summaryMap);
				}
				
			} else if (inSummaryList) {
				// We are in the summary list
				summaryMap = entries;
				checkConfigForRenderHints(context, summaryTemplateSettingsKey, summaryMap);
				storeSummerizeEmail(context, summaryMap);	
			}
			
		} else {
			log.debug("Email not in filter list and summary rule not configured.");
		}
		
		log.debug("Exiting summerizeEmail with map: " + (summaryMap != null ? summaryMap.toString() :  "NULL"));
		return summaryMap;
	}
	
	// Generate the summary map from the email options
	// this needs to filter SailPoint objects which are not persistable
	private Map<String,Object> generateSummaryMap(SailPointContext context,EmailOptions options) {
		
		// Create a map of the variables which will be summerized
		Map<String,Object> entries = new HashMap<String,Object>();
		
		entries.put(EnhancedNotifier.SUMMARY_MAP_ATTR_SEND_EMAIL_IMMEDIATELY, false);
		entries.put(EnhancedNotifier.SUMMARY_MAP_ATTR_SUMMARY_TO , options.getTo());
		
		Map<String,Object> variables = options.getVariables();
		
		for (String key : variables.keySet()) {
			
			Object obj = variables.get(key);
			if (obj != null) {
				
				
				
				//
				// Changed object check to AbstractXmlObject
				// instead of just SailPointObject to check to see if it
				// can be persisted.
				//
				
				if (log.isDebugEnabled()) log.debug("Processing variable " + key + " = " +  obj.toString());
				if (obj instanceof sailpoint.tools.xml.AbstractXmlObject
						|| obj instanceof String
						|| obj instanceof Integer
						|| obj instanceof Long
						|| obj instanceof Date
						|| obj instanceof List
						|| obj instanceof Map) {
					
					// also check if its a sailpoint object, if so ...rehydrate it
					if (obj instanceof sailpoint.tools.xml.AbstractXmlObject) {
						
						log.debug("Found Persistable SailPointObject");
						SailPointObject spo = (SailPointObject) obj;
						if (spo != null) {
							try {
								log.debug("Attempting to re-attached object to session");
								context.attach(spo);
								log.debug("Object re-attached.  Attempting to rehydrate object");;
								String xml = spo.toXml();
								if (log.isDebugEnabled()) log.debug("Adding key: " + key + " = " + obj + ", Class: " + obj.getClass().getSimpleName());
								entries.put(key, spo);
							} catch (GeneralException e) {
								log.error("Could not hydrate object " + key, e);
							}
						}
					} else if(obj instanceof SailPointObject) {
						
						if (log.isWarnEnabled()) log.warn("Cannot persist object " + key + " = " + obj.toString());
						
					} else {
						if (log.isDebugEnabled()) log.debug("Adding key: " + key + " = " + obj + ", Class: " + obj.getClass().getSimpleName());
						entries.put(key, obj);
					}
					
					
				} else {
					if (log.isDebugEnabled()) log.debug("Ignoring key: " + key + " = " + obj + ", Class: " + obj.getClass().getSimpleName());
				}
			} else {
				if (log.isDebugEnabled()) log.debug("Ignoring key: " + key + " with null value");
			}
		}
		
		return entries;
		
	}
	
	/**
	 * This method auto generates the config render hints for a given template if one does not exist
	 * 
	 * @param context
	 * @param key
	 * @param summaryMap
	 * @throws GeneralException
	 */
	private void checkConfigForRenderHints(SailPointContext context, String key, Map<String,Object> summaryMap) throws GeneralException {
		
		log.debug("Entering checkConfigForRenderHints");
		Configuration config = EnhancedNotifier.getConfiguration(context);
		@SuppressWarnings("unchecked")
		Map<String,Object> summaryTemplateMap = (Map<String,Object>) config.get(EnhancedNotifier.CONFIG_ATTRIBUTE_SUMMARY_TEMPLATE_FILTER_MAP);
		
		Object obj = summaryTemplateMap .get(key);
		Map<String,Object> renderHint = null;
		if (obj != null) {
			renderHint = (Map<String,Object>) obj;
		} else {
			renderHint = new HashMap<String,Object>();
			summaryTemplateMap.put(key, renderHint);
		}
		
		// Check to see if there is a title on the hint
		boolean saveChanges = false;
		if (!renderHint.containsKey(EnhancedNotifier.SUMMARY_RENDER_HINT_ATTRIBUTE_TITLE)) {
			renderHint.put(EnhancedNotifier.SUMMARY_RENDER_HINT_ATTRIBUTE_TITLE, key);
			saveChanges = true;
		}
		
		// Check to see if the columns have been configured
		if (!renderHint.containsKey(EnhancedNotifier.SUMMARY_RENDER_HINT_ATTRIBUTE_COLUMNS)) {
			List<Map<String,Object>> columns = new ArrayList<Map<String,Object>>();
			renderHint.put(EnhancedNotifier.SUMMARY_RENDER_HINT_ATTRIBUTE_COLUMNS, columns);
			
			List<String> ignoreVariables = new ArrayList<String>();
			ignoreVariables.add("to");
			ignoreVariables.add("sendImmediately");
			
			// TODO:  	Move this into a config item and not have this hardcoded
			//			for the next release
			
			Map<Class,List<String>> objCheck = new HashMap<Class,List<String>>();
			
			objCheck.put(WorkItem.class, Arrays.asList("name", "description", "id"));
			objCheck.put(Identity.class, Arrays.asList("name", "id", "manager", "firstname", "lastname", "email"));
			objCheck.put(IdentityRequest.class, Arrays.asList("name", "id", "completionStatus", "description", "type"));
			objCheck.put(WorkflowCase.class, Arrays.asList("name", "id", "description", "type"));
			
			for (String variableName : summaryMap.keySet()) {
				
				if (!ignoreVariables.contains(variableName)) {
					Object variable = summaryMap.get(variableName);
					boolean found = false;
					
					if (objCheck != null) {
						for (Class clazz : objCheck.keySet()) {
						
							if (clazz.isInstance(variable)) {
								found = true;
								List<String> attributes = objCheck.get(clazz);
								if (attributes != null) {
									for (String attribute : attributes) {
										columns.add(columnMap(variableName + "." + attribute, "$!" + variableName + "." + attribute, clazz.getSimpleName() + ": " + variableName + " " + attribute));
									}
								}
							}
						}
					}
					
					if (!found)	{
				
						// See if we should generically hande any sailpoint object
						if (variable instanceof SailPointObject) {
							String className = variable.getClass().getSimpleName();
							Method[] methods = variable.getClass().getMethods();
							for (Method method : methods) {
								if (method.getName().equals("getName") && method.getParameterTypes().length == 0) columns.add(columnMap(variableName + ".name", "$" + variableName + ".name", className + ": " + variableName + " name"));
								if (method.getName().equals("getId") && method.getParameterTypes().length == 0) columns.add(columnMap(variableName + ".id", "$" + variableName + ".id", className + ": " + variableName + " id"));
							}
						} else {
							columns.add(columnMap(variableName, "$" + variableName, variableName));
						}
					} 
				}
			}
			
			saveChanges = true;
		}
		
		if (saveChanges == true) {
			context.saveObject(config);
			context.commitTransaction();
		}
		
		log.debug("Exiting checkConfigForRenderHints");
	}
	
	/**
	 * Returns a map with the column information
	 * 
	 * @param name
	 * @param value
	 * @param label
	 * @return
	 */
	Map<String,Object> columnMap(String name, String value, String label ) {
		Map<String,Object> column = new HashMap<String,Object>();
		column.put(EnhancedNotifier.SUMMARY_RENDER_HINT_ATTRIBUTE_COLUMNS_NAME, name);
		column.put(EnhancedNotifier.SUMMARY_RENDER_HINT_ATTRIBUTE_COLUMNS_VALUE, value);
		column.put(EnhancedNotifier.SUMMARY_RENDER_HINT_ATTRIBUTE_COLUMNS_LABEL, label);
		return column;
	}
	
	/**
	 * Persist the email summary map into the relevant custom object
	 * 
	 * @param context
	 * @param summaryMap
	 * @throws GeneralException
	 */
	private void storeSummerizeEmail(SailPointContext context, Map<String,Object> summaryMap) throws GeneralException {

		log.debug("Entering storeSummerizeEmail");
		
		String templateName = (String) summaryMap.get(EnhancedNotifier.SUMMARY_MAP_ORIGINAL_TEMPLATE_NAME);
		String to = (String) summaryMap.get(EnhancedNotifier.SUMMARY_MAP_ATTR_SUMMARY_TO);
		// Get the summary custom object
		String summaryObjectName = getSummaryObjectName(to);
		
		int counter = context.countObjects(Custom.class, new QueryOptions(Filter.eq("name",summaryObjectName)));

		// Check to see if the summary object exists
		if (counter == 0) {
			// No summary object... create one!
			Custom newSummary = null;
			newSummary = new Custom();
			newSummary.setName(summaryObjectName);
			newSummary.put(EnhancedNotifier.SUMMARY_MAP_ATTR_SUMMARY_TO, to);
			
			context.saveObject(newSummary);
			context.commitTransaction();

			// Get rid of the new summary object from the cache and get the new
			// object from the persistence store.
			context.decache(newSummary);
		}
		
		try {
			context.startTransaction();
			Custom summaryObj = (Custom) ObjectUtil.lockObject(context, Custom.class, null, summaryObjectName, PersistenceManager.LOCK_TYPE_TRANSACTION);
			
			Map<String, Object> summeriesTemplate = (Map<String,Object>) summaryObj.get(SUMMARY_MAP_ATTR_SUMMARIES);
			if (summeriesTemplate == null) {
				
				summeriesTemplate = new HashMap<String,Object>();
				summaryObj.put(SUMMARY_MAP_ATTR_SUMMARIES, summeriesTemplate);
				
			}
			
			List<Map<String,Object>> summeries = (List<Map<String,Object>>) summeriesTemplate.get(templateName);
			if (summeries == null) {
				summeries = new ArrayList();
				summeriesTemplate.put(templateName, summeries);
			}
			
			summaryMap.put(EnhancedNotifier.SUMMARY_MAP_CREATED_DATE, new Date());
			summeries.add(summaryMap);
		
			context.saveObject(summaryObj);
			context.commitTransaction();
			context.decache();
		} catch (GeneralException e) {
			
			context.rollbackTransaction();
			throw new GeneralException("Could not perist custom summary object '" + summaryObjectName + "' due to exception:" + e.getMessage(), e );
			
		}
		log.debug("Exiting storeSummerzieEmail");
		
	}
	

	/**
	 * Initialise this component, detect what configuration options have been set
	 * 
	 * @param context
	 * @throws GeneralException
	 */
	private void init(SailPointContext context) throws GeneralException {
		
		
		log.debug("Init");
		Configuration config = EnhancedNotifier.getConfiguration(context);
		
		//
		// Check the dynamic email configuration aspect
		//
		
		dynamicEmailAttributeName = config.getString(EnhancedNotifier.CONFIG_ATTRIBUTE_DYNAMIC_EMAIL_IDENTITY_ATTRIBUTE);
		String dynamicEmailRuleName = config.getString(EnhancedNotifier.CONFIG_ATTRIBUTE_DYNAMIC_EMAIL_RULE_NAME);
		
		
		if (dynamicEmailRuleName != null) {
			dynamicEmailRule = context.getObjectByName(Rule.class, dynamicEmailRuleName);
			if (dynamicEmailRule == null) {
				throw new GeneralException("Dynamic emails rule '" + dynamicEmailRuleName + "' configuration has failed, rule does not exist.");
			} else {
				if (log.isDebugEnabled()) log.debug("Dynamic rule configured as " + dynamicEmailRule.getName());
			}
		}
		
		if (dynamicEmailAttributeName != null) {
			
			ObjectConfig objConfig = context.getObjectByName(ObjectConfig.class, "Identity");
			if (objConfig.getObjectAttribute(dynamicEmailAttributeName) != null) {
				if (log.isDebugEnabled()) log.debug("Dynamic emails configured based on identity attribute '" + dynamicEmailAttributeName + "' has been sucessful") ;
				isDynamicEmailTemplateConfigured = true;
			} else {
				if (log.isDebugEnabled()) log.debug("Dynamic emails based on the identity attribute '" + dynamicEmailAttributeName + "' has failed, attribute does not exist in Identity object schema");
			}
		} 
		
		if (dynamicEmailRule != null) {
			if (log.isDebugEnabled()) log.debug("Dynamic emails rule '" + dynamicEmailRuleName + "' has been sucessfully configured");
			isDynamicEmailTemplateConfigured = true;
			
		} 
		
		//
		// Check summary stuff
		//
		
		Object summaryFilters = (Map<String,Object>) config.get(CONFIG_ATTRIBUTE_SUMMARY_TEMPLATE_FILTER_MAP);
		if (summaryFilters != null && summaryFilters instanceof Map) {
			if (log.isDebugEnabled()) log.debug("Summary flters set as: " + summaryFilters.toString());
		} else {
			log.debug("No summary filters set");
		}
		
		String summaryFilterRuleName = config.getString(EnhancedNotifier.CONFIG_ATTRIBUTE_SUMMARY_RULE);
		if (summaryFilterRuleName != null) {
			if (log.isDebugEnabled()) log.debug("Summary filter rule name: " + summaryFilterRuleName );
		} else {
			log.debug("No summary filter rule name has been set");
		}
		
		if (summaryFilterRuleName != null) {
			summaryFilterRule = context.getObjectByName(Rule.class, summaryFilterRuleName);
			if (summaryFilterRule == null) {
				throw new GeneralException("Summary emailsrule '" + summaryFilterRuleName + "' configuration has failed, rule does not exist.");
			} else {
				if (log.isDebugEnabled()) log.debug("Summary filter rule object is configured as " + summaryFilterRule.getName());
			}
		}
		
		log.debug("Exiting init");
		
	}
	
	/**
	 * Check to see if an SailPoint Object exists
	 * 
	 * @param context			The SailPointContext 
	 * @param clazz				The SailPointObject class
	 * @param objectName		The object name to check
	 * @return					Returns true if objectName exists
	 * @throws GeneralException
	 */
	private boolean objectExists(SailPointContext context, Class<?> clazz, String objectName) throws GeneralException {
		QueryOptions qo = new QueryOptions(Filter.eq("name", objectName));
		int count = context.countObjects(clazz, qo);
		return (count == 1);
	}
	
	
	// Static methods
	/**
	 * Return the configuration object
	 * 
	 * @param context
	 *            SailPoint context
	 * @return
	 * @throws GeneralException
	 */
	public static final Configuration getConfiguration(SailPointContext context) throws GeneralException {

		Configuration config = context.getObjectByName(Configuration.class, EnhancedNotifier.CONFIG_OBJECT_NAME);
		if (config == null) throw new GeneralException("Cannot get Configuration object name " + EnhancedNotifier.CONFIG_OBJECT_NAME);
		return config;
	}
	
	/**
	 * Get the identity's object based in their email address.
	 * 
	 * @param context
	 *            SailPOintContext
	 * @param email
	 *            Email address of the identity
	 * @return Identity This could return null
	 * @throws GeneralException
	 */
	public static final Identity getIdentityFromEmailAddress(SailPointContext context, String email) {

		QueryOptions qo = new QueryOptions();
		List<String> returnFields = new ArrayList<String>();

		// Set up the query to return the language attribute in the search
		returnFields.add("name");
		qo.addFilter(Filter.eq(Identity.ATT_EMAIL, email));

		// Check to see how many identities have the specified email
		// address. If this is not ambiguous then continue, otherwise warn

		int idCount = 0;
		Identity identity = null;

		try {
			idCount = context.countObjects(Identity.class, qo);
		} catch (GeneralException e) {
			// OK, got an error of some sort.
			// Wrapped up in a General Exception (not always helpful).
			//
			// We'll throw it for now and see what kind of issues this
			// creates.
			log.warn("Cannot get Identity from email address", e);
		}

		if (idCount == 1) {
			// OK, we're good to go. Get the identity and
			// retrieve the preferred language setting

			Iterator<Identity> it = null;
			try {
				it = context.search(Identity.class, qo);
			} catch (GeneralException e) {
				log.error("Could not get iterator for qo: " + qo.getQuery());
			}
			
			if (it != null && it.hasNext()) {
				identity = it.next();
			} else {
				log.error("getIdentityFromEmailAddress Could not get identity from email, count suggested one available!");
			}

			// Flush the iterator... we only expected one record anyway
			Util.flushIterator(it);

		} else {
			// Ambiguous, could not find exactly one object so either the
			// email does not exist for any identity OR the same email address
			// is present on >1 identity. I'm not dealing with this problem at
			// the moment but will need to refect this in the log file... not mandatory
			
			log.warn("Could not find identity based on email address '" + email + "', returned '" + String.valueOf(idCount) + "' objects.");
		}

		// This can return null here... be warned.
		return identity;

	}

	/**
	 * This method always returns a custom object which has been persisted
	 * using the specific unique naming convention per email address.
	 * 
	 * If a new object is created, then the "to" field is automatically populated
	 * 
	 * @param emailAddress
	 * @throws GeneralException
	 */
	private String getSummaryObjectName(String emailAddress) {

		String summaryObjectName = SUMMARY_NAME_PREFIX + emailAddress;
		return summaryObjectName;

	}
	
	/**
	 * Get the identity's preferred language based on their preferred language
	 * setting. If none is specified then drop back to the servers default
	 * language setting.
	 * 
	 * @param email
	 * @return
	 * @throws GeneralException
	 */
	public static Identity getIdentityByEmailAddress(SailPointContext context, String email)
			throws GeneralException {

		QueryOptions qo = new QueryOptions();

		// Set up the query to return the language attribute in the search
		Identity identity = null;
		
		if (email != null) {
			
			//returnFields.add(languageAttribute);
			Filter filter = Filter.eq("email", email);
			qo.addFilter(filter);

			// Check to see how many identities have the specified email
			// address. If this is not ambiguous then continue, otherwise
			// go for the default.
	
			int idCount = 0;
	
			try {
				idCount = context.countObjects(Identity.class, qo);
			} catch (GeneralException e) {
				// Ok, got an error of some sort.
				// Wrapped up in a General Exception (not always helpful).
				//
				// We'll throw it for now and see what kind of issues this
				// creates.
				throw new GeneralException("Cannot get Identity from email address", e);
			}
	
			if (idCount == 1) {
				// Ok, we're good to go. Get the identity and
				// retrieve the preferred language setting
				identity = context.getUniqueObject(Identity.class, filter);
				
				if (identity == null) throw new GeneralException("Identity was sucessfully queried for by email address '" + email + "', but not found?");
				
	
			} else {
				// Ambiguous, could not find exactly one object so either the
				// email does not exist for any identity OR the same email address
				// is present on >1 identity. I'm not dealing with this problem at
				// the moment, so throw it.
				log.warn("Could not find identity based on email address '" + email	+ "', returned '" + String.valueOf(idCount) + "' objects.");
				identity = null;
			}
		}

		return identity;

	}
	
	
}
