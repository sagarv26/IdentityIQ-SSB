package sailpoint.services.standard.exclusionframework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.api.certification.DataOwnerCertifiableEntity;
import sailpoint.api.certification.DataOwnerCertifiableEntity.DataItem;
import sailpoint.object.AbstractCertifiableEntity;
import sailpoint.object.Application;
import sailpoint.object.Bundle;
import sailpoint.object.Capability;
import sailpoint.object.Filter;
import sailpoint.object.Filter.LeafFilter;
import sailpoint.object.Identity;
import sailpoint.object.ManagedAttribute;
import sailpoint.object.QueryOptions;
import sailpoint.search.JavaMatcher;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

public class CertifiableEntityMatcher extends JavaMatcher {

   private static Log log = LogFactory.getLog(CertifiableEntityMatcher.class);

   public CertifiableEntityMatcher(Filter filter) {
      super(filter);
   }

   @Override
   public Object getPropertyValue(LeafFilter leaf, Object o)
         throws GeneralException {

      Object val = null;
      if (o == null)
         return val;

      if (!(o instanceof AbstractCertifiableEntity)) {
         throw new GeneralException("Expected an AbstractCertifiableEntity: "
               + o);
      }

      if (o instanceof Identity) {
         if (log.isTraceEnabled())
            log.trace("Found entity of Type Identity");
         IdentityMatcher identityMatcher = new IdentityMatcher(this.filter);
         val = identityMatcher.getPropertyValue(leaf, (Identity) o);
      } else if (o instanceof Bundle) {
         if (log.isTraceEnabled())
            log.trace("Found entity of Type Bundle");
         BundleMatcher bundleMatcher = new BundleMatcher(this.filter);
         val = bundleMatcher.getPropertyValue(leaf, (Bundle) o);
      } else if (o instanceof DataOwnerCertifiableEntity) {
         if (log.isTraceEnabled())
            log.trace("Found entity of Type DataOwnerCertifiableEntity");
         ManagedAttributeMatcher maMatcher = new ManagedAttributeMatcher(
               this.filter);
         val = maMatcher.getPropertyValue(leaf, (DataOwnerCertifiableEntity) o);
      } else if (o instanceof ManagedAttribute) {
         if (log.isTraceEnabled())
            log.trace("Found entity of Type ManagedAttribute");
         ManagedAttributeMatcher maMatcher = new ManagedAttributeMatcher(
               this.filter);
         val = maMatcher.getPropertyValue(leaf, (ManagedAttribute) o);
      } else {
         throw new GeneralException("Unsupported class of entity: " + o);
      }
      // Always return a String
      if (null != val) {
         if ((val instanceof Integer) || val instanceof Boolean) {
            val = val.toString();
         } else if (val instanceof Identity) {
            val = ((Identity) val).getName();
         } else if (val instanceof Date) {
            val = Long.toString(((Date) val).getTime());
         }
      }
      return val;
   }

   public class IdentityMatcher extends JavaMatcher {

      public IdentityMatcher(Filter filter) {
         super(filter);
      }

      @Override
      public Object getPropertyValue(LeafFilter leaf, Object o)
            throws GeneralException {

         Object val = null;
         if (o == null)
            return val;

         Identity identity = null;
         if (!(o instanceof Identity)) {
            throw new GeneralException("Expected an Identity: " + o);
         }
         identity = (Identity) o;

         String property = leaf.getProperty();

         if (property != null) {
            if (property.startsWith("manager.")) {
               property = property.substring(property.indexOf(".") + 1);
               identity = identity.getManager();
            }
            if (null != identity) {
               if (("name".compareTo(property) == 0)) {
                  val = identity.getName();
               } else if (("displayName".compareTo(property) == 0)) {
                  val = identity.getDisplayName();
               } else if (("firstname".compareTo(property) == 0)) {
                  val = identity.getFirstname();
               } else if (("lastname".compareTo(property) == 0)) {
                  val = identity.getLastname();
               } else if (("email".compareTo(property) == 0)) {
                  val = identity.getEmail();
               } else if (("inactive".compareTo(property) == 0)) {
                  val = identity.isInactive();
               } else if (("manager".compareTo(property) == 0)) {
                  val = identity.getManager();
               } else if (("assignedRoles".compareTo(property) == 0)) {
                  List<Bundle> assignedRoles = identity.getAssignedRoles();
                  List<String> roleNames = new ArrayList<String>();
                  if (null != assignedRoles && !assignedRoles.isEmpty()) {
                     for (Bundle assignedRole : assignedRoles) {
                        roleNames.add(assignedRole.getName());
                     }
                  }
                  val = Util.listToCsv(roleNames);
               } else if (("detectedRoles".compareTo(property) == 0)) {
                  List<Bundle> detectedRoles = identity.getDetectedRoles();
                  List<String> roleNames = new ArrayList<String>();
                  if (null != detectedRoles && !detectedRoles.isEmpty()) {
                     for (Bundle detectedRole : detectedRoles) {
                        roleNames.add(detectedRole.getName());
                     }
                  }
                  val = Util.listToCsv(roleNames);
               } else if (("capabilities".compareTo(property) == 0)) {
                  List<Capability> capabilities = identity.getCapabilities();
                  List<String> capabilityNames = new ArrayList<String>();
                  if (null != capabilities && !capabilities.isEmpty()) {
                     for (Capability capability : capabilities) {
                        capabilityNames.add(capability.getName());
                     }
                  }
                  val = Util.listToCsv(capabilityNames);
               } else if (("class".compareTo(property) == 0)) {
                  val = "Identity";
               } else {
                  val = (identity.getAttributes() != null) ? identity
                        .getAttributes().get(property) : val;
               }
            }
         }
         return val;
      }
   }

