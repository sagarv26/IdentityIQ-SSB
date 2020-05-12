package sailpoint.services.standard.task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import sailpoint.api.SailPointContext;
import sailpoint.object.Application;
import sailpoint.object.Attributes;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.QueryOptions;
import sailpoint.object.TaskResult;
import sailpoint.object.TaskSchedule;
import sailpoint.task.AbstractTaskExecutor;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

/**
 * Export manual correlations to a CSV file. This file can then be imported to
 * another environment using the Manual Correlation Importer task.
 *
 * @author <a href="mailto:paul.wheeler@sailpoint.com">Paul Wheeler</a>
 */

public class ManualCorrelationExporter extends AbstractTaskExecutor {

   private static final Logger log = Logger
         .getLogger(ManualCorrelationExporter.class);

   public static final String EXPORT_FILE = "exportFile";
   public static final String APPLICATIONS = "applications";

   private boolean terminate = false;

   @Override
   public void execute(SailPointContext context, TaskSchedule taskSchedule,
         TaskResult taskResult, Attributes<String, Object> attributes)
         throws GeneralException, IOException {

      log.debug("Starting Manual Correlation Exporter");

      String exportFile = attributes.getString(EXPORT_FILE);
      String applications = attributes.getString(APPLICATIONS);

      // Applications are passed in as a csv of IDs
      List<Application> appObjects = new ArrayList<Application>();

      // Put the applications in a list
      if (null != applications && !applications.isEmpty()) {
         List<String> appIds = Util.csvToList(applications);
         for (String appId : appIds) {
            Application app = context.getObject(Application.class, appId);
            appObjects.add(app);
         }
      }

      if (log.isDebugEnabled())
         log.debug("Export file: " + exportFile);

      int totalExported = 0;

      FileWriter fw = null;
      try {
         fw = new FileWriter(exportFile, false);
         fw.write("Application,AccountName,Identity\r\n"); // Write header

         // Create a query for all manually correlated links
         QueryOptions qo = new QueryOptions();
         qo.addFilter(Filter.eq("manuallyCorrelated", true));
         // Filter by the specified applications. If none were specified we will
         // include all.
         if (!appObjects.isEmpty()) {
            qo.addFilter(Filter.in("application", appObjects));
         }

         Iterator<Object[]> it = context.search(Link.class, qo,
               "application,identity,nativeIdentity,id");

         // Iterate through the links and write to file
         while ((null != it) && (it.hasNext())) {
            Object[] thisLink = it.next();
            String appName = ((Application) thisLink[0]).getName();
            if (null == thisLink[1]) {
               throw new GeneralException("Link " + thisLink[3]
                     + " has no associated identity.");
            }
            String identityName = ((Identity) thisLink[1]).getName();
            String acctName = (String) thisLink[2];
            updateProgress(context, taskResult,
                  "Exporting correlation for account " + acctName
                        + " on application " + appName);
            if (log.isDebugEnabled())
               log.debug("Exporting manually correlated account " + acctName
                     + " for identity " + identityName + " on application "
                     + appName);
            fw.write(escapeCommas(appName) + "," + escapeCommas(acctName) + ","
                  + escapeCommas(identityName) + "\r\n"); // Appends string to
                                                          // file
            totalExported++;
         }
      } finally {
         if (null != fw)
            fw.close();
         taskResult.setAttribute("totalExported", totalExported);
         log.debug("Exiting Manual Correlation Exporter");
      }

   }

   /**
    * Enclose a string in quotes if it contains a comma.
    */
   private String escapeCommas(String text) {
      if (null != text && text.contains(","))
         text = "\"" + text + "\"";
      return text;
   }

   @Override
   public boolean terminate() {
      terminate = true;
      return terminate;
   }

}
