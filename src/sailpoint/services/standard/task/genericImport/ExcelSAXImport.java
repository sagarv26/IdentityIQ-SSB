package sailpoint.services.standard.task.genericImport;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sailpoint.api.SailPointContext;
import sailpoint.tools.GeneralException;

/**
 * The ExcelSAXImporter iterator grabs any OOXML Excel file and allows a worksheets
 * values to be iterated over.
 * 
 * 11/10.2016	Bug detected by PaulW, The class was resolving the sheet name properly, need to reference
 * 				a relationship document to properly find the correct worksheet filename instead of
 * 				deriving it from the worksheet ID. Fixed.
 * 			
 * 					Added getXmlFileFromZipArchive method to grab the XML artifacts as DOM document
 * 					form the XSLX file
 * 
 * 					getWorksheetEntry method was fixed up so it now resolves the relationships
 * 					of a worksheet instead of deriving the ID.
 * 
 * @version		1.0.1
 * @author christian.cairney
 *
 */
public class ExcelSAXImport extends AbstractGenericImport implements GenericImport {

	private static final String IMPORT_EXCEL = "excel_";
	
	public static final String IMPORT_EXCEL_FILENAME = IMPORT_EXCEL + "filename";
	public static final String IMPORT_EXCEL_HAS_HEADER = IMPORT_EXCEL + "hasHeader";
	public static final String IMPORT_EXCEL_HEADER_ROW = IMPORT_EXCEL + "headerRow";
	public static final String IMPORT_EXCEL_SHEET_NAME = IMPORT_EXCEL + "sheetName";
	
	private static final String XLSX_WORKBOOK_FILENAME = "xl/workbook.xml";
	private static final String XLSX_WORKBOOK_RELATIONSHIPS_FILENAME = "xl/_rels/workbook.xml.rels";

	private String sheetName;
	private File xlFile;
	private int headerRow;
	private boolean hasHeader;
	//private String temporaryFilename;
	private String[] sharedStrings;
	private InputStream worksheetStream = null;
	
	private static final Logger log = Logger.getLogger(ExcelSAXImport.class);
	
	private class ImportIterator implements Iterator<List<String>> {

		//private int recordNo = 0;
		//private boolean hasNext = false;
		private XMLInputFactory xmlInputFactory = null;
		private XMLEventReader eventReader = null;
		private List<String> nextRow = null;
		
		/**
		 * Constructor
		 * 
		 * Checks if we have an input stream and will attempt to find
		 * a header if one has been specified.
		 * 
		 * @throws GeneralException
		 */
		public ImportIterator() throws GeneralException  {

			if (worksheetStream == null) throw new GeneralException("No worksheet input stream is open");
			xmlInputFactory = XMLInputFactory.newInstance();
			
			try {
				eventReader = xmlInputFactory.createXMLEventReader(worksheetStream);
			} catch (XMLStreamException e) {
				throw new GeneralException("Could not read stream using XMLEventReader",e);
			}
			
			// Do we need to grab a header?
			Schema schema = getSchema();
			if (hasHeader) {
				// We have a header, lets grab the first row
				log.debug("Attempting to get header row");
				
				int rowCount = 0;
				
				List<String> header = null;
				while (rowCount < headerRow) {
					header =  getNextRecord();
					rowCount++;

					if (header == null) throw new GeneralException("Indicated that a header was present but no row avaialble in worksheet");
				} 
				
				schema.init(header);
				nextRow = getNextRecord();
				
			} else {
				
				log.debug("No header row specified.");
				nextRow = getNextRecord();
				schema.init(nextRow.size());
				
			} 
			
			if (log.isDebugEnabled()) log.debug("Schema: " + schema.toString());
			
		}

