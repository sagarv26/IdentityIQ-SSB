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
-- Table structure for table `spt_policy_violation`
--

DROP TABLE IF EXISTS `spt_policy_violation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_policy_violation` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(2000) DEFAULT NULL,
  `description` varchar(4000) DEFAULT NULL,
  `identity_id` varchar(32) DEFAULT NULL,
  `pending_workflow` varchar(32) DEFAULT NULL,
  `renderer` varchar(255) DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `policy_id` varchar(255) DEFAULT NULL,
  `policy_name` varchar(255) DEFAULT NULL,
  `constraint_id` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `constraint_name` varchar(2000) DEFAULT NULL,
  `left_bundles` longtext,
  `right_bundles` longtext,
  `activity_id` varchar(255) DEFAULT NULL,
  `bundles_marked_for_remediation` longtext,
  `entitlements_marked_for_remed` longtext,
  `mitigator` varchar(255) DEFAULT NULL,
  `arguments` longtext,
  PRIMARY KEY (`id`),
  KEY `spt_policy_violation_active` (`active`),
  KEY `FK6E4413E056651F3A` (`identity_id`),
  KEY `FK6E4413E0BD5A5736` (`pending_workflow`),
  KEY `FK6E4413E0486634B7` (`assigned_scope`),
  KEY `FK6E4413E0A5FB1B1` (`owner`),
  KEY `SPT_IDXBB0D4BCC29515FAC` (`assigned_scope_path`(255)),
  CONSTRAINT `FK6E4413E0486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK6E4413E056651F3A` FOREIGN KEY (`identity_id`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK6E4413E0A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK6E4413E0BD5A5736` FOREIGN KEY (`pending_workflow`) REFERENCES `spt_workflow_case` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_policy_violation`
--

LOCK TABLES `spt_policy_violation` WRITE;
/*!40000 ALTER TABLE `spt_policy_violation` DISABLE KEYS */;
INSERT INTO `spt_policy_violation` VALUES ('a9fe0bbd92d11bbd8192d1bf1ae0013a',1730095815392,1743569746777,'a9fe0bbd90c41bed8190d472dc1d0658',NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d472dba30656',NULL,'policySODDetails.xhtml',_binary '','a9fe0bbd92d11bbd8192d1bc58f00111','Role SOD','a9fe0bbd92d11bbd8192d1bc58f00112','Open','Role Rule',NULL,NULL,NULL,NULL,NULL,NULL,' '),('a9fe0bbd92d11bbd8192d1bf2097013b',1730095816855,1743569748124,'a9fe0bbd90c41bed8190d472dc1d0658',NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d472dcb6065a',NULL,'policySODDetails.xhtml',_binary '','a9fe0bbd92d11bbd8192d1bc58f00111','Role SOD','a9fe0bbd92d11bbd8192d1bc58f00112','Open','Role Rule','Admin,User','User,Admin',NULL,NULL,NULL,NULL,' ');
/*!40000 ALTER TABLE `spt_policy_violation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:14
