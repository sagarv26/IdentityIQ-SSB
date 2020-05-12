package sailpoint.services.standard.tasklauncher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.SailPointContext;
import sailpoint.api.TaskManager;
import sailpoint.object.Custom;
import sailpoint.object.Filter;
import sailpoint.object.Filter.MatchMode;
import sailpoint.object.QueryOptions;
import sailpoint.object.TaskDefinition;
import sailpoint.object.TaskResult;
import sailpoint.server.Service;
import sailpoint.tools.GeneralException;

public class TaskLauncherService extends Service {
   public static final Log log = LogFactory.getLog(TaskLauncherService.class);
   public static final String NAME = "TaskLauncher";

   @Override
   public void execute(SailPointContext context) throws GeneralException {

      String customNamePrefix = sailpoint.tools.Util.getHostName()
            + "_launchtask_";

      if (log.isDebugEnabled())
         log.debug("Looking for work to do: " + customNamePrefix);

      QueryOptions qo = new QueryOptions(Filter.like("name", customNamePrefix,
            MatchMode.START));

      int count = context.countObjects(Custom.class, qo);

      if (count > 0) {

         Iterator<Object[]> it = context.search(Custom.class, qo, "id");
         // The result set may get closed in between iterations so we will grab
         // all the values into memory and iterate through those...

         List<String> ids = new ArrayList<String>();
         while (it.hasNext()) {
            ids.add((String) it.next()[0]);
         }

         Iterator<String> idsIt = ids.iterator();

         while (idsIt.hasNext()) {

            String id = idsIt.next();
            Custom custom = context.getObjectById(Custom.class, id);
            String taskName = custom.getString("task");

            TaskDefinition taskDefinition = context.getObject(
                  TaskDefinition.class, taskName);
            if (taskDefinition == null)
               throw new GeneralException("Could not find TaskDefintion for '"
                     + taskName + "'.");

            if (log.isDebugEnabled())
               log.debug("Attempting to run task name " + taskName);

            TaskManager tm = new TaskManager(context);
            tm.setLauncher(context.getUserName());
            TaskResult result = tm.runSync(taskName, null);
            log.debug("Task result: " + result.toXml());

            // remove the request object, we don't need that anymore
            log.debug("Removing custom object");
            context.removeObject(custom);
            context.commitTransaction();
            log.debug("Committed...");
         }

      } else {

         log.debug("Nothing to do...");
      }

   }

}
