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
 * Created by Shant Jain on 02/01/17.
 */

public class DocFullTextIndex extends DocBase{
	
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
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.FullTextIndex.name());
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
               Element root = getRealRoot(doc.getDocumentElement(), "FullTextIndex");

               node = root;

               Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.FullTextIndex);

               Documentizer.base.name = node.getAttribute("name");
               Documentizer.base.docWriter.appendH1(Documentizer.base.name);
               DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);
               List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes(node);
                
               Documentizer.base.docWriter.documentDescription(table,node);              
               Documentizer.base.docWriter.appendTable(table);
               
               buildNavigationTable();            
	           
	         documentClasses();
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
    
    private static void documentClasses() throws IOException {
   	 List<String> row = new ArrayList<String>();
   	  NodeList attrList = node.getElementsByTagName("Attributes");
         Element attr = (Element) attrList.item(0);
         if(attr != null){

             HashMap<String, List<String>> attributes;
			try {
				attributes = getAttributesMap(attr);
				  for(String attrName : attributes.keySet()){
					  if(attrName.equals("fields")){
	            		  NodeList tagList = attr.getElementsByTagName("FullTextField");           		  
	            		  List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
	            		  Documentizer.base.docWriter.append("<div>");    
		                  row = new ArrayList<String>();
		                  row.add("<b>Name</b>");
		                  row.add("<b>Indexed</b>");
		                  row.add("<b>Stored</b>");
		                  row.add("<b>Analyzed</b>");
		                  row.add("<b>Ignored</b>");		                  
		                  table.add(row);
		                  for(int i=0;i<tagList.getLength();i++){
		                	  Element fullTextField = (Element)tagList.item(i);
		                	  row = new ArrayList<String>();
		                	 
		                      row.add(fullTextField.getAttribute("name")); 
		                      if(fullTextField.getAttribute("indexed").equalsIgnoreCase("true"))
			                      	row.add("True"); 
			                      else
			                      	row.add("False");
		                	  if(fullTextField.getAttribute("stored").equalsIgnoreCase("true"))
			                      	row.add("True"); 
			                      else
			                      	row.add("False");
		                	  if(fullTextField.getAttribute("analyzed").equalsIgnoreCase("true"))
			                      	row.add("True"); 
			                      else
			                      	row.add("False");
		                	  if(fullTextField.getAttribute("ignored").equalsIgnoreCase("true"))
			                      	row.add("True"); 
			                      else
			                      	row.add("False");		                   
			                   
			                   table.add(row);     	  
		                  }
		                   String anchor = Documentizer.base.docWriter.getAnchor(attrName);
		                   Documentizer.base.docWriter.appendTable(table, anchor);
		                  }else{
		                	  row = new ArrayList<String>();
		                	  List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
		                	  row.add(attrName);
		                	  row.add(attributes.get(attrName).toString());
		                	  table.add(row);
		                	  String anchor = Documentizer.base.docWriter.getAnchor(attrName);
		                	  Documentizer.base.docWriter.appendTable(table, anchor);
		                  }
		                 
		                  
	            	  }	                 
	                 
	                 
	                  
				 
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
         }
       	
       	//if(sigNode.getLocalName();
       	
       }
    
    private static void buildNavigationTable() throws IOException {
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();

        row.add(Documentizer.base.docWriter.getLink("#classes", "Classes"));
        table.add(row);
        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#fields", "Fields"));
        table.add(row);   
        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#indexer", "Indexer"));
        table.add(row);   
        

        Documentizer.base.docWriter.appendTable(table, "Structure");
    }



}
