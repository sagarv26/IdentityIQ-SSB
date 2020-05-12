package sailpoint.services.tools.docgen;

import static sailpoint.services.tools.docgen.DocumentizerUtils.getFilenameForObject;
import static sailpoint.services.tools.docgen.DocumentizerUtils.getRealRoot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Created by adam.creaney on 3/24/16.
 */
public class DocForm extends DocBase {

   public String seperator;
   private static Element node;

   public static void doDocumentation(ArrayList<File> files)
         throws ParserConfigurationException, SAXException, IOException, TransformerException {
      if (files != null && !files.isEmpty()) {
         if (!DocBase.objectList.isEmpty()) {
            DocBase.objectList.clear();
         }
         for (File f : files) {
            docObject(f);
         }
         Documentizer.base.setObjectType(DocBase.DOCTYPE.Form.name());
         Documentizer.base.buildFrameList();
      }
   }

   public static void docObject(File file) throws IOException, SAXException,
         ParserConfigurationException, TransformerException {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      // TODO: Why/how can we avoid the dtd processing besides this line?
      dbFactory.setFeature(
            "http://apache.org/xml/features/nonvalidating/load-external-dtd",
            false);

      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

      InputStream is = new FileInputStream(file);
      Document doc = dBuilder.parse(is);

      doc.getDocumentElement().normalize();

        try{
            Element root = getRealRoot(doc.getDocumentElement(), "Form");

         node = root;

         Documentizer.base.docWriter = new DocWriter(DocBase.destination, root,
               DocBase.DOCTYPE.Form);

         if (node.getTagName().equalsIgnoreCase("ImportAction")) {
            node = (Element) node.getFirstChild();
         }
         Documentizer.base.name = node.getAttribute("name");
         Documentizer.base.docWriter.appendH1(Documentizer.base.name);
         DocBase.objectList.put(getFilenameForObject(Documentizer.base.name),
               Documentizer.base.name);


         buildNavigationTable();

         Documentizer.base.docWriter.documentAttributes(node);
         documentButtons();
         documentSections();
        }  catch(NullPointerException e ){        	  
      	   
        }
        catch (Exception e) {
        	e.printStackTrace();
     	} finally {
         if (Documentizer.base.docWriter != null) {
            Documentizer.base.docWriter.close();
         }
      }
   }

