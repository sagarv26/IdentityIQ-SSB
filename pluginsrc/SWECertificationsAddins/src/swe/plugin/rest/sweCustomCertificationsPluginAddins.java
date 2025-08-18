package swe.plugin.rest;

import sailpoint.api.Certificationer;
import sailpoint.api.IdentityService;
import sailpoint.api.ObjectUtil;
import sailpoint.api.SailPointContext;

import sailpoint.object.Application;
import sailpoint.object.AuditEvent;
import sailpoint.object.Bundle;
import sailpoint.object.Certification;
import sailpoint.object.Certification.CertificationStatistics;
import sailpoint.object.Certification.Phase;
import sailpoint.object.CertificationAction;
import sailpoint.object.CertificationAction.RemediationAction;
import sailpoint.object.CertificationEntity;
import sailpoint.object.CertificationItem;
import sailpoint.object.Custom;
import sailpoint.object.Filter.LeafFilter;

import sailpoint.object.EntitlementSnapshot;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.IdentityEntitlement;
import sailpoint.object.Link;
import sailpoint.object.ManagedAttribute;
import sailpoint.object.Profile;

import sailpoint.object.QueryOptions;
import sailpoint.object.RoleAssignment;
import sailpoint.object.RoleTarget;
import sailpoint.object.Rule;
import sailpoint.object.TaskResult;
import sailpoint.rest.plugin.AllowAll;
import sailpoint.rest.plugin.BasePluginResource;
import sailpoint.server.Auditor;
import sailpoint.tools.GeneralException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Map;

import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import sailpoint.api.Workflower;
import sailpoint.object.Workflow;
import sailpoint.object.WorkflowLaunch;
import sailpoint.object.WorkflowSummary;


import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.auth.UsernamePasswordCredentials;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import sailpoint.tools.Message;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import com.swe.openconnector.oauthcustom.utils.HttpUtil;


@Path("sweCertificationsPluginAddins")

@Consumes({"application/xml", "application/json", "text/plain"})
@Produces({"application/xml", "application/json", "text/plain"})
//@RequiredRight("ViewCertifications")
//@RequiredRight("") //show to every one
@AllowAll
public class sweCustomCertificationsPluginAddins extends BasePluginResource {

	private Logger logger = Logger.getLogger(sweCustomCertificationsPluginAddins.class);
	private String SWE_ATTRIBUTE_PRIVILEGED = "sweEntPriv";
	private String SWE_ENT_ATTRIBUTE_SOX = "sweEntSox";
	private String SWE_APP_ATTRIBUTE_SOX = "sweAppSOX";
	private String SWE_ENT_ATTRIBUTE_CBT="sweEntCOUCBTNum";
	private String SWE_ROLE_ATTRIBUTE_CBT="sweCOUCBTNum";
	private String SWE_ENT_ATTRIBUTE_NPI="sweEntNPI";
	private String SWE_ENT_ATTRIBUTE_PCI="sweEntPCI";
	private String SWE_ATTRIBUTE_RELATED_APPLICATION = "sweEntRelatedApplication";
	//private DateFormat uidisplaydateFormat = new SimpleDateFormat("E dd MMM yyyy"); 
	private DateFormat uidisplaydateFormat = new SimpleDateFormat("MMMM d, yyyy"); 
	private DateFormat uidisplayonlydateFormat = new SimpleDateFormat("MMM yyyy");
	public String bearerToken="";
	//DateFormat uidisplaydateFormat = new SimpleDateFormat("E dd MMM yyyy"); 
	private Date currentDate = new Date();
	private int prevalenceRefreshThreshold = 4 ;
	boolean prevalenceScoresAPIHit = false;
	
	public String getPluginName()
	  {
	    //return this.pluginContext.getSettingString("defaultName");
		  return "SWECertificationsAddins";
	  }
	
	
	
	@POST
	  @Path("certsPluginCheckConfig")
	  public Map certsPluginCheckConfig(Map<String, Object> paramMap)
	  {
		//logger.setLevel(Level.DEBUG);
		logger.debug("Enter certsPluginCheckConfig paramMap: "+ paramMap);
		Map configMap=new HashMap();
		
		 String certUrl=(String)paramMap.get("certUrl");
		 String certId="";
		  if(null != certUrl)
			  certId=certUrl.substring(certUrl.lastIndexOf("/")+1,certUrl.length());
		  
		  logger.debug("certId: "+ certId);
		  if(null != certId && !certId.equals(""))
		  {
			  try {
				  SailPointContext context=getContext();
				Certification certObj= context.getObjectById(Certification.class, certId);
					if(null != certObj)
					{
						
						Custom sweCertsCustomObj=context.getObjectByName(Custom.class, "SWE Certification Addins Plugin Custom");		
						String certType=(String)certObj.getType().toString();
						boolean iscertCompleted=certObj.isComplete();
						Phase certPhase=certObj.getPhase();
						int totalCertItems=certObj.getTotalItems();
						logger.debug("certType: "+ certType);
						logger.debug("certPhase: "+ certPhase);
						logger.debug("iscertCompleted: "+ iscertCompleted);
						if(null != sweCertsCustomObj && null != sweCertsCustomObj.get("CertificationsPlugin"))
						{
							Map certsPluginCustMap=(Map)sweCertsCustomObj.get("CertificationsPlugin");
							Map eachTypeMap=(HashMap)certsPluginCustMap.get(certType);
														

							if(null != eachTypeMap &&  null != eachTypeMap.get("RemoveSelectEveryThingThreshold"))	
							{
								int RemoveSelectEveryThingThreshold=Integer.parseInt((String)eachTypeMap.get("RemoveSelectEveryThingThreshold"));
								if(totalCertItems <= RemoveSelectEveryThingThreshold)
								  configMap.put("REMOVESELECTEVERYTHING", true);
								else
								  configMap.put("REMOVESELECTEVERYTHING", false);
								//configMap.put("REMOVESELECTEVERYTHING_COUNT", Integer.parseInt((String)eachTypeMap.get("RemoveSelectEveryThingThreshold")));
								
								
							}
						    
							if(null != eachTypeMap &&  null != eachTypeMap.get("ShowExpandCollapseAll"))							
								configMap.put("SHOW_EXPAND_ALL", Boolean.valueOf((String)eachTypeMap.get("ShowExpandCollapseAll")));
							
							if(null != eachTypeMap &&  null != eachTypeMap.get("ShowFeedbackinSignoffPage"))	
							{
								configMap.put("SHOW_FEEDBACK_IN_SIGNOFF_PAGE", Boolean.valueOf((String)eachTypeMap.get("ShowFeedbackinSignoffPage")));
								
								if(null != eachTypeMap.get("Feedbackquestions"))
								{
									Map<String,HashMap> questionsMap=(HashMap<String,HashMap>)eachTypeMap.get("Feedbackquestions");
									List quesArray=new ArrayList();
									Map eachquesoptionsMap=new HashMap();
									Map eachquesMap=new HashMap();
									
									
									ArrayList<String> questionsList= new ArrayList<String>(questionsMap.keySet());
									logger.debug("before questionsList: "+ questionsList);
									/* Sorting in decreasing order*/
									   Collections.sort(questionsList);
							        
							        logger.debug("after questionsList: "+ questionsList);
							        
									for(String eachquestion: questionsList)
									{
										eachquesMap=new HashMap();
										eachquesMap=(HashMap)questionsMap.get(eachquestion);
										eachquesoptionsMap=new HashMap();
										
										eachquesoptionsMap.put("question", (String)eachquesMap.get("question"));
										eachquesoptionsMap.put("choice", (ArrayList)eachquesMap.get("choice"));										
										quesArray.add(eachquesoptionsMap);
									}
																		
									
								  configMap.put("feedbackQuestions", quesArray);
								}
								
								
							}
							
							if(null != eachTypeMap &&  null != eachTypeMap.get("ShowFixedHeader"))							
								configMap.put("SHOW_FIXED_HEADER", Boolean.valueOf((String)eachTypeMap.get("ShowFixedHeader")));
							
							if(null != eachTypeMap &&  null != eachTypeMap.get("ShowCustomFileExport"))							
								configMap.put("SHOW_CUSTOM_FILE_EXPORT", Boolean.valueOf((String)eachTypeMap.get("ShowCustomFileExport")));
							
							
							if(null != eachTypeMap &&  null != eachTypeMap.get("ShowFileDecisionsImport") && !iscertCompleted && null != eachTypeMap.get("fileDecisionsImportThreshold"))
							{
								
								int fileDecisionsImportThreshold=Integer.parseInt((String)eachTypeMap.get("fileDecisionsImportThreshold"));
								if(totalCertItems >= fileDecisionsImportThreshold)								
								configMap.put("SHOW_FILE_DECISIONS_IMPORT", Boolean.valueOf((String)eachTypeMap.get("ShowFileDecisionsImport")));								
									
							}
							
							if(null != eachTypeMap &&  null != eachTypeMap.get("ShowUncompletedCertCBTPopup")  &&  !iscertCompleted)		
							{
								
								// api to check cbt
								List<String> adminWorkGroupMembersList=getCertAdminWorkGroupMembers(context);
								String operator=context.getUserName();

								
								if((null != adminWorkGroupMembersList && adminWorkGroupMembersList.contains(operator))){logger.debug("User part of admin workgroup: "+ operator);}
								else if(isUserCLevel(context, operator)) {logger.debug("user is C level (VP or higher): "+ operator);}
							
								else{
									String CertCBTString=(String)eachTypeMap.get("CertCBTString");
									logger.debug("checking if user completed CBTs: "+ CertCBTString);
									 if(!isUserCompletedCBT(context, operator, CertCBTString))
									 {
									   configMap.put("SHOW_UNCOMPLETED_CERT_CBT_POPUP", Boolean.valueOf((String)eachTypeMap.get("ShowUncompletedCertCBTPopup")));
									   configMap.put("CERT_CBT_STRING", CertCBTString);
									 }
								}
							}
							
							if(null != eachTypeMap &&  null != eachTypeMap.get("HidePreviousRevokesFlag"))							
								configMap.put("HIDE_PREV_REVOKES", Boolean.valueOf((String)eachTypeMap.get("HidePreviousRevokesFlag")));
							
							if(null != eachTypeMap &&  null != eachTypeMap.get("ScrubTokens"))							
								configMap.put("SCRUB_TOKENS", Boolean.valueOf((String)eachTypeMap.get("ScrubTokens")));
							
							if(null != eachTypeMap &&  null != eachTypeMap.get("DescriptionsFeedback"))							
								configMap.put("DESC_FEEDBACK", Boolean.valueOf((String)eachTypeMap.get("DescriptionsFeedback")));
							
							
							if(null != eachTypeMap &&  null != eachTypeMap.get("SystemIDCertDecisionOptions") && certObj.getName().toLowerCase().startsWith("system"))							
								configMap.put("SHOW_SYSTEM_ID_CERT_OPTIONS", Boolean.valueOf((String)eachTypeMap.get("SystemIDCertDecisionOptions")));
                           
							if(null != eachTypeMap &&  null != eachTypeMap.get("CustomSignoffButton") && !iscertCompleted)							
								configMap.put("SHOW_CUSTOM_SIGNOFF_BTN", Boolean.valueOf((String)eachTypeMap.get("CustomSignoffButton")));
							
							if(null != eachTypeMap &&  null != eachTypeMap.get("EntOwnerCertDecisionOptions"))	
							{
								configMap.put("SHOW_ENT_OWNER_CERT_OPTIONS", Boolean.valueOf((String)eachTypeMap.get("EntOwnerCertDecisionOptions")));
								
								if(Boolean.valueOf((String)eachTypeMap.get("EntOwnerCertDecisionOptions")))
								{
									configMap.put("ROLES_TEAM_EMAIL", Boolean.valueOf((String)eachTypeMap.get("RolesTeamEmail")));
									configMap.put("ROLE_WIZARD_OPTION", Boolean.valueOf((String)eachTypeMap.get("RoleWizardRedirectOptions")));
									String result=findEntOwnerAccessDescFeedbackResults(certId);
	
									if(null != result)
									{		
										if(result.equalsIgnoreCase("Positive"))
											configMap.put("SHOW_ENT_OWNER_CERT_BANNER_POSITIVE", true);	
										else if(result.equalsIgnoreCase("Negative"))
											configMap.put("SHOW_ENT_OWNER_CERT_BANNER_POSITIVE", false);
										
											 
						   			    String myAssetsURL=(String)sweCertsCustomObj.get("MyAssetsEntsUpdateURL");					   			    
						                logger.debug("myAssetsURL: "+myAssetsURL);
						                configMap.put("ENT_OWNER_MY_ASSETS_URL", myAssetsURL);
									}	
								}
							}
							if(null != eachTypeMap &&  null != eachTypeMap.get("displayPrevalenceScores") && Boolean.valueOf((String)eachTypeMap.get("displayPrevalenceScores")))
							{
								 boolean displayPrevalenceScores=Boolean.valueOf((String)eachTypeMap.get("displayPrevalenceScores"));
								if(displayPrevalenceScores)
								{
									configMap.put("DISPLAY_PREVALENCE_SCORES", true);
									/*List targetUsersList=getTargetUsersEIDsItemsIdsByCertification(certObj);
									configMap.put("CERT_TARGET_USERS_LIST", targetUsersList);
									
									List targetUsersList=getTargetUsersEIDsByCertification(certObj);
									configMap.put("CERT_TARGET_USERS_LIST", targetUsersList);
									if(checkifCerthasPrevalenceScoresCache(certId,context))
									{
										  configMap.put("PREVALENCE_SCORES_CACHE", true);
										  configMap.put("PREVALENCE_DEV_EXCHANGE_TOKEN", "");
									}
									else
									{
										configMap.put("PREVALENCE_SCORES_CACHE", false);
										configMap.put("PREVALENCE_DEV_EXCHANGE_TOKEN", getPrevScoresBearerToken());

									}
									*/
								}
								
								
								
							}
							
							if(null != eachTypeMap &&  null != eachTypeMap.get("BannerMessage"))							
								configMap.put("CUSTOM_BANNER_MSG", eachTypeMap.get("BannerMessage"));
							
							
							/*if(null != eachTypeMap &&  null != eachTypeMap.get("EntOwnerCertDecisionOptions") && certObj.getName().toLowerCase().startsWith("entitlement"))	
							{
								configMap.put("SHOW_ENT_OWNER_CERT_OPTIONS", Boolean.valueOf((String)eachTypeMap.get("SystemIDCertDecisionOptions")));
								configMap.put("ROLES_TEAM_EMAIL", Boolean.valueOf((String)eachTypeMap.get("RolesTeamEmail")));
								
							}
							*/
							
							configMap.put("CERT_ID", certId);
						}
				   }
				
			} catch (GeneralException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				logger.error("Exception in certsPluginCheckConfig",e);
			}
			  
		  }
		  logger.debug("Exit certsPluginCheckConfig configMap: "+ configMap);
		return configMap;
	  }
	
	public boolean isUserCLevel(SailPointContext context, String operator)
	{
		logger.debug("Enter isUserCLevel: "+ operator);
		Identity operatorobj;
		boolean isCLevel=false;
		try {
			operatorobj = context.getObjectByName(Identity.class, operator);
			if(null != operatorobj)
			{
				String identityCLevel = (String)operatorobj.getAttribute("sweManagerLevel");
				
				
				if(identityCLevel != null){
					int managerLevel = 0;
					try{
						managerLevel = Integer.parseInt(identityCLevel);
						if(managerLevel >= 95){
							logger.debug("sweManagerLevel attribute for " + operator + " is >= 95");
							isCLevel = true;
						}
					} catch (NumberFormatException nfe){
						logger.debug("sweManagerLevel is not in a number format.  Failed to parse number for C-Level check");
						isCLevel = false;
					}
				}
			}
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			logger.error("Exception in isUserCLevel",e);
		}
		
		logger.debug("Exit isUserCLevel: "+ isCLevel);
		return isCLevel;
	}
	
	public boolean isUserCompletedCBT(SailPointContext context, String operator, String CertCBTString)
	{
		logger.debug("Enter runCBTRule: "+ operator);
		Map<String,Object> ruleargs = new HashMap<String,Object>();
		boolean isUserCompletedCBT=false;

	    try {
	    		    
	    	Rule rule = context.getObject( Rule.class, "SWE Certifications CBT Check Rule");		
	    	if(null != rule)
			 {
			 
			  ruleargs.put("identityName",operator);	
			  ruleargs.put("CertCBTString",CertCBTString);
				//execute rule
			  isUserCompletedCBT = (boolean)context.runRule( rule, ruleargs );
			  context.decache(rule);  			  
			 }
			 else
			 {
				 logger.error("errors: Rule Object not found");
				 isUserCompletedCBT=true;
			 }
	    	
	    } catch (Exception e) {
			
			//e.printStackTrace();
			logger.error("Error in runCBTRule", e);
			//resultMap.put("status","Failed");
			isUserCompletedCBT=true;
		} 	
	    
	    logger.debug("Exit runCBTRule isUserCompletedCBT: "+isUserCompletedCBT);
	    return isUserCompletedCBT;
	}
	
	
	public List getCertAdminWorkGroupMembers(SailPointContext context)
	{
		List<String> adminWorkGroupMembersList=new ArrayList<String>();
		 Iterator<Object[]> workGroupItr=null;
		 
		 Identity delegateWorkgroup;
		try {
			delegateWorkgroup = context.getObjectByName(Identity.class, "SWE Certification Admin Workgroup");
			if(null != delegateWorkgroup)
			    workGroupItr = ObjectUtil.getWorkgroupMembers(context,delegateWorkgroup , null);
				
				//if(null != delegateWorkgroup)
					//context.decache(delegateWorkgroup);
				
			 if(null != workGroupItr)
			 {
			    while (workGroupItr.hasNext()) {
				 Object[] object = (Object[]) workGroupItr.next();
				 Identity firstValue = (Identity) object[0];
				 adminWorkGroupMembersList.add(firstValue.getName());
			  }        
	            sailpoint.tools.Util.flushIterator(workGroupItr);
			 }
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			logger.error("Exception in getCertAdminWorkGroupMembers",e);
		}
		 
			   
		return adminWorkGroupMembersList;
	}
	
	@POST
	  @Path("isUserCompletedFeedback")
	  public Boolean isUserCompletedFeedback(Map<String, Object> paramMap)
	  {
		
		
		return true;
	  }
	
	
	
	
	
	@POST
	  @Path("postFeedback")
	  public String postFeedback(Map<String, Object> paramMap) throws GeneralException
	  {
		//logger.setLevel(Level.DEBUG);
		logger.debug("Enter postFeedback: ");		
	    String response="Success";
	    
	      //Object Manager Audit
		  try
		  {
			  SailPointContext context=getContext();
			  //get variables from request
			  String feedbacktext=(String)paramMap.get("feedbacktext");
			  String certId=(String)paramMap.get("certId");
			  String actionType=(String)paramMap.get("actionType");
			  Integer feedbackcode=(Integer)paramMap.get("feedbackcode");
			  String qanda=(String)paramMap.get("qanda");
			  

			  String operator=context.getUserName();
			  //takes a way the first / if found in certId
			  
			  if(null == certId)
			  {
				  certId="";
			  }
			  
			  //START of audit code	
			//  Boolean auditActive = this.getSettingBool("MyObjectsPageAudit");
			 // if(auditActive)
			  {			  		  
			  AuditEvent event = new AuditEvent();
			  event.setSource(operator);
			  event.setTarget(certId);
			  event.setAction(actionType);
			  event.setAttributeName("FEEDBACK_CODE");
			  event.setAttributeValue(String.valueOf(feedbackcode));
			  //event.setApplication(application);			 
			  event.setString1(feedbacktext);
			  event.setString2(qanda);
			  context.saveObject(event);
			  context.commitTransaction();
			  Auditor.log(event);
		  	  }
			  //DONE with Audit
			  
			  /*
			  //START of email
			  String emailAddress = this.getSettingString(certId);
			  if(null != emailAddress && !emailAddress.equalsIgnoreCase("none"))
			  {
				logger.debug("Email info found: " + emailAddress);				
				logger.debug("SWE Feedback plugin / Start");		
				//Configuration variables
				SailPointContext context = null;
				context = SailPointFactory.getCurrentContext();
				String emailTemplateStr = "SWE Feedback Plugin Notification";
				String targetEmail = emailAddress;
				String feedbackText = feedbacktext;	
				String ratingFound = feedbackcode.toString();
				String actionFound = actionType;
				String sweTarget = certUrl;
				String userName = operator;

				//End of configuration variables				
				logger.debug("SWE Feedback plugin / Done Setting up config and test vars");
				//Set up email and send
				EmailTemplate template = context.getObjectByName(EmailTemplate.class,emailTemplateStr);
				EmailOptions options = new EmailOptions(); 
				options.setTo(targetEmail); 
				Map args = new HashMap();
				args.put("user",userName);
				args.put("target",sweTarget);
				args.put("actionFound",actionFound);
				args.put("rating",ratingFound);
				args.put("feedbackText",feedbackText);
				options.setVariables(args); 
				
				//send email
				logger.debug("SWE Feedback plugin / sending email...");
				context.sendEmailNotification(template, options);
			  }			  
			  //END of email
			  */
			  
		  }
		  catch (GeneralException e)
		  {
			// e.printStackTrace();
			  logger.error("Exception in postFeedback",e);
			  response="Failed: "+e;
		  }
		  
		  
		  logger.debug("Exit postFeedback: "+response);
	    
		  
		  
	    return response;//new ListResult(localList, localList.size());
	  }
	
	 
	 @POST
	  @Path("certsImportDecisions")
	  public Map certsImportDecisions(Map<String, Object> paramMap)
	    throws GeneralException
	  {
		 logger.debug("Entered certsImportDecisions file: "+ paramMap);
			
		 String certId=(String)paramMap.get("certId");
		
		  
		  String csvFileStr=(String)paramMap.get("csvFileJson");
		  Map infoMsgMap = new HashMap();
		  Map ruleargs = new HashMap();
		  Map resultMap = new HashMap();
		  		
		 try
		  {
			  SailPointContext context=getContext();
			  logger.debug("context.getUserName()"+context.getUserName());			  
			  Rule rule = context.getObject( Rule.class, "SWE Certification Import File Decisions" );		
			 if(null != rule)
			 {
			 
			  infoMsgMap.put("certifier", context.getUserName());
			  infoMsgMap.put("certId", certId);
			  infoMsgMap.put("csvFileStr",csvFileStr);
			  ruleargs.put("ruleInputs",infoMsgMap);			 
				//execute rule
			  resultMap = (Map)context.runRule( rule, ruleargs );
			  context.decache(rule);  			  
			 }
			 else
				 resultMap.put("errors", "Rule Object not found");
			 
			 
			 
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in certsImportDecisions",e);
			  resultMap.put("errors", "Failed");
		  }
			return resultMap;
	  }
	 
	 @POST
	  @Path("checkCertRevokesorSignoff")
	  public List checkCertRevokesorSignoff(Map<String, Object> paramMap)
	    throws GeneralException
	  {
		 logger.debug("Entered checkCertRevokesorSignoff file: "+ paramMap);
			
		 String certId=(String)paramMap.get("certId");
		
		  
		  String csvFileStr=(String)paramMap.get("csvFileJson");
		  boolean isSystemIDCert=(boolean)paramMap.get("isSystemIDCert");
		  
		  List resultList = new ArrayList();
		  		
		 try
		  {
			  SailPointContext context=getContext();
			  Certification certObj = context.getObjectById(sailpoint.object.Certification.class,certId);
			  if(null != certObj)
			  {
				  CertificationStatistics certstats=certObj.getStatistics();
				  if(null != certstats)
				   {
					  if(isSystemIDCert && (certstats.getAccountsRemediated() > 0 || certstats.getExceptionsRemediated() > 0 || certstats.getRolesRemediated() > 0))
					  {
						  logger.debug("Revokes Exists and pulling out for certId: "+certId);
						  						
						  resultList=getRevocationsList(certObj,context);
					  }
					  else if(certObj.getItemPercentComplete()==100)
					  {						  	 
						  singOffCert(certObj, context, certId);						   
					  }
				   }
			  }
			 
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in checkCertRevokesorSignoff",e);
			  
		  }
		 
		 logger.debug("Exit checkCertRevokesorSignoff file: "+ resultList);
			return resultList;
	  }
	 
	 
	 @POST
	  @Path("entOwnerCertRevokesorSignoff")
	  public Map entOwnerCertRevokesorSignoff(Map<String, Object> paramMap)
	    throws GeneralException
	  {
		 logger.debug("Entered entOwnerCertRevokesorSignoff file: "+ paramMap);
			
		 String certId=(String)paramMap.get("certId");
		 
		  
		  String csvFileStr=(String)paramMap.get("csvFileJson");
		  Map resultMap = new HashMap();
		  		
		 try
		  {
			  SailPointContext context=getContext();
			  Certification certObj = context.getObjectById(sailpoint.object.Certification.class,certId);
			  if(null != certObj)
			  {
				  CertificationStatistics certstats=certObj.getStatistics();
				  if(null != certstats)
				   {
					  if(certstats.getAccountsRemediated() > 0 || certstats.getExceptionsRemediated() > 0 || certstats.getRolesRemediated() > 0)
					  {
						  logger.debug("Revokes Exists and pulling out for certId: "+certId);
						  
						  List<Map> revocationList=getRevocationsList(certObj,context);
						  logger.debug("revocationList: "+revocationList);
						  List<Map> finalResultList=new ArrayList();
						  
						  for(Map eachRevocationMap: revocationList)
						  {
							  String roleName=getAccessGrantedByRole(context, (String)eachRevocationMap.get("identName"),(String)eachRevocationMap.get("value"), (String)eachRevocationMap.get("attribute"),(String) eachRevocationMap.get("accountName"), (String)eachRevocationMap.get("appName"));
							  if(null != roleName && !roleName.isEmpty())
							  {
								  
								  logger.debug("roleName: "+roleName);
									if(null != roleName && !roleName.equals(""))
							    	   {
							    	    Bundle roleObj=context.getObjectByName(Bundle.class,roleName);	
							    	   
							    	       String roleDispName=""; 
								    	   if(null != roleObj)	
								    	   {
								    		   roleDispName=roleObj.getDisplayName();
								    	   
								    		   if(null != roleDispName && !roleDispName.isEmpty() )		    	   		    		   
									    		   roleName=roleDispName;
									    	   else
									    		   roleName=roleObj.getName();
							
		    	   		    		   
									    	  eachRevocationMap.put("grantedByRoleName", roleName);
									    	  eachRevocationMap.put("grantedByRoleId",roleObj.getId());
									    	  logger.debug("roleObj.getType(): "+roleObj.getType());
									    	  if(roleObj.getType().equalsIgnoreCase("job") || roleObj.getType().equalsIgnoreCase("applicationrole"))
									    		  eachRevocationMap.put("roleWizardOption", true);
								    	   }
							    	   
							    	   }
									
								  finalResultList.add(eachRevocationMap);
							  }
						  }
						  logger.debug("finalResultList: "+finalResultList);
						  if(null != finalResultList && !finalResultList.isEmpty())
						  {
						   resultMap.put("userRevokeAccessDetails", finalResultList);
						  }
						  else if(certObj.getItemPercentComplete()==100)
						  {						  	 
							  singOffCert(certObj, context, certId);						   
						  }
					  }
					  else if(certObj.getItemPercentComplete()==100)
					  {						  	 
						  singOffCert(certObj, context, certId);						   
					  }
				   }
			  }
			 
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in entOwnerCertRevokesorSignoff",e);
			  resultMap.put("errors", "Failed");
		  }
		 
		 logger.debug("Exit entOwnerCertRevokesorSignoff file: "+ resultMap);
			return resultMap;
	  }
	 
	 
	 public String getUserPlainDisplayName(String userName,  String firstName, String lastName)
	 {
		 String userDisplayName="";

		
		 if(null !=firstName && null !=lastName)
		 {
			 if(firstName.length() > 50)
				 firstName=firstName.substring(0,50)+".." ;
			 if(lastName.length() > 50)
				 lastName=lastName.substring(0,50)+".." ;
			 userDisplayName=firstName +" "+ lastName +" ("+ userName+")";
		 
		 }
		 else if(null !=firstName)
		 {
			 if(firstName.length() > 50)
				 firstName=firstName.substring(0,50)+".." ;
			 userDisplayName=firstName +" ("+ userName+")";
		 }
		 else if(null !=lastName)
		 {
			 if(lastName.length() > 50)
				 lastName=lastName.substring(0,50)+".." ;
			 userDisplayName=lastName +" ("+ userName+")";		  
		 }
		 		 
		 
		 if(null == userDisplayName || userDisplayName.equals(""))
			 userDisplayName=userName;
		 
		 if(userDisplayName.length() > 200)
			 userDisplayName=userDisplayName.substring(0,100)+".." ;
		 
		 return userDisplayName;
	 }
	
