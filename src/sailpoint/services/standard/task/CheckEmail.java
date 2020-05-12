package sailpoint.services.standard.task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.SailPointContext;
import sailpoint.object.Custom;

public class CheckEmail {

   private Properties props;
   private Store store;
   private Session session;
   private String port;
   private static Log log = LogFactory.getLog(CheckEmail.class);

   @SuppressWarnings({ "rawtypes" })
   public CheckEmail(SailPointContext context) throws Exception {

      this.props = new Properties();
      try {

         Map map = (Map) context.getObject(Custom.class, "Email Approvals")
               .get("ApproveDeny");

         if (map != null) {

            Set keys = map.keySet();

            for (Iterator i = keys.iterator(); i.hasNext();) {
               String key = (String) i.next();
               String value = (String) map.get(key);
               if (value != null) {
                  props.setProperty(key, value);
               }
            }
            this.session = Session.getInstance(props, null);
            String protocol = props.getProperty("mail.protocol");
            if (null == protocol)
               protocol = "imaps";
            this.store = session.getStore(protocol);
            this.port = props.getProperty("mail.port");
            if (null == this.port) {
               this.port = "993";
            }
         }
      } catch (Exception e1) {
         e1.printStackTrace();
      }

   }

   public boolean connectToMailServer() throws Exception {

      try {
         store.connect(props.getProperty("mail.host"),
               Integer.valueOf(port), props.getProperty("svcAccountEmail"),
               props.getProperty("svcAccountPassword"));
         log.debug("Connection to Email Account:" + store + " Success");

      } catch (Exception e1) {
         e1.printStackTrace();
      }
      return true;

   }

   public boolean setProps(String key, String value) throws Exception {
      try {

         this.props.setProperty(key, value);

      } catch (Exception e1) {
         e1.printStackTrace();
      }
      return true;

   }

   public String getProps(String key) throws Exception {
      try {

         return this.props.getProperty(key);

      } catch (Exception e1) {
         e1.printStackTrace();
      }

      return null;
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   public Map<String, String> getNextMessageNo(int messageNo) throws Exception {
      Map mMap = new HashMap();

      try {

         Folder inbox = this.store.getFolder("inbox");

         inbox.open(Folder.READ_WRITE); // Folder.READ_ONLY
         Message message = inbox.getMessage(messageNo);
         for (Flags.Flag flag : message.getFlags().getSystemFlags()) {
            if (flag == Flags.Flag.SEEN) {
               break;
            }
         }
         message.setFlag(Flags.Flag.SEEN, true);
         mMap.put("subject", message.getSubject());

         Address fromAddress[] = message.getFrom();
         if (fromAddress != null) {
            mMap.put("from", fromAddress[0].toString());
         }

         // Do not read message content. ( could enable if needed )
         /*
          * String content = dumpPart(message); if ( content != null) {
          * mMap.put("content",content ); }
          */

         inbox.close(true);

      } catch (Exception e1) {
         e1.printStackTrace();
      }

      return mMap;
   }

   public int getMessageCount() throws Exception {
      int messageCount = 0;
      try {

         Folder inbox = this.store.getFolder("inbox");
         inbox.open(Folder.READ_ONLY); // Folder.READ_ONLY
         messageCount = inbox.getMessageCount();
         inbox.close(true);
      } catch (Exception e1) {
         e1.printStackTrace();
      }

      return messageCount;
   }

   public boolean deleteMessageNo(int messageNo) throws Exception {
      try {

         Folder inbox = this.store.getFolder("inbox");
         inbox.open(Folder.READ_WRITE); // Folder.READ_ONLY
         Message message = inbox.getMessage(messageNo);
         message.setFlag(Flags.Flag.DELETED, true);
         inbox.close(true);
      } catch (Exception e1) {
         e1.printStackTrace();
      }

      return true;
   }

   public boolean closeConnection() throws Exception {
      try {

         this.store.close();
      } catch (Exception e1) {
         e1.printStackTrace();
      }
      return true;
   }

}
