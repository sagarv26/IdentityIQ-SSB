package sailpoint.services.tools.docgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * this class is the base Documentizer class, should be extended for each object
 * type implemented
 *
 * @author adam.creaney
 */

public class DocBase {

   public static Hashtable<String, ArrayList<String>> references = new Hashtable<String, ArrayList<String>>();
   public static File source;
   public static File destination;
   public DocWriter docWriter;
   protected FileOutputStream fos;
   protected OutputStreamWriter osw;
   public String name;
   protected String objectType;
   protected static HashMap<String, String> objectList;

   
   static private final String OBJECT_LIST_FRAME_TEMPLATE = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
         + "<!--NewPage-->\n"
         + "<HTML>\n"
         + "<head>\n"
         + "<!-- {generatedby} -->\n"
         + "</head>\n"
         + "<TITLE>\n"
         + "{ObjectType}\n"
         + "</TITLE>\n"
         + "<META NAME=\"keywords\" CONTENT=\"sailpoint.api package\">\n"
         + "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"../../stylesheet.css\" TITLE=\"Style\">\n"
         + "</HEAD>\n"
         + "<BODY BGCOLOR=\"white\">\n"
         + "<FONT size=\"+1\" CLASS=\"FrameTitleFont\">{ObjectType}</FONT>\n"
         + "<TABLE BORDER=\"0\" WIDTH=\"100%\" SUMMARY=\"\">\n"
         + "<TR>\n"
         + "<TD NOWRAP><FONT size=\"+1\" CLASS=\"FrameHeadingFont\">Objects</FONT>&nbsp;\n"
         + "<FONT CLASS=\"FrameItemFont\">\n"
         + "{ObjectList}\n"
         + "</TABLE>\n"
         + "</BODY>\n" + "</HTML>";

   static private final String newLine = "\n";

   public enum DOCTYPE {
      Application, AuditConfig, Bundle, Capability, Certification, CertificationDefinition, CertificationGroup, Configuration, CorrelationConfig, Custom, DashboardContent, Dictionary, DynamicScope, EmailTemplate, Form, FullTextIndex, GroupDefinition, GroupFactory, Identity, IdentityTrigger, IntegrationConfig, LocalizedAttribute, ObjectConfig, PasswordPolicy, Policy, QuickLink, QuickLinkOptions, RequestDefinition, Rule, RuleRegistry, ServiceDefinition, SPRight, Scope, Target, TargetAssociation, TargetSource, TaskDefinition, TaskSchedule, UIConfig, Workflow
   }

   public DocBase() {
      objectList = new HashMap<String, String>();
   }

   /**
    * this method will build out the frame list used for rendering the JavaDocs,
    * using the template found in ObjectListFrameTemplate and substituting in
    * the {ObjectType} and {ObjectList} values with the current object type, and
    * the list of all objects of that type respectively
    *
    * @throws IOException
    */
   public void buildFrameList() throws IOException {
      try {
         String s = OBJECT_LIST_FRAME_TEMPLATE.replaceAll("\\{ObjectType}",
               this.objectType);
         StringBuilder sb = new StringBuilder();
         String fileName;
         String objectName;
         String filePath;
         
         Map<String, String> treeMap = new TreeMap<String, String>(objectList);
         
         Iterator<Map.Entry<String, String>> it = treeMap.entrySet()
               .iterator();
         
        // Iterator<Map.Entry<String, String>> it = objectList.entrySet()
        //       .iterator();
         while (it.hasNext()) {
            fileName = (String) it.next().getKey();
            objectName = objectList.get(fileName);
            sb.append("<br/>" + newLine);
            sb.append("<a href='" + fileName + ".html' title='" + objectName
                  + "' target='objectFrame'>" + objectName + "</A>" + newLine);
         }
         s = s.replaceAll("\\{ObjectList}", sb.toString());
         filePath = destination.getAbsolutePath() + "/" + this.objectType
               + "/object-frame.html";
         File html = new File(filePath);
         File htmlParent = html.getParentFile();
         if (!htmlParent.exists())
            htmlParent.mkdirs();
         this.fos = new FileOutputStream(filePath);
         this.osw = new OutputStreamWriter(fos);
         osw.write(s);
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (osw != null) {
            osw.flush();
            osw.close();
         }
      }
   }

   /**
    * this method will add a backreference to the references Mapping
    *
    * @param refParts
    *           list of reference parts, used to concatenate the reference
    *           String
    * @param bookmark
    *           bookmark used to get link
    *
    * @deprecated
    *
    */
   public void addBackReference(List<String> refParts, String bookmark) {
      if (refParts != null && refParts.size() > 1 && bookmark != null
            && !bookmark.isEmpty()) {
         String name = refParts.get(0) + ":" + refParts.get(1);
         if (!references.containsKey(name)) {
            references.put(name, new ArrayList<String>());
         }
         String link = getLinkToSelf(bookmark);
         references.get(name).add(link);
      }
   }

   /**
    * this method does nothing
    *
    * @param bookmark
    *           String representation of a bookmark
    *
    * @return String
    *
    * @deprecated
    *
    */
   public String getLinkToSelf(String bookmark) {
      List<String> selfParts = new ArrayList<String>();
      selfParts.add(this.objectType);
      selfParts.add(this.name);

      return "";
   }

   public void setObjectType(String objectType) {
      this.objectType = objectType;
   }

}
