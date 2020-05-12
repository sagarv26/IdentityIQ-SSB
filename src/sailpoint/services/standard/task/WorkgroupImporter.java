package sailpoint.services.standard.task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import sailpoint.api.ObjectUtil;
import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.Capability;
import sailpoint.object.Identity;
import sailpoint.object.Identity.WorkgroupNotificationOption;
import sailpoint.object.TaskResult;
import sailpoint.object.TaskSchedule;
import sailpoint.task.AbstractTaskExecutor;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

/**
 * Import workgroups and their memberships from a CSV file. This can be a file
 * exported using the Workgroup Export task or created manually in the correct
 * format. Each row must have Workgroup Name, Workgroup Description, Members
 * (csv in quotes), Capabilities, Email, Notification Option in that order, with
 * a header on the first row. There are options on the TaskDefinition to define
 * whether to import some of these properties.
 *
 * @author <a href="mailto:paul.wheeler@sailpoint.com">Paul Wheeler</a>
 */

public class WorkgroupImporter extends AbstractTaskExecutor {

   private static final Logger log = Logger.getLogger(WorkgroupImporter.class);

   public static final String IMPORT_FILE = "importFile";
   public static final String DO_NOT_CREATE = "doNotCreate";
   public static final String IGNORE_MEMBERS = "ignoreMembers";
   public static final String IGNORE_DESCRIPTION = "ignoreDescription";
   public static final String IGNORE_CAPABILITIES = "ignoreCapabilities";
   public static final String IGNORE_OWNER = "ignoreOwner";
   public static final String IGNORE_EMAIL = "ignoreEmail";
   public static final String IGNORE_NOTIFICATION_OPTION = "ignoreNotificationOption";

   private boolean terminate = false;

   private int totalProcessed = 0;
   private int totalUpdated = 0;
   private int totalCreated = 0;
   private int totalIgnored = 0;
   private List<String> identitiesNotFoundList = new ArrayList<String>();

