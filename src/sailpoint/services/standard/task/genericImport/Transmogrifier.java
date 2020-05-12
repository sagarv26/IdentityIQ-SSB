package sailpoint.services.standard.task.genericImport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.proxy.HibernateProxy;

import sailpoint.api.Provisioner;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.connector.Connector;
import sailpoint.connector.ConnectorException;
import sailpoint.object.Application;
import sailpoint.object.Application.Feature;
import sailpoint.object.Attributes;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.ObjectAttribute;
import sailpoint.object.ObjectConfig;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AbstractRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.object.ProvisioningPlan.ObjectOperation;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.ObjectRequest;
import sailpoint.object.ProvisioningPlan.Operation;
import sailpoint.object.ProvisioningProject;
import sailpoint.object.SailPointObject;
import sailpoint.object.Schema;
import sailpoint.tools.GeneralException;
import sailpoint.object.AttributeDefinition;
/**
 * <p>The Transmogrifier tool allows the Data Row Map to be transformed
 * into a SailPoint first class Object or a ProvisioningPlan.</p>
 * 
 * <p>SailPoint IdentityIQ first class Objects can be automatically created, 
 * population or retrieved and updated based on the Data Row Map
 * values.  All SailPoint IdentityIQ first class objects must support
 * a setName() method before they can be merged or created 
 * using this class.</p>
 * 
 * <p>Provisioning Plans can be automatically created based 
 * on the values of the Data Row Map values.</p>
 * 
 * 
 * @author christian.cairney
 * @version		1.2
 * @since 		1.0
 * 
 * Updates:
 * 	16/DEC/2016		Updated executeProvisioning method to use the connectors Provision method
 * 					directly after compiling the project, this was needed as account
 * 					plans needed an Identity which which has the unwanted side affect of 
 * 					correlating the new account to that identity.
 * 
 *  05/JAN/2017		Updated Provision methods to allow the use of the provisioner if required
 * 					An additional method to execute to choose the path:
 * 
 * 						public void executeProvisioningPlan(ProvisioningPlan plan, Attributes<String,Object> args, boolean useProvisioner) 
 * 					
 * 					Additionally the following method always uses the Connector only:
 * 
 * 						public void executeProvisioningPlan(ProvisioningPlan plan)
 * 
 *  				And the following method always uses the Provisioner
 *  
 *  					public void executeProvisioningPlan(ProvisioningPlan plan, Attributes<String,Object> args) 
 *  
 *  12/JAN/2017 	Fixed issue raised by Lukasz Tupaj, mergeObjectWithRow was incorrectly
 * 					ignoring extended attribute which had no flags on them. 
 * 
 *  12/JAN/2017		Fixed an issue when sailpoint.objects.* are passed via Hibernate and due to them not being
 *  				initialised they are represented as HibernateProxy objects and the reflection findMethod
 *  				method fails to match a SailPointObject.  Solution is to call the HibernateProxy classes
 *  				implementation api to discover the underlying object being proxied.
 * 
 * 	12/JAN/2017		Version to 1.2
 *
 */
public class Transmogrifier {
	
	private static final String OBJECT_METHOD_SET_ATTRIBUTE = "setAttribute";
	private SailPointContext context;
	private String includeAttributeSpec = "*";
	private String excludeAttributeSpec = null;
	private String forceInProvisioningPlan = null;
	private String removePrefix = null;
	private SailPointObject spObject;
	private Attributes<String,Object> rowMetaData;
	
	// Our friend the logger.
	private static final Logger log = Logger.getLogger(Transmogrifier.class);
		
	/**
	 * Instantiated the Transmogrifier
	 * 
	 * The SailPointContext will be derived from the current
	 * login, and the row meta data will need to be set after this call.
	 * 
	 * @see #setRowMetaData(Attributes)
	 * @throws GeneralException
	 */
	public Transmogrifier() throws GeneralException {
		this(SailPointFactory.getCurrentContext());
	}
	
	/**
	 * Instantiated Transmogrifier class with the context passed to it.
	 * 
	 * @param context			SailPointContext object
	 * @see #Transmogrifier()
	 */
	public Transmogrifier(SailPointContext context) {
		
		this(context, null, null);
		
	}
	
	/**
	 * Instantiated Transmogrifier class with the context and a data row
	 * map passed to it.
	 * 
	 * @param context			SailPointContext object
	 * @param row				Row data map
	 * @throws GeneralException
	 * @see #Transmogrifier()
	 */
	public Transmogrifier(SailPointContext context, Attributes<String,Object> row) throws GeneralException {
		
		this(context, null, row);

	}

	/**
	 * Instantiated Transmogrifier class with a data row map. 
	 * The SailPointContext will be derived from the current login.
	 * 
	 * @param row				Row data map
	 * @throws GeneralException
	 */
	public Transmogrifier(Attributes<String,Object> row) throws GeneralException {
		
		this(SailPointFactory.getCurrentContext(), null, row);
		
		
	}

