package sailpoint.services.tools.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.catalina.ant.BaseRedirectorHelperTask;
import org.apache.tools.ant.BuildException;
import org.w3c.dom.Element;

import sailpoint.services.tools.docgen.DocumentizerUtils;

/*
 checks if SSB XML files follow naming convention
 @author surya.nellepalli@sailpoint.com
 @TODO
 for some odd reason ANT log statements are not being redirected.
 Reverting to System.out for now
 */

public class VerifyFileNamingConvention extends BaseRedirectorHelperTask {

   // BaseRedirectorHelperTask is from Catalina ANT tasks
   // and is used for adding support for output redirection
   // from ANT task

   private File _location;
   private String _convention;
   private String _ignoreFolderList;
   private boolean checkByObjectName = false;
   private boolean checkByFolderName = false;
   private StringBuffer skippedFiles = new StringBuffer();

   static List<String> ignoreFilePrefix = new ArrayList();

   static {
      // list of file prefix to be ignored
      ignoreFilePrefix.add(".DS");
      ignoreFilePrefix.add(".svn");
      ignoreFilePrefix.add(".git");
      ignoreFilePrefix.add("CVS");
      ignoreFilePrefix.add("SCCS");
      ignoreFilePrefix.add(".hprof");

   }

   // The method executing the task
   public void execute() throws BuildException {

      if (!_location.isDirectory())
         throw new BuildException(
               "VerifyFileNamingConvention: Specify Config Location ");
      if (_convention == null)
         throw new BuildException(
               "VerifyFileNamingConvention: Specify a File Name Pattern");

      if (_convention.indexOf("FOLDER") >= 0)
         checkByFolderName = true;
      if (_convention.indexOf("OBJECT") >= 0)
         checkByObjectName = true;

      System.out
            .println("\nFollowing files do not match Project Naming Conventions <<"
                  + _convention + ">>");
      System.out
            .println("------------------------------------------------------------------------------");

      // String folderPattern= _convention.replaceAll("FOLDER",folderName);
      checkByFolder(_location, _convention);
      System.out
            .println("------------------------------------------------------------------------------");
      System.out.println("Skipped for Name Convention checks");
      System.out
            .println("------------------------------------------------------------------------------");
      System.out.println(skippedFiles.toString());

   }

   public void checkByFolder(File folder, String folderPattern) {
      if (!folder.isDirectory()) {
         System.out.println("Expected Folder " + folder.getAbsolutePath());
         return;
      }

      if (folder == null)
         return;

      if (_ignoreFolderList != null
            && _ignoreFolderList.indexOf(folder.getName()) >= 0) {
         recordSkipped(folder,
               " skipped as this Folder is in ignore folder list");
         return;
      }

      File[] files = folder.listFiles();
      if (files == null)
         return;

      String patternToCheck = new String(folderPattern);
      if (checkByFolderName) {
         patternToCheck = patternToCheck.replaceAll("FOLDER", folder.getName());
      }
      Pattern pattern = Pattern.compile(patternToCheck);
      for (File currentFile : files) {
         String folderName = currentFile.getName();

         if (folderName.startsWith("."))
            continue;

         if (currentFile.isDirectory()) {
            checkByFolder(currentFile, folderPattern);
            continue;
         }
         String currentFileName = currentFile.getName();

         // ignore files like .DS_Store
         for (String ignore : ignoreFilePrefix) {
            if (currentFileName.startsWith(ignore))
               continue;
         }

         if (checkByObjectName) {
            // if check by Object Name
            // find all SailPoint Objects in current file being checked
            // and see if current file name matches file name pattern
            Element currentFileXmlRoot = DocumentizerUtils.getRoot(currentFile);
            if (currentFileXmlRoot == null) {
               recordSkipped(currentFile, "Unable to determine Object Type");
               continue;
            }
            ArrayList<String> spObjects = DocumentizerUtils
                  .getRealRootList(currentFileXmlRoot);
            if (spObjects != null && spObjects.size() > 1) {
               recordSkipped(currentFile,
                     "Pattern specifies OBJECT but has more than one SailPoint Object");
               continue;
            }
            if (spObjects.size() == 0) {
               recordSkipped(currentFile, "Unable to determine Object Type ");

               continue;
            }
            patternToCheck = new String(folderPattern);
            patternToCheck = patternToCheck.replaceAll("OBJECT",
                  spObjects.get(0));
            pattern = Pattern.compile(patternToCheck);

         }

         if (!matchesConvention(currentFileName, pattern)) {
            System.out.println(folder.getAbsolutePath() + File.separator
                  + currentFileName);
         }

      }

   }

   public boolean matchesConvention(String fileName, Pattern pattern) {
      if (pattern.matcher(fileName).matches())
         return true;
      return false;
   }

   protected void recordSkipped(File file, String msg) {
      if (file != null)
         recordSkipped(file.getAbsolutePath(), msg);
   }

   protected void recordSkipped(String fileName, String msg) {
      skippedFiles.append("Skipped ");
      skippedFiles.append(fileName);
      skippedFiles.append(" ");
      skippedFiles.append(msg);
      skippedFiles.append("\n");
   }

   // The setter for the "configLocation" attribute
   public void setConfigLocation(File configLocation) {
      this._location = configLocation;
   }

   // The setter for the "namingConvention" attribute
   public void setNamingConvention(String namingConvention) {
      this._convention = namingConvention;
   }

   // The setter for the "IgnoreFolderList" attribute
   public void setIgnoreFolderList(String ignoreList) {
      this._ignoreFolderList = ignoreList;
   }

}
