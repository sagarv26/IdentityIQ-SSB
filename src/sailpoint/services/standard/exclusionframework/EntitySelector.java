package sailpoint.services.standard.exclusionframework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.object.AbstractCertifiableEntity;
import sailpoint.object.Filter;
import sailpoint.tools.GeneralException;

public class EntitySelector extends FilterSelector {

   private static final long serialVersionUID = 5702297509378218544L;

   private static Log log = LogFactory.getLog(EntitySelector.class);

   String type = "entity";

   public EntitySelector() {
      super();
   }

   public EntitySelector(String filterString) {
      super(Filter.compile(filterString));
   }

   public EntitySelector(Filter filter) {
      super(filter);
   }

   public boolean isMatch(AbstractCertifiableEntity entity) {

      try {

         CertifiableEntityMatcher matcher = new CertifiableEntityMatcher(filter);
         return matcher.matches(entity);

      } catch (GeneralException e) {
         log.error(e);
         e.printStackTrace();
      }

      return false;
   }
}
