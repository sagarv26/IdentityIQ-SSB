package sailpoint.services.tools.docgen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sailpoint.services.tools.docgen.DocBase;
import sailpoint.services.tools.docgen.DocWriter;
import sailpoint.services.tools.docgen.Documentizer;

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
 * Created by Shant Jain on 02/01/17.
 */


public class DocDictionary extends DocBase{
	
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
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.Dictionary.name());
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
               Element root = getRealRoot(doc.getDocumentElement(), "Dictionary");

               node = root;

               Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.Dictionary);

               Documentizer.base.name = node.getAttribute("name");
               Documentizer.base.docWriter.appendH1(Documentizer.base.name);
               DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);
               List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes(node);
                
               Documentizer.base.docWriter.documentDescription(table,node);              
               Documentizer.base.docWriter.appendTable(table);
               
               buildNavigationTable();            
	           documentTerms();	       
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
    
    private static void documentTerms() throws IOException{
  	   List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
         List<String> row = new ArrayList<String>();
         Element terms = (Element) node.getElementsByTagName("Terms").item(0);
         NodeList termsList;
         String anchor; 
         table = Documentizer.base.docWriter.getTableEmpty();     
         
       
         Documentizer.base.docWriter.append("<div>");       
        
          
         if(terms != null){         	
      	 termsList = terms.getElementsByTagName("DictionaryTerm");        	 
           for(int i = 0; i < termsList.getLength(); i++){                
                 Element dictionaryterm = (Element) termsList.item(i);                  
                 Documentizer.base.docWriter.append("<div>");              
                
                 row = new ArrayList<String>();                     
                 row.add("DictionaryTerm : "+dictionaryterm.getAttribute("value"));  
           
                 table.add(row);   
                 Documentizer.base.docWriter.append("</div>");            
             }
             anchor = Documentizer.base.docWriter.getAnchor("Terms");
             Documentizer.base.docWriter.appendTable(table, anchor);  
         }
         
     }
    
    private static void buildNavigationTable() throws IOException {
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();

        row.add(Documentizer.base.docWriter.getLink("#Terms", "Terms"));
        table.add(row);      

        Documentizer.base.docWriter.appendTable(table, "Structure");
    }

}
