package sailpoint.services.standard.task.genericImport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import sailpoint.api.SailPointContext;
import sailpoint.api.Terminator;
import sailpoint.object.*;
import sailpoint.object.Filter.CompositeFilter;
import sailpoint.object.Filter.LeafFilter;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.tools.GeneralException;


public class ImporterUtil {

	private static final Logger log = Logger.getLogger(ImporterUtil.class);

	
	/**
	 * Get a SailPoint unique object using a filter.  This method will also
	 * allow the auto-creation of stub objects if need be.
	 * 
	 * @param className
	 * @param filter
	 * @param autoCreate
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws GeneralException
	 */
	public static SailPointObject getUniqueObject(SailPointContext context, Class<? extends SailPointObject> className, Filter filter, boolean autoCreate)
			throws InstantiationException, IllegalAccessException, GeneralException {

		SailPointObject ret = null;
		try {
			ret = context.getUniqueObject(className, filter);
		} catch (GeneralException e) {
			log.error("Get object by name returned the following error", e);
		}

		if (ret == null && autoCreate) {
			ret = (SailPointObject) className.newInstance();
			
			// i think this is where we get the leaf collection
			if (log.isDebugEnabled()) {
				log.debug("Got filter: " + filter.getExpression());
				
			}
			
			if (filter instanceof CompositeFilter) {
				
				log.debug("Composite filter detected");
				CompositeFilter composite = (CompositeFilter) filter;
				
				for (Filter child : composite.getChildren()) {
				
			    	LeafFilter leaf = (LeafFilter) child;
			    	
			    	String setterName = leaf.getProperty();
			    	Method m = findClassMethod(className, setterName, 1);
			    	boolean success = methodInvoke(context, m, ret, leaf.getValue());
			    	if (!success) {
			    		throw new GeneralException(String.format("Failed to create object due to methodInvoke failure on method name '%s'.", m.getName()));
			    	}
			    	
			    }
			} else {
				
				log.debug("Leaf filter detected");
				
				LeafFilter leaf = (LeafFilter) filter;
		    	
		    	String setterName = leaf.getProperty();
		    	Method m = findClassMethod(className, setterName,1);
		    	boolean success = methodInvoke(context, m, ret, leaf.getValue());
		    	if (!success) {
		    		throw new GeneralException(String.format("Failed to create object due to methodInvoke failure on method name '%s'.", m.getName()));
		    	}
			}
			
			context.saveObject(ret);
			context.commitTransaction();
			ret = context.getUniqueObject(className, filter);
		}

		return ret;
	}

	/**
	 * 
	 * @param context
	 * @param className
	 * @param filter
	 * @return
	 */
	public static boolean uniqueObjectExists(SailPointContext context, Class<? extends SailPointObject> className, Filter filter) {

		int ret = 0;
		try {
			ret = context.countObjects(className, new QueryOptions(filter));
		} catch (GeneralException e) {
			log.error("Get object by name returned the following error", e);
		}
		return (ret == 1);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Filter getFilterObjectByName(String name) {
		return Filter.eq("name", name);
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Filter getFilterObjectById(String id) {
		return Filter.eq("id", id);
	}
	/**
	 * 
	 * @param applicationName
	 * @param attribute
	 * @param value
	 * @return
	 */
	public static Filter getFilterManagedAttribute(String applicationName, String attribute, String value) {
		List<Filter> and = new ArrayList<Filter>();
		and.add(Filter.eq("application.name", applicationName));
		and.add(Filter.eq("attribute", attribute));
		and.add(Filter.eq("value", value));
		return Filter.and(and);
	}
	
	/**
	 * Allow the deletion/termination of object in IdentityIQ without having to instantiate them.
	 * Projected queries should perform better than object deletes if you do not have
	 * a reference to the object.
	 * 
	 * @param context
	 * @param className
	 * @param objectName
	 * @param objectId
	 * @param allowTerminate
	 * @return
	 * @throws GeneralException
	 */
	public static void removeObject(SailPointContext context, Class<? extends SailPointObject> className, Filter filter) {

		QueryOptions qo = null;
		
		qo = new QueryOptions(filter);

		Terminator term = new Terminator(context);
		try {
			
			term.deleteObjects(className, qo);
			
		} catch (GeneralException e) {
			
			log.error("Could not terminate object in deleteObjectByName", e);
		}


	}

	
	/**
	 * Coarse search for method by matching the parameters
	 * TODO:  We need to move away from this approach and make a better attempt to match the
	 * methods. 
	 * @param setterName
	 * @param clazz
	 * @return
	 */
	private static final Method findClassMethod(Class<?> clazz, String methodName, Integer parameters) {
		
		Method[] methods = clazz.getMethods();
		
		String callMethodName = (methodName.contains(".")) ? methodName.substring(0,methodName.indexOf(".")) : methodName;
		String callSetterMethodName = "set" + callMethodName.substring(0,1).toUpperCase() + callMethodName.substring(1);
		
		Method returnMethod = null;
		for (Method method : methods) {	
			
			int parameterCount = method.getParameterAnnotations().length;
			if (method.getName().equalsIgnoreCase(callMethodName) || method.getName().equalsIgnoreCase(callSetterMethodName)) {
				if (parameters == null) {
					returnMethod = method;
					break;
				} else if (parameterCount == parameters) {
					returnMethod = method;
					break;
				}	
			}
		}
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
	private static boolean methodInvoke(SailPointContext context, Method m, SailPointObject object, Object... params) {

		log.debug("Entering methodInvoke");
		boolean success = false;

		try {
			
			int parameterCount = m.getParameterAnnotations().length;
			
			if (log.isDebugEnabled()) 
				log.debug(String.format("Method: '%s', number of parameters: %d", m.getName(), String.valueOf(parameterCount)));
				
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
							newValue = getUniqueObject(context, (Class<? extends SailPointObject>) methodType, getFilterObjectByName((String) parameter), true);
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
	private static final boolean isSailPointObject(Class<?> clazz) {
		
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
	 * 
	 * @param plan
	 * @param identity
	 * @param roleNames
	 * @param operation
	 * @return
	 * @throws GeneralException
	 */
	public static ProvisioningPlan createRoleAssignmentPlan(ProvisioningPlan plan, Identity identity, List<String> roleNames, ProvisioningPlan.Operation operation) throws GeneralException {

		log.debug("Entering createRoleAssignmentPlan");
		if (identity == null) throw new GeneralException("createRoleAssignmentPlan: Identity cannot be null");
		if (roleNames == null) throw new GeneralException("createRoleAssignmentPlan: Role names cannot be null");
		if (operation == null) throw new GeneralException("createRoleAssignmentPlan: Operation cannot be null");
		
		if (operation.equals(ProvisioningPlan.Operation.Add) || 
				operation.equals(ProvisioningPlan.Operation.Remove)) {
			
			if (plan == null) {
				plan = new ProvisioningPlan();
				plan.setIdentity(identity);
				plan.setNativeIdentity(identity.getName());
			}
		
			
			AccountRequest ar = new AccountRequest();
			ar.setApplication(ProvisioningPlan.APP_IIQ);

			for (String roleName : roleNames) {
				
				AttributeRequest attr = new AttributeRequest();
				
				attr.setName("assignedRoles");
				attr.setOperation(operation);
				attr.setValue(roleName);
					
				ar.add(attr);
			}
			
			plan.add(ar);
		
		} else {
			throw new GeneralException("Operation " + operation.name() + " not supported");
		}
		
		log.debug("Exiting createRoleAssignmentPlan");
		return plan;
	}
		
}
