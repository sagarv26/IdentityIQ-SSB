package sailpoint.services.standard.task.genericImport;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import sailpoint.tools.GeneralException;

public class Parser {
	
	private static final Logger log = Logger.getLogger(Parser.class);
	private boolean hadQuotes = false;
	
	public enum ParseItemType {
		NOUN, OPERATOR, END
	}
	
	public class ParseItem {

		private String value = null;
		private List<ParseItem> values = null;
		private ParseItemType type = ParseItemType.NOUN;
		
		public ParseItemType getType() {
			return type;
		}

		 void setType(ParseItemType type) {
			this.type = type;
		}

		public String toString() {
			
			if (type.equals(ParseItemType.NOUN)) {
				return "{" + (value != null ? value : "[null]")  +
						(values != null ? "#values=" + values.toString() : "") +"}";
			} else if (type.equals(ParseItemType.OPERATOR)) {
				return "{" + value +"}";
			} else if (type.equals(ParseItemType.END)) {
				return "{#EOR}";
			}else {
				return "{ERROR}";
			}
		}
		
		public String getItem() {
			return value;
		}
		
		public List<ParseItem> getParseItems() {
			return values;
		}
		
	
	}
	public List<ParseItem> parse(String v) throws GeneralException {
		return parse(v, false);
	}
	
	
	private List<ParseItem> parse(String v, boolean subRecord) throws GeneralException {
				
		if (log.isDebugEnabled()) log.debug("Entering parse (" + v + ")");
		String value = v + ",";
		
		List<ParseItem>pvs = new ArrayList<ParseItem>();
		
		int start = 0;
		int bracket = 0;
		
		int startBracket = 0;
		int endBracket = 0;
		boolean quotes = false;
		
		ParseItem pv = new ParseItem();
		
		String attribute= null;
		
		for (int i=0; i < value.length() ; i++) {
			
			char c = value.charAt(i);
			if (log.isTraceEnabled()) log.trace("Char(" + i + "): " + c);
			if (c == '"') {
				quotes = !quotes;
				hadQuotes = true;
			}
			
			if (!quotes) {
				if (c == ')') {
						
					start = i;
					bracket--;
					if (bracket==0) {
						endBracket = i;
						String bracketValue = value.substring(startBracket, endBracket);
						pv.values = parse(bracketValue, true);
					}
				}
				if (c == '(') {
									
					if (bracket==0) {
						
						pv.value = returnValue(value, start, i);
						startBracket = i +1;
					}
					start = i;
					bracket++;
				}
				if (bracket == 0) {
					if (c == '=') {
						
						if (attribute == null) attribute = returnValue(value, start, i);
						if (attribute.length() > 0 ) pv.value = attribute;
						pvs.add(pv);
					
						attribute = null;
						
						pv = new ParseItem();
						pv.value="=";
						pv.setType(ParseItemType.OPERATOR);
						pvs.add(pv);
						
						pv = new ParseItem();
						start = i;
					}
					if (c == ',') {
						
						// This indicates a new record
						if (attribute == null) attribute = returnValue(value, start, i);
						if (attribute.length() > 0 ) pv.value = attribute;
						pvs.add(pv);
						
						attribute = null;
						
						if (!subRecord) {
							log.debug("New record#");
							pv = new ParseItem();
							pv.setType(ParseItemType.END);
							pvs.add(pv);
						}
						pv = new ParseItem();

						start = i;
					}
				}
			}
			
			
		}

		if (bracket != 0) throw new GeneralException("Syntax error, too many brackets");
		
		return pvs;

	}	
	
	/**
	 * 
	 * @param value
	 * @param start
	 * @param end
	 * @return
	 */
	private String returnValue(String value, int start, int end) {
		
		String returnValue = null;
		if (hadQuotes) {
			hadQuotes = false;
			returnValue = value.substring((start == 0 ? start +1: start +2), end -1 ).trim();
		} else {
			returnValue = value.substring((start == 0 ? start : start +1), end).trim();
		}
		if (log.isTraceEnabled()) log.trace("Return value: " + returnValue);
		return returnValue;

	}
	
}