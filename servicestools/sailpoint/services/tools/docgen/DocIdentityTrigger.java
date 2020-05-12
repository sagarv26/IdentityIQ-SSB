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
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sailpoint.services.tools.docgen.DocBase;
import sailpoint.services.tools.docgen.DocWriter;
import sailpoint.services.tools.docgen.Documentizer;

/**
 * Created by Shashank.Dubey on 02/10/2017
 */
public class DocIdentityTrigger extends DocBase {
   public String seperator;
   private static Element node;

   public static void doDocumentation(ArrayList<File> files)
         throws ParserConfigurationException, SAXException, IOException {
      if (files != null && !files.isEmpty()) {
         if (!DocBase.objectList.isEmpty()) {
            DocBase.objectList.clear();
         }
         for (File f : files) {
            docObject(f);
         }
         Documentizer.base
               .setObjectType(DocBase.DOCTYPE.IdentityTrigger.name());
         Documentizer.base.buildFrameList();
      }
   }

   @SuppressWarnings("unchecked")
   public static void docObject(File file) throws IOException, SAXException,
         ParserConfigurationException {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      // TODO: Why/how can we avoid the dtd processing besides this line?
      dbFactory.setFeature(
            "http://apache.org/xml/features/nonvalidating/load-external-dtd",
            false);

      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

      InputStream is = new FileInputStream(file);
      Document doc = dBuilder.parse(is);

      doc.getDocumentElement().normalize();

        try{
        	
            Element root = getRealRoot(doc.getDocumentElement(), "IdentityTrigger");
            
            

         node = root;

         Documentizer.base.docWriter = new DocWriter(DocBase.destination, root,
               DocBase.DOCTYPE.IdentityTrigger);

         Documentizer.base.name = node.getAttribute("name");
         Documentizer.base.docWriter.appendH1(Documentizer.base.name);
         DocBase.objectList.put(getFilenameForObject(Documentizer.base.name),
               Documentizer.base.name);

         List<List<String>> table = Documentizer.base.docWriter
               .getTableForXmlTagAttributes(node);
         Documentizer.base.docWriter.addReferenceRowToTable(table, node, "Owner");
         Documentizer.base.docWriter.addDescriptionToTable(node, table);
         Documentizer.base.docWriter.appendTable(table);

         buildNavigationTable();
         documentRuleReferences();
         documentHandlerParameters();
         documentIdentitySelector();
         
         // Documentizer.base.docWriter.documentAttributes(node);

        }  catch(NullPointerException e ){        	  
      	   
        }
        catch (Exception e) {
        	e.printStackTrace();
     	}finally {
         if (Documentizer.base.docWriter != null) {
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
      row.add(Documentizer.base.docWriter.getLink("#IdentitySelector", "IdentitySelector"));
      table.add(row);

      Documentizer.base.docWriter.appendTable(table, "Structure");
   }

   private static void documentHandlerParameters() throws IOException {
      Element resources = (Element) node.getElementsByTagName(
            "HandlerParameters").item(0);
      try {
         Documentizer.base.docWriter.documentAttributes(resources);
      } catch (TransformerException e) {
         e.printStackTrace();
      }
   }
   
   
 	private static void documentIdentitySelector() throws IOException{
		List<List<String>> table1 = Documentizer.base.docWriter.getTableEmpty();
		List<String> row = new ArrayList<String>();
		String anchor = Documentizer.base.docWriter.getAnchor("IdentitySelector");
		
		Element selector = (Element) node.getElementsByTagName("Selector").item(0);
		Element identitySelector = (Element) selector.getElementsByTagName("IdentitySelector").item(0);
		
		
		
		if(((Element) identitySelector.getElementsByTagName("MatchExpression").item(0))!=null){
			Element resource = (Element) identitySelector.getElementsByTagName("MatchExpression").item(0);
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
		else if(((Element) identitySelector.getElementsByTagName("RuleRef").item(0))!=null) 
			Documentizer.base.docWriter.addReferenceRowToTable(table1, identitySelector, "RuleRef");
				
		else if(((Element) identitySelector.getElementsByTagName("PopulationRef").item(0))!=null) 
			Documentizer.base.docWriter.addReferenceRowToTable(table1, identitySelector, "PopulationRef");

		else if(((Element) identitySelector.getElementsByTagName("Script").item(0))!=null) {
			Element script = (Element) identitySelector.getElementsByTagName("Script").item(0);
			row.add(script.getElementsByTagName("Source").item(0).getTextContent());
			table1.add(row);
			}

		else if(((Element) identitySelector.getElementsByTagName("CompoundFilter").item(0))!=null) {
			Element resource = (Element) identitySelector.getElementsByTagName("CompoundFilter").item(0);
			
			if(resource.getAttribute("operation").equals("AND"))			
				row.add("CompoundFilter operation: AND");
			else
				row.add("CompoundFilter operation: OR");
			table1.add(row);	
		
			Documentizer.base.docWriter.append("<div class='collapse'>");                   
			Documentizer.base.docWriter.appendH3(anchor);
			Documentizer.base.docWriter.append("<div>"); 
			
			row = new ArrayList<String>();
			row.add("<b>property</b>");
         	row.add("<b>operation</b>");
         	row.add("<b>value</b>");
         	row.add("<b>matchMode</b>");
         	row.add("<b>ignoreCase</b>");		
         	table1.add(row);
          
			NodeList filterlist = resource.getElementsByTagName("Filter");
          
			for(int i=0; i<filterlist.getLength(); i++){
              row = new ArrayList<String>();
              Element filter = (Element)filterlist.item(i);
              row.add(filter.getAttribute("property"));
              row.add(filter.getAttribute("operation"));
              row.add(filter.getAttribute("value"));
              row.add(filter.getAttribute("matchMode"));
              row.add(filter.getAttribute("ignoreCase"));		
              table1.add(row);
			}
		   
         }
		Documentizer.base.docWriter.appendTable(table1, anchor); 	
 	}	

   
    private static void documentRuleReferences() throws IOException {
        List<List<String>> table = new ArrayList<List<String>>();
        List<String> row = null;
        NodeList refRules = node.getElementsByTagName("TriggerRule");
        if (refRules.getLength()>0){
        Element refRule = (Element) refRules.item(0);
      //  System.out.println("refrule:" + refRule.toString());
        if (refRule != null) {
           NodeList refs = refRule.getElementsByTagName("Reference");
           for (int i = 0; i < refs.getLength(); i++) {
              Element reference = (Element) refs.item(i);
              row = new ArrayList<String>();
              List<String> ruleReferences = Documentizer.base.docWriter.getReferenceParts(reference);
              String objectFileName = getFilenameForObject(ruleReferences.get(1));
              String link = Documentizer.base.docWriter.getLink("../"+ruleReferences.get(0)+"/"+objectFileName, ruleReferences.get(1));
           //   System.out.println("link:" + link);
              row.add(ruleReferences.get(0));
              row.add(link);
              table.add(row);
            //  System.out.println("row:" + row);
           }
           Documentizer.base.docWriter.appendTable(table, "Rule References");
        }
        }
     }
    }
