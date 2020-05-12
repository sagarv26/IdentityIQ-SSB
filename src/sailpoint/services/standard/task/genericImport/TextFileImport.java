package sailpoint.services.standard.task.genericImport;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import sailpoint.api.SailPointContext;
import sailpoint.tools.GeneralException;
import sailpoint.tools.RFC4180LineIterator;
import sailpoint.tools.RFC4180LineParser;

/**
 * 
 * @author christian.cairney
 * 
 * Text file import for the generic import class.
 * 
 * Attribute:  importFileName = File name of the import file
 *
 */
public class TextFileImport extends AbstractGenericImport implements GenericImport {

	public static final String IMPORT_TEXT_FILE = "text_";
	
	public static final String IMPORT_TEXT_FILE_NAME = IMPORT_TEXT_FILE + "fileName";
	public static final String IMPORT_TEXT_FILE_DELIMITER = IMPORT_TEXT_FILE + "delimiter";
	public static final String IMPORT_TEXT_FILE_HAS_HEADER = IMPORT_TEXT_FILE + "hasHeader";
	public static final String IMPORT_TEXT_FILE_REMARK_TOKEN = IMPORT_TEXT_FILE + "remarkToken";
	public static final String IMPORT_TEXT_FILE_ENCODING = IMPORT_TEXT_FILE + "fileEncoding";
	
	// Text importer configuration
	private boolean hasHeader = false;
	private String fileDelimiter = ",";
	private String filename = null;
	private String remarkToken = "#!";
	private String encoding = null;

	//
	private InputStream stream;
	private RFC4180LineIterator lines = null;
	private RFC4180LineParser parser = null;

	private static final Logger log = Logger.getLogger(TextFileImport.class);

	public TextFileImport() throws GeneralException {
		super();
	}

	public TextFileImport(SailPointContext context) throws GeneralException {
		super(context);
	}

	@Override
	public void open() throws GeneralException {

	
		if (getAttributes().containsKey(IMPORT_TEXT_FILE_NAME)) {
			filename = getAttributes().getString(IMPORT_TEXT_FILE_NAME);
		} else {
			throw new GeneralException(
					"Cannot open file stream as not filename exists in key:"
							+ IMPORT_TEXT_FILE_NAME);
		}

		// If there are no values in the attributes map, then allow for the
		// defaults set in the class
		if (getAttributes().containsKey(IMPORT_TEXT_FILE_DELIMITER)) {
			fileDelimiter = getAttributes().getString(
					IMPORT_TEXT_FILE_DELIMITER);
		}

		if (getAttributes().containsKey(IMPORT_TEXT_FILE_HAS_HEADER)) {
			hasHeader = getAttributes().getBoolean(IMPORT_TEXT_FILE_HAS_HEADER);
		}

		if (getAttributes().containsKey(IMPORT_TEXT_FILE_REMARK_TOKEN)) {
			remarkToken = getAttributes().getString(
					IMPORT_TEXT_FILE_REMARK_TOKEN);
		}

		if (getAttributes().containsKey(IMPORT_TEXT_FILE_ENCODING)) {
			encoding = getAttributes().getString(IMPORT_TEXT_FILE_ENCODING);
		}
		
		// Init the stream
		parser = new RFC4180LineParser(fileDelimiter);
		lines = null;
		stream = null;

		// Get the input stream

		try {
			if (log.isDebugEnabled())
				log.debug("Opening filename: " + filename);
			
			File file = new File(filename);
			stream = new BufferedInputStream(new FileInputStream(file));
			
			if (log.isDebugEnabled())
				log.debug("Successfully opened the file.");

		} catch (Exception e) {
			throw new GeneralException("Could not open filename '" + filename
					+ "' in TextFileImport.open()", e);
		}

		if (encoding == null) {
			lines = new RFC4180LineIterator(new BufferedReader(
				new InputStreamReader(stream)));
		} else {
			try {
				lines = new RFC4180LineIterator(new BufferedReader(
					new InputStreamReader(stream, encoding)));
			} catch (UnsupportedEncodingException e) {
				throw new GeneralException("Could not open stream due to illegal encoding setting of " + encoding, e);
			}
		}

		// Do we need to read the header?
		if (hasHeader) {

			// Look for the first usable line as the header
			
			
		}

	}

