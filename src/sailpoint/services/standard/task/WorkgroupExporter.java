package sailpoint.services.standard.task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import sailpoint.api.ObjectUtil;
import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.Capability;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.Identity.WorkgroupNotificationOption;
import sailpoint.object.QueryOptions;
import sailpoint.object.TaskResult;
import sailpoint.object.TaskSchedule;
import sailpoint.task.AbstractTaskExecutor;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

/**
 * Export workgroups and memberships to a CSV file. This file can then be
 * imported to another environment using the Workgroup Importer task.
 *
 * @author <a href="mailto:paul.wheeler@sailpoint.com">Paul Wheeler</a>
 */

public class WorkgroupExporter extends AbstractTaskExecutor {

   private static final Logger log = Logger.getLogger(WorkgroupExporter.class);

   public static final String EXPORT_FILE = "exportFile";

   private boolean terminate = false;

   @Override
   public void execute(SailPointContext context, TaskSchedule taskSchedule,
         TaskResult taskResult, Attributes<String, Object> attributes)
         throws GeneralException, IOException {

      log.debug("Starting Workgroup Exporter");

      String exportFile = attributes.getString(EXPORT_FILE);

      if (log.isDebugEnabled())
         log.debug("Export file: " + exportFile);

      int totalExported = 0;
      Iterator<Object[]> workgroupIt = null;
      Iterator<Object[]> membersIt = null;

      FileWriter fw = null;
      try {

         List<String> workgroupMemberPropertiesToQuery = new ArrayList<String>();
         workgroupMemberPropertiesToQuery.add("name");

         fw = new FileWriter(exportFile, false);
         fw.write("Workgroup Name,Workgroup Description,Members,Owner,Capabilities,Email,Notification Option\r\n"); // Header

         QueryOptions qo = new QueryOptions();
         qo.addFilter(Filter.eq("workgroup", true));

         workgroupIt = context.search(Identity.class, qo, "name");

         int decacheCounter = 0;

         while (workgroupIt.hasNext()) {
            Object[] thisWorkgroup = workgroupIt.next();
            String workgroupName = (String) thisWorkgroup[0];
            if (log.isDebugEnabled()) {
               log.debug("Processing workgroup: " + workgroupName);
            }
            Identity workgroupIdentity = context.getObjectByName(
                  Identity.class, workgroupName);
            workgroupName = escapeCommas(workgroupName);
            String workgroupDescription = workgroupIdentity.getDescription();
            if (null == workgroupDescription) {
               workgroupDescription = "";
            }
            workgroupDescription = escapeCommas(workgroupDescription);

            List<String> memberList = new ArrayList<String>();
            membersIt = ObjectUtil.getWorkgroupMembers(context,
                  workgroupIdentity, workgroupMemberPropertiesToQuery);

            while (membersIt.hasNext()) {
               Object[] thisMemberList = membersIt.next();
               String memberName = (String) thisMemberList[0];
               if (log.isDebugEnabled()) {
                  log.debug("Processing workgroup member: " + memberName);
               }
               memberList.add(memberName);
            }
            log.debug("Completed adding members to list");

            String memberCsv;
            if (!memberList.isEmpty()) {
               memberCsv = Util.listToCsv(memberList);
            } else {
               memberCsv = "";
            }
            memberCsv = escapeCommas(memberCsv);

            String ownerName;
            Identity ownerIdentity = workgroupIdentity.getOwner();
            if (null != ownerIdentity)
               ownerName = ownerIdentity.getName();
            else
               ownerName = "";

            String capabilityNamesCsv = "";
            List<Capability> capabilities = workgroupIdentity.getCapabilities();
            if (null != capabilities && !capabilities.isEmpty()) {
               List<String> capabilityNames = new ArrayList<String>();
               for (Capability capability : capabilities) {
                  capabilityNames.add(capability.getName());
               }
               capabilityNamesCsv = escapeCommas(Util
                     .listToCsv(capabilityNames));
            }

            String workgroupEmail = workgroupIdentity.getEmail();
            if (null == workgroupEmail)
               workgroupEmail = "";

            String notificationOptionString = "";
            WorkgroupNotificationOption notificationOption = workgroupIdentity
                  .getNotificationOption();

            if (null != notificationOption) {
               notificationOptionString = notificationOption.toString();
            }

            log.debug("Writing record to file");
            fw.write(workgroupName + "," + workgroupDescription + ","
                  + memberCsv + "," + ownerName + "," + capabilityNamesCsv
                  + "," + workgroupEmail + "," + notificationOptionString
                  + "\r\n");
            totalExported++;

            decacheCounter++;
            // Decache every 20 iterations, to avoid hibernate cache bloat
            if (decacheCounter > 19) {
               context.decache();
               decacheCounter = 0;
            }
         }
         log.debug("All workgroups processed");

      } finally {
         if (null != fw)
            fw.close();
         if (workgroupIt != null) {
            Util.flushIterator(workgroupIt);
         }
         if (membersIt != null) {
            Util.flushIterator(membersIt);
         }
         taskResult.setAttribute("totalExported", totalExported);
         log.debug("Exiting Workgroup Exporter");
      }

   }

   @Override
   public boolean terminate() {
      terminate = true;
      return terminate;
   }

   /**
    * Enclose a string in quotes if it contains a comma.
    */
   private String escapeCommas(String text) {
      if (null != text && text.contains(","))
         text = "\"" + text + "\"";
      return text;
   }

}