	/**
	 * Instantiated Transmogrifier class with a reference to a First Class SailPointObject
	 * and a data row map. 
	 * 
	 * The SailPointObject need not be instantiated.
	 * 
	 * @param object			First Class SailPointObject
	 * @param row				Row data map
	 * @throws GeneralException
	 */
	public Transmogrifier(SailPointObject object, Attributes<String,Object> row) throws GeneralException {
		
		this(SailPointFactory.getCurrentContext(), object, row);
		
		
	}
	
	/**
	 * Instantiated Transmogrifier class with a reference to a SailPointContext, a First Class SailPointObject
	 * and a data row map. 
	 * 
	 * The SailPointObject need not be instantiated.
	 * 
	 * @param context			SailPointContext 
	 * @param object			First Class SailPointObject
	 * @param row				Row data map
	 * @throws GeneralException
	 */
	public Transmogrifier(SailPointContext context, SailPointObject object, Attributes<String,Object> row) {
		
		this.context = context;
		this.spObject = object;
		this.rowMetaData = row;
		
		
	}
	
	/**
	 * Get the current row meta data persisted in this class.
	 * @return	Attributes Map representing the row meta data.
	 */
	public Attributes<String, Object> getRowMetaData() {
		return rowMetaData;
	}

	/**
	 * Set the row meta data for this class to operator on.
	 * @param rowMetaData		The Attributes map containing the row meta data
	 */
	public void setRowMetaData(Attributes<String, Object> rowMetaData) {
		this.rowMetaData = rowMetaData;
	}

	/**
	 * Retrieve the current Include attribute specification
	 * @return 	Current include attribute specification
	 */
	public String getIncludeAttributeSpec() {
		return includeAttributeSpec;
	}


	/**
	 * <p>Set the include attribute specification.</p>
	 * 
	 * <p>This method sets a String or CSV value which is used by this class to include rows
	 * from the Row Meta Data map and filter out the rest.  As an example, a header row
	 * made up as follows can be filtered</p>	 
	 *
	 *<blockquote>{@code columname1,columnname2,columnname3,attributename,location}</blockquote>
	 *
	 *<p>The following filter is applied:</p>
	 *
	 *<blockquote>{@code columname1,columnname2,columnname3},attributename</blockquote>
	 *
	 *<p>This would result in the follow columns being retrieved when the Row Meta Data is processed</p>
	 *
	 *<blockquote>{@code columname1,columnname2,columnname3,attributename}</blockquote>
	 * 
	 * <p>The CSV value can also include wild cards of type "*" and "?" similar to filing system wildcards.  
	 * The following will give you the same result of the filter specification above</p>
	 * 	
	 * <blockquote>{@code column*,attributename}</blockquote>
	 * 
	 * <p>and</p>
	 * 
	 * <blockquote>{@code columnname?,attribute* }</blockquote>
	 * 
	 * The included attribute specification is used when merging SailPoint IdentityIQ First Class Objects
	 * and the creation of Provisioning Plans
	 * 
	 * @see #mergeObjectWithRow()
	 * 
	 * @param includeAttributeSpec		CSV list of attributes to be included from the attribute map
	 */
	public void setIncludeAttributeSpec(String includeAttributeSpec) {
		this.includeAttributeSpec = includeAttributeSpec;
	}


	/**
	 * Get the current Exclude Attribute specification
	 * @return Excluded attribute specification
	 */
	public String getExcludeAttributeSpec() {
		return excludeAttributeSpec;
	}


	/**
	 * <p>Set the exclude attribute specification.</p>
	 * 
	 * <p>This method sets a String or CSV value which is used by this class to exclude rows
	 * from the Row Meta Data map.  As an example, a header row made up as follows can be filtered</p>	 
	 *
	 *<blockquote>{@code columname1,columnname2,columnname3,attributename,location}</blockquote>
	 *
	 *<p>The following filter is applied:</p>
	 *
	 *<blockquote>{@code columname1,columnname2,columnname3,attributename}</blockquote>
	 *
	 *<p>This would result in the follow columns being retrieved when the Row Meta Data is processed</p>
	 *
	 *<blockquote>{@code location}</blockquote>
	 * 
	 * <p>The CSV value can also include wild cards of type "*" and "?" similar to filing system wildcards.  
	 * The following will give you the same result of the filter specification above</p>
	 * 	
	 * <blockquote>{@code column*,attributename}</blockquote>
	 * 
	 * <p>and</p>
	 * 
	 * <blockquote>{@code columnname?,attribute* }</blockquote>
	 * 
	 * The exclude attribute specification is used when merging SailPoint IdentityIQ First Class Objects
	 * and the creation of Provisioning Plans
	 * 
	 * @see #mergeObjectWithRow()
	 * @param excludeAttributeSpec
	 */
	public void setExcludeAttributeSpec(String excludeAttributeSpec) {
		this.excludeAttributeSpec = excludeAttributeSpec;
	}



	/**
	 * Get the current remove prefix string.
	 * @return		Removed Prefix
	 */
	public String getRemovePrefix() {
		return removePrefix;
	}