	@Override
	public void close() throws GeneralException {
		if (stream != null) {
			if (log.isDebugEnabled())
				log.debug("Closing file stream.");
			try {
				stream.close();
			} catch (IOException e) {
				throw new GeneralException("Could not close the file stream", e);

			}
		} else {
			if (log.isDebugEnabled())
				log.debug("Cannot close file stream as it is null.");
		}
	}

	@Override
	public Iterator<List<String>> iterator() throws GeneralException {

		ImportIterator it = null;
		it = new ImportIterator();
		
		return it;

	}


	private class ImportIterator implements Iterator<List<String>> {

		private int lineNo = 0;
		private boolean hasNext = false;
		private String line = null;

		public ImportIterator() throws GeneralException  {

			getNextLine();
			List<String> row = parseLine(line);
			Schema schema = getSchema();
			
			if ( hasHeader) {
				log.debug("Has header is detected, grabbing header columns names from row");
				
				schema.init(row);
				// We don't need to send back the header line to the client
				// so get the next one;
				getNextLine();
				
			} else {
				schema.init(row.size());
			}
			
		}

		@Override
		public boolean hasNext() {

			return hasNext;
		}
		
		List<String> parseLine(String parseLine) {

			List<String> values = null;

			Schema schema = getSchema();
			int maxColumns = schema.getSchemaColumnSize();
			
			if (parseLine != null) {
				
				// Check to make sure we have some content, if we don't then
				// don't
				// process the line.
				ArrayList<String> tokens;
				try {
					tokens = parser.parseLine(parseLine);
				} catch (GeneralException e) {

					// Urgh!
					//
					// I only get a GeneralException here.. no idea what
					// really got thrown... ah well..
					// I have to raise an error here, so it'll be
					// NoSuchElementException, bit
					// of a red herring though... ho hum.
					//
					// TODO: Look at the source and see what the parseLine
					// exception really is.
					//

					throw new NoSuchElementException(
							"Got an error in iterator.parseLine() call when parsing the line into tokens.  General Exception reported: "
									+ e.getMessage());

				}

				// Make sure the number of columns read in is the same as the
				// number
				// of columns in the header

				// Determine how many columns to process, may be the total
				// number of
				// tokens columns in the header, or if the parsed columns is
				// less, use
				// that instead.

				if (maxColumns == 0)
					maxColumns = tokens.size();
				if (tokens.size() < maxColumns)
					maxColumns = tokens.size();

				// Build the values hashmap, map the column numbers
				// with the names in the column header.

				values = new ArrayList<String>();
				for (int c = 0; c < maxColumns; c++) {

					String value = tokens.get(c);
					
					if (log.isDebugEnabled())
						log.debug("Adding to Values Array[" + String.valueOf(c) + "]=" + value);

					values.add( value);
				}

			} else {
				throw new NoSuchElementException("Does not have next");
			}

			return values;
		
		}
		
		// get the next line and set the hasNext status
		private void getNextLine() {
				
			try {
				line = lines.readLine();

				lineNo++;
				
				if (log.isDebugEnabled())
					log.debug("Read in line number " + String.valueOf(lineNo)
							+ ": " + line);

				// If it was a 0 length line, or before the header line then
				// loop and get the next record.

				// Fixed for SSDBUGS-20, was ignoring the remark token, fixed here.
				while (line != null	&& (line.length() == 0 || line.startsWith(remarkToken))) {

					if (log.isDebugEnabled())
						log.debug("Ignored line number: "
								+ String.valueOf(lineNo) + ", " + line);
					line = lines.readLine();
					lineNo++;
				}
				
			} catch (IOException e) {
				if (log.isDebugEnabled()) log.debug("Got an IO Error, so returning no line");
				line = null;
			}

			if (line == null) {
				if (log.isDebugEnabled())
					log.debug("hasNext = false");
				hasNext = false;
			} else {
				if (log.isDebugEnabled())
					log.debug("hasNext = true");
				hasNext = true;
 			}
			
		}

		@Override
		public List<String> next() throws NoSuchElementException {

			List values = parseLine(line);
			getNextLine();
			
			if (log.isDebugEnabled()) log.debug("Next() returns:" + values.toString());
			return values;
		}

		@Override
		public void remove() {
			// We don't support removes, no point on a read only CSV file?
			throw new UnsupportedOperationException();
		}
		
		/**
		 * We should always close the stream explicitly
		 */
		public void close() {
			lines.close();
			try {
				stream.close();
			} catch (IOException e) {
				log.warn("Could not close stream", e);
			}
		}

	}

}
