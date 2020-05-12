package sailpoint.services.standard.connector;

import sailpoint.api.Provisioner;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.connector.AbstractConnector;
import sailpoint.connector.AbstractObjectIterator;
import sailpoint.connector.Connector;
import sailpoint.connector.ConnectorException;
import sailpoint.object.Application;
import sailpoint.object.Application.Feature;
import sailpoint.object.AttributeDefinition;
import sailpoint.object.AttributeDefinition.UserInterfaceInputType;
import sailpoint.object.AttributeMetaData;
import sailpoint.object.Attributes;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.ObjectAttribute;
import sailpoint.object.ObjectConfig;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AbstractRequest;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.object.ProvisioningPlan.ObjectOperation;
import sailpoint.object.ProvisioningResult;
import sailpoint.object.QueryOptions;
import sailpoint.object.ResourceObject;
import sailpoint.object.Schema;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/** @author christian.cairney@sailpoint.com
 * 
 * IdentityIQ Loopback connector.
 *  
 *    o  Any object type to be modelled as entitlements
 *    o  Use provisioning plans instead of the API to avoid locking issues
 *    o  Schema dictated by Application schema and not hard coded 
 *    o  Delta aggregation support
 *    o  Performance improvements
 *  
 *  Class connects to IdentityIQ and aggregates Identities as accounts,
 *  and any other attribute from the identity.
 *  
 *  Default configuration has Workgroups are encapsulated as workgroups and
 *  Capabilities are brought in as is capabilities.  This can be changed
 *  by the implementer
 *  
 *  22 June 2017 - Fixed bug with NPE on object type with no filter
 * 
 * */
public class LoopbackConnector extends AbstractConnector {
	
	static final Logger log = Logger.getLogger(LoopbackConnector.class);

	public final static String ATTR_NAME = "name";
	public final static String ATTR_FIRSTNAME = "firstname";
	public final static String ATTR_LASTNAME = "lastname";
	public final static String ATTR_DISPLAYNAME = "displayName";
	public final static String ATTR_WORKGROUPS = "workgroups.name";
	public final static String ATTR_CAPABILITIES = "capabilities.name";
	public final static String ATTR_INACTIVE_FLAG = "inactive";
	
	public final static String CONNECTOR_TYPE = "IdentityIQ Connector";

	public final static String APP_ATTR_COMMON_DELTA_POSTFIX = "DeltaTimeStamp";
	public final static String APP_ATTR_ATTRIBUTE_TRANSFORM_MAP = "attributeTransformMap";
	public final static String APP_ATTR_IGNORE_IDENTITIES_WITH_NO_ENTITLETMENTS= "ignoreIdentitiesWithNoEntitlements";
	public final static String APP_ATTR_IGNORE_NON_CORRELATED_IDENTITIES = "ignoreNonCorrelatedIdentities";
	public final static String APP_ATTR_SAILPOINT_OBJECT_CLASS_FILTERS = "sailpointObjectClassFilters";
	public final static String APP_ATTR_IDENTITY_FILTER = "identiyFilter";
	
	public final static String AGGREGATION_DELTA = "deltaAggregation";
	
	public final static String TYPE_WORKGROUP = "workgroup";
	public final static String TYPE_CAPABILITY = "capability";
	
	private static final String	IIQ_OBJECT_NAME_WORKGROUP	= "Workgroup";
	private static final String	IIQ_OBJECT_NAME_IDENTITY	= "Identity";
	private static final String	IIQ_OBJECT_NAME_CAPABILITY	= "Capability";
	
	private Map<String,String> attributeMap = null;
	private Map<String,String> objectFilter = null;
	private Boolean ignoreNonCorrelated = false;
	private Boolean ignoreNullEntitlements = false;
	private Filter identityFilter = null;
	
	// Application context
	private SailPointContext context = null;

	/**
	 * Constructor
	 * @param application
	 * @throws GeneralException
	 */
	public LoopbackConnector(final Application application)
			throws GeneralException {

		super(application);
		
		log.debug(String.format("Constructor: IdentityIQConnector(%s)",
				application));

		init(application);

	}

	/**
	 * Constructor
	 * @param application
	 * @param instance
	 * @throws GeneralException
	 */
	public LoopbackConnector(final Application application,
			final String instance) throws GeneralException {
		
		super(application, instance);
		
		log.debug(String.format("Constructor: IdentityIQConnector(%s, %s)",
				application, instance));

		init(application);

	}

