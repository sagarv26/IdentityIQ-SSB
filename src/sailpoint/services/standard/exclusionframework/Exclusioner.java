package sailpoint.services.standard.exclusionframework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.api.certification.AccountGroupMembershipCertificationBuilder;
import sailpoint.api.certification.AccountGroupMembershipCertificationBuilder.MembershipCertifiable;
import sailpoint.api.certification.BusinessRoleCompositionCertificationBuilder.RoleCertifiable;
import sailpoint.api.certification.DataOwnerCertificationBuilder.DataOwnerCertifiable;
import sailpoint.object.AbstractCertifiableEntity;
import sailpoint.object.Certifiable;
import sailpoint.object.CertificationDefinition;
import sailpoint.object.EntitlementGroup;
import sailpoint.tools.GeneralException;
import sailpoint.tools.xml.XMLObjectFactory;

public class Exclusioner {

   private static List<Certifiable> candidateItems;

   private static List<Certifiable> matchedItems;

   private static ExclusionSet exclusionSet;

   private static Log log = LogFactory.getLog(Exclusioner.class);

   public static String processExclusions(String exclusionSetName,
         AbstractCertifiableEntity entity, List<Certifiable> items,
         List<Certifiable> itemsToExclude) throws GeneralException,
         NoSuchMethodException, SecurityException, IllegalAccessException,
         IllegalArgumentException, InvocationTargetException,
         InstantiationException {
      candidateItems = new ArrayList<Certifiable>();
      matchedItems = new ArrayList<Certifiable>();
      exclusionSet = new ExclusionSet(exclusionSetName);
      /*
       * Make sure we have some exclusions, otherwise this is a moot point.
       */
      if (exclusionSet != null) {

         /*
          * We map the incoming items to our local lists of items. We have more
          * generic names for our variables, as there are 'modes' this exclusion
          * rule framework can operate as.
          */
         SailPointContext context = SailPointFactory.getCurrentContext();
         if (null != items && !items.isEmpty()) {
            for (Certifiable item : items) {
               Certifiable candidateItem = null;
               if (item instanceof sailpoint.tools.xml.AbstractXmlObject) {
                  candidateItem = (Certifiable) XMLObjectFactory.getInstance()
                        .clone(item, context);
               } else if (item instanceof DataOwnerCertifiable) {
                  // DataOwnerCertifiable is not serializable so we'll clone it
                  // like this
                  candidateItem = new DataOwnerCertifiable(
                        ((DataOwnerCertifiable) item).getIdentity(),
                        ((DataOwnerCertifiable) item).getEntitlements());
               } else if (item instanceof MembershipCertifiable) {
                  // MembershipCertifiable is not serializable so we'll clone it
                  // like this
                  candidateItem = cloneMembershipCertifiable(context,
                        (MembershipCertifiable) item);
               } else if (item instanceof RoleCertifiable) {
                  // RoleCertifiable is not serializable so we'll clone it like
                  // this
                  candidateItem = new RoleCertifiable(
                        ((RoleCertifiable) item).getType(),
                        ((RoleCertifiable) item).getObject());
               } else {
                  throw new GeneralException("Unable to clone item: " + item);
               }
               if (null != candidateItem) {
                  candidateItems.add(candidateItem);
               }
            }
         }

         /*
          * Handle any items to exclude that have already been specified in the
          * itemsToExclude list in the exclusion rule.
          */
         matchedItems = new ArrayList<Certifiable>();
         if (null != itemsToExclude && !itemsToExclude.isEmpty()) {
            for (Certifiable itemToExclude : itemsToExclude) {
               Certifiable matchedItem = null;
               if (itemToExclude instanceof sailpoint.tools.xml.AbstractXmlObject) {
                  matchedItem = (Certifiable) XMLObjectFactory.getInstance()
                        .clone(itemToExclude, context);
               } else if (itemToExclude instanceof DataOwnerCertifiable) {
                  matchedItem = new DataOwnerCertifiable(
                        ((DataOwnerCertifiable) itemToExclude).getIdentity(),
                        ((DataOwnerCertifiable) itemToExclude)
                              .getEntitlements());
               } else if (itemToExclude instanceof MembershipCertifiable) {
                  matchedItem = cloneMembershipCertifiable(context,
                        (MembershipCertifiable) itemToExclude);
               } else if (itemToExclude instanceof RoleCertifiable) {
                  matchedItem = new RoleCertifiable(
                        ((RoleCertifiable) itemToExclude).getType(),
                        ((RoleCertifiable) itemToExclude).getObject());
               } else {
                  throw new GeneralException("Unable to clone excluded item: "
                        + itemToExclude);
               }
               if (null != matchedItem) {
                  matchedItems.add(matchedItem);
               }
            }
         }

         /*
          * First, we'll process any entity selectors if there are any.
          * 
          * Note: matches(...) is null safe.
          */
         if (exclusionSet.matches(entity)) {
            if (log.isDebugEnabled())
               log.debug("Entity Matches! " + entity);
            if (matchedItems == null)
               matchedItems = new ArrayList<Certifiable>();

            matchedItems.addAll(candidateItems);
            candidateItems.clear();
         }

         /*
          * Next we move on to the certifiable items themselves.
          */
         if (candidateItems != null) {

            for (Iterator<Certifiable> it = candidateItems.iterator(); it
                  .hasNext();) {

               Certifiable candidateItem = it.next();

               if (exclusionSet.matches(candidateItem)) {
                  if (log.isDebugEnabled())
                     log.debug("Item match found!" + candidateItem);
                  if (matchedItems == null)
                     matchedItems = new ArrayList<Certifiable>();

                  matchedItems.add(candidateItem);
                  it.remove();
               }
            }

         }

         /*
          * Last, we map our matches back to inclusions / exclusions.
          * 
          * If the mode is to include items, then matches are included, and
          * non-matches are excluded. If the mode is to exclude items (default),
          * then matches are excluded, and non-matches are included.
          */
         items.clear();
         itemsToExclude.clear();
         switch (exclusionSet.getMode()) {
         case INCLUDE:
            if (log.isTraceEnabled())
               log.trace("Exclusion Set is INCLUDE");
            items.addAll(matchedItems);
            itemsToExclude.addAll(candidateItems);
            break;
         case EXCLUDE:
         default:
            if (log.isTraceEnabled())
               log.trace("Exclusion Set is EXCLUDE");
            items.addAll(candidateItems);
            itemsToExclude.addAll(matchedItems);
            break;
         }

      }
      return (!itemsToExclude.isEmpty() && exclusionSet != null) ? exclusionSet
            .getExclusionReason() : null;
   }

