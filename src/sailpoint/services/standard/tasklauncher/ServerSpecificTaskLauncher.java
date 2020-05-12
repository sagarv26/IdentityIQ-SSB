package sailpoint.services.standard.tasklauncher;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import sailpoint.api.SailPointContext;
import sailpoint.api.TaskManager;
import sailpoint.object.Attributes;
import sailpoint.object.Custom;
import sailpoint.object.Filter;
import sailpoint.object.QueryOptions;
import sailpoint.object.Request;
import sailpoint.object.Server;
import sailpoint.object.ServiceDefinition;
import sailpoint.object.TaskDefinition;
import sailpoint.object.TaskItemDefinition;
import sailpoint.object.TaskResult;
import sailpoint.object.TaskSchedule;
import sailpoint.task.AbstractTaskExecutor;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

/**
 * Launch a task on a specific server or servers. For a single-server
 * non-partitioned task, this task creates a Custom object that gets detected by
 * a server running the TaskLauncherService which runs the task if the Custom
 * object name starts with the server name. For multi-server or partitioned
 * tasks this task temporarily modifies the 'Task' and 'Request'
 * ServiceDefinition objects so that they list only the server(s) specified. For
 * these cases it needs to wait 60 seconds before starting as the Servicer only
 * polls for updates to the ServiceDefinition object with this frequency.
 * 
 * @author <a href="mailto:paul.wheeler@sailpoint.com">Paul Wheeler</a>
 */

public class ServerSpecificTaskLauncher extends AbstractTaskExecutor {

   private static final Logger log = Logger
         .getLogger(ServerSpecificTaskLauncher.class);

   public static final String TASK_DEFINITION = "taskDefinition";
   public static final String SERVERS = "servers";
   public static final String TASK_START_TIMEOUT = "taskStartTimeout";
   public static final String PARTITIONED_TASK_FINISH_TIMEOUT = "partitionedTaskFinishTimeout";
   public static final String INCLUDE_MAINTENANCE_PARTITIONS = "includeMaintenancePartitions";

   private TaskResult launcherTaskResult;
   private boolean terminate = false;
   private boolean launched = false;

