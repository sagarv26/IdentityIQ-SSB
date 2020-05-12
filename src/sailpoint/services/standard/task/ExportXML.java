package sailpoint.services.standard.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.AuditConfig;
import sailpoint.object.AuditConfig.AuditAction;
import sailpoint.object.AuditConfig.AuditAttribute;
import sailpoint.object.AuditConfig.AuditClass;
import sailpoint.object.ClassLists;
import sailpoint.object.Configuration;
import sailpoint.object.Dictionary;
import sailpoint.object.DictionaryTerm;
import sailpoint.object.Filter;
import sailpoint.object.ObjectAttribute;
import sailpoint.object.ObjectConfig;
import sailpoint.object.QueryOptions;
import sailpoint.object.SailPointObject;
import sailpoint.object.TaskResult;
import sailpoint.object.TaskSchedule;
import sailpoint.object.UIConfig;
import sailpoint.server.Exporter.Cleaner;
import sailpoint.task.AbstractTaskExecutor;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;
import sailpoint.tools.xml.AbstractXmlObject;
import sailpoint.tools.xml.XMLObjectFactory;

/**
 * Export XML objects from IdentityIQ
 *
 * @author <a href="mailto:paul.wheeler@sailpoint.com">Paul Wheeler</a>
 */

public class ExportXML extends AbstractTaskExecutor {
   private static Log log = LogFactory.getLog(ExportXML.class);

   /**
    * Path that we will output directory structure to
    */
   public static final String ARG_BASE_PATH = "basePath";

   /**
    * Remove IDs and creation/modification timestamps if true
    */
   public static final String ARG_REMOVE_IDS = "removeIDs";

   /**
    * Comma-separated list of classes that we will export (all if blank)
    */
   public static final String ARG_CLASS_NAMES = "classNames";

   /**
    * Only export object if created or modified after this date (all dates if
    * blank)
    */
   public static final String ARG_FROM_DATE = "fromDate";

   /**
    * Target properties file for reverse lookup of tokens that will replace
    * matched text
    */
   public static final String ARG_TARGET_PROPS_FILE = "targetPropsFile";

   /**
    * Custom naming format for exported file using these tokens: $Class$ =
    * Object Class, $Name$ = Object Name
    */
   public static final String ARG_CUSTOM_NAMING_FORMAT = "namingFormat";

   /**
    * Path of directory containing base XML files used for object comparison
    * when exporting supported object types as merge files
    */
   public static final String ARG_MERGE_COMPARE_DIR_PATH = "mergeCompareDirPath";

   /**
    * If true, enclose Beanshell code in a CDATA section and unescape the code.
    */
   public static final String ARG_ADD_CDATA = "addCData";

   /**
    * If true, do not use an ID/Name map for looking up object names from IDs.
    * Instead, do a direct query. This will be MUCH slower but may be needed for
    * customers with a very large number of objects to avoid the map size taking
    * up too much memory. This is a hidden option.
    */
   public static final String NO_ID_NAME_MAP = "noIdNameMap";

   String _basePath;
   boolean _removeIDs;
   List<String> _classNames = new ArrayList<String>();
   Date _fromDate;
   String _targetPropsFile;
   String _namingFormat;
   String _mergeCompareDirPath;
   boolean _addCData;
   boolean _noIdNameMap;

   boolean terminate = false;
   int totalExported = 0;
   String exportDetails = null;
   int classObjectsExported = 0;

   Map<String, String> iDNameMap = new HashMap<String, String>();

