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
 * Created by Shant Jain on 02/06/17.
 */ 

public class DocUIConfig extends DocBase{
	
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
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.UIConfig.name());
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
               Element root = getRealRoot(doc.getDocumentElement(), "TargetSource");
               node = root;
               Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.UIConfig);
               Documentizer.base.name = node.getAttribute("name");
               Documentizer.base.docWriter.appendH1(Documentizer.base.name);
               DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);
               
               List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
               List<String> row = new ArrayList<String>();
               Element attributes = (Element) node.getElementsByTagName("Attributes").item(0);
               NodeList entryList;
                
               if(attributes != null){
                   row = new ArrayList<String>();
                   entryList = attributes.getElementsByTagName("entry");
                   for(int i = 0; i < entryList.getLength(); i++){
                       row = new ArrayList<String>();
                       Element entry = (Element) entryList.item(i);                   
                     
                       String name = entry.getAttribute("key");
                       String link = Documentizer.base.docWriter.getLink("#"+name, name);
                       row.add(link);
                       table.add(row);
                   }
                   String anchor = Documentizer.base.docWriter.getAnchor("Attributes");
                   Documentizer.base.docWriter.appendTable(table, anchor);              
               }
               
               documentEntry();
                    
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

    private static void documentEntry() throws IOException{
 	   
        List<String> row = new ArrayList<String>();
        Element map = (Element) node.getElementsByTagName("Map").item(0);
        NodeList entryList;
         
        if(map != null){
            row = new ArrayList<String>();
            entryList = map.getElementsByTagName("entry");
            for(int i = 0; i < entryList.getLength(); i++){
            	Boolean allcovered = false;
                row = new ArrayList<String>();
                Element entry = (Element) entryList.item(i);
                String name = entry.getAttribute("key");
                NodeList columnconfigList =  entry.getElementsByTagName("ColumnConfig");
                NodeList accountIconConfigList =  entry.getElementsByTagName("AccountIconConfig");
           	 	NodeList selectItemList =  entry.getElementsByTagName("SelectItem");
           	    List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
                if(columnconfigList !=null && columnconfigList.getLength() >0){                	
                	 Documentizer.base.docWriter.append("<div>");       
                     row = new ArrayList<String>();
                     row.add("<b>dataIndex</b>");
                     row.add("<b>headerKey</b>");
                     row.add("<b>hideable</b>");     
                     row.add("<b>property</b>");    
                     row.add("<b>sortProperty</b>");    
                     row.add("<b>sortable</b>"); 
                     row.add("<b>stateId</b>"); 
                     row.add("<b>renderer</b>"); 
                     row.add("<b>fieldOnly</b>"); 
                     row.add("<b>percentWidth</b>"); 
                     row.add("<b>percentWidth</b>"); 
                     row.add("<b>flex</b>");                       
                     table.add(row);                 	
                	for(int j = 0; j < columnconfigList.getLength(); j++){                	
                	  
                		Element columnconfig = (Element) columnconfigList.item(j);
                       row = new ArrayList<String>();
                       row.add(columnconfig.getAttribute("dataIndex"));
                       row.add(columnconfig.getAttribute("headerKey"));
                       row.add(columnconfig.getAttribute("hideable"));     
                       row.add(columnconfig.getAttribute("property"));    
                       row.add(columnconfig.getAttribute("sortProperty"));    
                       row.add(columnconfig.getAttribute("sortable")); 
                       row.add(columnconfig.getAttribute("stateId")); 
                       row.add(columnconfig.getAttribute("renderer")); 
                       row.add(columnconfig.getAttribute("fieldOnly")); 
                       row.add(columnconfig.getAttribute("percentWidth")); 
                       row.add(columnconfig.getAttribute("percentWidth")); 
                       row.add(columnconfig.getAttribute("flex"));                       
                       table.add(row);  
                       Documentizer.base.docWriter.append("</div>");   	 
                       allcovered=true;
                	}
                }
                	

                    if(accountIconConfigList !=null && accountIconConfigList.getLength() >0){                    	
                    	 Documentizer.base.docWriter.append("<div>");       
                         row = new ArrayList<String>();
                         row.add("<b>attribute</b>");
                         row.add("<b>source</b>");
                         row.add("<b>title</b>");     
                         row.add("<b>value</b>");    
                                      
                         table.add(row);                 	
                    	for(int j = 0; j < accountIconConfigList.getLength(); j++){                	
                    	  
                    		Element accountIconConfig = (Element) accountIconConfigList.item(j);
                           row = new ArrayList<String>();
                           row.add(accountIconConfig.getAttribute("attribute"));
                           row.add(accountIconConfig.getAttribute("source"));
                           row.add(accountIconConfig.getAttribute("title"));     
                           row.add(accountIconConfig.getAttribute("value"));                                              
                           table.add(row);  
                           Documentizer.base.docWriter.append("</div>");    
                           allcovered=true;
                    	} 
                    }
                    
                    if(selectItemList !=null && selectItemList.getLength() >0){                      	
                      	 Documentizer.base.docWriter.append("<div>");       
                           row = new ArrayList<String>();
                           row.add("<b>label</b>");
                           row.add("<b>value</b>");                                       
                           table.add(row);                 	
                      	for(int j = 0; j < selectItemList.getLength(); j++){                	
                      	  
                      		Element selectItem = (Element) selectItemList.item(j);
                             row = new ArrayList<String>();
                             row.add(selectItem.getAttribute("label"));
                             row.add(selectItem.getAttribute("value"));                                                                       
                             table.add(row);  
                             Documentizer.base.docWriter.append("</div>"); 
                             allcovered=true;
                      	}                      	
                      }
                    if(!allcovered){
                    	row.add("Value");  
                    	String value = entry.getAttribute("value").toString();
                    	if(value!=null){
                    	row.add(value); 
                    	table.add(row);
                    	}else{
                    		
                    		Documentizer.base.docWriter.addReferenceRowToTable(table, entry, "Field");
                    		  
                    	}
                    	
                    }
                	
                	 String anchor = Documentizer.base.docWriter.getAnchor(name);
                     Documentizer.base.docWriter.appendTable(table, anchor);   
                	
                }
                     
                
               
            }
                 
        }
    }  
    



