package sailpoint.services.tools.docgen;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import sailpoint.services.tools.docgen.DocBase;

/**
 * this class contains utility methods used throughout the DocumentGenerator
 * project
 *
 * @author adam.creaney
 *
 */
public class DocumentizerUtils {

   private static final Object[] EMPTY_ARRAY = new Object[0];

   /**
    * the purpose of this variable is to ignore certain tags from the XML
    * artifacts which are pre-known to us NOTE: in order extend ignore xml tags
    * add the tag names to this arraylist
    * */
   private static final ArrayList<String> IGNORE_TAGS = new ArrayList<String>(
         Arrays.asList(new String[] { "sailpoint", "importaction", "map" }));

   /**
    * the purpose of this variable is to search the pre-known XML tags which
    * resides under the ignored tags NOTE: in order extend search xml tags add
    * the tag names to this arraylist
    * */
    private static final ArrayList<String> SEARCH_TAGS = new ArrayList<String>(Arrays.asList(new String[]{"AuditConfig", "UIConfig", "GroupDefinition", "Workflow", "Configuration", "Identity", "Form", "Application", "Bundle", "Capability", "CertificationDefinition", "CertificationGroup", "IdentityTrigger", "IntegrationConfig", "CorrelationConfig", "Custom", "Dictionary", "DynamicScope", "EmailTemplate", "Documentation", "FullTextIndex", "GroupFactory", "LocalizedAttribute", "ObjectConfig", "PasswordPolicy", "Policy", "QuickLink", "QuickLinkOptions", "RequestDefinition", "Rule", "RuleRegistry", "ServiceDefinition", "SPRight", "TargetSource", "TaskDefinition", "TaskSchedule", "DashboardContent", "Workgroup"}));

   private static HashMap<String, ArrayList<File>> artifactsMap = new HashMap<String, ArrayList<File>>();