   /**
    * Main task execution method
    */
   public void execute(SailPointContext context, TaskSchedule schedule,
         TaskResult result, Attributes<String, Object> args) throws Exception {

      log.debug("Starting XML Object Exporter");
      // Get arguments
      _basePath = args.getString(ARG_BASE_PATH);
      _removeIDs = args.getBoolean(ARG_REMOVE_IDS);
      _classNames = args.getStringList(ARG_CLASS_NAMES);
      _fromDate = args.getDate(ARG_FROM_DATE);
      _targetPropsFile = args.getString(ARG_TARGET_PROPS_FILE);
      _namingFormat = args.getString(ARG_CUSTOM_NAMING_FORMAT);
      _mergeCompareDirPath = args.getString(ARG_MERGE_COMPARE_DIR_PATH);
      _addCData = args.getBoolean(ARG_ADD_CDATA);
      _noIdNameMap = args.getBoolean(NO_ID_NAME_MAP);

      log.debug("Base path: " + _basePath);
      log.debug("Remove IDs: " + _removeIDs);
      log.debug("Class names: " + _classNames);
      log.debug("From date: " + _fromDate);
      log.debug("Target properties file: " + _targetPropsFile);
      log.debug("Naming format: " + _namingFormat);
      log.debug("Merge comparison base directory: " + _mergeCompareDirPath);
      log.debug("No ID/Name Map: " + _noIdNameMap);

      String defaultClasses = "Application,AuditConfig,Bundle,Capability,Configuration,CorrelationConfig,Custom,DashboardContent,Dictionary,DynamicScope,EmailTemplate,Form,FullTextIndex,GroupFactory,IdentityTrigger,IntegrationConfig,LocalizedAttribute,MessageTemplate,ObjectConfig,PasswordPolicy,Plugin,Policy,QuickLink,QuickLinkOptions,RightConfig,Rule,RuleRegistry,SPRight,ScoreConfig,TaskDefinition,TaskSchedule,UIConfig,Workflow,Workgroup";
      Map<String, String> tokenMap = new HashMap<String, String>();

      // Build id/name map so we can replace id with names in some objects
      if (!_noIdNameMap) {
         log.debug("Building map of IDs to names");
         updateProgress(context, result, "Building id/name map");
         iDNameMap = buildIdNameMap(context);
         log.debug("Finished building map");
         log.debug("Map size: " + iDNameMap.size());
      }

      // If we have a target.properties file, do reverse-tokenisation based on
      // the tokens in the properties file.
      if (null != _targetPropsFile) {
         tokenMap = getTokenMap();
      }

      // Convert backslashes to forward slashes (will still work in Windows)
      _basePath = _basePath.replaceAll("\\\\", "/");
      if (!_basePath.endsWith("/")) {
         _basePath = _basePath + "/";
      }

      // Build a map of all the objects we find under the directory containing
      // the comparison files for merges
      Map<String, String> mergeCompareMap = new HashMap<String, String>();
      if (null != _mergeCompareDirPath) {
         _mergeCompareDirPath = _mergeCompareDirPath.replaceAll("\\\\", "/");
         if (!_mergeCompareDirPath.endsWith("/")) {
            _mergeCompareDirPath = _mergeCompareDirPath + "/";
         }
         mergeCompareMap = getMergeCompareMap();
      }

      // If there's no fromDate assume we want all objects
      if (null == _fromDate) {
         _fromDate = new Date(Long.MIN_VALUE);
      }

      if (null != _classNames && _classNames.size() > 0) {
         // If we find "default" in the list we need to merge in the default set
         // of classes
         if (_classNames.contains("default")) {
            List<String> defaultClassList = Arrays.asList(defaultClasses
                  .split(","));
            // Use a set to ensure we don't have duplicates if the user has
            // added any default classes to the list
            HashSet<String> mergedClassNames = new HashSet<String>(_classNames);
            mergedClassNames.addAll(defaultClassList);
            _classNames.clear();
            _classNames.addAll(mergedClassNames);
            _classNames.remove("default");
         }
         for (String className : _classNames) {
            updateProgress(context, result, "Exporting class " + className);
            exportClassObjects(context, className, tokenMap, mergeCompareMap);
         }

      } else {

         Class<?>[] allClasses = ClassLists.MajorClasses;
         for (int i = 0; i < allClasses.length; i++) {
            log.debug(allClasses[i].getName());
            String className = allClasses[i].getSimpleName();
            updateProgress(context, result, "Exporting class " + className);
            exportClassObjects(context, className, tokenMap, mergeCompareMap);
         }
         // Have to do Workgroup separately as it's not really a class of its
         // own
         exportClassObjects(context, "Workgroup", tokenMap, mergeCompareMap);
      }
      result.setAttribute("exportDetails", exportDetails);
      result.setAttribute("objectsExported", totalExported);
      log.debug("Exiting XML Object Exporter");
   }

