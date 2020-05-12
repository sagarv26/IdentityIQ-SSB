package sailpoint.services.standard.exclusionframework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.object.Filter;
import sailpoint.tools.xml.AbstractXmlObject;
import sailpoint.tools.xml.SerializationMode;
import sailpoint.tools.xml.XMLClass;
import sailpoint.tools.xml.XMLProperty;

@XMLClass
public class FilterSelector extends AbstractXmlObject {
		
	private static final long serialVersionUID = 6883030468231446610L;
	
	private static Log log = LogFactory.getLog(FilterSelector.class);
	
	Filter filter;
	
	String type;

	public FilterSelector() {
		super();
		this.filter = null;
		this.type = "generic";	
	}
	
	public FilterSelector( Filter filter ) {
		super();
		this.filter = filter;
		this.type = "generic";
	}

	@XMLProperty(mode=SerializationMode.UNQUALIFIED)
	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	@XMLProperty(mode=SerializationMode.ELEMENT)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}