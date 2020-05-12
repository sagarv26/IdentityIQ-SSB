package sailpoint.services.standard.task.genericImport;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Attributes;
import sailpoint.tools.GeneralException;
import sailpoint.tools.JdbcUtil;
/**
 * JdbcImport used the JdbcUtil.ARGS_ constants for the JDBC connection
 * parameters.
 * 
 * @author christian.cairney
 * @version 1.0
 * @since 0.1
 */
public class JDBCImport extends AbstractGenericImport implements GenericImport {
	
	private static final String IMPORT_JDBC = "jdbc_";
	// The attribute provided by this class
	public static final String IMPORT_JDBC_SQL_QUERY = IMPORT_JDBC + "sqlQuery";

	// Auguments which tally with the  JdbcUtil class
	public static final String IMPORT_JDBC_TYPE = IMPORT_JDBC + JdbcUtil.ARG_TYPE;
	public static final String IMPORT_JDBC_DRIVER_CLASS = IMPORT_JDBC + JdbcUtil.ARG_DRIVER_CLASS;
	public static final String IMPORT_JDBC_DRIVER_PREFIX = IMPORT_JDBC + JdbcUtil.ARG_DRIVER_PREFIX ;
	public static final String IMPORT_JDBC_URL =  IMPORT_JDBC + JdbcUtil.ARG_URL;
	public static final String IMPORT_JDBC_HOST = IMPORT_JDBC + JdbcUtil.ARG_HOST;
	public static final String IMPORT_JDBC_PORT =  IMPORT_JDBC + JdbcUtil.ARG_PORT;
	public static final String IMPORT_JDBC_DATABASE =  IMPORT_JDBC + JdbcUtil.ARG_DATABASE;
	public static final String IMPORT_JDBC_USER =  IMPORT_JDBC + JdbcUtil.ARG_USER;
	public static final String IMPORT_JDBC_PASSWORD = IMPORT_JDBC + JdbcUtil.ARG_PASSWORD;
	public static final String IMPORT_JDBC_SQL  = IMPORT_JDBC + JdbcUtil.ARG_SQL;
	public static final String IMPORT_JDBC_ARG1 = IMPORT_JDBC + JdbcUtil.ARG_ARG1;
	public static final String IMPORT_JDBC_ARG2 =  IMPORT_JDBC + JdbcUtil.ARG_ARG2;
	public static final String IMPORT_JDBC_ARG3 = IMPORT_JDBC + JdbcUtil.ARG_ARG3;
    
	//public static final String IMPORT_JDBC_GROUP_BY = IMPORT_JDBC + "resultSetGroupBy";
	
	private Connection con = null;
	
	// JDBC importer configuration
	private ResultSet rs = null;
	
	// Our friend, the logger
	private static final Logger log = Logger.getLogger(JDBCImport.class);

	/**
	 * Constructor
	 * 
	 * @throws GeneralException
	 */
	public JDBCImport() throws GeneralException {
		super();
	}
	public JDBCImport(SailPointContext context) throws GeneralException {
		super(context);
	}

	/**
	 * Open method for JDBC
	 */
	public void open() throws GeneralException {

		log.debug("Starting JdbcImport.open()");
		
		Schema schema = this.getSchema();
		
		SailPointContext context = SailPointFactory.getCurrentContext();
		
		Attributes<String,Object> attributes = getAttributes();
		
		// Rename the attributes map so they can be used as-is in the
		// JdbcUtil.
		
		Iterator<String> it = attributes.getKeys().iterator();
		while (it.hasNext()) {
			
			String key = it.next();
			
			if (key.startsWith(IMPORT_JDBC)) {
				
				if (!key.equals(IMPORT_JDBC_SQL_QUERY)) {
					
					String newKey = key.substring(IMPORT_JDBC.length());
					Object value = attributes.remove(key);
					attributes.put(newKey, value);
				}
			}
		}
		
		if (attributes.containsKey(JdbcUtil.ARG_PASSWORD)) 
			attributes.put(JdbcUtil.ARG_PASSWORD,  
					context.decrypt((String)attributes.get(JdbcUtil.ARG_PASSWORD)));
		
		con = JdbcUtil.getConnection(attributes);
		Statement stmt;
		try {
			stmt = con.createStatement();
			
			String query = getAttributes().get(IMPORT_JDBC_SQL_QUERY).toString();
			
			if (log.isDebugEnabled()) log.debug("  Issueing QUERY: " + query);
			
			rs = stmt.executeQuery(query);
			
		} catch (SQLException e) {
			throw new GeneralException("Exception when opening new JDBC connection in JdbcImport", e);
		}
		
		// Get the header
		ResultSetMetaData rsmd;
		try {
			rsmd = rs.getMetaData(); 
			List<String> header = new ArrayList<String>();
			for (int i=1; i <= rsmd.getColumnCount(); i++) {
				
				String columnName = rsmd.getColumnName(i);
				String columnLabel = rsmd.getColumnLabel(i);
				
				if (columnLabel != null) {
					header.add( columnLabel);	
				} else {
					header.add( columnName);
				}
			}
			// init the schema with the header information
			schema.init(header);
			
		} catch (SQLException e) {
			throw new GeneralException("Could not get JDBC Meta Data in JdbcImport", e);
		}
		
		log.debug("Exiting JdbcImport.open()");
	}