	  public List getRevocationsList(Certification certObj,SailPointContext context)
	  {
	
		logger.debug("Entered getRevocationsList file: "+ certObj);
		
		
		 
		  Map resultMap = new HashMap();
		  List userAccessRevocationsList=new ArrayList();
			  		
		 try
		  {

			  List certEntities = null;
		      List certitems=null;
		      CertificationItem perItemInCert=null;
		  	  CertificationEntity perEntityInCert=null;
		  	  Map eachLineItemMap= new HashMap();
	
			
		      
				if(null != certObj)
				{
					certEntities = certObj.getEntities();					
					Iterator certEntityIterator = certEntities.iterator();
					 while(certEntityIterator.hasNext())
						{
						 perEntityInCert = (CertificationEntity) certEntityIterator.next();
						 certitems = perEntityInCert.getItems();
						 Iterator certItemIterator= certitems.iterator();
						 while(certItemIterator.hasNext())
						  {
							 eachLineItemMap= new HashMap();
							 perItemInCert = (CertificationItem) certItemIterator.next();
							 if(null != perItemInCert)
							 {

								 try
								 {
									 CertificationAction action=perItemInCert.getAction();
						         if(null != action && null != action.getStatus() && action.getStatus().equals(CertificationAction.Status.RevokeAccount) || action.getStatus().equals(CertificationAction.Status.Remediated))
						         {

						         
									 
								  String identName=perEntityInCert.getIdentity();
								  eachLineItemMap.put("itemId",perItemInCert.getId());
								  eachLineItemMap.put("userFullName",perEntityInCert.getTargetDisplayName());
								  eachLineItemMap.put("identName",identName);
									  
								  Identity identObj=null;
								  if(null != identName)
									  identObj=context.getObjectByName(Identity.class, identName);
								  
								  String description="";
								  String appName="";
								  String directAppName="";
								  String attribute="";
								  String entorRole="";
								  String displayName="";
								  String accountName="";
								  String decision="";
								  //String completionComments="";
								  //String accessGrantedDateStr="";
								  String ownerNameStr="";
								  String lastLoginInfo="";
								  Date accessGrantedOn=null;
								  boolean highRisk=false;
								  String moverFlagStr="";
								  String relatedApplication="";
								  String accountStatus="";
								  String accountCreationDateStr="";
								  if(null != perItemInCert.getType() && (perItemInCert.getType().equals(CertificationItem.Type.Exception) || perItemInCert.getType().equals(CertificationItem.Type.Account)))
									{ 

									  
									  if(perItemInCert.getType().equals(CertificationItem.Type.Account))
									  {
									    description = "Account-Only";
									    entorRole="Account-Only";
									    eachLineItemMap.put("type","Account");
									  }
									  else
									  eachLineItemMap.put("type","Entitlement");
									  
									  
										 EntitlementSnapshot snap = perItemInCert.getExceptionEntitlements();
										 if(null != snap)
										 {
										  //accountName=snap.getNativeIdentity();
										  accountName=snap.getDisplayName();
										  appName=snap.getApplicationName();	
										  directAppName=appName;
										  
										  if(!perItemInCert.getType().equals(CertificationItem.Type.Account))
										  {  
										    attribute = (String)snap.getAttributeName();
										    entorRole = (String)snap.getAttributeValue();
										  }
										  
										  if(null != appName && null != attribute && null != entorRole)
										  {
											 // accessGrantedOn=getAccessGratedOn(appName, attribute, context, entorRole, accountName, identName);
											  
											  Filter filter = null;
											  
											    List<Filter> filterList = new ArrayList();											
										        filterList.add(Filter.eq("application.name", appName));									
										        if(null != attribute)
										          filterList.add(Filter.eq("attribute", attribute));									
										        if(null != entorRole)
										          filterList.add(Filter.eq("value", entorRole));									
										        filter = Filter.and(filterList);	
										        ManagedAttribute managedAttribute=null;
										        
										        try {
										         managedAttribute = context.getUniqueObject(ManagedAttribute.class, filter);	
										        }
										        catch(Exception e) {logger.error("Exception in getRevocationsList for managedAttribute perItemInCert:  "+perItemInCert, e);}
										        
										        if(null != managedAttribute)
										        {
										        	
										        	displayName=managedAttribute.getDisplayName();
										        	description=managedAttribute.getDescription("en_US");
										        	if(description == null)
									        			   description=""; 
										        	relatedApplication = (String)managedAttribute.getAttribute(SWE_ATTRIBUTE_RELATED_APPLICATION);
										        	 if(null != relatedApplication && !relatedApplication.isEmpty() && directAppName.equalsIgnoreCase("Active Directory"))
										        	 {
										        		 appName=relatedApplication;										        
										        	 }
										        	 
										        	 
										        	 Identity OwnerIdent=managedAttribute.getOwner();
										        	 ownerNameStr=getOwnerDisplayName(OwnerIdent);
										        	 
										        	 
										        	 highRisk=checkHighRiskAccess(managedAttribute,context);
										        	 //lastLoginInfo=getLastLoginInformation(appName,relatedApplication,entorRole, context,managedAttribute,identName,accountName,accessGrantedOn);
											            
										        }
										       // else
										        //	isSox=isSoxApplication(directAppName,context);
										        
										        logger.debug("managedAttribute: " + managedAttribute);
										       }
										        
										       /* if(null != identObj)
								            	{
										        	IdentityService IS=new IdentityService(context);
										        	try
										        	{
								            		Link accountLink=IS.getLink(identObj, context.getObjectByName(Application.class, appName), null,accountName);
								            		    
									            		logger.debug("accountLink: " + accountLink);
									            		if (null != accountLink)
									            		{
									            			
									            			Date accountCreatedOn=accountLink.getCreated();									            			
									            			 //if(description == null || description.equals(""))
									            		
									            			if(!directAppName.toLowerCase().startsWith("ncma"))									            			
									            				accountCreationDateStr=uidisplaydateFormat.format(accountCreatedOn);

									            		     accountStatus="Active";
									            		     if(accountLink.isDisabled())									            		     
									            		    	 accountStatus="Disabled";									            		    										            		     
									            		     else if(accountLink.isLocked())									            		     
									            		    	 accountStatus="Locked";   									            		    	 
									            		     
									            		     
									            		     
										            		if(null == lastLoginInfo || lastLoginInfo.isEmpty())
										            		  lastLoginInfo=allAccessApplicationLastLogin(appName, relatedApplication,accountLink,context, accessGrantedOn);
									            		}
										        	}
										        	catch (Exception e) {}
								            	}
								            	*/
										  }
										  
										  
										  
										  
									}
							
									 else if(null != perItemInCert.getType() && perItemInCert.getType().equals(CertificationItem.Type.Bundle))
									 {
										 eachLineItemMap.put("type","Role");
										 
										 entorRole=perItemInCert.getBundle();
										 displayName=perItemInCert.getTargetDisplayName();
										 
										 
										 
										/* accessGrantedOn=getAccessGratedOn(null, null, context, entorRole, null, identName);
								               
										 if(null != accessGrantedOn)
											 accessGrantedDateStr = uidisplaydateFormat.format(accessGrantedOn);  
										 */
								               Bundle roleObj = context.getObjectByName(Bundle.class, entorRole);
								        	   if (null != roleObj)
									           	 {
								        		   displayName=roleObj.getDisplayName();
								        		   description=roleObj.getDescription("en_US");
								        		   if(description == null)
								        			   description=""; 
								        		   
								        		   //Identity OwnerIdent=roleObj.getOwner();
										          // ownerNameStr=getOwnerDisplayName(OwnerIdent);
								        		   
									           	 }
								           	  
								        	
								        	   
								        	   /*
								        	     if(description.equals(""))
								        	    	 description="Role Type: "+roleObj.getType();
										  	          else
										  	        	description=description+", Role Type: "+roleObj.getType();
										  	          
										  	            if(null != roleObj && roleObj.isDisabled())
										  	            {
										  	              if(description.equals(""))
										  	            	description="Role Status: Inactive";
										  	              else
										  	            	description=description+", Role Status: Inactive";	  
										  	            }
										  	            else
										  	            {
										  	            	if(description.equals(""))
										  	            		description="Role Status: Active";
										  	            	else
										  	            		description=description+", Role Status: Active";
										  	            }
										       */
									 }
								  
								  
								     
							         
							         if(description == null || description.isEmpty())        
								      	   description = "Not Available";
							         
							         description=description.replaceAll("[\\t\\n\\r]+"," ");       
							         description=description.replaceAll("\\<.*?\\>", "");
							         
							         if(displayName == null || displayName.isEmpty())        
							        	 displayName = entorRole;
							         
							         
									  logger.debug("appName: " + appName);
									  logger.debug("attribute: " + attribute);
									  logger.debug("entorRole: " + entorRole);
									  logger.debug("accountName: " + accountName);
									  logger.debug("description: " + accountName);
									  logger.debug("accessGrantedOn: "+ accessGrantedOn);
							       /*  
									 try
					            	   {						               	               	               
							               if(null != accessGrantedOn)							               								            	  
							            	   accessGrantedDateStr = uidisplaydateFormat.format(accessGrantedOn);							               
					            	   }
					            	   catch(Exception e)
										 {
											 logger.error("Exception in getRevocationsList for accessGrantedOn:  "+accessGrantedOn, e);
										 }
									 
									 if(null != accessGrantedOn)											 										
									     moverFlagStr=getMoverViolation(accessGrantedOn, moverFlagStr, context, identObj, identName);
									*/
									 
									
								
									 eachLineItemMap.put("accountName", accountName);
									 eachLineItemMap.put("appName", appName);
									 eachLineItemMap.put("displayName", displayName);
									 eachLineItemMap.put("attribute", attribute);
									 eachLineItemMap.put("description",description);
									 eachLineItemMap.put("value", entorRole); 
									 eachLineItemMap.put("isHighRisk", highRisk); 
	
					            			
	
							    /*   eachRowsb.append(",\""+appName+"\"");
							         eachRowsb.append(",\""+displayName+"\"");
							         eachRowsb.append(",\""+lastLoginInfo+"\"");
							         eachRowsb.append(","+highRisk);
							         eachRowsb.append(","+moverFlagStr);
							         eachRowsb.append(","+accessGrantedDateStr);							         
							         eachRowsb.append(","+accountCreationDateStr);
							         eachRowsb.append(","+accountStatus);							         
							         eachRowsb.append(",\""+ownerNameStr+"\"");
							         eachRowsb.append(",\""+description+"\"");
							         eachRowsb.append(",\""+accountName+"\"");
							         eachRowsb.append(","+perItemInCert.getSummaryStatus());
							        
							         CertificationAction action=perItemInCert.getAction();
							         if(null != action && null != action.getStatus())
							         {
							        	 if(action.getStatus().equals(CertificationAction.Status.ApproveAccount) || action.getStatus().equals(CertificationAction.Status.Approved))
							        		 decision="Approved";
							        	 else
							        		 if(action.getStatus().equals(CertificationAction.Status.RevokeAccount) || action.getStatus().equals(CertificationAction.Status.Remediated)) 
							        			 decision="Revoked";
							        	 
							        	 if(null != action.getCompletionComments())
							        		 completionComments=action.getCompletionComments();
							         }
							         */
							         eachLineItemMap.put("decision", "Revoked");
							        // eachRowsb.append(",\""+completionComments+"\"");
							         
	
								  
									  if(null != eachLineItemMap)
									  {
										 //eachRowsb.append("\n");
									     userAccessRevocationsList.add(eachLineItemMap);
									  }
						          }
								 }
								 catch(Exception e)
								 {
									 logger.error("Exception in getRevocationsList for Cert Item:  "+perItemInCert, e);
								 }
							}
						  }
						 if(null != certItemIterator)
								sailpoint.tools.Util.flushIterator(certItemIterator);
						}
					 if(null != certEntityIterator)
							sailpoint.tools.Util.flushIterator(certEntityIterator);
					 
					  
				}
				
				 
				
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in getRevocationsList",e);
		  }
		 logger.debug("Exit getRevocationsList "+ userAccessRevocationsList);
			return userAccessRevocationsList;
	  }
	 
	 
	 @POST
	  @Path("customSignoffCertification")
	  public Map customSignoffCertification(Map<String, Object> paramMap)
	    throws GeneralException
	  {
		 logger.debug("Entered customSignoffCertification file: "+ paramMap);
			
		 String certId=(String)paramMap.get("certId");
		  
		  Map resultMap = new HashMap();
		  		
		 try
		  {
			  SailPointContext context=getContext();
			  Certification certObj = context.getObjectById(sailpoint.object.Certification.class,certId);
			  if(null != certObj)
			  {
				 if(certObj.getItemPercentComplete()==100)
					  {						  	 
						  singOffCert(certObj, context, certId);						   
					  }
				   
			  }
			 
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in customSignoffCertification",e);
			  resultMap.put("errors", "Failed");
		  }
			return resultMap;
	  }
	 
	 @POST
	  @Path("sysIdCompleteCertSaveDecisions")
	  public Map sysIdCompleteCertSaveDecisions(Map<String, Object> paramMap)
	    throws GeneralException
	  {
		 logger.debug("Entered sysIdCompleteCertSaveDecisions file: "+ paramMap);
			
		 String certId=(String)paramMap.get("certId");
		 List<String> approvedDecisions=(List<String>)paramMap.get("approvedDecisions");
		 List<String> revokedDecisions=(List<String>)paramMap.get("revokedDecisions");
		  Map resultMap = new HashMap();

		 try
		  {
			  SailPointContext context=getContext();
			  Certification certObj = context.getObjectById(sailpoint.object.Certification.class,certId);
			  CertificationItem certitem=null;
			  String workItemId = null;
			  String comments="";
			  RemediationAction remediationAction=null;
			  String recipient=null;
			  Identity certifierobj = context.getObjectByName(Identity.class, context.getUserName());	
			  if(null != certObj)
			  {
				  
				  
					  if(null != revokedDecisions)
					  for(String eachItemId: revokedDecisions)
					  {
						  certitem =context.getObjectById(CertificationItem.class, eachItemId);
						  if(null != certitem.getCertification())
								  {
							          if(certitem.isDelegated())												
									  certitem.revokeDelegation();		
								          try
								          {
								        	 certitem.remediate(context,certifierobj, workItemId, remediationAction, recipient, "",comments,null, null);
								        	// Save the item.
												context.saveObject(certitem);
												context.commitTransaction();
								        	 
								          }
								          catch (Exception e)
										  {
								        	  resultMap.put("Status", "Failed to Revoke for itemId: "+eachItemId);
								        	  logger.error("Failed to revoke for itemId: "+eachItemId,e);
										  }
									  
							          
								  }
					  }
					  
					  if(null != approvedDecisions)
						  for(String eachItemId: approvedDecisions)
						  {
							  certitem =context.getObjectById(CertificationItem.class, eachItemId);
							  if(null != certitem.getCertification())
									  {
								          if(certitem.isDelegated())												
										  certitem.revokeDelegation();		
									          try
									          {
											  certitem.approve(context, certifierobj, workItemId, comments);
											// Save the item.
												context.saveObject(certitem);
												context.commitTransaction();
									          }
									          catch (Exception e)
											  {
									        	  resultMap.put("Status", "Failed to approve for itemId: "+eachItemId);
									        	  logger.error("Failed to approve for itemId: "+eachItemId,e);
											  }
										  
								          
									  }
						  }
				  
				 if(certObj.getItemPercentComplete()==100)
					  {						  	 
						  singOffCert(certObj, context, certId);						   
					  }
				   
			  }
			 
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in sysIdCompleteCertSaveDecisions",e);
			  resultMap.put("errors", "Failed");
		  }
			return resultMap;
	  }
	 
	 
	 
	 @POST
	  @Path("revertCertSaveDecisions")
	  public Map revertCertSaveDecisions(Map<String, Object> paramMap)
	    throws GeneralException
	  {
		 logger.debug("Entered customCertSaveDecisions file: "+ paramMap);
			
		 String certId=(String)paramMap.get("certId");
		  
		  String csvFileStr=(String)paramMap.get("csvFileJson");
		  List<String> revokedDecisions=(ArrayList<String>)paramMap.get("revokedDecisions");

		  Map resultMap = new HashMap();

		 try
		  {
			  SailPointContext context=getContext();
			  logger.debug("context.getUserName()"+context.getUserName());			  
			  CertificationItem certitem=null;
			  String workItemId = null;
			  Identity certifierobj = context.getObjectByName(Identity.class, context.getUserName());	
			  if(null != certifierobj)
			  {
				  
				  if(null != revokedDecisions)
				  for(String eachItemId: revokedDecisions)
				  {
					  certitem =context.getObjectById(CertificationItem.class, eachItemId);
					  if(null != certitem.getCertification())
							  {
						            
						  
						          if(certitem.isDelegated())												
								  certitem.revokeDelegation();		
							          try
							          {
							        	  
							        	 certitem.setSummaryStatus(sailpoint.object.AbstractCertificationItem.Status.Open);
							        	 certitem.setAction(null);//.rollbackChanges(workItemId); //.clearDecision(context, certifierobj, workItemId);
							        	// Save the item.
											context.saveObject(certitem);
											context.commitTransaction();
											
											// Refresh the certification to get statistics, etc... updated.
											Certificationer certificationer = new Certificationer(context);
											certificationer.refresh(certitem.getCertification());
											
											// custom audit
											try
									          {											 
												  String operator=context.getUserName();
												  AuditEvent event = new AuditEvent();
												  event.setSource(operator);
												  event.setTarget(certId);
												  event.setAction("SystemID Cert Cleared Decisions");												  	 											 
												  event.setString1("Certifier Cleared Decisions to review again for ItemId: "+ eachItemId);
												  event.setString2(eachItemId);
												  context.saveObject(event);
												  context.commitTransaction();
												  Auditor.log(event);
									          }
											  catch (Exception e)
											  {									        	  
									        	  logger.error("Failed to audit for itemId: "+eachItemId,e);
											  }  
											
											
							          }
							          catch (Exception e)
									  {
							        	  resultMap.put("Status", "Failed to clear decision for itemId: "+eachItemId);
							        	  logger.error("Failed to clear decision for itemId: "+eachItemId,e);
									  }
								  
						          
							  }
				  }	
				  
				 
			  }

			  resultMap.put("Status", "Succcess");
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in revertCertSaveDecisions",e);
			  resultMap.put("Status", "Failed");
		  }
		 logger.debug("Exit revertCertSaveDecisions file: "+ resultMap);
			return resultMap;
	  }
	 
	 
	 @POST
	  @Path("entOwnerApproveCertDecisions")
	  public Map entOwnerApproveCertDecisions(Map<String, Object> paramMap)
	    throws GeneralException
	  {
		 logger.debug("Entered entOwnerApproveCertDecisions file: "+ paramMap);
			
		 String certId=(String)paramMap.get("certId");
		  
		  String csvFileStr=(String)paramMap.get("csvFileJson");
		  List<String> revokedDecisions=(ArrayList<String>)paramMap.get("revokedDecisions");

		  Map resultMap = new HashMap();

		 try
		  {
			  SailPointContext context=getContext();
			  logger.debug("context.getUserName(): "+context.getUserName());			  
			  CertificationItem certitem=null;
			  String workItemId = null;
			  Identity certifierobj = context.getObjectByName(Identity.class, context.getUserName());	
			  if(null != certifierobj)
			  {
				  
				  if(null != revokedDecisions)
				  for(String eachItemId: revokedDecisions)
				  {
					  certitem =context.getObjectById(CertificationItem.class, eachItemId);
					  if(null != certitem.getCertification())
							  {
						            
						  
						          if(certitem.isDelegated())												
								  certitem.revokeDelegation();		
							          try
							          {
							        	  
							        	 certitem.approve(context, certifierobj, workItemId, "Reverted Decision to Approve");
							        	// Save the item.
											context.saveObject(certitem);
											context.commitTransaction();
											
											// Refresh the certification to get statistics, etc... updated.
											Certificationer certificationer = new Certificationer(context);
											certificationer.refresh(certitem.getCertification());
											
											// custom audit
											try
									          {											 
												  String operator=context.getUserName();
												  AuditEvent event = new AuditEvent();
												  event.setSource(operator);
												  event.setTarget(certId);
												  event.setAction("Entitlement Owner Cert Revert Decisions");
												  event.setAttributeName("FEEDBACK_CODE");		 											  
												  event.setString1("Certifier Approved Decisions from Revoke for Granted By Role ItemId: "+ eachItemId);
												  event.setString2(eachItemId);
												  context.saveObject(event);
												  context.commitTransaction();
												  Auditor.log(event);
									          }
											  catch (Exception e)
											  {									        	  
									        	  logger.error("Failed to audit for itemId: "+eachItemId,e);
											  }  
											
											customSignoffCertification(paramMap);
							          }
							          catch (Exception e)
									  {
							        	  resultMap.put("Status", "Failed to clear decision for itemId: "+eachItemId);
							        	  logger.error("Failed to clear decision for itemId: "+eachItemId,e);
									  }
								  
						          
							  }
				  }	
				  
				 
			  }

			  resultMap.put("Status", "Succcess");
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in entOwnerApproveCertDecisions",e);
			  resultMap.put("Status", "Failed");
		  }
		 
		 logger.debug("Exit entOwnerApproveCertDecisions file: "+ resultMap);
			return resultMap;
	  }
	 
	 public boolean singOffCert(Certification certObj, SailPointContext context, String certId)
	 {
		 boolean result=false;
		 logger.debug("SigningOff cert with certId: "+certId);
		 try
		 {
		  Identity certifierobj = context.getObjectByName(Identity.class, context.getUserName());	
		  Certificationer certificationer = new Certificationer(context);
	      certificationer.refresh(certObj);			 
	      certificationer.sign(certObj, certifierobj);
	      result=true;
	      logger.debug("Successfully Signed");
		 }
		 catch(Exception e)
		 {
			 logger.error("Error in singOffCert",e);
			 result=false;
		 }		 
	     
	     return result;
	 }
	 
	 @POST
	  @Path("customCertSaveDecisions")
	  public Map customCertSaveDecisions(Map<String, Object> paramMap)
	    throws GeneralException
	  {
		 logger.debug("Entered customCertSaveDecisions file: "+ paramMap);
			
		 String certId=(String)paramMap.get("certId");
		  
		  String csvFileStr=(String)paramMap.get("csvFileJson");
		  List<String> approvedDecisions=(ArrayList<String>)paramMap.get("approvedDecisions");
		  List<String> revokedDecisions=(ArrayList<String>)paramMap.get("revokedDecisions");
		  List<String> revokeAccountDecisions=(ArrayList<String>)paramMap.get("revokeAccountDecisions");
		  List<String> imnotSureDecisions=(ArrayList<String>)paramMap.get("imnotSureDecisions");
		  

		  Map resultMap = new HashMap();
		  		
		  
		   
		 try
		  {
			  SailPointContext context=getContext();
			  logger.debug("context.getUserName()"+context.getUserName());			  
			  CertificationItem certitem=null;
			  String workItemId = null;
			  String comments="";
			  RemediationAction remediationAction=null;
			  String recipient=null;
			  Identity certifierobj = context.getObjectByName(Identity.class, context.getUserName());	
			  Certification certObj = context.getObjectById(sailpoint.object.Certification.class,certId);
			  if(null != certifierobj)
			  {
				  if(null != approvedDecisions)
				  for(String eachItemId: approvedDecisions)
				  {
					  certitem =context.getObjectById(CertificationItem.class, eachItemId);
					  if(null != certitem.getCertification())
							  {
						          if(certitem.isDelegated())												
								  certitem.revokeDelegation();		
							          try
							          {
									  certitem.approve(context, certifierobj, workItemId, comments);
									// Save the item.
										context.saveObject(certitem);
										context.commitTransaction();
							          }
							          catch (Exception e)
									  {
							        	  resultMap.put("Status", "Failed to approve for itemId: "+eachItemId);
							        	  logger.error("Failed to approve for itemId: "+eachItemId,e);
									  }
								  
						          
							  }
				  }
				  if(null != imnotSureDecisions)
				  for(String eachItemId: imnotSureDecisions)
				  {
					  certitem =context.getObjectById(CertificationItem.class, eachItemId);
					  if(null != certitem.getCertification())
							  {
						          if(certitem.isDelegated())												
								  certitem.revokeDelegation();		
							          try
							          {
									  certitem.approve(context, certifierobj, workItemId, "Im not sure but still Approving");
									// Save the item.
										context.saveObject(certitem);
										context.commitTransaction();
							          }
							          catch (Exception e)
									  {
							        	  resultMap.put("Status", "Failed to approve for itemId: "+eachItemId);
							        	  logger.error("Failed to approve im not sure for itemId: "+eachItemId,e);
									  }
								  
						          
							  }
				  }
				  if(null != revokedDecisions)
				  for(String eachItemId: revokedDecisions)
				  {
					  certitem =context.getObjectById(CertificationItem.class, eachItemId);
					  if(null != certitem.getCertification())
							  {
						          if(certitem.isDelegated())												
								  certitem.revokeDelegation();		
							          try
							          {
							        	 certitem.remediate(context,certifierobj, workItemId, remediationAction, recipient, "",comments,null, null);
							        	// Save the item.
											context.saveObject(certitem);
											context.commitTransaction();
							        	 
							          }
							          catch (Exception e)
									  {
							        	  resultMap.put("Status", "Failed to approve for itemId: "+eachItemId);
							        	  logger.error("Failed to revoke for itemId: "+eachItemId,e);
									  }
								  
						          
							  }
				  }
				  if(null != revokeAccountDecisions)
				  for(String eachItemId: revokeAccountDecisions)
				  {
					  certitem =context.getObjectById(CertificationItem.class, eachItemId);
					  if(null != certitem.getCertification())
							  {
						          if(certitem.isDelegated())												
								  certitem.revokeDelegation();		
							          try
							          {
							        	 certitem.revokeAccount(context,certifierobj, workItemId, remediationAction, recipient, "",comments);
							        	// Save the item.
											context.saveObject(certitem);
											context.commitTransaction();
							          }
							          catch (Exception e)
									  {
							        	  resultMap.put("Status", "Failed to approve for itemId: "+eachItemId);
							        	  logger.error("Failed to revoke account for itemId: "+eachItemId,e);
									  }
								  
						          
							  }
				  }
				  
				    
					
					
					try
					{
								// Refresh the certification to get statistics, etc... updated.
								Certificationer certificationer = new Certificationer(context);
								certificationer.refresh(certObj);
			   
			                 //check cert if its 100% completed
			                  certificationer = new Certificationer(context);
			                  certObj = context.getObjectById(sailpoint.object.Certification.class,certId);						  
							  /*CertificationStatistics certstats=certObj.getStatistics();
							  if(null != certstats)
							   {
								  resultMap.put("TotalItems",certstats.getTotalItems());
								  resultMap.put("OpenItems",certstats.getOpenItems());
								  resultMap.put("CompletedItems",certstats.getCompletedItems());
							   }
							   */
							  logger.debug("certobj.getItemPercentComplete(): " + certObj.getItemPercentComplete());
		                      if(certObj.getItemPercentComplete()==100)
		                       {					  
							      //certificationer.refresh(certobj);			 
							      //certificationer.sign(certobj, certifierobj);
		                    	  resultMap.put("isCertCompleted", true);						  
							   }
		                      else
		                    	  resultMap.put("isCertCompleted", false);
					 }
			  catch (Exception e)
									  {
										  logger.error("Exception while signing off the cert: "+certId + " Exception: " + e);		
										  								  
									  }	
					
			  }

			  resultMap.put("Status", "Succcess");
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in customCertSaveDecisions",e);
			  resultMap.put("Status", "Failed");
		  }
			return resultMap;
	  }
	 
	 
	 
