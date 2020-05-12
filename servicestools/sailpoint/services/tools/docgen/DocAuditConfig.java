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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Created by Shant Jain on 01/31/17.
 */

public class DocAuditConfig extends DocBase{
	
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
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.AuditConfig.name());
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
               Element root = getRealRoot(doc.getDocumentElement(), "AuditConfig");

               node = root;

               Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.AuditConfig);

               Documentizer.base.name = node.getAttribute("name");
               Documentizer.base.docWriter.appendH1(Documentizer.base.name);
               DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);
               List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes(node);
                
               Documentizer.base.docWriter.documentDescription(table,node);              
               Documentizer.base.docWriter.appendTable(table);
               
               buildNavigationTable();            
	           documentAuditActions();
	           documentAuditAttributes();
               documentAuditClasses();
               documentAuditSCIMResources();              
           }
           catch(NullPointerException e ){        	  
        	   
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
    
    private static void documentAuditSCIMResources() throws IOException{
    	   List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
           List<String> row = new ArrayList<String>();
           Element resources = (Element) node.getElementsByTagName("AuditSCIMResources").item(0);
           NodeList resourcesList;
           String anchor; 
           table = Documentizer.base.docWriter.getTableEmpty();
           Documentizer.base.docWriter.append("<div>");       
           row = new ArrayList<String>();
           row.add("<b>Name</b>");
           row.add("<b>Create</b>");
           row.add("<b>Delete</b>");
           row.add("<b>Update</b>");
           table.add(row); 
            
           if(resources != null){         	
        	   resourcesList = resources.getElementsByTagName("AuditSCIMResource");        	 
               for(int i = 0; i < resourcesList.getLength(); i++){                
                   Element classe = (Element) resourcesList.item(i);                  
                          
                   String name = classe.getAttribute("name");
                   String create = classe.getAttribute("create");
                   String delete = classe.getAttribute("delete");
                   String update = classe.getAttribute("update");
                   row = new ArrayList<String>();                     
                   row.add(name);  
                   row.add(create); 
                   row.add(delete); 
                   row.add(update); 
                   table.add(row);                          
               }
               Documentizer.base.docWriter.append("</div>");        
               anchor = Documentizer.base.docWriter.getAnchor("AuditSCIMResources");
               Documentizer.base.docWriter.appendTable(table, anchor);                       
           }
       }
    
    private static void documentAuditClasses() throws IOException{
   	   List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
          List<String> row = new ArrayList<String>();
          Element classes = (Element) node.getElementsByTagName("AuditClasses").item(0);
          NodeList classesList;
          String anchor; 
          table = Documentizer.base.docWriter.getTableEmpty();
          row = new ArrayList<String>();
          row.add("<b>Name</b>");
          row.add("<b>Display Name</b>");          
          table.add(row);  
           
          if(classes != null){         	
        	  classesList = classes.getElementsByTagName("AuditClass");        	 
              for(int i = 0; i < classesList.getLength(); i++){                
                  Element classe = (Element) classesList.item(i);                  
                  Documentizer.base.docWriter.append("<div>");              
                  String name = classe.getAttribute("name");
                  String displayName = classe.getAttribute("displayName");
                  row = new ArrayList<String>();                     
                  row.add(name);   
                  row.add(displayName);
                  table.add(row);   
                  Documentizer.base.docWriter.append("</div>");            
              }
              anchor = Documentizer.base.docWriter.getAnchor("AuditClasses");
              Documentizer.base.docWriter.appendTable(table, anchor);                       
          }
      }
    
    private static void documentAuditAttributes() throws IOException{
  	   List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
         List<String> row = new ArrayList<String>();
         Element attributes = (Element) node.getElementsByTagName("AuditAttributes").item(0);
         NodeList attributesList;
         String anchor; 
         table = Documentizer.base.docWriter.getTableEmpty();  
         row = new ArrayList<String>();
         row.add("<b>Name</b>");
         row.add("<b>Display Name</b>");    
         row.add("<b>Class</b>");  
         table.add(row); 
         
         if(attributes != null){         	
        	 attributesList = attributes.getElementsByTagName("AuditAttribute");        	 
             for(int i = 0; i < attributesList.getLength(); i++){                
                 Element attribute = (Element) attributesList.item(i);                  
                 Documentizer.base.docWriter.append("<div>");              
                 String name = attribute.getAttribute("name");
                 String displayName = attribute.getAttribute("displayName");
                 String classe = attribute.getAttribute("class");
                 
                 row = new ArrayList<String>();                     
                 row.add(name);    
                 row.add(displayName); 
                 row.add(classe); 
                 table.add(row);   
                 Documentizer.base.docWriter.append("</div>");            
             }
             anchor = Documentizer.base.docWriter.getAnchor("AuditAttributes");
             Documentizer.base.docWriter.appendTable(table, anchor);                       
         }
     }
    
    private static void documentAuditActions() throws IOException{
 	   List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();
        Element actions = (Element) node.getElementsByTagName("AuditActions").item(0);
        NodeList actionsList;
        String anchor; 
        table = Documentizer.base.docWriter.getTableEmpty();     
        
      
        Documentizer.base.docWriter.append("<div>");       
        row = new ArrayList<String>();
        row.add("<b>Name</b>");
        row.add("<b>Display Name</b>");
        row.add("<b>Enabled</b>");       
        table.add(row); 
         
        if(actions != null){         	
        	actionsList = actions.getElementsByTagName("AuditAction");        	 
            for(int i = 0; i < actionsList.getLength(); i++){                
                Element action = (Element) actionsList.item(i);                  
                Documentizer.base.docWriter.append("<div>");              
                String name = action.getAttribute("name");
                String displayName = action.getAttribute("displayName");
                String enabled = action.getAttribute("enabled");
                row = new ArrayList<String>();                     
                row.add(name);      
                row.add(displayName); 
                if(enabled.equalsIgnoreCase("true"))
                	row.add("True"); 
                else
                	row.add("False");
                table.add(row);   
                Documentizer.base.docWriter.append("</div>");            
            }
            anchor = Documentizer.base.docWriter.getAnchor("AuditActions");
            Documentizer.base.docWriter.appendTable(table, anchor);                       
        }
    }
    
    private static void buildNavigationTable() throws IOException {
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();

        row.add(Documentizer.base.docWriter.getLink("#AuditActions", "Audit Actions"));
        table.add(row);
          
        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#AuditAttributes", "Audit Attributes"));
        table.add(row);
        
        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#AuditClasses", "Audit Classes"));
        table.add(row);
        
        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#AuditSCIMResources", "Audit SCIM Resources"));
        table.add(row);

        Documentizer.base.docWriter.appendTable(table, "Structure");
    }
    

}