   @Override
   public void execute(SailPointContext context, TaskSchedule taskSchedule,
         TaskResult taskResult, Attributes<String, Object> attributes)
         throws GeneralException, IOException {

      log.debug("Starting Workgroup Importer");

      String importFile = attributes.getString(IMPORT_FILE);
      boolean doNotCreate = attributes.getBoolean(DO_NOT_CREATE);
      boolean ignoreDescription = attributes.getBoolean(IGNORE_DESCRIPTION);
      boolean ignoreMembers = attributes.getBoolean(IGNORE_MEMBERS);
      boolean ignoreCapabilities = attributes.getBoolean(IGNORE_CAPABILITIES);
      boolean ignoreOwner = attributes.getBoolean(IGNORE_OWNER);
      boolean ignoreEmail = attributes.getBoolean(IGNORE_EMAIL);
      boolean ignoreNotificationOption = attributes
            .getBoolean(IGNORE_NOTIFICATION_OPTION);

      if (log.isDebugEnabled()) {
         log.debug("Import file: " + importFile);
         log.debug("Do not create: " + doNotCreate);
         log.debug("Ignore description: " + ignoreDescription);
         log.debug("Ignore members: " + ignoreMembers);
         log.debug("Ignore capabilities: " + ignoreCapabilities);
         log.debug("Ignore owner: " + ignoreOwner);
         log.debug("Ignore email: " + ignoreEmail);
         log.debug("Ignore notification option: " + ignoreNotificationOption);
      }

      BufferedReader br = new BufferedReader(new FileReader(importFile));
      try {
         String line = br.readLine(); // First line is the header
         if (line != null) {
            line = br.readLine();
         }
         int counter = 0;
         while (line != null) {
            if (!line.equals("")) {
               List<String> attrs = Util.csvToList(line);
               String workgroupName = attrs.get(0);
               Identity workgroup = null;
               String membersCsv = null;
               boolean isCreate = false;
               if (null != workgroupName && !workgroupName.equals("")) {
                  String workgroupDescription = attrs.get(1);
                  membersCsv = attrs.get(2);
                  String ownerName = attrs.get(3);
                  String capabilitiesCsv = attrs.get(4);
                  String email = attrs.get(5);
                  String notificationOption = attrs.get(6);
                  updateProgress(context, taskResult, "Processing workgroup "
                        + workgroupName);

                  if (log.isDebugEnabled()) {
                     log.debug("Processing row with workgroup name: "
                           + workgroupName);
                  }

                  // Get the workgroup if it exists
                  workgroup = context.getObjectByName(Identity.class,
                        workgroupName);
                  if (null != workgroup) {
                     if (!workgroup.isWorkgroup()) {
                        throw new GeneralException(workgroup.getName()
                              + " already exists as an Identity object.");
                     } else {
                        // Workgroup exists so we need to modify it
                        if (log.isDebugEnabled())
                           log.debug("Workgroup exists");
                     }
                  } else if (!doNotCreate) {
                     if (log.isDebugEnabled())
                        log.debug("Workgroup does not exist - creating new workgroup "
                              + workgroupName);
                     // Workgroup does not exist so create one and set basic
                     // properties
                     workgroup = new Identity();
                     workgroup.setName(workgroupName);
                     workgroup.setWorkgroup(true);
                     isCreate = true;
                     totalCreated++;
                  } else {
                     log.debug("Will not create new workgroup " + workgroupName);
                     totalIgnored++;
                     totalProcessed++;
                     line = br.readLine();
                     continue;
                  }

                  // If it's an existing workgroup, try to acquire a lock
                  if (!isCreate) {
                     Identity lockedWorkgroup = acquireIdentityLock(context,
                           workgroupName, "Workgroup import", 10, 3);
                     if (null == lockedWorkgroup) {
                        throw new GeneralException(
                              "Unable to acquire lock on workgroup "
                                    + workgroupName);
                     }
                  }

                  if (!ignoreDescription) {
                     log.debug("Setting description to: "
                           + workgroupDescription);
                     workgroup.setDescription(workgroupDescription);
                  }
                  if (!ignoreCapabilities) {
                     List<Capability> capabilitiesList = new ArrayList<Capability>();
                     if (null != capabilitiesCsv) {
                        List<String> capabilities = Util
                              .csvToList(capabilitiesCsv);
                        for (String capabilityName : capabilities) {
                           Capability capability = context.getObjectByName(
                                 Capability.class, capabilityName);
                           if (null != capability) {
                              capabilitiesList.add(capability);
                           } else {
                              log.warn("Capability " + capabilityName
                                    + " does not exist");
                           }
                        }
                     }
                     log.debug("Setting capabilities to: " + capabilitiesList);
                     workgroup.setCapabilities(capabilitiesList);
                  }
                  if (!ignoreOwner) {
                     Identity ownerIdentity = null;
                     if (null != ownerName) {
                        ownerIdentity = context.getObjectByName(Identity.class,
                              ownerName);
                        log.debug("Owner is " + ownerName);
                     }
                     log.debug("Setting owner to: " + ownerName);
                     workgroup.setOwner(ownerIdentity);
                  }

                  if (!ignoreEmail) {
                     log.debug("Setting email to: " + email);
                     workgroup.setEmail(email);
                  }

                  if (!ignoreNotificationOption) {
                     if (null != notificationOption) {
                        WorkgroupNotificationOption wno = WorkgroupNotificationOption
                              .valueOf(notificationOption);
                        log.debug("Setting notification option to: "
                              + notificationOption);
                        workgroup.setNotificationOption(wno);
                     } else {
                        log.debug("Setting notification option to null");
                        workgroup.setNotificationOption(null);
                     }
                  }

                  // Save the workgroup
                  context.saveObject(workgroup);
                  context.commitTransaction();

                  if (!isCreate) {
                     ObjectUtil.unlockIdentity(context, workgroup);
                  }

               }

               // Set the workgroup membership
               if (!ignoreMembers) {
                  List<String> oldMemberList = new ArrayList<String>();
                  Iterator<java.lang.Object[]> memberIt = ObjectUtil
                        .getWorkgroupMembers(context, workgroup, null);
                  while (memberIt.hasNext()) {
                     Object[] members = (Object[]) memberIt.next();
                     int identityCounter = 0;
                     for (int j = 0; j < members.length; j++) {
                        Identity member = (Identity) members[j];
                        oldMemberList.add(member.getName());
                        if (identityCounter > 19) {
                           context.decache(member);
                           identityCounter = 0;
                        }
                     }
                  }

                  List<String> memberList = Util.csvToList(membersCsv);
                  if (log.isDebugEnabled())
                     log.debug("Old members: " + oldMemberList.toString());
                  if (log.isDebugEnabled())
                     log.debug("New members: " + memberList.toString());

                  oldMemberList.removeAll(memberList);

                  if (log.isDebugEnabled())
                     log.debug("Members to remove: " + oldMemberList.toString());

                  // Add workgroup membership to each identity in the list
                  for (Object memberNameObject : memberList) {
                     String memberName = (String) memberNameObject;
                     Identity member = context.getObjectByName(Identity.class,
                           memberName);
                     if (null != member) {
                        List<Identity> workgroups = member.getWorkgroups();
                        if (!workgroups.contains(workgroup)) {
                           if (log.isDebugEnabled())
                              log.debug(member.getName()
                                    + " will be added to workgroup "
                                    + workgroup.getName());
                           workgroups.add(workgroup);

                           // Try to acquire lock
                           Identity lockedIdentity = acquireIdentityLock(
                                 context, member.getName(), "Workgroup import",
                                 10, 3);
                           if (null == lockedIdentity) {
                              throw new GeneralException(
                                    "Unable to acquire lock on identity "
                                          + member.getName());
                           }
                           member.setWorkgroups(workgroups);
                           context.saveObject(member);
                           context.commitTransaction();
                           ObjectUtil.unlockIdentity(context, member);
                        } else {
                           if (log.isDebugEnabled())
                              log.debug(member.getName()
                                    + " is already a member of workgroup "
                                    + workgroup.getName());
                        }
                     } else {
                        if (log.isDebugEnabled())
                           log.debug("Identity " + memberName
                                 + " was not found");
                        if (!identitiesNotFoundList.contains(memberName)) {
                           identitiesNotFoundList.add(memberName);
                        }
                     }
                  }

                  // Remove workgroup membership from each identity no longer in
                  // the list
                  for (Object memberNameObject : oldMemberList) {
                     String memberName = (String) memberNameObject;
                     Identity member = context.getObjectByName(Identity.class,
                           memberName);
                     if (null != member) {
                        List<Identity> workgroups = member.getWorkgroups();
                        if (workgroups.contains(workgroup)) {
                           if (log.isDebugEnabled())
                              log.debug(member.getName()
                                    + " will be removed from workgroup "
                                    + workgroup.getName());
                           workgroups.remove(workgroup);

                           // Try to acquire lock
                           Identity lockedIdentity = acquireIdentityLock(
                                 context, member.getName(), "Workgroup import",
                                 10, 3);
                           if (null == lockedIdentity) {
                              throw new GeneralException(
                                    "Unable to acquire lock on identity "
                                          + member.getName());
                           }
                           member.setWorkgroups(workgroups);
                           context.saveObject(member);
                           context.commitTransaction();
                           ObjectUtil.unlockIdentity(context, member);
                        }
                     }
                  }
               }
               if (!isCreate)
                  totalUpdated++;
               totalProcessed++;

            }
            counter++;
            if (counter > 19) {
               // Decache every 20 records to avoid hibernate cache bloat
               context.decache();
               counter = 0;
            }
            line = br.readLine();
         }

      } finally {
         taskResult.setAttribute("totalProcessed", totalProcessed);
         taskResult.setAttribute("totalUpdated", totalUpdated);
         taskResult.setAttribute("totalCreated", totalCreated);
         taskResult.setAttribute("totalIgnored", totalIgnored);
         int identitiesNotFoundTotal = identitiesNotFoundList.size();
         String identitiesNotFound = Integer.toString(identitiesNotFoundTotal);
         if (identitiesNotFoundTotal > 0) {
            String separator;
            for (int i = 0; i < identitiesNotFoundTotal; i++) {
               if (i == 0) {
                  separator = ": ";
               } else {
                  separator = ", ";
               }
               identitiesNotFound = identitiesNotFound + separator
                     + identitiesNotFoundList.get(i);
            }
         }
         taskResult.setAttribute("identitiesNotFound", identitiesNotFound);
         br.close();
         log.debug("Exiting Workgroup Importer");
      }

   }