	 @POST
	  @Path("certDownloadtoFile")
	  public Map certDownloadtoFile(Map<String, Object> paramMap)
	  {
	
		logger.debug("Entered certDownloadtoFile file: "+ paramMap);
		
		String certId=(String)paramMap.get("certId");
		  

		  Map resultMap = new HashMap();
		  List rowsList=new ArrayList();
		  rowsList.add("ItemId,User,First Name,Last Name,Access Type,Application,Access Name,Last Login,High Risk,Granted in Prior Job,Granted,Account Creation Date,Account Status,Access Owner,Access Description,Account Name,Status,Decision,Decision Comment");
		  
		  		
		 try
		  {
			  SailPointContext context=getContext();
			  
			  Certification certObj= context.getObjectById(Certification.class, certId);
			  List certEntities = null;
		      List certitems=null;
		      CertificationItem perItemInCert=null;
		  	  CertificationEntity perEntityInCert=null;
		  	  StringBuffer eachRowsb=new StringBuffer();
			  
	
			
		      
				if(null != certObj)
				{
					certEntities = certObj.getEntities();
					Iterator certEntityIterator = certEntities.iterator();
					 while(certEntityIterator.hasNext())
						{
						 perEntityInCert = (CertificationEntity) certEntityIterator.next();
						 certitems = perEntityInCert.getItems();
						 Iterator certItemIterator= certitems.iterator();
						 while(certItemIterator.hasNext())
						  {
							 eachRowsb=new StringBuffer();
							 perItemInCert = (CertificationItem) certItemIterator.next();
							 if(null != perItemInCert)
							 {
								 
								 try
								 {
									 
								  String identName=perEntityInCert.getIdentity();
								  eachRowsb.append(perItemInCert.getId());
								  eachRowsb.append(","+identName);
								  eachRowsb.append(",\""+perEntityInCert.getFirstname()+"\"");
								  eachRowsb.append(",\""+perEntityInCert.getLastname()+"\"");
								  
								  Identity identObj=null;
								  if(null != identName)
									  identObj=context.getObjectByName(Identity.class, identName);
								  
								  String description="";
								  String appName="";
								  String directAppName="";
								  String attribute="";
								  String entorRole="";
								  String displayName="";
								  String accountName="";
								  String nativeIdentity="";
								  String decision="";
								  String completionComments="";
								  String accessGrantedDateStr="";
								  String ownerNameStr="";
								  String lastLoginInfo="";
								  Date accessGrantedOn=null;
								  boolean highRisk=false;
								  String moverFlagStr="";
								  String relatedApplication="";
								  String accountStatus="";
								  String accountCreationDateStr="";
								  if(null != perItemInCert.getType() && (perItemInCert.getType().equals(CertificationItem.Type.Exception) || perItemInCert.getType().equals(CertificationItem.Type.Account)))
									{ 

									  
									  if(perItemInCert.getType().equals(CertificationItem.Type.Account))
									  {
									    description = "Account-Only";
									    entorRole="Account-Only";
									    eachRowsb.append(",Account");
									  }
									  else
									  eachRowsb.append(",Entitlement");
									  
									  
										 EntitlementSnapshot snap = perItemInCert.getExceptionEntitlements();
										 if(null != snap)
										 {
										  //accountName=snap.getNativeIdentity();
											 accountName=snap.getDisplayName();
											 nativeIdentity=snap.getNativeIdentity();
										  appName=snap.getApplicationName();	
										  directAppName=appName;
										  
										  if(!perItemInCert.getType().equals(CertificationItem.Type.Account))
										  {  
										    attribute = (String)snap.getAttributeName();
										    entorRole = (String)snap.getAttributeValue();
										  }
										  
										  if(null != appName && null != attribute && null != entorRole)
										  {
											  accessGrantedOn=getAccessGratedOn(appName, attribute, context, entorRole, nativeIdentity, identName);
											  
											  Filter filter = null;
											  
											    List<Filter> filterList = new ArrayList();											
										        filterList.add(Filter.eq("application.name", appName));									
										        if(null != attribute)
										          filterList.add(Filter.eq("attribute", attribute));									
										        if(null != entorRole)
										          filterList.add(Filter.eq("value", entorRole));									
										        filter = Filter.and(filterList);	
										        ManagedAttribute managedAttribute=null;
										        
										        try {
										         managedAttribute = context.getUniqueObject(ManagedAttribute.class, filter);	
										        }
										        catch(Exception e) {logger.error("Exception in certDownloadtoFile for managedAttribute perItemInCert:  "+perItemInCert, e);}
										        
										        if(null != managedAttribute)
										        {
										        	
										        	displayName=managedAttribute.getDisplayName();
										        	description=managedAttribute.getDescription("en_US");
										        	if(description == null)
									        			   description=""; 
										        	relatedApplication = (String)managedAttribute.getAttribute(SWE_ATTRIBUTE_RELATED_APPLICATION);
										        	if(null != relatedApplication && !relatedApplication.isEmpty() && directAppName.equalsIgnoreCase("Active Directory"))
										        	 {
										        		 appName=relatedApplication;										        
										        	 }
										        	 //else
										        	//	 isSox=isSoxApplication(directAppName,context);
										        	 
										        	 
										        	 Identity OwnerIdent=managedAttribute.getOwner();
										        	 ownerNameStr=getOwnerDisplayName(OwnerIdent);
										        	 
										        	 
										        	 highRisk=checkHighRiskAccess(managedAttribute,context);
										        	 lastLoginInfo=getLastLoginInformation(appName,relatedApplication,entorRole, context,managedAttribute,identName,nativeIdentity,accessGrantedOn);
											            
										        }
										       // else
										        //	isSox=isSoxApplication(directAppName,context);
										        
										        logger.debug("managedAttribute: " + managedAttribute);
										       }
										        
										        if(null != identObj)
								            	{
										        	IdentityService IS=new IdentityService(context);
										        	try
										        	{
								            		Link accountLink=IS.getLink(identObj, context.getObjectByName(Application.class, directAppName), null,nativeIdentity);
								            		    
									            		logger.debug("accountLink: " + accountLink);
									            		if (null != accountLink)
									            		{
									            			
									             		    //custom account name for service identities AD account
									            			String accName=(String)accountLink.getAttribute("displayName");
									            			logger.debug("accName: " + accName);
									            			if(null != directAppName && directAppName.equalsIgnoreCase("Active Directory") && null != accountLink && null != identObj.getStringAttribute("sweAccountType") && null != accName && !accName.isEmpty() && identObj.getStringAttribute("sweAccountType").equalsIgnoreCase("system"))
									             		   {
									             			   accountName=accName;									             			   
									             		   }
									            			
									            			
									            			Date accountCreatedOn=accountLink.getCreated();									            			
									            			 //if(description == null || description.equals(""))
									            		
									            			if(!directAppName.toLowerCase().startsWith("ncma"))		
									            			{
									            				accountCreationDateStr=uidisplaydateFormat.format(accountCreatedOn);
									            				String diffStr=getDatesDiff(currentDate,accountCreatedOn);
									            				accountCreationDateStr=diffStr+" ("+accountCreationDateStr +")";
									            			}
									            		     accountStatus="Active";
									            		     if(accountLink.isDisabled())									            		     
									            		    	 accountStatus="Disabled";									            		    										            		     
									            		     else if(accountLink.isLocked())									            		     
									            		    	 accountStatus="Locked";   									            		    	 
									            		     
									            		     
									            		     
										            		if(null == lastLoginInfo || lastLoginInfo.isEmpty())
										            		  lastLoginInfo=allAccessApplicationLastLogin(appName, relatedApplication,accountLink,context, accessGrantedOn);
										            		
										            		
										            		
										            		 //////////////////////////Check if it is mail box/////////////////////////////////////
									            		     
									          		         try
									               		    {
									          		        	  String DN=(String)accountLink.getAttribute("distinguishedName");
									          		        	  String mail=(String)accountLink.getAttribute("mail");
									          		        	  
									          		        	  if(null != DN && DN.toLowerCase().contains("ou=resource mailboxes") && null != mail)
									          		        	  {
										          		        											          		        		
									          		        		if(description.equals(""))
									          		        			description="Shared Mail Box (This is a Shared Mail Box with Name "+mail+")";
													    					else
													    						description=", Shared Mail Box (This is a Shared Mail Box with Name "+mail+"), "+ description;
									          		        	    
									          		        		
									          		        		/*
									          		        		  if(description.equals(""))
									          		        			description="Mail Box Name: "+mail;
								          	            			 else
								          	            				description=description=", Mail Box Name: "+mail; 
								          		        		   */
									          		        	  }
									          		    	          

										                		}
										              		catch(Exception e)
										              		{
										              			logger.debug("Error in  mail box check ",e);
										              		}
									          		        //////////////////////////////////////////////////////////////////////////////////////////////////
										            		
									            		}
										        	}
										        	catch (Exception e) {}
								            	}
										  }
										  
										  
										  
										  
									}
							
									 else if(null != perItemInCert.getType() && perItemInCert.getType().equals(CertificationItem.Type.Bundle))
									 {
										 eachRowsb.append(",Role");
										 
										 entorRole=perItemInCert.getBundle();
										 displayName=perItemInCert.getTargetDisplayName();
										 
										 
										 accessGrantedOn=getAccessGratedOn(null, null, context, entorRole, null, identName);
								               
										 if(null != accessGrantedOn)
										 {
											 accessGrantedDateStr = uidisplaydateFormat.format(accessGrantedOn);  
											 String diffStr=getDatesDiff(currentDate,accessGrantedOn);
											 accessGrantedDateStr=diffStr+" ("+accessGrantedDateStr +")";
										 }
								               Bundle roleObj = context.getObjectByName(Bundle.class, entorRole);
								        	   if (null != roleObj)
									           	 {
								        		   displayName=roleObj.getDisplayName();
								        		   description=roleObj.getDescription("en_US");
								        		   if(description == null)
								        			   description=""; 
								        		   
								        		   Identity OwnerIdent=roleObj.getOwner();
										           ownerNameStr=getOwnerDisplayName(OwnerIdent);
								        		   
									           	 }
								           	  
								        	
								        	   
								        	   
								        	     if(description.equals(""))
								        	    	 description="Role Type: "+roleObj.getType();
										  	          else
										  	        	description=description+", Role Type: "+roleObj.getType();
										  	          
										  	            if(null != roleObj && roleObj.isDisabled())
										  	            {
										  	              if(description.equals(""))
										  	            	description="Role Status: Inactive";
										  	              else
										  	            	description=description+", Role Status: Inactive";	  
										  	            }
										  	            /*else
										  	            {
										  	            	if(description.equals(""))
										  	            		description="Role Status: Active";
										  	            	else
										  	            		description=description+", Role Status: Active";
										  	            }*/
										 
									 }
								  
								  
								     
							         
							         if(description == null || description.isEmpty())        
								      	   description = "Not Available";
							         
							         description=description.replaceAll("[\\t\\n\\r]+"," ");       
							         description=description.replaceAll("\\<.*?\\>", "");
							         
							         if(displayName == null || displayName.isEmpty())        
							        	 displayName = entorRole;
							         
							         
									  logger.debug("appName: " + appName);
									  logger.debug("attribute: " + attribute);
									  logger.debug("entorRole: " + entorRole);
									  logger.debug("accountName: " + accountName);
									  logger.debug("description: " + description);
									  logger.debug("accessGrantedOn: "+ accessGrantedOn);
							         
									 try
					            	   {						               	               	               
							               if(null != accessGrantedOn)	
							               {
							            	   accessGrantedDateStr = uidisplaydateFormat.format(accessGrantedOn);
							            	   String diffStr=getDatesDiff(currentDate,accessGrantedOn);
											   accessGrantedDateStr=diffStr+" ("+accessGrantedDateStr +")";
							               }
					            	   }
					            	   catch(Exception e)
										 {
											 logger.error("Exception in certDownloadtoFile for accessGrantedOn:  "+accessGrantedOn, e);
										 }
									 
									 if(null != accessGrantedOn)											 										
									     moverFlagStr=getMoverViolation(accessGrantedOn, moverFlagStr, context, identObj, identName);
																		 
								
					            			
							         eachRowsb.append(",\""+appName+"\"");
							         eachRowsb.append(",\""+displayName+"\"");
							         eachRowsb.append(",\""+lastLoginInfo+"\"");
							         eachRowsb.append(",\""+highRisk+"\"");
							         eachRowsb.append(",\""+moverFlagStr+"\"");
							         eachRowsb.append(",\""+accessGrantedDateStr+"\"");
							         eachRowsb.append(",\""+accountCreationDateStr+"\"");
							         eachRowsb.append(",\""+accountStatus+"\"");							         
							         eachRowsb.append(",\""+ownerNameStr+"\"");
							         eachRowsb.append(",\""+description+"\"");
							         eachRowsb.append(",\""+accountName+"\"");
							         eachRowsb.append(",\""+perItemInCert.getSummaryStatus()+"\"");
							         
							         CertificationAction action=perItemInCert.getAction();
							         if(null != action && null != action.getStatus())
							         {
							        	 if(action.getStatus().equals(CertificationAction.Status.ApproveAccount) || action.getStatus().equals(CertificationAction.Status.Approved))
							        		 decision="Approved";
							        	 else
							        		 if(action.getStatus().equals(CertificationAction.Status.RevokeAccount) || action.getStatus().equals(CertificationAction.Status.Remediated)) 
							        			 decision="Revoked";
							        	 
							        	 if(null != action.getCompletionComments())
							        		 completionComments=action.getCompletionComments();
							         }
							         eachRowsb.append(","+decision);
							         eachRowsb.append(","+completionComments);
							         
	
								  
									  if(null != eachRowsb)
									  {
										 //eachRowsb.append("\n");
									     rowsList.add(eachRowsb.toString());
									  }
								 }
								 catch(Exception e)
								 {
									 logger.error("Exception in certDownloadtoFile for Cert Item:  "+perItemInCert, e);
								 }
							}
						  }
						 if(null != certItemIterator)
								sailpoint.tools.Util.flushIterator(certItemIterator);
						}
					 if(null != certEntityIterator)
							sailpoint.tools.Util.flushIterator(certEntityIterator);
					 
					   resultMap.put("fileData", rowsList);
					   
					   //Thread.sleep(300000);
					   //Date date = new Date();
						//long timeInMillis = date.getTime();
					   resultMap.put("certName", certObj.getName().replaceAll(" ","_"));//+"_"+timeInMillis);
				}
				
				 
				
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in certDownloadtoFile",e);
			  resultMap.put("errors", "Failed");
		  }
		 logger.debug("Exit certDownloadtoFile "+ resultMap);
			return resultMap;
	  }
	 
	@POST
	@Path("entOwnerCertDownloadtoFile")
	public Map entOwnerCertDownloadtoFile(Map<String,Object>paramMap)throws GeneralException {

logger.debug("Entered entOwnerCertDownloadtoFile file: " + paramMap);

String certId =(String)paramMap.get("certId");

Map resultMap =new HashMap();
List rowsList =new ArrayList();
rowsList.add("ItemId,User,First Name,Last Name,Access Type,Application,Access Name,Last Login,High Risk,Granted in Prior Job,Granted,Account Creation Date,Account Status,Access Owner,Access Description,Account Name,Status,Decision,Decision Comment");


try {
SailPointContext context = getContext();

Certification certObj = context.getObjectById(Certification.class, certId);
List certifierList = certObj.getCertifiers();
String loggedInUser =context.getUserName();
List certEntities = null;
List certitems = null;
CertificationItem perItemInCert = null;
CertificationEntity perEntityInCert = null;
StringBuffer eachRowsb = new StringBuffer();

 


if (null != certObj) {
certEntities = certObj.getEntities();
Iterator certEntityIterator = certEntities.iterator();
while (certEntityIterator.hasNext()) {
perEntityInCert = (CertificationEntity) certEntityIterator.next();
certitems = perEntityInCert.getItems();
Iterator certItemIterator = certitems.iterator();
while (certItemIterator.hasNext()) {
	boolean isSelfCertifier = false;
eachRowsb = new StringBuffer();
perItemInCert = (CertificationItem) certItemIterator.next();
if (null != perItemInCert) {

	try {

		String identName = perItemInCert.getTargetName();
		eachRowsb.append(perItemInCert.getId());


		Identity identObj = null;
		if (null != identName)
		identObj = context.getObjectByName(Identity.class, identName);
		if (null != identObj){
			 if (identName.equals(loggedInUser)){
                	isSelfCertifier = true;
                }
		eachRowsb.append("," + identName);
		eachRowsb.append(",\"" + identObj.getFirstname() + "\"");
		eachRowsb.append(",\"" + identObj.getLastname() + "\"");
		}

String description = "";
String appName = "";
String directAppName = "";
String attribute = "";
String entorRole = "";
String displayName = "";
String accountName = "";
String nativeIdentity = "";
String decision = "";
String completionComments = "";
String accessGrantedDateStr = "";
String ownerNameStr = "";
String lastLoginInfo = "";
Date accessGrantedOn = null;
boolean highRisk = false;
String moverFlagStr = "";
String relatedApplication = "";
String accountStatus = "";
String accountCreationDateStr = "";
if (null != perItemInCert.getType() && perItemInCert.getType().equals(CertificationItem.Type.DataOwner)) {
eachRowsb.append(",Entitlement");
EntitlementSnapshot snap = perItemInCert.getExceptionEntitlements();
if (null != snap) {
//accountName=snap.getNativeIdentity();
accountName = snap.getDisplayName();
nativeIdentity = snap.getNativeIdentity();
appName = snap.getApplicationName();
directAppName = appName;
attribute = (String)snap.getAttributeName();
entorRole = (String)snap.getAttributeValue();
if (null != appName && null != attribute && null != entorRole) {
accessGrantedOn = getAccessGratedOn(appName, attribute, context, entorRole, nativeIdentity, identName);

Filter filter = null;

List<Filter> filterList = new ArrayList();
filterList.add(Filter.eq("application.name", appName));
if (null != attribute)
filterList.add(Filter.eq("attribute", attribute));
if (null != entorRole)
filterList.add(Filter.eq("value", entorRole));
filter = Filter.and(filterList);
ManagedAttribute managedAttribute = null;

try {
managedAttribute = context.getUniqueObject(ManagedAttribute.class, filter);
} catch (GeneralException e) {
logger.error("Exception in entOwnerCertDownloadtoFile for managedAttribute perItemInCert: " + perItemInCert, e);
}

if (null != managedAttribute) {

displayName = managedAttribute.getDisplayName();
description = managedAttribute.getDescription("en_US");
if (description == null)
description = "";
relatedApplication = (String)managedAttribute.getAttribute(SWE_ATTRIBUTE_RELATED_APPLICATION);
if (null != relatedApplication && !relatedApplication.isEmpty() && directAppName.equalsIgnoreCase("Active Directory")) {
appName = relatedApplication;
}
//else
// isSox=isSoxApplication(directAppName,context);


Identity OwnerIdent = managedAttribute.getOwner();
ownerNameStr = getOwnerDisplayName(OwnerIdent);


highRisk = checkHighRiskAccess(managedAttribute, context);
lastLoginInfo = getLastLoginInformation(appName, relatedApplication, entorRole, context, managedAttribute, identName, nativeIdentity, accessGrantedOn);

}
// else
// isSox=isSoxApplication(directAppName,context);

logger.debug("managedAttribute: " + managedAttribute);
}

if (null != identObj) {
IdentityService IS = new IdentityService(context);
try {
Link accountLink = IS.getLink(identObj, context.getObjectByName(Application.class, directAppName), null, nativeIdentity);

logger.debug("accountLink: " + accountLink);
if (null != accountLink) {
	
									            			
									             		    //custom account name for service identities AD account
									            			//String accName=(String)accountLink.getAttribute("displayName");
									            			//logger.debug("accName: " + accName);
									            			//if(null != directAppName && directAppName.equalsIgnoreCase("Active Directory") && null != accountLink && null != identObj.getStringAttribute("sweAccountType") && null != accName && !accName.isEmpty() && identObj.getStringAttribute("sweAccountType").equalsIgnoreCase("system"))
									             		  // {
									             			  // accountName=accName;									             			   
									             		  // }

Date accountCreatedOn = accountLink.getCreated();
//if(description == null || description.equals(""))

if (!directAppName.toLowerCase().startsWith("ncma")) {
accountCreationDateStr = uidisplaydateFormat.format(accountCreatedOn);
String diffStr = getDatesDiff(currentDate, accountCreatedOn);
accountCreationDateStr = diffStr + " (" + accountCreationDateStr + ")";
}
accountStatus = "Active";
if (accountLink.isDisabled())
accountStatus = "Disabled";
else if (accountLink.isLocked())
accountStatus = "Locked";

 

if (null == lastLoginInfo || lastLoginInfo.isEmpty())
lastLoginInfo = allAccessApplicationLastLogin(appName, relatedApplication, accountLink, context, accessGrantedOn);

 

//////////////////////////Check if it is mail box/////////////////////////////////////

try {
String DN = (String) accountLink.getAttribute("distinguishedName");
String mail = (String) accountLink.getAttribute("mail");

if (null != DN && DN.toLowerCase().contains("ou=resource mailboxes") && null != mail) {

if (description.equals(""))
description = "Shared Mail Box (This is a Shared Mail Box with Name " + mail + ")";
else
description = ", Shared Mail Box (This is a Shared Mail Box with Name " + mail + "), " + description;


/*
if(description.equals(""))
description="Mail Box Name: "+mail;
else
description=description=", Mail Box Name: "+mail;
*/
}

} catch (Exception e) {
logger.debug("Error in mail box check ", e);
}

}
} catch (Exception e) {}
}
}

 


}

 


if (description == null || description.isEmpty())
description = "Not Available";

description = description.replaceAll("[\\t\\n\\r]+", " ");
description = description.replaceAll("\\<.*?\\>", "");

if (displayName == null || displayName.isEmpty())
displayName = entorRole;


logger.debug("appName: " + appName);
logger.debug("attribute: " + attribute);
logger.debug("entorRole: " + entorRole);
logger.debug("accountName: " + accountName);
logger.debug("description: " + description);
logger.debug("accessGrantedOn: " + accessGrantedOn);

try {
if (null != accessGrantedOn) {
accessGrantedDateStr = uidisplaydateFormat.format(accessGrantedOn);
String diffStr = getDatesDiff(currentDate, accessGrantedOn);
accessGrantedDateStr = diffStr + " (" + accessGrantedDateStr + ")";
}
} catch (Exception e) {
logger.error("Exception in entOwnerCertDownloadtoFile for accessGrantedOn: " + accessGrantedOn, e);
}

if (null != accessGrantedOn)
moverFlagStr = getMoverViolation(accessGrantedOn, moverFlagStr, context, identObj, identName);

 

eachRowsb.append(",\"" + appName + "\"");
eachRowsb.append(",\"" + displayName + "\"");
eachRowsb.append(",\"" + lastLoginInfo + "\"");
eachRowsb.append(",\"" + highRisk + "\"");
eachRowsb.append(",\"" + moverFlagStr + "\"");
eachRowsb.append(",\"" + accessGrantedDateStr + "\"");
eachRowsb.append(",\"" + accountCreationDateStr + "\"");
eachRowsb.append(",\"" + accountStatus + "\"");
eachRowsb.append(",\"" + ownerNameStr + "\"");
eachRowsb.append(",\"" + description + "\"");
eachRowsb.append(",\"" + accountName + "\"");
eachRowsb.append(",\"" + perItemInCert.getSummaryStatus() + "\"");

CertificationAction action = perItemInCert.getAction();
if (null != action && null != action.getStatus()) {
if (action.getStatus().equals(CertificationAction.Status.ApproveAccount) || action.getStatus().equals(CertificationAction.Status.Approved))
decision = "Approved";
else
if (action.getStatus().equals(CertificationAction.Status.RevokeAccount) || action.getStatus().equals(CertificationAction.Status.Remediated))
decision = "Revoked";

if (null != action.getCompletionComments())
completionComments = action.getCompletionComments();
}
eachRowsb.append("," + decision);
eachRowsb.append("," + completionComments);

 

if (null != eachRowsb) {
//eachRowsb.append("\n");
 if(!isSelfCertifier){
rowsList.add(eachRowsb.toString());
 }
}
} catch (GeneralException e) {
logger.error("Exception in entOwnerCertDownloadtoFile for Cert Item: " + perItemInCert, e);
}
}
}
if (null != certItemIterator)
sailpoint.tools.Util.flushIterator(certItemIterator);
}
if (null != certEntityIterator)
sailpoint.tools.Util.flushIterator(certEntityIterator);

resultMap.put("fileData", rowsList);

//Date date = new Date();
//long timeInMillis = date.getTime();
resultMap.put("certName", certObj.getName().replaceAll(" ", "_")); //+"_"+timeInMillis);
}

 

} catch (GeneralException e) {
// e.printStackTrace();
logger.error("Error in entOwnerCertDownloadtoFile", e);
resultMap.put("errors", "Failed");
}
logger.debug("Exit entOwnerCertDownloadtoFile " + resultMap);
return resultMap;
}
	
	 
	 
	 public String allAccessApplicationLastLogin(String applicationName, String relatedApplication,Link accountLink,SailPointContext context, Date accessGrantedOn)
	 {
		 
		 //////////////////////////last login logic for AA applications/////////////////////////////////////
	     String accessFlags="";
	     try
 		{
	    	 Custom customObj=context.getObjectByName(Custom.class, "SWE Certification Exclusion Custom");
		     if(null !=customObj && null !=customObj.get("lastLoginApplications"))
		     {
		    	 Map<String,Map> appsMap=( HashMap<String,Map> )customObj.get("lastLoginApplications");
		    	 if ((null != appsMap && appsMap.containsKey(applicationName) && null != appsMap.get(applicationName)) || (null != appsMap && appsMap.containsKey(relatedApplication) && null != appsMap.get(relatedApplication)))
		    	 {
		    		 
		    		 Map<String,String> attsMap=( HashMap<String,String> )appsMap.get(applicationName);
		    		 if(null == attsMap || attsMap.isEmpty())
		    		 {
		    			 attsMap=( HashMap<String,String> )appsMap.get(relatedApplication);
		    		 }
		    		 
		    		 String lastLoginAtt=(String)attsMap.get("schemaAttribute");
		    		 String dateFormat=(String)attsMap.get("dateFormat");
		    		 logger.debug("lastLoginAtt: "+lastLoginAtt);
		    		 logger.debug("dateFormat: "+dateFormat);
		    		 logger.debug("accountLink.getAttribute(lastLoginAtt): "+accountLink.getAttribute(lastLoginAtt));
		    		 if(null != lastLoginAtt)
		    		 {
		    			 
		    			 
		    			 try
		    			 {
		    				// current date before 6 months
		    					
		    					
		    					Calendar currentDateBefore2Months = Calendar.getInstance();
		    					currentDateBefore2Months.add(Calendar.DAY_OF_MONTH, -60);
		    					Calendar currentDateBefore1Months = Calendar.getInstance();
		    					currentDateBefore1Months.add(Calendar.DAY_OF_MONTH, -30);
		    				 if(null !=accountLink.getAttribute(lastLoginAtt) && !((String)accountLink.getAttribute(lastLoginAtt)).isEmpty())
		    				 {
    		    				 Date lastLoginDate=new SimpleDateFormat(dateFormat).parse((String)accountLink.getAttribute(lastLoginAtt));  
    		    				 String lastLoginDateStr = uidisplaydateFormat.format(lastLoginDate);
    		    				// String lastLoginOnlyDateStr = uidisplayonlydateFormat.format(lastLoginDate);
    		    				 logger.debug("lastLoginDateStr: "+lastLoginDateStr);
    		    				 String diffStr=getDatesDiff(currentDate,lastLoginDate);
    		    				 
    		    				 // code to check if last login is 01/01/1970 then never login
    		    				// code to check if last login is 01/01/1970 then never login
    		    				 SimpleDateFormat neverloginformat = new SimpleDateFormat(dateFormat);
    		    				 neverloginformat.setTimeZone(TimeZone.getTimeZone("UTC"));
    		    				 String tempDateStr=neverloginformat.format(new Date(0)); //01/01/1970
    		    				 Date neverLoginDate=new SimpleDateFormat(dateFormat).parse(tempDateStr);  
    		    				 
    		    				 if(lastLoginDate.equals(neverLoginDate)) //if 01/01/1970 then never login    		    				 
    		    				 {
    		    					 if(accessGrantedOn.before(currentDateBefore1Months.getTime()))
	   			    				  {
	   		    					  if(accessFlags.equals(""))
	   				    				   accessFlags="Last Login: Never (This Access never been used from access granted date, which is more than 30 days. Please review carefully as this user may no longer need this access.)";
	   		    					  else
	   		    						  accessFlags=accessFlags+", Last Login: Never (This Access never been used from access granted date, which is more than 30 days. Please review carefully as this user may no longer need this access.)";
	   		    						  
	   			    				  }
	   			    				  else
	   			    				  {
	   			    					if(accessFlags.equals(""))
	   			    					  accessFlags="Last Login: Never (This Access never been used from access granted date. Please review carefully as this user may no longer need this access.)";
	   			    					else
	   			    						accessFlags=accessFlags+", Last Login: Never (This Access never been used from access granted date. Please review carefully as this user may no longer need this access.)";
	   			    				  }
    		    				  }
    		    				
    		    				 else if(lastLoginDate.before(currentDateBefore2Months.getTime()))
    		    				 {	                		    				  	 
    		    				   //accessHelpInfo=accessHelpInfo+", Last Login Date: "+lastLoginDateStr;
    		    					 if(accessFlags.equals(""))
    		    				     accessFlags="Last Login: "+diffStr+" (Last Login Date was "+lastLoginDateStr+" which is more than 60 days. Please review carefully as this user may no longer need this access.)";
    		    					 else
    		    						 accessFlags=accessFlags+", Last Login: "+diffStr+" (Last Login Date was "+lastLoginDateStr+" which is more than 60 days. Please review carefully as this user may no longer need this access.)";
    		    				 }
    		    				 else if(lastLoginDate.before(currentDateBefore1Months.getTime()))
    		    				 {	                		    				  	 
    		    				   //accessHelpInfo=accessHelpInfo+", Last Login Date: "+lastLoginDateStr;
    		    					 if(accessFlags.equals(""))
    		    				     accessFlags="Last Login: "+diffStr+" (Last Login Date was "+lastLoginDateStr+" which is more than 30 days. Please review carefully as this user may no longer need this access.)";
    		    					 else
    		    						 accessFlags=accessFlags+", Last Login: "+diffStr+" (Last Login Date was "+lastLoginDateStr+" which is more than 30 days. Please review carefully as this user may no longer need this access.)";
    		    				 }
    		    				 else
    		    				 {
    		    					 if(accessFlags.equals(""))
			    						 accessFlags="Last Login: "+diffStr.toLowerCase()+" ("+lastLoginDateStr +")";
			    					 else
			    					 accessFlags=accessFlags+"Last Login: "+diffStr.toLowerCase()+" ("+lastLoginDateStr +")";
    		    				 }
		    				 
		    				 } 
		    				 else if(((String)accountLink.getAttribute(lastLoginAtt)).isEmpty())
		    				 {
		    					 
		    				  if(accessGrantedOn.before(currentDateBefore1Months.getTime()))
			    				  {
		    					  if(accessFlags.equals(""))
				    				   accessFlags="Last Login: Never (This Access never been used from access granted date, which is more than 30 days. Please review carefully as this user may no longer need this access.)";
		    					  else
		    						  accessFlags=accessFlags+", Last Login: Never (This Access never been used from access granted date, which is more than 30 days. Please review carefully as this user may no longer need this access.)";
		    						  
			    				  }
			    				  else
			    				  {
			    					if(accessFlags.equals(""))
			    					  accessFlags="Last Login: Never (This Access never been used from access granted date. Please review carefully as this user may no longer need this access.)";
			    					else
			    						accessFlags=accessFlags+", Last Login: Never (This Access never been used from access granted date. Please review carefully as this user may no longer need this access.)";
			    				  }
			    				  		
		    					// accessHelpInfo=accessHelpInfo+", Last Login Date: Not Accessed in Tracking Period";
		    				 }
		    					 
		    				  
		    			 }
		    				catch(Exception e)
		    			 {
		    				
		    				logger.error("Error inside allAccessApplicationLastLogin ",e);
		    			 }
		    			
		    	            
		    		 }
		    	 }
		     }
 			
 		}
 		catch(Exception e)
 		{
 			logger.debug("Error in allAccessApplicationLastLogin ",e);
 		}
	    

	     return accessFlags;
	     //////////////////////////////////////////////////////////////////////////////////////////////////
	 }
	 