	/**
	 * <p>setRemovePrefix method sets a String or CSV list value of prefix(es) to remove from
	 * the beginning of Row Meta Data map column names. Example: An attribute map has the following columns</p>
	 * 
	 * <blockquote>{@code columname1,columnname2,columnname3,attributename,location}</blockquote>
	 *
	 *<p>A remove prefix is set as</p>
	 *
	 *<blockquote>{@code column,attribute}</blockquote>
	 *
	 *<p>Will render the columns in the Row Meta Data map to be transformed as</p>
	 *
	 *<blockquote>{@code name1,name2,name3,name,location}</blockquote>
	 * 
	 * @param removePrefix
	 */
	public void setRemovePrefix(String removePrefix) {
		this.removePrefix = removePrefix;
	}

	/**
	 * Get the current Force In Provisioning Plan override string
	 * @return	Provisioning Plan override string
	 */
	public String getForceInProvisioningPlan() {
		return forceInProvisioningPlan;
	}

	/**
	 * Sets a String or CSV value which augments the creation of Provisioning Plans
	 * and forces attributes listed in the Row Meta Data map which are not listed in
	 * the IdentityIQ Application configuration schema.
	 * 
	 * @param forceInProvisioningPlan
	 */
	public void setForceInProvisioningPlan(String forceInProvisioningPlan) {
		this.forceInProvisioningPlan = forceInProvisioningPlan;
	}

	/**
	 * Filter the attributes based on an include and exclude attribute specification held in a CSV
	 * 
	 * The includeAttributeSpec and excludeAttributeSpec is a CSV which is in the following format:
	 * 
	 * 	columname1,columnname2,columnname3,attributename,location etc
	 * 
	 * The CSV value can also include wild cards of type * and ? similar to how the work in a Unix
	 * file system, so the following should give you the same result of the filter specification above
	 * 
	 * 	column*,attributename,location 
	 * 
	 * and
	 * 
	 * 	columnname?,attribute*,location
	 * 
	 * removePrefix is also a csv if the key name needs sanitising before being passed to the method matcher
	 * but does not support wild-cards
	 * 
	 * @param row
	 * @param includeAttributeSpec
	 * @param excludeAttributeSpec
	 * @return
	 */
	private Attributes<String, Object> filterAtributes(Attributes<String, Object> row, String includeAttributeSpec, String excludeAttributeSpec, String removePrefix) {
		
		
		String includeAttributeFilters[] = includeAttributeSpec.split(",");
		String excludeAttributeFilters[] = null;
		String removePrefixes[] = null;
		
		if (excludeAttributeSpec != null) excludeAttributeFilters = excludeAttributeSpec.split(",");
		if (removePrefix != null) removePrefixes = removePrefix.split(",");
		
		Attributes<String, Object> filteredAttributes = new Attributes<String,Object>();
		for (String key : row.keySet()) {
			
			key = key.trim();
			
			// See if we need to include the value
			for (String includeAttributeFilter : includeAttributeFilters) {
				
				includeAttributeFilter = includeAttributeFilter.trim();
				
				String regexInclude = includeAttributeFilter.replace("?", ".?").replace("*", ".*?");
				if (key.matches(regexInclude)) {
					
					boolean foundExcluded = false;
					if (excludeAttributeFilters != null) {
						
						for (String excludeAttributeFilter : excludeAttributeFilters) {
						
							excludeAttributeFilter = excludeAttributeFilter.trim();
							
							String regexExclude = excludeAttributeFilter.replace("?", ".?").replace("*", ".*?");
							if (key.matches(regexExclude)) {
								
								foundExcluded = true;
								break;
							}
						}				
					}
					
					if (!foundExcluded) {
						
						// Check to see if we need to remove any prefixes so they don't get in the way of any
						// method matching etc
						String newKey = key;
						if (removePrefixes != null) {
							for (String prefix : removePrefixes) {
								if (key.startsWith(prefix)) {
									newKey = key.substring(prefix.length()); 
									break;
								}
							}
						}
						filteredAttributes.put(newKey, row.get(key));
					}
					
					break;
				}
			}
		}
		
		return filteredAttributes;
		
	}
	
	/**
	 * findObject method will attempt to find an object by it's type based on the filtered attributes
	 * Different object types need different types of queries to find them.  Currently any object
	 * which has a name and ManagedAttributes are supported.  
	 * 
	 * @param object
	 * @param filteredAttributes
	 * @return
	 */
	private SailPointObject findObject(SailPointObject object, Attributes<String,Object>filteredAttributes) {
		
		
		Filter filter = null;
		String classSimpleName = object.getClass().getSimpleName();
		
		if (classSimpleName.equals("ManagedAttribute")) {
			
			String applicationKey = containsIgnoreCase("application", (List<String>) filteredAttributes.keySet());
			String attributeKey = containsIgnoreCase("attribute", (List<String>) filteredAttributes.keySet());
			String valueKey = containsIgnoreCase("value", (List<String>) filteredAttributes.keySet());
			
			if (applicationKey != null && attributeKey != null && valueKey != null) {
				filter = ImporterUtil.getFilterManagedAttribute((String) filteredAttributes.get(applicationKey), (String) filteredAttributes.get(attributeKey), (String) filteredAttributes.get(valueKey));				
			}
		} else {
			
			String nameKey = containsIgnoreCase("name", (List<String>) filteredAttributes.keySet());
			if (nameKey != null) {
				filter = ImporterUtil.getFilterObjectByName((String) filteredAttributes.get(nameKey));
			}
		}
		
		if (filter != null) {
			try {
				object = ImporterUtil.getUniqueObject(context, object.getClass(), filter, false);
			} catch (InstantiationException e) {
				log.error("Could not perform search in findObject due to exception", e);;
			} catch ( IllegalAccessException e) {
				log.error("Could not perform search in findObject due to exception", e);;
			} catch ( GeneralException e) {
				log.error("Could not perform search in findObject due to exception", e);;
			}
		}
		return object;
	}
	
