package sailpoint.services.tools.docgen;

import static sailpoint.services.tools.docgen.DocumentizerUtils.getFilenameForObject;
import static sailpoint.services.tools.docgen.DocumentizerUtils.getRealRoot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DocPasswordPolicy extends DocBase {
   public static String seperator;
   private static Element node;

   public static void doDocumentation(ArrayList<File> files)
         throws ParserConfigurationException, SAXException, IOException {
      if (files != null && !files.isEmpty()) {
         if (!DocBase.objectList.isEmpty()) {
            DocBase.objectList.clear();
         }
         for (File f : files) {
            docObject(f);
         }
         Documentizer.base.setObjectType(DocBase.DOCTYPE.PasswordPolicy.name());
         Documentizer.base.buildFrameList();
      }
   }

   @SuppressWarnings("unchecked")
   public static void docObject(File file) throws IOException, SAXException,
         ParserConfigurationException {
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
            Element root = getRealRoot(doc.getDocumentElement(), "PasswordPolicy");

         node = root;

         Documentizer.base.docWriter = new DocWriter(DocBase.destination, root,
               DocBase.DOCTYPE.PasswordPolicy);

         Documentizer.base.name = node.getAttribute("name");
         Documentizer.base.docWriter.appendH1(Documentizer.base.name);
         DocBase.objectList.put(getFilenameForObject(Documentizer.base.name),
               Documentizer.base.name);
         List<List<String>> table = Documentizer.base.docWriter
               .getTableForXmlTagAttributes(node);
         Documentizer.base.docWriter.appendTable(table);
         buildNavigationTable();
         documentPasswordConstraints();

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

   private static void buildNavigationTable() throws IOException {
      List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
      List<String> row = new ArrayList<String>();

      row.add(Documentizer.base.docWriter.getLink("#PasswordConstraints",
            "Password Constraints"));
      table.add(row);

      Documentizer.base.docWriter.appendTable(table, "Structure");
   }

   private static void documentPasswordConstraints() throws IOException {

      Element passconst = (Element) node.getElementsByTagName(
            "PasswordConstraints").item(0);

      if (passconst != null) {

         HashMap<String, List<String>> attributes;
         try {
            attributes = DocumentizerUtils.getAttributesMap(passconst);

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

            String anchor = Documentizer.base.docWriter
                  .getAnchor("PasswordConstraints");
            Documentizer.base.docWriter.appendTable(table, anchor);
         } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

}
