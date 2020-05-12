package sailpoint.services.tools.docgen;

import static sailpoint.services.tools.docgen.DocumentizerUtils.getFilenameForObject;
import static sailpoint.services.tools.docgen.DocumentizerUtils.getRealRoot;
import static sailpoint.services.tools.docgen.DocumentizerUtils.padLeft;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Created by Shant Jain on 02/06/17.
 */

public class DocRuleRegistry extends DocBase {

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
         Documentizer.base.setObjectType(DocBase.DOCTYPE.RuleRegistry.name());
         Documentizer.base.buildFrameList();
      }
   }


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
               Element root = getRealRoot(doc.getDocumentElement(), "RuleRegistry");
               node = root;
               Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.RuleRegistry);

         Documentizer.base.name = node.getAttribute("name");
         Documentizer.base.docWriter.appendH1(Documentizer.base.name);
         DocBase.objectList.put(getFilenameForObject(Documentizer.base.name),
               Documentizer.base.name);
         List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
         List<String> row = new ArrayList<String>();
         row = new ArrayList<String>();
         NodeList ruleList = node.getElementsByTagName("Rule");
         for (int i = 0; i < ruleList.getLength(); i++) {
            row = new ArrayList<String>();
            Element rule = (Element) ruleList.item(i);
            String name = rule.getAttribute("name");
            String link = Documentizer.base.docWriter.getLink("#" + name, name);
            row.add(link);
            table.add(row);
         }
         String anchor = Documentizer.base.docWriter.getAnchor("Rule List");
         Documentizer.base.docWriter.appendTable(table, anchor);
         documentRule(ruleList);

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

   private static void documentRule(NodeList ruleList) {
      for (int i = 0; i < ruleList.getLength(); i++) {
         Element rule = (Element) ruleList.item(i);
         try {
            List<List<String>> table = Documentizer.base.docWriter
                  .getTableForXmlTagAttributes(rule);
            Documentizer.base.docWriter.addDescriptionToTable(rule, table);
            String anchor = Documentizer.base.docWriter.getAnchor(rule
                  .getAttribute("name"));
            Documentizer.base.docWriter.appendTable(table, anchor);
            Documentizer.base.docWriter.documentSignature(rule);

            documentRuleReferences();
            documentRuleBackReferences();

            NodeList nl = node.getElementsByTagName("Source");
            Element source = (Element) nl.item(0);
            if (source != null) {
               String ruleCode = source.getTextContent();
               if (!ruleCode.contains("\r\n")) {
                  ruleCode = ruleCode.replace("\n", "\r\n");
               }
               documentCode(ruleCode);
               Documentizer.base.docWriter.appendBreak();
            }
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   private static void documentRuleReferences() throws IOException {
      List<List<String>> table = new ArrayList<List<String>>();
      List<String> row = null;
      NodeList refRules = node.getElementsByTagName("ReferencedRules");
      Element refRule = (Element) refRules.item(0);
      if (refRule != null) {
         NodeList refs = refRule.getElementsByTagName("Reference");
         for (int i = 0; i < refs.getLength(); i++) {
            Element reference = (Element) refs.item(i);
            row = new ArrayList<String>();
            List<String> ruleReferences = Documentizer.base.docWriter
                  .getReferenceParts(reference);
            String objectFileName = getFilenameForObject(ruleReferences.get(1));
            String link = Documentizer.base.docWriter.getLink(objectFileName,
                  ruleReferences.get(1));
            row.add(ruleReferences.get(0));
            row.add(link);
            table.add(row);
         }
         Documentizer.base.docWriter.appendTable(table, "Rule References");
      }
   }

   private static void documentRuleBackReferences() throws IOException {
      List<List<String>> table = new ArrayList<List<String>>();
      List<String> row = null;
      String fullName = Documentizer.base.objectType + ":"
            + Documentizer.base.name;
      if (DocBase.references.containsKey(fullName)) {
         for (String link : DocBase.references.get(fullName)) {
            row = new ArrayList<String>();
            row.add(link);
            table.add(row);
         }
         Documentizer.base.docWriter.appendTable(table, "Rule Back-References");
      }
   }

   private static String makeBookmark(String methodName,
         List<String> methodParams) {
      StringBuilder sb = new StringBuilder();
      sb.append(methodName.trim());
      for (String paramPair : methodParams) {
         int index = paramPair.indexOf(' ');
         sb.append(paramPair.substring(0, index).trim());
      }
      return sb.toString();
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   private static void documentCode(String ruleCode) throws IOException {
      RuleParser parser = new RuleParser();
      List<HashMap<String, List<String>>> methods = parser.getMethods(ruleCode);
      if (methods.size() == 0) {
         Documentizer.base.docWriter.appendTable("Code");
         Documentizer.base.docWriter.appendCodeBlock(ruleCode);
         return;
      }

      List<List<String>> table = new ArrayList<List<String>>();
      List<String> row = new ArrayList<String>();
      row.add("<b>Return Type</b>");
      row.add("<b>Signature</b>");
      table.add(row);

      for (HashMap method : methods) {
         String methodName = ((List<String>) method.get("name")).get(0);
         if (methodName.equalsIgnoreCase("if")
               || methodName.equalsIgnoreCase("else")) {
            continue;
         }
         String returns = ((List<String>) method.get("returns")).get(0);
         

         List<String> methodParams = (List<String>) method.get("params");
         row = new ArrayList<String>();
         row.add(returns);

         // String joinedParams = String.join(",", methodParams);
         String joinedParams = "";
         for (String methodParam : methodParams) {
            if (joinedParams.equals("")) {
               joinedParams = methodParam;
            } else {
               joinedParams = joinedParams + "," + methodParam;
            }
         }
         String methodSignature = String.format("%s(%s)", methodName,
               joinedParams);
         String bookmarkLabel = makeBookmark(methodName, methodParams);
         String link = Documentizer.base.docWriter.getLink("#" + bookmarkLabel,
               methodSignature);

         row.add(link);
         table.add(row);
      }

      Documentizer.base.docWriter.appendTable(table, "Method Summary");

      Documentizer.base.docWriter.appendTable("Method Detail");

      for (HashMap method : methods) {
         String methodName = ((List<String>) method.get("name")).get(0);
         if (methodName.equalsIgnoreCase("if")
               || methodName.equalsIgnoreCase("else")) {
            continue;
         }
         String returns = ((List<String>) method.get("returns")).get(0);
         String access = ((List<String>) method.get("access")).get(0);
         String codeBlock = ((List<String>) method.get("code")).get(0);

         List<String> methodParams = (List<String>) method.get("params");
         String space = "";
         space = padLeft(space,
               access.length() + returns.length() + methodName.length() + 2);

         // String joinedParams = String.join(",\n" + space, methodParams);
         String joinedParams = "";
         for (String methodParam : methodParams) {
            if (joinedParams.equals("")) {
               joinedParams = methodParam;
            } else {
               joinedParams = joinedParams + ",\n" + space + methodParam;
            }
         }
    

         String comments = ((List<String>) method.get("comments")).get(0);

         String bookmarkLabel = makeBookmark(methodName, methodParams);

         Documentizer.base.docWriter.appendAnchor(bookmarkLabel, "");
         Documentizer.base.docWriter.appendH2(methodName);

         String methodWithParams = String.format("%s %s %s(%s)", access,
               returns, methodName, joinedParams);

         Documentizer.base.docWriter.appendCodeBlock(comments);
         Documentizer.base.docWriter.appendCodeBlock(methodWithParams);
         Documentizer.base.docWriter.appendCodeBlock(codeBlock);
         Documentizer.base.docWriter.appendHorizontalRule();

      }

   }

}
