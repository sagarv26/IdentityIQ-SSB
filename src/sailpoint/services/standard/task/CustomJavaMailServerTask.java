package sailpoint.services.standard.task;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.Emailer;
import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.EmailOptions;
import sailpoint.object.EmailTemplate;
import sailpoint.object.TaskResult;
import sailpoint.object.TaskSchedule;
import sailpoint.object.WorkItem;
import sailpoint.task.AbstractTaskExecutor;
import sailpoint.task.TaskMonitor;
import sailpoint.tools.Message;

public class CustomJavaMailServerTask extends AbstractTaskExecutor {

   private static Log log = LogFactory.getLog(CustomJavaMailServerTask.class);
   public static final String ARG_OUT_REQUEST_APPROVED = "requestApproved";
   public static final String ARG_OUT_REQUEST_DENIED = "requestDenied";
   public static final String ARG_OUT_REQUEST_IGNORED = "requestIgnored";
   public static final String ARG_OUT_MESSAGES = "messages";

   private static String messages = null;
   private static String workItemOperation = "";
   private int requestsApproved = 0, requestsDenied = 0, requestsIgnored = 0;

   // Termination variable
   @SuppressWarnings("unused")
   private Boolean term = false;

   // Task Monitor
   private TaskMonitor monitor;
   private SailPointContext context = null;

   // Task Argument Type
   public enum TASK_ARG_TYPE {
      RequestsApproved, RequestsDenied, RequestsIgnored
   }

   /**
    * This function is invoked when IdentityIQ runs task.
    */
   @Override
   public void execute(SailPointContext context, TaskSchedule schedule,
         TaskResult result, Attributes<String, Object> args) throws Exception {

      setContext(context);
      monitor = new TaskMonitor(context, result);
      setMonitor(monitor);
      CheckEmail emailChecker = new CheckEmail(context);

      try {
         String decryptedPassword = context.decrypt(emailChecker
               .getProps("svcAccountPassword"));
         emailChecker.setProps("svcAccountPassword", decryptedPassword);
         emailChecker.connectToMailServer();
         int newMessageCtr = emailChecker.getMessageCount();
         log.debug("Total New Emails: " + newMessageCtr);

         while (newMessageCtr > 0) {
            Map<String, String> mMap = emailChecker
                  .getNextMessageNo(newMessageCtr);
            log.debug(newMessageCtr + ") " + "From: " + mMap.get("from") + ", "
                  + "Email Subject:" + mMap.get("subject"));

            String s = mMap.get("subject");
            if (s != null && s.contains("wkAction")) {
               Map<String, String> mRequest = new HashMap<String, String>();
               String pairs [];
               // Allow split on comma or semicolon.  Semicolon is preferred 
               // because commas were found to cause issues on email services
               // that have a redirect.
               if (s.contains(";")) {
                  pairs = s.split(";");
               } else {
                  pairs = s.split(",");
               }
               for (int i = 0; i < pairs.length; i++) {
                  String pair = pairs[i];
                  if (pair.contains("=")) {
                     String[] keyValue = pair.split("=");
                     mRequest.put(keyValue[0], keyValue[1]);
                  }
               }

               String inputIdentityRequestId = mRequest.get("reqId");
               String inputWorkflowCaseId = mRequest.get("caseId");
               String workItemId = mRequest.get("wkId");

               WorkItem wi = context.getObjectById(WorkItem.class, workItemId);
               String requesteeName = null;
               if (wi != null)
                  requesteeName = wi.getTargetName();
               else
                  requesteeName = "WorkItem Id:" + workItemId;

               String inputApproverEmail = mMap.get("from");
               inputApproverEmail = inputApproverEmail.substring(
                     inputApproverEmail.indexOf("<") + 1,
                     inputApproverEmail.indexOf(">"));

               String inputAction = mRequest.get("wkAction");
               String inputComments = "Test";

               log.debug("wkId:" + workItemId);
               log.debug("reqId:" + inputIdentityRequestId);
               log.debug("caseId:" + inputWorkflowCaseId);
               log.debug("from" + inputApproverEmail);
               log.debug("action:" + inputAction);

               ApproveDenyWorkItem robotApprover = new ApproveDenyWorkItem();
               Boolean approvalResult = robotApprover.approverOrDenyWorkItem(
                     workItemId, context, inputApproverEmail,
                     inputIdentityRequestId, inputWorkflowCaseId, inputAction,
                     inputComments);

               log.debug("Result:" + approvalResult);
               if (getWorkItemOperation().equals("approved"))
                  incrementArgs(TASK_ARG_TYPE.RequestsApproved);
               else if (getWorkItemOperation().equals("denied"))
                  incrementArgs(TASK_ARG_TYPE.RequestsDenied);
               else if (getWorkItemOperation().equals("ignored"))
                  incrementArgs(TASK_ARG_TYPE.RequestsIgnored);

               if (emailChecker.getProps("notifyApprover").equalsIgnoreCase(
                     "true")) {
                  log.debug("Notifying Approver ");
                  Emailer sendEmail = new Emailer(context, null);
                  EmailTemplate emailTemplate = (EmailTemplate) context
                        .getObjectByName(EmailTemplate.class,
                              "Email Approvals Confirmation");
                  EmailOptions emailopts = new EmailOptions();

                  emailopts.setTo(inputApproverEmail);
                  emailopts.setVariable("emailApprovalResult", approvalResult);
                  emailopts.setVariable("identityDisplayName", requesteeName);

                  log.debug("Notify Admin? "
                        + emailChecker.getProps("notifyadmin"));
                  if (!approvalResult
                        && emailChecker.getProps("notifyadmin")
                              .equalsIgnoreCase("true"))
                     emailopts.setVariable("notifyadminemail",
                           emailChecker.getProps("notifyadminemail"));
                  else
                     emailopts.setVariable("notifyadminemail", null);

                  sendEmail.sendEmailNotification(emailTemplate, emailopts);
               }
            }

            emailChecker.deleteMessageNo(newMessageCtr);
            newMessageCtr--;
         }
      } catch (Throwable t) {
         result.addMessage(new Message(Message.Type.Error, getMessages(), t));
      }
      emailChecker.closeConnection();
      populateTaskResult(result);
   }