	/**
	 * Initialise, used on either constructors
	 * 
	 * @throws GeneralException
	 */
	private void init(Application app) throws GeneralException {
		
		log.debug("Init");
		context = SailPointFactory.getCurrentContext();
		Object attributeMapValue =  app.getAttributeValue(APP_ATTR_ATTRIBUTE_TRANSFORM_MAP);
		if (attributeMapValue!= null && attributeMapValue instanceof Map) attributeMap = (Map<String,String>) attributeMapValue;
		
		this.ignoreNullEntitlements = app.getBooleanAttributeValue(APP_ATTR_IGNORE_IDENTITIES_WITH_NO_ENTITLETMENTS);
		this.ignoreNonCorrelated = app.getBooleanAttributeValue(APP_ATTR_IGNORE_NON_CORRELATED_IDENTITIES);
		
		Object objectFilterValue = app.getAttributeValue(APP_ATTR_SAILPOINT_OBJECT_CLASS_FILTERS);
		if (objectFilterValue!= null && objectFilterValue instanceof Map) objectFilter = (Map<String,String>) objectFilterValue;
		
		String identityFilterSource = app.getStringAttributeValue(APP_ATTR_IDENTITY_FILTER);
		
		if (identityFilterSource != null && identityFilterSource.length()>0) identityFilter = Filter.compile(identityFilterSource);
		
		if (log.isDebugEnabled()) {
			
			if (attributeMap == null) {
				log.debug("Attribute map is null");
			} else {
				log.debug("Initialised attribute map: " + attributeMap.toString());
			}
			
			if (objectFilter == null) {
				log.debug("objectFilter map is null");
			} else {
				log.debug("Initialised objectFilter map: " + objectFilter.toString());
			}
		}
		
		if ( app.getSchemas() == null || app.getSchemas().size() == 0) {
			log.debug("Getting default schema objects");
			app.setSchemas(getDefaultSchemas());
			if (log.isDebugEnabled()) log.debug("Appication: " + app.toXml());
		} else if (app != null) {
			log.debug("Schema has been configured");
		} else {
			log.debug("Application has not been persisted, cannot set default schema");
		}
		
		log.debug("Exiting init");
		
	}

	/**
	 * Test connection. Will always return OK in this case.
	 *
	 * @throws ConnectorException
	 */

	@Override
	public void testConfiguration() throws ConnectorException {
		return;
	}

	@Override
	public String getConnectorType() {
		log.debug("Entering getConnectorType()");
		
		//
		//
		
		if (log.isDebugEnabled()) log.debug("Exiting with value " + CONNECTOR_TYPE);
		
		return CONNECTOR_TYPE;
	}

	/**
	 * Get a single object by it's name.
	 *
	 * @param objectType
	 * @param identityName
	 * @param options
	 * @return
	 * @throws ConnectorException
	 */
	@Override
	public ResourceObject getObject(final String objectType,final String identityName, final Map<String, Object> options) throws ConnectorException {
		
		if (log.isDebugEnabled()) log.debug(String.format("Enter: getObject(%s, %s, %s)", objectType, identityName, options));

		Application app = getApplication();
		Schema schema = app.getSchema(objectType);
		
		if (schema ==  null) throw new ConnectorException("Application object " + app.getName() + " does not support object type " + objectType);
		
		ResourceObject returnObject = null;
		try {
			returnObject = createResourceObject(null, identityName, schema);
		} catch (GeneralException e) {
			log.error("Could not get resource object in getObject method",e);
		}
		
		return returnObject;
	}