	 public String getOwnerDisplayName(Identity OwnerIdent)
	 {
		 String ownerNameStr="";
		 logger.debug("Ent OwnerIdent: " + OwnerIdent);
         if(null != OwnerIdent && (null != OwnerIdent.getFirstname() || null != OwnerIdent.getLastname()))
         {
        	 ownerNameStr=OwnerIdent.getFirstname() +" " + OwnerIdent.getLastname()+" ("+OwnerIdent.getName()+")";
         	
         }
         else if(null != OwnerIdent)
         {
        	 ownerNameStr=OwnerIdent.getName();
         }
         
         return ownerNameStr;
		 
	 }
	 
	 public boolean isSoxApplication(String appName, SailPointContext context)
	 {
		 boolean isSox=false;
		 try
   		{
			 if(null != appName && appName.equalsIgnoreCase("Active Directory")) 
				 return false;
			 
		    Application a = context.getObjectByName(Application.class,appName);
	      	if(null != a)
	      	{	      		
	      			if(null != a.getAttributeValue(SWE_APP_ATTRIBUTE_SOX) && a.getAttributeValue(SWE_APP_ATTRIBUTE_SOX) instanceof Boolean)
	      			isSox = (Boolean)a.getAttributeValue(SWE_APP_ATTRIBUTE_SOX);
	      			else if(null != a.getAttributeValue(SWE_APP_ATTRIBUTE_SOX) && a.getAttributeValue(SWE_APP_ATTRIBUTE_SOX) instanceof String)
	             	{
	      			     String isSoxstr = (String)a.getAttributeValue(SWE_APP_ATTRIBUTE_SOX);
	      			     if(null != isSoxstr && isSoxstr.equalsIgnoreCase("TRUE"))
	      			    	isSox=true;
	             	}
	      			
	      		logger.debug("CustomSOXApplicationEntitlementColumn: " + isSox);			

	      	}
   		}
   		catch(Exception e)
   		{
   			logger.error("Exception in isSoxApplication for appName:  "+appName, e);
   		} 
         return isSox;
		 
	 }
	 
	 public List getLastLoginTimeStamp(String appName, String relatedApplication,String entorRole, SailPointContext context, ManagedAttribute managedAttribute, String identName, String nativeIdentity)
	 {
		 logger.debug("Entered getLastLoginTimeStamp: ");
			
		 Timestamp lastLogin=null;
		 List lastLoginList=new ArrayList();
		  		
		 try
		  {
			 
			 String value="";
			 if(null != managedAttribute)
				 value=managedAttribute.getValue();
			 
			 Custom sweCertsCustomObj=context.getObjectByName(Custom.class, "SWE Certification Addins Plugin Custom");
			 List<String> lastLoginAppsContains = new ArrayList<String>();
			 if(null != sweCertsCustomObj && null != sweCertsCustomObj.get("LastLoginAppsContains"))
				 lastLoginAppsContains=(List)sweCertsCustomObj.get("LastLoginAppsContains");
			 boolean accessInScope=false;
			 for(String eachAppName: lastLoginAppsContains)
			 {
				 if((null != appName && appName.toLowerCase().contains(eachAppName.toLowerCase())) || (null != relatedApplication && relatedApplication.toLowerCase().contains(eachAppName.toLowerCase())) || (null != value && value.toLowerCase().contains(eachAppName.toLowerCase())))
						 accessInScope=true;
			 }

			 
	         if(accessInScope)	
	          {

	           	    String LastLoginSPHINXSQL="sql: Select LAST_ACCESSED_DTTM From iiqcap1.PAC_LAST_LOGIN_DATA Where ID='"+managedAttribute.getId()+"' and Upper(LOGIN_ID)=UPPER('"+identName+"') and  Upper(NATIVE_IDENTITY)=UPPER('"+nativeIdentity+"') and active_record = 1";
	       		    //String LastLoginSPHINXSQL="sql: Select LAST_ACCESSED_DTTM From identityiq.SPHINX_USAGE Where ID='"+managedAttribute.getId()+"' and Upper(LOGIN_ID)=UPPER('"+identName+"') and  Upper(NATIVE_IDENTITY)=UPPER('"+nativeIdentity+"') and active_record = 1";
		            	QueryOptions qo = new QueryOptions();
		            	qo.setQuery(LastLoginSPHINXSQL);
		    			logger.debug("Executing query: LastLoginSPHINXSQL: "+ LastLoginSPHINXSQL);
		    			Iterator lastLoginIterator = context.search(LastLoginSPHINXSQL,null,qo);
		    			lastLoginList = sailpoint.tools.Util.iteratorToList(lastLoginIterator);
		    			sailpoint.tools.Util.flushIterator(lastLoginIterator);
		    			
		    			
	          }
			
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in getLastLoginTimeStamp",e);
			 
		  }
		 logger.debug("Exit getLastLoginTimeStamp "+ lastLoginList);
			return lastLoginList;
	 }
	 public String getLastLoginInformation(String appName, String relatedApplication,String entorRole, SailPointContext context, ManagedAttribute managedAttribute, String identName, String nativeIdentity, Date accessGrantedOn)
	 {
		 logger.debug("Entered getLastLoginInformation: ");
		 ////////////////Last Login at Entitlement Level//////////////////
         
         String accessFlags="";


       	  try
	    		{
           	   
	    			// current date before 6 months
					
					
					Calendar currentDateBefore2Months = Calendar.getInstance();
					currentDateBefore2Months.add(Calendar.DAY_OF_MONTH, -60);
					Calendar currentDateBefore1Months = Calendar.getInstance();
					currentDateBefore1Months.add(Calendar.DAY_OF_MONTH, -30);
					Timestamp lastLogin=null;
					logger.debug("appName: "+appName+" relatedApplication: "+relatedApplication+" entorRole: "+entorRole+" managedAttribute: "+managedAttribute+" identName: "+identName+" nativeIdentity: "+nativeIdentity);
					List lastLoginList =getLastLoginTimeStamp(appName,relatedApplication,entorRole, context,managedAttribute,identName,nativeIdentity);
					 logger.debug("lastLoginList: "+lastLoginList);
					if(null != lastLoginList && lastLoginList.size() > 0 )
	    			{
	    				lastLogin = (Timestamp) lastLoginList.get(0);
	    			
					
	    				 logger.debug("lastLogin: "+lastLogin);
	    			  if(null != lastLogin)
	    			  {
	    				String lastLoginDateStr = uidisplaydateFormat.format(lastLogin);

	    				String diffStr=getDatesDiff(currentDate,lastLogin);
	    				
	    				logger.debug("lastLoginDateStr: "+lastLoginDateStr);
		    				
	    				 if(lastLogin.before(currentDateBefore2Months.getTime()))
	    				 {
	    					 logger.debug("lastLoginDateStr is more than 2 months old");
	    				   //accessHelpInfo=accessHelpInfo+"Last Login Date: (Last Login Date is more than 6 months. Please review carefully as this user may no longer need this access.) "+lastLoginDateStr+"</span>";
	    					 if(accessFlags.equals(""))
	    				       accessFlags="Last Login: "+diffStr.toLowerCase()+"(Last Login Date was "+lastLoginDateStr+" which is more than 60 days. Please review carefully as this user may no longer need this access.)";
	    					 else
	    						 accessFlags=accessFlags+", Last Login: "+diffStr.toLowerCase()+"(Last Login Date was "+lastLoginDateStr+" which is more than 60 days. Please review carefully as this user may no longer need this access.)";
	    						 
	    				 }
	    				 else if(lastLogin.before(currentDateBefore1Months.getTime()))
	    				 {
	    					 logger.debug("lastLoginDateStr is more than 1 months old");
	    				   //accessHelpInfo=accessHelpInfo+"Last Login Date: (Last Login Date is more than 6 months. Please review carefully as this user may no longer need this access.) "+lastLoginDateStr+"</span>";
	    					 if(accessFlags.equals(""))
	    				       accessFlags="Last Login: "+diffStr.toLowerCase()+"(Last Login Date was "+lastLoginDateStr+" which is more than 30 days. Please review carefully as this user may no longer need this access.)";
	    					 else
	    						 accessFlags=accessFlags+", Last Login: "+diffStr.toLowerCase()+"(Last Login Date was "+lastLoginDateStr+" which is more than 30 days. Please review carefully as this user may no longer need this access.)";
	    						 
	    				 }
	    				 else
	    				 {
	    					 if(accessFlags.equals(""))
	    						 accessFlags="Last Login: "+diffStr.toLowerCase()+" ("+lastLoginDateStr +")";
	    					 else
	    					 accessFlags=accessFlags+"Last Login: "+diffStr.toLowerCase()+" ("+lastLoginDateStr +")";					    					 	 
	    				 }
	    			  }	
	    			  else
	    			  {
	    				  if(accessGrantedOn.before(currentDateBefore1Months.getTime()))
	    				  {
	    					  if(accessFlags.equals(""))
		    				   accessFlags="Last Login: Never (This access has not been used since the access was granted, which is more than 30 days. Please review carefully as this user may no longer need this access.)";
	    					  else
	    						  accessFlags=accessFlags+", Last Login: Never (This access has not been used since the access was granted, which is more than 30 days. Please review carefully as this user may no longer need this access.)";
	    				  }
	    				  else
	    				  {
	    					  if(accessFlags.equals(""))
	    					  accessFlags="Last Login: Never (This access has not been used since the access was granted. Please review carefully.)";
	    					  else
	    					  accessFlags=accessFlags+", Last Login: Never (This access has not been used since the access was granted. Please review carefully.)";
	    				  }
	    				  				    					  
	    				 // accessHelpInfo=accessHelpInfo+"<i class=\"fa fa-clock-o accessinfoicon\"/><span class=\"sweentlbl3\">Last Login Date: </span><span class=\"sweentval3\">Not Accessed in Tracking Period</span>";
	    			  }
	    		}	
             }
   		
   		catch(Exception e)
   		{
   			logger.error("Error in getting the last login for identName: "+ identName+ " managedAttribute.getId(): "+ managedAttribute.getId(), e);
   		}
      
   ////////////////////////////////////////////////////////////////// 
       	logger.debug("Exit getLastLoginInformation "+ accessFlags);
         return accessFlags;
	 }
	 
	 public Date  getAccessGratedOn(String applicationName, String attribute, SailPointContext context, String value, String nativeIdentity, String identName )
		{
			List<Filter> filterList = new ArrayList();
			Filter filter = null;
			Date accessGrantedOn=null;
		           filterList = new ArrayList();

		              if(null != applicationName && !applicationName.isEmpty())		       			    
			        filterList.add(Filter.eq("application.name", applicationName));	              
			        filterList.add(Filter.eq("identity.name", identName));
			        if(null != nativeIdentity && !nativeIdentity.isEmpty())
			        filterList.add(Filter.eq("nativeIdentity", nativeIdentity));
			        
			          
			        if(null != attribute && !attribute.isEmpty())
			          filterList.add(Filter.eq("name", attribute));
			
			        if(null != value && !value.isEmpty())
			          filterList.add(Filter.eq("value", value));
			
			        filter = Filter.and(filterList);
			
			        IdentityEntitlement identEnt;
					try {
						identEnt = context.getUniqueObject(IdentityEntitlement.class, filter);
						logger.debug("**************identEnt: " + identEnt);
					       
						
				        if(identEnt != null)
				        {
				        	accessGrantedOn=identEnt.getCreated();			        				        	
				        	//IdentityRequestItem reqitem=identEnt.getRequestItem();
				        	//if(null != reqitem)
				        	//	accessHelpInfo="Access Granted Date: "+accessGrantedOn.toString()+"<br/>"; reqitem.getIdentityRequest();
				        	//CurrentRequestItem
				        }
					} catch (GeneralException e) {
					}			        			       
			        
			       
		       
		      
	        
		       return accessGrantedOn;
		}
	 
	 
	 public String  checkMoverFlag(Date accessGrantedOn, SailPointContext context, Identity identObj, String identName)
		{
			 logger.debug("Entered checkMoverFlag: ");
				
			 String moverDatedispStr="";
			 try
			  {
				 if(null != identName && identName.toUpperCase().contains("_SRVC_"))
						return "";
					
				IdentityService IS=new IdentityService(context);
				Link hrmsaccountLink=IS.getLink(identObj, context.getObjectByName(Application.class, "TEDS - HR"), null,identName);
				
				if (null != hrmsaccountLink && null != accessGrantedOn)
				{
					// current date before 3 months
					Calendar currentDateBefore3Months = Calendar.getInstance();
					currentDateBefore3Months.add(Calendar.DAY_OF_MONTH, -90);
					
					if(null != hrmsaccountLink.getAttribute("MOVERDATE") && null != hrmsaccountLink.getAttribute("MOVERFLAG"))
					{
						
		    				String moverFlag = (String)hrmsaccountLink.getAttribute("MOVERFLAG");
		    				String moverDateStr = (String)hrmsaccountLink.getAttribute("MOVERDATE");
		    				
		    				if(moverFlag.equalsIgnoreCase("Y"))
		    				{
		    					Date moverDate=null;
		    					try {
		    						//moverDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a").parse(moverDateStr);
		    						moverDate = new SimpleDateFormat("MM/dd/yyyy").parse(moverDateStr);
		    						logger.debug(moverDate+"\t"+moverDate); 	            							            						
		    						
		    					} catch (ParseException e) {	            						            							            			
		    					}
		    					
		    					
		    					// if mover date is after access granted data and mover data is with in 3 months
		    					if(null != moverDate && accessGrantedOn.before(moverDate)) // && moverDate.after(currentDateBefore3Months.getTime()))
								{
		    						moverDatedispStr = uidisplaydateFormat.format(moverDate); 

								}
		    				}
						}
					else
					{
						// check if user has mover in history from TEDS Source
						String tedMoverSQL="sql: select max(to_date(moverDate, 'MM/DD/YYYY HH:MI:SS PM')) from iiqcap1.teds_iiq_history where accountid ='"+identName+"' and moverflag='Y' AND EMPLOYEESTATUS='A' AND load_ts >= sysdate-180";
						 
		     		    //String LastLoginSPHINXSQL="sql: Select LAST_ACCESSED_DTTM From identityiq.SPHINX_USAGE Where ID='"+managedAttribute.getId()+"' and Upper(LOGIN_ID)=UPPER('"+identName+"') and  Upper(NATIVE_IDENTITY)=UPPER('"+nativeIdentity+"') and active_record = 1";
			            	QueryOptions qo = new QueryOptions();
			            	qo.setQuery(tedMoverSQL);
			    			logger.debug("Executing query: tedMoverSQL: "+ tedMoverSQL);
			    			Iterator moverIterator = context.search(tedMoverSQL,null,qo);
			    			List moverList = sailpoint.tools.Util.iteratorToList(moverIterator);
			    			sailpoint.tools.Util.flushIterator(moverIterator);
			    			
			    					if(moverList.size() > 0 )
					    			{
			    						Date moverDate=(Date)moverList.get(0);
			    						if(null != moverDate && accessGrantedOn.before(moverDate))// && moverDate.after(currentDateBefore3Months.getTime()))
			    						{
			        						moverDatedispStr = uidisplaydateFormat.format(moverDate); 			        						
			    						}
					    			}
					}
						
					}
				
			  }
			  catch (Exception e)
			  {
				 // e.printStackTrace();
				  logger.debug("Error in checkMoverFlag",e);
				 
			  }
			 logger.debug("Exit checkMoverFlag "+ moverDatedispStr);
			return moverDatedispStr;
		}
	 
	 public String  getMoverViolation(Date accessGrantedOn, String accessFlags, SailPointContext context, Identity identObj, String identName)
		{
			// check for user recent job change
		 logger.debug("Enter getMoverViolation ");
			try
			{				
				String moverDatedispStr= checkMoverFlag(accessGrantedOn, context, identObj, identName);

			  if(null != moverDatedispStr && !moverDatedispStr.isEmpty())
			  {
				String accessGrantedOnStr = uidisplaydateFormat.format(accessGrantedOn);
				if(accessFlags.equals(""))
				 accessFlags="Granted in Prior Job (This User recently transferred roles on "+moverDatedispStr+" but was granted this access prior to that on "+accessGrantedOnStr+". Please review carefully to ensure they still require this access)";
				else
				accessFlags=accessFlags+", Granted in Prior Job (This User recently transferred roles on "+moverDatedispStr+" but was granted this access prior to that on "+accessGrantedOnStr+". Please review carefully to ensure they still require this access)";
			  }
			}		
			catch(Exception e)
			{
				
			}
			logger.debug("Exit getMoverViolation "+ accessFlags);
			return accessFlags;
		}
	 
	 @POST
	  @Path("getRoleDetails")
	  public Map getRoleDetails(Map<String, Object> paramMap)
	    throws GeneralException
	  {
		 logger.debug("Entered getRoleDetails: "+ paramMap);
			
		 Map resultMap = new HashMap();
		 String roleId=(String)paramMap.get("roleId");
		 if(null != roleId)
			 roleId=roleId.trim();
		 
		 List finalEntList=new ArrayList();
		 SailPointContext context=getContext();
		  		
		 try
		  {
			 Bundle roleObj = context.getObjectById(Bundle.class, roleId);
      	   if (null != roleObj)
         	  {
				 List rolesHierarchyList=getRoleHeirarchy(roleObj);
				 finalEntList=getRoleEntitlements(rolesHierarchyList, context);
				 resultMap.put("roleAccessDetails", finalEntList);
				 String roleName=roleObj.getName();
				 resultMap.put("roleName", roleName);
				 String roleDisplayName=roleObj.getDisplayName();
	  	            if(null == roleDisplayName || roleDisplayName.isEmpty())
	  	            	roleDisplayName=roleName;
				 resultMap.put("roleDispName", roleDisplayName);
         	  }
		  }
		  catch (Exception e)
		  {
			 // e.printStackTrace();
			  logger.error("Error in getRoleDetails",e);
			 
		  }
		 logger.debug("Exit getRoleDetails:resultMap "+ resultMap);
			return resultMap;
			
	  }
	 
	 

		
		// this method gets complete role hierarchy like child roles
			public List  getRoleHeirarchy(Bundle roleObj)
			  {
			 logger.debug("Enter getRoleHeirarchy roleObj: " + roleObj);
			  List rolesHierarchyList=new ArrayList();
			    try
			    {
				        List subroleslist=new ArrayList();
			            rolesHierarchyList.add(roleObj);

			            //get child required roles
			         if(roleObj.getFlattenedRequirements()!=null)
			          {
			            subroleslist=new ArrayList();
			            subroleslist.addAll(roleObj.getFlattenedRequirements());
			            rolesHierarchyList.addAll(subroleslist);
			          } 
			            //get child inherited roles
			        if(roleObj.getFlattenedInheritance()!=null)
			          {
			            subroleslist=new ArrayList();
			            subroleslist.addAll(roleObj.getFlattenedInheritance());
			            rolesHierarchyList.addAll(subroleslist);
			          }
			             //get child permited roles
			        if(roleObj.getFlattenedPermits()!=null)
			          {
			            subroleslist=new ArrayList();
			            subroleslist.addAll(roleObj.getFlattenedPermits());
			            rolesHierarchyList.addAll(subroleslist);
			          }   
				}	  
				  catch (Exception e)
				  {
					 logger.warn("Error while fetching getRoleHeirarchy from roleObj: "+ roleObj +" Exception: "+e);

				  }
			   logger.debug("Exit getRoleHeirarchy: " + rolesHierarchyList);
				return rolesHierarchyList;
				
			  }
			
			public HashMap<String, Object> getManagedAttributeDetails(String appName,String entitlement,String attribute, SailPointContext context)
		    {
				logger.debug("Enter getManagedAttributeDetails: appName "+ appName + "entitlement "+ entitlement + "attribute "+ attribute);
		    	Filter filter = null;
		    	
				
				
				ManagedAttribute ma=null;
			    Boolean isNPI=false;
		        Boolean isPCI=false;
		        boolean highRisk=false;

		        
		        HashMap<String, Object> eachTempMap= new HashMap<String, Object>();
		    	
		    	
		    	List<Filter> filterList = new ArrayList<Filter>();    	
		    	
		    	if(null != appName)
		        filterList.add(Filter.eq("application.name", appName));

		        if(null != attribute)
		          filterList.add(Filter.eq("attribute", attribute));

		        if(null != entitlement)
		          filterList.add(Filter.eq("value", entitlement));

		        filter = Filter.and(filterList);
		        try {
					 ma = context.getUniqueObject(ManagedAttribute.class, filter);
					
					if(null != ma)
					{
					 eachTempMap=new HashMap<String, Object>();
					 //eachTempMap.put("Id",accessId);
					 String dispName=ma.getDisplayName();
					 
					 if(null != dispName && !dispName.isEmpty())
					 eachTempMap.put("displayName",ma.getDisplayName());
					 else
					 eachTempMap.put("displayName",ma.getValue());				 
					 eachTempMap.put("value", ma.getValue()); 				 
					 eachTempMap.put("attribute",ma.getAttribute());
					 //eachTempMap.put("type","Entitlement");
					 String eachDesc=ma.getDescription("en_US");
					 if(null == eachDesc || eachDesc.isEmpty())
					 eachTempMap.put("description","Not Available");
					 else
					 eachTempMap.put("description",ma.getDescription("en_US"));
					 

					 highRisk=checkHighRiskAccess(ma,context);
			         String cbtinfo = (String)ma.getAttribute(SWE_ENT_ATTRIBUTE_CBT);
			         

			           if(null != ma.getAttribute(SWE_ENT_ATTRIBUTE_NPI))
			           {
			        	    String npi=(String)ma.getAttribute(SWE_ENT_ATTRIBUTE_NPI);
			        	    if(npi != null && npi.equalsIgnoreCase("TRUE"))
			   			      isNPI =true;
			           }

						if(null != ma.getAttribute(SWE_ENT_ATTRIBUTE_PCI))
			           {
			        	    String pci=(String)ma.getAttribute(SWE_ENT_ATTRIBUTE_PCI);
			        	    if(pci != null && pci.equalsIgnoreCase("TRUE"))
			        	    	isPCI =true;
			           }
			           
			           
			           
			            
			            Application directappObj = context.getObjectById(Application.class,ma.getApplicationId());
			            
			            if(null != directappObj)
			            {
				              eachTempMap.put("appName",directappObj.getName());
				              eachTempMap.put("iiqAppName",directappObj.getName());
			            }
			            
			           /* if(null != directappObj && null != directappObj.getAttributeValue(SWE_APP_ATTRIBUTE_SOX))
			            {
			  			isSox = (Boolean)directappObj.getAttributeValue(SWE_APP_ATTRIBUTE_SOX);            			
			            }
			            */
			            
			            String relatedApplication = (String)ma.getAttribute(SWE_ATTRIBUTE_RELATED_APPLICATION);
			            if(relatedApplication != null)
			            {
			              Application relatedappObj = context.getObjectByName(Application.class,relatedApplication);	
			              if(null != relatedappObj)
			              eachTempMap.put("appName",relatedappObj.getName());
			              	            	 
			            }
			            
			            
			            eachTempMap.put("isNPI",isNPI);
			            eachTempMap.put("isPCI",isPCI);
			            
			            if(null != cbtinfo && !cbtinfo.equals(""))
			            {
			            	cbtinfo="This Entitlement Requires following CBTs: "+cbtinfo;            
			                eachTempMap.put("cbtinfo",cbtinfo);
			            }
	            	
			            eachTempMap.put("isHighRisk",highRisk);
			            
			            eachTempMap.put("isPriv",highRisk);
			           
			            
			            if(null != ma.getOwner())
				            eachTempMap.put("ownerDisplayName",getUserDisplayName(ma.getOwner()));	
			            
					 }//if ma not null
					
				} catch (Exception e) {
					
					logger.error("Error in getManagedAttributeDetails", e);
				}
		        
		        logger.debug("Exit getManagedAttributeDetails: eachTempMap: "+eachTempMap);		        
		        return eachTempMap;
		        
		    }
			
			public String getUserDisplayName(Identity IdentObj )
			 {
				 String userDisplayName="";
				 String userName=IdentObj.getName();
				 String firstName=IdentObj.getFirstname();
				 String lastName=IdentObj.getLastname();
				 String sweTitle=(String)IdentObj.getAttribute("sweTitle");
				 String sweDeptIDDesc=(String)IdentObj.getAttribute("sweDeptIDDesc");
				 
				 if(null !=firstName && null !=lastName)
				 {
					 if(firstName.length() > 50)
						 firstName=firstName.substring(0,50)+".." ;
					 if(lastName.length() > 50)
						 lastName=lastName.substring(0,50)+".." ;
					 userDisplayName=firstName +" "+ lastName +" ("+ userName+")";
				 
				 }
				 else if(null !=firstName)
				 {
					 if(firstName.length() > 50)
						 firstName=firstName.substring(0,50)+".." ;
					 userDisplayName=firstName +" ("+ userName+")";
				 }
				 else if(null !=lastName)
				 {
					 if(lastName.length() > 50)
						 lastName=lastName.substring(0,50)+".." ;
					 userDisplayName=lastName +" ("+ userName+")";		  
				 }
				 
				 if(null !=sweTitle && !sweTitle.isEmpty())
				 {
					 if(sweTitle.length() > 50)
						 sweTitle=sweTitle.substring(0,50)+".." ;
					 userDisplayName=userDisplayName+" - "+ sweTitle;
				 }
				 if(null !=sweDeptIDDesc && !sweDeptIDDesc.isEmpty())
				 {
					 if(sweDeptIDDesc.length() > 50)
						 sweDeptIDDesc=sweDeptIDDesc.substring(0,50)+".." ;
					 userDisplayName=userDisplayName+", "+ sweDeptIDDesc;
				 }
				 
				 if(null == userDisplayName || userDisplayName.equals(""))
					 userDisplayName=userName;
				 
				 if(userDisplayName.length() > 200)
					 userDisplayName=userDisplayName.substring(0,100)+".." ;
				 
				 return userDisplayName;
			 }
			
