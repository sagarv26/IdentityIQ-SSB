package sailpoint.services.tools.docgen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static sailpoint.services.tools.docgen.DocumentizerUtils.*;

/**
 * Created by Shashank.Dubey on 02/14/2017
 */
public class DocDynamicScope extends DocBase{
    public String seperator;
    private static Element node;
    
    public static void doDocumentation(ArrayList<File> files) throws ParserConfigurationException, SAXException, IOException {
        if(files != null && !files.isEmpty()) {
        	if(!DocBase.objectList.isEmpty()) {
	    		DocBase.objectList.clear();
	    	}
        	for(File f : files){
	            docObject(f);
	        }
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.DynamicScope.name());
	        Documentizer.base.buildFrameList();
        }
    }

    @SuppressWarnings("unchecked")
	public static void docObject(File file) throws IOException, SAXException, ParserConfigurationException{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        //TODO: Why/how can we avoid the dtd processing besides this line?
        dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputStream is = new FileInputStream(file);
        Document doc = dBuilder.parse(is);

        doc.getDocumentElement().normalize();

        try{
            Element root = getRealRoot(doc.getDocumentElement(), "DynamicScope");

            node = root;
            
            Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.DynamicScope);

            Documentizer.base.name = node.getAttribute("name");
            Documentizer.base.docWriter.appendH1(Documentizer.base.name);
            DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);

            List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes(node);
            Documentizer.base.docWriter.addReferenceRowToTable(table, node, "ApplicationRequestControl");
            Documentizer.base.docWriter.addReferenceRowToTable(table, node, "ManagedAttributeRequestControl");
            Documentizer.base.docWriter.addReferenceRowToTable(table, node, "RoleRequestControl");
            Documentizer.base.docWriter.appendTable(table);
            Documentizer.base.docWriter.addDescriptionToTable(node, table);
            buildNavigationTable();
            documentIdentitySelector();
            documentPopulationRequestAuthority();
   
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

        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#IdentitySelector", "IdentitySelector"));
        table.add(row);
        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#PopulationRequestAuthority", "PopulationRequestAuthority"));
        table.add(row);
        Documentizer.base.docWriter.appendTable(table, "Structure");
    }
    
	private static void documentIdentitySelector() throws IOException{
		List<List<String>> table1 = Documentizer.base.docWriter.getTableEmpty();
		List<String> row = new ArrayList<String>();
		Element resource = (Element) node.getElementsByTagName("MatchExpression").item(0);
		NodeList matchtermlist = resource.getElementsByTagName("MatchTerm");
        //List<List<String>> table1 = Documentizer.base.docWriter.getTableForXmlTagAttributes(resource);
		//matchTerm.getAttribute(name);
		
		for(int i=0; i<matchtermlist.getLength(); i++){
			row = new ArrayList<String>();
			Element matchTerm = (Element)matchtermlist.item(i);
			row.add(matchTerm.getAttribute("name"));
			row.add(matchTerm.getAttribute("value"));
			row.add(Documentizer.base.docWriter.getReference( matchTerm, "ApplicationRef"));
			table1.add(row);
			
		}
		
		
		//matchTerm.getAttribute(name);
		
        String anchor = Documentizer.base.docWriter.getAnchor("IdentitySelector");
        Documentizer.base.docWriter.appendTable(table1, anchor);    
	}
	
	private static void documentPopulationRequestAuthority() throws IOException{
		List<List<String>> table2 = Documentizer.base.docWriter.getTableEmpty();
		List<String> row = new ArrayList<String>();
		Element populationreqauth = (Element) node.getElementsByTagName("PopulationRequestAuthority").item(0);
		Element matchConfig = (Element) populationreqauth.getElementsByTagName("MatchConfig").item(0);
		
		Documentizer.base.docWriter.append("<div>");                   
        
		row = new ArrayList<String>();
        row.add("<b>enableSubordinateControl</b>");
        row.add("<b>maxHierarchyDepth</b>");
        row.add("<b>subordinateOption</b>");
        table2.add(row);
		
		
		row = new ArrayList<String>();
        row.add(matchConfig.getAttribute("enableSubordinateControl"));
        row.add(matchConfig.getAttribute("maxHierarchyDepth"));
        row.add(matchConfig.getAttribute("subordinateOption"));
        table2.add(row);
        
       Documentizer.base.docWriter.append("</div>");
        Documentizer.base.docWriter.appendHorizontalRule();
        String anchor = Documentizer.base.docWriter.getAnchor("PopulationRequestAuthority");
        Documentizer.base.docWriter.appendTable(table2, anchor);    
	}

}