	/**
	 * Search for matching objects and return an iterator.
	 *
	 * @param objectType
	 * @param filter
	 * @param options
	 * @return
	 * @throws ConnectorException
	 */
	@Override
	public AbstractObjectIterator<ResourceObject> iterateObjects(final String objectType, final Filter filter, final Map<String, Object> options) throws ConnectorException {
		
		if (log.isDebugEnabled()) log.debug(String.format("Enter: iterateObjects(%s, %s, %s)",objectType, filter, options));
		
		List<Filter> localFilters = new ArrayList<Filter>();
		if (filter != null) localFilters.add(filter);
		
		Boolean delta = false;
		Date deltaDate = null;
		Date newDeltaDate = new Date();
		String appAttrDeltaAttribute = null;

		AbstractObjectIterator<ResourceObject> it = null;

		Application app = getApplication();
		if (app == null) throw new ConnectorException("Could not get application to iterate on.");
		Schema schema = app.getSchema(objectType);
		
		if (schema == null) throw new ConnectorException("Could not get schema for object type " + objectType);
		
		String nativeObjectType = schema.getNativeObjectType();
		if (nativeObjectType == null) throw new ConnectorException("Could not find native object type from connector object type " + objectType);

		if (options != null && options.containsKey(AGGREGATION_DELTA)) {

			delta = (options.get(AGGREGATION_DELTA).toString().toLowerCase().equals("true")) ? true : false;
			if (log.isDebugEnabled()) log.debug("Delta flag has been set to " + String.valueOf(delta));
		} else {
			log.debug("Delta flag is not being used.");
		}
		
		if (objectType != null && objectType.equals(TYPE_ACCOUNT)) {
			if (this.ignoreNullEntitlements ) localFilters.add(getFilterIdentityWithoutEntitlements());
			if (this.ignoreNonCorrelated) localFilters.add(getFilterIdentityCorrelated());
			if (this.identityFilter != null) localFilters.add(this.identityFilter);
		}
			
		appAttrDeltaAttribute = schema.getObjectType() + APP_ATTR_COMMON_DELTA_POSTFIX;
		if (log.isDebugEnabled()) log.debug("Delta attribute name for object type " + schema.getObjectType() + "is " + appAttrDeltaAttribute);

		// Add object type filer if required
		Filter f = getIdentityIQObjectFilter(schema);
		if (f != null) localFilters.add(f);

		// If delta is needed then attempt to add it here
		if (delta) {
			deltaDate = app.getAttributes().getDate(appAttrDeltaAttribute);
			if (deltaDate != null) {
				// Add delta filter if need be
				localFilters.add(Filter.or(Filter.gt("modified", deltaDate), Filter.gt("created", deltaDate)));
			}
		}

		Filter localFilter = null;
		
		if (localFilters.size() > 1) {
			localFilter = Filter.and(localFilters);
		} else if (localFilters.size() == 1) {
			localFilter = localFilters.get(0);
		} else {
			localFilter = null;
		}
		try {
			if (log.isDebugEnabled()) log.debug("Filter: " + (localFilter != null ? localFilter.toXml() : "NO LOCAL FILTER"));
			it = new LoopbackIterator(schema, localFilter);

		}

		catch (final GeneralException ex) {
			log.error("Error getting iterator", ex);
			throw new ConnectorException(ex);
		}
	
		
		// Update the app object with the new delta date
		try {
			app.setAttribute(appAttrDeltaAttribute, newDeltaDate);
			
			context.saveObject(app);
			context.commitTransaction();
		} catch (GeneralException e) {
			log.warn("Could not save application delta time stamp",
					e);
		}
		
		log.debug("Returning iterator");
		return it;
	}


