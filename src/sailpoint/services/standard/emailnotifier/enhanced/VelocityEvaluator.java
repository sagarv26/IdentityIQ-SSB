package sailpoint.services.standard.emailnotifier.enhanced;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.log4j.Logger;
import sailpoint.tools.GeneralException;
import sailpoint.tools.VelocityUtil;

public class VelocityEvaluator {

	private static Logger log = Logger.getLogger(VelocityEvaluator.class);
	
	public String evaluate(String src, Map<String,Object> args, Locale locale, TimeZone tz) throws GeneralException {
		
		if (log.isDebugEnabled()) log.debug("Evaluating Source: " + src + ", Map: " + args.toString());
		if (log.isDebugEnabled()) {
			for (String key : args.keySet()) {
				log.debug("  " +  key + " == " + args.get(key).toString());
			}
		}
		return VelocityUtil.render(src,  args, locale, tz);
		
	}
	public String evaluate(String src, Map<String,Object> args) throws GeneralException {
		
		return evaluate(src, args, Locale.getDefault(), TimeZone.getDefault());
		
	}
}