   private void populateTaskResult(TaskResult result) {
      if (log.isDebugEnabled()) {
         StringBuffer buffer = new StringBuffer();
         buffer.append("Results: ");
         buffer.append(ARG_OUT_REQUEST_APPROVED + " = " + getRequestsApproved());
         buffer.append("\n");
         buffer.append(ARG_OUT_REQUEST_DENIED + " = " + getRequestsDenied());
         buffer.append("\n");
         buffer.append(ARG_OUT_REQUEST_IGNORED + " = " + getRequestsIgnored());
         buffer.append(ARG_OUT_MESSAGES + " = " + getMessages());
         log.debug(buffer.toString());
      }
      if (getMessages() != null && !getMessages().isEmpty())
         result.addMessage(new Message(Message.Type.Error, getMessages()));
      result.setAttribute(ARG_OUT_REQUEST_APPROVED, getRequestsApproved());
      result.setAttribute(ARG_OUT_REQUEST_DENIED, getRequestsDenied());
      result.setAttribute(ARG_OUT_REQUEST_IGNORED, getRequestsIgnored());
      result.setAttribute(ARG_OUT_MESSAGES, getMessages());

   }

   public TaskMonitor getMonitor() {
      return (this.monitor);
   }

   private void setMonitor(TaskMonitor monitor) {
      this.monitor = monitor;
   }

   public SailPointContext getContext() {
      return (this.context);
   }

   private void setContext(SailPointContext context) {
      this.context = context;
   }

   /**
    * @return the messages
    */
   public String getMessages() {
      return messages;
   }

   /**
    * @param messages
    *           the messages to set
    */
   public static void setMessages(String msgs) {
      messages = msgs;
   }

   public void incrementArgs(TASK_ARG_TYPE type) {
      int index = type.ordinal();
      switch (index) {
      case 0:
         this.requestsApproved++;
         log.trace("Request Approved counter++");
         break;
      case 1:
         this.requestsDenied++;
         log.trace("Request Denied counter++");
         break;
      default:
         this.requestsIgnored++;
         log.trace("Request Ignored counter++");
         break;
      }
   }

   private int getRequestsApproved() {
      return this.requestsApproved;
   }

   private int getRequestsDenied() {
      return this.requestsDenied;
   }

   private int getRequestsIgnored() {
      return this.requestsIgnored;
   }

   public static void setWorkItemOperation(String operation) {
      log.trace("WorkItem operation set to : " + operation);
      workItemOperation = operation;
   }

   public static String getWorkItemOperation() {
      return workItemOperation;
   }

   /**
    * This method is called when IdentityIQ forcefully terminates a custom task.
    * This needs to clean up any threads or database connections established by
    * the custom task.
    */
   @Override
   public boolean terminate() {
      this.term = true;
      return true;
   }

}