	/**
	 * Provision method uses the IdentityIQ internal provisioning 
	 * methods instead of direct API calls.
	 * 
	 */
	@Override
	public ProvisioningResult provision(final ProvisioningPlan plan)
			throws ConnectorException, GeneralException {
		
		log.debug("Entering provision");

		ProvisioningResult result = new ProvisioningResult();

		if (plan != null) {

			ProvisioningPlan iiqPlan = (ProvisioningPlan) plan.deepCopy(context);
			Identity identity = context.getObjectByName(Identity.class,iiqPlan.getNativeIdentity());
			if (identity == null) throw new GeneralException("Could not find identity: " + iiqPlan.getNativeIdentity());
			
			iiqPlan.setIdentity(identity);
			
			if (log.isDebugEnabled())
				log.debug("IIQPlan:" + iiqPlan.toXml());

			List<AccountRequest> requests = iiqPlan.getAccountRequests();
			List<AccountRequest> removeRequests = new ArrayList<AccountRequest>();
			
			if (requests != null) {
				// Transform the account requests to IIQ Application
				
				Iterator<AccountRequest> itRequests = requests.iterator();
				
				while (itRequests.hasNext()) {
					
					AccountRequest ar  = itRequests.next();
					
					if (log.isDebugEnabled()) log.debug(String.format("Account request operation: %s.", ar.getOp()));

					// Change the application target to IIQ
					ar.setApplication(ProvisioningPlan.APP_IIQ);
					
					//We may need to change the provisioning plan's
					// attribute request name...
					List<AttributeRequest> attributeRequests = ar.getAttributeRequests();
					
					if (attributeRequests != null) {
						for (AttributeRequest attributeRequest : ar.getAttributeRequests()) {
							
							if (attributeMap.containsKey(attributeRequest.getName())) {
								if (log.isDebugEnabled()) log.debug("Transformed Attribute request from: " + attributeRequest.toXml() );
								attributeRequest.setName(attributeMap.get(attributeRequest.getName()));
								if (log.isDebugEnabled()) log.debug("To: " + attributeRequest.toXml() );
							}
						}
					} else {
						log.debug("No attribute requests to process");
					}
					
					// Further transforms may be necessary depending on the
					// operation
					ProvisioningPlan.ObjectOperation op = ar.getOp();

					switch (op) {

						case Disable:
							
							ar.setOp(ProvisioningPlan.ObjectOperation.Modify);
							ar.add(new AttributeRequest(ATTR_INACTIVE_FLAG,ProvisioningPlan.Operation.Set,"true"));
							
							break;
					
						case Enable:
							
							ar.setOp(ProvisioningPlan.ObjectOperation.Modify);
							ar.add(new AttributeRequest("inactive",ProvisioningPlan.Operation.Set,""));
							break;
						
						case Create:
							// Create provisioning plan should be transformed to 
							// a modify and the native identity set to the identities
							// name().
							
							log.debug("Transforming a Create provisioning plan to Modify");
							
							ar.setOp(ProvisioningPlan.ObjectOperation.Modify);
							ar.setNativeIdentity(iiqPlan.getNativeIdentity());
							result.setStatus(ProvisioningResult.STATUS_COMMITTED);
							
							setAccountRequestStatus(ar, ProvisioningResult.STATUS_COMMITTED);

							
							break;
						case Modify:
	
							// Send modify as is to the provisioner
							result.setStatus(ProvisioningResult.STATUS_COMMITTED);
							setAccountRequestStatus(ar, ProvisioningResult.STATUS_COMMITTED);
							
							break;
	
						case Set:
							
							// Send set as is to the provisioner
							result.setStatus(ProvisioningResult.STATUS_COMMITTED);
							setAccountRequestStatus(ar, ProvisioningResult.STATUS_COMMITTED);
							
						case Delete:
	
							// Deletes are not supported, so we should remove this plan							
							removeRequests.add(ar);
							
							break;
						default:
	
							String unsupportedOp = String.format("Unsupported operation: %s", op);
							log.debug(unsupportedOp);
							result.addError(unsupportedOp);

							// We do not know what this request is, so remove it!
							removeRequests.add(ar);
							
							break;
					}
					
					
				}
			} else {
				throw new ConnectorException(
						"No account request found for operation");
			}

			// Ok, if this isn't something we are managing ourselves, then we need
			// to send it off to the provisioner as it should be now transformed
			// to target IdentityIQ internal application.
			
			
			if (iiqPlan.getAccountRequests()!= null && iiqPlan.getAccountRequests().size() > 0) {

				
				// Remove any surplus request objects that we do not need.
				for (AccountRequest ar : removeRequests) {
					if (log.isDebugEnabled()) log.debug("Removing account request type from plan:" + ar.getType());
					iiqPlan.remove(ar);
				}
				
				if (log.isDebugEnabled())
					log.debug(String.format("Transformed Plan: %s", iiqPlan.toXml()));
				
				Provisioner provisioner = new Provisioner(context);
				
				log.trace("Compiling plan");
				
				Attributes<String,Object> options = new Attributes<String, Object>();
				
				provisioner.setNoLocking(true);		
				provisioner.setSource(iiqPlan.getSource());
				//provisioner.setOptimisticProvisioning(true);
				
				// Compile the plan and off we go
				provisioner.compile(iiqPlan, options);
				

				log.trace("Executing provisioner");
				provisioner.execute();
				log.trace("Provisioner executed");

				result.setObject(getObject(this.TYPE_ACCOUNT, plan.getNativeIdentity(), null));
				result.setStatus(ProvisioningResult.STATUS_COMMITTED);
				
				if (log.isDebugEnabled()) log.debug("Setting result object: " + result.toXml());
				
			} else {

				if (result.getErrors() == null|| result.getErrors().size() == 0) {
					result.setStatus(ProvisioningResult.STATUS_COMMITTED);
				} else {
					result.setStatus(ProvisioningResult.STATUS_FAILED);
				}
			}

		} else {
			throw new ConnectorException(
					"No plan found for requested operation");
		}


		plan.setResult(result);

		log.debug("Exiting provision");
		
		return result;
	}
	
	
	/**
	 * Set the account status based on the operation status
	 * 
	 * @param ar
	 * @param status
	 */
	private void setAccountRequestStatus(AbstractRequest ar, String status) {
		
		ProvisioningResult result = new ProvisioningResult();
		result.setStatus(status);
		setAccountRequestStatus(ar, result);
		
	}
	