   @Override
   public void execute(SailPointContext context, TaskSchedule taskSchedule,
         TaskResult taskResult, Attributes<String, Object> attributes)
         throws GeneralException, InterruptedException {
      if (log.isDebugEnabled())
         log.debug("Starting Server-Specific Task Launcher");

      launcherTaskResult = taskResult;
      String taskDefinition = attributes.getString(TASK_DEFINITION);
      String servers = attributes.getString(SERVERS);
      String taskStartTimeout = attributes.getString(TASK_START_TIMEOUT);
      String partitionedTaskFinishTimeout = attributes
            .getString(PARTITIONED_TASK_FINISH_TIMEOUT);
      String includeMaintenancePartitions = attributes
            .getString(INCLUDE_MAINTENANCE_PARTITIONS);

      String initialTaskHosts = null;
      String initialRequestHosts = null;
      ServiceDefinition sdTask = null;
      ServiceDefinition sdRequest = null;

      // taskStartTimeout is the number of seconds we wait for the task to start
      // before switching back the ServiceDefinition values
      if (null == taskStartTimeout) {
         taskStartTimeout = "120";
      }
      // partitionedTaskFinishTimeout is the number of seconds we wait for a
      // partitioned task to finish assigning hosts before switching back the
      // ServiceDefinition values
      if (null == partitionedTaskFinishTimeout) {
         partitionedTaskFinishTimeout = "7200";
      }

      TaskDefinition taskDefinitionObj = context.getObject(TaskDefinition.class, taskDefinition);

      String taskName = taskDefinitionObj.getName();

      // The servers are passed in as a csv of IDs. Need a list of server names.
      List<String> serverNamesList = new ArrayList<String>();
      // Put the server names in a list
      if (null != servers && !servers.isEmpty()) {
         List<String> serverIds = Util.csvToList(servers);
         for (String serverId : serverIds) {
        	    String serverName = context.getObject(Server.class, serverId).getName();
            serverNamesList.add(serverName);
         }
      }
      String serverNames = Util.listToCsv(serverNamesList);
      serverNames = serverNames.replaceAll("\\s", "");

      if (log.isDebugEnabled()) {
         log.debug("Task to run: " + taskName);
         log.debug("Run on server(s): " + serverNames);
         log.debug("Task start timeout: " + taskStartTimeout);
         log.debug("Partitioned task finish timeout: "
               + partitionedTaskFinishTimeout);
         log.debug("Include maintenance partitions: "
               + includeMaintenancePartitions);
      }

      List<String> launchHosts = new ArrayList<String>();

      String enablePartitioning = (String) taskDefinitionObj
            .getArgument("enablePartitioning");
      if (log.isDebugEnabled())
         log.debug("enablePartitioning: " + enablePartitioning);

      if (!"true".equals(enablePartitioning) && serverNamesList.size() == 1) {
         // Single server selected, and partitioning not enabled. Use the
         // TaskLauncher service and a Custom object to trigger on the selected
         // server.

         if (log.isDebugEnabled())
            log.debug("Single server and no partitioning");

         String serverName = serverNamesList.get(0);
         createCustomTrigger(context, serverName, taskName);
         if (log.isDebugEnabled())
            log.debug("Custom trigger object created for server " + serverName);

         String launchResult = "Task '" + taskName
               + "' will be launched on server " + serverName;
         launcherTaskResult.setAttribute("launchResult", launchResult);

      } else {

         // Multiple servers selected and/or partitioning enabled. Need to
         // temporarily modify the Task and Request ServiceDefinitions.

         // First, check to see if another task is running, which means the
         // existing ServiceDefinition hosts will not have their 'normal'
         // values. In order to get these values so that we can put them back at
         // the end we need to wait until the other launcher task has finished.

         QueryOptions qo = new QueryOptions();
         qo.addFilter(Filter.eq("definition.parent", taskResult.getDefinition()
               .getParent()));
         qo.addFilter(Filter.isnull("completed"));

         if (context.countObjects(TaskResult.class, qo) > 1) {
            updateProgress(
                  context,
                  launcherTaskResult,
                  "Waiting for an existing instance of the task launcher to finish before launching task '"
                        + taskName + "'.");
            if (log.isDebugEnabled())
               log.debug("Waiting for an existing instance of the task launcher to finish before launching task '"
                     + taskName + "'.");
         }

         while (context.countObjects(TaskResult.class, qo) > 1 && !terminate) {
            Thread.sleep(1000);
         }

         sdTask = context.getObjectByName(ServiceDefinition.class, "Task");
         sdRequest = context
               .getObjectByName(ServiceDefinition.class, "Request");
         initialTaskHosts = sdTask.getHosts();
         initialRequestHosts = sdRequest.getHosts();

         if (!terminate) {
            try {

               if (log.isDebugEnabled()) {
                  log.debug("Task hosts initial value " + initialTaskHosts);
                  log.debug("Request hosts initial value "
                        + initialRequestHosts);
               }
               // Temporarily modify the hosts in the Task and Request
               // ServiceDefinition objects.
               if (!initialTaskHosts.equals(serverNames)) {
                  if (log.isDebugEnabled())
                     log.debug("Setting Task hosts to " + serverNames);
                  sdTask.setHosts(serverNames);
                  context.saveObject(sdTask);
                  context.commitTransaction();
               }
               if (!initialRequestHosts.equals(serverNames)) {
                  if (log.isDebugEnabled())
                     log.debug("Setting Request hosts to " + serverNames);
                  sdRequest.setHosts(serverNames);
                  context.saveObject(sdRequest);
                  context.commitTransaction();
               }

               // ServiceDefinitions for Task and Request are only polled every
               // 60 seconds so we need to pause here.
               if (log.isDebugEnabled())
                  log.debug("Pausing for 60 seconds to allow the Servicer to poll the ServiceDefinitions for the new host values...");

               // Do this as a loop so it can be terminated if needed.
               for (int i = 60; i > 0; i--) {
                  updateProgress(context, launcherTaskResult,
                        "Pausing to allow refresh of ServiceDefinition hosts.  Launching task '"
                              + taskName + "' in " + i + " seconds.");
                  if (!terminate) {
                     Thread.sleep(1000);
                  } else {
                     break;
                  }
               }

               if (!terminate) {
                  if (log.isDebugEnabled()) {
                     log.debug("... and we're back.");
                     log.debug("Scheduling task '" + taskName + "' to run now.");
                  }

                  updateProgress(context, launcherTaskResult,
                        "Scheduling task '" + taskName + "' to run now.");
                  TaskManager tm = new TaskManager(context);
                  Attributes<String, Object> atts = new Attributes<String, Object>();

                  // Launch the task
                  TaskSchedule schedule = tm
                        .run(context.getObjectByName(TaskDefinition.class,
                              taskName), atts);

                  TaskResult result = null;
                  // Wait until we have a TaskResult
                  if (log.isDebugEnabled())
                     log.debug("Waiting for a TaskResult to be created");
                  while (result == null && !terminate) {
                     result = schedule.getLatestResult();
                     if (result == null) {
                        Thread.sleep(100);
                     }
                  }
                  if (log.isDebugEnabled())
                     log.debug("TaskResult '" + result.getName() + "' created");

                  if (!"true".equals(enablePartitioning)) {
                     launchHosts.add(result.getHost());
                  }

                  String trName = result.getName();
                  TaskResult res = null;
                  Date launchedTimestamp;

                  int count = 0;

                  while ((!launched)
                        && count < Integer.parseInt(taskStartTimeout)
                        && !terminate) {

                     updateProgress(context, launcherTaskResult,
                           "Waiting for task '" + taskName + "' to launch.");
                     if (log.isDebugEnabled())
                        log.debug("Waiting for task '" + taskName
                              + "' to launch.");

                     res = context.getObjectByName(TaskResult.class, trName);
                     launchedTimestamp = res.getLaunched();

                     if ("true".equals(enablePartitioning)) {
                        if (null != res.getAttribute("taskResultPartitions")
                              || null != res.getCompletionStatus()) {
                           launched = true;
                           updateProgress(context, launcherTaskResult,
                                 "Partitioned task '" + taskName
                                       + "' has been launched.");
                           if (log.isDebugEnabled())
                              log.debug("Partitioned task '" + taskName
                                    + "' has been launched.");
                        }

                     } else {
                        if (null != launchedTimestamp) {
                           launched = true;
                           updateProgress(context, launcherTaskResult, "Task '"
                                 + taskName + "' has been launched.");
                           if (log.isDebugEnabled())
                              log.debug("Task '" + taskName
                                    + "' has been launched.");
                        }
                     }
                     if (!launched) {
                        Thread.sleep(1000);
                        count++;
                        context.decache(res);
                     }

                  }

                  if (launched && "true".equals(enablePartitioning)
                        && !terminate) {
                     // Partitioned tasks create Request objects that we need to
                     // monitor to determine whether they have their hosts
                     // assigned yet.

                     if (log.isDebugEnabled())
                        log.debug("Monitoring partitions for host assignment");
                     qo = new QueryOptions();
                     qo.addFilter(Filter.eq("taskResult.name", res.getName()));
                     if (TaskItemDefinition.Type.AccountAggregation.equals(res
                           .getType())) {
                        // For account aggregations, unless we specify that
                        // maintenance partitions will be included we'll only
                        // track phase 2 Request objects, which are the ones
                        // that do the aggregation work because we don't want to
                        // wait for the "Finish Aggregation" and
                        // "Check Deleted Objects" partitions which are
                        // processed at the end.
                        if (!("true".equals(includeMaintenancePartitions))) {
                           qo.addFilter(Filter.eq("phase", 2));
                        }
                     }
                     if (TaskItemDefinition.Type.Identity.equals(res.getType())) {
                        // For role propagation tasks, unless we specify that
                        // maintenance partitions will be included we won't
                        // track the Finish Role Propagation partition.
                        if (!("true".equals(includeMaintenancePartitions))) {
                           qo.addFilter(Filter.ne("name",
                                 "Finish Role Propagation"));
                        }
                     }

                     List<TaskResult> partitionResults = res
                           .getPartitionResults();
                     if (null != partitionResults
                           && !partitionResults.isEmpty()) {
                        boolean waitingForHosts = true;
                        count = 0;
                        if (log.isDebugEnabled())
                           log.debug("Waiting for hosts to be assigned to partitions");
                        while (waitingForHosts
                              && count < Integer
                                    .parseInt(partitionedTaskFinishTimeout)
                              && !terminate) {
                           boolean gotAllHosts = true;

                           Iterator<Object[]> it = context.search(
                                 Request.class, qo, "name,host");
                           while ((null != it) && (it.hasNext()) && !terminate) {
                              Object[] thisReq = it.next();
                              String reqName = (String) thisReq[0];
                              String reqHost = (String) thisReq[1];
                              if (log.isDebugEnabled())
                                 log.debug("Request '" + reqName
                                       + "' has host " + reqHost);
                              if (null == reqHost) {
                                 gotAllHosts = false;
                                 break;
                              } else {
                                 if (!launchHosts.contains(reqHost)) {
                                    launchHosts.add(reqHost);
                                 }
                              }
                           }
                           sailpoint.tools.Util.flushIterator(it);
                           // }
                           if (gotAllHosts) {
                              waitingForHosts = false;
                              if (log.isDebugEnabled())
                                 log.debug("All monitored partitions have a host assigned for task '"
                                       + taskName + "'.");
                              updateProgress(context, launcherTaskResult,
                                    "All monitored partitions have a host assigned for task '"
                                          + taskName + "'.");
                           } else {
                              Thread.sleep(1000);

                              res = context.getObjectByName(TaskResult.class,
                                    trName);
                              partitionResults = res.getPartitionResults();
                              updateProgress(context, launcherTaskResult,
                                    "Waiting for partition host assignment to complete for task  '"
                                          + taskName + "'.");
                              count++;
                           }
                        }
                        if (!(count < Integer
                              .parseInt(partitionedTaskFinishTimeout))) {
                           log.warn("Partition processing timed out.  You may need to adjust the value of partitionedTaskFinishTimeout.");
                        }
                     }
                  }

                  String launchResult = "Task '" + taskName + "' was launched";
                  if (!launchHosts.isEmpty()) {
                     launchResult = launchResult + " on server(s): "
                           + Util.listToCsv(launchHosts);
                  }
                  launcherTaskResult.setAttribute("launchResult", launchResult);
                  if (log.isDebugEnabled())
                     log.debug(launchResult);

               }

            } finally {
               if (log.isDebugEnabled())
                  log.debug("Setting Task hosts back to " + initialTaskHosts);
               if (!initialTaskHosts.equals(serverNames)) {
                  sdTask.setHosts(initialTaskHosts);
                  context.saveObject(sdTask);
                  context.commitTransaction();
               }
               if (log.isDebugEnabled())
                  log.debug("Setting Request hosts back to "
                        + initialRequestHosts);
               if (!initialRequestHosts.equals(serverNames)) {
                  sdRequest.setHosts(initialRequestHosts);
                  context.saveObject(sdRequest);
                  context.commitTransaction();
               }
            }
         }

         if (log.isDebugEnabled())
            log.debug("Exiting Server-Specific Task Launcher");
      }

   }