   public class ManagedAttributeMatcher extends JavaMatcher {

      public ManagedAttributeMatcher(Filter filter) {
         super(filter);
      }

      @Override
      public Object getPropertyValue(LeafFilter leaf, Object o)
            throws GeneralException {

         Object val = null;
         if (o == null)
            return val;

         // If it's a ManagedAttribute we're doing an Account Group
         // certification, otherwise it's an owner certification.
         if (!(o instanceof DataOwnerCertifiableEntity)
               && !(o instanceof ManagedAttribute)) {
            throw new GeneralException(
                  "Expected a DataOwnerCertifiableEntity or ManagedAttribute: "
                        + o);
         }
         DataItem dataItem = null;
         if (o instanceof DataOwnerCertifiableEntity) {
            dataItem = ((DataOwnerCertifiableEntity) o).getDataItem();
         }

         String property = leaf.getProperty();
         if (log.isTraceEnabled())
            log.trace("Getting val for property: " + property);
         if (property != null) {
            // If it's a MangedAttribute pre-append the "ma."
            if ((o instanceof ManagedAttribute)) {
               if (!property.startsWith("ma.")) {
                  property = "ma." + property;
               }
            }
            if (property.startsWith("ma.")) {
               // If we want to get values directly from the ManagedAttribute,
               // we should specify ma.<attribute>
               property = property.substring(3);
               if (log.isTraceEnabled())
                  log.trace("Property starts with MA get attribute and getting val for property: "
                        + property);
               Object obj = null;
               if (o instanceof DataOwnerCertifiableEntity) {
                  EntityToObjectMatcher eToObjectMatcher = new EntityToObjectMatcher();
                  obj = eToObjectMatcher.getObjectFromEntity(o);
               } else {
                  obj = o;
               }
               if (obj == null) {
                  log.error("Couldn't find ManagedAttribute for: " + o);
                  return null;
               }
               ManagedAttribute ma = null;
               if (obj instanceof ManagedAttribute) {
                  ma = (ManagedAttribute) obj;
               }

               if (("value".compareTo(property) == 0)) {
                  val = ma.getValue();
               } else if (("displayName".compareTo(property) == 0)) {
                  val = ma.getDisplayName();
                  if (val == null) {
                     val = ma.getDisplayableName();
                  }
               } else if (("attribute".compareTo(property) == 0)) {
                  val = ma.getAttribute();
               } else if (("application".compareTo(property) == 0)) {
                  val = ma.getApplication().getName();
               } else if (("type".compareTo(property) == 0)) {
                  val = ma.getType().toString();
               } else if (("owner".compareTo(property) == 0)) {
                  val = ma.getOwner().getName();
               } else if (("owner.name".compareTo(property) == 0)) {
                  val = ma.getOwner().getName();
               } else if (("owner.id".compareTo(property) == 0)) {
                  val = ma.getOwner().getId();
               } else if (("class".compareTo(property) == 0)) {
                  val = ma.getClass().getSimpleName();
               } else {
                  val = (ma.getAttributes() != null) ? ma.getAttributes().get(
                        property) : val;
               }
            } else if (o instanceof DataOwnerCertifiableEntity) {
               if (("application".compareTo(property) == 0)) {
                  val = dataItem.getApplicationName();
               } else if (("value".compareTo(property) == 0)) {
                  val = dataItem.getValue();
               } else if (("name".compareTo(property) == 0)) {
                  val = dataItem.getName();
               } else if (("type".compareTo(property) == 0)) {
                  val = dataItem.getType().toString();
               }
            }
         }
         if (log.isTraceEnabled())
            log.trace("Returning " + val + " for entity " + o
                  + " and leafFilter " + leaf.getExpression(true));
         return val;
      }
   }

   public class BundleMatcher extends JavaMatcher {

      public BundleMatcher(Filter filter) {
         super(filter);
      }