	/**
	 * Merge the SailPointObject with the Row Meta Data.  Any first class SailPoint IdentityIQ Object
	 * is supported.  This method will perform the following
	 * 
	 * <ul>
	 * <li>If the SailPointObject is set as null then an attempt to find the object based on it's name will be performed.</li>
	 * <li>If the SailPointObject is null and the object is not found in the IdentityIQ Repository then one will be created</li>
	 * <li>If the SailPointObject is not null, then values from the Row Meta Data map will be merged into the object</li>
	 * </ul>
	 * 
	 * <p>When the Row Meta Data map is merged with a SailPointObject the following occurs</p>
	 * 
	 * <ul>
	 * <li>The SailPointObject ObjectConfig is interrogated and a match between the object config's 
	 * attribute names and the Row Meta Data map is attempted, any matching values are set on the
	 * SailPointObject from the Row Meta Data map</li>
	 * <li>The SailPointObject setter methods are interrogated and an attempt to match the Row Meta Data column
	 * names is performed. The setter method "set" name is removed before attempting to match against the Row Meta
	 * data column name</li>
	 * </ul>
	 * 
	 * This method applies the included, excluded and removed prefixes before any matching is performed.
	 * 
	 *  @see #setIncludeAttributeSpec(String)
	 *  @see #setExcludeAttributeSpec(String)
	 *  @see #setRemovePrefix(String)
	 * 
	 * @throws GeneralException
	 */
	public void mergeObjectWithRow() throws GeneralException {

		// Pre-reqs

		if (rowMetaData == null) throw new GeneralException("No Attributes row object in mergeObjectSchemaWithRow");	
				
		if (log.isDebugEnabled()) log.debug("Entering mergeObjectSchemaWithRow (" + spObject.toString() + "," + rowMetaData.toString() + ")");
		
		// Filter the attributes to the ones we are interested in
		Attributes<String,Object> attributes = filterAtributes(rowMetaData, includeAttributeSpec, excludeAttributeSpec, removePrefix);
		if (log.isDebugEnabled()) log.debug("Filtered attributes: " + attributes.toString());

	
		// if the object is null, can we find one from the filtered attributes?
		if (spObject == null) {
			// We are going on a journey.. to find the object we are looking for.
			// Normally, we can just look for the object name, but this is not the case for all objects,
			// such as the ManagedAttribute class where we would need the Application Name, Attribute value and
			// Attribute name to get it.  I'm not putting all the logic here though.
			
			spObject = findObject(spObject, attributes);
			
		}
		if (spObject == null) throw new GeneralException("No SailPoint object in mergeObjectSchemaWithRow");
		
		
		// Grab the object config for the give object if one is available
		ObjectConfig config = ObjectConfig.getObjectConfig(spObject.getClass());
		
		Map<String, ObjectAttribute> objectAttributes = null;
		if (config != null) {
			objectAttributes = config.getObjectAttributeMap();
			if (log.isDebugEnabled()) log.debug("Object config for object " + spObject.getClass().getCanonicalName() + " = " + config.getName() + " (" + config.getId() + ")");
		} else {
			if (log.isDebugEnabled()) log.debug("Object does not have an ObjectConfig");
		}

		// Get the methods for this class into a hashmap for easy reference
		Class<? extends SailPointObject> objectClass = spObject.getClass();
		

		// Iterate through the allowed attributes
		for (String key : attributes.keySet()) {

			if (log.isDebugEnabled()) log.debug(String.format("Checking key: '%s'", key));

			// Ok, so we have a theoretical target key, now we need to
			// understand how to set the value of this on the object

			// If the object config has the key as an attribute value then
			// set it in the setAttributes method.
			String lowerCaseKey = key.toLowerCase();
			boolean found = false;
			ObjectAttribute objectAttribute = null;
			
			// Check to see if there is an object config in the first place to check for.
			if (objectAttributes != null) {
				for (String keyOa: objectAttributes.keySet()) {
					objectAttribute = objectAttributes.get(keyOa);
					
					//
					// Bug reported by Lukasz Tupaj
					//
					// objectAttributes where being filtered by using the .isCustom()
					// flag, this is incorrect and we need to use .isSystem to indicate
					// we should be using the objects getter/setters instead
					//
					if ((!objectAttribute.isSystem()) && keyOa.toLowerCase().equals(lowerCaseKey)) {
						found = true;
						break;
					}
				}
			}

			if (found) {

				// Update the attribute by using the objects set attribute method
				
				Class[] methodArgs = new Class[2];
				methodArgs[0] = String.class;
				methodArgs[1] = Object.class;
				
				Method method = null;
				try {
					method = objectClass.getMethod(OBJECT_METHOD_SET_ATTRIBUTE, methodArgs);
					Object[] params = new Object[2];
					params[0] = objectAttribute.getName();
					params[1] = attributes.get(key);
					try {
						method.invoke(spObject, params);
					} catch (IllegalAccessException  e) {
						throw new GeneralException("Illegal action whe calling " + 
							OBJECT_METHOD_SET_ATTRIBUTE + "(" + objectAttribute.getName() + "," + attributes.get(key).toString());
					} catch (  IllegalArgumentException e) {
						throw new GeneralException("Illegal action whe calling " + 
							OBJECT_METHOD_SET_ATTRIBUTE + "(" + objectAttribute.getName() + "," + attributes.get(key).toString());
					} catch ( InvocationTargetException e) {
						throw new GeneralException("Illegal action whe calling " + 
							OBJECT_METHOD_SET_ATTRIBUTE + "(" + objectAttribute.getName() + "," + attributes.get(key).toString());
					}
					
				} catch (NoSuchMethodException e) {
					log.warn(objectClass.getCanonicalName() + " class does not have a setAttribute method, cannot map this value.");
				} catch (SecurityException e) {
					log.warn("Cannot access class " + objectClass.getCanonicalName() + " setAttribnute method, cannot map this value.");
				}	
				
			} else {
				// The key is not in the Attributes values, so we'll look
				// for a Setter for the information instead.
				if (log.isDebugEnabled()) log.debug(String.format("Key '%s' is not in the object attributes, looking for object Setter", key));

				// Have a look for the method.
				String setMethodName = key;
				if (log.isDebugEnabled()) log.debug(String.format("Checking for a method signature: %s with value %s", setMethodName, attributes.get(key)));
				
				// Get the current object class, we have to be 
				// careful here, because the current object may be a hibernate
				// proxy, so then we'll need to grab the class
				Object currentObject = attributes.get(key);
				Class currentObjectClass = null;
				if (currentObject instanceof HibernateProxy) {
					// cast it to the proxy class
					HibernateProxy hp = (HibernateProxy) currentObject;
					currentObjectClass = hp.getHibernateLazyInitializer().getImplementation().getClass();
					if (log.isDebugEnabled()) log.debug("Find class method: " + setMethodName + ",  Current object is a HibernateProxy: '" 
							+ currentObject.getClass().getCanonicalName() + "', transforming to " + currentObjectClass.getCanonicalName());
				} else {
					currentObjectClass = currentObject.getClass();
					if (log.isDebugEnabled()) log.debug("Find class method: " + setMethodName + ", Current object type is " + currentObjectClass.getCanonicalName());
				}
				Method m = findClassMethod(objectClass, setMethodName, currentObjectClass);
				
				if (m != null) {
						
					log.debug("Method matched");
					int parameterCount = m.getParameterAnnotations().length;
					if (parameterCount == 1) {
						Object[] params = new Object[1];
						params[0] = attributes.get(key);
						try {
							m.invoke(spObject, params);
						} catch (IllegalAccessException  e) {
							throw new GeneralException("Illegal action whe calling " + 
								m.getName() + "(" + attributes.get(key).toString());
						} catch ( IllegalArgumentException e) {
							throw new GeneralException("Illegal action whe calling " + 
								m.getName() + "(" + attributes.get(key).toString());
						} catch ( InvocationTargetException e) {
							throw new GeneralException("Illegal action whe calling " + 
								m.getName() + "(" + attributes.get(key).toString());
						}
					
					} else {
						if (log.isEnabledFor(Level.WARN))
							log.warn(String
									.format("Method name '%s' does not have one parameter, cannot instantiate it",
											m.getName()));
					}
					
				} else {
					log.debug("Could not find a matching method");
				}
			}
		}

		log.debug("Exiting mergeObjectSchemaWithRow");
	}
	
	
	/**
	 * Returns the actual value with the valid case on a search value from a list
	 * If no value is found then return NULL.
	 * 
	 * If the searchValue or the list values are null, then this method will
	 * return NULL.
	 * 
	 * @param searchValue
	 * @param values
	 * @return
	 */
	String containsIgnoreCase(String searchValue, List<String> values) {
		
		if (searchValue == null || values == null) return null;
		
		for (String value : values) {
			if (value.equalsIgnoreCase(searchValue)) return value;
		}
		return null;
	}
	String containsIgnoreCase(String searchValue, Set<String> values) {
		
		if (searchValue == null || values == null) return null;
		
		for (String value : values) {
			if (value.equalsIgnoreCase(searchValue)) return value;
		}
		return null;
	}
		

	
	