	@Override
	public void close() throws GeneralException {
		try {
			if (rs != null)	rs.close();
			if (con != null) con.close();
		} catch (SQLException e) {
			throw new GeneralException("Could not close database in JdbcImport", e);
		}
		
	}

	@Override
	public Iterator<List<String>> iterator() {

		Iterator<List<String>> it = null;
		it = new ImportIterator();		

		return it;

	}

	private class ImportIterator implements Iterator<List<String>> {

		private int recordNo = 0;
		private boolean hasNext = false;

		public ImportIterator()  {

			getNextLine();
			

		}

		@Override
		public boolean hasNext() {
			return hasNext;
			
		}

		// get the next line and set the hasNext status
		private void getNextLine() {

			try {
				if (rs.next()) {
					hasNext = true;
					recordNo++;
					if (log.isDebugEnabled())
						log.debug("Read in record number " + String.valueOf(recordNo));

				} else {
					hasNext = false;
				}
			} catch (SQLException e) {
				log.error("Cannot getNextLine in JdbcImport iterator. Returning fale for hasNext.", e);
				hasNext = false;
			}
			
		}

		@Override
		public List<String> next() throws NoSuchElementException {

			List<String> values = null;
			//Schema schema = getSchema();

			if (hasNext) {
				if (log.isDebugEnabled())
					log.debug("Read record number " + String.valueOf(recordNo));

				// Make sure the number of columns read in is the same as the
				// number
				// of columns in the header

				// Determine how many columns to process, may be the total
				// number of
				// tokens columns in the header, or if the parsed columns is
				// less, use
				// that instead.

				//int maxColumns = header.size();

				// Build the values hashmap, map the column numbers
				// with the names in the column header.

				values = new ArrayList<String>();
				
				int maxColumns = 0;
				try {
					maxColumns = rs.getMetaData().getColumnCount();
				} catch (SQLException e1) {
					//  I know, anti pattern but NoSuchElementException will not allow
					// chaining the exception so Ineed to put it somewhere to help debugging etc.
					log.error("Could not get row column count", e1);
					throw new NoSuchElementException("Could not get row column count: " + e1.getMessage());
				}
				
				for (int c=1; c <= maxColumns; c++) {

					// The value is of type object, not fixed to
					// string as in the text delimited import
					String value = null;
					try {
						value = rs.getString(c);
					} catch (SQLException e) {
						// TODO: Ok, so we're going to ignore this exception for now, may work around
						// this later and throw the exception back to the callee as a general 
						// exception.
						log.error("next() returned an error when retrieving column number " + String.valueOf(c), e);
					} 

					if (log.isDebugEnabled()) {
						if (log.isDebugEnabled())
							log.debug("Adding to Values Array[" + String.valueOf(c) + "]=" + value);
						
					}
					
					values.add(value);
				}

			} else {
				throw new NoSuchElementException("Does not have next");
			}

			getNextLine();
			return values;
		}

		@Override
		public void remove() {
			// We don't support removes, no point on a read only CSV file?
			throw new UnsupportedOperationException();
		}
	}
}
