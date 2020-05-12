package sailpoint.services.tools.docgen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static sailpoint.services.tools.docgen.DocumentizerUtils.*;

/**
 * Created by adam.creaney on 3/24/16.
 */
public class DocConfiguration extends DocBase {

    public String seperator;
    private static Element node;
    
    public static void doDocumentation(ArrayList<File> files) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        if(files != null && !files.isEmpty()) {
        	if(!DocBase.objectList.isEmpty()) {
	    		DocBase.objectList.clear();
	    	}
        	for(File f : files){
	            docObject(f);
	        }
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.Configuration.name());
	        Documentizer.base.buildFrameList();
        }
    }

    public static void docObject(File file) throws IOException, SAXException, ParserConfigurationException, TransformerException{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        //TODO: Why/how can we avoid the dtd processing besides this line?
        dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputStream is = new FileInputStream(file);
        Document doc = dBuilder.parse(is);

        doc.getDocumentElement().normalize();

        try{
            Element root = getRealRoot(doc.getDocumentElement(), "Configuration");

            node = root;

            Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.Configuration);


            if(node.getTagName().equalsIgnoreCase("ImportAction")){
                node = (Element) node.getFirstChild();
            }
            Documentizer.base.name = node.getAttribute("name");
            Documentizer.base.docWriter.appendH1(Documentizer.base.name);
            DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);

            if(Documentizer.base.name.equalsIgnoreCase("AuditConfig")){
                documentAuditConfig();
            }

            if(Documentizer.base.name.equalsIgnoreCase("WebResources")){
                documentWebResource();
            }

            Documentizer.base.docWriter.documentAttributes(node);
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

    private static void documentAuditConfig() throws IOException {
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();

        row.add("<b>Name</b>");
        row.add("<b>Enabled</b>");
        table.add(row);

        NodeList actions = node.getElementsByTagName("AuditAction");
        for(int i = 0; i < actions.getLength(); i++){
            Element action = (Element) actions.item(i);
            row = new ArrayList<String>();
            row.add(action.getAttribute("name"));
            String enabled = action.getAttribute("enabled");
            if(!enabled.equalsIgnoreCase("true")){
                enabled = "false";
            }
            row.add(enabled);
            table.add(row);
        }
        Documentizer.base.docWriter.appendTable(table, "AuditActions");
    }

    private static void documentWebResource() throws IOException{
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();

        row.add("<b>Rights</b>");
        row.add("<b>URL</b>");
        table.add(row);

        NodeList webResources = node.getElementsByTagName("WebResource");
        for(int i = 0; i < webResources.getLength(); i++){
            Element webResource = (Element) webResources.item(i);
            row = new ArrayList<String>();
            row.add(webResource.getAttribute("rights"));
   

            table.add(row);
        }
        Documentizer.base.docWriter.appendTable(table, "WebResources");
    }
}
