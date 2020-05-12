package sailpoint.services.tools.docgen;

import static sailpoint.services.tools.docgen.DocumentizerUtils.getFilenameForObject;
import static sailpoint.services.tools.docgen.DocumentizerUtils.getNodeTextForPath;
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

/**
 * Created by adam.creaney on 3/24/16.
 */
public class DocApplication extends DocBase {
	public static String seperator;
	private static Element node;

	public static void doDocumentation(ArrayList<File> files)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		if (files != null && !files.isEmpty()) {
			if (!DocBase.objectList.isEmpty()) {
				DocBase.objectList.clear();
			}
			for (File f : files) {
				docObject(f);
			}
			Documentizer.base.setObjectType(DocBase.DOCTYPE.Application.name());
			Documentizer.base.buildFrameList();
		}
	}

	@SuppressWarnings("unchecked")
	public static void docObject(File file) throws IOException, SAXException,
			ParserConfigurationException, TransformerException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		// TODO: Why/how can we avoid the dtd processing besides this line?
		dbFactory
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		InputStream is = new FileInputStream(file);
		Document doc = dBuilder.parse(is);

		doc.getDocumentElement().normalize();

		try {
			Element root = getRealRoot(doc.getDocumentElement(), "Application");

			// Element root = doc.createElement("Application");

			node = root;

			Documentizer.base.docWriter = new DocWriter(DocBase.destination,
					root, DocBase.DOCTYPE.Application);

			Documentizer.base.name = node.getAttribute("name");
			Documentizer.base.docWriter.appendH1(Documentizer.base.name);
			DocBase.objectList.put(
					getFilenameForObject(Documentizer.base.name),
					Documentizer.base.name);

			List<List<String>> table = Documentizer.base.docWriter
					.getTableForXmlTagAttributes(node);
			Documentizer.base.docWriter.addReferenceRowToTable(table, node,
					"Owner");
			Documentizer.base.docWriter.addReferenceRowToTable(table, node,
					"ProxyApplication");
			Documentizer.base.docWriter.addReferenceRowToTable(table, node,
					"AccountCorrelationConfig");
			Documentizer.base.docWriter.appendTable(table);

			buildNavigationTable();
			documentAttributes();
			documentApplicationSchemas();
			documentApplicationRules();
			documentProvisioningForms();
			documentProvisioningForm();
			documentProvisioningConfig();
			documentApplicationTemplates();
			documentProxyApplication();

		}  catch(NullPointerException e ){        	  
     	   
        }
        catch (Exception e) {
        	e.printStackTrace();
     	} finally {
			if (Documentizer.base.docWriter != null) {
				Documentizer.base.docWriter.close();
			}
		}
	}

	private static void buildNavigationTable() throws IOException {
		List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
		List<String> row = new ArrayList<String>();

		row.add(Documentizer.base.docWriter
				.getLink("#Attributes", "Attributes"));
		table.add(row);

		row = new ArrayList<String>();
		row.add(Documentizer.base.docWriter.getLink("#Schemas", "Schemas"));
		table.add(row);

		row = new ArrayList<String>();
		row.add(Documentizer.base.docWriter.getLink("#Rules", "Rules"));
		table.add(row);

		row = new ArrayList<String>();
		row.add(Documentizer.base.docWriter.getLink("#ProvisioningForms",
				"ProvisioningForms"));
		table.add(row);

		row = new ArrayList<String>();
		row.add(Documentizer.base.docWriter.getLink("#ProvisioningConfig",
				"ProvisioningConfig"));
		table.add(row);

		row = new ArrayList<String>();
		row.add(Documentizer.base.docWriter.getLink("#Templates",
				"Provisioning Templates"));
		table.add(row);

		Documentizer.base.docWriter.appendTable(table, "Structure");
	}

	private static void documentAttributes() throws IOException,
			TransformerException {
		Documentizer.base.docWriter.documentAttributes(node);
	}

	private static void documentApplicationSchemas() throws IOException {
		List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
		List<String> row = new ArrayList<String>();

		Element schemas = (Element) node.getElementsByTagName("Schemas")
				.item(0);
		if (schemas != null) {
			row = new ArrayList<String>();
			NodeList schemaList = schemas.getElementsByTagName("Schema");
			for (int i = 0; i < schemaList.getLength(); i++) {
				row = new ArrayList<String>();
				Element schema = (Element) schemaList.item(i);
				String objectType = schema.getAttribute("objectType");
				String link = Documentizer.base.docWriter.getLink(
						"#Object Type: " + objectType, objectType);
				row.add(link);
				table.add(row);
			}
			String anchor = Documentizer.base.docWriter.getAnchor("Schemas");
			Documentizer.base.docWriter.appendTable(table, anchor);

			for (int i = 0; i < schemaList.getLength(); i++) {
				Element schema = (Element) schemaList.item(i);
				String objectType = schema.getAttribute("objectType");
				anchor = Documentizer.base.docWriter.getAnchor("Object Type: "
						+ objectType);
				Documentizer.base.docWriter.append("<div class='collapse'>");

				Documentizer.base.docWriter.appendH3(anchor);
				Documentizer.base.docWriter.append("<div>");

				documentApplicationSchema(schema);
				Documentizer.base.docWriter.append("</div>");
				Documentizer.base.docWriter.append("</div>");
				Documentizer.base.docWriter.appendHorizontalRule();
			}
		}
	}

	/*
	 * private static void documentApplicationSchema(Element schema) throws
	 * IOException { String objectType = schema.getAttribute("objectType");
	 * String heading = String.format("%s Schema", objectType); String anchor =
	 * Documentizer.base.docWriter.getAnchor(objectType, heading);
	 * Documentizer.base.docWriter.documentXmlTagAttributes(schema, anchor);
	 * 
	 * NodeList attrDefs = schema.getElementsByTagName("AttributeDefinition");
	 * List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
	 * List<String> row = new ArrayList<String>();
	 * row.add("<b>Attribute Name</b>"); row.add("<b>Type</b>"); table.add(row);
	 * 
	 * for(int i = 0; i < attrDefs.getLength(); i++){ row = new
	 * ArrayList<String>(); Element attrDef = (Element) attrDefs.item(i); String
	 * name = attrDef.getAttribute("name"); String type =
	 * attrDef.getAttribute("type"); System.out.println("111111");
	 * row.add(name);
	 * 
	 * String isEntitlement = attrDef.getAttribute("entitlement");
	 * if(isEntitlement.equalsIgnoreCase("true")){ String managed =
	 * attrDef.getAttribute("managed"); String multi =
	 * attrDef.getAttribute("multi"); System.out.println("22222"); String
	 * schemaObjType = attrDef.getAttribute("schemaObjectType"); String link =
	 * Documentizer.base.docWriter.getLink("#objectType", schemaObjType);
	 * row.add(link + " entitlement {" + name + ", " + type + ", " + managed +
	 * ", " + multi + "}"); } else{ row.add(type); } table.add(row);
	 * //this.docWriter.documentXmlTagAttributes((Element) attrDefs.item(i)); }
	 * Documentizer.base.docWriter.appendTable(table, heading + " Attributes");
	 * }
	 */

	private static void documentApplicationSchema(Element schema)
			throws IOException {
		String objectType = schema.getAttribute("objectType");
		String heading = String.format("%s Schema", objectType);
		String anchor = Documentizer.base.docWriter.getAnchor(objectType,
				heading);
		Documentizer.base.docWriter.documentXmlTagAttributes(schema, anchor);

		NodeList attrDefs = schema.getElementsByTagName("AttributeDefinition");
		List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
		List<String> row = new ArrayList<String>();
		row.add("<b>Attribute Name</b>");
		row.add("<b>Type</b>");
		table.add(row);

		for (int i = 0; i < attrDefs.getLength(); i++) {
			row = new ArrayList<String>();
			Element attrDef = (Element) attrDefs.item(i);
			String name = attrDef.getAttribute("name");
			String type = attrDef.getAttribute("type");
			row.add(name);

			String isEntitlement = attrDef.getAttribute("entitlement");
			String remediationModificationType = attrDef
					.getAttribute("remediationModificationType");
			String managed = attrDef.getAttribute("managed");
			String multi = attrDef.getAttribute("multi");
			String schemaObjType = attrDef.getAttribute("schemaObjectType");

			if (isEntitlement.equalsIgnoreCase("true")
					&& !(remediationModificationType.isEmpty()))
				row.add(type + " [Entitlement:true, Managed:" + managed
						+ ", Multi-Valued:" + multi + ", SchemaObjectType:"
						+ schemaObjType + ", remediationModificationType:"
						+ remediationModificationType + "]");

			else if (isEntitlement.equalsIgnoreCase("true")
					&& remediationModificationType.isEmpty())
				row.add(type + " [Entitlement:true, Managed:" + managed
						+ ", Multi-Valued:" + multi + ", SchemaObjectType:"
						+ schemaObjType + "]");

			else if (isEntitlement.isEmpty()
					&& !(remediationModificationType.isEmpty()))
				row.add(type + " [remediationModificationType:"
						+ remediationModificationType + "]");

			else {
				row.add(type);
			}
			table.add(row);
		}
		Documentizer.base.docWriter.appendTable(table, heading + " Attributes");
	}

	@SuppressWarnings("unchecked")
	private static void documentProvisioningConfig() throws IOException {
		String anchor = Documentizer.base.docWriter
				.getAnchor("ProvisioningConfig");
		List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
		Element provisioningConfig = (Element) node.getElementsByTagName(
				"ProvisioningConfig").item(0);
		if (provisioningConfig != null) {
			table = Documentizer.base.docWriter.getTableForXmlTagAttributes(
					provisioningConfig, "");
			List<String> refParts = Documentizer.base.docWriter
					.getReferenceParts(provisioningConfig, "PlanInitializer");
			if (refParts.size() > 0) {
				List<String> row = new ArrayList<String>();
				String link = Documentizer.base.docWriter
						.getLinkForObject(refParts);
				row.add(link);
				table.add(row);
			}
			Documentizer.base.docWriter.appendTable(table, anchor);
		}
	}

	private static void documentApplicationRules() throws IOException {
		String anchor = Documentizer.base.docWriter.getAnchor("Rules");
		List<List<String>> table = new ArrayList<List<String>>();
		List<String> row = new ArrayList<String>();

		List<String> referencedRules = new ArrayList<String>();
		referencedRules.add("CustomizationRule");
		referencedRules.add("CorrelationRule");
		referencedRules.add("CreationRule");
		referencedRules.add("ManagedAttributeCustomizationRule");
		referencedRules.add("ManagerCorrelationRule");

		for (String rule : referencedRules) {
			List<String> ruleParts = Documentizer.base.docWriter
					.getReferenceParts(node, rule);
			if (ruleParts.size() > 0) {
				row = new ArrayList<String>();
				String link = Documentizer.base.docWriter
						.getLinkForObject(ruleParts);
				String bookmark = "AppRule:" + ruleParts.get(1);
				String linkAnchor = Documentizer.base.docWriter.getAnchor(
						bookmark, link);
				row.add(linkAnchor);
				Documentizer.base.addBackReference(ruleParts, bookmark);
				table.add(row);
			}
		}

		List<String> valueRules = new ArrayList<String>();
		valueRules.add("provisionRule");
		valueRules.add("buildMapRule");
		valueRules.add("afterProvisioningRule");
		valueRules.add("beforeProvisioningRule");
		valueRules.add("jdbcCreateProvisioningRule");
		valueRules.add("jdbcDeleteProvisioningRule");
		valueRules.add("jdbcDisableProvisioningRule");
		valueRules.add("jdbcEnableProvisioningRule");
		valueRules.add("jdbcModifyProvisioningRule");
		valueRules.add("jdbcProvisionRule");
		valueRules.add("jdbcUnlockProvisioningRule");
		valueRules.add("mapToResourceObjectRule");
		valueRules.add("mergeMapsRule");

		List<String> ruleParts = new ArrayList<String>();
		for (String r : valueRules) {
			ruleParts = Documentizer.base.docWriter
					.getReferencePartsNoReference(node, r, "Rule");
			if (ruleParts.size() > 0) {
				row = new ArrayList<String>();
				String link = Documentizer.base.docWriter
						.getLinkForObject(ruleParts);
				String bookmark = "AppRule:" + ruleParts.get(1);
				String linkAnchor = Documentizer.base.docWriter.getAnchor(
						bookmark, link);
				row.add(linkAnchor);
				Documentizer.base.addBackReference(ruleParts, bookmark);
				table.add(row);
			}
		}

		Documentizer.base.docWriter.appendTable(table, anchor);
	}

	private static void documentProxyApplication() throws IOException {
		String anchor = Documentizer.base.docWriter
				.getAnchor("ProxyApplication");
		List<List<String>> table = new ArrayList<List<String>>();
		List<String> row = new ArrayList<String>();

		List<String> parts = Documentizer.base.docWriter.getReferenceParts(
				node, "ProxyApplication");
		if (parts.size() > 0) {
			row = new ArrayList<String>();
			String link = Documentizer.base.docWriter.getLinkForObject(parts);
			String bookmark = "ProxyApp:" + parts.get(1);
			String linkAnchor = Documentizer.base.docWriter.getAnchor(bookmark,
					link);
			row.add(linkAnchor);
			Documentizer.base.addBackReference(parts, bookmark);
			table.add(row);
			Documentizer.base.docWriter.appendTable(table, anchor);
		}
	}

	private static void documentApplicationTemplates() throws IOException {
		String anchor = Documentizer.base.docWriter.getAnchor("Templates");
		List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
		List<String> row = new ArrayList<String>();
		Element templates = (Element) node.getElementsByTagName("Templates")
				.item(0);
		if (templates != null) {
			NodeList nl = templates.getElementsByTagName("Template");
			for (int i = 0; i < nl.getLength(); i++) {
				Element template = (Element) nl.item(i);
				row = new ArrayList<String>();
				String templateName = template.getAttribute("name");
				String templateUsage = template.getAttribute("usage");
				String bookmark = String.format("%s:%s", templateUsage,
						templateName);
				String link = Documentizer.base.docWriter.getLink("#"
						+ bookmark, bookmark);
				row.add(link);
				table.add(row);
			}
			Documentizer.base.docWriter.appendTable(table, anchor);
			for (int j = 0; j < nl.getLength(); j++) {
				Element template = (Element) nl.item(j);
				String templateName = template.getAttribute("name");
				String templateUsage = template.getAttribute("usage");
				documentApplicationTemplate(template, templateName,
						templateUsage);
			}
		}
	}

	private static void documentApplicationTemplate(Element template,
			String templateName, String templateUsage) throws IOException {
		String heading = String.format("Template:%s Usage:%s", templateName,
				templateUsage);
		String bookmark = String.format("%s:%s", templateUsage, templateName);
		String anchor = Documentizer.base.docWriter
				.getAnchor(bookmark, heading);
		// this.docWriter.appendTable(anchor);

		Documentizer.base.docWriter.append("<div class='collapse'>");
		Documentizer.base.docWriter.appendH3(anchor);
		Documentizer.base.docWriter.append("<div>");
		NodeList fields = template.getElementsByTagName("Field");
		for (int i = 0; i < fields.getLength(); i++) {
			Element field = (Element) fields.item(i);
			String fieldName = field.getAttribute("name");
			String fieldHeading = fieldName;
			String fieldBookmark = String.format("%s: %s: %s", templateUsage,
					templateName, fieldName);
			String fieldAnchor = Documentizer.base.docWriter.getAnchor(
					fieldBookmark, fieldHeading);

			List<List<String>> table = Documentizer.base.docWriter
					.getTableForXmlTagAttributes(field, fieldHeading);
			List<String> ruleParts = Documentizer.base.docWriter
					.getReferenceParts(field, "RuleRef");
			if (ruleParts.size() > 0) {
				List<String> row = new ArrayList<String>();
				row.add("Value");
				String link = Documentizer.base.docWriter.getLinkForObject(
						ruleParts, "Rule:" + ruleParts.get(1), "");
				String linkAnchor = Documentizer.base.docWriter.getAnchor(
						fieldBookmark, link);
				row.add(linkAnchor);
				Documentizer.base.addBackReference(ruleParts, fieldBookmark);
				table.add(row);
			}
			String scriptBlock = getNodeTextForPath(field, "Script");
			if (scriptBlock.length() > 0) {
				List<List<String>> tableScript = Documentizer.base.docWriter
						.getTableEmpty();
				List<String> row = new ArrayList<String>();
				row.add("Field Script");
				tableScript.add(row);
				row = new ArrayList<String>();
				row.add(Documentizer.base.docWriter.getCodeBlock(scriptBlock));
				table.add(row);
			}
			Documentizer.base.docWriter.appendTable(table);
			Documentizer.base.docWriter.append("</div>");
			Documentizer.base.docWriter.append("</div>");
		}
	}

	private static void documentProvisioningForms() throws IOException {
		List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
		List<String> row = new ArrayList<String>();
		Element provisioningForms = (Element) node.getElementsByTagName(
				"ProvisioningForms").item(0);
		NodeList formList;

		if (provisioningForms != null) {
			row = new ArrayList<String>();
			formList = provisioningForms.getElementsByTagName("Form");
			for (int i = 0; i < formList.getLength(); i++) {
				row = new ArrayList<String>();
				Element form = (Element) formList.item(i);
				// Element constraints = (Element)
				// profile.getElementsByTagName("Constraints").item(0);
				// Element filter = (Element)
				// constraints.getElementsByTagName("Filter").item(0);
				String name = form.getAttribute("name");
				String link = Documentizer.base.docWriter.getLink("#Form : "
						+ name, name);
				row.add(link);
				table.add(row);
			}
			String anchor = Documentizer.base.docWriter
					.getAnchor("ProvisioningForms");
			Documentizer.base.docWriter.appendTable(table, anchor);
		}
	}

	private static void documentProvisioningForm() throws IOException {
		List<String> row = new ArrayList<String>();
		Element provisioningForms = (Element) node.getElementsByTagName(
				"ProvisioningForms").item(0);
		NodeList formList;
		String anchor;
		if (provisioningForms != null) {
			row = new ArrayList<String>();
			formList = provisioningForms.getElementsByTagName("Form");
			for (int i = 0; i < formList.getLength(); i++) {
				List<List<String>> table = Documentizer.base.docWriter
						.getTableEmpty();
				row = new ArrayList<String>();
				Element form = (Element) formList.item(i);
				String name = form.getAttribute("name");
				anchor = Documentizer.base.docWriter
						.getAnchor("Object Type: Form - " + name);

				row = new ArrayList<String>();
				String tablevalues = "Name:" + form.getAttribute("name")
						+ ", ObjectType:" + form.getAttribute("objectType")
						+ ", Type:" + form.getAttribute("type");

				row.add(tablevalues);
				table.add(row);

				Element description = (Element) form.getElementsByTagName(
						"Description").item(0);
				if (description != null) {
					row = new ArrayList<String>();
					String desc = "Description: "
							+ form.getElementsByTagName("Description").item(0)
									.getTextContent();
					row.add(desc);
					table.add(row);
				}

				// populating form field value columns
				Documentizer.base.docWriter.append("<div class='collapse'>");
				Documentizer.base.docWriter.appendH3(anchor);
				Documentizer.base.docWriter.append("<div>");

				row = new ArrayList<String>();
				row.add("<b>DisplayName</b>");
				row.add("<b>Value</b>");
				row.add("<b>Name</b>");
				row.add("<b>Required</b>");
				row.add("<b>Type</b>");
				row.add("<b>FilterString</b>");
				table.add(row);

				Documentizer.base.docWriter.append("</div>");
				Documentizer.base.docWriter.append("</div>");

				NodeList fieldList = form.getElementsByTagName("Field");
				for (int j = 0; j < fieldList.getLength(); j++) {
					Element field = (Element) fieldList.item(j);

					// populating field values

					row = new ArrayList<String>();
					row.add(field.getAttribute("displayName"));

					// if displayname=password, value=*****, if there is script
					// for value, extract data from script

					String displayname = field.getAttribute("displayName");
					if (displayname.toLowerCase().contains("password"))
						row.add("*****");
					else if (field.getElementsByTagName("Script").getLength() != 0) {
						Element script = (Element) field.getElementsByTagName(
								"Script").item(0);
						row.add(script.getElementsByTagName("Source").item(0)
								.getTextContent());
					}

					// below code can be used to display script code in a
					// separate code table(proper java format) with a link in
					// form table

					/*
					 * else
					 * if(field.getElementsByTagName("Script").getLength()!=0){
					 * Element script = (Element)
					 * field.getElementsByTagName("Script").item(0); Element
					 * source = (Element)
					 * script.getElementsByTagName("Source").item(0); if (source
					 * != null) { String ruleCode = source.getTextContent(); if
					 * (!ruleCode.contains("\r\n")) ruleCode =
					 * ruleCode.replace("\n", "\r\n"); String codetable =
					 * displayname.toLowerCase() + "-script"; String link =
					 * Documentizer.base.docWriter.getLink("#" + codetable,
					 * codetable); row.add(link); documentCodeinTable(ruleCode,
					 * codetable); } }
					 */

					else
						row.add(field.getAttribute("value"));
					row.add(field.getAttribute("name"));
					row.add(field.getAttribute("required"));
					row.add(field.getAttribute("type"));
					row.add(field.getAttribute("filterString"));
					table.add(row);
				}
				Documentizer.base.docWriter.appendHorizontalRule();
				anchor = Documentizer.base.docWriter
						.getAnchor("Form : " + name);
				Documentizer.base.docWriter.appendTable(table, anchor);
			}

		}
	}

	// use below method to render script code in proper format with a link
	// available in form table.

	/*
	 * private static void documentCodeinTable(String ruleCode, String
	 * tablename) throws IOException { RuleParser parser = new RuleParser();
	 * List<HashMap<String, List<String>>> methods =
	 * parser.getMethods(ruleCode); if (methods.size() == 0) {
	 * Documentizer.base.docWriter.appendTable(tablename);
	 * Documentizer.base.docWriter.appendCodeBlock(ruleCode); return; } }
	 */
}
