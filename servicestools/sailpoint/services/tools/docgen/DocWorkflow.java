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
import java.util.HashMap;
import java.util.List;

import static sailpoint.services.tools.docgen.DocumentizerUtils.*;

/**
 * Created by adam.creaney on 3/24/16.
 */
public class DocWorkflow extends DocBase {
    public String seperator;
    private static Element node;

    private static HashMap<String, String> stepLinks;
    static HashMap<String, String> variableValues;

    public static void doDocumentation(ArrayList<File> files) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        if(files != null && !files.isEmpty()) {
        	if(!DocBase.objectList.isEmpty()) {
	    		DocBase.objectList.clear();
	    	}
        	for(File f : files){
	            docObject(f);
	        }
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.Workflow.name());
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

            Element root = getRealRoot(doc.getDocumentElement(), "Workflow");

            node = root;

            Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.Workflow);
            variableValues = new HashMap<String, String>();

            Documentizer.base.name = root.getAttribute("name");
            Documentizer.base.docWriter.appendH1(Documentizer.base.name);

            DocBase.objectList.put(DocumentizerUtils.getFilenameForObject(Documentizer.base.name), Documentizer.base.name);

            List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes(node);

            Documentizer.base.docWriter.addReferenceRowToTable(table, node, "Owner");
            Documentizer.base.docWriter.appendTable(table);

            buildNavigationTable();

            documentRuleLibraries();
            Documentizer.base.docWriter.documentAttributes(node);
            documentVariables();
            documentSteps();

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

    /**
     * this method builds a navigation table for the workflow class containing
     * attributes, variables, and steps
     *
     * @throws                  IOException
     */
    static void buildNavigationTable() throws IOException {
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#Attributes", "Attributes"));
        table.add(row);

        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#Variables", "Variables"));
        table.add(row);

        row = new ArrayList<String>();
        row.add(Documentizer.base.docWriter.getLink("#Steps", "Steps"));
        table.add(row);

        try {
        	Documentizer.base.docWriter.appendTable(table, "Structure");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method documents any rule library references included
     * in the workflow
     *
     * @throws                  IOException
     */

    private static void documentRuleLibraries() {
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();
        NodeList ruleLibraries = node.getElementsByTagName("RuleLibraries");
        if(ruleLibraries.getLength() > 0){
            Element libNode = (Element) ruleLibraries.item(0);
            NodeList refLibs = libNode.getElementsByTagName("Reference");
            for(int i = 0; i < refLibs.getLength(); i++){
                row = new ArrayList<String>();
                List<String> ruleRef = Documentizer.base.docWriter.getReferenceParts((Element) refLibs.item(i));
                
                String link = Documentizer.base.docWriter.getLinkForObject(ruleRef);

                row.add(ruleRef.get(0));
                row.add(link);
                table.add(row);
            }
            try {
            	Documentizer.base.docWriter.appendTable(table, "Rule Libraries");
            }catch (IOException e){

            }
        }
    }

    /**
     * this method documents the workflow variables
     *
     * @throws                  IOException
     */
    static void documentVariables() throws IOException {
        String anchor = Documentizer.base.docWriter.getAnchor("Variables");
        Documentizer.base.docWriter.appendTable(anchor);
        NodeList nl = node.getElementsByTagName("Variable");
        for(int i = 0; i < nl.getLength(); i++){
            documentVariable((Element) nl.item(i));
        }
    }

    /**
     * helper method to document individual workflow variables, called
     * from documentVariables()
     *
     * @throws                  IOException
     */
    @SuppressWarnings("unchecked")
	static void documentVariable(Element variable) throws IOException {
        List<String> row = new ArrayList<String>();
        String name = getNameForElement(variable);
        Documentizer.base.docWriter.appendHorizontalRule();
        Documentizer.base.docWriter.append("<div class='collapse'>");
        Documentizer.base.docWriter.appendH3(name);
        Documentizer.base.docWriter.append("<div>");

        List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes(variable);
        processTableForReferences(table);
        table.add(Documentizer.base.docWriter.getTagTextValueRow(variable, "Description"));
        table.add(Documentizer.base.docWriter.getTagTextValueRow(variable, "AllowedValues"));
        table.add(Documentizer.base.docWriter.getTagTextValueRow(variable, "DefaultValue"));
        table.add(Documentizer.base.docWriter.getTagTextValueRow(variable, "Prompt"));

        String script = getNodeAttribute(variable, "initializer");
        String scriptBlock = getNodeTextForPath(variable, "Script");
        variableValues.put(name, script);
        if(scriptBlock.length() > 0){
            String codeBlock = Documentizer.base.docWriter.getCodeBlock(scriptBlock);
            variableValues.put(name, codeBlock);
            row = new ArrayList<String>();
            row.add("Script");
            row.add(codeBlock);
            table.add(row);
        }

        Documentizer.base.docWriter.appendTable(table);
        Documentizer.base.docWriter.append("</div>");
        Documentizer.base.docWriter.append("</div>");
    }

    /**
     * this method scans table for references and attempts to
     * get links for each
     *
     * @throws                  IOException
     */
    static void processTableForReferences(List<List<String>> table){
        for(List<String> row : table){
            if(row.size() == 2){
                String cell = (String) row.get(1);
                if(cell.startsWith("ref:")){
                    String refName = cell.substring(4);
                    if(variableValues.containsKey(refName)){
                        String refValue = variableValues.get(refName);
                        if(refValue.length() == 0){
                            refValue = "No value supplied in this workflow.";
                        }
                        String anchor = Documentizer.base.docWriter.getLink(null, cell, refValue);
                        row.set(1, anchor);
                    }
                    else{
                        String anchor = Documentizer.base.docWriter.getLink(null, cell, "No initializer found.");
                        row.set(1, anchor);
                    }
                }
            }
        }
    }

    /**
     * this method documents a workflow step
     *
     * @throws                  IOException
     */
    @SuppressWarnings("unchecked")
	private static void documentStep(Element step) throws IOException {
        List<String> row = new ArrayList<String>();
        String name = getNameForElement(step);
        String anchor = Documentizer.base.docWriter.getAnchor("step:" + name, "Step: " + name);

        Documentizer.base.docWriter.append("<div class='collapse'>");
        Documentizer.base.docWriter.appendH3(anchor);
        Documentizer.base.docWriter.append("<div>");

        List<List<String>> table = Documentizer.base.docWriter.getTableForXmlTagAttributes(step, anchor);
        table.add(Documentizer.base.docWriter.getTagTextValueRow(step, "Description"));
        row.add("Arguments");
        List<List<String>> tableArguement = Documentizer.base.docWriter.getTableForArguments(step);
        table.addAll(tableArguement);

        processTableForReferences(table);
        Documentizer.base.docWriter.appendTable(table);
        documentStepWorkflowRef(step);
        //this.documentStepApproval(step);

        String scriptBlock = getNodeTextForPath(step, "Script");
        if(scriptBlock.length() > 0){
            List<List<String>> tableScript = Documentizer.base.docWriter.getTableEmpty();
            row = new ArrayList<String>();
            row.add("Step Script");
            tableScript.add(row);
            row = new ArrayList<String>();
            row.add(Documentizer.base.docWriter.getCodeBlock(scriptBlock));
            tableScript.add(row);
            Documentizer.base.docWriter.appendTable(tableScript);
        }

        documentStepTransitions(step);
        documentStepReturns(step);
        Documentizer.base.docWriter.append("</div>");
        Documentizer.base.docWriter.append("</div>");
        Documentizer.base.docWriter.appendHorizontalRule();

    }

    /**
     * this method documents and call to a sub process from a workflow
     * step
     *
     * @throws                  IOException
     */
    private static void documentStepWorkflowRef(Element step) throws IOException {
        List<List<String>> table = new ArrayList<List<String>>();
        List<String> row = null;
        Element wfr = (Element) step.getElementsByTagName("WorkflowRef").item(0);
        if(wfr != null){
            NodeList refs = wfr.getElementsByTagName("Reference");
            for(int i = 0; i < refs.getLength(); i++){
                row = new ArrayList<String>();
                Element reference = (Element) refs.item(i);
                List<String> ruleRef = Documentizer.base.docWriter.getReferenceParts(reference);
                String link = Documentizer.base.docWriter.getLinkForObject(ruleRef);
                row.add(ruleRef.get(0));
                row.add(link);
                table.add(row);
            }
            Documentizer.base.docWriter.appendTable(table, "Workflow Reference");
        }
    }

    /**
     * this method documents all steps of a workflow
     *
     * @throws                  IOException
     */
    private static void documentSteps() throws IOException {

    	Documentizer.base.docWriter.appendHorizontalRule();
        List<List<String>> table = new ArrayList<List<String>>();
        List<String> row = new ArrayList<String>();
        String anchor = Documentizer.base.docWriter.getAnchor("Steps", "Workflow Steps");
        stepLinks = new HashMap<String, String>();
        NodeList nl = node.getElementsByTagName("Step");
        for(int i = 0; i < nl.getLength(); i++){
            Element step = (Element) nl.item(i);
            row = new ArrayList<String>();
            String name = getNameForElement(step);
            String link = Documentizer.base.docWriter.getLink("#" + "step:" + name, name);
            row.add(link);
            table.add(row);
            stepLinks.put(name, link);
        }
        Documentizer.base.docWriter.appendTable(table, anchor);
        for(int j = 0; j < nl.getLength(); j++){
            Element step = (Element) nl.item(j);
            documentStep(step);
        }
    }

    /**
     * this method documents the return values of a workflow step
     *
     * @throws                  IOException
     */
    private static void documentStepReturns(Element step) throws IOException {
        List<List<String>> table = new ArrayList<List<String>>();
        List<String> row = new ArrayList<String>();
        row.add("Name");
        row.add("To");
        table.add(row);

        NodeList nl = step.getElementsByTagName("Return");
        for(int i = 0; i < nl.getLength(); i++){
            Element stepReturn = (Element) nl.item(i);
            row = new ArrayList<String>();
            row.add(getNodeAttribute(stepReturn, "name"));
            row.add(getNodeAttribute(stepReturn, "to"));
            table.add(row);
        }
        Documentizer.base.docWriter.appendTable(table, "Step Return Variables");
    }

    /**
     * this method documents the transitions of a workflow step
     *
     * @throws                  IOException
     */
    private static void documentStepTransitions(Element step) throws IOException {
        List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
        List<String> row = new ArrayList<String>();
        row.add("Step to Transition to");
        row.add("Condition");
        table.add(row);

        NodeList nl = step.getElementsByTagName("Transition");
        for(int i = 0; i < nl.getLength(); i++){
            Element arg = (Element) nl.item(i);
            String transitionTo = getNodeAttribute(arg, "to");
            if(stepLinks.containsKey(transitionTo)){
                transitionTo = stepLinks.get(transitionTo);
            }
            String transitionWhen = getNodeAttribute(arg, "when");
            String script = getNodeTextForPath(arg, "Script");
            if(transitionWhen.length() > 0){
                row = new ArrayList<String>();
                row.add(transitionTo);
                row.add(transitionWhen);
                table.add(row);
            }
            else if(script.length() > 0){
                row = new ArrayList<String>();
                row.add(transitionTo);
                row.add("Script Block");
                table.add(row);

                row = new ArrayList<String>();
                row.add(Documentizer.base.docWriter.getCodeBlock(script));
                table.add(row);
            }
            else{
                row = new ArrayList<String>();
                row.add(transitionTo);
                row.add("Always");
                table.add(row);
            }
        }
        processTableForReferences(table);
        Documentizer.base.docWriter.appendTable(table, "Transition");
    }
}