   /**
    * Export objects of a given class, with reverse-tokenisation using the token
    * map and creating merge files using the merge map.
    */
   private void exportClassObjects(SailPointContext context, String className,
         Map<String, String> tokenMap, Map<String, String> mergeCompareMap)
         throws Exception {

      log.debug("Starting export of class " + className);

      String providedClassName = className;
      if (className.equalsIgnoreCase("Workgroup"))
         className = "Identity";
      String fullClassName = "sailpoint.object." + className;

      Class<? extends SailPointObject> currentClass = null;

      try {
         currentClass = Class.forName(fullClassName).asSubclass(
               SailPointObject.class);

      } catch (ClassNotFoundException e) {
         StringBuffer sb = new StringBuffer();
         sb.append("Could not find class: ");
         sb.append(fullClassName);
         log.warn(sb.toString());
         return;
      }

      QueryOptions qo = new QueryOptions();

      Filter dateFilter = Filter.or(Filter.ge("created", _fromDate),
            Filter.ge("modified", _fromDate));
      qo.add(dateFilter);

      if (providedClassName.equalsIgnoreCase("Workgroup")) {
         Filter workgroupFilter = Filter.eq("workgroup", true);
         qo.add(workgroupFilter);
      }

      // List<Object> objects = new ArrayList<Object>();
      Iterator<Object[]> objIterator = null;
      try {
         objIterator = context.search(currentClass, qo, "id");
      } catch (Exception e) {
         if (e.getMessage().contains("could not resolve property:")) {
            log.warn("Ignoring class " + className
                  + " as it has no created or modified property");
            return;
         }
      }

      if (null != objIterator && objIterator.hasNext()) {
         File dir = new File(_basePath + providedClassName);
         // Create object directory if it doesn't exist
         if (!dir.exists()) {
            if (dir.mkdirs()) {
               log.debug("Created directory " + dir.getPath());
            } else {
               log.error("Could not create directory " + dir.getPath() + "!");
            }
         } else {
            log.debug("Directory " + dir.getPath() + " already exists");
         }
      }

      classObjectsExported = 0;
      int counter = 0;
      List<String> propertiesToClean = new ArrayList<String>(Arrays.asList(
            "id", "created", "modified", "targetId", "assignedScopePath",
            "policyId", "assignmentId", "roleId", "identityId"));

      while (null != objIterator && objIterator.hasNext()) {
         Object thisObject[] = objIterator.next();
         String objectId = (String) thisObject[0];
         SailPointObject object = context.getObjectById(currentClass, objectId);
         if (null != object) {
            String objectName = object.getName();
            String normalizedObjectName = null;
            if (null == objectName) // Some objects don't have names so must use
                                    // the ID
               objectName = objectId;
            // Replace all illegal filename characters and spaces with
            // underscore
            normalizedObjectName = objectName.replaceAll("[^a-zA-Z0-9.-]", "_");
            String fileName;
            if (null != _namingFormat) {
               fileName = _namingFormat;
               fileName = fileName.replaceAll("\\$Name\\$",
                     normalizedObjectName);
               fileName = fileName.replaceAll("\\$Class\\$", providedClassName);
               if (!fileName.toLowerCase().endsWith(".xml")) {
                  fileName = fileName + ".xml";
               }
            } else {
               fileName = normalizedObjectName + ".xml";
            }

            String xml = ((AbstractXmlObject) object).toXml();

            if (null != _mergeCompareDirPath) {
               if (className.equals("Configuration")
                     || className.equals("UIConfig")
                     || className.equals("ObjectConfig")
                     || className.equals("AuditConfig")
                     || className.equals("Dictionary")) {
                  log.debug("Getting merge compare map");
                  String origXml = mergeCompareMap.get(className + ","
                        + objectName);
                  log.debug("Got merge compare map");
                  if (null != origXml) {
                     XMLObjectFactory f = XMLObjectFactory.getInstance();

                     SailPointObject compareObject = (SailPointObject) f
                           .parseXml(context, origXml, true);
                     xml = getMergeXml(object, compareObject);

                  }
               }
            }

            if (_removeIDs) {
               Cleaner cleaner = new Cleaner(propertiesToClean);
               xml = cleaner.clean(xml);
            }

            // Match any 32 character hex id that we can find, unless it's
            // preceded by something
            // that tells us it really needs to be an id, in which case we can't
            // really do anything with it.

            Pattern pattern = Pattern
                  .compile("((?<!Id\" value=\")(?<!id\" value=\")(?<!Id=\")(?<!id=\")(?<!id=)[0-9a-f]{32})");

            Matcher matcher = pattern.matcher(xml);

            while (matcher.find()) {
               String id = matcher.group();
               String resolvedObjectName = null;
               if (_noIdNameMap) {
                  resolvedObjectName = getObjectNameFromId(context, id);
               } else {
                  resolvedObjectName = (String) iDNameMap.get(id);
               }
               if (null != resolvedObjectName) {
                  log.debug("Resolved id " + id + " to " + resolvedObjectName
                        + " for object " + objectName + " of class "
                        + className);
                  xml = xml.replace(id, resolvedObjectName);
               }
            }

            if (!tokenMap.isEmpty()) {
               Iterator<Entry<String, String>> it = tokenMap.entrySet()
                     .iterator();
               while (it.hasNext()) {
                  Map.Entry<String, String> pairs = (Map.Entry<String, String>) it
                        .next();
                  String token = (String) pairs.getKey();
                  String value = (String) pairs.getValue();
                  String containsValue = value.replace("\\\\", "\\"); // Deal
                                                                      // with
                                                                      // backslashes
                  log.debug("Checking for token value " + value);
                  if (xml.contains(containsValue)) {
                     // Escape regex special characters
                     String replaceValue = value.replaceAll("\\\\", "\\\\");
                     replaceValue = replaceValue.replaceAll("\\+", "\\\\+");
                     replaceValue = replaceValue.replaceAll("\\^", "\\\\^");
                     replaceValue = replaceValue.replaceAll("\\$", "\\\\"
                           + Matcher.quoteReplacement("$")); // $ sign has a
                                                             // special meaning
                                                             // in replaceAll
                     replaceValue = replaceValue.replaceAll("\\.", "\\\\.");
                     replaceValue = replaceValue.replaceAll("\\|", "\\\\|");
                     replaceValue = replaceValue.replaceAll("\\?", "\\\\?");
                     replaceValue = replaceValue.replaceAll("\\*", "\\\\*");
                     replaceValue = replaceValue.replaceAll("\\(", "\\\\(");
                     replaceValue = replaceValue.replaceAll("\\)", "\\\\)");
                     replaceValue = replaceValue.replaceAll("\\[", "\\\\[");
                     replaceValue = replaceValue.replaceAll("\\{", "\\\\{");
                     log.debug("Found value " + value
                           + ", replacing with token " + token);
                     xml = xml.replaceAll(replaceValue, token);
                  }
               }
            }

            if (_addCData) {
               xml = addCData(xml);
            }

            // Ensure the file opens nicely in Notepad.
            // In some cases we will just have a LF at the end of the line but
            // in others we will already have CRLF
            // so make them all LF, then replace with CRLF.
            xml = xml.replaceAll("\\r\\n", "\n");
            xml = xml.replaceAll("\\n", "\r\n");

            log.debug("Exporting " + providedClassName + " " + objectName
                  + " to " + _basePath + providedClassName + "/" + fileName);
            Util.writeFile(_basePath + providedClassName + "/" + fileName, xml);
            totalExported++;
            classObjectsExported++;
            counter++;
            if (counter > 49) { // Decache every 50 objects
               context.decache();
               counter = 0;
            }
         }
      }
      if (classObjectsExported > 0) {
         if (null == exportDetails) {
            exportDetails = providedClassName + ": " + classObjectsExported;
         } else {
            exportDetails = exportDetails + ", " + providedClassName + ": "
                  + classObjectsExported;
         }
      }
   }

   /**
    * Return the name of an object from a given ID, where we don't know the
    * class.
    */
   @SuppressWarnings("unchecked")
   private String getObjectNameFromId(SailPointContext context, String id)
         throws GeneralException {
      String resolvedObjectName = null;
      SailPointObject object = null;

      for (Class<SailPointObject> cls : ClassLists.MajorClasses) {
         object = context.getObjectById(cls, id);
         if (null != object) {
            break;
         }
      }

      if (null == object)
         return null;
      try {
         resolvedObjectName = object.getName();
      } catch (Exception e) {
         if (e.getMessage().contains("could not resolve property:")) {
            log.debug("Ignoring class " + object.getClass().getName()
                  + " as it has no name property");
         }
      }

      return resolvedObjectName;

   }