	/**
	 * Set the account status based on a ProvisioningResult
	 * @param ar
	 * @param result
	 */
	private void setAccountRequestStatus(AbstractRequest ar, ProvisioningResult result) {
		
		ar.setResult(result);
		if (ar.getAttributeRequests() != null) {
			for (AttributeRequest attr : ar.getAttributeRequests()) {
				attr.setResult(result);
			}
		}
	}
	

	/**
	 * Create a resource object from an object request
	 *
	 * @param identity
	 * @param objectType
	 * @return
	 * @throws GeneralException 
	 */
	ResourceObject createResourceObject(String id, String name, Schema objectSchema) throws GeneralException {
		
		if (log.isDebugEnabled()) log.debug(String.format("Entering createResourceObject ID=%s, Name=%s and type=%s", id, name, objectSchema.toXml()));
		
		ResourceObject resourceObject = new ResourceObject();
		
		// Work out the default filtering options
		List<Filter> filterList = new ArrayList<Filter>();
		Class objectClass = null;
	
		// See if the object type needs an additional filter then
		// if required, add it to the filter list
		Filter f = getIdentityIQObjectFilter(objectSchema);
		if (f != null) filterList.add(f);
	
		if (id != null) filterList.add(Filter.eq("id", id));
		if (name != null) filterList.add(Filter.eq("name", name));
		
		Filter filter = Filter.and(filterList);
		QueryOptions qo = new QueryOptions(filter);
		
		//Application app = this.getApplication();

		Class nativeObjectClass = getIdentityIQObjectType(objectSchema);
		resourceObject.setObjectType(objectSchema.getObjectType());

		// Default flags
		resourceObject.setAttribute(Connector.ATT_IIQ_DISABLED, "false");
		
		List<String> attributeNames = objectSchema.getAttributeNames();
		Iterator<Object[]> it = context.search(nativeObjectClass, qo, attributeNames);
		
		int entitlementCount = 0;
		
		if (it.hasNext()) {
			
			while (it.hasNext()) {
				
				Object[] values = it.next();
				for (int i=0; i < values.length; i++) {
					
					String attributeName = attributeNames.get(i);
		
					Object value = values[i];
					AttributeDefinition attrDef = objectSchema.getAttributeDefinition(attributeName);
					
					if (log.isDebugEnabled()) log.debug("Transforming field " + attributeName + "=" + value);
					
					if (attributeName.equals(objectSchema.getIdentityAttribute())) {
						resourceObject.setIdentity((String) value);
					} else if (attributeName.equals(objectSchema.getDisplayAttribute())) {
						resourceObject.setDisplayName((String) value);
					} 
						
					if (attrDef.isMulti()) {
						
						List fieldValues = (List) resourceObject.get(attributeName);
						
						if (fieldValues == null) {
							fieldValues = new ArrayList();
							resourceObject.setAttribute(attributeName, fieldValues);
						}
						if (value != null && !fieldValues.contains(value)) fieldValues.add(value);
						
					} else {
						if (value != null) resourceObject.setAttribute(attributeName, value);
					}
					
					// Determine special attributes for account types
					if (objectSchema.getObjectType().equalsIgnoreCase(TYPE_ACCOUNT)) {
						
						if (attributeName.equals(ATTR_INACTIVE_FLAG)) {
							resourceObject.setAttribute(Connector.ATT_IIQ_DISABLED, value);
						}
					}
					
					if (attrDef.isEntitlement() && value != null) entitlementCount++;
				}
			}
			
			
		} else {
			log.error("Ambiguous filter did not return any records: " + filter.toXml());
			throw new GeneralException("Ambiguous filter did not return any identity in createResourceObjectFromIdentity");
		}
	
		if (log.isTraceEnabled()) log.trace(String.format("Resource object: %s", resourceObject.toXml()));	
		
		return resourceObject;
	}

	/**
	 * 
	 */
	@Override
	protected void finalize() throws Throwable {
		log.debug("finalize");
		super.finalize();
	}

