package sailpoint.services.standard.task.genericImport;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.tools.GeneralException;

/**
 * Abstract Generic Import class gives the default methods for
 * import classes to extend.
 * 
 * 19/Aug/2014	1.0	Initial release
 * 
 * @author christian.cairney
 * @version 1.0
 * @since 0.1 (Pre-release)
 *
 */
public abstract class AbstractGenericImport implements GenericImport {


	private SailPointContext context = null;				// The SailPoint context
	private Attributes<String, Object> attributes = null;	// Attributes used to define the import
															// parameters.

	private Schema schema = null;						// The schema for this import
	
	// Our friend the logger.
	private static final Logger log = Logger.getLogger(AbstractGenericImport.class);
	// Common delimiter used as by default
	public static final String STRING_DELIMITER = ";";
	public static final String LOGGER = "logger";
	
	/**
	 * The constructor, this should be called by
	 * all extending classes, as this would be super() ! :)
	 * 
	 * @throws GeneralException
	 */
	public AbstractGenericImport(SailPointContext context) throws GeneralException {
		
		log.debug("Entering AbstractGenericImport(SailPointContext) constructor");
		
		this.context = context;
		init();
	
		log.debug("Exiting AbstractGenericImport(SailPointContext) constructor");
	
	}
	
	public AbstractGenericImport() throws GeneralException {
		
		log.debug("Entering AbstractGenericImport() constructor");
		
		try {
			this.context = sailpoint.api.SailPointFactory.getCurrentContext();
		} catch (GeneralException e) {
			// Cannot get SailPoint's current context.
			throw new GeneralException("Could not get current context in AbstractGenericImport", e);
		}

		init();
		log.debug("Exiting AbstractGenericImport() constructor");
	
	}

	private void init() {
		
		// Create the new attributes map just in case
		attributes = new Attributes<String,Object>();
		schema = new Schema(context);
	}



	
	/**
	 * Attributes hold the specific configuration for each type of
	 * importer.
	 */
	public Attributes<String,Object> getAttributes() {
		return this.attributes;
	}
	/**
	 * @throws GeneralException 
	 * 
	 */
	public void setAttributes(Attributes<String,Object> attributes)  {	
		
		this.attributes = attributes;

	}
	
	public Schema getSchema() {
		return schema;
	}	
		
	/**
	 * Abstract 
	 */
	public abstract void open() throws GeneralException;
	public abstract void close() throws GeneralException;
	public abstract Iterator<List<String>> iterator() throws GeneralException;
	
}