   /**
    * Get the tokens and their values from the referenced target properties file
    * and build a map that we can use for reverse-tokenisation.
    */
   private Map<String, String> getTokenMap() throws GeneralException,
         IOException {
      Map<String, String> tokenMapFromPropsFile = new HashMap<String, String>();
      BufferedReader br = new BufferedReader(new FileReader(_targetPropsFile));
      try {
         String line = br.readLine();
         while (line != null) {
            if (line.startsWith("%%") && line.contains("=")
                  && !line.contains("%%TARGET%%")) {
               String[] splitLine = line.split("=", 2);
               String tokenName = splitLine[0];
               String tokenValue = splitLine[1];
               // Ignore simple true/false values - these will not be tokenised
               // as they appear everywhere.
               // Also ignore any value that is just whitespace or blank.
               if (!tokenValue.toLowerCase().equals("true")
                     && !tokenValue.toLowerCase().equals("false")
                     && tokenValue.trim().length() > 0)
                  tokenMapFromPropsFile.put(tokenName, tokenValue);
            }
            line = br.readLine();
         }

      } finally {
         br.close();
      }
      return tokenMapFromPropsFile;
   }

   /**
    * Find all the original XML files under the references directory path that
    * we need to compare with the XML to be exported to find differences so that
    * we can build merge files. Build a map with a key of
    * "Class name,Object name" and a value of the full XML of the original file
    * that we're going to compare with our XML.
    */
   private Map<String, String> getMergeCompareMap() throws GeneralException,
         IOException {
      log.debug("Comparing...");
      Map<String, String> mergeMap = new HashMap<String, String>();
      File dir = new File(_mergeCompareDirPath);
      if (!dir.exists()) {
         throw new GeneralException("Merge comparison path " + dir.toString()
               + " does not exist");
      }
      List<String> compareFiles = listXmlFilesForDir(dir);
      for (String compareFile : compareFiles) {
         log.debug("Comparing " + compareFile);
         String origXml = null;
         BufferedReader br = new BufferedReader(new FileReader(compareFile));
         StringBuilder origXmlSB = new StringBuilder();
         try {
            String line = br.readLine();

            while (null != line) {
               if (null != origXml) {
                  origXmlSB.append("\n" + line);
                  line = br.readLine();
               } else {
                  origXmlSB.append(line);
                  line = br.readLine();
               }
            }
            origXml = origXmlSB.toString();
            String xmlClassName = null;
            String xmlObjectName = null;
            Pattern classPattern = Pattern.compile("!DOCTYPE (.*?) PUBLIC");
            Matcher classMatcher = classPattern.matcher(origXml);
            if (classMatcher.find()) {
               xmlClassName = classMatcher.group(1).trim();
            }
            Pattern namePattern = Pattern.compile("name=\"(.*?)\"");
            Matcher nameMatcher = namePattern.matcher(origXml);
            if (nameMatcher.find()) {
               xmlObjectName = nameMatcher.group(1).trim();
            }
            if (null != xmlClassName && null != xmlObjectName) {
               String objectKey = xmlClassName + "," + xmlObjectName;
               log.debug("Adding " + xmlClassName + "," + xmlObjectName);
               mergeMap.put(objectKey, origXml);
            }
         } finally {
            br.close();
         }
      }
      return mergeMap;
   }

   /**
    * Build a map of ID against name for all objects. This is used so that we
    * can easily look up the name of an object from its ID when we need to
    * replace the ID with the name in some objects. This will result in a large
    * map, but it's far more efficient than querying every time we need to look
    * it up individually.
    */
   private Map<String, String> buildIdNameMap(SailPointContext context)
         throws GeneralException {
      Map<String, String> iDNameMap = new HashMap<String, String>();
      Class<?>[] allMajorClasses = ClassLists.MajorClasses;
      for (int i = 0; i < allMajorClasses.length; i++) {
         String className = allMajorClasses[i].getSimpleName();
         String fullClassName = "sailpoint.object." + className;
         Class<? extends SailPointObject> currentClass = null;
         try {
            currentClass = Class.forName(fullClassName).asSubclass(
                  SailPointObject.class);

         } catch (Exception e) {
            StringBuffer sb = new StringBuffer();
            sb.append("Could not find class: ");
            sb.append(fullClassName);
            throw new GeneralException(sb.toString());
         }
         try {
            log.debug("Adding to ID/Name map for class " + className);
            QueryOptions qo = new QueryOptions();
            if (className.equals("Identity")) {
               // Ensure we include workgroups when querying identities - by
               // default this doesn't happen.
               List<Boolean> trueAndFalse = new ArrayList<Boolean>();
               trueAndFalse.add(new Boolean(true));
               trueAndFalse.add(new Boolean(false));
               qo.addFilter(Filter.in("workgroup", trueAndFalse));
            }
            Iterator<Object[]> it = context
                  .search(currentClass, qo, "name, id");

            while (it.hasNext()) {
               Object[] obj = it.next();
               String objName = (String) obj[0];
               if (null != objName) {
                  String objId = (String) obj[1];
                  log.debug("Adding " + objId + " " + objName);
                  iDNameMap.put(objId, objName);
               }
            }

         } catch (Exception e) {
            if (e.getMessage().contains("could not resolve property:")) {
               log.debug("Ignoring class " + className
                     + " as it has no name property");
            }
         }
      }
      return iDNameMap;

   }

   /**
    * Return a list of XML files under a directory path.
    */
   private List<String> listXmlFilesForDir(final File dir) {
      List<String> fileList = new ArrayList<String>();
      for (final File fileEntry : dir.listFiles()) {
         if (fileEntry.isDirectory()) {
            fileList.addAll(listXmlFilesForDir(fileEntry));
         } else {
            String filePath = fileEntry.getPath();
            if (filePath.toLowerCase().endsWith(".xml")) {
               fileList.add(filePath);
               log.debug("Found file path: " + filePath);
            }
         }
      }
      return fileList;
   }