	/**
	 * 
	 * @author christian.cairney
	 *
	 */
	private class LoopbackIterator extends AbstractObjectIterator<ResourceObject>  {

		private final Iterator<Object[]> iter;
		private final Class nativeObjectType;
		private final Schema objectSchema;

		// private SailPointContext context;
		// private boolean cleanContext = false;

		public LoopbackIterator(Schema schema,  Filter filt) throws GeneralException {
			
			super(schema);
			log.debug("Entering IiqWorkgroupConnectorIterator");
			
			objectSchema = schema;
			
			final QueryOptions qo = new QueryOptions();
			qo.setDistinct(true);
			if (filt != null) qo.add(filt);
			qo.setResultLimit(0);

			nativeObjectType = getIdentityIQObjectType(schema);
			Iterator<Object[]> it = context.search(nativeObjectType, qo, "id");
			
			List<Object[]>  iterator = new ArrayList<Object[]> ();
			
			// We have to copy the iterator otherwise the calling method
			// we iterates through this has a nasty habit of closing the
			// iterator at 100 objects... don't know why yet
			//
			// TODO:  	find a way of removing this code, luckily it does
			//			not perform too badly for 1000's of objects.
			//
			
			while (it.hasNext()) {
				iterator.add(it.next());
			}
			
			iter = iterator.iterator();
			
			log.debug("Exiting IiqConnectorIterator");

		}

		@Override
		public boolean hasNext() {
			
			log.debug("Entering hasNext()");
			boolean hasNext = iter.hasNext();
			
			if (log.isDebugEnabled() ) log.debug("Exiting hasNext() = " + String.valueOf(hasNext));
			
			return hasNext;
		}

		@Override
		public sailpoint.object.ResourceObject next() {
			
			
			log.debug("Entering Iterator next()");
			
			sailpoint.object.ResourceObject resourceObj = null;
			
			if (iter.hasNext()) {
				
				log.debug("Iterator has next()");
				
				final Object[] item = iter.next();				
				final String id = (String) item[0];
				
				if (log.isDebugEnabled()) log.debug("Iterating on ID=" + id); 

				try {
					resourceObj = createResourceObject( id, null, objectSchema);
				} catch (GeneralException e) {
					log.error("Could not create resource object, returning null", e);
				}
				
				return resourceObj;
			}
			
			if (log.isTraceEnabled()) {
				try {
					log.trace("Exiting Iterator next() with resource object:" + resourceObj.toXml());
				} catch (GeneralException e) {
					log.debug("Exiting Iterator next(), could not serialise resource object"); 
				}
			} else {
				log.debug("Exiting Iterator next()");
			}
			
			return resourceObj;
		}

		@Override
		public void close() {
			// Avoid memory leaks by flushing the iterator
			Util.flushIterator(iter);
			log.debug("iterator closed");
		}

		@Override
		protected void finalize() throws Throwable {
			log.debug("finalize");
			super.finalize();

		}
	}
	
	/**
	 * From the application schema, get the object types name and return the
	 * IdentityIQ sailpoint object class to query against.
	 * 
	 * @param schema
	 * @return
	 * @throws GeneralException
	 */
	private Class getIdentityIQObjectType(Schema schema) throws GeneralException {
		
		Class nativeObjectType;
		
		try {
			
			String nativeObjectTypeName = schema.getNativeObjectType();
			if (nativeObjectTypeName.equals(IIQ_OBJECT_NAME_WORKGROUP)) {
				nativeObjectTypeName = "sailpoint.object.Identity";
			} 

			nativeObjectType = Class.forName( (nativeObjectTypeName.startsWith("sailpoint.object") ? nativeObjectTypeName : "sailpoint.object." + nativeObjectTypeName ));
			
		} catch (ClassNotFoundException e) {
			throw new GeneralException("Could not find object class " + schema.getNativeObjectType());
		}
		return nativeObjectType;
		
	}
	