			public List  getRoleEntitlements(List<Bundle> rolesHierarchyList, SailPointContext context)
			  {
				logger.debug("Enter getRoleEntitlements: "+ rolesHierarchyList);
			 	   List<Profile> eachRoleProfiles=new ArrayList<Profile>();
			 	   List finalEntList=new ArrayList();
			 	   List<String> eachvalList=new ArrayList<String>();
			 	   Map eachEntMap=new HashMap();
			 	   LeafFilter leafFilter=null; 	
			  try
			   {
				for(Bundle eachRoleObj: rolesHierarchyList)
				   {
					
				          eachRoleProfiles=eachRoleObj.getProfiles();
						  logger.debug("eachRoleProfiles: "+ eachRoleProfiles);
						   if(null != eachRoleProfiles && !eachRoleProfiles.isEmpty())
						   {
							   for(Profile eachProfile : eachRoleProfiles)
	                        {
								   List listFilter = eachProfile.getConstraints();
								   Application eachappObj=eachProfile.getApplication();
								   String appName="";
								   if(null != eachappObj)
									   appName=eachappObj.getName();
								   int filterSize = listFilter.size();
								   for(int iCount=0;iCount<filterSize;iCount++){
										if(listFilter.get(iCount) instanceof LeafFilter){
										  leafFilter = (LeafFilter)listFilter.get(iCount);
										  Object eachValue = leafFilter.getValue();
										  String eachattrName=leafFilter.getProperty();
										 logger.debug("eachValue: "+ eachValue);
										 logger.debug("eachattrName: "+ eachattrName);
											 if(eachValue instanceof ArrayList || eachValue instanceof List){
												 
												 //finalEntList.addAll((List)eachValue);
												 eachvalList=new ArrayList<String>((List)eachValue);
												 logger.debug("enter list block: eachvalList: "+eachvalList);
												 for(String eachentVal: eachvalList)
												 {
													 logger.debug("enter for block: eachentVal: "+eachentVal);
													 eachEntMap= getManagedAttributeDetails(appName, (String)eachentVal,eachattrName,context);
													 logger.debug("eachEntMap: "+ eachEntMap);
													   if(null == eachEntMap || eachEntMap.isEmpty())
													   {
														   eachEntMap=new HashMap();
														   eachEntMap.put("appName", appName);
														   eachEntMap.put("displayName", eachentVal);
														   eachEntMap.put("attribute", eachattrName);
														   eachEntMap.put("description","Not Available");
													   }
													   finalEntList.add(eachEntMap);
												 }
											  }
											 if(eachValue instanceof String){
											   {
												   //finalEntList.add(eachValue);
												   
												   eachEntMap= getManagedAttributeDetails(appName, (String)eachValue,eachattrName,context);
												   logger.debug("eachEntMap: "+ eachEntMap);
												   if(null == eachEntMap || eachEntMap.isEmpty())
												   {
													   eachEntMap=new HashMap();
													   eachEntMap.put("appName", appName);
													   eachEntMap.put("displayName", eachValue);
													   eachEntMap.put("attribute", eachattrName);
													   eachEntMap.put("description","Not Available");
												   }
												   finalEntList.add(eachEntMap);
											   }
											 }
											   												
										 
									  } 
	                        }
						   }
					   }
					   
			      }
			    }
				catch (Exception e)
				  {
					 logger.error("Error while fetching getRoleHeirarchy from roleObj: "+ rolesHierarchyList +" Exception: "+e);

				  }
			  logger.debug("Exit getRoleEntitlements: "+ finalEntList);
				return finalEntList;
			  }
			
			
			public String getDatesDiff(Date currentDate,Date datetoCheck)
			{
				String dispDateStr="";
				 try
			    	{ 
					 long difference_In_Time = currentDate.getTime()-datetoCheck.getTime();
			 	     long days=TimeUnit.DAYS.convert(difference_In_Time, TimeUnit.MILLISECONDS);
			 	     
			 	    LocalDate localDatetoCheck = datetoCheck.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		 			LocalDate currLocalDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			 	     
			 	    if(days <= 31 && days > 7) 
			 	    {
			 	    	long diffInDays = ChronoUnit.DAYS.between(localDatetoCheck, currLocalDate); 
	 			        long diffWeeks = diffInDays / 7;
			 	    	if(diffWeeks == 1)
			 	    	dispDateStr=diffWeeks+ " Week Ago";
			 	    	else
			 	    	dispDateStr=diffWeeks+ " Weeks Ago";	
			 	    	
			 	    	
			 	     }
			 	     else if(days > 31)
			 	      {
			 	    	   long monthsDiff = ChronoUnit.MONTHS.between(localDatetoCheck,currLocalDate);
						    if(monthsDiff > 12)
						    {
						    	long difference_In_Years = java.time.temporal.ChronoUnit.YEARS.between( localDatetoCheck , currLocalDate );
						    	if(difference_In_Years == 1)
						    	dispDateStr=difference_In_Years+ " Year Ago";
						    	else
						    	dispDateStr=difference_In_Years+ " Years Ago";
						    }
						    else
						    {
						    	if(monthsDiff == 1)
						    	dispDateStr=monthsDiff+ " Month Ago";
						    	else
						    		dispDateStr=monthsDiff+ " Months Ago";	
						    }
			 	     }
			 	      else
			 	      {
			 	    	  if(days == 1)
			 	    		 dispDateStr=days+ " Day Ago";
			 	    	  else
			 	    	 dispDateStr=days+ " Days Ago";
			 	      }

			 	      
			 	      
			      }
			 	catch(Exception e) {logger.warn("Error getDatesDiff ",e);}
			    
				    return dispDateStr;
			}
	  
			@POST
			  @Path("insertAccessDescFeedback")
			  public String insertAccessDescFeedback(Map<String, Object> paramMap) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				logger.debug("Enter insertAccessDescFeedback: ");		
			    String response="Success";
			    
			      //Object Manager Audit
			    Connection con=null;
			    PreparedStatement pstmt=null;
				  try
				  {
					  SailPointContext context=getContext();
					  //get variables from request
					  String feedbacktext=(String)paramMap.get("feedbacktext");
					  String certId=(String)paramMap.get("certId");
					  String certItemId=(String)paramMap.get("certItemId");
					  String accessId=(String)paramMap.get("accessId");
					  String rating=(String)paramMap.get("rating");
					  String accessType="";
					  String entorRole="";
					  String appName="";
					  String attribute="";
					  if(null != certItemId && !certItemId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem && null != certItem.getType() && certItem.getType().equals(CertificationItem.Type.Exception))
						  {
							  accessType="Entitlement";

							  EntitlementSnapshot snap = certItem.getExceptionEntitlements();
								 if(null != snap)
								 {
								    appName=snap.getApplicationName();	
 								    attribute = (String)snap.getAttributeName();
								    entorRole = (String)snap.getAttributeValue();
								  }
						  }
						  else  if(null != certItem && null != certItem.getType() && certItem.getType().equals(CertificationItem.Type.Bundle))
						  {
							  entorRole=certItem.getBundle();
							  accessType="Role";
							  appName="IIQ";
							  attribute="Role";
						  }

					  }
	
					  
					  String operator=context.getUserName();
					  //takes a way the first / if found in certId
					  
					  if(null == certId)
					  {
						  certId="";
					  }
					  
					  con=context.getJdbcConnection();
					  
					  if(accessType.equalsIgnoreCase("Role"))
					  {
						  pstmt = con.prepareStatement("delete from iiqcap1.CERT_ACCESS_DESC_FEEDBACK  where Access_value = ? and CERTIFIER_EID = ? and CERT_ID= ?");
						  pstmt.setString(1,entorRole);
						  pstmt.setString(2,operator);
						  pstmt.setString(3,certId);
					  }
					  else
					  {
						  pstmt = con.prepareStatement("delete from iiqcap1.CERT_ACCESS_DESC_FEEDBACK  where application_name = ? and Access_name = ? and Access_value = ? and CERTIFIER_EID= ? and CERT_ID= ?");  		
						  pstmt.setString(1,appName);
						  pstmt.setString(2,attribute);
						  pstmt.setString(3,entorRole);
						  pstmt.setString(4,operator);
						  pstmt.setString(5,certId);
					  }
					  
					  pstmt.execute();
					  if(null != pstmt)
						  pstmt.close();
					  