   /*
    * Create the Custom object that gets processed by the TaskLauncher service.
    */
   public static final void createCustomTrigger(SailPointContext context,
         String serverName, String taskDefinitionName) throws GeneralException {

      Server server = context.getObject(Server.class, serverName);
      if (server == null)
         throw new GeneralException("Server '" + serverName + "' not found.");

      TaskDefinition taskDefinition = context.getObject(TaskDefinition.class,
            taskDefinitionName);
      if (taskDefinition == null)
         throw new GeneralException("Task defintion '" + taskDefinitionName
               + "' not found.");

      String customObjectTriggerName = serverName + "_launchtask_"
            + UUID.randomUUID();

      Custom customTrigger = context.getObjectByName(Custom.class,
            customObjectTriggerName);
      if (customTrigger == null) {
         customTrigger = new Custom();

         customTrigger.setName(customObjectTriggerName);
         customTrigger.put("server", server.getName());
         customTrigger.put("task", taskDefinition.getName());
         customTrigger.put("start", new Date());

         context.saveObject(customTrigger);
         context.commitTransaction();
         context.decache();

      } else {
         throw new GeneralException("Trigger for task '"
               + taskDefinition.getName() + " on server '" + server.getName()
               + "' has already been created and has not been processed yet.");
      }

   }

   @Override
   public boolean terminate() {
      terminate = true;
      launcherTaskResult.setTerminated(true);
      if (log.isDebugEnabled())
         log.debug("Task was terminated.");
      return true;
   }

}
