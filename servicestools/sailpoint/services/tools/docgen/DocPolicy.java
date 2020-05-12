package sailpoint.services.tools.docgen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static sailpoint.services.tools.docgen.DocumentizerUtils.*;

/**
 * Created by adam.creaney on 3/24/16.
 */
public class DocPolicy extends DocBase{
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
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.Policy.name());
	        Documentizer.base.buildFrameList();
        }
    }

    @SuppressWarnings("unchecked")
	public static void docObject(File file) throws IOException, SAXException, ParserConfigurationException, TransformerException{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        //TODO: Why/how can we avoid the dtd processing besides this line?
        dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputStream is = new FileInputStream(file);
        Document doc = dBuilder.parse(is);

        doc.getDocumentElement().normalize();

        try{
            Element root = getRealRoot(doc.getDocumentElement(), "Policy");

            node = root;

            Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.Policy);

            Documentizer.base.name = node.getAttribute("name");
            Documentizer.base.docWriter.appendH1(Documentizer.base.name);
            DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);

            List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes(node);
            Documentizer.base.docWriter.addReferenceRowToTable(table, node, "Owner");
            Documentizer.base.docWriter.addDescriptionToTable(node, table);
            Documentizer.base.docWriter.appendTable(table);

            buildNavigationTable();
            documentConstraints();

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

    private static void buildNavigationTable() throws IOException {
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();

        row.add(Documentizer.base.docWriter.getLink("#Attributes", "Attributes"));
        table.add(row);

        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#Contraints", "Constraints"));
        table.add(row);

        Documentizer.base.docWriter.appendTable(table, "Structure");
    }

    private static void documentConstraints() throws IOException{
        Element genCons = (Element) node.getElementsByTagName("GenericConstraints").item(0);
        if(genCons != null){
            NodeList genConList = genCons.getElementsByTagName("GenericConstraint");
            for(int i = 0; i < genConList.getLength(); i++){
                Element con = (Element) genConList.item(i);
                String name = con.getAttribute("name");
                Documentizer.base.docWriter.append("<div class='collapse'>");
                Documentizer.base.docWriter.appendH3(name);
                Documentizer.base.docWriter.append("<div>");
            }
        }
     
        if(genCons != null){
            NodeList genConList = genCons.getElementsByTagName("SODConstraint");
            for(int i = 0; i < genConList.getLength(); i++){
                Element con = (Element) genConList.item(i);
                String name = con.getAttribute("name");
                Documentizer.base.docWriter.append("<div class='collapse'>");
                Documentizer.base.docWriter.appendH3(name);
                Documentizer.base.docWriter.append("<div>");
            }
        }
    }
}
