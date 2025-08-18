package AccessRequestApprovalAddins.swe.plugin.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import sailpoint.api.SailPointContext;
import sailpoint.object.ApprovalItem;
import sailpoint.object.ApprovalSet;
import sailpoint.object.Attributes;
import sailpoint.object.WorkItem;
import sailpoint.object.Identity;
import sailpoint.rest.plugin.AllowAll;
import sailpoint.rest.plugin.BasePluginResource;
import sailpoint.tools.GeneralException;

@Path("AccessRequestApprovalAddinsUI")
//@Produces({"application/json"})
//@Consumes({"application/json"})
public class AccessRequestApprovalAddinsUI extends BasePluginResource {
	private static Logger log = Logger.getLogger(AccessRequestApprovalAddinsUI.class);
	int MAX_SEARCH_THRESOLD = 20;
	AccessRequestApprovalAddinsUI objService;

	
	public String getPluginName()
	  {
	    //return this.pluginContext.getSettingString("defaultName");
		  return "AccessRequestApprovalAddins";
	  }

	
	
	
	/* to get the workitem display attribute by passing the id */
	
	@GET
	@Path("getWorkitemDetails")
	@Produces({ "application/json" })
	@AllowAll

	public List getWorkitemDetails(@QueryParam("searchKey") String searchKey) throws GeneralException {
		// public static void main(String[] args) throws GeneralException {
		// TODO Auto-generated method stub
		SailPointContext context = getContext();

		List valuesMapList = new ArrayList();

		String strItemId = searchKey;

		WorkItem workItemObj = context.getObjectById(WorkItem.class, strItemId);
		
		// Attributes<String, Object> attributes = workItemObj.getAttributes();

		ApprovalSet approvalSet = workItemObj.getApprovalSet();

		List<ApprovalItem> items = approvalSet.getItems();

		for (ApprovalItem approvalItem : items) {

			Map testMap = new HashMap();
			
			String nativeIdentity = approvalItem.getNativeIdentity() != null ? approvalItem.getNativeIdentity() : "";
			
			Attributes<String, Object> attributes = approvalItem.getAttributes();

			Map attrMap = attributes != null ? (Map) attributes.getMap() : null;

			String testValue = attrMap != null && attrMap.get("pairedAccountName")!= null ? (String) attrMap.get("pairedAccountName") : "";

			testMap.put("pairedAccountName", testValue);
			
			testMap.put("nativeIdentity", nativeIdentity);

			valuesMapList.add(testMap);

			log.debug("to catch testValue is : " + testValue);

			log.debug("to catch valuesMapList is : " + valuesMapList);
		}

		return valuesMapList;

	}

	
	
	/* function to get the Identity account type flag */

	@GET
	@Path("getIdentityById")
	@Produces({ "application/json" })
	@AllowAll

	public Map getIdentityById(@QueryParam("searchKey") String searchKey) throws GeneralException {
		
		SailPointContext context = getContext();
		Map resultMap = new HashMap();
		
		boolean serviceIdentity = false;
		
		String strItemId = searchKey;
		String type = "";

		if(strItemId != null) {
			Identity identity = context.getObjectById(Identity.class, strItemId);
			type = identity != null && identity.getAttribute("cofAccountType") != null ? (String) identity.getAttribute("cofAccountType") : "";
		}
			
		if(type.equalsIgnoreCase("System")) {
			resultMap.put("serviceIdentity", true);
		}
		else{
		    resultMap.put("serviceIdentity", serviceIdentity);
		}

		return resultMap;

	}

}
