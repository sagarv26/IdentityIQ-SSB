package sailpoint.services.tools.docgen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static sailpoint.services.tools.docgen.DocumentizerUtils.*;

/**
 * Created by adam.creaney on 3/24/16.
 */
public class DocCorrelationConfig extends DocBase {
    public String seperator;
    private static Element node;

    static HashMap<String, String> variableValues;

    public static void doDocumentation(ArrayList<File> files) throws ParserConfigurationException, SAXException, IOException {
        if(files != null && !files.isEmpty()) {
        	if(!DocBase.objectList.isEmpty()) {
	    		DocBase.objectList.clear();
	    	}
        	for(File f : files){
	            docObject(f);
	        }
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.CorrelationConfig.name());
	        Documentizer.base.buildFrameList();
        }
    }

    public static void docObject(File file) throws IOException, SAXException, ParserConfigurationException{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        //TODO: Why/how can we avoid the dtd processing besides this line?
        dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputStream is = new FileInputStream(file);
        Document doc = dBuilder.parse(is);

        doc.getDocumentElement().normalize();

        try{
            Element root = getRealRoot(doc.getDocumentElement(), "CorrelationConfig");

            node = root;

            Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.CorrelationConfig);
            variableValues = new HashMap<String, String>();

            Documentizer.base.name = root.getAttribute("name");

            DocBase.objectList.put(DocumentizerUtils.getFilenameForObject(Documentizer.base.name), Documentizer.base.name);

            Documentizer.base.docWriter.appendH1(Documentizer.base.name);
            documentCorrelation();

        }  catch(NullPointerException e ){        	  
      	   
        }
        catch (Exception e) {
        	e.printStackTrace();
     	}
        finally{
            if(Documentizer.base.docWriter != null){
            	Documentizer.base.docWriter.close();
            }
        }
    }

    private static void documentCorrelation() throws IOException {
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();
        row.add("<b>Property</b>");
        row.add("<b>Operation</b>");
        row.add("<b>Value</b>");
        table.add(row);

        Element attrAssigns = (Element) node.getElementsByTagName("AttributeAssignments").item(0);
        if(attrAssigns != null){

            NodeList filters = attrAssigns.getElementsByTagName("Filter");
            for(int i = 0; i < filters.getLength(); i++){
                row = new ArrayList<String>();
                Element filter = (Element) filters.item(i);
                String op = filter.getAttribute("operation");
                String prop = filter.getAttribute("property");
                String val = filter.getAttribute("value");
                row.add(prop);
                row.add(op);
                row.add(val);

                table.add(row);
            }
        }

        Documentizer.base.docWriter.appendTable(table, "AttributeAssignments");
    }
}