					  pstmt = con.prepareStatement("insert into iiqcap1.CERT_ACCESS_DESC_FEEDBACK values (?,?,?,sysdate,?,?,?,?,?,?,?)");
					  pstmt.setString(1,certId);
					  pstmt.setString(2,operator);
					  pstmt.setString(3,certItemId);
					  pstmt.setString(4,accessType);
					  pstmt.setString(5,accessId);					  
					  pstmt.setString(6,appName);
					  pstmt.setString(7,attribute);
					  pstmt.setString(8,entorRole);
					  pstmt.setInt(9,Integer.parseInt(rating));
					  pstmt.setString(10,feedbacktext);
					  pstmt.execute();
				  }
				  catch (Exception e)
				  {
					// e.printStackTrace();
					  logger.error("Exception in insertAccessDescFeedback",e);
					  response="Failed: "+e;
				  }
				  finally
				  {
					  try
					  {
						  if(null != con)
							  con.close();
						  
						  if(null != pstmt)
							  pstmt.close();
					  }
					  catch (Exception e) {}
				  }
				  
				  
				  logger.debug("Exit insertAccessDescFeedback: "+response);
			    
				  
				  
			    return response;//new ListResult(localList, localList.size());
			  }
			@POST
			  @Path("updateAccessDescFeedback")
			  public String updateAccessDescFeedback(Map<String, Object> paramMap) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				logger.debug("Enter updateAccessDescFeedback: ");		
			    String response="Success";
			    
			      //Object Manager Audit
			    Connection con=null;
			    PreparedStatement pstmt=null;
				  try
				  {
					  SailPointContext context=getContext();
					  //get variables from request
					  String feedbacktext=(String)paramMap.get("feedbacktext");
					  String certItemId=(String)paramMap.get("certItemId");
					  String rating=(String)paramMap.get("rating");
					  String operator=context.getUserName();
					  String certId=(String)paramMap.get("certId");
					  String accessId=(String)paramMap.get("accessId");
					  
					  
					  if(null == certId)
					  {
						  certId="";
					  }
					 
					  
					  con=context.getJdbcConnection();
					  pstmt = con.prepareStatement("update iiqcap1.CERT_ACCESS_DESC_FEEDBACK set PlAIN_FEEDBACK = ? where ACCESS_ID= ? and CERT_ID= ? and CERTIFIER_EID= ? and LAST_UPDATED_DTTM=(select max(LAST_UPDATED_DTTM) from iiqcap1.CERT_ACCESS_DESC_FEEDBACK where ACCESS_ID= ? and CERT_ID= ? and CERTIFIER_EID= ?)");
					  pstmt.setString(1,feedbacktext);
					  pstmt.setString(2,accessId);
					  pstmt.setString(3,certId);
					  pstmt.setString(4,operator);
					  pstmt.setString(5,accessId);
					  pstmt.setString(6,certId);
					  pstmt.setString(7,operator);
					  
					  pstmt.execute();
				  }
				  catch (Exception e)
				  {
					// e.printStackTrace();
					  logger.error("Exception in updateAccessDescFeedback",e);
					  response="Failed: "+e;
				  }
				  finally
				  {
					  try
					  {
						  if(null != con)
							  con.close();
						  
						  if(null != pstmt)
							  pstmt.close();
					  }
					  catch (Exception e) {}
				  }
				  
				  
				  logger.debug("Exit updateAccessDescFeedback: "+response);
			    
				  
				  
			    return response;//new ListResult(localList, localList.size());
			  }
			
			@POST
			  @Path("getExistingAccessDescFeedback")
			  public String getExistingAccessDescFeedback(Map<String, Object> paramMap) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				logger.debug("Enter getExistingAccessDescFeedback: ");		
			    String feedbackText="";
			    

				  try
				  {
					  SailPointContext context=getContext();
					  //get variables from request
					  String certItemId=(String)paramMap.get("certItemId");
					  String accessType="";
					  String entorRole="";
					  String appName="";
					  String attribute="";
					  
					  if(null != certItemId && !certItemId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem.getType() && certItem.getType().equals(CertificationItem.Type.Exception))
						  {
							  accessType="Entitlement";

							  EntitlementSnapshot snap = certItem.getExceptionEntitlements();
								 if(null != snap)
								 {
								    appName=snap.getApplicationName();	
								    attribute = (String)snap.getAttributeName();
								    entorRole = (String)snap.getAttributeValue();
								  }
						  }
						  else  if(null != certItem.getType() && certItem.getType().equals(CertificationItem.Type.Bundle))
						  {
							  entorRole=certItem.getBundle();
							  accessType="Role";
						  }

					  }

					  String operator=context.getUserName();
					  String descRatingSQL="";
					  
					// existing feedback of requester
						if(accessType.equalsIgnoreCase("Role"))
							descRatingSQL="sql: select PLAIN_FEEDBACK  from (select tab1.PLAIN_FEEDBACK from iiqcap1.CERT_ACCESS_DESC_FEEDBACK tab1 where tab1.Access_value='"+entorRole+"' and tab1.CERTIFIER_EID='"+operator+"'  order by LAST_UPDATED_DTTM desc) where rownum < 2";
			               else
			               descRatingSQL="sql: select PLAIN_FEEDBACK  from (select tab1.PLAIN_FEEDBACK from iiqcap1.CERT_ACCESS_DESC_FEEDBACK tab1 where tab1.application_name='"+appName+"' and tab1.Access_name='"+attribute+"' and tab1.Access_value='"+entorRole+"' and tab1.CERTIFIER_EID='"+operator+"'  order by LAST_UPDATED_DTTM desc) where rownum < 2";
						

						       List resultList=new ArrayList();
						        
						
						       try
								{
			  	            	QueryOptions qo = new QueryOptions();
				            	qo.setQuery(descRatingSQL);
				            	logger.debug("Executing query: getExistingAccessDescFeedback: descRatingSQL "+ descRatingSQL);
				    			Iterator moverIterator = context.search(descRatingSQL,null,qo);
				    			resultList = sailpoint.tools.Util.iteratorToList(moverIterator);
				    			sailpoint.tools.Util.flushIterator(moverIterator);
				    			
				    			if(resultList.size() > 0)
				    				feedbackText=(String) resultList.get(0);
		    						
								}	
								catch(Exception e)
								{
									logger.warn("Error in current getExistingAccessDescFeedback ",e);
								}

				  }
				  catch (Exception e)
				  {
					// e.printStackTrace();
					  logger.error("Exception in getExistingAccessDescFeedback",e);
					  
				  }				  
				  logger.debug("Exit getExistingAccessDescFeedback: "+feedbackText);

			    return feedbackText;//new ListResult(localList, localList.size());
			  }
			
			@POST
			  @Path("clearExistingAccessDescFeedback")
			  public String clearExistingAccessDescFeedback(Map<String, Object> paramMap) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				logger.debug("Enter clearExistingAccessDescFeedback: ");		
			    String response="Success";
			    
			      //Object Manager Audit
			    Connection con=null;
			    PreparedStatement pstmt=null;
				  try
				  {
					  SailPointContext context=getContext();
					  //get variables from request
					  String certItemId=(String)paramMap.get("certItemId");
					  String operator=context.getUserName();
					  String accessType="";
					  String entorRole="";
					  String appName="";
					  String attribute="";
					  logger.debug("certItemId: "+certItemId);
					  if(null != certItemId && !certItemId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem.getType() && certItem.getType().equals(CertificationItem.Type.Exception))
						  {
							  accessType="Entitlement";

							  EntitlementSnapshot snap = certItem.getExceptionEntitlements();
								 if(null != snap)
								 {
								    appName=snap.getApplicationName();	
								    attribute = (String)snap.getAttributeName();
								    entorRole = (String)snap.getAttributeValue();
								  }
						  }
						  else  if(null != certItem.getType() && certItem.getType().equals(CertificationItem.Type.Bundle))
						  {
							  entorRole=certItem.getBundle();
							  accessType="Role";
						  }

					  }
					  logger.debug("accessType: "+accessType);
					  logger.debug("entorRole: "+entorRole);
					  logger.debug("attribute: "+attribute);
					  logger.debug("operator: "+operator);
					  
					  
					  
					  con=context.getJdbcConnection();
					  if(accessType.equalsIgnoreCase("Role"))
					  {
						  pstmt = con.prepareStatement("delete from iiqcap1.CERT_ACCESS_DESC_FEEDBACK  where Access_value = ? and CERTIFIER_EID = ?");
						  pstmt.setString(1,entorRole);
						  pstmt.setString(2,operator);
					  }
					  else
					  {
						  pstmt = con.prepareStatement("delete from iiqcap1.CERT_ACCESS_DESC_FEEDBACK  where application_name = ? and Access_name = ? and Access_value = ? and CERTIFIER_EID= ?");  		
						  pstmt.setString(1,appName);
						  pstmt.setString(2,attribute);
						  pstmt.setString(3,entorRole);
						  pstmt.setString(4,operator);
					  }
					  
					  pstmt.execute();
				  }
				  catch (Exception e)
				  {
					// e.printStackTrace();
					  logger.error("Exception in clearExistingAccessDescFeedback",e);
					  response="Failed: "+e;
				  }
				  finally
				  {
					  try
					  {
						  if(null != con)
							  con.close();
						  
						  if(null != pstmt)
							  pstmt.close();
					  }
					  catch (Exception e) {}
				  }
				  
				  
				  logger.debug("Exit clearExistingAccessDescFeedback: "+response);
			    
				  
				  
			    return response;//new ListResult(localList, localList.size());
			  }
			
			public boolean checkHighRiskAccess(ManagedAttribute managedAttribute,SailPointContext context) throws GeneralException
			{
				//logger.setLevel(Level.DEBUG);
				logger.debug("Enter checkHighRiskAccess: ");		
			    boolean result=false;
		
				 boolean isPriv=false;
				 boolean isSox=false;
				  try
				  {

						  String privileged = (String)managedAttribute.getAttribute(SWE_ATTRIBUTE_PRIVILEGED);
				        	 if(privileged != null && privileged.equalsIgnoreCase("TRUE"))											            
					            	isPriv=true;
						  if(!isPriv)
						  {
							  Application appObj=managedAttribute.getApplication();
							  String directAppName="";
							  if(null != appObj)
								  directAppName=  appObj.getName();
							  
							  String relatedApplication = (String)managedAttribute.getAttribute(SWE_ATTRIBUTE_RELATED_APPLICATION);
							  if(null != relatedApplication && !relatedApplication.isEmpty() && directAppName.equalsIgnoreCase("Active Directory"))
					        	 {
	
					        		 isSox=isSoxApplication(relatedApplication,context);										        
					        	 }
							  
							  if(!isSox && !directAppName.equalsIgnoreCase("Active Directory"))
					           {
					        	 if(null != managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX) && managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX) instanceof Boolean)
					   			     isSox = (Boolean)managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX);
					             	else if(null != managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX) && managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX) instanceof String)
					             	{
					      			     String isSoxstr = (String)managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX);
					      			     if(null != isSoxstr && isSoxstr.equalsIgnoreCase("TRUE"))
					      			    	isSox=true;
					             	}
					           }
							  
							  
						  
					    }
					  
					  
					  if(isPriv || isSox)
						  result=true;
				  }
				  catch (Exception e)
				  {
					// e.printStackTrace();
					  logger.error("Exception in checkHighRiskAccess",e);

				  }
				  
				  logger.debug("Exit checkHighRiskAccess: "+result);

			    return result;
			}
			
			@POST
			  @Path("isThisHighRiskAccess")
			  public Map isThisHighRiskAccess(Map<String, Object> paramMap) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				 Map resultMap=new HashMap();
				logger.debug("Enter isThisHighRiskAccess paramMap: "+paramMap);		
			    boolean result=false;
				String accessId=(String)paramMap.get("accessId");
				String certItemId=(String)paramMap.get("certItemId");
				
				 boolean isPriv=false;
				 boolean isSox=false;
				  try
				  {
					  SailPointContext context=getContext();
					  ManagedAttribute managedAttribute=context.getObjectById(ManagedAttribute.class,accessId);
					  if(null != managedAttribute)
					  {
						  String privileged = (String)managedAttribute.getAttribute(SWE_ATTRIBUTE_PRIVILEGED);
				        	 if(privileged != null && privileged.equalsIgnoreCase("TRUE"))											            
					            	isPriv=true;

							  Application appObj=managedAttribute.getApplication();
							  String directAppName="";
							  if(null != appObj)
								  directAppName=  appObj.getName();
							  
							  String relatedApplication = (String)managedAttribute.getAttribute(SWE_ATTRIBUTE_RELATED_APPLICATION);
							  if(null != relatedApplication && !relatedApplication.isEmpty() && directAppName.equalsIgnoreCase("Active Directory"))
					        	 {
	
					        		 isSox=isSoxApplication(relatedApplication,context);										        
					        	 }
							  
							  if(!isSox && !directAppName.equalsIgnoreCase("Active Directory"))
					           {
					        	 if(null != managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX) && managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX) instanceof Boolean)
					   			     isSox = (Boolean)managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX);
					             	else if(null != managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX) && managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX) instanceof String)
					             	{
					      			     String isSoxstr = (String)managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_SOX);
					      			     if(null != isSoxstr && isSoxstr.equalsIgnoreCase("TRUE"))
					      			    	isSox=true;
					             	}
					           }
							  
							  
					           if(null != managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_NPI))
					           {
					        	    String npi=(String)managedAttribute.getAttribute(SWE_ENT_ATTRIBUTE_NPI);
					        	    if(npi != null && npi.equalsIgnoreCase("TRUE"))
					        	    resultMap.put("isNPI", true);
					           }

					  
					  
						  if(isPriv || isSox)
						  {
							  resultMap.put("HighRisk", true);							
						  }
						  
						  if(isPriv)
							  resultMap.put("isPriv", true);
						  if(isSox)
							  resultMap.put("isSox", true);
						  
						  
						  if(null == resultMap || resultMap.isEmpty())
								resultMap=new HashMap();
							
								resultMap.put("result", true);
								resultMap.put("certItemId", certItemId);
						  
					  }
				  }
				  catch (Exception e)
				  {
					  logger.warn("Exception in isThisHighRiskAccess",e);

				  }
				  
				  logger.debug("Exit isThisHighRiskAccess: "+resultMap);

			    return resultMap;
			  }
			
			
			@POST
			  @Path("getOtherData")
			  public Map getOtherData(Map<String, Object> paramMap) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				logger.debug("Enter getOtherData: paramMap: "+ paramMap);
				 Map resultMap=new HashMap();
				 Map tempMap=new HashMap();
				  try
				  {
					  
					  tempMap=isThisHighRiskAccess(paramMap);
					  resultMap.putAll(tempMap);
					  tempMap=new HashMap();
					  
					  tempMap=getLastLoginInfo(paramMap);
					  resultMap.putAll(tempMap);
					  tempMap=new HashMap();
					  
					  tempMap=getGrantedinPriorJob(paramMap);
					  resultMap.putAll(tempMap);
					  tempMap=new HashMap();
					  
					  tempMap=getAccessStatusOtherInfo(paramMap);
					  resultMap.putAll(tempMap);
					  tempMap=new HashMap();
					  
					  if(null != paramMap && null != paramMap.get("sysIdCert") && ((boolean) paramMap.get("sysIdCert")))
					  {
					   tempMap=getSharedMailBoxInfo(paramMap);
					   resultMap.putAll(tempMap);
					   tempMap=new HashMap();
					  }
					  if(null != paramMap && null != paramMap.get("entOwnerCert") && ((boolean) paramMap.get("entOwnerCert")))
					  {
						   tempMap=getEntGrantedByRoleinfo(paramMap);
						   resultMap.putAll(tempMap);
						   tempMap=new HashMap();
					   
						  tempMap=isSelfReviewItem(paramMap);
						  resultMap.putAll(tempMap);
						  tempMap=new HashMap();
					  }
					  
					 /* if(null != paramMap && null != paramMap.get("displayPrevScores") && ((boolean) paramMap.get("displayPrevScores")))
					  {
						  tempMap=getPrevalenceScoresPerAccessfromCacheorAPI(paramMap);
						  resultMap.putAll(tempMap);
					  }
					  */
					  
					  
					   tempMap=getAccessSunsetDateInfo(paramMap);
					  resultMap.putAll(tempMap);
					  tempMap=new HashMap();
					  

				  }
				  catch (Exception e)
				  {
					  logger.warn("Exception in getOtherData",e);

				  }
				  
				  logger.debug("Exit getOtherData: "+resultMap);

			    return resultMap;
			  }
			
			public Map  isSelfReviewItem(Map<String, Object> paramMap) throws GeneralException
			{
				Map resultMap=new HashMap();
				logger.debug("Enter isSelfReviewItem");
			
				
				try
				{
					
					SailPointContext context=getContext();
					  //get variables from request
					  String certItemId=(String)paramMap.get("certItemId");
					  String accessId=(String)paramMap.get("accessId");
					  String operator=context.getUserName();
					 
					  
					  if(null != certItemId && !certItemId.isEmpty())
					  {
                          
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  logger.debug("operator: "+ operator);
						  if(null != certItem && certItem.getTargetName().equalsIgnoreCase(operator))
								  {
							          logger.debug("self item certItem.getTargetName(): "+ certItem.getTargetName());
									  resultMap.put("selfReviewItem", true);									 
								  }
						  
						  						  
						 
					  }
					  
					if(null == resultMap || resultMap.isEmpty())
						resultMap=new HashMap();
					
						resultMap.put("result", true);
						resultMap.put("certItemId", certItemId);
						
					
				}		
				catch(Exception e)
				{
					logger.warn("Error in isSelfReviewItem ",e);
				}
				
				logger.debug("Exit isSelfReviewItem: "+ resultMap);
				return resultMap;
			}
			
			@POST
			  @Path("getAccessSunsetDateInfo")			
			public Map  getAccessSunsetDateInfo(Map<String, Object> paramMap) throws GeneralException
			{
				Map resultMap=new HashMap();
				logger.debug("Enter getRolesSunsetDate");
			
				
				try
				{
					
					SailPointContext context=getContext();
					  //get variables from request
					  String certItemId=(String)paramMap.get("certItemId");
					  String accessId=(String)paramMap.get("accessId");
					  String operator=context.getUserName();
					  String value="";
					  String applicationName="";
					  String attribute="";
					  String nativeIdentity="";
					  String identName="";
					  String relatedApplication="";
					  Date accessGrantedOn=null;
					  
					  if(null != certItemId && !certItemId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem && null != certItem.getType() && (certItem.getType().equals(CertificationItem.Type.Exception) || certItem.getType().equals(CertificationItem.Type.DataOwner)))
						  {
					        				    			
								   
						  }
						  else  if(null != certItem && null != certItem.getType() && certItem.getType().equals(CertificationItem.Type.Bundle))
						  {
							  identName=certItem.getIdentity();
							  String roleName=certItem.getBundle();
							  Bundle bundObj=null;
							  if(null != accessId)
								  bundObj=context.getObjectById(Bundle.class, accessId);
							  
							  if(null == bundObj)
								  bundObj=context.getObjectByName(Bundle.class, roleName);
							  
			 				      Identity identObj=null;
								  if(null != identName)
									  identObj=context.getObjectByName(Identity.class, identName);
								 
								  logger.debug("roleName: "+roleName);
								  logger.debug("bundObj: "+bundObj);
								  if(null != identObj && null != bundObj && null != identObj.getActiveRoleAssignments(bundObj) && null !=bundObj.getType() && bundObj.getType().equalsIgnoreCase("job"))
									{
									  for(RoleAssignment eachRoleAssignment: identObj.getActiveRoleAssignments(bundObj))
									   {
										  if(null != eachRoleAssignment && null != eachRoleAssignment.getEndDate())
										  {
											  Date endDate=eachRoleAssignment.getEndDate();
											  logger.debug("endDate: "+endDate);
											  Calendar currentDateBefore1Months = Calendar.getInstance();
						    				  currentDateBefore1Months.add(Calendar.DAY_OF_MONTH, -30);
						    				  if(endDate.after(currentDateBefore1Months.getTime()))	
						    				  {
						    					  String endDateDateStr = uidisplaydateFormat.format(endDate);
						    					  long difference_In_Time = endDate.getTime()-currentDate.getTime();
						    				 	  long daysToExpire=TimeUnit.DAYS.convert(difference_In_Time, TimeUnit.MILLISECONDS);
						    				 	 
						    				 	     
											      resultMap.put("endDate", endDateDateStr);
											      resultMap.put("daysToExpire", daysToExpire);
						    				  }
										  }
									   }
									}
						  }
													  
						 
					  }
					  
					if(null == resultMap || resultMap.isEmpty())
						resultMap=new HashMap();
					
						resultMap.put("result", true);
						resultMap.put("certItemId", certItemId);
						
					
				}		
				catch(Exception e)
				{
					logger.warn("Error in getLastLoginInfo ",e);
				}
				
				logger.debug("Exit getLastLoginInfo: "+ resultMap);
				return resultMap;
			}
			
			 public Map getAAApplicationLastLogin(String applicationName, String relatedApplication,Link accountLink,SailPointContext context, Date accessGrantedOn)
			 {
				 logger.debug("Enter getAAApplicationLastLogin");
				 //////////////////////////last login logic for AA applications/////////////////////////////////////
				 Map resultMap=new HashMap();
			     try
		 		{
			    	 Custom customObj=context.getObjectByName(Custom.class, "SWE Certification Exclusion Custom");
				     if(null !=customObj && null !=customObj.get("lastLoginApplications"))
				     {
				    	 Map<String,Map> appsMap=( HashMap<String,Map> )customObj.get("lastLoginApplications");
				    	 if ((null != appsMap && appsMap.containsKey(applicationName) && null != appsMap.get(applicationName)) || (null != appsMap && appsMap.containsKey(relatedApplication) && null != appsMap.get(relatedApplication)))
				    	 {
				    		 
				    		 Map<String,String> attsMap=( HashMap<String,String> )appsMap.get(applicationName);
				    		 if(null == attsMap || attsMap.isEmpty())
				    		 {
				    			 attsMap=( HashMap<String,String> )appsMap.get(relatedApplication);
				    		 }
				    		 
				    		 String lastLoginAtt=(String)attsMap.get("schemaAttribute");
				    		 String dateFormat=(String)attsMap.get("dateFormat");
				    		 logger.debug("lastLoginAtt: "+lastLoginAtt);
				    		 logger.debug("dateFormat: "+dateFormat);
				    		 logger.debug("accountLink.getAttribute(lastLoginAtt): "+accountLink.getAttribute(lastLoginAtt));
				    		 if(null != lastLoginAtt)
				    		 {
				    			 
				    			 
				    			 try
				    			 {
				    				// current date before 6 months
				    					
				    					
				    					Calendar currentDateBefore2Months = Calendar.getInstance();
				    					currentDateBefore2Months.add(Calendar.DAY_OF_MONTH, -60);
				    					Calendar currentDateBefore1Months = Calendar.getInstance();
				    					currentDateBefore1Months.add(Calendar.DAY_OF_MONTH, -30);
				    				 if(null !=accountLink.getAttribute(lastLoginAtt) && !((String)accountLink.getAttribute(lastLoginAtt)).isEmpty())
				    				 {
		    		    				 Date lastLogin=new SimpleDateFormat(dateFormat).parse((String)accountLink.getAttribute(lastLoginAtt));  
		    		    				 String lastLoginDateStr = uidisplaydateFormat.format(lastLogin);
					    		    	 String diffStr=getDatesDiff(currentDate,lastLogin);					    		    	 
					    		    	 resultMap.put("lastlogindiffStr",diffStr);
				    					 resultMap.put("lastLoginDateStr",lastLoginDateStr);
				    					 
				    					// code to check if last login is 01/01/1970 then never login
		    		    				 SimpleDateFormat neverloginformat = new SimpleDateFormat(dateFormat);
		    		    				 neverloginformat.setTimeZone(TimeZone.getTimeZone("UTC"));
		    		    				 String tempDateStr=neverloginformat.format(new Date(0)); //01/01/1970
		    		    				 Date neverLoginDate=new SimpleDateFormat(dateFormat).parse(tempDateStr);  
		    		    				 
		    		    				 if(lastLogin.equals(neverLoginDate)) //if 01/01/1970 then never login
		    		    				 {
		    		    					 if(accessGrantedOn.before(currentDateBefore1Months.getTime()))					    				  
						    					  resultMap.put("staleaccount",true);					    				  
						    				  else
						    					  resultMap.put("recentaccount",true);
		    		    				  }
		    		    				
		    		    				 else
					    				 if(lastLogin.before(currentDateBefore2Months.getTime()))
					    				 {
					    					 logger.debug("lastLoginDateStr is more than 2 months old");
					    					 resultMap.put("lastlogin6monthsbefore",true);					    					 
					    				 }
					    				 else if(lastLogin.before(currentDateBefore1Months.getTime()))
					    				 {
					    					 logger.debug("lastLoginDateStr is more than 1 months old");
					    					 resultMap.put("lastlogin3monthsbefore",true);		
					    				 }
					    				 else					    				 
					    					 resultMap.put("lastloginrecent",true);	
				    				 
				    				 } 
				    				 else if(((String)accountLink.getAttribute(lastLoginAtt)).isEmpty())
				    				 {

				    					 if(accessGrantedOn.before(currentDateBefore1Months.getTime()))					    				  
					    					  resultMap.put("staleaccount",true);					    				  
					    				  else
					    					  resultMap.put("recentaccount",true);
					    				  		
				    				 }
				    					 
				    				  
				    			 }
				    				catch(Exception e)
				    			 {
				    				
				    				logger.error("Error inside getAAApplicationLastLogin  on accountLink: "+accountLink,e);
				    			 }
				    			
				    	            
				    		 }
				    	 }
				     }
		 			
		 		}
		 		catch(Exception e)
		 		{
		 			logger.debug("Error in getAAApplicationLastLogin on accountLink: "+accountLink,e);
		 		}
			    
			     logger.debug("Exit getAAApplicationLastLogin: "+ resultMap);
			     return resultMap;
			     //////////////////////////////////////////////////////////////////////////////////////////////////
			 }
			
			@POST
			  @Path("getLastLoginInfo")			
			public Map  getLastLoginInfo(Map<String, Object> paramMap) throws GeneralException
			{
				Map resultMap=new HashMap();
				logger.debug("Enter getLastLoginInfo");
				//String existingdescFeedback="";
				
				try
				{
					
					SailPointContext context=getContext();
					  //get variables from request
					  String certItemId=(String)paramMap.get("certItemId");
					  String accessId=(String)paramMap.get("accessId");
					  String operator=context.getUserName();
					  String value="";
					  String applicationName="";
					  String attribute="";
					  String nativeIdentity="";
					  String identName="";
					  String relatedApplication="";
					  Date accessGrantedOn=null;
					  
					  if(null != certItemId && !certItemId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem && null != certItem.getType() && (certItem.getType().equals(CertificationItem.Type.Exception) || certItem.getType().equals(CertificationItem.Type.Account) || certItem.getType().equals(CertificationItem.Type.DataOwner)))
						  {
					

							  EntitlementSnapshot snap = certItem.getExceptionEntitlements();
								 if(null != snap)
								 {
									 applicationName=snap.getApplicationName();	
									    if(!certItem.getType().equals(CertificationItem.Type.Account))
										  {  
										    attribute = (String)snap.getAttributeName();
										    value = (String)snap.getAttributeValue();
										  }
								    nativeIdentity=snap.getNativeIdentity();
								  }
								 
								 identName=certItem.getIdentity();
								 accessGrantedOn=getAccessGratedOn(applicationName, attribute, context, value, nativeIdentity, identName);
						 

						  
								 ManagedAttribute managedAttribute=context.getObjectById(ManagedAttribute.class,accessId);
								  if(null != managedAttribute)
								  {
									
								    relatedApplication = (String)managedAttribute.getAttribute(SWE_ATTRIBUTE_RELATED_APPLICATION);
								    

								    
								    Timestamp lastLogin=null;
									List lastLoginList = getLastLoginTimeStamp(applicationName, relatedApplication,value,context,managedAttribute,identName,nativeIdentity);
									if(lastLoginList.size() > 0 )
					    			{
					    				lastLogin = (Timestamp) lastLoginList.get(0);
					    			

								 // current date before 6 months
			    					
			    					
			    					Calendar currentDateBefore2Months = Calendar.getInstance();
			    					currentDateBefore2Months.add(Calendar.DAY_OF_MONTH, -60);
			    					Calendar currentDateBefore1Months = Calendar.getInstance();
			    					currentDateBefore1Months.add(Calendar.DAY_OF_MONTH, -30);
			    					
					    			  if(null != lastLogin)
					    			  {
					    				
					    				 String lastLoginDateStr = uidisplaydateFormat.format(lastLogin);
					    		    	 String diffStr=getDatesDiff(currentDate,lastLogin);					    		    	 
					    		    	 resultMap.put("lastlogindiffStr",diffStr);
				    					 resultMap.put("lastLoginDateStr",lastLoginDateStr);
					    				 if(lastLogin.before(currentDateBefore2Months.getTime()))
					    				 {
					    					 logger.debug("lastLoginDateStr is more than 2 months old");
					    					 resultMap.put("lastlogin6monthsbefore",true);					    					 
					    				 }
					    				 else if(lastLogin.before(currentDateBefore1Months.getTime()))
					    				 {
					    					 logger.debug("lastLoginDateStr is more than 1 months old");
					    					 resultMap.put("lastlogin3monthsbefore",true);		
					    				 }
					    				 else					    				 
					    					 resultMap.put("lastloginrecent",true);						    				 
					    			  }	
					    			  else
					    			  {
					    				  if(accessGrantedOn.before(currentDateBefore1Months.getTime()))					    				  
					    					  resultMap.put("staleaccount",true);					    				  
					    				  else
					    					  resultMap.put("recentaccount",true);

					    			  }
					    			}  
								  }	  
					    			  ////////////////////////////////////////////////////////////////////////
					    			  if(null == resultMap || resultMap.isEmpty())
										{
											    IdentityService IS=new IdentityService(context);
											    Identity identObj=null;
												  if(null != identName)
													  identObj=context.getObjectByName(Identity.class, identName);
												  Link accountLink=null;
												  
										  if(null != identObj)
											{
											      accountLink=IS.getLink(identObj, context.getObjectByName(Application.class, applicationName), null,nativeIdentity);
											      if(null != accountLink)
											      resultMap=getAAApplicationLastLogin(applicationName,relatedApplication,accountLink,context, accessGrantedOn);
											}
									
									  }
					    			  ////////////////////////////////////////////////////////////////////////
					    			
								   
						  }		 
							
						  
						 
					  }
					  
					if(null == resultMap || resultMap.isEmpty())
						resultMap=new HashMap();
					
						resultMap.put("result", true);
						resultMap.put("certItemId", certItemId);
						
					
				}		
				catch(Exception e)
				{
					logger.warn("Error in getLastLoginInfo ",e);
				}
				
				logger.debug("Exit getLastLoginInfo: "+ resultMap);
				return resultMap;
			}
			
			@POST
			  @Path("getGrantedinPriorJob")			
			public Map  getGrantedinPriorJob(Map<String, Object> paramMap) throws GeneralException
			{
				Map resultMap=new HashMap();
				logger.debug("Enter getGrantedinPriorJob");
				//String existingdescFeedback="";
				
				try
				{
					
					SailPointContext context=getContext();
					  //get variables from request
					  String certItemId=(String)paramMap.get("certItemId");
					  String accessId=(String)paramMap.get("accessId");
					  String operator=context.getUserName();
					  String value="";
					  String applicationName="";
					  String attribute="";
					  String nativeIdentity="";
					  String identName="";
					  Date accessGrantedOn=null;
					  
					  if(null != certItemId && !certItemId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem && null != certItem.getType() && (certItem.getType().equals(CertificationItem.Type.Exception) || certItem.getType().equals(CertificationItem.Type.Account) || certItem.getType().equals(CertificationItem.Type.DataOwner)))
						  {
							  identName=certItem.getIdentity();

							  EntitlementSnapshot snap = certItem.getExceptionEntitlements();
								 if(null != snap)
								 {
									 applicationName=snap.getApplicationName();	
									    if(!certItem.getType().equals(CertificationItem.Type.Account))
										  {  
										    attribute = (String)snap.getAttributeName();
										    value = (String)snap.getAttributeValue();
										  }
								    nativeIdentity=snap.getNativeIdentity();
								 } 
								 
								 accessGrantedOn=getAccessGratedOn(applicationName, attribute, context, value, nativeIdentity, identName);
						  }
						  else  if(null != certItem && null != certItem.getType() && certItem.getType().equals(CertificationItem.Type.Bundle))
						  {
							  value=certItem.getBundle();
							  identName=certItem.getIdentity();
							  accessGrantedOn=getAccessGratedOn(null, null, context, value, null, identName);
						  }
						  
								    
								    Identity identObj=null;
									  if(null != identName)
										  identObj=context.getObjectByName(Identity.class, identName);
									  

								    
									String accessGrantedOnStr = uidisplaydateFormat.format(accessGrantedOn);
								    String moverDatedispStr= checkMoverFlag(accessGrantedOn, context, identObj, identName);
								    if(null != moverDatedispStr && !moverDatedispStr.isEmpty())
								    	{
								    	 resultMap.put("moverflag", true);
								    	 resultMap.put("moverDatedispStr", moverDatedispStr);
								    	 resultMap.put("accessGrantedOnStr", accessGrantedOnStr);								    	
								    	}
								
								  
						 
						 
					  }
					  
					
					  if(null == resultMap || resultMap.isEmpty())
							resultMap=new HashMap();
						
							resultMap.put("result", true);
							resultMap.put("certItemId", certItemId);
					
				}		
				catch(Exception e)
				{
					logger.debug("Error in getGrantedinPriorJob ",e);
				}
				
				logger.debug("Exit getGrantedinPriorJob: "+ resultMap);
				return resultMap;
			}
			
			@POST
			  @Path("getSharedMailBoxInfo")			
			public Map  getSharedMailBoxInfo(Map<String, Object> paramMap) throws GeneralException
			{
				Map resultMap=new HashMap();
				logger.debug("Enter getSharedMailBoxInfo");
				//String existingdescFeedback="";
				
				try
				{
					
					SailPointContext context=getContext();
					  //get variables from request
					  String certItemId=(String)paramMap.get("certItemId");
					  String accessId=(String)paramMap.get("accessId");
					  String operator=context.getUserName();
					  String applicationName="";
					  String nativeIdentity="";
					  
					  if(null != certItemId && !certItemId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem && null != certItem.getType() && (certItem.getType().equals(CertificationItem.Type.Exception) || certItem.getType().equals(CertificationItem.Type.Account) || certItem.getType().equals(CertificationItem.Type.DataOwner)))
						  {


							  EntitlementSnapshot snap = certItem.getExceptionEntitlements();
								 if(null != snap)
								 {
									nativeIdentity=snap.getNativeIdentity();
									applicationName=snap.getApplicationName();	
								    String identName=certItem.getIdentity();
								    Identity identObj=null;
									  if(null != identName)
										  identObj=context.getObjectByName(Identity.class, identName);
									  
	
								    IdentityService IS=new IdentityService(context);
								    Link accountLink=IS.getLink(identObj, context.getObjectByName(Application.class, applicationName), null,nativeIdentity);
								      if(null != accountLink)
								      {
								       String DN=(String)accountLink.getAttribute("distinguishedName");
		           		        	  String mail=(String)accountLink.getAttribute("mail");

			           		        	  if(null != DN && DN.toLowerCase().contains("ou=resource mailboxes") && null != mail)
			           		        	  {		           		        		
			           		        		 resultMap.put("sharedmailbox", true);
									    	 resultMap.put("mail", mail);	
									    	 
									    	 ManagedAttribute managedAttribute=context.getObjectById(ManagedAttribute.class,accessId);
											  if(null != managedAttribute)
											  {
												 String description=managedAttribute.getDescription("en_US");
												 description=description.replaceAll("[\\t\\n\\r]+"," ");       
										         description=description.replaceAll("\\<.*?\\>", "");
												 if(description == null || description.equals("")) 
										            {
										         	   description = "Account-Only";
										            }
									    	     resultMap.put("descupdate", "Mail Box Name: "+mail+", "+description);
											  }
			           		        	  }
								      }
								 } 
								  
						  }
						 
					  }
					  
					  if(null == resultMap || resultMap.isEmpty())
							resultMap=new HashMap();
						
							resultMap.put("result", true);
							resultMap.put("certItemId", certItemId);
					
				}		
				catch(Exception e)
				{
					logger.debug("Error in getSharedMailBoxInfo ",e);
				}
				
				logger.debug("Exit getSharedMailBoxInfo: "+ resultMap);
				return resultMap;
			}
			
			
			@POST
			  @Path("getAccessStatusOtherInfo")			
			public Map  getAccessStatusOtherInfo(Map<String, Object> paramMap) throws GeneralException
			{
				Map resultMap=new HashMap();
				logger.debug("Enter getAccessStatusOtherInfo");
				//String existingdescFeedback="";
				
				try
				{
					
					SailPointContext context=getContext();
					  //get variables from request
					  String certItemId=(String)paramMap.get("certItemId");
					  String operator=context.getUserName();
					  String value="";
					  String applicationName="";
					  String attribute="";
					  String nativeIdentity="";
					  String identName="";
					  Date accessGrantedOn=null;
					  if(null != certItemId && !certItemId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem && null != certItem.getType() && (certItem.getType().equals(CertificationItem.Type.Exception) || certItem.getType().equals(CertificationItem.Type.Account) || certItem.getType().equals(CertificationItem.Type.DataOwner)))
						  {
							  identName=certItem.getIdentity();

							  EntitlementSnapshot snap = certItem.getExceptionEntitlements();
								 if(null != snap)
								 {
									applicationName=snap.getApplicationName();	
								    nativeIdentity=snap.getNativeIdentity();
								    if(!certItem.getType().equals(CertificationItem.Type.Account))
									  {  
									    attribute = (String)snap.getAttributeName();
									    value = (String)snap.getAttributeValue();
									  }
								    
								 } 
								 
								 accessGrantedOn=getAccessGratedOn(applicationName, attribute, context, value, nativeIdentity, identName);
								  
						  }
						  else  if(null != certItem && null != certItem.getType() && certItem.getType().equals(CertificationItem.Type.Bundle))
						  {
							  value=certItem.getBundle();
							  identName=certItem.getIdentity();
							  accessGrantedOn=getAccessGratedOn(null, null, context, value, null, identName);
						  }
						  
						  if(null != accessGrantedOn)
			               {
			            	 try
			               	{ 
			            	   String diffStr=getDatesDiff(currentDate,accessGrantedOn);
			            	   String accessGrantedOnStr = uidisplaydateFormat.format(accessGrantedOn);  
			            	   resultMap.put("foundgranted", true);
			            	   resultMap.put("granteddiffStr", diffStr);
			            	   resultMap.put("accessGrantedOnStr", accessGrantedOnStr);
			                 }
			            	catch(Exception e) {logger.debug("Error in converting access granted date ",e);}
			               }
						  
						    Identity identObj=null;
							  if(null != identName)
								  identObj=context.getObjectByName(Identity.class, identName);
							  

						    IdentityService IS=new IdentityService(context);
						    Link accountLink=IS.getLink(identObj, context.getObjectByName(Application.class, applicationName), null,nativeIdentity);
						      if(null != accountLink)
						      {
						    	  if(null == accessGrantedOn)
						    	  {
						    		    Date accountCreatedOn=accountLink.getCreated();
				            			String accountCreatedOnStr = uidisplaydateFormat.format(accountCreatedOn);
				            			String diffStr=getDatesDiff(currentDate,accountCreatedOn);
				            			   resultMap.put("foundgranted", true);
						            	   resultMap.put("granteddiffStr", diffStr);
						            	   resultMap.put("accessGrantedOnStr", accountCreatedOnStr);	
						    	  }
						    	  if(accountLink.isDisabled())			            		     
						    		  resultMap.put("disabled", true);			            		     
			            		  else if(accountLink.isLocked())			            		     
			            		      resultMap.put("locked", true);
			            		     
						      }
						 
					  }
					  
					  if(null == resultMap || resultMap.isEmpty())
							resultMap=new HashMap();
						
							resultMap.put("result", true);
							resultMap.put("certItemId", certItemId);
					
				}		
				catch(Exception e)
				{
					logger.debug("Error in getAccessStatusOtherInfo ",e);
				}
				
				logger.debug("Exit getAccessStatusOtherInfo: "+ resultMap);
				return resultMap;
			}
			
			
			
			
			@POST
			  @Path("getExistingAccessDescFeedbackRating")			
			public String  getExistingAccessDescFeedbackRating(Map<String, Object> paramMap) throws GeneralException
			{
				String descFeedback="";
				logger.debug("Enter getExistingAccessDescFeedbackRating");
				//String existingdescFeedback="";
				
				try
				{
					
					SailPointContext context=getContext();
					  //get variables from request
					  String certItemId=(String)paramMap.get("certItemId");
					  String accessId=(String)paramMap.get("accessId");
					  String operator=context.getUserName();
					  String accessType="";
					  String value="";
					  String applicationName="";
					  String attribute="";
					  
					  if(null != certItemId && !certItemId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem && null != certItem.getType() && certItem.getType().equals(CertificationItem.Type.Exception))
						  {
							  accessType="Entitlement";

							  EntitlementSnapshot snap = certItem.getExceptionEntitlements();
								 if(null != snap)
								 {
									 applicationName=snap.getApplicationName();	
								    attribute = (String)snap.getAttributeName();
								    value = (String)snap.getAttributeValue();
								  }
						  }
						  else  if(null != certItem &&  null != certItem.getType() && certItem.getType().equals(CertificationItem.Type.Bundle))
						  {
							  value=certItem.getBundle();
							  accessType="Role";
						  }

					  }
					  
					String descRatingSQL="";
					
					// existing feedback of requester
					if(accessType.equalsIgnoreCase("Role"))
						descRatingSQL="sql: select rating  from (select tab1.rating from iiqcap1.CERT_ACCESS_DESC_FEEDBACK tab1 where tab1.Access_value='"+value+"' and tab1.CERTIFIER_EID='"+operator+"'  order by LAST_UPDATED_DTTM desc) where rownum < 2";
		               else
		               descRatingSQL="sql: select rating  from (select tab1.rating from iiqcap1.CERT_ACCESS_DESC_FEEDBACK tab1 where tab1.application_name='"+applicationName+"' and tab1.Access_name='"+attribute+"' and tab1.Access_value='"+value+"' and tab1.CERTIFIER_EID='"+operator+"'  order by LAST_UPDATED_DTTM desc) where rownum < 2";
					

					       List resultList=new ArrayList();
					
					       try
							{
		  	            	QueryOptions qo = new QueryOptions();
			            	qo.setQuery(descRatingSQL);
			            	logger.debug("Executing query: getExistingAccessDescFeedbackRating: descRatingSQL "+ descRatingSQL);
			    			Iterator moverIterator = context.search(descRatingSQL,null,qo);
			    			resultList = sailpoint.tools.Util.iteratorToList(moverIterator);
			    			sailpoint.tools.Util.flushIterator(moverIterator);
							}		
							catch(Exception e)
							{
								//logger.error("Error getting current getExistingAccessDescFeedbackRating ",e);
							}
			    					if(resultList.size() > 0 )
					    			{
			    						int rating=((BigDecimal) resultList.get(0)).intValue();	    						
			    							    						
			    						if(rating == 0)
			    							descFeedback="No";
			    						else	    					    
			    							descFeedback="Yes";		    						
					    			}					
						
					
				}		
				catch(Exception e)
				{
					logger.debug("Error in getExistingAccessDescFeedbackRating ",e);
				}
				
				logger.debug("Exit getAccessDescFeedbackYesNo descFeedback: "+ descFeedback);
				return descFeedback;
			}
			
			@POST
			  @Path("getEntGrantedByRoleinfo")			
			public Map  getEntGrantedByRoleinfo(Map<String, Object> paramMap) throws GeneralException
			{
				Map resultMap=new HashMap();
				logger.debug("Enter getEntGrantedByRoleinfo");
				//String existingdescFeedback="";
				
				try
				{
					
					SailPointContext context=getContext();
					  //get variables from request
					  String certItemId=(String)paramMap.get("certItemId");
					  String operator=context.getUserName();
					  String entValue="";
					  String applicationName="";
					  String attribute="";
					  String nativeIdentity="";
					  String identName="";
					  Date accessGrantedOn=null;
					  if(null != certItemId && !certItemId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem && null != certItem.getType() && (certItem.getType().equals(CertificationItem.Type.Exception) || certItem.getType().equals(CertificationItem.Type.DataOwner)))
						  {
							  identName=certItem.getIdentity();

							  EntitlementSnapshot snap = certItem.getExceptionEntitlements();
								 if(null != snap)
								 {
									applicationName=snap.getApplicationName();	
								    nativeIdentity=snap.getNativeIdentity();
									attribute = (String)snap.getAttributeName();
									entValue = (String)snap.getAttributeValue();
									String roleName=getAccessGrantedByRole(context, identName,entValue, attribute, nativeIdentity, applicationName);
									logger.debug("roleName: "+roleName);
									if(null != roleName && !roleName.equals(""))
							    	   {
							    	    Bundle roleObj=context.getObjectByName(Bundle.class,roleName);	
							    	   
							    	       String roleDispName=""; 
								    	   if(null != roleObj)	
								    	   {
								    		   roleDispName=roleObj.getDisplayName();
								    	   
									    	   if(null != roleDispName && !roleDispName.isEmpty() )		    	   		    		   
									    		   roleName=roleDispName;
									    	   else
									    		   roleName=roleObj.getName();
								    	   		    		   
									    	    resultMap.put("grantedByRoleName", roleName);
									    	    
								    	   }
							    	   
							    	   }
									
								    
								 } 
								
								  
						  }						  
						 
					  }
					  
					  if(null == resultMap || resultMap.isEmpty())
							resultMap=new HashMap();
						
							resultMap.put("result", true);
							resultMap.put("certItemId", certItemId);
					
				}		
				catch(Exception e)
				{
					logger.debug("Error in getEntGrantedByRoleinfo ",e);
				}
				
				logger.debug("Exit getEntGrantedByRoleinfo: "+ resultMap);
				return resultMap;
			}
			
			
			
			public String getAccessGrantedByRole(SailPointContext context, String identName,String entValue, String attribute, String nativeIdentity, String applicationName)
			{
				logger.debug("Enter: getAccessGrantedByRole identName: "+ identName + " entValue: "+entValue+ " applicationName: "+applicationName);
				Bundle roleObj=null;
		 	   String targetRoleName="";
		 	   List<Profile> eachRoleProfiles=new ArrayList<Profile>();
		 	   List eachAppList=new ArrayList();
		 	   Application eachAppObj=null;
		 	   LeafFilter leafFilter=null; 	
		 	  String eachNativeIdentity="";
		 	  String eachAppName="";
		 	   String roleName="";
		 	  List<Bundle> eachroleList=new ArrayList<Bundle>();
		 	   try
		 	   {
		 		  Identity identObj = context.getObjectByName(Identity.class, identName);	
		 	   if(null != identObj && null !=  identObj.getActiveRoleAssignments())
		 	   {
		 		   for(RoleAssignment eachRoleAssignment: identObj.getActiveRoleAssignments())
					   {
		 			   if(null != eachRoleAssignment && null != eachRoleAssignment.getTargets())
							 {
		 				   for(RoleTarget eachRoleTarget: eachRoleAssignment.getTargets())
								{
		 					      eachNativeIdentity=eachRoleTarget.getNativeIdentity();
		 					      eachAppName=eachRoleTarget.getApplicationName();
		 					    logger.debug("eachAppName: "+ eachAppName);
		 					   
		 					   if(null != eachNativeIdentity && null != eachAppName && 
		 							  eachNativeIdentity.toLowerCase().equalsIgnoreCase(nativeIdentity.toLowerCase()) && eachAppName.toLowerCase().equals(applicationName.toLowerCase()))
		 					   {
		 						  eachroleList=new ArrayList<Bundle>();
		 						   targetRoleName= eachRoleTarget.getRoleName();
		 						  
		 						  if(null == targetRoleName)
		 						  {
		 							 targetRoleName=eachRoleAssignment.getRoleName();
		 							roleObj=context.getObjectByName(Bundle.class,targetRoleName);	
		 							eachroleList=getRoleHeirarchy(roleObj);
		 						  }
		 						  else
		 						  {
		 							 roleObj=context.getObjectByName(Bundle.class,targetRoleName);	
		 							 eachroleList.add(roleObj);
		 						  }
		 						  logger.debug("targetRoleName: "+ targetRoleName);
		 						   if(null != eachroleList && !eachroleList.isEmpty())
		 						   for(Bundle eachRoleObj: eachroleList)
		 						   {
		 						     
		 						      eachAppList=new ArrayList();
		 						      eachAppObj=context.getObjectByName(Application.class,applicationName);
										  eachAppList.add(eachAppObj);							  
										   eachRoleProfiles=eachRoleObj.getProfilesForApplications(eachAppList);
										   logger.debug("eachRoleProfiles: "+ eachRoleProfiles);
										   if(null != eachRoleProfiles && !eachRoleProfiles.isEmpty())
										   {
											   for(Profile eachProfile : eachRoleProfiles)
				                               {
												   List listFilter = eachProfile.getConstraints();
												   int filterSize = listFilter.size();
												   for(int iCount=0;iCount<filterSize;iCount++){
														if(listFilter.get(iCount) instanceof LeafFilter){
														  leafFilter = (LeafFilter)listFilter.get(iCount);
														  Object eachValue = leafFilter.getValue();
														  String eachattrName=leafFilter.getProperty();
														  logger.debug("eachValue: "+ eachValue);
														  logger.debug("eachattrName: "+ eachattrName);
														  if(null != eachattrName && eachattrName.equalsIgnoreCase(attribute))
														    {
															 
															 if(eachValue instanceof ArrayList || eachValue instanceof List){
																 
																 List entList=(ArrayList)eachValue;
																 if(entList.contains(entValue))
																	 return eachRoleAssignment.getRoleName();
															  }
															 if(eachValue instanceof String){
															   {
																  String eachEntValue=(String)eachValue;
																  if(entValue.equalsIgnoreCase(eachEntValue))
																	  return eachRoleAssignment.getRoleName();
															   }
															 }
															   												
														 }
													  } 
				                               }
										   }
			 						   }
			 						   
		 					      }
		 						 
								}
							 }
					   }
		 	        }
			    }
		 	   }
		 	   catch (Exception e)
		 	   {
		 		  logger.warn("Error in getAccessGrantedByRole "+ e);
		 	   }
		 	  logger.debug("Exit: getAccessGrantedByRole: "+ roleName);
		 	   return roleName;
		  }
			
			
			public String getAccessGrantedByRoleIdentityEnt(SailPointContext context, String identName,String value, String attribute, String nativeIdentity, String applicationName)
			{
				 List<Filter> filterList = new ArrayList();
				 String rolesStr="";
				 Filter filter = null;
			        filterList.add(Filter.eq("application.name", applicationName));
			        filterList.add(Filter.eq("grantedByRole", true));
			        filterList.add(Filter.eq("identity.name", identName));
			        filterList.add(Filter.eq("nativeIdentity", nativeIdentity));
			        
			        
			        if(null != attribute)
			          filterList.add(Filter.eq("name", attribute));
			
			        if(null != value)
			          filterList.add(Filter.eq("value", value));
			
			        filter = Filter.and(filterList);
			
			        IdentityEntitlement identEnt=null;
					try {
						identEnt = context.getUniqueObject(IdentityEntitlement.class, filter);
					} catch (GeneralException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			        if(null == identEnt)
			        {	
			        	logger.debug("We got a corner case. We couldn't find the managed attribute for this item. returning a empty string as related app.");
			        	//return "";
			        }
			        
			       logger.debug("identEnt: " + identEnt);
			       
			
			        if(identEnt != null)
			        {
			          rolesStr = (String)identEnt.getAttribute("sourceAssignableRoles");
			          
			        /*  if(null!= rolesStr && !rolesStr.equals(""))
			        	  rolesStr="Assigned Role: "+rolesStr;
			          else			          
			          {
			        	  rolesStr = (String)identEnt.getAttribute("sourceDetectedRoles");
			        	  if(null!= rolesStr && !rolesStr.equals(""))
			              rolesStr= (String)"Detected Role: "+identEnt.getAttribute("sourceDetectedRoles");
			          }
			          */
			        }
			        
			        return rolesStr;
			}
			
			
			@POST
			  @Path("entOwnerCertinvokeRoleWizardWF")			
			public String  entOwnerCertinvokeRoleWizardWF(Map<String, Object> paramMap) throws GeneralException
			{
				String roleWizardURL="";
				logger.debug("Enter entOwnerCertinvokeRoleWizardWF");
				//String existingdescFeedback="";
				
				try
				{
					
					SailPointContext context=getContext();
					  //get variables from request
					  String certItemId=(String)paramMap.get("certItemId");
					  String roleId=(String)paramMap.get("grantedByRoleId");
					  String operator=context.getUserName();
					  String entValue="";
					  String applicationName="";
					  String attribute="";
					  String nativeIdentity="";
					  String identName="";
					  Date accessGrantedOn=null;
					  if(null != certItemId && !certItemId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem && null != certItem.getType() && (certItem.getType().equals(CertificationItem.Type.Exception) || certItem.getType().equals(CertificationItem.Type.DataOwner)))
						  {
							  identName=certItem.getIdentity();

							  EntitlementSnapshot snap = certItem.getExceptionEntitlements();
								 if(null != snap)
								 {
									applicationName=snap.getApplicationName();	
								    nativeIdentity=snap.getNativeIdentity();
									attribute = (String)snap.getAttributeName();
									entValue = (String)snap.getAttributeValue();
									logger.debug("roleId: "+roleId);
									
									WorkflowLaunch wflaunch = new WorkflowLaunch();
									Workflow wf = (Workflow) context.getObjectByName(Workflow.class,"SWE Role Wizard Modify Workflow");
									if(wf!=null)
									{
										logger.debug("launching role wizard workflow.. ");
										wflaunch.setWorkflowName(wf.getName());
										wflaunch.setWorkflowRef(wf.getName());
										Map wfVariables= new HashMap();
								        
								        wfVariables.put("operation","modify");
								        wfVariables.put("modifyRole",roleId);
								        List removeEntitlements= new ArrayList();
								        removeEntitlements.add(entValue+":::"+applicationName+":::"+attribute);
								        wfVariables.put("removeEntitlements",removeEntitlements);								        
								        wfVariables.put("businessJustification","Entitlement Owner Certification Entitlement Remove Request");		
								        wfVariables.put("requester",operator);
								      //Create Workflower and launch workflow from WorkflowLaunch
										Workflower workflower = new Workflower(context);
										WorkflowLaunch launch = workflower.launch(wflaunch);
										TaskResult taskResult=launch.getTaskResult();
										
										if(taskResult!=null && taskResult.getAttributes()!=null )
								          {

								            WorkflowSummary wrkflsummary=(WorkflowSummary)taskResult.getAttribute("workflowSummary");

								            if (wrkflsummary!=null &&  wrkflsummary.getStep().equalsIgnoreCase("Modify Role Form")  && wrkflsummary.getInteractions()!=null && wrkflsummary.getInteractions().size() > 0 &&  wrkflsummary.getInteractions().get(0) !=null && wrkflsummary.getInteractions().get(0).getWorkItemId()!=null)
								            {

								              if (wrkflsummary.getInteractions().get(0).getWorkItemType() != null   && wrkflsummary.getInteractions().get(0).getWorkItemType().getMessageKey() != null && wrkflsummary.getInteractions().get(0).getWorkItemType().getMessageKey().equalsIgnoreCase("Form"));
								              {

								                String workitemid=(String)wrkflsummary.getInteractions().get(0).getWorkItemId();
								                logger.debug("workitemid: "+workitemid);
								                Custom sweCertsCustomObj=context.getObjectByName(Custom.class, "SWE Access Request Modal Custom");			 
								   			    String allaccessURL=(String)sweCertsCustomObj.get("AllAccessURL");
								   			    logger.debug("allaccessURL: "+allaccessURL);
								                roleWizardURL=allaccessURL+"workitem/commonWorkItem.jsf#/commonWorkItem/" + workitemid;
								                

								              } // if  wrkflsummary

								            }// if wrkflsummary
									}
								    
								 } 
								
							 }	  
						  }						  
						 
					  }

					
				}		
				catch(Exception e)
				{
					logger.debug("Error in entOwnerCertinvokeRoleWizardWF ",e);
				}
				roleWizardURL="https://allaccess-ci.clouddqt.capitalone.com/identityiq/workitem/commonWorkItem.jsf#/commonWorkItem/8a3b07727705a25d0177066cd19500bf";
				logger.debug("Exit entOwnerCertinvokeRoleWizardWF: "+ roleWizardURL);
				return roleWizardURL;
			}
			
			
			@POST
			  @Path("getEntOwnerAccessDescFeedbackResults")
			  public List getEntOwnerAccessDescFeedbackResults(Map<String, Object> paramMap) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				logger.debug("Enter getEntOwnerAccessDescFeedbackResults: ");		
			    List finalResultList=new ArrayList();
			    Map eachFeedbackMap=new HashMap();
			    String certId=(String)paramMap.get("certId");
				  
				  logger.debug("certId: "+ certId);
				  if(null != certId && !certId.equals(""))
				  {

				  try
				  {
					  SailPointContext context=getContext();

					  String operator=context.getUserName();
					  
					// existing feedback of requester
					  String descRatingSQL="sql: select certdata.target_id, feedback.application_name, feedback.Access_value,feedback.Access_name,feedback.rating,feedback.plain_feedback,count(feedback.rating) from\r\n" + 
					  		"(select feedback.* from iiqcap1.CERT_ACCESS_DESC_FEEDBACK feedback,\r\n" + 
					  		"(select max(LAST_UPDATED_DTTM) as LAST_UPDATED_DTTM,application_name,Access_name,Access_value,access_id,CERTIFIER_EID  from iiqcap1.CERT_ACCESS_DESC_FEEDBACK\r\n" + 
					  		"group by application_name,Access_name,Access_value,access_id,CERTIFIER_EID) latestfeedback\r\n" + 
					  		"where \r\n" + 
					  		"(feedback.rating=1 or (feedback.rating=0 and plain_feedback is not null and length(plain_feedback) > 1)) and \r\n" + 
					  		"feedback.CERTIFIER_EID = latestfeedback.CERTIFIER_EID and latestfeedback.application_name=feedback.application_name and latestfeedback.Access_name=feedback.Access_name and latestfeedback.Access_value=feedback.Access_value and feedback.LAST_UPDATED_DTTM=latestfeedback.LAST_UPDATED_DTTM)\r\n" + 
					  		"feedback,\r\n" + 
					  		"(select entity.target_id,entity.application,entity.native_identity,entity.target_name from identityiq.spt_certification cert,identityiq.SPT_CERTIFICATION_ENTITY entity where cert.id = entity.certification_id and cert.id='"+certId+"') certdata\r\n" + 
					  		"where feedback.access_id=certdata.target_id or (feedback.application_name=certdata.application and feedback.Access_value=certdata.native_identity and feedback.Access_name=certdata.target_name)  \r\n" + 
					  		"group by feedback.application_name, feedback.Access_value,feedback.Access_name,feedback.rating,feedback.plain_feedback,certdata.target_id\r\n" + 
					  		"order by rating,count(feedback.plain_feedback) desc";
						

						       List resultList=new ArrayList();
						        
						
						       try
								{
			  	            	QueryOptions qo = new QueryOptions();
				            	qo.setQuery(descRatingSQL);
				            	logger.debug("Executing query: getEntOwnerAccessDescFeedbackResults: descRatingSQL "+ descRatingSQL);
				    			Iterator iterator = context.search(descRatingSQL,null,qo);
				    			//resultList = sailpoint.tools.Util.iteratorToList(iterator);
				    			
				    			
				    			if(null != iterator)
				    			{
				    				 Map tempRatingConsolidationMap=new HashMap();
				    				 TreeMap<String,Object> tempMap=new TreeMap<String,Object>();
				    				 List tempList=new ArrayList();
				    				 List tempTempList=new ArrayList();
				    				 List<Map> tempFinalList=new ArrayList();
				    				 
				    				 while (iterator.hasNext()) {

					    					
					    					 Object[] object = (Object[]) iterator.next();				    					 
					    					    String accessId=(String) object[0];
					    					    if(null != tempRatingConsolidationMap && tempRatingConsolidationMap.containsKey(accessId))
					    					    {
					    					    	Integer rating=((BigDecimal) object[4]).intValue(); 
					    					    	String feedbackText=(String) object[5];
					    					    	Integer feedbackTextCount=((BigDecimal) object[6]).intValue(); 
					    							

					    					    	tempMap=(TreeMap)tempRatingConsolidationMap.get(accessId);
					    					    	if(rating == 1)
					    					    		tempMap.put("Helpful",feedbackTextCount);
										    			  else
										    			tempMap.put(feedbackText,feedbackTextCount);				    					    	
					    					    	
								    				tempRatingConsolidationMap.put(accessId, tempMap);
					    					    }
					    					    else
					    					    {
								    				String appName=(String) object[1];
								    				String entVal=(String) object[2];
								    				String attribute=(String) object[3];
								    				Integer rating=((BigDecimal) object[4]).intValue(); 
								    				String feedbackText=(String) object[5];
								    				Integer feedbackTextCount=((BigDecimal) object[6]).intValue(); 
								    				String displayName="";
								    				String desc="";
								    				eachFeedbackMap=new HashMap();
								    				eachFeedbackMap.put("accessId", accessId);
								    				
								    				
								    				ManagedAttribute managedAttribute=context.getObjectById(ManagedAttribute.class,accessId);
								    				if(null != managedAttribute)
								    				{
								    					displayName=managedAttribute.getDisplayName();
								    					desc=managedAttribute.getDescription("en_US");
								    					if(null == displayName || displayName.isEmpty())
								    						displayName=entVal;
								    					if(null == desc || desc.isEmpty())
								    						desc="Not Available";	
								    					
								    					desc=desc.replaceAll("[\\t\\n\\r]+"," ");       
								    					desc=desc.replaceAll("\\<.*?\\>", "");
								    						
								    					String relatedApplication = (String)managedAttribute.getAttribute(SWE_ATTRIBUTE_RELATED_APPLICATION);
											        	 if(null != relatedApplication && !relatedApplication.isEmpty())
											        	 {
											        		 appName=relatedApplication;										        
											        	 }
								    				}
								    				
								    				
								    				eachFeedbackMap.put("appName", appName);
								    				eachFeedbackMap.put("displayName", displayName);
								    				eachFeedbackMap.put("description", desc);
								    				eachFeedbackMap.put("attribute", attribute);

								    				tempMap=new TreeMap();
					    					    	if(rating == 1)
					    					    		tempMap.put("Helpful",feedbackTextCount);
										    			  else
										    			tempMap.put(feedbackText,feedbackTextCount);				    					    	
					    					    	
								    				tempRatingConsolidationMap.put(accessId, tempMap);						    				
								    				tempFinalList.add(eachFeedbackMap);
					    					    }

							    				
					    				  } 
				    				sailpoint.tools.Util.flushIterator(iterator);
				    				logger.debug("tempRatingConsolidationMap: "+tempRatingConsolidationMap);
				    				logger.debug("tempFinalList: "+tempFinalList);
				    				for(Map eachMap: tempFinalList)
		    					    {
		    					    	 String accessId=(String) eachMap.get("accessId");
		    					    	 tempMap=(TreeMap)tempRatingConsolidationMap.get(accessId);
		    					    	 tempList=new ArrayList();
						    			 tempTempList=new ArrayList();
						    			 tempTempList.add("Reason");
						    			 tempTempList.add("Rating");
						    			 tempList.add(tempTempList);
						    			 
						    			 if(null != tempMap)
						    			 {
						    				 if(!tempMap.keySet().contains("Helpful"))
						    					 tempMap.put("Helpful",0);
						    				 if(!tempMap.keySet().contains("Too much technical and/or business jargon"))
						    					 tempMap.put("Too much technical and/or business jargon",0);
						    				 if(!tempMap.keySet().contains("Needs more info about how it is used"))
						    					 tempMap.put("Needs more info about how it is used",0);
						    				 if(!tempMap.keySet().contains("Both"))
						    					 tempMap.put("Both",0);
						    				 
						    					 
			    					    	 for(String eachString: tempMap.keySet())
			    					    	 {
			    					    		 tempTempList=new ArrayList();
			    					    		 tempTempList.add(eachString);
			    					    		 tempTempList.add(tempMap.get(eachString));
			    					    		 tempList.add(tempTempList);
			    					    	 }
						    			 }
		    					    	 
		    					    	 eachMap.put("rating",tempList);
		    					    	finalResultList.add(eachMap);
		    					    }
				    				
				    				
				    			}
				    			
								}	
								catch(Exception e)
								{
									logger.warn("Error in current getEntOwnerAccessDescFeedbackResults ",e);
								}

				  }
				  catch (Exception e)
				  {
					// e.printStackTrace();
					  logger.error("Exception in getEntOwnerAccessDescFeedbackResults",e);
					  
				  }				  
				}
				  logger.debug("Exit getEntOwnerAccessDescFeedbackResults: "+finalResultList);

			    return finalResultList;//new ListResult(localList, localList.size());
			  }
			
			@POST
			  @Path("downloadEntOwnerAccessDescFeedbackResults")
			  public List downloadEntOwnerAccessDescFeedbackResults(Map<String, Object> paramMap) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				logger.debug("Enter downloadEntOwnerAccessDescFeedbackResults: ");		
			    List finalResultList=new ArrayList();
			    Map eachFeedbackMap=new HashMap();
			    String certId=(String)paramMap.get("certId");
				  
				  logger.debug("certId: "+ certId);
				  if(null != certId && !certId.equals(""))
				  {

					  finalResultList.add("AccessId,Application,Attribute,Access,Description,Helpful,Too much technical and/or business jargon,Needs more info about how it is used,Both");


				  try
				  {
					  SailPointContext context=getContext();

					  String operator=context.getUserName();
					  
					// existing feedback of requester
					  String descRatingSQL="sql: select certdata.target_id, feedback.application_name, feedback.Access_value,feedback.Access_name,feedback.rating,feedback.plain_feedback,count(feedback.rating) from\r\n" + 
					  		"(select feedback.* from iiqcap1.CERT_ACCESS_DESC_FEEDBACK feedback,\r\n" + 
					  		"(select max(LAST_UPDATED_DTTM) as LAST_UPDATED_DTTM,application_name,Access_name,Access_value,access_id,CERTIFIER_EID  from iiqcap1.CERT_ACCESS_DESC_FEEDBACK\r\n" + 
					  		"group by application_name,Access_name,Access_value,access_id,CERTIFIER_EID) latestfeedback\r\n" + 
					  		"where \r\n" + 
					  		"(feedback.rating=1 or (feedback.rating=0 and plain_feedback is not null and length(plain_feedback) > 1)) and \r\n" + 
					  		"feedback.CERTIFIER_EID = latestfeedback.CERTIFIER_EID and latestfeedback.application_name=feedback.application_name and latestfeedback.Access_name=feedback.Access_name and latestfeedback.Access_value=feedback.Access_value and feedback.LAST_UPDATED_DTTM=latestfeedback.LAST_UPDATED_DTTM)\r\n" + 
					  		"feedback,\r\n" + 
					  		"(select entity.target_id,entity.application,entity.native_identity,entity.target_name from identityiq.spt_certification cert,identityiq.SPT_CERTIFICATION_ENTITY entity where cert.id = entity.certification_id and cert.id='"+certId+"') certdata\r\n" + 
					  		"where feedback.access_id=certdata.target_id or (feedback.application_name=certdata.application and feedback.Access_value=certdata.native_identity and feedback.Access_name=certdata.target_name)  \r\n" + 
					  		"group by feedback.application_name, feedback.Access_value,feedback.Access_name,feedback.rating,feedback.plain_feedback,certdata.target_id\r\n" + 
					  		"order by rating,count(feedback.plain_feedback) desc";
						

						       List resultList=new ArrayList();
						        
						
						       try
								{
			  	            	QueryOptions qo = new QueryOptions();
				            	qo.setQuery(descRatingSQL);
				            	logger.debug("Executing query: getEntOwnerAccessDescFeedbackResults: descRatingSQL "+ descRatingSQL);
				    			Iterator iterator = context.search(descRatingSQL,null,qo);
				    			//resultList = sailpoint.tools.Util.iteratorToList(iterator);
				    			
				    			
				    			if(null != iterator)
				    			{
				    				 Map tempRatingConsolidationMap=new HashMap();
				    				 TreeMap<String,Object> tempMap=new TreeMap<String,Object>();
				    	
				    				 List<Map> tempFinalList=new ArrayList();
				    				 
				    				 while (iterator.hasNext()) {

					    					
				    					 Object[] object = (Object[]) iterator.next();				    					 
				    					    String accessId=(String) object[0];
				    					    if(null != tempRatingConsolidationMap && tempRatingConsolidationMap.containsKey(accessId))
				    					    {
				    					    	Integer rating=((BigDecimal) object[4]).intValue(); 
				    					    	String feedbackText=(String) object[5];
				    					    	Integer feedbackTextCount=((BigDecimal) object[6]).intValue(); 
				    							

				    					    	tempMap=(TreeMap)tempRatingConsolidationMap.get(accessId);
				    					    	if(rating == 1)
				    					    		tempMap.put("Helpful",feedbackTextCount);
									    			  else
									    			tempMap.put(feedbackText,feedbackTextCount);				    					    	
				    					    	
							    				tempRatingConsolidationMap.put(accessId, tempMap);
				    					    }
				    					    else
				    					    {
							    				String appName=(String) object[1];
							    				String entVal=(String) object[2];
							    				String attribute=(String) object[3];
							    				Integer rating=((BigDecimal) object[4]).intValue(); 
							    				String feedbackText=(String) object[5];
							    				Integer feedbackTextCount=((BigDecimal) object[6]).intValue(); 
							    				String displayName="";
							    				String desc="";
							    				eachFeedbackMap=new HashMap();
							    				eachFeedbackMap.put("accessId", accessId);
							    				
							    				
							    				ManagedAttribute managedAttribute=context.getObjectById(ManagedAttribute.class,accessId);
							    				if(null != managedAttribute)
							    				{
							    					displayName=managedAttribute.getDisplayName();
							    					desc=managedAttribute.getDescription("en_US");
							    					if(null == displayName || displayName.isEmpty())
							    						displayName=entVal;
							    					if(null == desc || desc.isEmpty())
							    						desc="Not Available";	
							    					
							    					desc=desc.replaceAll("[\\t\\n\\r]+"," ");       
							    					desc=desc.replaceAll("\\<.*?\\>", "");
							    						
							    					String relatedApplication = (String)managedAttribute.getAttribute(SWE_ATTRIBUTE_RELATED_APPLICATION);
										        	 if(null != relatedApplication && !relatedApplication.isEmpty())
										        	 {
										        		 appName=relatedApplication;										        
										        	 }
							    				}
							    				
							    				
							    				eachFeedbackMap.put("appName", appName);
							    				eachFeedbackMap.put("displayName", displayName);
							    				eachFeedbackMap.put("description", desc);
							    				eachFeedbackMap.put("attribute", attribute);

							    				tempMap=new TreeMap();
				    					    	if(rating == 1)
				    					    		tempMap.put("Helpful",feedbackTextCount);
									    			  else
									    			tempMap.put(feedbackText,feedbackTextCount);				    					    	
				    					    	
							    				tempRatingConsolidationMap.put(accessId, tempMap);						    				
							    				tempFinalList.add(eachFeedbackMap);
				    					    }

						    				
				    				  } 
			    				sailpoint.tools.Util.flushIterator(iterator);
			    				logger.debug("tempRatingConsolidationMap: "+tempRatingConsolidationMap);
			    				logger.debug("tempFinalList: "+tempFinalList);
				    				StringBuffer eachRowsb=new StringBuffer();
				    				for(Map eachMap: tempFinalList)
		    					    {
				    					 eachRowsb=new StringBuffer();
		    					    	 String accessId=(String) eachMap.get("accessId");
		    					    	 tempMap=(TreeMap)tempRatingConsolidationMap.get(accessId);
		    
						    			 eachRowsb.append("\""+eachMap.get("accessId")+"\"");
						    			 eachRowsb.append(",\""+eachMap.get("appName")+"\"");
						    			 eachRowsb.append(",\""+eachMap.get("attribute")+"\"");
						    		     eachRowsb.append(",\""+eachMap.get("displayName")+"\"");
						    			 eachRowsb.append(",\""+eachMap.get("description")+"\"");
						    			 
						    			 if(null != tempMap)
						    			 {
						    				 if(!tempMap.keySet().contains("Helpful"))
						    					 tempMap.put("Helpful",0);
						    				 if(!tempMap.keySet().contains("Too much technical and/or business jargon"))
						    					 tempMap.put("Too much technical and/or business jargon",0);
						    				 if(!tempMap.keySet().contains("Needs more info about how it is used"))
						    					 tempMap.put("Needs more info about how it is used",0);
						    				 if(!tempMap.keySet().contains("Both"))
						    					 tempMap.put("Both",0);
						    				 
						    					 
			    					    	
			    					    		 eachRowsb.append(",\""+tempMap.get("Helpful")+"\"");
			    					    		 eachRowsb.append(",\""+tempMap.get("Too much technical and/or business jargon")+"\"");
			    					    		 eachRowsb.append(",\""+tempMap.get("Needs more info about how it is used")+"\"");
			    					    		 eachRowsb.append(",\""+tempMap.get("Both")+"\"");
			    					    		 
			    					    	 
						    			 }
                                      
						    			
		    					    	finalResultList.add(eachRowsb.toString());
		    					    }
				    				
				    				
				    			}
				    			
								}	
								catch(Exception e)
								{
									logger.warn("Error in current downloadEntOwnerAccessDescFeedbackResults ",e);
								}

				  }
				  catch (Exception e)
				  {
					// e.printStackTrace();
					  logger.error("Exception in downloadEntOwnerAccessDescFeedbackResults",e);
					  
				  }				  
				}
				  logger.debug("Exit downloadEntOwnerAccessDescFeedbackResults: "+finalResultList);

			    return finalResultList;//new ListResult(localList, localList.size());
			  }
			
			
			
			public String  findEntOwnerAccessDescFeedbackResults(String certId) throws GeneralException
			{
				String descFeedback="";
				logger.debug("Enter findEntOwnerAccessDescFeedbackResults");
				//String existingdescFeedback="";
				
				try
				{
					
					SailPointContext context=getContext();
					 
					

		               String descRatingSQL="sql: select distinct feedback.rating from iiqcap1.CERT_ACCESS_DESC_FEEDBACK feedback, (select entity.target_id,entity.application,entity.native_identity,entity.target_name from identityiq.spt_certification cert,identityiq.SPT_CERTIFICATION_ENTITY entity where cert.id = entity.certification_id and cert.id='"+certId+"') certdata\r\n" + 
		               		"where (feedback.access_id=certdata.target_id or (feedback.application_name=certdata.application and feedback.Access_value=certdata.native_identity and feedback.Access_name=certdata.target_name))";
					

					       List resultList=new ArrayList();
					
					       try
							{
		  	            	QueryOptions qo = new QueryOptions();
			            	qo.setQuery(descRatingSQL);
			            	logger.debug("Executing query: findEntOwnerAccessDescFeedbackResults: descRatingSQL "+ descRatingSQL);
			    			Iterator moverIterator = context.search(descRatingSQL,null,qo);
			    			resultList = sailpoint.tools.Util.iteratorToList(moverIterator);
			    			sailpoint.tools.Util.flushIterator(moverIterator);
			    			logger.debug("resultList: "+resultList);
							}		
							catch(Exception e)
							{
								logger.error("Error getting current getExistingAccessDescFeedbackRating ",e);
							}
			    					if(null != resultList &&  resultList.size() > 1)
					    			{
			    						descFeedback="Negative";		    						
					    			}
			    					else if(null != resultList &&  resultList.size() == 1)
			    					{
			    						int rating=((BigDecimal) resultList.get(0)).intValue();	   	
			    						if(rating == 0)
			    							descFeedback="Negative";
			    						else
			    							descFeedback="Positive";
			    					}
						
					
				}		
				catch(Exception e)
				{
					logger.error("Error in findEntOwnerAccessDescFeedbackResults ",e);
				}
				
				logger.debug("Exit findEntOwnerAccessDescFeedbackResults descFeedback: "+ descFeedback);
				return descFeedback;
			}
			
			public List getTargetUsersEIDsItemsIdsByCertification(Certification certObj) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				logger.debug("Enter getTargetUsersEIDsByCertification: ");		

				  Map resultMap= new HashMap();
				  List certUsersList=new ArrayList();

				  try
				  {
					  
					  if(null != certObj)
						{
						  if(certObj.getType().equals(Certification.Type.DataOwner))
						  {
							  List<CertificationEntity> certEntities = certObj.getEntities();
							   for(CertificationEntity eachEntity: certEntities)
							   {
								   List<CertificationItem> certitems = eachEntity.getItems();
								   for(CertificationItem eachItem: certitems)
								   {
									  // if(!certUsersList.contains(eachItem.getTargetName()))
									   resultMap= new HashMap();
									   resultMap.put("certItemId", eachItem.getId());									   
									   resultMap.put("userId", eachItem.getTargetName());
									   certUsersList.add(resultMap);
								   }
							   }
						  }
						  else
						  {
						   List<CertificationEntity> certEntities = certObj.getEntities();
						   for(CertificationEntity eachEntity: certEntities)
						   {
							   //if(!certUsersList.contains(eachEntity.getTargetName()))
							   {
								   List<CertificationItem> certitems = eachEntity.getItems();
								   for(CertificationItem eachItem: certitems)
								   {
									   resultMap= new HashMap();
									   resultMap.put("certItemId", eachItem.getId());
									   resultMap.put("userId", eachEntity.getTargetName());
									   certUsersList.add(resultMap);
								   }								   
							   }
						   }
						  }
						}
					  
					 

				  }
				  catch (Exception e)
				  {
					// e.printStackTrace();
					  logger.error("Exception in getTargetUsersEIDsByCertification",e);
					  
				  }				  
				
				  logger.debug("Exit getTargetUsersEIDsByCertification: "+certUsersList);

				  return certUsersList;
			  }

			  public List getTargetUsersEIDsByCertification(Certification certObj) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				logger.debug("Enter getTargetUsersEIDsByCertification: ");		

				  List<String> certUsersList=new ArrayList();

				  try
				  {
					  
					  if(null != certObj)
						{
						  if(certObj.getType().equals(Certification.Type.DataOwner))
						  {
							  List<CertificationEntity> certEntities = certObj.getEntities();
							   for(CertificationEntity eachEntity: certEntities)
							   {
								   List<CertificationItem> certitems = eachEntity.getItems();
								   for(CertificationItem eachItem: certitems)
								   {
									   if(!certUsersList.contains(eachItem.getTargetName()))
									     certUsersList.add(eachItem.getTargetName());
								   }
							   }
						  }
						  else
						  {
						   List<CertificationEntity> certEntities = certObj.getEntities();
						   for(CertificationEntity eachEntity: certEntities)
						   {
							   if(!certUsersList.contains(eachEntity.getTargetName()))
							   certUsersList.add(eachEntity.getTargetName());
						   }
						  }
						}
					  
					 

				  }
				  catch (Exception e)
				  {
					// e.printStackTrace();
					  logger.error("Exception in getTargetUsersEIDsByCertification",e);
					  
				  }				  
				
				  logger.debug("Exit getTargetUsersEIDsByCertification: "+certUsersList);

				  return certUsersList;
			  }
			/*@POST
			  @Path("getCertificationTargetUsersEIDs")
			  public List getCertificationTargetUsersEIDs(Map<String, Object> paramMap) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				logger.debug("Enter getCertificationTargetUsersEIDs: ");		

			    Map eachFeedbackMap=new HashMap();
			    
			    List<String> certUsersList=new ArrayList();
				 String certId=(String)paramMap.get("certId");
				  
				  logger.debug("certId: "+ certId);
				  if(null != certId && !certId.equals(""))
				  {

				  try
				  {
					  SailPointContext context=getContext();
					  Certification certObj= context.getObjectById(Certification.class, certId);
					  if(null != certObj)
						{
						  if(certObj.getType().equals(Certification.Type.DataOwner))
						  {
							  List<CertificationEntity> certEntities = certObj.getEntities();
							   for(CertificationEntity eachEntity: certEntities)
							   {
								   List<CertificationItem> certitems = eachEntity.getItems();
								   for(CertificationItem eachItem: certitems)
								   {
									   if(!certUsersList.contains(eachItem.getTargetName()))
									     certUsersList.add(eachItem.getTargetName());
								   }
							   }
						  }
						  else
						  {
						   List<CertificationEntity> certEntities = certObj.getEntities();
						   for(CertificationEntity eachEntity: certEntities)
						   {
							   if(!certUsersList.contains(eachEntity.getTargetName()))
							   certUsersList.add(eachEntity.getTargetName());
						   }
						  }
						}
					  
					 

				  }
				  catch (Exception e)
				  {
					// e.printStackTrace();
					  logger.error("Exception in getCertificationTargetUsersEIDs",e);
					  
				  }				  
				}
				  logger.debug("Exit getCertificationTargetUsersEIDs: "+certUsersList);

				  return certUsersList;
			  }
			*/
			@POST
			  @Path("refreshPrevalenceScoresCachePerUser")
			public String  refreshPrevalenceScoresCachePerUser(Map<String, Object> paramMap) throws GeneralException
			{
				 
				logger.debug("Enter refreshPrevalenceScoresCachePerUser");
				String result="";
				String identName=(String)paramMap.get("identName");
				bearerToken=(String)paramMap.get("bearerToken");
				String certId=(String)paramMap.get("certId");
				try
				{
					
					SailPointContext context=getContext();
					
					Custom sweCertsCustomObj=context.getObjectByName(Custom.class, "SWE Certification Addins Plugin Custom");			 		   			 
					prevalenceRefreshThreshold=Integer.parseInt((String)sweCertsCustomObj.get("PrevalenceRefreshThresholdHours"));	
					prevalenceScoresAPIHit=Boolean.valueOf((String)sweCertsCustomObj.get("PrevalenceScoresAPIHit"));
					 
	
		            //        String getPrevSQL="sql: select ACCESS_ID,PREVALENCE_SCORE,PREVALENCE_COUNT,COMMUNITY_SIZE,COMMUNITY_TYPE from iiqcap1.CERT_PREVALENCE_SCORES_CACHE where TARGET_EID = '"+identName+"'  and LAST_UPDATED_DTTM >= (NOW() - INTERVAL 4 HOUR)";
					String getPrevSQL="sql: select ACCESS_ID,PREVALENCE_SCORE,PREVALENCE_COUNT,COMMUNITY_SIZE,COMMUNITY_TYPE from iiqcap1.CERT_PREVALENCE_SCORES_CACHE where TARGET_EID = '"+identName+"'  and LAST_UPDATED_DTTM >= (sysdate - interval '"+prevalenceRefreshThreshold+"' hour)";
		                   

					       List resultList=new ArrayList();
					
					       try
							{
			  	            	QueryOptions qo = new QueryOptions();
				            	qo.setQuery(getPrevSQL);
				            	logger.debug("Executing query: getPrevalenceScoresPerAccessfromCacheorAPI: descRatingSQL "+ getPrevSQL);
				    			Iterator iterator = context.search(getPrevSQL,null,qo);
				    			resultList = sailpoint.tools.Util.iteratorToList(iterator);
				    			sailpoint.tools.Util.flushIterator(iterator);
				    			//logger.error("123 getPrevalenceScorefromCache resultList: "+resultList);
				    			
				    			
		    					if((null ==resultList || resultList.isEmpty()))
		    					{
		    						if(prevalenceScoresAPIHit)
		    						{
			    						Map resultMap=hitPrevalenceScoreAPIandupdateCache(identName,"",context,certId);
			    						if(null != resultMap && null != resultMap.get("prevResultStatus"))
			    							result=(String)resultMap.get("prevResultStatus");
			    						else
			    						result="Failed to get prevalance scores for "+identName;
		    						}
		    					}
		    					else
		    						result=identName+" already has Prevelance Scores Cache";
		    					
							}		
							catch(Exception e)
							{
								logger.error("Error getting current refreshPrevalenceScoresCachePerUser ",e);
							}

					
				}		
				catch(Exception e)
				{
					logger.error("Error in refreshPrevalenceScoresCachePerUser ",e);
					result="Prevelance Scores fetching with Exeption for "+identName;
				}
				
				logger.debug("Exit refreshPrevalenceScoresCachePerUser: ");
				 return result;
			}
			
			@GET
			  @Path("getPrevScoresBearerToken")
			public String  getPrevScoresBearerToken() throws GeneralException
			{				 
				logger.debug("Enter getPrevScoresBearerToken");

				try
				{
					 SailPointContext context=getContext();
					  Custom sweCertsCustomObj=context.getObjectByName(Custom.class, "SWE Certification Addins Plugin Custom");			 
		  			  String DevExchangeTokenURL=(String)sweCertsCustomObj.get("DevExchangeTokenURL");	
		  			  String DevExchangePrevalenceAPICID=(String)sweCertsCustomObj.get("DevExchangePrevalenceAPICID");
		  			  String DevExchangePrevalenceAPISID=(String)sweCertsCustomObj.get("DevExchangePrevalenceAPISID");
		  			  String DevExchangeTokenConnectionTimeoutSeconds=(String)sweCertsCustomObj.get("DevExchangeToken_Connection_Timeout_Seconds");
		             logger.debug("DevExchangeTokenURL: "+DevExchangeTokenURL);
		             logger.debug("DevExchangePrevalenceAPICID: "+DevExchangePrevalenceAPICID);
		             logger.debug("DevExchangePrevalenceAPISID: "+DevExchangePrevalenceAPISID);
		             logger.debug("DevExchangeTokenConnectionTimeoutSeconds: "+DevExchangeTokenConnectionTimeoutSeconds);
					  
					  String clientId=DevExchangePrevalenceAPICID;
					  String clientSecret=DevExchangePrevalenceAPISID;
					  String tokenURL=DevExchangeTokenURL;
					  //logger.debug("bearerToken: "+ bearerToken);
					  if(null == bearerToken || bearerToken.isEmpty())
					  {
						  logger.warn("getting bearer Token ");
					  bearerToken=getExchangeBearerToken(clientId,clientSecret,tokenURL,Integer.parseInt(DevExchangeTokenConnectionTimeoutSeconds));					  
					  }
					
				}		
				catch(Exception e)
				{
					logger.error("Error in getPrevScoresBearerToken ",e);
					
				}
				
				logger.debug("Exit getPrevScoresBearerToken: ");
				return bearerToken;
			}
			
			@POST
			  @Path("getprevalenceScoresData")
			public Map  getPrevalenceScoresPerAccessfromCacheorAPI(Map<String, Object> paramMap) throws GeneralException
			{
				 Map resultMap=new HashMap();
				logger.debug("Enter getPrevalenceScoresPerAccessfromCacheorAPI");
				//String existingdescFeedback="";
				
				try
				{
					
					SailPointContext context=getContext();
					 
					String certItemId=(String)paramMap.get("certItemId");
					String accessId=(String)paramMap.get("accessId");
					String certId=(String)paramMap.get("certId");
					
					Custom sweCertsCustomObj=context.getObjectByName(Custom.class, "SWE Certification Addins Plugin Custom");			 		   			 
					prevalenceRefreshThreshold=Integer.parseInt((String)sweCertsCustomObj.get("PrevalenceRefreshThresholdHours"));	
					prevalenceScoresAPIHit=Boolean.valueOf((String)sweCertsCustomObj.get("PrevalenceScoresAPIHit"));
					
					
					bearerToken=(String)paramMap.get("bearerToken");
					//logger.warn("getprevalenceScoresData bearerToken: "+bearerToken);
					
					if(null != certItemId && !certItemId.isEmpty() && null != accessId && !accessId.isEmpty())
					  {
						  CertificationItem certItem=context.getObjectById(CertificationItem.class, certItemId);
						  if(null != certItem && null != certItem.getType() && (certItem.getType().equals(CertificationItem.Type.Exception) || certItem.getType().equals(CertificationItem.Type.Bundle) || certItem.getType().equals(CertificationItem.Type.DataOwner)))
						  {
							  String identName=certItem.getIdentity();

		                   // String getPrevSQL="sql: select PREVALENCE_SCORE,PREVALENCE_COUNT,COMMUNITY_SIZE,COMMUNITY_TYPE from iiqcap1.CERT_PREVALENCE_SCORES_CACHE where TARGET_EID = '"+identName+"' and ACCESS_ID='"+accessId+"' and LAST_UPDATED_DTTM >= (NOW() - INTERVAL 4 HOUR)";
		                    //String getPrevSQL="sql: select ACCESS_ID,PREVALENCE_SCORE,PREVALENCE_COUNT,COMMUNITY_SIZE,COMMUNITY_TYPE from iiqcap1.CERT_PREVALENCE_SCORES_CACHE where TARGET_EID = '"+identName+"'  and LAST_UPDATED_DTTM >= (NOW() - INTERVAL 4 HOUR)";
		                     String getPrevSQL="sql: select ACCESS_ID,PREVALENCE_SCORE,PREVALENCE_COUNT,COMMUNITY_SIZE,COMMUNITY_TYPE from iiqcap1.CERT_PREVALENCE_SCORES_CACHE where TARGET_EID = '"+identName+"'  and LAST_UPDATED_DTTM >= (sysdate - interval '"+prevalenceRefreshThreshold+"' hour)";
							

					       List resultList=new ArrayList();
					
					       try
							{
			  	            	QueryOptions qo = new QueryOptions();
				            	qo.setQuery(getPrevSQL);
				            	logger.debug("Executing query: getPrevalenceScoresPerAccessfromCacheorAPI: descRatingSQL "+ getPrevSQL);
				    			Iterator iterator = context.search(getPrevSQL,null,qo);
				    			//resultList = sailpoint.tools.Util.iteratorToList(iterator);
				    			
				    			//logger.error("123 getPrevalenceScorefromCache resultList: "+resultList);
				    			
				    			if(iterator.hasNext())
				    			{
				    				logger.debug("start iterating..");
				    				while (iterator.hasNext()) {

				    					
				    					 Object[] object = (Object[]) iterator.next();				    					 
				    					    String eachAccessId=(String) object[0];
				    					    logger.debug("eachAccessId: "+eachAccessId);
				    					    logger.debug("accessId: "+accessId);
				    					    if(null != eachAccessId && eachAccessId.equalsIgnoreCase(accessId))
				    					    {				    					    
					    						resultMap.put("prevalenceScore", object[1]);
							                	resultMap.put("prevalenceCount", object[2]);
							                	resultMap.put("communitySize", object[3]);
							                	resultMap.put("communityType", object[4]);	
							                	logger.debug("*********** got result resultMap *************"+resultMap);							                	
				    					    }
				    					    resultMap.put("prevResultStatus",identName+" already has Prevelance Scores Cache");
				    				}
		    												
				    			}
		    					else if(prevalenceScoresAPIHit)
		    					{
		    						
		    						logger.warn("*********** Duplicate prevalence scores api hit for user *************"+identName);
		    						resultMap=hitPrevalenceScoreAPIandupdateCache(identName,accessId,context,certId);
		    					}
		    					sailpoint.tools.Util.flushIterator(iterator);
							}		
							catch(Exception e)
							{
								logger.error("Error getting current getPrevalenceScoresPerAccessfromCacheorAPI ",e);
							}
					       
		    					
					       if(null == resultMap || resultMap.isEmpty())
								resultMap=new HashMap();
							
								resultMap.put("result", true);
								resultMap.put("certItemId", certItemId);
					  }	
					}
				}		
				catch(Exception e)
				{
					logger.error("Error in getPrevalenceScoresPerAccessfromCacheorAPI ",e);
				}
				
				logger.debug("Exit getPrevalenceScoresPerAccessfromCacheorAPI: "+ resultMap);
				return resultMap;
			}
			
			public boolean checkifCerthasPrevalenceScoresCache(String certId,SailPointContext context) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				boolean result=false;
				logger.debug("Enter checkifCerthasPrevalenceScoresCache: certId: "+certId);		
			    
			      //Object Manager Audit
			    Connection con=null;
			    PreparedStatement pstmt=null;

					  
					  
	             // String getPrevSQL="sql: select count(*) from iiqcap1.CERT_PREVALENCE_SCORES_CACHE where CERT_ID = '"+certId+"'  and LAST_UPDATED_DTTM >= (NOW() - INTERVAL 4 HOUR)";
			    String getPrevSQL="sql: select count(*) from iiqcap1.CERT_PREVALENCE_SCORES_CACHE where CERT_ID = '"+certId+"'  and LAST_UPDATED_DTTM >= (sysdate - interval '"+prevalenceRefreshThreshold+"' hour)";
						

				       List resultList=new ArrayList();
				
				       try
						{
		  	            	QueryOptions qo = new QueryOptions();
			            	qo.setQuery(getPrevSQL);
			            	logger.debug("Executing query: checkifCerthasPrevalenceScoresCache: getPrevSQL "+ getPrevSQL);
			    			Iterator iterator = context.search(getPrevSQL,null,qo);
			    			resultList = sailpoint.tools.Util.iteratorToList(iterator);
			    			logger.debug("resultList: "+resultList);	
			    			if(null != resultList && !resultList.isEmpty() && ((BigDecimal)resultList.get(0)).intValue() > 0)			    			
			    				result=true;
			    			
						}
			    			catch(Exception e) {
		    					logger.error("Error in checkifCerthasPrevalenceScoresCache getting existing cache from table ",e);
		    				}
				   
				       logger.debug("Exit checkifCerthasPrevalenceScoresCache: result: "+result);		
				  return result;
				  
			  }
			public boolean checkifUserhasPrevalenceScoresCache(String userId,SailPointContext context) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				boolean result=false;
				logger.debug("Enter checkifUserhasPrevalenceScoresCache: certId: "+userId);		
			    
			      //Object Manager Audit
			    Connection con=null;
			    PreparedStatement pstmt=null;

					  
					  
	             // String getPrevSQL="sql: select count(*) from iiqcap1.CERT_PREVALENCE_SCORES_CACHE where TARGET_EID = '"+userId+"'  and LAST_UPDATED_DTTM >= (NOW() - INTERVAL 4 HOUR)";
			    String getPrevSQL="sql: select count(*) from iiqcap1.CERT_PREVALENCE_SCORES_CACHE where TARGET_EID = '"+userId+"'  and LAST_UPDATED_DTTM >= (sysdate - interval '"+prevalenceRefreshThreshold+"' hour)";
	              
						

				       List resultList=new ArrayList();
				
				       try
						{
		  	            	QueryOptions qo = new QueryOptions();
			            	qo.setQuery(getPrevSQL);
			            	logger.debug("Executing query: checkifUserhasPrevalenceScoresCache: getPrevSQL "+ getPrevSQL);
			    			Iterator iterator = context.search(getPrevSQL,null,qo);
			    			resultList = sailpoint.tools.Util.iteratorToList(iterator);
			    			
			    			if(null != resultList && !resultList.isEmpty() && ((BigDecimal)resultList.get(0)).intValue() > 0)
			    				result=true;
						}
			    			catch(Exception e) {
		    					logger.error("Error in checkifUserhasPrevalenceScoresCache getting existing cache from table ",e);
		    				}
				   
				       logger.debug("Exit checkifUserhasPrevalenceScoresCache: result: "+result);		
				  return result;
				  
			  }
			
			public Map refreshPrevalanceScoresTableCache(Map responseMap, String userID,String accessId,SailPointContext context,String certID) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				Map resultMap=new HashMap();
				logger.debug("Enter refreshPrevalanceScoresTableCache: userID: "+userID +" responseMap: "  +responseMap);		
			    String response="Success";
			    
			      //Object Manager Audit
			    Connection con=null;
			    PreparedStatement pstmt=null;
				  try
				  {
					  
					  
					  boolean isUserthasPrevalenceScores=checkifUserhasPrevalenceScoresCache(userID,context);
			
					  //get variables from request
				       
				        Map metaDataMap=(Map)responseMap.get("metadata");
					    Map communityMap=(Map)metaDataMap.get("community");
					    double communitySize=(Double)communityMap.get("communitySize");
			            String communityType=(String)communityMap.get("communityType");
			            logger.debug("communitySize: "+communitySize);
			            logger.debug("communityType: "+communityType);
			            List<Map> scores=(ArrayList)responseMap.get("scores");
			            
			            if(isUserthasPrevalenceScores)
			            {
				            for (Map eachScoreMap: scores) {
				            	logger.debug("eachScoreMap: "+eachScoreMap);			            	
				                String eachAccessId = (String)eachScoreMap.get("accessId");
				                double prevalenceScore = (Double)eachScoreMap.get("prevalenceScore");
				                double prevalenceCount = (double)eachScoreMap.get("prevalenceCount");
				                logger.debug("accessId: "+eachAccessId);
				                logger.debug("prevalenceScore: "+prevalenceScore);
				                logger.debug("prevalenceCount: "+prevalenceCount);
				                
				                if(accessId.equalsIgnoreCase(eachAccessId))
				                {
				                	resultMap.put("prevalenceScore", prevalenceScore);
				                	resultMap.put("prevalenceCount", prevalenceCount);
				                	resultMap.put("communitySize", communitySize);
				                	resultMap.put("communityType", communityType);
				                }			                
				            }
				            logger.warn("concurrent API hits observerd for : "+userID +" , so not inserting the duplicate data");
				            return resultMap;
			            } 
			            
			            
					 
					  con=context.getJdbcConnection();	
					  try
	    				{
						// delete existing data
						 				  
						  pstmt = con.prepareStatement("delete from iiqcap1.CERT_PREVALENCE_SCORES_CACHE  where TARGET_EID = ?");
						  pstmt.setString(1,userID);						  
						  pstmt.execute();
	    				}
	    				catch(Exception e) {
	    					logger.debug("Error in deleting existing cache from table ",e);
	    				}
					  
					  
					  if(null != pstmt)
						  pstmt.close();
					 
	
					  
					  
					  String insertSQL="insert into iiqcap1.CERT_PREVALENCE_SCORES_CACHE(CERT_ID,TARGET_EID,ACCESS_ID,PREVALENCE_SCORE,PREVALENCE_COUNT,COMMUNITY_SIZE,COMMUNITY_TYPE)\r\n" + 
				  		"select ? CERT_ID,? TARGET_EID ,? ACCESS_ID ,? PREVALENCE_SCORE,? PREVALENCE_COUNT,? COMMUNITY_SIZE,? COMMUNITY_TYPE from dual";
					  pstmt = con.prepareStatement(insertSQL);
					  
					   
			            
			            int recordcount = 0;
			            int tableinsertsbatchSize=5;
			            
			            for (Map eachScoreMap: scores) {
			            	logger.debug("eachScoreMap: "+eachScoreMap);
			            	
			                String eachAccessId = (String)eachScoreMap.get("accessId");
			                double prevalenceScore = (Double)eachScoreMap.get("prevalenceScore");
			                double prevalenceCount = (double)eachScoreMap.get("prevalenceCount");
			                logger.debug("accessId: "+eachAccessId);
			                logger.debug("prevalenceScore: "+prevalenceScore);
			                logger.debug("prevalenceCount: "+prevalenceCount);
			                
			                if(accessId.equalsIgnoreCase(eachAccessId))
			                {
			                	resultMap.put("prevalenceScore", prevalenceScore);
			                	resultMap.put("prevalenceCount", prevalenceCount);
			                	resultMap.put("communitySize", communitySize);
			                	resultMap.put("communityType", communityType);
			                }
			                
			                pstmt.setString(1, certID);
			                pstmt.setString(2, userID);
			                pstmt.setString(3, eachAccessId);
			                pstmt.setDouble(4, prevalenceScore);
			                pstmt.setDouble(5, prevalenceCount);
			                pstmt.setDouble(6, communitySize);
			                pstmt.setString(7, communityType);
			                pstmt.addBatch();
			                
			                if(++recordcount % tableinsertsbatchSize == 0 && null != pstmt) {
			                	logger.debug("Executing the Ent batch.. recordcount: "+ recordcount);
			                	//recordcount=0;
			    				try
			    				{
			    					pstmt.executeBatch();
			    				}
			    				catch(Exception e) {
			    					logger.error("Error in refreshPrevalanceScoresTableCache executing batch ",e);
			    				}
			    			 }
			            }
			            
			            try
	    				{
			            	if(null != pstmt)
	    					pstmt.executeBatch();
	    				}
	    				catch(Exception e) {
	    					logger.debug("Error in refreshPrevalanceScoresTableCache executing remaining batch.. ",e);
	    				}
				  }
				  catch (Exception e)
				  {
					// e.printStackTrace();
					  logger.error("Exception in refreshPrevalanceScoresTableCache",e);
					  response="Failed: "+e;
				  }
				  finally
				  {
					  try
					  {
						  if(null != con)
							  con.close();
						  
						  if(null != pstmt)
							  pstmt.close();
					  }
					  catch (Exception e) {}
				  }
				  
				  responseMap=null;
				  logger.debug("Exit refreshPrevalanceScoresTableCache: "+resultMap);
			    
				  
				  
			    return resultMap;
			  }
			public Map hitPrevalenceScoreAPIandupdateCache(String userID,String accessId,SailPointContext context,String certId) throws GeneralException
			  {
				//logger.setLevel(Level.DEBUG);
				 Map resultMap=new HashMap();
				logger.warn("Enter hitPrevalenceScoreAPIandupdateCache paramMap: "+userID);		
			    boolean result=false;

				
				  try
				  {
					  //SailPointContext context=getContext();
					  
					  Custom sweCertsCustomObj=context.getObjectByName(Custom.class, "SWE Certification Addins Plugin Custom");			 
		   			  
		   			  String DevExchangePrevalenceAPIURL=(String)sweCertsCustomObj.get("DevExchangePrevalenceAPIURL");		   			  
		   			  String PrevalenceAPIConnectionTimeoutSeconds=(String)sweCertsCustomObj.get("PrevalenceAPI_Connection_Timeout_Seconds");
		   			 
		              
		              logger.debug("DevExchangePrevalenceAPIURL: "+DevExchangePrevalenceAPIURL);
		              logger.debug("PrevalenceAPIConnectionTimeoutSeconds: "+PrevalenceAPIConnectionTimeoutSeconds);
		              
					  
					 
					  //logger.debug("bearerToken: "+ bearerToken);
					  if(null == bearerToken || bearerToken.isEmpty())
					  {
						  String DevExchangeTokenURL=(String)sweCertsCustomObj.get("DevExchangeTokenURL");	
			   			  String DevExchangePrevalenceAPICID=(String)sweCertsCustomObj.get("DevExchangePrevalenceAPICID");
			   			  String DevExchangePrevalenceAPISID=(String)sweCertsCustomObj.get("DevExchangePrevalenceAPISID");
			   			  String DevExchangeTokenConnectionTimeoutSeconds=(String)sweCertsCustomObj.get("DevExchangeToken_Connection_Timeout_Seconds");
			   			  logger.debug("DevExchangeTokenURL: "+DevExchangeTokenURL);
			              logger.debug("DevExchangePrevalenceAPICID: "+DevExchangePrevalenceAPICID);
			              logger.debug("DevExchangePrevalenceAPISID: "+DevExchangePrevalenceAPISID);
			              logger.debug("DevExchangeTokenConnectionTimeoutSeconds: "+DevExchangeTokenConnectionTimeoutSeconds);
						  logger.warn("getting bearer Token for "+userID);
					      bearerToken=getExchangeBearerToken(DevExchangePrevalenceAPICID,DevExchangePrevalenceAPISID,DevExchangeTokenURL,Integer.parseInt(DevExchangeTokenConnectionTimeoutSeconds));					  
					  }
					  else
						  logger.warn("Using the Existing bearer Token for "+userID);
					  String prevalenceURL=DevExchangePrevalenceAPIURL+userID;
					  //String prevalenceURL=DevExchangePrevalenceAPIURL+"DPE945";
					  
					  
					  //HttpUtil utilObj = new HttpUtil();
					  //HttpClient client = HttpClientBuilder.create().build().setSSLSocketFactory(utilObj.getSSLConnectionToByPassCertification()).build();
					  HttpClient client = HttpClientBuilder.create().build();
					 // HttpClient client=sweCustomCertificationsPluginAddins.createSSLInsecureClient();
	
	                  HttpPost post = new HttpPost(prevalenceURL);	
	                  int timeoutSeconds=Integer.parseInt(PrevalenceAPIConnectionTimeoutSeconds);
	                    int CONNECTION_TIMEOUT_MS = timeoutSeconds * 1000; // Timeout in millis.
	                    RequestConfig requestConfig = RequestConfig.custom()
	                        .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
	                        .setConnectTimeout(CONNECTION_TIMEOUT_MS)
	                        .setSocketTimeout(CONNECTION_TIMEOUT_MS)
	                        .build();
	                    post.setConfig(requestConfig);
	                    
	                  post.setHeader("Accept", "application/json;v=2.0");
	                  post.setHeader("Content-type", "application/json");
	                  post.setHeader("Authorization", bearerToken);
				  		
	                  Map requestBodyMap = new HashMap();
	                  requestBodyMap.put("ranker_type", "standard");
	                  requestBodyMap.put("style", "certification");
	                  requestBodyMap.put("threshold", 0.5);	
	                  requestBodyMap.put("job_code_min_peers", 4);
	                  requestBodyMap.put("exclude_manager_min_peers", 1);
	                  requestBodyMap.put("traversals_up", 1);
	                  requestBodyMap.put("extra_traversals_down", 0);
	                  requestBodyMap.put("singleton_prevalence", -99);
	                  requestBodyMap.put("exclude_target", false);
			           
	                  Gson gsonObj = new Gson();
	                  String inputJson = gsonObj.toJson(requestBodyMap);
	                  StringEntity stringEntity = new StringEntity(inputJson);
	                  post.setEntity(stringEntity);    
			            
	                  HttpResponse httpResponse = client.execute(post);
	                    String response = EntityUtils.toString(httpResponse.getEntity());
	                    logger.debug("response: "+response);
	                    if (null != response && !response.isEmpty()) {
	                    	Map responseMap=gsonObj.fromJson(response, Map.class);
	                    	logger.debug("responseMap: "+responseMap);
	                    	if(null != responseMap && responseMap.containsKey("scores") && responseMap.containsKey("metadata"))
	                    		{	                    		
	                    		 resultMap= refreshPrevalanceScoresTableCache(responseMap,userID,accessId,context,certId);
	                    		 resultMap.put("prevResultStatus", "got the Prevalence Scores for user: "+userID);
	                    		}
	                    	else
	                    	{
	                    		resultMap.put("prevResultStatus", "Failed to get Prevalence Scores for user: "+userID+" reason: "+responseMap);
	                    		logger.warn("API failed to get the prevalence scores for " + userID + " resultMap: "+ resultMap);

	                    	}
	                    }
					  
					  
				  }
				  catch (Exception e)
				  {
					  logger.error("Exception in hitPrevalenceScoreAPIandupdateCache",e);

				  }
				  
				  logger.debug("Exit hitPrevalenceScoreAPIandupdateCache: "+resultMap);

			    return resultMap;
			  }
			
			public String  getExchangeBearerToken(String clientId,String clientSecret,String url, int timeoutSeconds) throws GeneralException
			{
				String bearerToken="";
				logger.debug("Enter getBearerToken");
				//String existingdescFeedback="";
				
				try
				{
									
					HttpClient client = HttpClientBuilder.create().build();
					//HttpClient client=sweCustomCertificationsPluginAddins.createSSLInsecureClient();
                    HttpPost post = new HttpPost(url);

                    int CONNECTION_TIMEOUT_MS = timeoutSeconds * 1000; // Timeout in millis.
                    RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
                        .setConnectTimeout(CONNECTION_TIMEOUT_MS)
                        .setSocketTimeout(CONNECTION_TIMEOUT_MS)
                        .build();
                    post.setConfig(requestConfig);
                    
                    List<NameValuePair> arguments = new ArrayList<>(3);
        	        arguments.add(new BasicNameValuePair("client_id", clientId));
        	        arguments.add(new BasicNameValuePair("client_secret", clientSecret));
        	        arguments.add(new BasicNameValuePair("grant_type", "client_credentials"));
        		    

        	        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        	        post.addHeader("cache-control", "no-cache");
        	        post.setEntity(new UrlEncodedFormEntity(arguments));
                    
                    Gson gsonObj = new Gson();
                    
                    
                    HttpResponse httpResponse = client.execute(post);
                    String response = EntityUtils.toString(httpResponse.getEntity());
                    logger.debug("response: "+response);
                    if (null != response && !response.isEmpty()) {
                    	Map responseMap=gsonObj.fromJson(response, Map.class);
                    	logger.debug("responseMap: "+responseMap);
                    	if(null != responseMap && responseMap.containsKey("access_token"))
                    		bearerToken="Bearer " +responseMap.get("access_token");
                    	else
                    		logger.warn("Failed to get the prev score Bearer token responseMap: "+responseMap);
                    		
                    }

			        
				}		
				catch(Exception e)
				{
					logger.error("Error in getBearerToken ",e);
				}
				
				logger.debug("Exit getBearerToken: "+ bearerToken);
				return bearerToken;
			}
			
			
			public static HttpClient createSSLInsecureClient() {
		        SSLContext sslcontext = createSSLContext();
		        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
		          HttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		        return httpclient;
		    }


		    private static SSLContext createSSLContext() {
		        SSLContext sslcontext = null;
		        try {
		            sslcontext = SSLContext.getInstance("TLS");
		            sslcontext.init(null, new TrustManager[] {new TrustAnyTrustManager()}, new SecureRandom());
		        } catch (NoSuchAlgorithmException e) {
		            e.printStackTrace();
		        } catch (KeyManagementException e) {
		            e.printStackTrace();
		        }
		        return sslcontext;
		    }


		    private static class TrustAnyTrustManager implements X509TrustManager {

		        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

		        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

		        public X509Certificate[] getAcceptedIssuers() {
		            return new X509Certificate[] {};
		        }
		    }
}