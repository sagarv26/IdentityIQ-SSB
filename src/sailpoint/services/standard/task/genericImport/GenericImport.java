package sailpoint.services.standard.task.genericImport;

import java.util.Iterator;
import java.util.List;

import sailpoint.object.Attributes;
import sailpoint.tools.GeneralException;

/**
 * Generic Import interface, defines the signature for this class, implemented in the
 * Abastact Generic Import class to define default behaviour.
 * 
 * @author christian.cairney
 *
 */
public interface GenericImport {
	
	public static final String IMPORT_MANUAL_HEADER = "importManualHeader";
	
	public Attributes<String,Object> getAttributes();
	
	public void setAttributes(Attributes<String,Object> attributes);
	
	public void open() throws GeneralException;
	
	public void close() throws GeneralException;
	
	public Schema getSchema() throws GeneralException;
	
	public Iterator<List<String>> iterator() throws GeneralException;
}
