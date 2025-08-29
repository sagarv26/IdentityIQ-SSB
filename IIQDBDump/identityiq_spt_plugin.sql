-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: identityiq
-- ------------------------------------------------------
-- Server version	5.7.22-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `spt_plugin`
--

DROP TABLE IF EXISTS `spt_plugin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_plugin` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `install_date` bigint(20) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `disabled` bit(1) DEFAULT NULL,
  `right_required` varchar(255) DEFAULT NULL,
  `min_system_version` varchar(255) DEFAULT NULL,
  `max_system_version` varchar(255) DEFAULT NULL,
  `attributes` longtext,
  `position` int(11) DEFAULT NULL,
  `certification_level` varchar(255) DEFAULT NULL,
  `file_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `file_id` (`file_id`),
  KEY `spt_plugin_dn_ci` (`display_name`),
  KEY `spt_plugin_name_ci` (`name`),
  KEY `FK13AE22BBF7C36E0D` (`file_id`),
  CONSTRAINT `FK13AE22BBF7C36E0D` FOREIGN KEY (`file_id`) REFERENCES `spt_persisted_file` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_plugin`
--

LOCK TABLES `spt_plugin` WRITE;
/*!40000 ALTER TABLE `spt_plugin` DISABLE KEYS */;
INSERT INTO `spt_plugin` VALUES ('a9fe0bbd97a01e0b8197ab42e2c60956','SupportPlugin',1750924976838,NULL,1750924976837,'Support DataCollector Plugin','2.2.0',_binary '\0',NULL,'7.1',NULL,'<Attributes>\n  <Map>\n    <entry key=\"fullPage\">\n      <value>\n        <FullPage title=\"Support Plugin\"/>\n      </value>\n    </entry>\n    <entry key=\"minUpgradableVersion\" value=\"0.0\"/>\n    <entry key=\"restResources\">\n      <value>\n        <List>\n          <String>sailpoint.support.plugin.rest.SupportPluginResource</String>\n        </List>\n      </value>\n    </entry>\n    <entry key=\"serviceExecutors\">\n      <value>\n        <List>\n          <String>sailpoint.support.plugin.server.SupportPluginRequestService</String>\n        </List>\n      </value>\n    </entry>\n    <entry key=\"settings\"/>\n    <entry key=\"snippets\"/>\n  </Map>\n</Attributes>\n',0,'None','a9fe0bbd97a01e0b8197ab42e0080954');
/*!40000 ALTER TABLE `spt_plugin` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:19
