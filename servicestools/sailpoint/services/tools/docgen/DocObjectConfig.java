package sailpoint.services.tools.docgen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static sailpoint.services.tools.docgen.DocumentizerUtils.*;

/**
 * Created by adam.creaney on 3/24/16.
 */
public class DocObjectConfig extends DocBase{

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
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.ObjectConfig.name());
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
            Element root = getRealRoot(doc.getDocumentElement(), "ObjectConfig");

            node = root;

            Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.ObjectConfig);
            Documentizer.base.name = root.getAttribute("name");
            Documentizer.base.docWriter.appendH1(Documentizer.base.name);
            DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);

            docRoleTypeDefinitions();
            docObjectAttributes();

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

    public static void docRoleTypeDefinitions() throws IOException {

        NodeList nl = node.getElementsByTagName("RoleTypeDefinition");
        if(nl.getLength() > 0 ){
        	Documentizer.base.docWriter.appendHorizontalRule();
        	Documentizer.base.docWriter.appendH1("Role Type Definitions");
        }
        for(int i = 0; i < nl.getLength(); i++){

            @SuppressWarnings("unchecked")
			List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes((Element) nl.item(i));

            NodeList rr = ((Element) nl.item(i)).getElementsByTagName("RequiredRights");

            List<String> row = new ArrayList<String>();

            if(rr.getLength() > 0){
                Element e = (Element) rr.item(0);
                NodeList children = e.getChildNodes();
                for(int j = 0; j < children.getLength(); j++){
                    if(children.item(j).getNodeName().equalsIgnoreCase("Reference")){
                        String reference = Documentizer.base.docWriter.getReference((Element) children.item(j));
                        row = new ArrayList<String>();
                        row.add("Required Right");
                        row.add(reference);
                        table.add(row);
                    }
                }
            }
            row = new ArrayList<String>();
            row.add("Description");
            row.add(getNodeTextForPath((Element) nl.item(i), "Description"));
            table.add(row);
            Documentizer.base.docWriter.appendTable(table);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void docObjectAttributes() throws IOException {

    	Documentizer.base.docWriter.appendHorizontalRule();
    	Documentizer.base.docWriter.appendH1("Object Attributes");

        NodeList nl = node.getElementsByTagName("ObjectAttribute");
        for(int i = 0; i < nl.getLength(); i++){
            List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes((Element) nl.item(i));
            List<String> row = new ArrayList<String>();
            NodeList av = ((Element)nl.item(i)).getElementsByTagName("AllowedValues");
            if(av.getLength() > 0){
                for(int j = 0; j < av.getLength(); j++){
                    row = new ArrayList<String>();
                    row.add("AllowedValue");
                    row.add(av.item(j).getTextContent());
                    table.add(row);
                }
            }

            NodeList as = ((Element)nl.item(i)).getElementsByTagName("AttributeSource");
            if(as.getLength() > 0){
                for(int k = 0; k < as.getLength(); k++){
                    Element source = (Element) as.item(k);

                    List<String> refParts = Documentizer.base.docWriter.getReferenceParts(source, "ApplicationRef");
                    if(refParts.size() > 0){
                        String appLink = Documentizer.base.docWriter.getLinkForObject(refParts);
                        String attrName = getNameForElement(source);
                        row = new ArrayList<String>();
                        row.add("ApplicationRef");
                        row.add(appLink + "(" + attrName + ")");
                        table.add(row);
                    }

                    List<String> refPartsRule = Documentizer.base.docWriter.getReferenceParts(source, "RuleRef");
                    if(refPartsRule.size() > 0){
                        String appLinkRule = Documentizer.base.docWriter.getLinkForObject(refPartsRule);
                        String attrNameRule = getNameForElement(source);
                        row = new ArrayList<String>();
                        row.add("RuleRef");
                        row.add(appLinkRule + "(" + attrNameRule + ")");
                        table.add(row);
                    }
                }
            }

            NodeList listeners = ((Element) nl.item(i)).getElementsByTagName("ListenerRule");
            if(listeners.getLength() > 0){
                List<String> refParts = Documentizer.base.docWriter.getReferenceParts((Element) nl.item(i), "ListenerRule");
                String listenerLinkRule = Documentizer.base.docWriter.getLinkForObject(refParts);
                String listenerNameRule = getNameForElement((Element) nl.item(i));
                row = new ArrayList<String>();
                row.add("ListenerRule");
                row.add(listenerLinkRule + "(" + listenerNameRule + ")");
                table.add(row);
            }

            NodeList at = ((Element)nl.item(i)).getElementsByTagName("AttributeTargets");
            if(at.getLength() > 0){
                try{
                    for(int m = 0; m < at.getLength(); m++){
                        NodeList individualAttrTargets = ((Element) at.item(m)).getElementsByTagName("AttributeTarget");
                        if(individualAttrTargets != null){
                            for(int l = 0; l < individualAttrTargets.getLength(); l++){

                                List tableTarget = Documentizer.base.docWriter.getTableForXmlTagAttributes((Element) at.item(l));
                                List<String> refParts = Documentizer.base.docWriter.getReferenceParts((Element) at.item(l), "ApplicationRef");
                                String appLink = Documentizer.base.docWriter.getLinkForObject(refParts);
                                String attrName = ((Element) individualAttrTargets.item(l)).getAttribute("name");
                                String heading = "<b>Attribute Target</b>";
                                row = new ArrayList<String>();
                                row.add(heading);
                                row.add(appLink);
                                tableTarget.add(row);

                                row = new ArrayList<String>();
                                row.add("Target Attribute");
                                row.add(attrName);
                                tableTarget.add(row);

                                row = new ArrayList<String>();
                                refParts = Documentizer.base.docWriter.getReferenceParts((Element) at.item(l), "RuleRef");
                                if(refParts.size() == 2){
                                    String ruleLink = Documentizer.base.docWriter.getLinkForObject(refParts);
                                    row.add("Target RuleRef");
                                    row.add(ruleLink);
                                    tableTarget.add(row);
                                    //this.addBackReference(refParts, "");
                                }

                                //tableTarget.set(0, row);
                                for(Object rowTarget : tableTarget){
                                    table.add((List<String>) rowTarget);
                                }
                            }
                        }
                    }
                }
                catch(Exception e){

                }
            }
            Documentizer.base.docWriter.appendTable(table);
        }

    }

}