	/**
	 * Attempt to find a class method based on a column key.  This class method search is case 
	 * in-sensitive
	 * 
	 * @param clazz
	 * @param methodName
	 * @param types
	 * @return
	 */
	private  final Method findClassMethod(Class<?> clazz, String methodName, Class ... types) {
		
		log.debug("Starting findClassMethod");
		Method[] methods = clazz.getMethods();
		
		String callMethodName = methodName;
		String callSetterMethodName = "set" + callMethodName.substring(0,1).toUpperCase() + callMethodName.substring(1);
		
		if (log.isDebugEnabled()) log.debug("Checking for class methods: " + callMethodName + " & " + callSetterMethodName);
		
		Method returnMethod = null;
		
		for (Method method : methods) {	
				
			int parameterCount = method.getParameterAnnotations().length;
			
			String currentMethodName = method.getName();
			if (log.isTraceEnabled()) log.trace("Attempt to match method name = '" + currentMethodName  + "'");
			if (currentMethodName .equalsIgnoreCase(callMethodName) || currentMethodName .equalsIgnoreCase(callSetterMethodName)) {
				if (types == null) {
					log.trace("No types specified, auto matching");
					returnMethod = method;
					break;
				} else if (parameterCount == types.length) {
					
					log.trace("Types were specified, checking...");
					Class[] methodTypes = method.getParameterTypes();
					
					boolean found = true;
					for (int i=0; i < types.length ; i++) {

						Class methodType = methodTypes[i];
						Class type = types[i];
						
						//
						// Hibernate may be proxying the class to allow lazy inits etc
						// so we need to get the superclass to find the real class!!
						//
						//if (HibernateProxy.class.isAssignableFrom(type)) {
						//	log.trace("  Hibernate class detected, Class " + types[i].getCanonicalName() + " has been transformed to " + type.getCanonicalName());
						//}
					
						if (!methodTypes[i].equals(type)) {
							if (log.isTraceEnabled()) {
								log.trace("  Method type " + methodType.getCanonicalName() + " != " + type.getCanonicalName());
								log.trace("  Do not match, ignoring this method signature");
							}
							found=false;
							break;
						} else {
							if (log.isTraceEnabled()) log.trace("  Method type " + methodTypes[i].getCanonicalName() + " == " + types[i].getCanonicalName());
						}
					}
					if (found) {
					
						returnMethod = method;
						break;
					}
				}	
			}
			
			log.trace("Not matched method name");
		}
		if (log.isDebugEnabled()) {
			if (returnMethod == null) {
				log.debug("No method found for: " + methodName );
			} else {
				log.debug("Found method " + returnMethod.getName() + " from " + methodName );
			}
		}
		
		log.debug("Exiting findClassMethod");
		return returnMethod;
	}

