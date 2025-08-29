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
-- Table structure for table `spt_work_item_archive`
--

DROP TABLE IF EXISTS `spt_work_item_archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_work_item_archive` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `work_item_id` varchar(128) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `identity_request_id` varchar(128) DEFAULT NULL,
  `assignee` varchar(255) DEFAULT NULL,
  `requester` varchar(255) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `handler` varchar(255) DEFAULT NULL,
  `renderer` varchar(255) DEFAULT NULL,
  `target_class` varchar(255) DEFAULT NULL,
  `target_id` varchar(255) DEFAULT NULL,
  `target_name` varchar(255) DEFAULT NULL,
  `archived` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `severity` varchar(255) DEFAULT NULL,
  `attributes` longtext,
  `system_attributes` longtext,
  `immutable` bit(1) DEFAULT NULL,
  `signed` bit(1) DEFAULT NULL,
  `completer` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `spt_item_archive_severity` (`severity`),
  KEY `spt_item_archive_name` (`name`),
  KEY `spt_item_archive_assignee_ci` (`assignee`),
  KEY `spt_item_archive_type` (`type`),
  KEY `spt_item_archive_completer` (`completer`),
  KEY `spt_item_archive_requester_ci` (`requester`),
  KEY `spt_item_archive_workItemId` (`work_item_id`),
  KEY `spt_item_archive_owner_ci` (`owner_name`),
  KEY `spt_item_archive_target` (`target_id`),
  KEY `spt_item_archive_ident_req` (`identity_request_id`),
  KEY `FKDFABED7C486634B7` (`assigned_scope`),
  KEY `FKDFABED7CA5FB1B1` (`owner`),
  KEY `SPT_IDX1647668E11063E4` (`assigned_scope_path`(255)),
  CONSTRAINT `FKDFABED7C486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKDFABED7CA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_work_item_archive`
--

LOCK TABLES `spt_work_item_archive` WRITE;
/*!40000 ALTER TABLE `spt_work_item_archive` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_work_item_archive` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:07
