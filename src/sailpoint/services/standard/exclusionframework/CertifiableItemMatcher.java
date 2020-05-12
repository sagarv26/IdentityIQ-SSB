package sailpoint.services.standard.exclusionframework;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.api.certification.AccountGroupMembershipCertificationBuilder.MembershipCertifiable;
import sailpoint.api.certification.BusinessRoleCompositionCertificationBuilder.RoleCertifiable;
import sailpoint.api.certification.DataOwnerCertificationBuilder.DataOwnerCertifiable;
import sailpoint.object.Application;
import sailpoint.object.Attributes;
import sailpoint.object.Bundle;
import sailpoint.object.Capability;
import sailpoint.object.Certifiable;
import sailpoint.object.EntitlementGroup;
import sailpoint.object.Entitlements;
import sailpoint.object.Filter;
import sailpoint.object.Filter.LeafFilter;
import sailpoint.object.Identity;
import sailpoint.object.ManagedAttribute;
import sailpoint.object.PolicyViolation;
import sailpoint.object.Profile;
import sailpoint.object.QueryOptions;
import sailpoint.search.JavaMatcher;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

public class CertifiableItemMatcher extends JavaMatcher {

   private static Log log = LogFactory.getLog(CertifiableItemMatcher.class);

   public CertifiableItemMatcher(Filter filter) {
      super(filter);
   }