		/**
		 * 
		 * @return
		 * @throws GeneralException
		 * 
		 */
		private List<String> getNextRecord() throws GeneralException {

			//TODO:  Revisit this method and look to see how to make it more robust as row 
			// variable is assumed to be instantiated.
			
			List<String> row = null;

			try {

				boolean endRow = false;
				String columnType = null;
				int columnId = 0;
				String rowId = null;
				
				boolean columnValue = false;

				while (eventReader.hasNext()) {

					XMLEvent event = eventReader.nextEvent();
					switch (event.getEventType()) {
						case XMLStreamConstants.START_ELEMENT:

							StartElement startElement = event.asStartElement();
							
							String elementName = startElement.getName().getLocalPart();
							if (log.isDebugEnabled())
								log.trace("Start element: " + elementName);
							
							// 
							// Parse the element, normally I would use
							// 
							if (elementName.equals("row")) {
								
								row = new ArrayList<String>();
								if (log.isDebugEnabled()) log.debug("Starting a new Row");
								QName qnameRowId = new QName("r");
								Attribute attrRowId = startElement.getAttributeByName(qnameRowId);
								if (attrRowId ==  null) throw new GeneralException("Row element does not have QName R");
								rowId = attrRowId.getValue();
								
							} else if (elementName.equals("c")) {
								
								QName qnameT = new QName("t");
								QName qnameR = new QName("r");
								Attribute type = startElement
										.getAttributeByName(qnameT);
								Attribute id = startElement
										.getAttributeByName(qnameR);
								
								if (id ==  null) throw new GeneralException("C element does not have QName R");

								if (type == null) {
									columnType = "n";
								} else {
									columnType = type.getValue();
								}
								
								
								
								// Return he column ID for the key name, but we will need to 
								// strip the row data otherwise it will be a unique column name per row!!
								// .... which is bad....
								
								String columnIdString = id.getValue();
								columnIdString = columnIdString.substring(0, columnIdString.length() - rowId.length());
								columnId = excelColumnToInt(columnIdString);
								if (log.isDebugEnabled()) log.debug("ColumnType=" + columnType + ", Column ID=" + columnId);
								
							} else if (elementName.equals("v")) {
								
								//if (columnId == null) {
								//	throw new GeneralException(
								//			"v element encountered without an id");
								//}
								columnValue = true;
								
							} else {
								if (log.isDebugEnabled()) log.debug("Element '" + elementName + "' was not handled.");
							}

							break;

						case XMLStreamConstants.CHARACTERS:

							Characters characters = event.asCharacters();
							
							if (columnValue) {
								
								if (log.isDebugEnabled()) log.debug("Processing column value of type '" + columnType + "'");
								
								// The columnValue flag is raised so we know the value
								// in the event is something we are interested in.
								// We need to transform the value to the appropriate data
								// type and then store in the map.
								
								// first though, this may not be the column we think it is... so check the column id.
								if (columnId > row.size() +1) {
									// It isn't, so lets add some null columns to pad this out
									for (int i = row.size() +1; i < columnId ; i++) {
										log.debug("Padding row data due to null values");
										row.add("");
									}
								}
								columnValue = false;
								
								if (columnType.equals("s")) {
									
									int lookup = Integer.parseInt(characters.getData());
									String value = sharedStrings[lookup];
									if (log.isDebugEnabled()) log.debug("Row.add(" + value + ")");
									row.add(sharedStrings[lookup]);
									
								} else if (columnType.equals("b")) {
									
									boolean bvalue = sailpoint.tools.Util.atob(characters.getData());
									String value = bvalue ? "true" : "false";
									if (log.isDebugEnabled()) log.debug("Row.add(" + value + ")");
									row.add(value);
									
								} else if (columnType.equals("n")) {
									
									String value = characters.getData();
									if (log.isDebugEnabled()) log.debug("Row.add(" + value + ")");
									row.add(value);
									
								} else if (columnType.equals("d")) {
									
									// Yodatime is our friend here, transform the excel
									// date time format to a java Date.
									DateTimeFormatter dateParser = ISODateTimeFormat.dateTimeNoMillis();
									String value = dateParser.parseDateTime(characters.getData()).toString("dd/mmm/yyyy hh:mm:ss").toString();
									if (log.isDebugEnabled()) log.debug("Row.add(" + value + ")");
									row.add(value);
									
								} else if (columnType.equals("inlineStr") || columnType.equals("str")) {
									
									String value = characters.getData();
									if (log.isDebugEnabled()) log.debug("Row.add(" + value + ")");
									row.add(value);
								
								} else {
									
									String value = characters.getData();
									if (log.isDebugEnabled()) log.debug("Row.add(" + value + ")");
									row.add(value);
								}
		
							}

							if (log.isTraceEnabled())
								log.trace("  Characters: "
										+ characters.getData());

							break;
						case XMLStreamConstants.END_ELEMENT:

							EndElement endElement = event.asEndElement();
							if (log.isTraceEnabled())
								log.trace("End element: "
										+ endElement.getName().getLocalPart());

							String elementNameLocalPart = endElement.getName().getLocalPart();
							
							if (elementNameLocalPart.equals("row")) {
								endRow = true;
								if (log.isDebugEnabled()) log.debug("Row: " + (row != null ? row.toString() : " NULL"));
							}
							break;
					}

					if (endRow) {
						break;
					}
				}

			} catch (XMLStreamException e) {
				throw new GeneralException("Error readintg the XML Event", e);
			}
			
			// make sure the column count is the same as the schema
			if (row != null && row.size() < getSchema().getSchemaColumnSize()) {
				int schemaSize = getSchema().getSchemaColumnSize();
				if (log.isDebugEnabled()) log.debug("Padding out row size from " + row.size() + " to " + schemaSize + " columns");
				while (row.size() < schemaSize) {
					row.add("");
				}
			}
			return row;
		}
		
