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
-- Table structure for table `spt_certification_action`
--

DROP TABLE IF EXISTS `spt_certification_action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_certification_action` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `email_template` varchar(255) DEFAULT NULL,
  `comments` longtext,
  `expiration` datetime(6) DEFAULT NULL,
  `work_item` varchar(255) DEFAULT NULL,
  `completion_state` varchar(255) DEFAULT NULL,
  `completion_comments` longtext,
  `completion_user` varchar(128) DEFAULT NULL,
  `actor_name` varchar(128) DEFAULT NULL,
  `actor_display_name` varchar(128) DEFAULT NULL,
  `acting_work_item` varchar(255) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `decision_date` bigint(20) DEFAULT NULL,
  `decision_certification_id` varchar(128) DEFAULT NULL,
  `reviewed` bit(1) DEFAULT NULL,
  `bulk_certified` bit(1) DEFAULT NULL,
  `mitigation_expiration` bigint(20) DEFAULT NULL,
  `remediation_action` varchar(255) DEFAULT NULL,
  `remediation_details` longtext,
  `additional_actions` longtext,
  `revoke_account` bit(1) DEFAULT NULL,
  `ready_for_remediation` bit(1) DEFAULT NULL,
  `remediation_kicked_off` bit(1) DEFAULT NULL,
  `remediation_completed` bit(1) DEFAULT NULL,
  `source_action` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `spt_item_ready_for_remed` (`ready_for_remediation`),
  KEY `FK198026E3486634B7` (`assigned_scope`),
  KEY `FK198026E310F4E42A` (`source_action`),
  KEY `FK198026E3A5FB1B1` (`owner`),
  KEY `SPT_IDX9D89C40FB709EAF2` (`assigned_scope_path`(255)),
  CONSTRAINT `FK198026E310F4E42A` FOREIGN KEY (`source_action`) REFERENCES `spt_certification_action` (`id`),
  CONSTRAINT `FK198026E3486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK198026E3A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_certification_action`
--

LOCK TABLES `spt_certification_action` WRITE;
/*!40000 ALTER TABLE `spt_certification_action` DISABLE KEYS */;
INSERT INTO `spt_certification_action` VALUES ('a9fe0bbd92d11bbd8192d1cb59e7017d',1730096617959,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730096617957,'a9fe0bbd92d11bbd8192d1cb23230177',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7391c8083b',1730275414472,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Mitigated',1730275414470,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',1732953814470,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7392d5083c',1730275414741,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Mitigated',1730275414738,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',1732953814738,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc739333083d',1730275414836,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Mitigated',1730275414832,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',1732953814832,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc739376083e',1730275414902,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Mitigated',1730275414899,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',1732953814899,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7393b0083f',1730275414960,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275414958,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7393f30840',1730275415027,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415025,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc73945a0841',1730275415130,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415127,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7394b00842',1730275415216,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415214,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7394e80843',1730275415272,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415270,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7395210844',1730275415329,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415326,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7395540845',1730275415380,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415377,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7395960846',1730275415446,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415444,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7395d90847',1730275415513,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415510,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7396150848',1730275415573,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415571,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7396490849',1730275415625,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415622,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc739673084a',1730275415667,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415664,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7396bc084b',1730275415740,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415735,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),('a9fe0bbd92d11bbd8192dc7396f7084c',1730275415799,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'spadmin','The Administrator',NULL,NULL,'Approved',1730275415797,'a9fe0bbd92d11bbd8192dc7211340817',_binary '',_binary '\0',NULL,NULL,' ',' ',_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL);
/*!40000 ALTER TABLE `spt_certification_action` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:24
