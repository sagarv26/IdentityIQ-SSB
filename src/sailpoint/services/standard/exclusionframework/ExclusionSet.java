package sailpoint.services.standard.exclusionframework;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.AbstractCertifiableEntity;
import sailpoint.object.Certifiable;
import sailpoint.object.Custom;
import sailpoint.object.Filter;
import sailpoint.tools.GeneralException;
import sailpoint.tools.xml.AbstractXmlObject;
import sailpoint.tools.xml.SerializationMode;
import sailpoint.tools.xml.XMLClass;
import sailpoint.tools.xml.XMLProperty;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("serial")
@XMLClass
public class ExclusionSet extends AbstractXmlObject {

   @XMLClass
   public enum Mode {

      @SerializedName("include")
      INCLUDE, @SerializedName("exclude")
      EXCLUDE;
   }

   String name;

   String exclusionReason;

   Mode mode;

   List<ItemSelector> itemSelectors;

   List<EntitySelector> entitySelectors;

   private static Log log = LogFactory.getLog(ExclusionSet.class);

   public ExclusionSet() {
      super();
   }

   public ExclusionSet(String name, Mode mode) {
      super();
      this.name = name;
      this.mode = mode;
      this.itemSelectors = new ArrayList<ItemSelector>();
      this.entitySelectors = new ArrayList<EntitySelector>();
   }

   public ExclusionSet(String name, Mode mode,
         List<ItemSelector> itemSelectors, List<EntitySelector> entitySelectors) {
      super();
      this.name = name;
      this.mode = mode;
      this.itemSelectors = itemSelectors;
      this.entitySelectors = entitySelectors;
   }

   @SuppressWarnings("unchecked")
   public ExclusionSet(String exclusionSetCustomName) throws GeneralException {
      super();
      SailPointContext context = SailPointFactory.getCurrentContext();

      Custom exclusionSetCustom = context.getObjectByName(Custom.class,
            exclusionSetCustomName);
      if (null == exclusionSetCustom) {
         throw new GeneralException("Cannot find Custom object "
               + exclusionSetCustomName);
      }

      this.name = (String) exclusionSetCustom.get("name");

      this.mode = Mode.valueOf((String) exclusionSetCustom.get("mode"));

      List<ItemSelector> itemSelectors = new ArrayList<ItemSelector>();
      List<Filter> itemSelectorList = (List<Filter>) exclusionSetCustom
            .get("itemSelectors");
      if (null != itemSelectorList && !(itemSelectorList == null)) {
         for (Filter itemSelectorFilter : itemSelectorList) {
            ItemSelector itemSelector = new ItemSelector(itemSelectorFilter);
            itemSelectors.add(itemSelector);
         }
         this.itemSelectors = itemSelectors;
      }

      List<EntitySelector> entitySelectors = new ArrayList<EntitySelector>();
      List<Filter> entitySelectorList = (List<Filter>) exclusionSetCustom
            .get("entitySelectors");
      if (null != entitySelectorList && !(entitySelectorList == null)) {
         for (Filter entitySelectorFilter : entitySelectorList) {
            EntitySelector entitySelector = new EntitySelector(
                  entitySelectorFilter);
            entitySelectors.add(entitySelector);
         }
         this.entitySelectors = entitySelectors;
      }

      this.exclusionReason = (String) exclusionSetCustom.get("reason");
   }

   @XMLProperty(mode = SerializationMode.ATTRIBUTE)
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @XMLProperty(mode = SerializationMode.ATTRIBUTE)
   public Mode getMode() {
      return mode;
   }

   public void setMode(Mode mode) {
      this.mode = mode;
   }

   @XMLProperty(mode = SerializationMode.ATTRIBUTE, xmlname = "reason")
   public String getExclusionReason() {
      return exclusionReason;
   }

   public void setExclusionReason(String exclusionReason) {
      this.exclusionReason = exclusionReason;
   }

   @XMLProperty(mode = SerializationMode.LIST)
   public List<ItemSelector> getItemSelectors() {
      return itemSelectors;
   }

   public void setItemSelectors(List<ItemSelector> itemSelectors) {
      this.itemSelectors = itemSelectors;
   }

   public void addItemSelector(ItemSelector itemSelector) {
      this.itemSelectors.add(itemSelector);
   }

   public void removeItemSelector(ItemSelector itemSelector) {
      this.itemSelectors.remove(itemSelector);
   }

   @XMLProperty(mode = SerializationMode.LIST)
   public List<EntitySelector> getEntitySelectors() {
      return entitySelectors;
   }

   public void setEntitySelectors(List<EntitySelector> entitySelectors) {
      this.entitySelectors = entitySelectors;
   }

   public void addEntitySelector(EntitySelector entitySelector) {
      this.entitySelectors.add(entitySelector);
   }

   public void removeEntitySelector(EntitySelector entitySelector) {
      this.entitySelectors.remove(entitySelector);
   }

   /*
    * See if a certifiable item matches any of the item exclusion policies.
    * Note: This is null safe to simplify upstream code.
    */
   public boolean matches(Certifiable item) {

      if (item != null) {

         List<ItemSelector> itemSelectors = getItemSelectors();

         if ((itemSelectors != null) && (!itemSelectors.isEmpty())) {

            for (ItemSelector itemSelector : itemSelectors) {

               if (itemSelector.isMatch(item)) {
                  return true;
               }
            }
         }

      }
      return false;
   }

   /*
    * See if an entity matches any of the entity exclusion policies. Note: This
    * is null safe to simplify upstream code.
    */
   public boolean matches(AbstractCertifiableEntity entity) {

      if (entity != null) {

         List<EntitySelector> entitySelectors = getEntitySelectors();

         if ((entitySelectors != null) && (!entitySelectors.isEmpty())) {

            for (EntitySelector entitySelector : entitySelectors) {

               if (entitySelector.isMatch(entity)) {
                  ;
                  return true;
               }
            }
         }

      }
      return false;
   }
}