	/**
	 * Helper method to invoke the class member method.
	 * If the attribute types do not align it will try to resolve them
	 * 
	 * @param context
	 * @param m
	 * @param object
	 * @param params
	 * @return
	 */
	private boolean methodInvoke(Method m, SailPointObject object, Object... params) {

		log.debug("Entering methodInvoke");
		boolean success = false;

		try {
			
			int parameterCount = m.getParameterAnnotations().length;
			if (log.isDebugEnabled()) 
				log.debug(String.format("Method: '%s', number of parameters: %d", m.getName(), parameterCount));
				
			
			Object[] transformedParams = new Object[parameterCount]; 
			

			for (int i=0; i < parameterCount; i++) {
	
				if (log.isDebugEnabled()) log.debug(String.format("Iteration [%d]", i));
				
				Object parameter = params[i];
				Class<?> methodType = m.getParameterTypes()[i];
				
				Class<? extends Object> paramType = null;
				if (parameter != null) paramType = parameter.getClass();
				
				if (log.isDebugEnabled()) {
					
					log.debug(String.format("Parameter %d, Method parameter type '%s', Actual Parameter type: '%s' with value '%s'",
							i, methodType.getCanonicalName(), (paramType == null) ? "{NULL}" : paramType.getCanonicalName(), 
							(parameter == null) ? "{NULL}" : parameter.toString()));
				}
				// If the method and parameter types match or method parameter is an object
				// then we don't need to transform.
				if (parameter == null || methodType.equals(paramType) || methodType.equals(Object.class)) {
					// nothing to transform
					
					log.debug("Adding parameter");
					transformedParams[i] = parameter;
					
				} else {
					
					log.debug("Transforming parameter");
									
					// Yikes, this gets interesting.  I'll see if the source param
					// is a string and the method an Identity object then I think
					// I can resolve this.
					
					if (paramType.equals(String.class) && isSailPointObject(methodType)) {
						// Ok, so I'm guessing the string holds an SailPoint object name or ID
						SailPointObject newValue =  null;
						try {
							if (log.isDebugEnabled()) log.debug(String.format("Attempting to get %s with object name '%s'", methodType.getCanonicalName(), parameter.toString()));
							newValue = ImporterUtil.getUniqueObject(context, (Class<? extends SailPointObject>) methodType, ImporterUtil.getFilterObjectByName((String) parameter), true);
						} catch (GeneralException e) {
							log.error(String.format("Could not get object type '%s' for parameter '%s'", methodType.getName(), params[i]));
							newValue = null;
						} catch (InstantiationException e) {
							log.error("InstantiationException when attempting to transform method call parameter", e );
							newValue = null;
						}
						params[i] = newValue;
					} else {
						
						log.error("Cannot transform this parameter");
					}
				}
			}

			log.debug("Invoking method");
			m.invoke(object, params);
			success = true;

		} catch (IllegalAccessException e) {
			success = false;
			log.error("methodInvokde throw an exception: " + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			success = false;
			log.error("methodInvokde throw an exception: " + e.getMessage(), e);
		} catch (InvocationTargetException e) {
			success = false;
			log.error("methodInvokde throw an exception: " + e.getMessage(), e);
		} finally {

			log.debug(String.format("Exiting methodInvoke with return value success: %b", success ));
			
		}
		return success;

	}
	
	
	/**
	 * Recursive method to check if an object is a SailPointObject
	 * 
	 * @param clazz
	 * @return
	 */
	private final boolean isSailPointObject(Class<?> clazz) {
		
		Class<?> superClazz = clazz.getSuperclass();
		
		if (superClazz.equals(Object.class)) {
			return false;
		}
		if (superClazz.equals(SailPointObject.class)) {
			return true;
		} else {
			return isSailPointObject(superClazz);
		}
	}
	
	/**
	 * Create an Account Provisioning plan based on the Row Meta Data.The Account Provisioning
	 * Plan is for creating account in target systems.  The Row Meta Data map is matched against 
	 * the Application Config schema and any matches are added to the provisioning plan.  Any include / excluded
	 * and prefix removals attributes are honoured before attempting the match.  Any attribute
	 * which has been forced in the provisioning plan will also be rendered in the final Provisioning Plan
	 * 
	 * This method applies the included, excluded, force field into Provisioning plan and removed prefixes before any matching is performed.
	 * 
	 * @see #includeAttributeSpec
	 * @see #excludeAttributeSpec
	 * @see #removePrefix
	 * @see #forceInProvisioningPlan
	 *  
	 * @param applicationName
	 * @param schemaName
	 * @param operation
	 * @param nativeIdentity
	 * @return			Returns a Provisioning Plan based on the Row Meta Data map.
	 * @throws GeneralException
	 */
	public ProvisioningPlan createAccountProvisioningPlan(String applicationName, String schemaName, ObjectOperation operation, 
			String nativeIdentity) throws GeneralException {
		
		return createProvisioningPlan( applicationName, schemaName, operation, nativeIdentity, true);
		
	}
	
	/**
	 * Create an Object Provisioning plan based on the Row Meta Data
	 * 
	 * 	This method applies the included, excluded, force field into Provisioning plan and removed prefixes before any matching is performed.
	 * 
	 * @see #includeAttributeSpec
	 * @see #excludeAttributeSpec
	 * @see #removePrefix
	 * @see #forceInProvisioningPlan
	 * 
	 * @param applicationName
	 * @param schemaName
	 * @param operation
	 * @param nativeIdentity
	 * @return		Returns a Provisioning Plan based on the Row Meta Data map.
	 * @throws GeneralException
	 */
	public ProvisioningPlan createObjectProvisioningPlan(String applicationName, String schemaName, ObjectOperation operation, 
			String nativeIdentity) throws GeneralException {
		
		return createProvisioningPlan( applicationName, schemaName, operation, nativeIdentity, false);
		
	}
	
	
	private ProvisioningPlan createProvisioningPlan(String applicationName, String schemaName, ObjectOperation operation, 
			String nativeIdentity, boolean accountRequest) throws GeneralException {
		
		ProvisioningPlan plan = new ProvisioningPlan();
		
		Identity spadmin = this.context.getObjectByName(Identity.class, "spadmin");
		plan.setIdentity(spadmin);
		// Filter the attributes to the ones we are interested in
		Attributes<String,Object> attributes = filterAtributes(rowMetaData, includeAttributeSpec, excludeAttributeSpec, removePrefix);
		if (log.isDebugEnabled()) log.debug("Unfiltered attributes: " + attributes.toString());

		Application application = context.getObjectByName(Application.class, applicationName);
		if (application == null) throw new GeneralException("Could not find application name = " + applicationName);
		
		AbstractRequest request = null;
		if (accountRequest) {
			request = new AccountRequest();
		} else {
			request = new ObjectRequest();
		}
		
		request.setType(schemaName);
		request.setApplication(application.getName());
		request.setOp(operation);
		request.setNativeIdentity(nativeIdentity);
		
		// Add the object request to the provisioning plan
		plan.addRequest(request);
		
		//
		// We (Royal) only want to add attribute requests if its a create or modify operation
		//
		
		if (operation.equals(ObjectOperation.Create) || operation.equals(ObjectOperation.Modify)) {
			Schema schema = application.getSchema(schemaName);
		
			if (schema == null) throw new GeneralException("Could not find schema " + schemaName + " in application " + applicationName);
			
			for (String key : attributes.keySet()) {
				
				Object value = attributes.get(key);
				
				AttributeDefinition attributeDefinition = keyExistsInSchema(key, schema);
				if (attributeDefinition != null) {
					
					
					if (attributeDefinition.isMultiValued()) {
						
						if (value instanceof List) {
							for (Object v : (List) value) {
								AttributeRequest attr = new AttributeRequest();
								attr.setName(attributeDefinition.getName());
								attr.setOp(Operation.Add);
								attr.setValue(v);
								request.add(attr);
							}
							
						} else {
							AttributeRequest attr = new AttributeRequest();
							attr.setName(attributeDefinition.getName());
							attr.setOp(Operation.Add);
							attr.setValue(value);
							request.add(attr);
						}
						
					} else {
						AttributeRequest attr = new AttributeRequest();
						attr.setName(attributeDefinition.getName());
						if (operation.equals(ObjectOperation.Modify)) {
							attr.setOp(Operation.Set);
						} else if (operation.equals(ObjectOperation.Create)){
							attr.setOp(Operation.Add);
						}
						attr.setValue(value);
						request.add(attr);
					}
				} else {
					String force = keyExistsInForceInProvisioningPlan(key);
					if (force != null) {
						AttributeRequest attr = new AttributeRequest();
						attr.setName(key);
						if (operation.equals(ObjectOperation.Modify)) {
							attr.setOp(ProvisioningPlan.Operation.Set);
						} else if (operation.equals(ObjectOperation.Create)){
							attr.setOp(Operation.Add);
						}
						attr.setValue(value);
						request.add(attr);
					}
				}
			}
		}
		
		return plan;
	}
	
	/**
	 * Executes the Provisioning plan directly to the connector
	 * 
	 * @param plan
	 * @throws GeneralException
	 */
	public void executeProvisioningPlan(ProvisioningPlan plan) throws GeneralException {
		executeProvisioningPlan(plan, null, false);
	}
	
	/**
	 * Executes the Provision Plan using the Provisioner class
	 * @param plan
	 * @param args
	 * @throws GeneralException
	 */
	public void executeProvisioningPlan(ProvisioningPlan plan, Attributes<String,Object> args) throws GeneralException {
		executeProvisioningPlan(plan, null, true);
	}
	
	/**
	 * Executes the provisioning plan with arguments to be sent to the Provisioner
	 * 
	 * @param plan
	 * @param args	Provisioner arguments Attributes map
	 * @param useProvisioner	True = Use the Provisioner class to provision the plan
	 * 							False = Use the connector to provision the plan and ignore
	 * 									the args attributes map.
	 * @throws GeneralException
	 */
	public void executeProvisioningPlan(ProvisioningPlan plan, Attributes<String,Object> args, boolean useProvisioner) throws GeneralException {
		
		log.debug("Entering executeProvisioningPlan");
		
		ProvisioningProject project = null;
		Provisioner provisioner = new Provisioner(this.context);;
		
		// Decide if we are using the provisioner, please note that if this
		// is an account plan and we are using the provisioner
		// then we will need an identity to associate with it!
		
	
		if (args != null) {
			project = provisioner.compile(plan, args);
		} else {
			project = provisioner.compile(plan);
		}			
		if (useProvisioner) {
			log.debug("Sending to Provisioner, plan cannot be sent direct to connector.provision method as application does not support PROVISION");
			provisioner.execute(project);
		} else {
		
			for (ProvisioningPlan p : project.getPlans()) {
			
				String applicationName = p.getTargetIntegration();
				if (log.isDebugEnabled()) log.debug("Executing Plan for " + applicationName + " = " + project.toXml());
				Application application = context.getObjectByName(Application.class, applicationName);
				List<Application.Feature> features = application.getFeatures();
				
				if (features == null || features.size() == 0 || !features.contains(Feature.PROVISIONING)) {
					
					// We can't send to the connector as it does not allow provisioning! 
					// so we need to throw an exception saying it's not supported
					
					throw new GeneralException("Plan to '" + p.getTargetIntegration() + " cannot be sent direct to connector.provision method as application does not support PROVISION");
					
				} else {					
					
					Connector connector = sailpoint.connector.ConnectorFactory.getConnector(application, null);
					if (log.isDebugEnabled()) log.debug("Direct connector used for provisioning: " + connector.getClass().getName());
					
					try {
						connector.provision(p);
						log.debug("Completed provisioning direct to the connector");
					} catch (ConnectorException e) {
						throw new GeneralException("Transmogrifier could not execute the connector.provision", e);
					}
				}
			
			}
			log.debug("Project Executed, Exiting executeProvisioningPlan");
		}
			
	}
	
	
	private AttributeDefinition keyExistsInSchema(String key, Schema schema) {
		
		String keyDefinition = containsIgnoreCase(key, schema.getAttributeNames());
		if (keyDefinition != null) return schema.getAttributeDefinition(keyDefinition);
		return null;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	private String keyExistsInForceInProvisioningPlan(String key) {
		
		
		if (forceInProvisioningPlan != null) {
			String[] force = forceInProvisioningPlan.split(",");
			if (force.length > 0) {
				for (int i=0; i < force.length; i++) {
					if (force[i].toLowerCase().equals(key.toLowerCase())) {
						return key;
					}
				}
			}
		}
		return null;
		
	}
}