   static void buildNavigationTable() throws IOException {
      List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
      List<String> row = new ArrayList<String>();
      row.add(Documentizer.base.docWriter.getLink("#Attributes", "Attributes"));
      table.add(row);

      row = new ArrayList<String>();
      row.add(Documentizer.base.docWriter.getLink("#Sections", "Sections"));
      table.add(row);

      row = new ArrayList<String>();
      row.add(Documentizer.base.docWriter.getLink("#Buttons", "Buttons"));
      table.add(row);

      try {
         Documentizer.base.docWriter.appendTable(table, "Structure");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private static void documentButtons() throws IOException {
      List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
      List<String> row = new ArrayList<String>();
      row.add("<b>Action</b>");
      row.add("<b>Label</b>");
      table.add(row);
      NodeList buttons = node.getElementsByTagName("Button");
      for (int i = 0; i < buttons.getLength(); i++) {
         row = new ArrayList<String>();
         Element button = (Element) buttons.item(i);
         String action = button.getAttribute("action");
         String label = button.getAttribute("label");
         row.add(action);
         row.add(label);
         table.add(row);
      }

      Documentizer.base.docWriter.appendTable(table, "Buttons");
   }

   private static void documentSections() throws IOException,
         TransformerException {
      String anchor = Documentizer.base.docWriter.getAnchor("Sections");
      Documentizer.base.docWriter.appendTable(anchor);

      List<List<String>> table = new ArrayList<List<String>>();
      List<String> row = new ArrayList<String>();

      NodeList sections = node.getElementsByTagName("Section");
      for (int i = 0; i < sections.getLength(); i++) {

         Element section = (Element) sections.item(i);

         String displayName = section.getAttribute("label");
         String name = section.getAttribute("name");
         String label = section.getAttribute("label");
         String type = section.getAttribute("type");
         String cols = section.getAttribute("columns");

         if (displayName == null) {
            displayName = name;
         }

         Documentizer.base.docWriter.appendHorizontalRule();
         Documentizer.base.docWriter.append("<div class='collapse'>");
         Documentizer.base.docWriter.appendH3(displayName);
         Documentizer.base.docWriter.append("<div>");

         table = new ArrayList<List<String>>();
         row = new ArrayList<String>();

         row.add("<b>Argument</b>");
         row.add("<b>Value</b>");
         table.add(row);

         row = new ArrayList<String>();
         row.add("name");
         row.add(name);
         table.add(row);

         row = new ArrayList<String>();
         row.add("label");
         row.add(label);
         table.add(row);

         row = new ArrayList<String>();
         row.add("type");
         row.add(type);
         table.add(row);

         row = new ArrayList<String>();
         row.add("columns");
         row.add(cols);
         table.add(row);

         Documentizer.base.docWriter.appendTable(table, displayName);

         // now to get the possible field attributes
         Documentizer.base.docWriter.documentAttributes(section);

         NodeList fields = section.getElementsByTagName("Field");
         if (fields.getLength() > 0) {
            Documentizer.base.docWriter.appendH3("Fields");
         }
         for (int j = 0; j < fields.getLength(); j++) {
            Element field = (Element) fields.item(j);
            table = Documentizer.base.docWriter.getTableEmpty();
            row = new ArrayList<String>();
            row.add("<b>Argument</b>");
            row.add("<b>Value</b>");
            table.add(row);

            row = new ArrayList<String>();
            row.add("name");
            row.add(field.getAttribute("name"));
            table.add(row);

            row = new ArrayList<String>();
            row.add("displayName");
            row.add(field.getAttribute("displayName"));
            table.add(row);

            row = new ArrayList<String>();
            row.add("helpKey");
            row.add(field.getAttribute("helpKey"));
            table.add(row);

            row = new ArrayList<String>();
            row.add("filterString");
            row.add(field.getAttribute("filterString"));
            table.add(row);

            row = new ArrayList<String>();
            row.add("multi");
            row.add(field.getAttribute("multi"));
            table.add(row);

            // Validation Scripts
            if (field.getElementsByTagName("Description").getLength() > 0) {
               row = new ArrayList<String>();
               row.add("Description");
               String desc = field.getElementsByTagName("Description").item(0)
                     .getTextContent();
               row.add(desc);
               table.add(row);
            }

            // RuleRef
            if (field.getElementsByTagName("RuleRef").getLength() > 0) {
               row = new ArrayList<String>();
               Element validRules = (Element) field.getElementsByTagName(
                     "RuleRef").item(0);
               NodeList refs = validRules.getElementsByTagName("Reference");
               for (int k = 0; k < refs.getLength(); k++) {
                  row = new ArrayList<String>();
                  List<String> ruleRef = Documentizer.base.docWriter
                        .getReferenceParts((Element) refs.item(k));
                  String link = Documentizer.base.docWriter
                        .getLinkForObject(ruleRef);

                  row.add("RuleRef");
                  row.add(link);
               }
               table.add(row);
            }

            // Validation Rule
            if (field.getElementsByTagName("ValidationRule").getLength() > 0) {
               row = new ArrayList<String>();
               Element validRules = (Element) field.getElementsByTagName(
                     "ValidationRule").item(0);
               NodeList refs = validRules.getElementsByTagName("Reference");
               for (int k = 0; k < refs.getLength(); k++) {
                  row = new ArrayList<String>();
                  List<String> ruleRef = Documentizer.base.docWriter
                        .getReferenceParts((Element) refs.item(k));
                  String link = Documentizer.base.docWriter
                        .getLinkForObject(ruleRef);

                  row.add("ValidationRule");
                  row.add(link);
               }
               table.add(row);
            }

            // Validation Script
            if (field.getElementsByTagName("ValidationScript").getLength() > 0) {
               row = new ArrayList<String>();
               String script = field.getElementsByTagName("ValidationScript")
                     .item(0).getTextContent();
               row.add("ValidationScript");
               row.add(script);
               table.add(row);
            }

            // Script
            if (field.getElementsByTagName("Script").getLength() > 0) {
               row = new ArrayList<String>();
               String script = field.getElementsByTagName("Script").item(0)
                     .getTextContent();
               row.add("Script");
               row.add(script);
               table.add(row);
            }
            // Allowed Values
            if (field.getElementsByTagName("AllowedValues").getLength() > 0) {
               row = new ArrayList<String>();
               Element avs = (Element) field.getElementsByTagName(
                     "AllowedValues").item(0);
               List<String> allowed = new ArrayList<String>();
               NodeList nl = avs.getElementsByTagName("String");
               for (int l = 0; l < nl.getLength(); l++) {
                  allowed.add(nl.item(l).getTextContent());
               }

               String csv = StringUtils.join(allowed, ",");
               row.add("AllowedValues");
               row.add(csv);
               table.add(row);
            }
            // Values
            if (field.getElementsByTagName("Value").getLength() > 0) {
               row = new ArrayList<String>();
               Element avs = (Element) field.getElementsByTagName("Value")
                     .item(0);
               String text = avs.getTextContent();
               row.add("Value");
               row.add(text);
               table.add(row);
            }

            Documentizer.base.docWriter.appendTable(table,
                  field.getAttribute("name"));
            Documentizer.base.docWriter.documentAttributes(field);

         }
         Documentizer.base.docWriter.append("</div></div>");
      }
   }
}