   /*
    * In order to support 6.3 we need to use Reflection to clone the
    * MembershipCertifiable items because the getAccount method only exists in
    * 6.4+
    */
   private static Certifiable cloneMembershipCertifiable(
         SailPointContext context, MembershipCertifiable mcItem)
         throws IllegalAccessException, IllegalArgumentException,
         InvocationTargetException, NoSuchMethodException, SecurityException,
         InstantiationException {
      AccountGroupMembershipCertificationBuilder membershipCertBuilder = new AccountGroupMembershipCertificationBuilder(
            context, new CertificationDefinition());
      EntitlementGroup entGroup = ((MembershipCertifiable) mcItem)
            .getEntitlementGroup();
      String identityName = ((MembershipCertifiable) mcItem).getIdentityName();
      String identityId = ((MembershipCertifiable) mcItem).getIdentityId();

      Certifiable mcCandidateItem;
      boolean pre64 = false;
      String account = null;
      Method getAccountMethod;
      try {
         getAccountMethod = mcItem.getClass().getMethod("getAccount",
               (Class<?>[]) null);
         account = (String) getAccountMethod.invoke(
               (MembershipCertifiable) mcItem, (Object[]) null);
      } catch (NoSuchMethodException e) {
         pre64 = true;
      }

      Constructor<? extends Certifiable> membershipCertifiableConstructor;
      if (pre64) {
         membershipCertifiableConstructor = mcItem.getClass().getConstructor(
               AccountGroupMembershipCertificationBuilder.class,
               EntitlementGroup.class, String.class, String.class);
         mcCandidateItem = (Certifiable) membershipCertifiableConstructor
               .newInstance(membershipCertBuilder, entGroup, identityName,
                     identityId);
      } else {
         membershipCertifiableConstructor = mcItem.getClass()
               .getConstructor(
                     AccountGroupMembershipCertificationBuilder.class,
                     EntitlementGroup.class, String.class, String.class,
                     String.class);
         mcCandidateItem = (Certifiable) membershipCertifiableConstructor
               .newInstance(membershipCertBuilder, entGroup, identityName,
                     identityId, account);
      }
      return mcCandidateItem;

   }

}
