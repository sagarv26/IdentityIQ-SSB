package sailpoint.services.standard.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.SailPointContext;
import sailpoint.api.Workflower;
import sailpoint.object.ApprovalItem;
import sailpoint.object.ApprovalSet;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.QueryOptions;
import sailpoint.object.WorkItem;
import sailpoint.object.WorkflowCase;
import sailpoint.tools.GeneralException;

public class ApproveDenyWorkItem {

   private static Log log = LogFactory.getLog(ApproveDenyWorkItem.class);

   public boolean approverOrDenyWorkItem(String workItemId,
         SailPointContext context, String inputApproverEmail,
         String inputIdentityRequestId, String inputWorkflowCaseId,
         String inputAction, String inputComments) {

      log.debug("Inside approveOrDenyWorkItem:" + workItemId);
      boolean result = false;
      if (workItemId != null) {

         try {
            WorkItem item = null;
            if (workItemId != null)
               item = context.getObjectById(WorkItem.class, workItemId);

            if (item != null
                  && inputApproverEmail != null
                  && inputWorkflowCaseId != null
                  && inputAction != null
                  && (WorkItem.Type.Approval.equals(item.getType()) || WorkItem.Type.Form
                        .equals(item.getType())))

            {

               log.debug("Input parameter not null validation and workitem type check passed, Workitem Type:"
                     + item.getType());

               // Get Workflow Case Id
               WorkflowCase workflowCase = item.getWorkflowCase();
               String workflowCaseId = workflowCase.getId();

               // Get Identity Request Id
               String identityRequestId = item.getIdentityRequestId();

               String targetId = item.getTargetId();
               log.debug("TargetID:" + targetId);

               String targetName = item.getTargetName();
               log.debug("TargetName:" + targetName);

               // Get Owner
               Identity owner = item.getOwner();
               // Get the Identity based on email address
               List<String> properties = new ArrayList<String>();
               properties.add("id");
               Identity searchedOwner = null;
               QueryOptions newQo = new QueryOptions();
               newQo.addFilter(Filter.ignoreCase(Filter.eq("email",
                     inputApproverEmail)));
               Iterator<Object[]> iteratorCubes = context.search(
                     Identity.class, newQo, properties);
               if (iteratorCubes != null) {
                  while (iteratorCubes.hasNext()) {
                     Object[] row = (Object[]) iteratorCubes.next();
                     String id = null;
                     if (row != null) {

                        id = (String) row[0];
                     }
                     if (id != null) {
                        searchedOwner = context.getObjectById(Identity.class,
                              id);

                     }

                  }// END WHILE LOOP

               }

               if (owner.isWorkgroup()) {
                  log.debug("Workgroup member Check for approver");
                  Iterator<Identity> iteratorWGCubesMembers = getWorkgroupMembers(
                        context, owner);
                  Identity searchedOwnerWGMembers = null;
                  if (iteratorWGCubesMembers != null) {
                     while (iteratorWGCubesMembers.hasNext()) {
                        // Object[] row = (Object[])
                        // iteratorWGCubesMembers.next();

                        Identity row = iteratorWGCubesMembers.next();
                        String id = null;

                        /*
                         * if(row!=null) {
                         * 
                         * id= (String) row[0]; }
                         */

                        id = row.getId();

                        if (id != null) {
                           searchedOwnerWGMembers = context.getObjectById(
                                 Identity.class, id);
                           if (searchedOwner != null
                                 && searchedOwnerWGMembers != null) {
                              if (searchedOwner.getName().equalsIgnoreCase(
                                    searchedOwnerWGMembers.getName())) {
                                 owner = searchedOwnerWGMembers;
                                 log.debug("approver workgroup member check passed.");
                              }

                           }

                        }

                     }// END WHILE LOOP

                  }

               }

               if (searchedOwner != null) {
                  log.debug("SearchedOwner:" + searchedOwner.getName());
               } else {
                  log.debug("SearchedOwner:null");
               }

               log.debug("Owner:" + owner.getName());

               log.debug("WkCaseID:" + workflowCaseId);
               log.debug("IdentityRequestId:" + identityRequestId);

               // Do all the validation here
               if (searchedOwner != null
                     && owner != null
                     && workflowCaseId != null
                     && searchedOwner.getName().equalsIgnoreCase(
                           owner.getName())
                     && inputWorkflowCaseId.equalsIgnoreCase(workflowCaseId)) {

                  // Approve or Deny Work Item
                  log.debug("Approve/Deny validations passed");
                  result = approveOrReject(context, item, inputComments,
                        inputAction, owner);

               } else {
                  log.debug("Approve/Deny validations failed");
                  CustomJavaMailServerTask.setWorkItemOperation("ignored");
               }
            } else {
               if (item != null) {
                  log.debug("Input parameter not null validation and workitem type check failed , Workitem Type:"
                        + item.getType());
               } else {
                  log.debug("Input parameter not null validation and workitem type check failed , Workitem not found");
               }
               CustomJavaMailServerTask.setWorkItemOperation("ignored");
            }

         } catch (GeneralException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CustomJavaMailServerTask.setMessages(e.getMessage());
         }

      }

      log.debug("Done approveOrDenyWorkItem:" + workItemId);

      return result;

   }

