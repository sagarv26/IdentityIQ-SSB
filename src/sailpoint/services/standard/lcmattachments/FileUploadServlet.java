package sailpoint.services.standard.lcmattachments;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Custom;
import sailpoint.object.WorkItem;
import sailpoint.object.WorkflowCase;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

/**
 * LCM Attachments.
 * 
 * Add attachments to an LCM request, using a shared file system.  
 * Currently, only access requests are supported.
 * 
 * @version 1.0
 */

public class FileUploadServlet extends HttpServlet {

   private static Log log = LogFactory.getLog(FileUploadServlet.class);
   private static final long serialVersionUID = 1L;
   private File destinationDir;
   private String basePath = null;
   private String baseUrl = null;
   private String permittedExtensions = null;

   private int maxLimit;
   private int maxSizeInMB;

   public FileUploadServlet() {
      super();
   }

   /**
    * Get the settings stored in the Custom object
    */
   public void init(ServletConfig config) throws ServletException {
      super.init(config);
      log.info("Inside init of FileUploadServlet");

      SailPointContext context = null;
      try {
         context = SailPointFactory.createContext();
         Custom custom = context.getObjectByName(Custom.class,
               "File Attachment Parameters");
         log.debug("Global Parameters " + custom.toXml());
         basePath = (String) custom.get("basePath");
         basePath.replaceAll("\\\\", "/");
         if (!basePath.endsWith("/")) {
            basePath = basePath + "/";
         }
         baseUrl = (String) custom.get("baseUrl");
         if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
         }

         permittedExtensions = (String) custom.get("permittedExtensions");
         if (null == permittedExtensions) {
            permittedExtensions = "doc,pdf,xls,csv,ppt,pptx,xlsx,docx,jpg,png,gif,txt";
         }

         String maxLimitString = (String) custom.get("maxLimit");
         if (null != maxLimitString) {
            maxLimit = Integer.parseInt((String) maxLimitString);
         } else {
            maxLimit = 2;
         }

         String maxSizeInMBString = (String) custom.get("maxSizeInMB");
         if (null != maxSizeInMBString) {
            maxSizeInMB = Integer.parseInt((String) maxSizeInMBString);
         } else {
            maxSizeInMB = 2;
         }

      } catch (GeneralException e) {
         log.error("Error creating context in IIQ ", e);
      } finally {
         if (null != context) {
            // Complete the use of the context
            log.debug("Releasing the SailPoint context.");
            try {
               SailPointFactory.releaseContext(context);
            } catch (GeneralException e) {
               if (log.isWarnEnabled())
                  log.warn(
                        "Failed releasing SailPointContext: "
                              + e.getLocalizedMessage(), e);
            }
         }
      }

