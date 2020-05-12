package sailpoint.services.standard.exclusionframework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.object.Certifiable;
import sailpoint.object.Filter;
import sailpoint.tools.GeneralException;
import sailpoint.tools.xml.XMLClass;

@XMLClass
public class ItemSelector extends FilterSelector {

   private static final long serialVersionUID = 4203893344011136653L;

   private static Log log = LogFactory.getLog(ItemSelector.class);

   String type = "item";

   public ItemSelector() {
      super();
   }

   public ItemSelector(String filterString) {
      super(Filter.compile(filterString));
   }

   public ItemSelector(Filter filter) {
      super(filter);
   }

   public boolean isMatch(Certifiable item) {

      try {

         if (log.isTraceEnabled())
            log.trace("Trying item[" + item + "] with filter [" + filter + "]");
         CertifiableItemMatcher matcher = new CertifiableItemMatcher(filter);

         return matcher.matches(item);

      } catch (GeneralException e) {
         log.error(e);
         e.printStackTrace();
      }

      return false;
   }
}