   /**
    * A helper function that attempts to acquire a lock on an Identity. It will
    * wait for 'waitSecs' for any existing locks to go away before giving up an
    * attempt, and it will re-attempt 'retryTimes' before giving up entirely. On
    * a successful lock it will return a valid sailpoint.object.Identity
    * reference. If it fails to acquire a lock then it will return a null
    * Identity reference to the caller and it will display various messages in
    * the log file. After the last re-attempt it will give up and log a full
    * stack trace allowing system administrators to review the issue. The
    * 'lockName' argument is an option string that can describe the process that
    * is acquiring the lock on the Identity. If null or an empty string is
    * passed for this then the host + thread name will be substituted in for the
    * value of 'lockName'.
    * 
    * @throws UnknownHostException
    * @throws GeneralException
    */
   public Identity acquireIdentityLock(SailPointContext context,
         String identityId, String lockName, int waitSecs, int retryTimes)
         throws UnknownHostException, GeneralException {

      // Sanity check the arguments passed in, to prevent irrationally short
      // calls.
      if (retryTimes <= 0)
         retryTimes = 1;
      if (waitSecs <= 0)
         waitSecs = 5;

      // Make sure we've been asked to lock a valid Identity.
      Identity idToLock = context.getObjectById(Identity.class, identityId);
      if (null == idToLock) {
         idToLock = context.getObjectByName(Identity.class, identityId);
      }
      if (null == idToLock) {
         log.error("Could not find an Identity to lock matching: ["
               + identityId + "]");
         return null;
      }

      int numLockRetries = 0;
      Identity lockedId = null;

      // If no lock name was passed in then create one that's descriptive of
      // the host and thread that acquired the lock came from.
      if ((lockName instanceof String) && (0 == lockName.length())) {
         lockName = null;
      }
      if (null == lockName) {
         String hostName = java.net.InetAddress.getLocalHost().getHostName();
         Long threadId = Thread.currentThread().getId();
         String threadName = Thread.currentThread().getName();
         lockName = "host:[" + hostName + "], thread ID:[" + threadId
               + "], thread:[" + threadName + "]";
      }

      while ((lockedId == null) && (numLockRetries < retryTimes)) {

         try {

            // Attempt to acquire a lock in the object.
            lockedId = sailpoint.api.ObjectUtil.lockIdentity(context,
                  identityId, lockName, waitSecs);

         } catch (sailpoint.api.ObjectAlreadyLockedException ex) {

            // Let's see who's got this object currently locked.
            String lockString = idToLock.getLock();
            if ((null == lockString) || (0 == lockString.length())) {
               lockString = "unspecified";
            }

            // Log the stack trace on the final attempt to retry.
            if (numLockRetries == (retryTimes - 1)) {
               String eMsg = "Failed to acquire lock on Identity ["
                     + identityId + "], lock held by: [" + lockString + "]";
               log.error(eMsg, ex);
            } else {
               String wMsg = "Timeout acquiring lock on Identity ["
                     + identityId + "], lock held by: [" + lockString
                     + "], retrying.";
               log.warn(wMsg);
            }

         }
         numLockRetries++;
      }
      return lockedId;
   }

   @Override
   public boolean terminate() {
      terminate = true;
      return terminate;
   }

}