   @SuppressWarnings("unchecked")
   /**
    * Get the XML of the merged object by comparing the current object with the original object from the filesystem.
    */
   private String getMergeXml(SailPointObject object,
         SailPointObject compareObject) throws GeneralException,
         NoSuchMethodException, IllegalAccessException,
         IllegalArgumentException, InvocationTargetException,
         InstantiationException {

      String mergeXml = null;
      Class<? extends SailPointObject> clazz = compareObject.getClass();

      SailPointObject baseObject = clazz.cast(compareObject);
      SailPointObject currentObject = clazz.cast(object);

      Attributes<String, Object> baseAttributes = new Attributes<String, Object>();
      Attributes<String, Object> currentAttributes = new Attributes<String, Object>();
      List<ObjectAttribute> baseObjectAttributes = new ArrayList<ObjectAttribute>();
      List<ObjectAttribute> currentObjectAttributes = new ArrayList<ObjectAttribute>();
      Map<String, ObjectAttribute> baseObjectAttributesMap = new HashMap<String, ObjectAttribute>();
      Map<String, ObjectAttribute> currentObjectAttributesMap = new HashMap<String, ObjectAttribute>();
      List<AuditAttribute> baseAuditAttributes = new ArrayList<AuditAttribute>();
      List<AuditAttribute> currentAuditAttributes = new ArrayList<AuditAttribute>();
      Map<String, AuditAttribute> baseAuditAttributesMap = new HashMap<String, AuditAttribute>();
      Map<String, AuditAttribute> currentAuditAttributesMap = new HashMap<String, AuditAttribute>();
      List<AuditClass> baseAuditClasses = new ArrayList<AuditClass>();
      List<AuditClass> currentAuditClasses = new ArrayList<AuditClass>();
      Map<String, AuditClass> baseAuditClassesMap = new HashMap<String, AuditClass>();
      Map<String, AuditClass> currentAuditClassesMap = new HashMap<String, AuditClass>();
      List<AuditAction> baseAuditActions = new ArrayList<AuditAction>();
      List<AuditAction> currentAuditActions = new ArrayList<AuditAction>();
      Map<String, AuditAction> baseAuditActionsMap = new HashMap<String, AuditAction>();
      Map<String, AuditAction> currentAuditActionsMap = new HashMap<String, AuditAction>();
      List<DictionaryTerm> baseDictionaryTerms = new ArrayList<DictionaryTerm>();
      List<DictionaryTerm> currentDictionaryTerms = new ArrayList<DictionaryTerm>();
      List<String> baseDictionaryTermsValues = new ArrayList<String>();
      List<String> currentDictionaryTermsValues = new ArrayList<String>();
      if (compareObject instanceof Configuration
            || compareObject instanceof UIConfig) {
         Method getAttributesMethod = clazz.getMethod("getAttributes",
               (Class<?>[]) null);
         baseAttributes = (Attributes<String, Object>) getAttributesMethod
               .invoke(baseObject, (Object[]) null);
         currentAttributes = (Attributes<String, Object>) getAttributesMethod
               .invoke(currentObject, (Object[]) null);
      }

      if (compareObject instanceof ObjectConfig) {
         Method getConfigAttributesMethod = clazz.getMethod(
               "getConfigAttributes", (Class<?>[]) null);
         baseAttributes = (Attributes<String, Object>) getConfigAttributesMethod
               .invoke(baseObject, (Object[]) null);
         currentAttributes = (Attributes<String, Object>) getConfigAttributesMethod
               .invoke(currentObject, (Object[]) null);
         Method getObjectAttributesMethod = clazz.getMethod(
               "getObjectAttributes", (Class<?>[]) null);
         baseObjectAttributes = (List<ObjectAttribute>) getObjectAttributesMethod
               .invoke(baseObject, (Object[]) null);
         if (null != baseObjectAttributes) {
            for (ObjectAttribute baseObjectAttribute : baseObjectAttributes) {
               // Put the base ObjectAttributes into a map, with a key of the
               // ObjectAttribute name
               baseObjectAttributesMap.put(baseObjectAttribute.getName(),
                     baseObjectAttribute);
            }
         }
         currentObjectAttributes = (List<ObjectAttribute>) getObjectAttributesMethod
               .invoke(currentObject, (Object[]) null);
         if (null != currentObjectAttributes) {
            for (ObjectAttribute currentObjectAttribute : currentObjectAttributes) {
               // Put the current ObjectAttributes into a map, with a key of the
               // ObjectAttribute name
               currentObjectAttributesMap.put(currentObjectAttribute.getName(),
                     currentObjectAttribute);
            }
         }
      } else if (compareObject instanceof AuditConfig) {
         Method getAttributesMethod = clazz.getMethod("getAttributes",
               (Class<?>[]) null);
         baseAuditAttributes = (List<AuditAttribute>) getAttributesMethod
               .invoke(baseObject, (Object[]) null);
         if (null != baseAuditAttributes) {
            for (AuditAttribute baseAuditAttribute : baseAuditAttributes) {
               // Put the base AuditAttributes into a map, with a key of the
               // AuditAttribute name
               baseAuditAttributesMap.put(baseAuditAttribute.getName(),
                     baseAuditAttribute);
            }
         }
         currentAuditAttributes = (List<AuditAttribute>) getAttributesMethod
               .invoke(currentObject, (Object[]) null);
         if (null != currentAuditAttributes) {
            for (AuditAttribute currentAuditAttribute : currentAuditAttributes) {
               // Put the current AuditAttributes into a map, with a key of the
               // AuditAttribute name
               currentAuditAttributesMap.put(currentAuditAttribute.getName(),
                     currentAuditAttribute);
            }
         }
         Method getClassesMethod = clazz.getMethod("getClasses",
               (Class<?>[]) null);
         baseAuditClasses = (List<AuditClass>) getClassesMethod.invoke(
               baseObject, (Object[]) null);
         if (null != baseAuditClasses) {
            for (AuditClass baseAuditClass : baseAuditClasses) {
               // Put the base AuditClasses into a map, with a key of the
               // AuditClass name
               baseAuditClassesMap
                     .put(baseAuditClass.getName(), baseAuditClass);
            }
         }
         currentAuditClasses = (List<AuditClass>) getClassesMethod.invoke(
               currentObject, (Object[]) null);
         if (null != currentAuditClasses) {
            for (AuditClass currentAuditClass : currentAuditClasses) {
               // Put the current AuditClasses into a map, with a key of the
               // AuditClass name
               currentAuditClassesMap.put(currentAuditClass.getName(),
                     currentAuditClass);
            }
         }
         Method getActionsMethod = clazz.getMethod("getActions",
               (Class<?>[]) null);
         baseAuditActions = (List<AuditAction>) getActionsMethod.invoke(
               baseObject, (Object[]) null);
         if (null != baseAuditActions) {
            for (AuditAction baseAuditAction : baseAuditActions) {
               // Put the base AuditActions into a map, with a key of the
               // AuditAction name
               baseAuditActionsMap.put(baseAuditAction.getName(),
                     baseAuditAction);
            }
         }
         currentAuditActions = (List<AuditAction>) getActionsMethod.invoke(
               currentObject, (Object[]) null);
         if (null != currentAuditActions) {
            for (AuditAction currentAuditAction : currentAuditActions) {
               // Put the current AuditActions into a map, with a key of the
               // AuditAction name
               currentAuditActionsMap.put(currentAuditAction.getName(),
                     currentAuditAction);
            }
         }
      } else if (compareObject instanceof Dictionary) {
         Method getTermsMethod = clazz.getMethod("getTerms", (Class<?>[]) null);
         baseDictionaryTerms = (List<DictionaryTerm>) getTermsMethod.invoke(
               baseObject, (Object[]) null);

         if (null != baseDictionaryTerms) {
            for (DictionaryTerm baseDictionaryTerm : baseDictionaryTerms) {
               // DictionaryTerms have a value but no name - put the values into
               // a list
               baseDictionaryTermsValues.add(baseDictionaryTerm.getValue());
            }
         }
         currentDictionaryTerms = (List<DictionaryTerm>) getTermsMethod.invoke(
               currentObject, (Object[]) null);
         if (null != currentDictionaryTerms) {
            for (DictionaryTerm currentDictionaryTerm : currentDictionaryTerms) {
               // DictionaryTerms have a value but no name - put the values into
               // a list
               currentDictionaryTermsValues.add(currentDictionaryTerm
                     .getValue());
            }
         }
      }

      Attributes<String, Object> mergeAttributes = new Attributes<String, Object>();

      if (null != currentAttributes && !currentAttributes.isEmpty()) {
         Iterator<Entry<String, Object>> itAttr = currentAttributes.entrySet()
               .iterator();
         while (itAttr.hasNext()) {
            Map.Entry<String, Object> newPair = (Map.Entry<String, Object>) itAttr
                  .next();
            String keyName = (String) newPair.getKey();
            Object newValue = newPair.getValue();
            Object oldValue;
            if (null == baseAttributes) {
               oldValue = null;
            } else {
               oldValue = baseAttributes.get(keyName);
            }
            mergeAttributes = (Attributes<String, Object>) buildMergeMap(
                  mergeAttributes, keyName, oldValue, newValue);

         }
      }

      List<ObjectAttribute> mergeObjectAttributes = new ArrayList<ObjectAttribute>();
      Map<String, Object> mergeObjectAttributesMap = new HashMap<String, Object>();
      if (compareObject instanceof ObjectConfig
            && null != currentObjectAttributesMap) {
         Iterator<Entry<String, ObjectAttribute>> itObjAttr = currentObjectAttributesMap
               .entrySet().iterator();
         while (itObjAttr.hasNext()) {
            Map.Entry<String, ObjectAttribute> newPair = (Map.Entry<String, ObjectAttribute>) itObjAttr
                  .next();
            String keyName = (String) newPair.getKey();
            Object newValue = newPair.getValue();
            Object oldValue = baseObjectAttributesMap.get(keyName);

            mergeObjectAttributesMap = (HashMap<String, Object>) buildMergeMap(
                  mergeObjectAttributesMap, keyName, oldValue, newValue);

         }
         // Put the ObjectAttributes from the map into a list so that we can set
         // them on the ObjectConfig later
         for (Map.Entry<String, Object> entry : mergeObjectAttributesMap
               .entrySet()) {
            mergeObjectAttributes.add((ObjectAttribute) entry.getValue());
         }
      }

      List<AuditAttribute> mergeAuditAttributes = new ArrayList<AuditAttribute>();
      Map<String, Object> mergeAuditAttributesMap = new HashMap<String, Object>();
      List<AuditClass> mergeAuditClasses = new ArrayList<AuditClass>();
      Map<String, Object> mergeAuditClassesMap = new HashMap<String, Object>();
      List<AuditAction> mergeAuditActions = new ArrayList<AuditAction>();
      Map<String, Object> mergeAuditActionsMap = new HashMap<String, Object>();
      if (compareObject instanceof AuditConfig) {
         if (null != currentAuditAttributesMap) {
            Iterator<Entry<String, AuditAttribute>> itAudAttr = currentAuditAttributesMap
                  .entrySet().iterator();
            while (itAudAttr.hasNext()) {
               Map.Entry<String, AuditAttribute> newPair = (Map.Entry<String, AuditAttribute>) itAudAttr
                     .next();
               String keyName = (String) newPair.getKey();
               Object newValue = newPair.getValue();
               Object oldValue = baseAuditAttributesMap.get(keyName);
               mergeAuditAttributesMap = (HashMap<String, Object>) buildMergeMap(
                     mergeAuditAttributesMap, keyName, oldValue, newValue);
            }
            // Put the AuditAttributes from the map into a list so that we can
            // set them on the AuditConfig later
            for (Map.Entry<String, Object> entry : mergeAuditAttributesMap
                  .entrySet()) {
               mergeAuditAttributes.add((AuditAttribute) entry.getValue());
            }
         }
         if (null != currentAuditClassesMap) {
            Iterator<Entry<String, AuditClass>> itAudClass = currentAuditClassesMap
                  .entrySet().iterator();
            while (itAudClass.hasNext()) {
               Map.Entry<String, AuditClass> newPair = (Map.Entry<String, AuditClass>) itAudClass
                     .next();
               String keyName = (String) newPair.getKey();
               Object newValue = newPair.getValue();
               Object oldValue = baseAuditClassesMap.get(keyName);
               mergeAuditClassesMap = (HashMap<String, Object>) buildMergeMap(
                     mergeAuditClassesMap, keyName, oldValue, newValue);
            }
            // Put the AuditClasses from the map into a list so that we can set
            // them on the AuditConfig later
            for (Map.Entry<String, Object> entry : mergeAuditClassesMap
                  .entrySet()) {
               mergeAuditClasses.add((AuditClass) entry.getValue());
            }
         }
         if (null != currentAuditActionsMap) {
            Iterator<Entry<String, AuditAction>> itAudAction = currentAuditActionsMap
                  .entrySet().iterator();
            while (itAudAction.hasNext()) {
               Map.Entry<String, AuditAction> newPair = (Map.Entry<String, AuditAction>) itAudAction
                     .next();
               String keyName = (String) newPair.getKey();
               Object newValue = newPair.getValue();
               Object oldValue = baseAuditActionsMap.get(keyName);
               mergeAuditActionsMap = (HashMap<String, Object>) buildMergeMap(
                     mergeAuditActionsMap, keyName, oldValue, newValue);
            }
            // Put the AuditActions from the map into a list so that we can set
            // them on the AuditConfig later
            for (Map.Entry<String, Object> entry : mergeAuditActionsMap
                  .entrySet()) {
               mergeAuditActions.add((AuditAction) entry.getValue());
            }
         }
      }

      List<DictionaryTerm> mergeDictionaryTerms = new ArrayList<DictionaryTerm>();
      if (compareObject instanceof Dictionary) {
         if (null != currentDictionaryTermsValues
               && !currentDictionaryTermsValues.isEmpty()) {
            for (String currentDictionaryTermValue : currentDictionaryTermsValues) {
               if (null != currentDictionaryTermValue
                     && !baseDictionaryTermsValues
                           .contains(currentDictionaryTermValue)) {
                  DictionaryTerm mergeDictionaryTerm = new DictionaryTerm();
                  mergeDictionaryTerm.setValue(currentDictionaryTermValue);
                  mergeDictionaryTerms.add(mergeDictionaryTerm);
               }
            }
         }
      }

      SailPointObject mergeObject = clazz.newInstance();

      if (compareObject instanceof Configuration
            || compareObject instanceof UIConfig) {
         Method setAttributesMethod = clazz.getMethod("setAttributes",
               Attributes.class);
         setAttributesMethod.invoke(mergeObject, mergeAttributes);
      } else if (compareObject instanceof ObjectConfig) {
         if (!mergeAttributes.isEmpty()) {
            Method setConfigAttributesMethod = clazz.getMethod(
                  "setConfigAttributes", Attributes.class);
            setConfigAttributesMethod.invoke(mergeObject, mergeAttributes);
         }
         if (!mergeObjectAttributes.isEmpty()) {
            Method setObjectAttributesMethod = clazz.getMethod(
                  "setObjectAttributes", List.class);
            setObjectAttributesMethod
                  .invoke(mergeObject, mergeObjectAttributes);
         }
      } else if (compareObject instanceof AuditConfig) {
         if (!mergeAuditAttributes.isEmpty()) {
            Method setAttributesMethod = clazz.getMethod("setAttributes",
                  List.class);
            setAttributesMethod.invoke(mergeObject, mergeAuditAttributes);
         }
         if (!mergeAuditClasses.isEmpty()) {
            Method setClassesMethod = clazz.getMethod("setClasses", List.class);
            setClassesMethod.invoke(mergeObject, mergeAuditClasses);
         }
         if (!mergeAuditActions.isEmpty()) {
            Method setActionsMethod = clazz.getMethod("setActions", List.class);
            setActionsMethod.invoke(mergeObject, mergeAuditActions);
         }
      } else if (compareObject instanceof Dictionary) {
         if (!mergeDictionaryTerms.isEmpty()) {
            Method setTermsMethod = clazz.getMethod("setTerms", List.class);
            setTermsMethod.invoke(mergeObject, mergeDictionaryTerms);
         }
      }
      String objectName = object.getName();

      if (null == objectName)
         objectName = object.getId();

      mergeObject.setName(objectName);
      mergeXml = mergeObject.toXml();

      mergeXml = mergeXml.replace("<!DOCTYPE " + clazz.getSimpleName(),
            "<!DOCTYPE sailpoint");
      mergeXml = mergeXml.replace("\"sailpoint.dtd\">",
            "\"sailpoint.dtd\">\n<sailpoint>\n<ImportAction name=\"merge\">");
      mergeXml = mergeXml + "</ImportAction>\n</sailpoint>";
      return mergeXml;
   }