      @Override
      public Object getPropertyValue(LeafFilter leaf, Object o)
            throws GeneralException {

         Object val = null;
         if (o == null)
            return val;

         Bundle bundle = null;
         if (!(o instanceof Bundle)) {
            throw new GeneralException("Expected a Bundle: " + o);
         }
         bundle = (Bundle) o;

         String property = leaf.getProperty();

         if (property != null) {
            if (("name".compareTo(property) == 0)) {
               val = bundle.getName();
            } else if (("displayName".compareTo(property) == 0)) {
               val = bundle.getDisplayName();
               if (val == null) {
                  bundle.getDisplayableName();
               }
            } else if (("type".compareTo(property) == 0)) {
               val = bundle.getType();
            } else if (("riskScoreWeight".compareTo(property) == 0)) {
               val = bundle.getRiskScoreWeight();
            } else if (("applications".compareTo(property) == 0)) {
               List<Application> applications = new ArrayList<Application>(
                     bundle.getApplications());
               List<String> applicationNames = new ArrayList<String>();
               if (null != applications && !applications.isEmpty()) {
                  for (Application application : applications) {
                     applicationNames.add(application.getName());
                  }
               }
               val = Util.listToCsv(applicationNames);
            } else if (("requirements".compareTo(property) == 0)) {
               List<Bundle> requiredRoles = bundle.getRequirements();
               List<String> roleNames = new ArrayList<String>();
               if (null != requiredRoles && !requiredRoles.isEmpty()) {
                  for (Bundle requiredRole : requiredRoles) {
                     roleNames.add(requiredRole.getName());
                  }
               }
               val = Util.listToCsv(roleNames);
            } else if (("permits".compareTo(property) == 0)) {
               List<Bundle> permittedRoles = bundle.getPermits();
               List<String> roleNames = new ArrayList<String>();
               if (null != permittedRoles && !permittedRoles.isEmpty()) {
                  for (Bundle permittedRole : permittedRoles) {
                     roleNames.add(permittedRole.getName());
                  }
               }
               val = Util.listToCsv(roleNames);
            } else if (("roles".compareTo(property) == 0)) {
               List<Bundle> roles = bundle.getRoles();
               List<String> roleNames = new ArrayList<String>();
               if (null != roles && !roles.isEmpty()) {
                  for (Bundle role : roles) {
                     roleNames.add(role.getName());
                  }
               }
               val = Util.listToCsv(roleNames);
            } else if (("owner".compareTo(property) == 0)) {
               Identity owner = bundle.getOwner();
               if (owner != null) {
                  val = owner.getName();
               }
            } else if (("owner.name".compareTo(property) == 0)) {
               Identity owner = bundle.getOwner();
               if (owner != null) {
                  val = owner.getName();
               }
            } else if (("owner.id".compareTo(property) == 0)) {
               Identity owner = bundle.getOwner();
               if (owner != null) {
                  val = owner.getId();
               }
            } else if (("class".compareTo(property) == 0)) {
               val = "Bundle";
            } else {
               val = (bundle.getAttributes() != null) ? bundle.getAttributes()
                     .get(property) : val;
            }
         }
         return val;
      }
   }

   public class EntityToObjectMatcher {

      private SailPointContext _context;

      public EntityToObjectMatcher() {
         super();
         try {
            _context = SailPointFactory.getCurrentContext();
            if (_context == null) {
               _context = SailPointFactory.createContext();
            }
         } catch (Exception e) {
            log.error(e);
         }
      }

      public EntityToObjectMatcher(SailPointContext context) {
         super();
         _context = context;
      }

      public Object getObjectFromEntity(Object o) throws GeneralException {
         if (!(o instanceof AbstractCertifiableEntity)) {
            throw new GeneralException(
                  "Expected an AbstractCertifiableEntity: " + o);
         }
         Object retVal = null;

         if (o instanceof Identity) {
            retVal = (Identity) o;
         } else if (o instanceof Bundle) {
            retVal = (Bundle) o;
         } else if (o instanceof DataOwnerCertifiableEntity) {
            // If we want to get values directly from the ManagedAttribute, we
            // should specify ma.<attribute>
            DataItem dataItem = ((DataOwnerCertifiableEntity) o).getDataItem();
            String maAppName = dataItem.getApplicationName();
            String maValue = dataItem.getValue();
            String maAttr = dataItem.getName();
            String maType = dataItem.getType().toString();
            QueryOptions options = new QueryOptions();
            Filter filter = Filter.ignoreCase(Filter.eq("attribute", maAttr));
            filter = Filter.and(filter,
                  Filter.ignoreCase(Filter.eq("type", maType)));
            filter = Filter.and(filter,
                  Filter.ignoreCase(Filter.eq("application.name", maAppName)));
            filter = Filter.and(filter,
                  Filter.ignoreCase(Filter.eq("value", maValue)));
            options.addFilter(filter);
            List<ManagedAttribute> maMatches;
            try {
               if (log.isTraceEnabled())
                  log.trace("Searching for MA: " + maAttr + ", " + maAppName
                        + ", " + maValue);
               maMatches = _context.getObjects(ManagedAttribute.class, options);
               if (log.isTraceEnabled())
                  log.trace("Found: " + maMatches);
               if (maMatches == null || maMatches.isEmpty()) {
                  throw new GeneralException(
                        "Found no matches for DataOwnerCertifiableEntity: " + o);
               } else if (maMatches.size() > 1) {
                  throw new GeneralException(
                        "Found multiple matches for DataOwnerCertifiableEntity: "
                              + o);
               }
               retVal = maMatches.get(0);
            } catch (GeneralException e) {
               log.error(e);
               throw e;
            }
         }
         if (log.isTraceEnabled())
            log.trace("Returning " + retVal + " for " + o);
         return retVal;
      }
   }
}