		@Override
		public boolean hasNext() {
			return (nextRow != null);
		}


		@Override
		public List<String> next() throws NoSuchElementException {

			List<String> returnRow = new ArrayList<String>(nextRow);		
			try {
				nextRow = getNextRecord();
			} catch (GeneralException e) {
				
				// I know this is not great practice (anit-patter) but I need to output the actual error
				// as I cannot chain it to NoSuchElementException... probably my bad design
				// but frustrating all the same....
				
				log.error("Error in ExcekSAX.ImportIterator.next()", e);
				log.error(e.getStackTrace());
				
				// Then I'll throw something to hint there is a problem
				throw new NoSuchElementException("Could not get element, please check stack trace.  " + e.getMessage());
				
				
			}
			if (log.isDebugEnabled()) log.debug("Returning row: " +  (returnRow != null ? returnRow.toString() : " NULL"));
			return returnRow;
		}

		@Override
		public void remove() {
			// We don't support removes, no point on a read only CSV file?
			throw new UnsupportedOperationException();
		}
		
		private int excelColumnToInt(String name) {
	        int number = 0;
	        for (int i = 0; i < name.length(); i++) {
	            number = number * 26 + (name.charAt(i) - ('A' - 1));
	        }
	        return number;
	    }

	}

	
	public ExcelSAXImport(SailPointContext context) throws GeneralException {
		super(context);
	}
	
	public ExcelSAXImport() throws GeneralException {
		super();
		
	}
	
		
	/**
	 * Open the Excel XML file and ready for the iterator call
	 * @throws IOException 
	 */
	@Override
	public void open() throws GeneralException {
		
		// Get the filename
		if (getAttributes().containsKey(IMPORT_EXCEL_FILENAME)) {
			xlFile = new File(getAttributes().getString(IMPORT_EXCEL_FILENAME));
			if (!xlFile.exists()) {
				throw new GeneralException("Not found or not a file: " + xlFile.getPath());
			}
		} else {
			throw new GeneralException("No filename has been specified for the Excel importer.");
		}

		// get the sheet name
		if (getAttributes().containsKey(IMPORT_EXCEL_SHEET_NAME)) {
			sheetName = getAttributes().getString(IMPORT_EXCEL_SHEET_NAME);
		} else {
			throw new GeneralException("No Excel Sheet has been specified.");
		}

		// Get the header details
		hasHeader = getAttributes().getBoolean(IMPORT_EXCEL_HAS_HEADER);
		if (hasHeader) {
		
			if (getAttributes().containsKey(IMPORT_EXCEL_HEADER_ROW)) {
				headerRow = getAttributes().getInt(IMPORT_EXCEL_HEADER_ROW);
			} else {
				throw new GeneralException("Header has been specified but no header row indicated");
			}
			
		} 
		
		// Attempt to open the XLSX file, which is an
		// ZIP file.
		
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(xlFile.getAbsolutePath());
		} catch (IOException e) {
			throw new GeneralException("IO Exception while getting zipfile (xls)", e);
		}
		