   @Override
   public Object getPropertyValue(LeafFilter leaf, Object o)
         throws GeneralException {

      Object val = null;
      if (o == null)
         return val;

      if (!(o instanceof Certifiable)) {
         throw new GeneralException("Expected a CertificationItem: " + o);
      }

      if (o instanceof RoleCertifiable) {
         if (log.isTraceEnabled())
            log.trace("Found item of Type RoleCertifiable; getting Object");
         o = ((RoleCertifiable) o).getObject();

      }
      if (o instanceof MembershipCertifiable) {
         if (log.isTraceEnabled())
            log.trace("Found item of Type MembershipCertifiable; getting EntitlementGroup");
         o = ((MembershipCertifiable) o).getEntitlementGroup();
      }

      if (o instanceof Bundle) {
         if (log.isTraceEnabled())
            log.trace("Found item of Type Bundle");
         BundleMatcher bundleMatcher = new BundleMatcher(this.filter);
         val = bundleMatcher.getPropertyValue(leaf, (Bundle) o);
      } else if (o instanceof EntitlementGroup) {
         if (log.isTraceEnabled())
            log.trace("Found item of Type EntitlementGroup");
         EntitlementMatcher entMatcher = new EntitlementMatcher(this.filter);
         val = entMatcher.getPropertyValue(leaf, (EntitlementGroup) o);
      } else if (o instanceof DataOwnerCertifiable) {
         if (log.isTraceEnabled())
            log.trace("Found item of Type DataOwnerCertifiable");
         DataOwnerCertifiableMatcher entMatcher = new DataOwnerCertifiableMatcher(
               this.filter);
         val = entMatcher.getPropertyValue(leaf, (DataOwnerCertifiable) o);
      } else if (o instanceof PolicyViolation) {
         if (log.isTraceEnabled())
            log.trace("Found item of Type PolicyViolation");
         PolicyViolationMatcher violationMatcher = new PolicyViolationMatcher(
               this.filter);
         val = violationMatcher.getPropertyValue(leaf, (PolicyViolation) o);
      } else if (o instanceof Profile) {
         if (log.isTraceEnabled())
            log.trace("Found item of Type Profile");
         ProfileMatcher profileMatcher = new ProfileMatcher(this.filter);
         val = profileMatcher.getPropertyValue(leaf, (Profile) o);
      } else {
         throw new GeneralException("Unsupported class of item: " + o);
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

   public class EntitlementMatcher extends JavaMatcher {

      public EntitlementMatcher(Filter filter) {
         super(filter);
      }

      @Override
      public Object getPropertyValue(LeafFilter leaf, Object o)
            throws GeneralException {

         Object val = null;
         if (o == null)
            return val;

         EntitlementGroup ent = null;
         if (!(o instanceof EntitlementGroup)) {
            throw new GeneralException("Expected an EntitlementGroup: " + o);
         }
         ent = (EntitlementGroup) o;

         String property = leaf.getProperty();

         if (property != null) {
            if (property.startsWith("ma.") && ent.getAttributes() != null) {
               // If we want to get values directly from the ManagedAttribute,
               // we should specify ma.<attribute>
               property = property.substring(3);
               ItemToObjectMatcher itemToObjectMatcher = new ItemToObjectMatcher();
               Object obj = itemToObjectMatcher.getObjectFromItem(o);
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
               } else if (("application.name".compareTo(property) == 0)) {
                  val = ma.getApplication().getName();
               } else if (("application.id".compareTo(property) == 0)) {
                  val = ma.getApplicationId();
               } else if (("type".compareTo(property) == 0)) {
                  val = ma.getType().toString();
               } else if (("owner".compareTo(property) == 0)) {
                  val = ma.getOwner().getName();
               } else if (("class".compareTo(property) == 0)) {
                  val = ma.getClass().getSimpleName();
               } else {
                  val = (ma.getAttributes() != null) ? ma.getAttributes().get(
                        property) : val;
               }
            } else if (("nativeIdentity".compareTo(property) == 0)) {
               val = ent.getNativeIdentity();
            } else if (("displayName".compareTo(property) == 0)) {
               val = ent.getDisplayName();
            } else if (("application".compareTo(property) == 0)) {
               val = ent.getApplicationName();
            } else if (("instance".compareTo(property) == 0)) {
               val = ent.getInstance();
            } else if (("description".compareTo(property) == 0)) {
               val = ent.getDescription();
            } else if (("class".compareTo(property) == 0)) {
               val = ent.getClass().getSimpleName();
            } else {
               val = (ent.getAttributes() != null) ? ent.getAttributes().get(
                     property) : val;
            }
         }
         return val;
      }
   }

   public class DataOwnerCertifiableMatcher extends JavaMatcher {

      public DataOwnerCertifiableMatcher(Filter filter) {
         super(filter);
      }

      @Override
      public Object getPropertyValue(LeafFilter leaf, Object o)
            throws GeneralException {

         Object val = null;
         if (o == null)
            return val;

         DataOwnerCertifiable certifiable = null;
         if (!(o instanceof DataOwnerCertifiable)) {
            throw new GeneralException("Expected an DataOwnerCertifiable: " + o);
         }
         certifiable = (DataOwnerCertifiable) o;
         Entitlements ent = certifiable.getEntitlements();

         Identity identity = certifiable.getIdentity();
         String property = leaf.getProperty();

         if (property != null) {
            if (property.startsWith("identity.")) {
               // If we want to get values directly from the Identity, we should
               // specify identity.<attribute>
               property = property.substring(property.indexOf(".") + 1);
               // If we want to get values from the identity's manager, we
               // should specify identity.manager.<attribute>
               if (property.startsWith("manager.")) {
                  property = property.substring(property.indexOf(".") + 1);
                  identity = identity.getManager();
               }
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
                  val = identity.getClass().getSimpleName();
               } else {
                  if (identity.getAttributes() != null) {
                     val = identity.getAttributes().get(property);
                     if (val != null) {
                        val = val.toString();
                     }
                  }
               }
            } else if (("nativeIdentity".compareTo(property) == 0)) {
               val = ent.getNativeIdentity();
            } else if (("displayName".compareTo(property) == 0)) {
               val = ent.getDisplayName();
            } else if (("application".compareTo(property) == 0)) {
               val = ent.getApplicationName();
            } else if (("instance".compareTo(property) == 0)) {
               val = ent.getInstance();
            } else if (("class".compareTo(property) == 0)) {
               val = ent.getClass().getSimpleName();
            } else {
               val = (ent.getAttributes() != null) ? ent.getAttributes().get(
                     property) : val;
            }
         }
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

         Bundle role = null;
         if (!(o instanceof Bundle)) {
            throw new GeneralException("Expected a Bundle: " + o);
         }
         role = (Bundle) o;

         String property = leaf.getProperty();

         if (property != null) {
            if (("name".compareTo(property) == 0)) {
               val = role.getName();
            } else if (("id".compareTo(property) == 0)) {
               val = role.getId();
            } else if (("type".compareTo(property) == 0)) {
               val = role.getType();
            } else if (("riskScoreWeight".compareTo(property) == 0)) {
               val = role.getRiskScoreWeight();
            } else if (("applications".compareTo(property) == 0)) {
               List<Application> applications = new ArrayList<Application>(
                     role.getApplications());
               List<String> applicationNames = new ArrayList<String>();
               if (null != applications && !applications.isEmpty()) {
                  for (Application application : applications) {
                     applicationNames.add(application.getName());
                  }
               }
               val = Util.listToCsv(applicationNames);
            } else if (("requirements".compareTo(property) == 0)) {
               List<Bundle> requiredRoles = role.getRequirements();
               List<String> roleNames = new ArrayList<String>();
               if (null != requiredRoles && !requiredRoles.isEmpty()) {
                  for (Bundle requiredRole : requiredRoles) {
                     roleNames.add(requiredRole.getName());
                  }
               }
               val = Util.listToCsv(roleNames);
            } else if (("permits".compareTo(property) == 0)) {
               List<Bundle> permittedRoles = role.getPermits();
               List<String> roleNames = new ArrayList<String>();
               if (null != permittedRoles && !permittedRoles.isEmpty()) {
                  for (Bundle permittedRole : permittedRoles) {
                     roleNames.add(permittedRole.getName());
                  }
               }
               val = Util.listToCsv(roleNames);
            } else if (("roles".compareTo(property) == 0)) {
               List<Bundle> roles = role.getRoles();
               List<String> roleNames = new ArrayList<String>();
               if (null != roles && !roles.isEmpty()) {
                  for (Bundle thisRole : roles) {
                     roleNames.add(thisRole.getName());
                  }
               }
               val = Util.listToCsv(roleNames);
            } else if (("class".compareTo(property) == 0)) {
               val = "Bundle";
            } else if (("owner.name".compareTo(property) == 0)) {
               Identity owner = role.getOwner();
               if (owner != null) {
                  val = owner.getName();
               }
            } else if (("owner.id".compareTo(property) == 0)) {
               Identity owner = role.getOwner();
               if (owner != null) {
                  val = owner.getId();
               }
            } else {
               val = (role.getAttributes() != null) ? role.getAttributes().get(
                     property) : val;
            }
         }
         return val;
      }
   }

