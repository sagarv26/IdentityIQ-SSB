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
import java.util.HashMap;
import java.util.List;

import static sailpoint.services.tools.docgen.DocumentizerUtils.*;

/**
 * Created by Shashank Dubey on 02/10/2017
 */
public class DocCustom extends DocBase{
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
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.Custom.name());
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
            Element root = getRealRoot(doc.getDocumentElement(), "Custom");

            node = root;
            
            Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.Custom);

            Documentizer.base.name = node.getAttribute("name");
            Documentizer.base.docWriter.appendH1(Documentizer.base.name);
            DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);

            List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes(node);
            Documentizer.base.docWriter.appendTable(table);

            buildNavigationTable();
            
         //   Documentizer.base.docWriter.documentAttributes(node);
            documentCustom(node);
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
        
        NodeList attrList = node.getElementsByTagName("Attributes");
        Element attr = (Element) attrList.item(0);
        if(attr != null){

            HashMap<String, List<String>> attributes;
			try {
				attributes = getAttributesMap(attr);
				
			
			 for(String attrName : attributes.keySet()){
				 row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#"+attrName, attrName));
        table.add(row);
			 }
                
                
				 
   			} catch (TransformerException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
        }

        Documentizer.base.docWriter.appendTable(table, "Structure");
    }
    
    
    public static void documentCustom(Element node) throws IOException, TransformerException {
    	
    	
    	List<String> row = new ArrayList<String>();
    	 
      	  NodeList attrList = node.getElementsByTagName("Attributes");
            Element attr = (Element) attrList.item(0);
            if(attr != null){

                HashMap<String, List<String>> attributes;
   			try {
   				attributes = getAttributesMap(attr);
   				
   			
   			 for(String attrName : attributes.keySet()){
   				 
   				List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
   			 row = new ArrayList<String>();
             row.add("<b>Entry</b>");
             row.add("<b>Value</b>");
            
             table.add(row);
   				 
   				
                 row = new ArrayList<String>();
                 
                 row.add(attrName);
                 row.add(DocumentizerUtils.encodeHTML(attributes.get(attrName).get(0)));
                 table.add(row);
                 
                 
                 String anchor = Documentizer.base.docWriter.getAnchor(attrName);
                 Documentizer.base.docWriter.appendTable(table, anchor); 
   			 }
   	                 
   	                  
   				 
   			} catch (TransformerException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
              
            }
          	
          	//if(sigNode.getLocalName();
       
        
      
        	
       
    }

}