		// Ok, got the XLSX file (zip), now we need to get
		// the worksheet entry
		ZipEntry workSheet = getWorksheetEntry(zipFile, sheetName);
		
		// Pull in all the shared strings
		readSharedStrings(zipFile);
			
		// Create a temp file
		File temp = null;
		try {
			temp = File.createTempFile("worksheet", ".xml");
			temp.deleteOnExit();
		} catch (IOException e) {
			throw new GeneralException("Could not create worksheet temporary file", e);
		} 
		
		// Save the zip file input stream as a temporary file so
		// we can close the zip file.

		
		log.debug("Getting workstation input stream");
		try {
			worksheetStream = zipFile.getInputStream(workSheet);
		} catch (IOException e1) {
			throw new GeneralException("Could not get workSheet input stream", e1);
		}

	}
	@Override
	public void close() throws GeneralException {
		if (worksheetStream != null) {
			try {
				worksheetStream.close();
			} catch (IOException e) {
				throw new GeneralException("Could not close Excel Worksheet stream due to error", e);
			}
		}
		
	}
	@Override
	/**
	 * Return the iterator which will iterate over the 
	 * worksheet stream.
	 * Expected that the open() method would have been 
	 * called first.
	 * 
	 */
	public Iterator<List<String>> iterator() throws GeneralException {
		
		return new ImportIterator();
	}
	
	/**
	 * Shared strings optimize space requirements when the spreadsheet contains multiple instances of the same string. 
	 * Spreadsheets that contain business or analytical data often contain repeating strings. If these strings were 
	 * stored using inline string markup, the same markup would appear over and over in the worksheet. While this 
	 * is a valid approach, there are several downsides. First, the file requires more space on disk because of the 
	 * redundant content. Moreover, loading and saving also takes longer.
	 * 
	 * We will load all the shred strings into an Array which is instantiated by grabbing the
	 * uniqueCount attribute on the sst element (Document root).
	 * 
	 * @param xlsxFile
	 * @throws GeneralException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private void readSharedStrings(ZipFile xlsxFile) throws GeneralException  {
		
		log.debug("Entering readSharedStrings()");
		
		try {
			ZipEntry entry = xlsxFile.getEntry("xl/sharedStrings.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xlsxFile.getInputStream(entry));
			Element docElement = doc.getDocumentElement();
			if (log.isDebugEnabled()) log.debug("Document: " + docElement.getNodeName());
			
			NamedNodeMap attributes = docElement.getAttributes();
			if (attributes == null) throw new GeneralException("No attributes found on " + docElement.getNodeName() + " document root.");
			Node uniqueCountNode = attributes.getNamedItem("uniqueCount");
			String uniqueCount = uniqueCountNode.getNodeValue().toString();
			Integer numberSharedStrings = Integer.parseInt(uniqueCount);
			if (log.isDebugEnabled()) log.debug("Number of shared strings to read in: " + String.valueOf(uniqueCount));
			
			sharedStrings = new String[numberSharedStrings];
			
			// All the child nodes are <si> elements, and they all have a <t> element with the value
			// order is important.
			NodeList siElements = docElement.getChildNodes();
			for (int i=0; i < siElements.getLength(); i++) {
				Node n = siElements.item(i);
				if (log.isDebugEnabled()) log.debug("Value: [" + String.valueOf(i) + "] = " + n.getTextContent());
				sharedStrings[i] = n.getTextContent();
			}
		} catch (ParserConfigurationException  e) {
			throw new GeneralException("Could not load shared strings", e);
		} catch (SAXException  e) {
			throw new GeneralException("Could not load shared strings", e);
		} catch (IOException e) {
			throw new GeneralException("Could not load shared strings", e);
		} finally {
			
		}
			
		log.debug("Exiting readSharedStrings()");
		
	}
	
	// Grab an XML file from a Zip Archive and return a DOM Document
	private Document getXmlFileFromZipArchive(ZipFile xlsxFile, String filename) throws GeneralException {
		
		ZipEntry entry = xlsxFile.getEntry(filename);
		StringBuilder sb = null;
		
		if (entry != null) {
			
			//Get the document into memory (Stringbuilder)
			
			BufferedReader inputReader;
			try {
				inputReader = new BufferedReader(new InputStreamReader(xlsxFile.getInputStream(entry)));
			} catch (IOException e) {
				throw new GeneralException("Could not get input reader from workbook");
			}
	        sb = new StringBuilder();
	        String inline = "";
	        try {
				while ((inline = inputReader.readLine()) != null) {
				  sb.append(inline);
				}
			} catch (IOException e) {
				throw new GeneralException("Could not read '" + filename + "' file from archive '" + xlsxFile.getName() + "'");
			}
	        
		} else {
			throw new GeneralException("Could not retrieve filename '" + filename + " from archive " + xlsxFile.getName());
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        
		
			try {
				builder = factory.newDocumentBuilder();
				document = builder.parse(IOUtils.toInputStream((sb.toString())));
			} catch (ParserConfigurationException  e) {
				throw new GeneralException("Could not parse workbook XML file", e);
			} catch ( SAXException e) {
				throw new GeneralException("Could not parse workbook XML file", e);
			} catch ( IOException  e) {
				throw new GeneralException("Could not parse workbook XML file", e);
			}
		return document;
		
	}
	
	/**
	 * The workbook.xml is the root element for the main document part.
	 * Here we need to fine the worksheet id so we can
	 * derive the worksheet entry in the ZIP(XLSX) file.
	 * 
	 * This method must return a value, or return an exception
	 * 
	 * @param xlsxFile
	 * @param worksheetName
	 * @return
	 * @throws GeneralException 
	 */
	private ZipEntry getWorksheetEntry(ZipFile xlsxFile, String worksheetName ) throws GeneralException {
		
		//  grab the workbook.xml
		Document workbook = getXmlFileFromZipArchive(xlsxFile, XLSX_WORKBOOK_FILENAME);
		Document workbookRelationships = getXmlFileFromZipArchive(xlsxFile, XLSX_WORKBOOK_RELATIONSHIPS_FILENAME);
		
		String worksheetFilename = null;
		
		if (workbook != null && workbookRelationships != null) {
			
			//work out the filename of the worksheet
			
			
	        // We now have the workbook and workbook relationshps XML file in memory, so we can
	        // do a quick XPATH expression to find the sheet name
			//  
			// First lookup the sheet name in the workbook.xml document and find the releationship id
			// This should be in the attribute r:id
	        
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder;
	        
			try {
				
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				
				String xpathExpr = "//*[@name='" + sheetName + "']/@id";
				if (log.isDebugEnabled()) log.debug("XPath expression to lookup resource ID = " + xpathExpr);
				XPathExpression expr = xpath.compile(xpathExpr);

				String relationshipId =  (String) expr.evaluate(workbook, XPathConstants.STRING);
				if (relationshipId == null || relationshipId.length() == 0) 
					throw new GeneralException("Could not resolve releationship ID from the resource name " + sheetName);
				
				if (log.isDebugEnabled()) log.debug("Relationship ID returned is = " + relationshipId);
				
				// We now have the relationship ID, so we now need to look
				// this up in the workbook releationships xml document to
				// find the actual resource filename
				
				xpathExpr = "//*[@Id='" + relationshipId + "']/@Target";
				if (log.isDebugEnabled()) log.debug("XPath expression to lookup resource ID = " + xpathExpr);
				expr = xpath.compile(xpathExpr);

				worksheetFilename =  (String) expr.evaluate(workbookRelationships, XPathConstants.STRING);
				if (worksheetFilename == null || worksheetFilename.length() == 0) 
					throw new GeneralException("Could not resolve worksheetFilName from the releationship '" + relationshipId + " while resolving sheet name '" + sheetName +"'");				

				if (log.isDebugEnabled()) log.debug("Worksheet filename returned is = " + worksheetFilename);
				
			} catch ( XPathExpressionException e) {
				throw new GeneralException("Could not parse workbook XML file", e);
			} 

		} else {
			throw new GeneralException("Could not find Workbook xml document in XLSX file.");
		}
		
		return xlsxFile.getEntry("xl/" + worksheetFilename);
		
	}
}