	/**
	 * If an object filter is needed for the given object type
	 * then this function will return it, otherwise expect
	 * null.
	 * 
	 * @param schema
	 * @return
	 */
	private Filter getIdentityIQObjectFilter(Schema schema) {
		
		Filter filter = null;
		String objectType = schema.getNativeObjectType();

		try {
			System.out.println("Schema: " + schema.toXml());
		} catch (GeneralException e1) {
			
		}
		
		if (objectFilter == null) {
			
			// Defaults to base object filter for Workgroup and Identity if no
			// object filter is specified in the config
			
			if (objectType.equals(IIQ_OBJECT_NAME_IDENTITY)) {
				filter = Filter.ne("workgroup", true);
			} else if (objectType.equals(IIQ_OBJECT_NAME_WORKGROUP)) {
				filter = Filter.eq("workgroup", true);
			}
		} else {
			
			// We have a filter object.
			if (log.isDebugEnabled()) {
				log.debug("Object type: " + objectType);
				log.debug("Object filters:" + objectFilter.toString());
			}
			
			String filterExpression = objectFilter.get(objectType);
			if (log.isDebugEnabled()) log.debug("Object filter: " + filterExpression);
			if (filterExpression != null) filter = Filter.compile(filterExpression);
		}
		
		if (log.isDebugEnabled())
			try {
				
				if (filter != null)
					log.debug("Filter returned from IQObjectFilter: " + filter.toXml());
				else
					log.debug("No filter returned, returning NULL");
				
			} catch (GeneralException e) {
				log.error("Cannot return IQObjectFilter expression due to unexpected error.", e );
			}
		
		return filter;
		
	}
	
	public List<Feature> getSupportedFeatures() {
		
		List<Feature> features = new ArrayList<Feature>();

		features.add(Application.Feature.SYNC_PROVISIONING);
		features.add(Application.Feature.PROVISIONING);
		features.add(Application.Feature.SEARCH);

		return features;
		
	}
	
	/**
	 * Using reflection, try to find the method, used for backwards compatibility
	 * 
	 * @param instanceObject
	 * @param methodName
	 * @param args
	 * @return
	 */
	private Method getMethod(Object instanceObject, String methodName, Object... args) {
		

		Method[] methods = instanceObject.getClass().getMethods();
		for (int i=0; i < methods.length; i++) {
			
			Method method = methods[i];
			if (method.getName().equals(methodName)) {
				Class[] clazzes = method.getParameterTypes();
				if (clazzes.length == args.length) {
					boolean match = true;
					for (int clazzCount=0; clazzCount < clazzes.length; clazzCount++) {
						Class clazz = clazzes[clazzCount];
						Object arg = args[clazzCount];
						if (!arg.getClass().equals(clazz)) {
							match = false;
							break;
						}
					}
					if (match) {
						return method;
					}
				}
			}
			
		}
		return null;
	}
	
	@Override
	public List<Schema> getDefaultSchemas() {
		log.debug("Enter getDefaultSchemas()");
		List<Schema> schemaList = super.getDefaultSchemas();
		
		String spVersion = sailpoint.Version.getVersion();
		
		try {
			if (spVersion.equals("5.5") ||
					spVersion.equals("6.0") ||
					spVersion.equals("6.1") ||
					spVersion.equals("6.2") ||
					spVersion.equals("6.3")) {
				
				schemaList.add(discoverSchema(TYPE_ACCOUNT,
					new HashMap<String, Object>()));
				schemaList.add(discoverSchema(TYPE_GROUP,
					new HashMap<String, Object>()));
				
			} else  {
				schemaList.add(discoverSchema(TYPE_ACCOUNT,
					new HashMap<String, Object>()));
				schemaList.add(discoverSchema(TYPE_GROUP,
					new HashMap<String, Object>()));
				schemaList.add(discoverSchema(TYPE_CAPABILITY,
					new HashMap<String, Object>()));
			}
		} catch (final ConnectorException ex) {
			log.error("Error discovering schemas", ex);
		}
		return schemaList;

	}

	
	/**
	 * Dictate the schema for the three support object types, other types can be manually added
	 * but has not been tested at this time.
	 * 
	 */
	public Schema discoverSchema(final String objectType,final Map<String, Object> options) throws ConnectorException {
		
		if (log.isDebugEnabled()) log.debug(String.format("Enter: discoverSchema(%s, %s)", objectType,	options));
		
		Application app = this.getApplication();
		Schema schema = app.getSchema(objectType);
		if (log.isDebugEnabled()) {
			try {
				log.debug("Schema: " + (schema == null ? "NULL" : schema.toXml()));
			} catch (GeneralException e1) {
				log.debug("Cannot output schema");
			}
		}
		if (schema == null) {
			schema = new Schema();
			schema.setObjectType(objectType);
		} else {
			schema.setAttributes(new ArrayList<AttributeDefinition>());
		}
		
		String nativeObject = schema.getNativeObjectType();
		if (nativeObject == null) throw new ConnectorException("Native object type must be specified");
		
		try {
			// Check to see if the class exists
			if (!nativeObject.equals("Workgroup")) {
				Class clazz = Class.forName("sailpoint.object." + nativeObject);
			}
		} catch (ClassNotFoundException e1) {
			
			throw new ConnectorException("Class was not fond for native object type " + nativeObject, e1);
		}
		
		// Account schema
		log.debug("Getting " + nativeObject + " schema");
		addAttributeDefinitionFromObject(schema, nativeObject);
		
		if (log.isDebugEnabled())
			try {
				log.debug("Returning schema: " + schema.toXml());
			} catch (GeneralException e) {
				log.error("Could not return schema");
			}
		
		return schema;
	}
	