      destinationDir = new File(basePath);
      if (!destinationDir.isDirectory()) {
         log.error("Could not find directory path " + basePath);
         throw new ServletException(basePath + " is not a directory");
      }
   }

   /**
    * Manage the file parameters on the WorkflowCase object
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      log.info("Inside GET of FileUploadServlet");
      PrintWriter out = response.getWriter();
      response.setContentType("text/html");
      SailPointContext context = null;
      JSONObject myObj = new JSONObject();
      try {
         String workItemId = request.getParameter("workItemId");
         String action = request.getParameter("action");
         log.info("Workitem Id been processed " + workItemId);
         log.info("Action : " + action);
         if (action != null && "view".equalsIgnoreCase(action)) {
            if (workItemId != null && !workItemId.isEmpty()) {
               context = SailPointFactory.createContext();
               WorkItem workItem = (WorkItem) context.getObjectById(
                     WorkItem.class, workItemId);
               WorkflowCase workflowCase = workItem.getWorkflowCase();
               if (workflowCase != null) {
                  if (workflowCase.get("fileParameters") != null) {
                     log.debug("Existing file list on the workflow "
                           + workflowCase.get("fileParameters"));
                     myObj.put("fileParameters",
                           workflowCase.get("fileParameters"));
                  }
               }
            }
         } else if (action != null && "update".equalsIgnoreCase(action)) {
            if (workItemId != null && !workItemId.isEmpty()) {
               context = SailPointFactory.createContext();
               WorkItem workItem = (WorkItem) context.getObjectById(
                     WorkItem.class, workItemId);

               WorkflowCase workflowCase = workItem.getWorkflowCase();
               if (workflowCase != null) {
                  if (workflowCase.get("fileParameters") != null) {
                     log.debug("Existing file list on the workflow "
                           + workflowCase.get("fileParameters"));
                     String filePath = request.getParameter("file");
                     log.info("File to be deleted " + filePath);
                     if (filePath != null && !filePath.isEmpty()) {
                        List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
                        fileList = (List) workflowCase.get("fileParameters");
                        if (fileList != null && !fileList.isEmpty()) {
                           for (Iterator iterator = fileList.iterator(); iterator
                                 .hasNext();) {
                              Map<String, String> map = (Map<String, String>) iterator
                                    .next();
                              if (filePath.equalsIgnoreCase(map.get("file"))) {
                                 iterator.remove();
                                 File file = new File(filePath);
                                 file.delete();
                              }
                           }
                           workflowCase.put("fileParameters", fileList);
                           context.saveObject(workflowCase);
                           context.commitTransaction();
                           log.debug("Updated file list on the workflow "
                                 + fileList);
                           myObj.put("fileParameters", fileList);

                        } else {
                           myObj.put("fileParameters", fileList);
                        }
                     } else {
                        // Do nothing
                        myObj.put("fileParameters",
                              workflowCase.get("fileParameters"));
                     }
                  }
               }
            }
         }
      } catch (GeneralException e) {
         log.error("Error while updating workflow in IIQ ", e);
      } catch (Exception ex) {
         log.error("Error encountered while uploading file", ex);
      } finally {
         if (null != context) {
            // Complete the use of the context
            log.debug("Releasing the SailPoint context.");
            try {
               SailPointFactory.releaseContext(context);
            } catch (GeneralException e) {
               if (log.isWarnEnabled())
                  log.warn(
                        "Failed releasing SailPointContext: "
                              + e.getLocalizedMessage(), e);
            }
         }
      }
      // convert the JSON object to string and send the response back
      out.println(myObj.toString());
      out.close();
   }

   /**
    * Upload the file and write it to its shared location
    */
   @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      log.info("Inside POST of FileUploadServlet");
      // PrintWriter to send the JSON response back
      PrintWriter out = response.getWriter();
      // set content type
      response.setContentType("text/html");
      // out.println("POST");
      DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
      // Set the size threshold, above which content will be stored on disk.
      fileItemFactory.setSizeThreshold(maxSizeInMB * 1024 * 1024); // 2 MB
      // Set the temporary directory to store the uploaded files of size above
      // threshold.
      ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
      // uploadHandler.setFileSizeMax(maxSizeInMB * 1024 * 1024);
      // Create a JSON object to send response
      JSONObject myObj = new JSONObject();
      SailPointContext context = null;
      String errorMessage = null;
      boolean error = false;
      try {
         // Parse the request
         myObj.put("success", false);
         List items = uploadHandler.parseRequest(request);
         Iterator iterator = items.iterator();
         // Map for storing file params in workflow
         Map<String, String> fileParams = new HashMap<String, String>();
         while (iterator.hasNext()) {
            FileItem item = (FileItem) iterator.next();
            // Handle Form Fields
            if (item.isFormField()) {
               if (item.getFieldName() != null) {
                  myObj.put(item.getFieldName(), item.getString());
                  fileParams.put(item.getFieldName(), item.getString());
               }
               log.info("File Name = " + item.getFieldName() + ", Value = "
                     + item.getString());
            }
            // Handle Uploaded files.
            else {
               log.info("Field Name = " + item.getFieldName()
                     + ", File Name = " + item.getName() + ", Content type = "
                     + item.getContentType() + ", File Size = "
                     + item.getSize());
               // Write file to the ultimate location.
               validate(item);
               long datetime = System.currentTimeMillis();
               String[] strArray = item.getName().split("\\\\");
               log.info("Truncated File Name " + strArray[strArray.length - 1]);

               File file = new File(destinationDir, datetime
                     + strArray[strArray.length - 1]);
               item.write(file);
               myObj.put("fileName", strArray[strArray.length - 1]);
               myObj.put(item.getFieldName(), basePath + datetime
                     + strArray[strArray.length - 1]);
               myObj.put("url", baseUrl + datetime
                     + strArray[strArray.length - 1]);
               fileParams.put("fileName", strArray[strArray.length - 1]);
               fileParams.put(item.getFieldName(), basePath + datetime
                     + strArray[strArray.length - 1]);
               fileParams.put("url", baseUrl + datetime
                     + strArray[strArray.length - 1]);

               String workItemId = request.getParameter("workItemId");
               log.info("Workitem Id been processed " + workItemId);
               if (workItemId != null && !workItemId.isEmpty()) {
                  context = SailPointFactory.createContext();
                  WorkItem workItem = (WorkItem) context.getObjectById(
                        WorkItem.class, workItemId);
                  WorkflowCase workflowCase = workItem.getWorkflowCase();
                  if (workflowCase != null) {
                     if (workflowCase.get("fileParameters") != null) {
                        List<Map<String, String>> fileList = (List) workflowCase
                              .get("fileParameters");
                        if (fileList.size() >= maxLimit) {
                           log.warn("Cannot exceed limit of " + maxLimit
                                 + " attachments");
                           throw new FileUploadException(
                                 "Cannot exceed limit of " + maxLimit
                                       + " attachments");
                        } else {
                           fileList.add(fileParams);
                           log.debug("Existing file list on the workflow "
                                 + fileList);
                           workflowCase.put("fileParameters", fileList);
                        }
                     } else {
                        List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
                        fileList.add(fileParams);
                        log.debug("Creating new file Parameter on the workflow "
                              + fileList);
                        workflowCase.put("fileParameters", fileList);
                     }
                  }
                  context.saveObject(workflowCase);
                  context.commitTransaction();
               }
            }
         }
         // sets success to true
         myObj.put("success", true);

      } catch (FileUploadException ex) {
         log.error("Error encountered while parsing the request ", ex);
         errorMessage = ex.getMessage();
         error = true;
      } catch (GeneralException e) {
         log.error("Error while updating workflow in IIQ ", e);
         errorMessage = e.getMessage();
         error = true;
      } catch (Exception ex) {
         log.error("Error encountered while uploading file", ex);
         errorMessage = ex.getMessage();
         error = true;
      } finally {
         try {
            myObj.put("errorMessage", errorMessage);
            myObj.put("error", error);
            if (null != context) {
               SailPointFactory.releaseContext(context);
               log.debug("Releasing the SailPoint context.");
            }
         } catch (GeneralException e) {
            if (log.isWarnEnabled())
               log.warn(
                     "Failed when releasing SailPointContext: "
                           + e.getLocalizedMessage(), e);
         } catch (Exception ex) {
            log.error("Error encountered while uploading file", ex);

         }
      }

      // convert the JSON object to string and send the response back
      out.println(myObj.toString());
      out.close();
   }

   /**
    * Ensure the file has a valid extension and size
    */
   private void validate(FileItem item) throws FileUploadException {
      try {
         if ((!item.isFormField()) && item.getName() != null) {
            List<String> validExtensions = Util.csvToList(permittedExtensions);
            String uploadedExtension = null;
            String fileName = item.getName();
            if (fileName.contains(".") && !(fileName.endsWith("."))) {
               int indexOfPeriod = fileName.lastIndexOf(".");
               uploadedExtension = fileName.substring(indexOfPeriod + 1);
               uploadedExtension = uploadedExtension.toLowerCase();
            }
            log.warn("File extension used for upload is " + uploadedExtension);
            if (null == uploadedExtension
                  || !validExtensions.contains(uploadedExtension)) {
               throw new FileUploadException(
                     "Attachment must have one of the following extensions: "
                           + permittedExtensions.replaceAll(",", ", "));
            }
            Long fileSize = item.getSize();
            log.debug("Size: " + fileSize);
            if (fileSize > (maxSizeInMB * 1024 * 1024)) {
               throw new FileUploadException(
                     "File is larger than the maximum permitted size of "
                           + maxSizeInMB + "MB.");
            }

         }
      } catch (Exception e) {
         log.error("Error while validating file ", e);
         throw new FileUploadException(e.getMessage());
      }
   }
}