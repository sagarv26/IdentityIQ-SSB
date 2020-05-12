package sailpoint.services.tools.docgen;

import static sailpoint.services.tools.docgen.DocumentizerUtils.encodeHTML;
import static sailpoint.services.tools.docgen.DocumentizerUtils.getAttributesMap;
import static sailpoint.services.tools.docgen.DocumentizerUtils.getFilenameForObject;
import static sailpoint.services.tools.docgen.DocumentizerUtils.getNodeAttribute;
import static sailpoint.services.tools.docgen.DocumentizerUtils.getNodeTextForPath;
import static sailpoint.services.tools.docgen.DocumentizerUtils.getSignatureMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import sailpoint.services.tools.docgen.DocBase;
import sailpoint.services.tools.docgen.DocumentizerUtils;

/**
 * this class is primarily used to create and write to an HTML document, and
 * will be instantiated by each subclass of DocBase.
 *
 * @author adam.creaney
 */
public class DocWriter {

   OutputStreamWriter htmlFile;
   OutputStream os;
   public String seperator;
   public String objectType;

   static private final String newLine = "\n";

   public DocWriter(File file, Element node, DocBase.DOCTYPE objectType) {

      this.seperator = FileSystems.getDefault().getSeparator();
      this.objectType = objectType.toString();
      String fileName = DocumentizerUtils.getFilenameForObject(node);

      String fullPath = file.getAbsolutePath() + this.seperator
            + this.objectType + this.seperator + fileName + ".html";

      try {
         File html = new File(fullPath);
         File htmlParent = html.getParentFile();
         if (!htmlParent.exists())
            htmlParent.mkdirs();
         this.os = new FileOutputStream(fullPath);
         this.htmlFile = new OutputStreamWriter(os);
         this.htmlFile.write("<html>" + newLine);
         this.writeHead();
         this.htmlFile.write("<body>" + newLine);

      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public DocWriter(String fileName) {
      try {
         this.os = new FileOutputStream(fileName);
         this.htmlFile = new OutputStreamWriter(os);
         this.htmlFile.write("<html>" + newLine);
         this.writeHead();
         this.htmlFile.write("<body>" + newLine);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * this method is used to write the header information for a new JavaDoc
    * style html page
    *
    * @throws IOException
    */
   private void writeHead() throws IOException {
      this.htmlFile.write("<head>" + newLine);
      this.htmlFile.write("<style type='text/css' media='screen'>" + newLine);
      this.htmlFile
            .write("table{border-collapse:collapse;border:1px solid black;}"
                  + newLine);
      this.htmlFile.write("table, td{border:3px solid black;}</style>"
            + newLine);
      this.htmlFile
            .write("<link href='../css/ui-lightness/jquery-ui-1.10.3.custom.css' rel='stylesheet'>"
                  + newLine);
      this.htmlFile.write("<script src='../js/jquery-1.9.1.js'></script>"
            + newLine);
      this.htmlFile
            .write("<script src='../js/jquery-ui-1.10.3.custom.js'></script>"
                  + newLine);
      this.htmlFile.write("<script language='javascript'>" + newLine);
      this.htmlFile.write("window.onload = function(e){ " + newLine
            + "$('.collapse').accordion({ " + newLine + "collapsible: true, "
            + newLine + "active: false " + newLine + "}); }" + newLine
            + "</script>" + newLine);
      this.htmlFile.write("</head>" + newLine);
   }

   /**
    * this method is used to write the footer information for a JavaDoc style
    * html page
    *
    * @throws IOException
    */
   public void close() throws IOException {
      if (this.htmlFile != null) {
         this.htmlFile.write("</body>" + newLine);
         this.htmlFile.write("</html>" + newLine);
         this.htmlFile.flush();
         this.htmlFile.close();
      }
   }

   /**
    * this method is used to write the footer information for a JavaDoc style
    * html page
    *
    * @param format
    *           String to append to end of HTML file
    *
    * @throws IOException
    */
   public void append(String format) throws IOException {
      this.htmlFile.write(format);
   }

   /**
    * this method appends a line break to an html page
    *
    * @throws IOException
    */
   public void appendBreak() throws IOException {
      this.htmlFile.write("<br/>" + newLine);
   }

   /**
    * this method appends a horizontal html page
    *
    * @throws IOException
    */
   public void appendHorizontalRule() throws IOException {
      this.htmlFile.write("<hr/>" + newLine);
   }

   /**
    * this method appends a String to the html page at the input heading level
    *
    * @param headingLevel
    *           heading level for the String
    * @param arg
    *           String object to be appended
    *
    * @throws IOException
    */
   public void appendHeading(String headingLevel, String arg)
         throws IOException {
      this.htmlFile.write("<" + headingLevel + ">" + arg + "</" + headingLevel
            + ">" + newLine);
   }

   /**
    * this method appends a String to the html page at h1 level
    *
    * @param arg
    *           String object to be appended
    *
    * @throws IOException
    */
   public void appendH1(String arg) throws IOException {
      appendHeading("h1", arg);
   }

   /**
    * this method appends a String to the html page at h2 level
    *
    * @param arg
    *           String object to be appended
    *
    * @throws IOException
    */
   public void appendH2(String arg) throws IOException {
      appendHeading("h2", arg);
   }

   /**
    * this method appends a String to the html page at h3 level
    *
    * @param arg
    *           String object to be appended
    *
    * @throws IOException
    */
   public void appendH3(String arg) throws IOException {
      appendHeading("h3", arg);
   }

   /**
    * this method is used to parse and append an argument map from a sailpoint
    * Object into a table consisting of key/value pairs. password argument wil
    * be represented in the table by a series of *'s
    *
    * @param node
    *           the Element to be parsed for arguments
    *
    * @throws IOException
    * @throws TransformerException
    */
   public void documentArguments(Element node) throws IOException,
         TransformerException {
      NodeList attrList = node.getElementsByTagName("Arguments");
      Element attr = (Element) attrList.item(0);
      if (attr != null) {

         HashMap<String, List<String>> attributes = getAttributesMap(attr);

         List<List<String>> table = new ArrayList<List<String>>();
         List<String> row = new ArrayList<String>();
         row.add("<b>Key</b>");
         row.add("<b>Value</b>");
         table.add(row);

         for (String attrName : attributes.keySet()) {
            // String attrValue = String.join(",", attributes.get(attrName));
            String attrValue = "";
            for (String attribute : attributes.get(attrName)) {
               if (attrValue.equals("")) {
                  attrValue = attribute;
               } else {
                  attrValue = attrValue + "," + attribute;
               }
            }
            row = new ArrayList<String>();
            row.add(attrName);
            if (attrName.equalsIgnoreCase("password")) {
               row.add("*********");
            } else {
               row.add(attrValue);
            }
            table.add(row);
         }

         String anchor = this.getAnchor("Arguments");
         this.appendTable(table, anchor);
      }
   }

   /**
    * this method is used to parse and append an attributes map from a sailpoint
    * Object into a table consisting of key/value pairs. password argument wil
    * be represented in the table by a series of *'s
    *
    * @param node
    *           the Element to be parsed for arguments
    *
    * @throws IOException
    * @throws TransformerException
    */
   public void documentAttributes(Element node) throws IOException,
         TransformerException {
      NodeList attrList = node.getElementsByTagName("Attributes");
      Element attr = (Element) attrList.item(0);
      if (attr != null) {

         HashMap<String, List<String>> attributes = getAttributesMap(attr);

         List<List<String>> table = new ArrayList<List<String>>();
         List<String> row = new ArrayList<String>();
         row.add("<b>Name</b>");
         row.add("<b>Value</b>");
         table.add(row);

         for (String attrName : attributes.keySet()) {
            // String attrValue = String.join(",", attributes.get(attrName));
            String attrValue = "";
            for (String attribute : attributes.get(attrName)) {
               if (attrValue.equals("")) {
                  attrValue = attribute;
               } else {
                  attrValue = attrValue + "," + attribute;
               }
            }
            row = new ArrayList<String>();
            row.add(attrName);
            if (attrName.equalsIgnoreCase("password")) {
               row.add("*********");
   } else if (attrValue.contains("SAMLConfig")){
	   row.add("Content not available");
   }
     else {
               row.add(attrValue);
            }
            table.add(row);
         }

         String anchor = this.getAnchor("Attributes");
         this.appendTable(table, anchor);
      }
   }

   /**
    * this method is used to recursively parse an argument map from a sailpoint
    * Object into a table consisting of key/value pairs. password argument wil
    * be represented in the table by a series of *'s
    *
    * @param attrs
    *           Element object representing the current node in document
    * @param anchor
    *           String representation of the html document anchor place
    *
    * @throws IOException
    */
   public void documentAttributesRec(Element attrs, String anchor)
         throws IOException {

      if (anchor == null || anchor.length() == 0) {
         anchor = this.getAnchor(attrs.getTagName());
      }

      List<List<String>> table = new ArrayList<List<String>>();
      List<String> row = new ArrayList<String>();

      if (attrs.getChildNodes().getLength() == 0) {

         row = new ArrayList<String>();
         row.add(attrs.getAttribute("key"));
         row.add(attrs.getAttribute("value"));
         this.appendTable(table, anchor);
      } else {
         anchor = this.getAnchor(attrs.getAttribute("key"));
         NodeList entries = attrs.getElementsByTagName("entry");
         row.add("Name");
         row.add("Value");
         table.add(row);
         for (int i = 0; i < entries.getLength(); i++) {
            Element entry = (Element) entries.item(i);
            documentAttributesRec(entry, anchor);
         }
         this.appendTable(table, anchor);
      }
   }

   /**
    * this method is used to return the HTML formatted anchor for a given String
    *
    * @param name
    *           Element object representing the current node in document
    *
    * @return returns HTML formatted string with inner text equal to name
    */
   public String getAnchor(String name) {
      return getAnchor(name, name);
   }

   /**
    * this method is used to return the HTML formatted anchor for a given
    * String, with provided text as the inner text of the HTML node
    *
    * @param name
    *           Element object representing the current node in document
    * @param text
    *           String
    *
    * @return returns HTML formatted string with inner text equal to input
    */
   public String getAnchor(String name, String text) {
      return "<a name='" + name + "'>" + text + "</a>";
   }

   /**
    * this method is to get an empty 'table', which is an ArrayList of Lists of
    * Strings representing a list of table rows
    *
    * @return returns skeleton ArrayList table with no content
    */
   public List<List<String>> getTableEmpty() {
      return new ArrayList<List<String>>();
   }

   /**
    * this method is append a table to the HTML document with a specified header
    *
    * @param heading
    *           String to be used as the table header
    *
    * @throws IOException
    */
   public void appendTable(String heading) throws IOException {
      this.htmlFile
            .write("<table border='1' width='100%' cellpadding='3' cellspacing='0' summary=''>"
                  + newLine);
      this.htmlFile.write("<tr bgcolor='#CCCCFF' class='TableHeadingColor'>"
            + newLine);
      this.htmlFile.write("<th align='left'>" + newLine);
      this.htmlFile.write("<font size='+2'><b>" + heading + "</b></font>"
            + newLine);
      this.htmlFile.write("</th>" + newLine);
      this.htmlFile.write("</tr>" + newLine);
      this.htmlFile.write("</table>" + newLine);
   }

   /**
    * this method is used to append a table to the html document, a heading will
    * be generated from the first row of the input table (if not empty)
    *
    * @param table
    *           table to be appended, with heading equal to the first row
    *
    * @throws IOException
    */
   public void appendTable(List<List<String>> table) throws IOException {
      String heading = "";
      if (table.size() > 0) {
         if (table.get(0).size() == 1) {
            heading = table.get(0).get(0);
            table.remove(0);
         }
      }
      appendTable(table, heading);
   }

   /**
    * this method is used to append a table to the html document, with heading
    * equal to provided input
    *
    * @param table
    *           table to be appended to html document
    * @param heading
    *           heading to be used for table
    *
    * @throws IOException
    */
   public void appendTable(List<List<String>> table, String heading)
         throws IOException {
      if (table.size() == 0) {
         return;
      }
      int maxColumns = 0;
      HashMap<Integer, String> widths = new HashMap<Integer, String>();
      widths.put(0, " width='20%'");
      widths.put(1, " width='60%'");

      for (List<String> row : table) {
         if (row.size() > maxColumns) {
            maxColumns = row.size();
         }
      }
      this.htmlFile
            .write("<table border='1' width='100%' cellpadding='3' cellspacing='0' summary=''>"
                  + newLine);
      if (heading.trim().length() > 0) {
         int colSpan = 1;
         if (table.size() > 0) {
            colSpan = getMaxTableCols(table);
         }
         this.htmlFile.write("<tr bgcolor='#CCCCFF' class='TableHeadingColor'>"
               + newLine);
         this.htmlFile.write("<th align='left' colspan=\"" + colSpan + "\">"
               + newLine);
         this.htmlFile.write("<font size='+2'><b>" + heading + "</b></font>"
               + newLine);
         this.htmlFile.write("</th>" + newLine);
         this.htmlFile.write("</tr>" + newLine);
      }

      for (List<String> row : table) {
         this.htmlFile.write("<tr>");
         int cellIndex = 0;
         if (row.size() < maxColumns) {
            for (String cell : row) {
               this.htmlFile.write("<td colspan='" + maxColumns + "'>"
                     + newLine);
               this.htmlFile.write(cell);
               this.htmlFile.write("</td>");
               cellIndex++;
            }
         } else {
            for (String cell : row) {
               this.htmlFile.write("<td " + widths.get(cellIndex) + ">"
                     + newLine);
               this.htmlFile.write(cell);
               this.htmlFile.write("</td>");
               cellIndex++;
            }
         }
         this.htmlFile.write("</tr>");
      }
      this.htmlFile.write("</table>");
      this.appendBreak();
   }

   /**
    * this method is used to retrieve html formatted string representing a block
    * of input code
    *
    * @param code
    *           String of 'code' to be written to html doc
    *
    * @return html formatted code block
    */
   public String getCodeBlock(String code) {
      StringBuilder sb = new StringBuilder();
      sb.append("<pre><code>" + newLine);
      sb.append(code + newLine);
      sb.append("</code></pre>" + newLine);
      return sb.toString();
   }

   /**
    * this method is used to parse and append a table representation of a
    * signature map from a sailpoint Object into a table consisting of name,
    * descript,type tris.
    *
    * @param node
    *           the Element to be parsed for signature
    *
    * @throws IOException
    */
   public void documentSignature(Element node) throws IOException {
      NodeList nl = node.getElementsByTagName("Signature");
      Element sigNode = (Element) nl.item(0);
      if (sigNode != null) {

         HashMap<String, List<HashMap<String, String>>> sigMap = getSignatureMap(node);
         List<List<String>> table = new ArrayList<List<String>>();
         List<String> row = new ArrayList<String>();

         Iterator<Map.Entry<String, List<HashMap<String, String>>>> it = sigMap
               .entrySet().iterator();
         while (it.hasNext()) {
            String key = it.next().getKey();

            row = new ArrayList<String>();
            row.add(key);
            table.add(row);

            row = new ArrayList<String>();
            row.add("<b>Name</b>");
            row.add("<b>Description</b>");
            row.add("<b>Type</b>");
            table.add(row);

            List<HashMap<String, String>> args = sigMap.get(key);
            for (HashMap<String, String> arg : args) {
               row = new ArrayList<String>();
               row.add(arg.get("name"));
               row.add(arg.get("description"));
               row.add(arg.get("type"));
               table.add(row);
            }

         }
         String anchor = this.getAnchor("Signatures");
         this.appendTable(table, anchor);
      }
   }

   /**
    * this method is used to parse and append a table representation of a
    * signature map from a sailpoint Object into a table consisting of name,
    * descript,type tris.
    *
    * @param node
    *           the Element to be parsed for signature
    *
    * @throws IOException
    */
   public void documentDescription(List<List<String>> table, Element node)
         throws IOException {
      List<String> row = new ArrayList<String>();
      NodeList attrList = node.getElementsByTagName("Attributes");
      Element attr = (Element) attrList.item(0);
      if (attr != null) {

         HashMap<String, List<String>> attributes;
         try {
            attributes = getAttributesMap(attr);
            for (String attrName : attributes.keySet()) {
               if (attrName.equals("en_US")) {
                  List<String> attrValue = (List<String>) attributes.get(attrName);
                  row = new ArrayList<String>();
                  row.add("Description");
                  row.add(attrValue.get(0).toString());
                  table.add(row);
               }

            }
         } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }

         // if(sigNode.getLocalName();

      }

      // String anchor = this.getAnchor("Description");
      // this.appendTable(table, anchor);
   }

   /**
    * this method is used to get a table (default heading) representation of the
    * tag data associated with a sailpoint object (name, created, modified,
    * etc...)
    *
    * @param node
    *           the Element to be parsed for tag attributes
    *
    * @return List representing a table of sailpoint object tag data
    */
   public List getTableForXmlTagAttributes(Element node) {
      return getTableForXmlTagAttributes(node, "");
   }

   /**
    * this method is used to get a table (specified heading) representation of
    * the tag data associated with a sailpoint object (name, created, modified,
    * etc...)
    *
    * @param node
    *           the Element to be parsed for tag attributes
    * @param tableHeading
    *           String to be used as table header
    *
    * @return List representing a table of sailpoint object tag data
    */
   public List getTableForXmlTagAttributes(Element node, String tableHeading) {
      List<List<String>> table = new ArrayList<List<String>>();
      List<String> row = null;
      String tableName = getNodeAttribute(node, "name");

      if (tableHeading.length() > 0) {
         tableName = tableHeading;
      }
      if (tableName.length() > 0) {
         row = new ArrayList<String>();
         row.add(tableName);
         table.add(row);
      }

      row = new ArrayList<String>();
      row.add("Name");
      row.add("Value");

      NamedNodeMap nnm = node.getAttributes();
      for (int i = 0; i < nnm.getLength(); i++) {
         row = new ArrayList<String>();
         String attrName = nnm.item(i).getNodeName();
         String attrVal = nnm.item(i).getNodeValue();
         row.add(attrName);
         if (attrVal.toLowerCase().startsWith("rule:")) {
            List<String> parts = new ArrayList<String>();
            parts.add("Rule");
            parts.add(attrVal.substring(5));
            String link = this.getLinkForObject(parts);
            row.add(link);
         } else {
            row.add(attrVal);
         }
         if (!attrName.equalsIgnoreCase("name")) {
            table.add(row);
         }
      }
      return table;
   }

   /**
    * this method is used to get a string representation of an html link pointed
    * to the input reference (no bookmark)
    *
    * @param refParts
    *           List of attributes from reference tag, from which to build link
    *
    * @return String html link pointed to the input reference
    */
   public String getLinkForObject(List<String> refParts) {
      return getLinkForObject(refParts,
            refParts.get(0) + ":" + refParts.get(1), "");

   }

   /**
    * this method is used to get a string representation of an html link pointed
    * to the input reference (bookmark)
    *
    * @param refParts
    *           List of attributes from reference tag, from which to build link
    * @param bookmark
    *           bookmark to be used for intra page linking
    *
    * @return String html link pointed to the input reference
    */
   public String getLinkForObjects(List<String> refParts, String bookmark) {
      return getLinkForObject(refParts,
            refParts.get(0) + ":" + refParts.get(1), bookmark);
   }

   /**
    * this method is used to get a string representation of an html link pointed
    * to the input reference (bookmark) with inner text equal to input
    *
    * @param refParts
    *           List of attributes from reference tag, from which to build link
    * @param text
    *           String to be used as the inner text of the html link tag
    * @param bookmark
    *           bookmark to be used for intra page linking
    *
    * @return String html link pointed to the input reference with inner text
    */
   public String getLinkForObject(List<String> refParts, String text,
         String bookmark) {
      String fileName = getFilenameForObject(refParts.get(1));
      String relativePath = "../" + refParts.get(0) + "/" + fileName;

      if (bookmark.length() > 0) {
         if (!bookmark.startsWith("#")) {
            bookmark = "#" + bookmark;
         }
      }
      String link = this.getLink(relativePath + bookmark, text);
      return link;
   }

   /**
    * this method is used to get a string representation of an html link pointed
    * to the input href with specified inner text
    *
    * @param href
    *           destination link
    * @param text
    *           inner text for html link node
    *
    * @return String html link pointed to the input href
    */
   public String getLink(String href, String text) {
      String ret = "<a href='" + href + ".html'>" + text + "</a>";
      if (href.indexOf("#") > -1) {
         ret = "<a href='" + href + "'>" + text + "</a>";
      }
      return ret;
   }

   /**
    * this method is used to get a string representation of an html link pointed
    * to the input href, with a title and inner text as specified
    *
    * @param href
    *           href to be linked
    * @param text
    *           inner text to be used for the html tag
    * @param title
    *           title to be used in the href tag
    *
    * @return String html link pointed to the input href with title and text
    */
   public String getLink(String href, String text, String title) {
      String titleEncoded = encodeHTML(title);
      String ret = "<a href='" + href + ".html' title='" + titleEncoded + "'>"
            + text + "</a>";
      if (href == null) {
         href = "javascript:void()";
      }
      if (href.indexOf("#") > -1) {
         ret = "<a href='" + href + "' title='" + titleEncoded + "'>" + text
               + "</a>";
      }
      return ret;
   }

   /**
    * this method is used retrieve a reference attribute(s) from the provided
    * Element of the Document
    *
    * @param node
    *           Element from which to begin search for reference Element
    * @param tagName
    *           tag name to be used to search for specific type of reference
    *           (rule library, rule, workflow etc)
    *
    * @return CSV list of reference Strings
    */
   public String getReference(Element node, String tagName) {
      String reference = "";
      Element tag = (Element) node.getElementsByTagName(tagName).item(0);
      if (tag != null) {
         NodeList nl = tag.getElementsByTagName("Reference");
         List<String> refs = new ArrayList<String>();
         for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            reference = getReference(e);
            refs.add(reference);
         }
         reference = StringUtils.join(refs, ",");
      }
      return reference;
   }

   /**
    * this method is used build a reference for an input Element - class
    * name:object name
    *
    * @param node
    *           Element from which to begin search for reference Element
    *
    * @return String reference built from class name and object name
    */
   public String getReference(Element node) {
      String reference = "";
      String className = getNodeAttribute(node, "class");
      int index = className.lastIndexOf(".");
      if (index > 0) {
         className = className.substring(index + 1);
      }
      String objectName = getNodeAttribute(node, "name");
      reference = className + ":" + objectName;

      return reference;
   }

   /**
    * this method is used to build a reference list for the input class type
    * when no reference parts from the original xml document are available
    *
    * @param node
    *           Element from which to begin search for reference Element
    * @param tagName
    *           tag name to be used to search for specific type of reference
    *           (rule library, rule, workflow etc)
    * @param className
    *           class name for which to build reference
    *
    * @return list of reference Strings
    */
   public List<String> getReferencePartsNoReference(Element node,
         String tagName, String className) {
      List<String> reference = new ArrayList<String>();
      NodeList entryList = node.getElementsByTagName("entry");
      for (int i = 0; i < entryList.getLength(); i++) {
         Element entry = (Element) entryList.item(i);
         if (entry.getAttribute("key").equalsIgnoreCase(tagName)) {
            String value = entry.getAttribute("value");
            reference.add(className);
            reference.add(value);
            break;
         }
      }
      return reference;
   }

   /**
    * this method is used to build a reference list when no reference parts from
    * the original xml document are available
    *
    * @param node
    *           Element from which to begin search for reference Element
    * @param tagName
    *           tag name to be used to search for specific type of reference
    *           (rule library, rule, workflow etc)
    *
    * @return list of reference Strings
    */
   public List<String> getReferenceParts(Element node, String tagName) {
      List<String> parts = new ArrayList<String>();
      NodeList tagList = node.getElementsByTagName(tagName);
      if (tagList.getLength() > 0) {
         NodeList nl = ((Element) tagList.item(0))
               .getElementsByTagName("Reference");
         if (nl.getLength() > 0) {
            Element rule = (Element) nl.item(0);
            if (rule != null) {
               parts = getReferenceParts(rule);
            }
         }
      }

      return parts;
   }

   /**
    * this method is used to build a reference list when no reference parts from
    * the original xml document are available
    *
    * @param node
    *           Element from which to begin search for reference Element
    *
    * @return list of reference Strings
    */
   public List<String> getReferenceParts(Element node) {
      List<String> reference = new ArrayList<String>();
      String className = node.getAttribute("class");
      int index = className.lastIndexOf(".");
      if (index > 0) {
         className = className.substring(index + 1);
      }
      String objectName = node.getAttribute("name");
      reference.add(className);
      reference.add(objectName);
      return reference;
   }

   /**
    * this method is used to build a reference list when no reference parts from
    * the original xml document are available
    *
    * @param table
    *           table that should have row appended to it
    * @param node
    *           Element from which to get reference row
    * @param referenceTag
    *           Tag to be used for reference row
    */
   public void addReferenceRowToTable(List<List<String>> table, Element node,
         String referenceTag) {
      List<String> row = getReferenceRow(node, referenceTag);
      table.add(row);
   }

   /**
    * this method returns a List of Strings representing an html table row
    * containing the input reference tag
    *
    * @param node
    *           Element from which to begin search for reference Element
    * @param referenceTag
    *           reference to be added to the row List
    *
    * @return list representing table row for a reference
    */
   public List<String> getReferenceRow(Element node, String referenceTag) {
      String reference = getReference(node, referenceTag);
      List<String> row = new ArrayList<String>();
      row.add(referenceTag);
      row.add(reference);
      return row;
   }

   /**
    * this method returns a list representation of a table row containing text
    * from a given node and path
    *
    * @param node
    *           Element from which to begin search for node text
    * @param path
    *           path to find correct node from which to get value for row
    *
    * @return list representing table row with text for a given node
    */
   public List<String> getTagTextValueRow(Element node, String path) {
      return this.getTagTextValueRow(node, path, path);
   }

   /**
    * this method returns a list representation of a table row containing text
    * from a given node and path, plus description
    *
    * @param node
    *           Element from which to begin search for node text
    * @param path
    *           path to find correct node from which to get value for row
    * @param description
    *           description to be added to the row
    *
    * @return list representing table row with text and description for a given
    *         node
    */
   public List<String> getTagTextValueRow(Element node, String path,
         String description) {
      String text = getNodeTextForPath(node, path);
      List<String> row = new ArrayList<String>();
      row.add(description);
      row.add(text);
      return row;
   }

   /**
    * this method returns a List of Lists representing a table and rows filled
    * with the name/value/script for an input Document Element
    *
    * @param node
    *           Element from which to build table
    *
    * @return list of lists representing table filled with arguments
    */
   public List<List<String>> getTableForArguments(Element node) {
      List<List<String>> table = new ArrayList<List<String>>();
      List<String> row = new ArrayList<String>();
      NodeList args = node.getElementsByTagName("Arg");
      for (int i = 0; i < args.getLength(); i++) {
         Element arg = (Element) args.item(i);
         String argName = getNodeAttribute(arg, "name");
         String argValue = getNodeAttribute(arg, "value");
         String script = getNodeAttribute(arg, "Script");

         if (argValue.length() > 0) {
            row = new ArrayList<String>();
            row.add(argName);
            row.add(argValue);
            table.add(row);
         } else {
            row = new ArrayList<String>();
            row.add(argName);
            row.add("Script Block");
            table.add(row);
            row = new ArrayList<String>();
            row.add(this.getCodeBlock(script));
            table.add(row);
         }
      }
      return table;
   }

   /**
    * this method appends a table to the HTML document with the xml objects tag
    * attributes, with the provided table heading
    *
    * @param node
    *           Element from which to build table
    * @param heading
    *           heading for the table
    *
    * @throws IOException
    */
   public void documentXmlTagAttributes(Element node, String heading)
         throws IOException {
      List<List<String>> table = getTableForXmlTagAttributes(node);
      appendTable(table, heading);
   }

   /**
    * this method appends a table to the HTML document with the xml objects tag
    * attributes, with default table heading
    *
    * @param node
    *           Element from which to build table
    *
    * @throws IOException
    */
   public void documentXmlTagAttributes(Element node) throws IOException {
      List<List<String>> table = getTableForXmlTagAttributes(node);
      appendTable(table);
   }

   /**
    * this method appends a code block to the html file
    *
    * @param code
    *           Element from which to build table
    *
    * @throws IOException
    */
   public void appendCodeBlock(String code) throws IOException {
      this.htmlFile.write(this.getCodeBlock(code));
   }

   /**
    * this method appends an anchor line to the html document
    *
    * @param name
    *           anchor name
    * @param text
    *           text for html tag
    *
    * @throws IOException
    */
   public void appendAnchor(String name, String text) throws IOException {
      this.appendLine(this.getAnchor(name, text));
   }

   /**
    * this method formats a string and then appends to the html document and a
    * newline character
    *
    * @param format
    *           String containing dynamic elements to be sub'd
    * @param args
    *           Object Array of arguments to sub into the provded String
    *
    * @throws IOException
    */
   public void appendLine(String format, Object[] args) throws IOException {
      this.htmlFile.write(String.format(format, args) + newLine);
   }

   /**
    * this method appends a String to the HTML document and a new line character
    *
    * @param format
    *           String containing dynamic elements to be sub'd
    *
    * @throws IOException
    */
   public void appendLine(String format) throws IOException {
      this.htmlFile.write(format + newLine);
   }

   /**
    * this method returns the max number of columns in a table
    *
    * @param table
    *           List of Lists representing a table
    *
    */
   public int getMaxTableCols(List<List<String>> table) {
      int max = 1;
      for (List<String> subList : table) {
         if (subList.size() > max) {
            max = subList.size();
         }
      }
      return max;
   }

   /**
    * this method appends an anchor line to the html document
    *
    * @param node
    *           root Element object to search for description Element
    * @param table
    *           table to append the description to
    *
    */
   public void addDescriptionToTable(Element node, List<List<String>> table) {
      if (node.getElementsByTagName("Description").getLength() > 0) {
         List<String> row = new ArrayList<String>();
         row.add("Description");
         row.add(node.getElementsByTagName("Description").item(0)
               .getTextContent());
         table.add(row);
      } else {
         // no description
      }
   }
}
