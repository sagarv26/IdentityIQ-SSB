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
 * Created by Shant Jain on 01/30/17.
 */

public class DocBundle extends DocBase{
	
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
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.Bundle.name());
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
               Element root = getRealRoot(doc.getDocumentElement(), "Bundle");

               node = root;

               Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.Bundle);

               Documentizer.base.name = node.getAttribute("name");
               Documentizer.base.docWriter.appendH1(Documentizer.base.name);
               DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);
               List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes(node);
               Documentizer.base.docWriter.addReferenceRowToTable(table, node, "Owner");
               Documentizer.base.docWriter.addReferenceRowToTable(table, node, "Inheritance");  
               Documentizer.base.docWriter.addReferenceRowToTable(table, node, "ProvisioningForms");
               Documentizer.base.docWriter.addReferenceRowToTable(table, node, "Requirements");
               Documentizer.base.docWriter.addReferenceRowToTable(table, node, "Permits");
               Documentizer.base.docWriter.documentDescription(table,node);              
               Documentizer.base.docWriter.appendTable(table);
               
               buildNavigationTable();
               Documentizer.base.docWriter.documentAttributes(node);
               documentEntitlements();
               documentEntitlement();
               documentIdentitySelector();
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
       
    
    	private static void documentEntitlements() throws IOException{
    	   List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
           List<String> row = new ArrayList<String>();
           Element profiles = (Element) node.getElementsByTagName("Profiles").item(0);
           NodeList profileList;
            
           if(profiles != null){
               row = new ArrayList<String>();
               profileList = profiles.getElementsByTagName("Profile");
               for(int i = 0; i < profileList.getLength(); i++){
                   row = new ArrayList<String>();
                   Element profile = (Element) profileList.item(i);                   
                   Element constraints = (Element) profile.getElementsByTagName("Constraints").item(0);                   
                   Element filter = (Element) constraints.getElementsByTagName("Filter").item(0);
                   String name = filter.getTextContent().trim();
                   String link = Documentizer.base.docWriter.getLink("#Object Type: " + name, name);
                   row.add(link);
                   table.add(row);
               }
               String anchor = Documentizer.base.docWriter.getAnchor("Entitlements");
               Documentizer.base.docWriter.appendTable(table, anchor);              
           }
       }
       
       private static void documentEntitlement() throws IOException{
    	   
           List<String> row = new ArrayList<String>();
           Element profiles = (Element) node.getElementsByTagName("Profiles").item(0);
           NodeList profileList;
           String anchor; 
           if(profiles != null){
               row = new ArrayList<String>();
               profileList = profiles.getElementsByTagName("Profile");
               for(int i = 0; i < profileList.getLength(); i++){
            	   List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
                   row = new ArrayList<String>();
                   Element profile = (Element) profileList.item(i);            
                   Element constraints = (Element) profile.getElementsByTagName("Constraints").item(0);                   
                   Element filter = (Element) constraints.getElementsByTagName("Filter").item(0);
                   String name = filter.getTextContent().trim();
                   anchor = Documentizer.base.docWriter.getAnchor("Object Type: " + name);
                   Documentizer.base.docWriter.append("<div class='collapse'>");                   
                   Documentizer.base.docWriter.appendH3(anchor);
                   Documentizer.base.docWriter.append("<div>");                   
                   
                   row = new ArrayList<String>();
                   row.add("<b>Application</b>");
                   row.add("<b>Property</b>");
                   row.add("<b>Value</b>");
                   table.add(row);
                   
                   row = new ArrayList<String>();
                   row.add(Documentizer.base.docWriter.getReference(profile, "ApplicationRef"));
                   row.add(filter.getAttribute("property"));
                   row.add(filter.getTextContent().trim());
                   table.add(row);
                   
                   Documentizer.base.docWriter.append("</div>");
                   Documentizer.base.docWriter.append("</div>");
                   Documentizer.base.docWriter.appendHorizontalRule();
                   anchor = Documentizer.base.docWriter.getAnchor("Entitlement : "+ name);
                   Documentizer.base.docWriter.appendTable(table, anchor);    
               }
                        
           }
       }
       
       private static void buildNavigationTable() throws IOException {
           List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
           List<String> row = new ArrayList<String>();

           row.add(Documentizer.base.docWriter.getLink("#Attributes", "Attributes"));
           table.add(row);
             
           row = new ArrayList<String>();
           row.add(Documentizer.base.docWriter.getLink("#Entitlements", "Entitlements"));
           table.add(row);

           row = new ArrayList<String>();
           row.add(Documentizer.base.docWriter.getLink("#IdentitySelector", "IdentitySelector"));
           table.add(row);
           
           Documentizer.base.docWriter.appendTable(table, "Structure");
       }

     	private static void documentIdentitySelector() throws IOException{
    		List<List<String>> table1 = Documentizer.base.docWriter.getTableEmpty();
    		List<String> row = new ArrayList<String>();
    		
    		Element selector = (Element) node.getElementsByTagName("Selector").item(0);
    		if(selector != null){
    		Element identitySelector = (Element) selector.getElementsByTagName("IdentitySelector").item(0);
    		
    			
    			
    		
    		Element ruleRef = (Element) identitySelector.getElementsByTagName("RuleRef").item(0);
    		Element resource = (Element) identitySelector.getElementsByTagName("MatchExpression").item(0);
    		
    		if(resource!=null){
    		if(resource.getAttribute("and").equals("true"))			
    			row.add("MatchExpression: AND");
    		else
    			row.add("MatchExpression: OR");
    		table1.add(row);	
    		
    		
    		NodeList matchtermlist = resource.getElementsByTagName("MatchTerm");
    		
    		for(int i=0; i<matchtermlist.getLength(); i++){
    			row = new ArrayList<String>();
    			Element matchTerm = (Element)matchtermlist.item(i);
    			row.add(matchTerm.getAttribute("name"));
    			row.add(matchTerm.getAttribute("value"));
    			row.add(Documentizer.base.docWriter.getReference( matchTerm, "ApplicationRef"));
    			table1.add(row);
    		}
        		}
    		else if (ruleRef!=null) 
    			Documentizer.base.docWriter.addReferenceRowToTable(table1, identitySelector, "RuleRef"); 
    		
            String anchor = Documentizer.base.docWriter.getAnchor("IdentitySelector");
            Documentizer.base.docWriter.appendTable(table1, anchor); 
    		}
      	}
       
       
   /*	private static void documentIdentitySelector() throws IOException{
		List<List<String>> table1 = Documentizer.base.docWriter.getTableEmpty();
		List<String> row = new ArrayList<String>();
		Element resource = (Element) node.getElementsByTagName("MatchExpression").item(0);

		if(resource.getAttribute("and").equals("true"))			
			row.add("MatchExpression: AND");
		else
			row.add("MatchExpression: OR");
		table1.add(row);	
		
		
		NodeList matchtermlist = resource.getElementsByTagName("MatchTerm");
		
		for(int i=0; i<matchtermlist.getLength(); i++){
			row = new ArrayList<String>();
			Element matchTerm = (Element)matchtermlist.item(i);
			row.add(matchTerm.getAttribute("name"));
			row.add(matchTerm.getAttribute("value"));
			row.add(Documentizer.base.docWriter.getReference( matchTerm, "ApplicationRef"));
			table1.add(row);
			
		}
		*/
		
		
		//matchTerm.getAttribute(name);
       
}
