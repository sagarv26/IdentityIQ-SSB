package sailpoint.services.tools.docgen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static sailpoint.services.tools.docgen.DocumentizerUtils.*;

/**
 * Created by adam.creaney on 3/24/16.
 */
public class DocCertificationDefinition extends DocBase {

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
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.CertificationDefinition.name());
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
            Element root = getRealRoot(doc.getDocumentElement(), "CertificationDefinition");

            node = root;

            Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.CertificationDefinition);


            if(node.getTagName().equalsIgnoreCase("ImportAction")){
                node = (Element) node.getFirstChild();
            }
            Documentizer.base.name = node.getAttribute("name");
            Documentizer.base.docWriter.appendH1(Documentizer.base.name);
            DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);

            List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
            table = Documentizer.base.docWriter.getTableForXmlTagAttributes(node);

            Documentizer.base.docWriter.addReferenceRowToTable(table, node, "Owner");
            Documentizer.base.docWriter.addReferenceRowToTable(table, node, "Tags");
            Documentizer.base.docWriter.appendTable(table);
            Documentizer.base.docWriter.appendHorizontalRule();
            buildNavigationTable();
            documentOwnerAndTag();
            documentCertificationRules();
            documentRemindersAndEscalations();
            Documentizer.base.docWriter.appendHorizontalRule();

            Documentizer.base.docWriter.appendH2("Attributes");
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

    static void buildNavigationTable() throws IOException {
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#Reminders And Escalations", "Reminders and Escalations"));
        table.add(row);

        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#Attributes", "Attributes"));
        table.add(row);

        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#Rules", "Rules"));
        table.add(row);

        try {
        	Documentizer.base.docWriter.appendTable(table, "Structure");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private void documentOwnerAndTag() throws IOException{
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();
        row.add("<b>Attribute</b>");
        row.add("<b>Value</b>");
        table.add(row);

        String ownerVal = "";
        String tagVal = "";
        Element ownerNode = (Element) node.getElementsByTagName("Owner").item(0);
        if(ownerNode != null){
            Element ownerRef = (Element) ownerNode.getElementsByTagName("Reference").item(0);
            if(ownerRef != null){
                ownerVal = Documentizer.base.docWriter.getReference(ownerRef);
            }
        }
        row = new ArrayList<String>();
        row.add("Owner");
        row.add(ownerVal);
        table.add(row);

        List<String> tags = new ArrayList<String>();
        Element tagNode = (Element) node.getElementsByTagName("Tags").item(0);
        if(tagNode != null){
            NodeList tagList = tagNode.getElementsByTagName("Reference");
            for(int i = 0; i < tagList.getLength(); i++){
                Element tag = (Element) tagList.item(i);
                tagVal = Documentizer.base.docWriter.getReference(tag);
                tags.add(tagVal);
            }

        }

        row = new ArrayList<String>();
        row.add("Tags");
        row.add(java.util.Arrays.toString(tags.toArray(new String[tags.size()])));
        table.add(row);

        Documentizer.base.docWriter.appendTable(table, "Properties");
    }

    private static void documentRemindersAndEscalations() throws IOException{
        String anchor = Documentizer.base.docWriter.getAnchor("Reminders And Escalations");
        Documentizer.base.docWriter.appendTable(anchor);

        List<String> row = new ArrayList<String>();
        NodeList nl = node.getElementsByTagName("entry");
        for(int i = 0; i < nl.getLength(); i++){
            row = new ArrayList<String>();
            Element curr = (Element) nl.item(i);
            String key = curr.getAttribute("key");
            if(key.endsWith(".remindersAndEscalations")){

            	Documentizer.base.docWriter.appendHorizontalRule();
            	Documentizer.base.docWriter.append("<div class='collapse'>");
            	Documentizer.base.docWriter.appendH3(key);
            	Documentizer.base.docWriter.append("<div>");

                List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
                row = new ArrayList<String>();
                row.add("<b>Type</b>");
                row.add("<b>Email Template</b>");
                row.add("<b>Before</b>");
                row.add("<b>Milliseconds</b>");
                row.add("<b>Once</b>");
                table.add(row);
                //have a reminder config
                row = new ArrayList<String>();
                Element reminder = (Element) curr.getElementsByTagName("ReminderConfig").item(0);
                if(reminder != null){
                    row.add("Reminder");
                    row.add(reminder.getAttribute("emailTemplateName"));
                    row.add(reminder.getAttribute("before"));
                    row.add(reminder.getAttribute("millis"));
                    row.add(reminder.getAttribute("once"));
                    table.add(row);
                }

                row = new ArrayList<String>();
                Element escalation = (Element) curr.getElementsByTagName("EscalationConfig").item(0);
                if(escalation != null){
                    row.add("Escalation");
                    row.add(escalation.getAttribute("emailTemplateName"));
                    row.add(escalation.getAttribute("before"));
                    row.add(escalation.getAttribute("millis"));
                    row.add(escalation.getAttribute("once"));
                    table.add(row);
                }

                Documentizer.base.docWriter.appendTable(table, key);
                Documentizer.base.docWriter.append("</div></div>");
            }
        }
    }

    private static void documentCertificationRules() throws IOException{
        String anchor = Documentizer.base.docWriter.getAnchor("Rules");
        List<List<String>> table = new ArrayList<List<String>>();
        List<String> row = new ArrayList<String>();

        List<String> valueRules = new ArrayList<String>();
        NodeList nl = node.getElementsByTagName("entry");
        for(int i = 0; i < nl.getLength(); i++) {
            row = new ArrayList<String>();
            Element curr = (Element) nl.item(i);
            String key = curr.getAttribute("key");
            if (key.toUpperCase().endsWith("RULE") || key.toUpperCase().endsWith("RULENAME")) {
                valueRules.add(key);
            }
        }

        List<String> ruleParts = new ArrayList<String>();
        for(String r : valueRules){
            ruleParts = Documentizer.base.docWriter.getReferencePartsNoReference(node, r, "Rule");
            if(ruleParts.size() > 0){
                row = new ArrayList<String>();
                String link = Documentizer.base.docWriter.getLinkForObject(ruleParts);
                String bookmark = "CertRule:" + ruleParts.get(1);
                String linkAnchor = Documentizer.base.docWriter.getAnchor(bookmark, link);
                row.add(linkAnchor);
                Documentizer.base.addBackReference(ruleParts, bookmark);
                table.add(row);
            }
        }

        Documentizer.base.docWriter.appendTable(table, anchor);
    }
}
