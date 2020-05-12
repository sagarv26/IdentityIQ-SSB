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
 * Created by Shashank.Dubey on 02/16/17.
 */

public class DocGroupDefinition extends DocBase{
	
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
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.GroupDefinition.name());
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
               Element root = getRealRoot(doc.getDocumentElement(), "GroupDefinition");

               node = root;

               Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.GroupDefinition);

               Documentizer.base.name = node.getAttribute("name");
               Documentizer.base.docWriter.appendH1(Documentizer.base.name);
               DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);
               List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes(node);
               Documentizer.base.docWriter.addReferenceRowToTable(table, node, "Owner");
               Documentizer.base.docWriter.addDescriptionToTable(node, table);              
               Documentizer.base.docWriter.appendTable(table);
               
               buildNavigationTable();
			   documentGroupFilter(); 
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

           row.add(Documentizer.base.docWriter.getLink("#CompositeFilter", "CompositeFilter"));
           table.add(row);
             
           Documentizer.base.docWriter.appendTable(table, "Structure");
       }

	private static void documentGroupFilter() throws IOException{
		List<List<String>> tableGrpfilter = Documentizer.base.docWriter.getTableEmpty();
		List<String> row = new ArrayList<String>();
		Element resource = (Element) node.getElementsByTagName("CompositeFilter").item(0);
		String anchor = Documentizer.base.docWriter.getAnchor("CompositeFilter");


		if(resource.getAttribute("operation").equals("AND"))			
			row.add("CompositeFilter operation: AND");
		else
			row.add("CompositeFilter operation: OR");
		tableGrpfilter.add(row);	
		
        Documentizer.base.docWriter.append("<div class='collapse'>");                   
        Documentizer.base.docWriter.appendH3(anchor);
        Documentizer.base.docWriter.append("<div>"); 

        row = new ArrayList<String>();
        row.add("<b>property</b>");
        row.add("<b>operation</b>");
        row.add("<b>matchMode</b>");
        row.add("<b>ignoreCase</b>");		
        tableGrpfilter.add(row);
		
		NodeList filterlist = resource.getElementsByTagName("Filter");
		
		for(int i=0; i<filterlist.getLength(); i++){
			row = new ArrayList<String>();
			Element filter = (Element)filterlist.item(i);
			row.add(filter.getAttribute("property"));
			row.add(filter.getAttribute("operation"));
			row.add(filter.getAttribute("matchMode"));	
			row.add(filter.getAttribute("ignoreCase"));		
			tableGrpfilter.add(row);
		}

			
			Documentizer.base.docWriter.appendTable(tableGrpfilter, anchor);
		}
	} 