   public class PolicyViolationMatcher extends JavaMatcher {

      public PolicyViolationMatcher(Filter filter) {
         super(filter);
      }

      @Override
      public Object getPropertyValue(LeafFilter leaf, Object o)
            throws GeneralException {
         Object val = null;
         if (o == null)
            return val;

         PolicyViolation violation = null;
         if (!(o instanceof PolicyViolation)) {
            throw new GeneralException("Expected a PolicyViolation: " + o);
         }
         violation = (PolicyViolation) o;

         String property = leaf.getProperty();

         if (property != null) {
            if (("name".compareTo(property) == 0)) {
               val = violation.getName();
            } else if (("identity.name".compareTo(property) == 0)) {
               Identity identity = violation.getIdentity();
               if (identity != null) {
                  val = identity.getName();
               }
            } else if (("identity.id".compareTo(property) == 0)) {
               Identity identity = violation.getIdentity();
               if (identity != null) {
                  val = identity.getId();
               }
            } else if (("policyName".compareTo(property) == 0)) {
               val = violation.getPolicyName();
            } else if (("policyId".compareTo(property) == 0)) {
               val = violation.getPolicyId();
            } else if (("id".compareTo(property) == 0)) {
               val = violation.getId();
            } else if (("constraintName".compareTo(property) == 0)) {
               val = violation.getConstraintName();
            } else if (("leftBundles".compareTo(property) == 0)) {
               val = violation.getLeftBundles();
            } else if (("rightBundles".compareTo(property) == 0)) {
               val = violation.getRightBundles();
            } else if (("sodSummary".compareToIgnoreCase(property) == 0)) {
               val = violation.getSODSummary();
            } else if (("status".compareTo(property) == 0)) {
               PolicyViolation.Status status = violation.getStatus();
               if (null != status) {
                  val = status.toString();
               }
            } else if (("class".compareTo(property) == 0)) {
               val = "PolicyViolation";
            } else if (("mitigator".compareTo(property) == 0)) {
               val = violation.getMitigator();
            } else if (("active".compareTo(property) == 0)) {
               val = violation.isActive();
            } else if (("owner.name".compareTo(property) == 0)) {
               Identity owner = violation.getOwner();
               if (owner != null) {
                  val = owner.getName();
               }
            } else if (("owner.id".compareTo(property) == 0)) {
               Identity owner = violation.getOwner();
               if (owner != null) {
                  val = owner.getId();
               }
            } else if (("relevantApplications".compareTo(property) == 0)) {
               List<String> appNames = violation.getRelevantApps();
               val = Util.listToCsv(appNames);
            } else {
               val = (violation.getArguments() != null) ? violation
                     .getArguments().get(property) : val;
            }
         }
         return val;
      }
   }