   @SuppressWarnings("unchecked")
   /**
    * Compare a new value to an old value and add the key and new value to the merge map if it's different.
    * Used to build up a map that we can use to create the merge files.
    */
   private Map<String, Object> buildMergeMap(
         Map<String, Object> mergeAttributes, String keyName, Object oldValue,
         Object newValue) throws GeneralException {
      Boolean oldEqualsNew = true;

      if (null != newValue && !(newValue instanceof List<?>)
            && !(newValue instanceof Map<?, ?>)) {
         oldEqualsNew = (objectsAreEqual(oldValue, newValue));
      }

      if (newValue instanceof Map<?, ?>) {
         Map<String, Object> newMap = new HashMap<String, Object>();
         Iterator<Entry<String, Object>> itMap = ((Map<String, Object>) newValue)
               .entrySet().iterator();
         while (itMap.hasNext()) {
            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>) itMap
                  .next();
            String key = pairs.getKey();
            Object newVal = pairs.getValue();
            Object oldVal = ((Map<String, Object>) oldValue).get(key);

            if (null == oldVal
                  || (null != oldVal && !objectsAreEqual(oldVal, newVal))) {
               oldEqualsNew = false;
               newMap.put(key, newVal);
            }
         }
         if (!newMap.isEmpty()) {
            oldEqualsNew = false;
            newValue = newMap;
         }
      }