   /**
    * Given an Identity that is a workgroup Iterator over the properties of a
    * groups members. This method does a projection query.
    */
   public Iterator<Identity> getWorkgroupMembers(SailPointContext ctx,
         Identity identity) throws GeneralException {

      List<Identity> workgroups = new ArrayList<Identity>();
      workgroups.add(identity);
      QueryOptions ops = new QueryOptions();
      ops.add(Filter.containsAll("workgroups", workgroups));
      return ctx.search(Identity.class, ops);
   }

   /**
    * Approve a work item.
    */
   private boolean approveOrReject(SailPointContext ctx, WorkItem item,
         String comments, String status, Identity owner) {

      boolean approveOrReject = false;

      try {
         if (item == null)
            log.debug("Invalid WorkItem id");
         else if (item.getState() != null) {
            log.debug("Work item is already completed");
         } else {
            item.setState(WorkItem.State.Finished);
            if (comments != null)
               item.setCompletionComments(comments);

            if (status != null && owner != null) {
               WorkItem.State state = WorkItem.State.Rejected;
               if ("approve".equalsIgnoreCase(status)) {
                  state = WorkItem.State.Finished;
                  CustomJavaMailServerTask.setWorkItemOperation("approved");
               } else if ("reject".equalsIgnoreCase(status)) {
                  state = WorkItem.State.Rejected;
                  CustomJavaMailServerTask.setWorkItemOperation("denied");
               }

               log.debug("Updating approval set");
               ApprovalSet aset = item.getApprovalSet();
               if (aset != null) {
                  List<ApprovalItem> items = aset.getItems();
                  if (items != null) {
                     for (ApprovalItem appitem : items) {
                        if (appitem.getState() == null) {
                           appitem.setState(state);
                           appitem.setApprover(owner.getName());
                        }
                     }
                  }
               }

               // For Form type work item set the state to rejected if not
               // approved for approval set type workitem is set
               // to finished and approvalset items are set.
               if (WorkItem.Type.Form.equals(item.getType())) {
                  log.debug("WorkItem type:Form, Setting state to:"
                        + state.toString());
                  item.setState(state);
               } else {
                  log.debug("WorkItem set state to Finished");
               }

            }

            // note that we can't just save these
            Workflower flower = new Workflower(ctx);
            flower.process(item, true);
            approveOrReject = true;
         }
      } catch (Exception ex) {
         ex.printStackTrace();
         CustomJavaMailServerTask.setMessages(ex.getMessage());
      }
      return approveOrReject;

   }

}