   public class ProfileMatcher extends JavaMatcher {

      public ProfileMatcher(Filter filter) {
         super(filter);
      }

      @Override
      public Object getPropertyValue(LeafFilter leaf, Object o)
            throws GeneralException {
         Object val = null;
         if (o == null)
            return val;

         Profile profile = null;
         if (!(o instanceof Profile)) {
            throw new GeneralException("Expected a Profile: " + o);
         }
         profile = (Profile) o;

         String property = leaf.getProperty();

         if (property != null) {
            if (("name".compareTo(property) == 0)) {
               val = profile.getName();
            } else if (("accountType".compareTo(property) == 0)) {
               val = profile.getAccountType();
            } else if (("application.name".compareTo(property) == 0)) {
               Application application = profile.getApplication();
               if (application != null) {
                  val = application.getName();
               }
            } else if (("application.id".compareTo(property) == 0)) {
               Application application = profile.getApplication();
               if (application != null) {
                  val = application.getId();
               }
            } else if (("application.owner".compareTo(property) == 0)) {
               Application application = profile.getApplication();
               if (application != null) {
                  val = application.getOwner().getName();
               }
            } else if (("application.owner.name".compareTo(property) == 0)) {
               Application application = profile.getApplication();
               if (application != null) {
                  val = application.getOwner().getName();
               }
            } else if (("application.owner.id".compareTo(property) == 0)) {
               Application application = profile.getApplication();
               if (application != null) {
                  val = application.getOwner().getId();
               }
            } else if (("bundle.name".compareTo(property) == 0)) {
               Bundle profileBundle = profile.getBundle();
               if (profileBundle != null) {
                  val = profileBundle.getName();
               }
            } else if (("bundle.id".compareTo(property) == 0)) {
               Bundle profileBundle = profile.getBundle();
               if (profileBundle != null) {
                  val = profileBundle.getId();
               }
            } else if (("bundle.owner".compareTo(property) == 0)) {
               Bundle profileBundle = profile.getBundle();
               if (profileBundle != null) {
                  val = profileBundle.getOwner().getName();
               }
            } else if (("bundle.owner.name".compareTo(property) == 0)) {
               Bundle profileBundle = profile.getBundle();
               if (profileBundle != null) {
                  val = profileBundle.getOwner().getName();
               }
            } else if (("bundle.owner.id".compareTo(property) == 0)) {
               Bundle profileBundle = profile.getBundle();
               if (profileBundle != null) {
                  val = profileBundle.getOwner().getId();
               }
            } else if (("class".compareTo(property) == 0)) {
               val = "Profile";
            } else {
               val = (profile.getAttributes() != null) ? profile
                     .getAttributes().get(property) : val;
            }
         }
         return val;
      }
   }

   public class ItemToObjectMatcher {

      private SailPointContext _context;

      public ItemToObjectMatcher() {
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

      public ItemToObjectMatcher(SailPointContext context) {
         super();
         _context = context;
      }

      public Object getObjectFromItem(Object o) throws GeneralException {
         if (!(o instanceof Certifiable)) {
            throw new GeneralException("Expected a Certifiable: " + o);
         }
         Object retVal = null;

         if (o instanceof Bundle) {
            retVal = (Bundle) o;
         } else if (o instanceof EntitlementGroup) {
            Attributes attrs = ((EntitlementGroup) o).getAttributes();
            List entlist = attrs.getKeys();
            Iterator entit = entlist.iterator();
            String maAppName = ((EntitlementGroup) o).getApplicationName();
            String maValue = null;
            String maAttr = null;
            if (entit.hasNext()) {
               maAttr = (String) entit.next();
               maValue = attrs.getString(maAttr);
            }
            QueryOptions options = new QueryOptions();
            Filter filter = Filter.ignoreCase(Filter.eq("attribute", maAttr));
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
         } else if (o instanceof DataOwnerCertifiable) {
            retVal = ((DataOwnerCertifiable) o).getIdentity();
         }
         if (log.isTraceEnabled())
            log.trace("Returning " + retVal + " for " + o);
         return retVal;
      }
   }
}