   /**
    * this method returns a a UTF-8 encoded String that represents the content
    * of a file specified by the input full path
    *
    * @param path
    *           String representing the absolute path to a file
    **
    * @return UTF-8 encoded String representation of the file
    *
    * @throws IOException
    */
   public static String readFile(String path) throws IOException {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, "UTF-8");
   }

   /**
    * this method returns all sub-directories found under a top-level File
    * object that is passed in as input
    *
    * @param folder
    *           top level File object
    **
    * @return File array containing all the directories contained in the input
    *         File object
    *
    */
   public static File[] getDirectories(File folder) {
      FileFilter directoryFilter = new FileFilter() {
         public boolean accept(File file) {
            return file.isDirectory();
         }
      };

      File[] allFiles = folder.listFiles(directoryFilter);

      return allFiles;
   }

   /**
    * this method returns boolean true if a file object is a directory
    *
    * @param file
    *           File to check
    **
    * @return Boolean true if File is a Directory
    *
    */
   public static boolean isDirectory(File file) {
      boolean isDirectory = false;
      if (file.isDirectory()) {
         isDirectory = true;
      }
      return isDirectory;
   }

   /**
    * this method returns a file name for a given object, based on the 'name'
    * attribute of the passed-in Element object
    *
    * @param node
    *           Element will be used to create a file name
    *
    * @return String representatin of the file name
    *
    */
   public static String getFilenameForObject(Element node) {
      String objectName = node.getAttribute("name");
      return getFilenameForObject(objectName);
   }

   /**
    * this method returns a sanitized filename for an object. The object name
    * has any characters not a-z, A-Z, or 0-9, ., or - replaced with an
    * underscore
    *
    * @param name
    *           String that will have illegal characters replaced
    *
    * @return String that has been sanitized
    *
    */
   public static String getFilenameForObject(String name) {
      String objectName = name;
      String newName = objectName.replaceAll("[^a-zA-Z0-9.-]", "_");
      return newName;
   }

   /**
    * this method to create a directory on the file system given a java File
    * object
    *
    * @param file
    *           File object to create
    *
    */
   public static void createDirectory(File file) {
      try {
         file.mkdir();
      } catch (SecurityException se) {

      }
   }

   /**
    * this method returns a HashMap of the Documents attribute tag section
    *
    * @param node
    *           Element that will be checked for attributes
    **
    * @return HashMap representation of the Documents attribute tag
    *
    * @throws TransformerException
    */
   public static HashMap<String, List<String>> getAttributesMap(Element node)
         throws TransformerException {
      // TODO:Limit the number of "entry" elements returned to just the first
      // level
      HashMap<String, List<String>> attributes = new HashMap<String, List<String>>();
      NodeList nl = node.getElementsByTagName("Map");
      Element mapNode = (Element) nl.item(0);
      if (mapNode != null) {
         NodeList children = mapNode.getElementsByTagName("entry");
         for (int i = 0; i < children.getLength(); i++) {
            Element e = (Element) children.item(i);
            buildAttribute(e, attributes);
         }
      }
      return attributes;
   }

   /**
    * this method will attempt to build a representation of an attribute based
    * on its type, to be added to the overall attributes HashMap of the
    * Document. It takes the entry Element and gets the key. Then, when looking
    * at the value it needs to determine what type of value it represents.
    * Currently implemented are: date boolean map script OTHER
    *
    * It then adds this to the HashMap that represents the attribute mapping of
    * the current Document
    *
    * @param node
    *           Element that represents an "entry" node from an XML resource
    *
    * @param attributes
    *           HashMap of document Attributes
    *
    * @throws TransformerException
    *
    */
   public static void buildAttribute(Element node,
         HashMap<String, List<String>> attributes) throws TransformerException {
      String attrName = node.getAttribute("key");
      List<String> attrValues = new ArrayList<String>();
      if (node.getAttributes().getLength() == 2) {
         attrValues.add(node.getAttribute("value"));
      } else if (getFirstChildFix(node) != null) {
         Element valueNode = getFirstChildFix(node);
         Element valueNodeType = getFirstChildFix(valueNode);
         // switch (valueNodeType.getNodeName().toLowerCase())
         String type = valueNodeType.getNodeName().toLowerCase();
         if (null != type && type.equals("date")) {
            String dt = valueNode.getTextContent().trim();
            if (dt != null && !dt.isEmpty()) {
               Long unixDt = Long.parseLong(dt);
               Date d = new Date(unixDt);
               SimpleDateFormat format = new SimpleDateFormat(
                     "yyyy-MM-dd HH:mm:ss");
               String formattedDate = format.format(d);
               attrValues.add(formattedDate);
            }
         } else if (null != type && type.equals("boolean")) {
            if (getFirstChildFix(valueNode) != null) {
               String value = getFirstChildFix(valueNode).getTextContent();
               if (value == null) {
                  attrValues.add("false");
               } else if (value.equalsIgnoreCase("true")) {
                  attrValues.add("true");
               } else {
                  attrValues.add("false");
               }
            } else {
               attrValues.add("false");
            }
         } else if (null != type && type.equals("map")) {
            // TODO: figure out how to accurately print an entire map (which may
            // contain various other subelements)
            NodeList subElements = valueNodeType.getElementsByTagName("entry");
            List<String> entries = new ArrayList<String>();
            for (int i = 0; i < subElements.getLength(); i++) {
               Element entry = (Element) subElements.item(i);
               String key = entry.getAttribute("key");
               entries.add(key);
            }
            // String allKeys = String.join(",", entries);
            String allKeys = "";
            for (String entry : entries) {
               if (allKeys.equals("")) {
                  allKeys = entry;
               } else {
                  allKeys = allKeys + "," + entry;
               }
            }
            attrValues.add(allKeys);
         } else if (null != type && type.equals("script")) {
            // TODO: figure out how to accurately print an entire map (which may
            // contain various other subelements)
            String script = valueNodeType.getTextContent();
            attrValues.add("script:" + script);
         } else {
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance()
                  .newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                  "yes");
            transformer.transform(new DOMSource(valueNode), xmlOutput);
            String nodeAsAString = xmlOutput.getWriter().toString();
            attrValues.add(nodeAsAString);
         }
      } else {
         attrValues.add("Not Specified");
      }
      attributes.put(attrName, attrValues);
   }

   /**
    * this method is a fix that will get the first 'real' child element of a
    * Element node, and ignore any text or whitespace nodes
    *
    * @param e
    *           Element from which to get the first child
    **
    * @return first 'real' child of the input Element
    **
    */
   public static Element getFirstChildFix(Element e) {
      Element ret = null;
      NodeList nl = e.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
         Node child = nl.item(i);
         if (child.getNodeType() == Node.ELEMENT_NODE) {
            ret = (Element) child;
         }
      }
      return ret;
   }

   /**
    * this method will return a string representation of the input Node object.
    *
    * @param n
    *           Node object to parse to String
    **
    * @return String representing the Node object
    *
    */
   public static String elementToString(Node n) {

      String name = n.getNodeName();

      short type = n.getNodeType();

      if (Node.CDATA_SECTION_NODE == type) {
         return "<![CDATA[" + n.getNodeValue() + "]]&gt;";
      }

      if (name.startsWith("#")) {
         return "";
      }

      StringBuffer sb = new StringBuffer();
      sb.append('<').append(name);

      NamedNodeMap attrs = n.getAttributes();
      if (attrs != null) {
         for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            sb.append(' ').append(attr.getNodeName()).append("=\"")
                  .append(attr.getNodeValue()).append("\"");
         }
      }

      String textContent = null;
      NodeList children = n.getChildNodes();

      if (children.getLength() == 0) {
         if ((textContent = getTextContent(n)) != null
               && !"".equals(textContent)) {
            sb.append(textContent).append("</").append(name).append('>');
            ;
         } else {
            sb.append("/>").append('\n');
         }
      } else {
         sb.append('>').append('\n');
         boolean hasValidChildren = false;
         for (int i = 0; i < children.getLength(); i++) {
            String childToString = elementToString(children.item(i));
            if (!"".equals(childToString)) {
               sb.append(childToString);
               hasValidChildren = true;
            }
         }

         if (!hasValidChildren && ((textContent = getTextContent(n)) != null)) {
            sb.append(textContent);
         }

         sb.append("</").append(name).append('>');
      }

      return sb.toString();
   }

   /**
    * this method returns all of the text content from a node
    *
    * @param n
    *           Node object for which to get text content
    **
    * @return all text content of a given Node
    *
    */
   public static String getTextContent(Node n) {
      if (n.hasChildNodes()) {
         StringBuffer sb = new StringBuffer();
         NodeList nl = n.getChildNodes();
         for (int i = 0; i < nl.getLength(); i++) {
            sb.append(elementToString(nl.item(i)));
            if (i < nl.getLength() - 1) {
               sb.append('\n');
            }
         }

         String s = sb.toString();
         if (s.length() != 0) {
            return s;
         }
      }

      Method[] methods = Node.class.getMethods();

      for (int i = 0; i < methods.length; i++) {
         if ("getTextContent".equals(methods[i].getName())) {
            Method getTextContext = methods[i];
            try {
               return (String) getTextContext.invoke(n, EMPTY_ARRAY);
            } catch (Exception e) {
               return null;
            }
         }
      }

      String textContent = null;

      if (n.hasChildNodes()) {
         NodeList nl = n.getChildNodes();
         for (int i = 0; i < nl.getLength(); i++) {
            Node c = nl.item(i);
            if (c.getNodeType() == Node.TEXT_NODE) {
               textContent = n.getNodeValue();
               if (textContent == null) {
                  // TODO This is a hack. Get rid of it and implement this
                  // properly
                  String s = c.toString();
                  int idx = s.indexOf("#text:");
                  if (idx != -1) {
                     textContent = s.substring(idx + 6).trim();
                     if (textContent.endsWith("]")) {
                        textContent = textContent.substring(0,
                              textContent.length() - 1);
                     }
                  }
               }
               if (textContent == null) {
                  break;
               }
            }
         }

         // TODO This is a hack. Get rid of it and implement this properly
         String s = n.toString();
         int i = s.indexOf('>');
         int i2 = s.indexOf("</");
         if (i != -1 && i2 != -1) {
            textContent = s.substring(i + 1, i2);
         }
      }

      return textContent;
   }

   /**
    * this method returns inner text of a specified Element
    *
    * @param node
    *           Element that will be checked for inner text
    *
    * @param tag
    *           String representation of the Tag name
    *
    * @return String of inner text of the Element
    *
    */
   public static String getNodeTextForPath(Element node, String tag) {
      String value = "";

      try {
         NodeList nl = (NodeList) node.getElementsByTagName(tag);
         Node selectedNode = (Node) nl.item(0);
         if (selectedNode != null) {
            value = selectedNode.getTextContent();
         } else {
         }
      } catch (Exception e) {

      }
      return value;
   }

   /**
    * this method returns a HashMap of the signature tags from a given element
    * if it exists
    *
    * @param node
    *           Element that will be checked for signature tags
    **
    * @return signature HashMap
    **
    */
   public static HashMap<String, List<HashMap<String, String>>> getSignatureMap(
         Element node) {
      HashMap<String, List<HashMap<String, String>>> signature = new HashMap<String, List<HashMap<String, String>>>();
      ArrayList<HashMap<String, String>> ins = new ArrayList<HashMap<String, String>>();
      ArrayList<HashMap<String, String>> outs = new ArrayList<HashMap<String, String>>();
      NodeList nl = node.getElementsByTagName("Inputs");
      if (nl.getLength() > 0) {
         NodeList inputs = nl.item(0).getChildNodes();
         for (int i = 0; i < inputs.getLength(); i++) {
            if (inputs.item(i).getNodeName().equalsIgnoreCase("Argument")) {
               HashMap<String, String> map = buildArgument((Element) inputs
                     .item(i));
               ins.add(map);
            }
         }
         signature.put("<b>Inputs</b>", ins);
      }
      NodeList nlo = node.getElementsByTagName("Returns");
      if (nlo.getLength() > 0) {
         NodeList outputs = nlo.item(0).getChildNodes();

         for (int j = 0; j < outputs.getLength(); j++) {
            if (outputs.item(j).getNodeName().equalsIgnoreCase("Argument")) {
               HashMap<String, String> map = buildArgument((Element) outputs
                     .item(j));
               outs.add(map);
            }
         }
         signature.put("<b>Returns</b>", outs);
      }

      return signature;
   }

   /**
    * this method builds a HashMap for Element arguments: name, type,
    * description
    *
    * @param node
    *           Element that will be checked for arguments
    *
    * @return argument map containing name, type, and description
    *
    *
    */
   
   /**
    * 
    * @param node
    * @return
    * adding condition to include rule signatures without description
    */
   public static HashMap<String, String> buildArgument(Element node) {
      HashMap<String, String> arg = new HashMap<String, String>();
      arg.put("name", node.getAttribute("name"));
      arg.put("type", node.getAttribute("type"));
      if(node.getElementsByTagName("Description").getLength()!=0)
      arg.put("description", node.getElementsByTagName("Description").item(0).getTextContent());
      else 
    	  arg.put("description", node.getAttribute("description"));
      return arg;
   }

   /**
    * this method returns a give attribute from a node
    *
    * @param node
    *           Element that will be checked for attribute
    *
    * @param attrName
    *           attribute to get from the node
    *
    * @return attribute string
    *
    */
   public static String getNodeAttribute(Element node, String attrName) {
      String value = "";
      try {
         value = node.getAttribute(attrName);
      } catch (Exception e) {

      }
      return value;
   }

   /**
    * this method will safe-encode a string to an HTML string Element
    *
    * @param s
    *           String to sanitize
    *
    * @return HTML encoded string
    *
    */
   public static String encodeHTML(String s) {
      StringBuffer out = new StringBuffer();
      for (int i = 0; i < s.length(); i++) {
         char c = s.charAt(i);
         if (c > 127 || c == '"' || c == '<' || c == '>') {
            out.append("&#" + (int) c + ";");
         } else {
            out.append(c);
         }
      }
      return out.toString();
   }

   /**
    * this method will return the 'name' attribute of a given Element
    *
    * @param node
    *           the node whose name will be returned
    *
    * @return the 'name' attribute of the node
    *
    */
   public static String getNameForElement(Element node) {
      return getNodeAttribute(node, "name");
   }

   /**
    * this method will pad a right-pad a string
    *
    * @param s
    *           the String to be padded
    *
    * @param n
    *           the amount of characters to pad
    *
    * @return String the right padded string
    */
   public static String padRight(String s, int n) {
      return String.format("%1$-" + n + "s", s);
   }

   /**
    * this method will pad a left-pad a string
    *
    * @param s
    *           the String to be padded
    *
    * @param n
    *           the amount of characters to pad
    *
    * @return String the left padded string
    */
   public static String padLeft(String s, int n) {
      return String.format("%1$" + n + "s", s);
   }

   /**
    * this method will recursively add all files with .xml extension located in
    * a specified directory
    *
    * @param directory
    *           the File object representing current directory
    *
    * @param files
    *           ArrayList that contains all subfiles
    *
    */
   public static void listf(File directory, ArrayList<File> files) {

      File[] fList = directory.listFiles();
      for (File file : fList) {
         if (file.isFile()) {
            if (file.getName().indexOf(".xml") > -1) {
               files.add(file);
            }
         } else if (file.isDirectory()) {
            listf(file, files);
         }
      }
   }

   /**
    * this method is used to return the actual root element of xml file
    * 
    * @param File
    *           object of xml
    * @return Element object for root
    **/
   public static Element getRoot(File file) {
      try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory
               .newInstance();
         // TODO: Why/how can we avoid the dtd processing besides this line?
         dbFactory
               .setFeature(
                     "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                     false);

         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         InputStream is = null;
         is = new FileInputStream(file);
         Reader reader = new InputStreamReader(is, "UTF-8");
         InputSource source = new InputSource(reader);
         source.setEncoding("UTF-8");
         Document doc = dBuilder.parse(source);

         doc.getDocumentElement().normalize();
         is.close();
         return doc.getDocumentElement();
      } catch (Exception e) {
         System.out.println(file.getName());
         e.printStackTrace();
      }
      return null;
   }
   
   
     public static ArrayList<String> getRealRootList(Element root){
    	
    	ArrayList<String> nodelist = new ArrayList<String>();
    	Element node;
        if(SEARCH_TAGS.contains(root.getTagName())){        
        	nodelist.add(root.getTagName());
        	}
        else if (IGNORE_TAGS.contains(root.getTagName().toLowerCase())){
        	for(String tag : SEARCH_TAGS) {        		
        		NodeList nList = root.getElementsByTagName(tag);        		
        		if(nList != null && nList.getLength() > 0) {        			
        			node = (Element)nList.item(0);        			
        			nodelist.add(node.getTagName());
        			}
        		}
        	} 
        	        
		return nodelist;
    }

   /**
    * @author Rohit.Pant this method is to return real root element by ignoring
    *         all element types added in IGNORE_TAGS list as some of the tags
    *         eg. sailpoint, ImportAction etc have to be ignored in order to get
    *         correct result
    * 
    *         NOTE: In case any new tags are introduced which needs to be
    *         ignored please add it to the list
    *
    * @param root
    *           the root Element of the Document
    *
    * @param objectType
    *           the objectType that this doc represents, based on the folder
    *           where the source doc resides
    *
    * @return returns the new "root" Element for the Document
    *
    */
 public static Element getRealRoot(Element root, String objecttype){       
       	Element node = null;
            if(SEARCH_TAGS.contains(root.getTagName())){       
            	return root;}
            else if (IGNORE_TAGS.contains(root.getTagName().toLowerCase())){
            	for(String tag : SEARCH_TAGS) {
            		NodeList nList = root.getElementsByTagName(tag);            
            		if(nList != null && nList.getLength() > 0) {
            			node = (Element)nList.item(0);
            			if(node != null && node.getTagName().equals(objecttype) )            				
            				return (Element) node;            		}
            	}
            }
           
            return (Element) node;
        }

   /**
    * Method to get all subdirectories within directory
    * 
    * @param File
    *           object of the base directory
    * @return List having File objects of all subdirectories
    */
   private static List<File> getSubdirs(File file) {
      List<File> subdirs = Arrays.asList(file.listFiles(new FileFilter() {
         public boolean accept(File f) {
            return f.isDirectory();
         }
      }));
      subdirs = new ArrayList<File>(subdirs);

      // searching for all the directories recursively
      List<File> deepSubdirs = new ArrayList<File>();
      for (File subdir : subdirs) {
         deepSubdirs.addAll(getSubdirs(subdir));
      }
      subdirs.addAll(deepSubdirs);
      return subdirs;
   }

   /**
    * Method to get all XML file objects
    * 
    * @param String
    *           base directory path containing the XML files
    * @return List having File objects of all XML files
    */
   private static List<File> getAllXmlFiles(File file) {
      File[] immediateXmls = file.listFiles(new FileFilter() {
         @Override
         public boolean accept(File arg0) {
            if (arg0.getName().toLowerCase().endsWith(".xml"))
               return true;
            return false;
         }
      });
      List<File> subDirs = getSubdirs(file);
      List<File> xmlFilesList = null;
      if (immediateXmls != null && immediateXmls.length > 0)
         xmlFilesList = new ArrayList<File>(Arrays.asList(immediateXmls));
      else
         xmlFilesList = new ArrayList<File>();
      for (File sub : subDirs) {
         // adding custom file filter to retrieve XML file objects
         File[] xmlFiles = sub.listFiles(new FileFilter() {
            @Override
            public boolean accept(File arg0) {
               if (arg0.getName().toLowerCase().endsWith(".xml"))
                  return true;
               return false;
            }
         });
         if (xmlFiles != null && xmlFiles.length > 0) {
            xmlFilesList.addAll(Arrays.asList(xmlFiles));
         }
      }
      return xmlFilesList;
   }

  public static void processAllXmlFiles() {
		List<File> xmlFiles = getAllXmlFiles(DocBase.source);
		ArrayList<File> xmlList;
		Element root = null;
		ArrayList<String> realRoot = null;
		for(File xml : xmlFiles) {
			
			root = getRoot(xml);
			String realRoottemp;
			if(root != null) {
			    realRoot = getRealRootList(root);
			    for(int i = 0; i < realRoot.size(); i++){			
				realRoottemp = realRoot.get(i);
				if(realRoottemp != null){
				xmlList = artifactsMap.get(realRoottemp);
				
				if(xmlList == null) {
					xmlList = new ArrayList<File>();
					xmlList.add(xml);
				} else {
					xmlList.add(xml);
				}
				
				artifactsMap.put(realRoottemp, xmlList);
			}
			}
			}
		}
		
	}

   public static HashMap<String, ArrayList<File>> getArtifactsMap() {
      return artifactsMap;
   }

}