	void addAttributeDefinitionFromObject(Schema schema, String type) {
		
		log.debug("Starting addAttributeDefinitionFromObject");
		
		ObjectConfig objectConfig = null;
		try {
			objectConfig = context.getObjectByName(ObjectConfig.class, type);
		} catch (GeneralException e) {
			// log the warning and move on
			log.warn("Could not get object config due to error", e);
		}
		
		if (objectConfig != null) {
			List<ObjectAttribute> attributes = objectConfig.getSearchableAttributes();
			for (ObjectAttribute attribute : attributes) {
				
				AttributeDefinition attr = null;
				// If the attribute is a sailpoint object, then make sure we just get the name
				// and not the object!
				if (log.isDebugEnabled()) log.debug("Discovering attribute " + attribute.getName() + " of type " + attribute.getType());
				if (attribute.getType() != null && attribute.getType().startsWith("sailpoint.object.")) {
					log.debug("Fixing up refrence to SailPoint Object");
					attr = new AttributeDefinition(attribute.getName() + ".name", 
							"string", attribute.getDisplayableName(), attribute.isRequired());
				} else {
					attr = new AttributeDefinition(attribute.getName(), 
							(attribute.getType() == null ? "string" : attribute.getType()), 
							attribute.getDisplayableName(), attribute.isRequired());
				
				}
				if (log.isDebugEnabled()) log.debug("Adding attribute: " + attr.toString());
				schema.addAttributeDefinition(attr);
			}
		} 
		
		// Add default naming attribute
		AttributeDefinition attr = new AttributeDefinition(ATTR_NAME, "string", type + " " + ATTR_NAME, true);
		schema.addAttributeDefinition(attr);
		
		if (type.equals("Identity")) {
			AttributeDefinition workgroups = new AttributeDefinition(	ATTR_WORKGROUPS, "string","IdentityIQ Workgroups", false);
			workgroups.setManaged(true);
			workgroups.setEntitlement(true);
			workgroups.setMulti(true);
			workgroups.setRequired(false);
			workgroups.setType("string");
			workgroups.setSchemaObjectType("workgroup");
			schema.addAttributeDefinition(workgroups);
		}
		
		if (type.equals("Identity") || type.equals("Workgroup")) {
			AttributeDefinition capabilities = new AttributeDefinition(ATTR_CAPABILITIES, "string","Workgroup Capabilities", false);
			capabilities.setManaged(true);
			capabilities.setEntitlement(true);
			capabilities.setMulti(true);
			capabilities.setType("string");
			capabilities.setSchemaObjectType("capability");
			capabilities.setRequired(false);
			schema.addAttributeDefinition(capabilities);
		}
		
		if (type.equals("Workgroup")) {
			schema.addAttributeDefinition(ATTR_NAME, "string","Workgroup name", true);
			schema.addAttributeDefinition(ATTR_DISPLAYNAME, "string","Display name", false);
		}
		
	}
	
	/**
	 * Return a filter to exclude identities which do not have
	 * entitlements
	 * 
	 * @return
	 */
	private Filter getFilterIdentityWithoutEntitlements() {
		
		Schema schema = getApplication().getSchema(TYPE_ACCOUNT);
		List andFilter = new ArrayList();
		for (AttributeDefinition attrDef : schema.getAttributes()) {
			if (attrDef.isEntitlement()) {
				andFilter.add(Filter.notnull(attrDef.getName()));
			}
		}
		return Filter.or(andFilter);
		
	}
	
	/**
	 * Return a filter to exclude all non correlated identities.
	 * @return
	 */
	private Filter getFilterIdentityCorrelated() {
		
		return Filter.eq("correlated",true);
	
	}
	
}
