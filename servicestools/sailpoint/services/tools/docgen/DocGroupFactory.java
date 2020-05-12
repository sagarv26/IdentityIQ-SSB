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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import sailpoint.services.tools.docgen.DocBase;
import sailpoint.services.tools.docgen.DocWriter;
import sailpoint.services.tools.docgen.Documentizer;

public class DocGroupFactory extends DocBase {
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
         Documentizer.base.setObjectType(DocBase.DOCTYPE.GroupFactory.name());
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
            Element root = getRealRoot(doc.getDocumentElement(), "GroupFactory");

         node = root;

         Documentizer.base.docWriter = new DocWriter(DocBase.destination, root,
               DocBase.DOCTYPE.GroupFactory);

         Documentizer.base.name = node.getAttribute("name");
         Documentizer.base.docWriter.appendH1(Documentizer.base.name);
         DocBase.objectList.put(getFilenameForObject(Documentizer.base.name),
               Documentizer.base.name);
         List<List<String>> table = Documentizer.base.docWriter
               .getTableForXmlTagAttributes(node);
         Documentizer.base.docWriter.addReferenceRowToTable(table, node,
               "GroupOwnerRule");
         Documentizer.base.docWriter.appendTable(table);

        }  catch(NullPointerException e ){        	  
      	   
        }
        catch (Exception e) {
        	e.printStackTrace();
     	}finally {
         if (Documentizer.base.docWriter != null) {
            Documentizer.base.docWriter.close();
         }
      }
   }

}