      if (newValue instanceof List<?> && null != oldValue) {
         List<String> serializedOldValueList = new ArrayList<String>();
         Boolean listNotSerializable = false;

         for (Object oldVal : (List<Object>) oldValue) {
            if (oldVal instanceof AbstractXmlObject) {
               AbstractXmlObject oldValAbstract = (AbstractXmlObject) oldVal;
               serializedOldValueList.add(oldValAbstract.toXml());
            } else {
               break;
            }
         }

         List<Object> newItems = new ArrayList<Object>();
         for (Object newVal : (List<Object>) newValue) {
            if (newVal instanceof AbstractXmlObject) {
               AbstractXmlObject newValAbstract = (AbstractXmlObject) newVal;

               if (!serializedOldValueList.contains(newValAbstract.toXml())) {
                  newItems.add(newVal);
               }
            } else if (!((List<Object>) oldValue).contains(newVal)) {
               newItems.add(newVal);
            } else {
               break;
            }
         }

         if (!newItems.isEmpty()) {
            oldEqualsNew = false;
            newValue = newItems;
         }

         if (listNotSerializable && oldValue != null && oldValue != newValue
               && !oldValue.equals(newValue))
            oldEqualsNew = false;
      }

      if (((null == oldValue) && (null != newValue))
            || (null != oldValue && !oldEqualsNew)) {
         mergeAttributes.put(keyName, newValue);
      }
      return mergeAttributes;
   }

   /**
    * Add CDATA sections for beanshell code and unescape XML escape codes
    */
   private String addCData(String xml) {

      // Add CDATA for anything inside <Source> tags
      Pattern sourceTagPattern = Pattern.compile("(<Source>.+?</Source>)",
            Pattern.DOTALL);
      Matcher sourceMatcher = sourceTagPattern.matcher(xml);
      List<String> codeSegments = new ArrayList<String>();
      while (sourceMatcher.find()) {
         String code = sourceMatcher.group(1);
         if (!codeSegments.contains(code)) {
            codeSegments.add(code);
         }
      }
      for (String codeSegment : codeSegments) {
         String unescaped = StringEscapeUtils.unescapeXml(codeSegment);
         xml = xml.replace(
               codeSegment,
               "<Source><![CDATA["
                     + unescaped.substring(8, unescaped.length() - 9)
                     + "]]></Source>");
      }

      // Add CDATA for anything inside html tags - used in html EmailTemplates
      Pattern htmlTagPattern = Pattern.compile(
            "&lt;html.*?&gt;(.+?)&lt;/html&gt;", Pattern.DOTALL);
      Matcher htmlMatcher = htmlTagPattern.matcher(xml);
      List<String> htmlSegments = new ArrayList<String>();
      while (htmlMatcher.find()) {
         String html = htmlMatcher.group(0);
         if (!htmlSegments.contains(html)) {
            htmlSegments.add(html);
         }
      }
      for (String htmlSegment : htmlSegments) {
         xml = xml
               .replace(htmlSegment,
                     "<![CDATA[" + StringEscapeUtils.unescapeXml(htmlSegment)
                           + "]]>");
      }

      // Add CDATA for SOAP messages in IntegrationConfigs
      Pattern soapMessagePattern = Pattern.compile(
            "<entry key=\"soapMessage\"(.+?)/>", Pattern.DOTALL);
      Matcher soapMessageMatcher = soapMessagePattern.matcher(xml);
      while (soapMessageMatcher.find()) {
         String entry = soapMessageMatcher.group(0);
         // Transform it from a single line 'value=...' to multiple lines inside
         // <value> tags
         String modifiedEntry = entry.replace(
               "<entry key=\"soapMessage\" value=\"", "");
         modifiedEntry = modifiedEntry.replace("\"/>", "");
         modifiedEntry = "<entry key=\"soapMessage\"> <value> <String><![CDATA["
               + StringEscapeUtils.unescapeXml(modifiedEntry)
               + "]]></String> </value> </entry>";
         xml = xml.replace(entry, modifiedEntry);
      }
      return xml;
   }

   /**
    * Check whether two objects are equal
    */
   private Boolean objectsAreEqual(Object oldObject, Object newObject)
         throws GeneralException {
      // Check whether two objects are "equal"
      // Best way to test for equality is to compare the serialised XML if we
      // can
      if (oldObject instanceof AbstractXmlObject) {
         if (!((AbstractXmlObject) oldObject).toXml().equals(
               ((AbstractXmlObject) newObject).toXml())) {
            return false;
         }
         // Otherwise compare the string values
      } else if (!String.valueOf(newObject).equals(String.valueOf(oldObject))) {
         return false;
      }
      return true;
   }

   public boolean terminate() {

      terminate = true;

      return terminate;
   }

}
