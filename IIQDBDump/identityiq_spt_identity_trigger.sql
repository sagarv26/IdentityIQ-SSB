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
-- Table structure for table `spt_identity_trigger`
--

DROP TABLE IF EXISTS `spt_identity_trigger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_identity_trigger` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(256) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `disabled` bit(1) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `rule_id` varchar(32) DEFAULT NULL,
  `attribute_name` varchar(256) DEFAULT NULL,
  `old_value_filter` varchar(256) DEFAULT NULL,
  `new_value_filter` varchar(256) DEFAULT NULL,
  `selector` longtext,
  `handler` varchar(256) DEFAULT NULL,
  `parameters` longtext,
  PRIMARY KEY (`id`),
  KEY `FKE207B8BF3908AE7A` (`rule_id`),
  KEY `FKE207B8BF486634B7` (`assigned_scope`),
  KEY `FKE207B8BFA5FB1B1` (`owner`),
  KEY `SPT_IDX321B16EB1422CFAA` (`assigned_scope_path`(255)),
  CONSTRAINT `FKE207B8BF3908AE7A` FOREIGN KEY (`rule_id`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FKE207B8BF486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKE207B8BFA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_identity_trigger`
--

LOCK TABLES `spt_identity_trigger` WRITE;
/*!40000 ALTER TABLE `spt_identity_trigger` DISABLE KEYS */;
INSERT INTO `spt_identity_trigger` VALUES ('4028ab1063fdfb6d0163fdfc4a79000f',1528974559865,1739425286374,NULL,NULL,NULL,'Joiner','Process a new employee.',_binary '\0','Create',NULL,NULL,NULL,NULL,' ','sailpoint.api.WorkflowTriggerHandler','<Attributes>\n  <Map>\n    <entry key=\"workflow\" value=\"Lifecycle Event - Joiner\"/>\n  </Map>\n</Attributes>\n'),('4028ab1063fdfb6d0163fdfc4ae00010',1528974559968,1739425286519,NULL,NULL,NULL,'Leaver','Disable all accounts when a user leaves.',_binary '\0','AttributeChange',NULL,'inactive','false','true',' ','sailpoint.api.WorkflowTriggerHandler','<Attributes>\n  <Map>\n    <entry key=\"workflow\" value=\"Lifecycle Event - Leaver\"/>\n  </Map>\n</Attributes>\n'),('4028ab1063fdfb6d0163fdfc4b150011',1528974560021,1739425286599,NULL,NULL,NULL,'Reinstate','Re-enable all accounts when a user has been reinstated.',_binary '\0','Rule','a9fe0bbd939010d78193957532b401be',NULL,NULL,NULL,' ','sailpoint.api.WorkflowTriggerHandler','<Attributes>\n  <Map>\n    <entry key=\"workflow\" value=\"Lifecycle Event - Reinstate\"/>\n  </Map>\n</Attributes>\n'),('4028ab1063fdfb6d0163fdfc4ba80012',1528974560169,1617706123740,NULL,NULL,NULL,'Manager transfer','Disable all accounts when manager transfer occurs.',_binary '','ManagerTransfer',NULL,NULL,NULL,NULL,' ','sailpoint.api.WorkflowTriggerHandler','<Attributes>\n  <Map>\n    <entry key=\"workflow\" value=\"Lifecycle Event - Manager Transfer\"/>\n  </Map>\n</Attributes>\n');
/*!40000 ALTER TABLE `spt_identity_trigger` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:33
