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
-- Table structure for table `spt_remediation_item`
--

DROP TABLE IF EXISTS `spt_remediation_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_remediation_item` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `remediation_entity_type` varchar(255) DEFAULT NULL,
  `work_item_id` varchar(32) DEFAULT NULL,
  `certification_item` varchar(255) DEFAULT NULL,
  `assignee` varchar(32) DEFAULT NULL,
  `remediation_identity` varchar(255) DEFAULT NULL,
  `remediation_details` longtext,
  `completion_comments` longtext,
  `completion_date` bigint(20) DEFAULT NULL,
  `assimilated` bit(1) DEFAULT NULL,
  `comments` longtext,
  `idx` int(11) DEFAULT NULL,
  `attributes` longtext,
  PRIMARY KEY (`id`),
  KEY `FK53608075FCF09A9D` (`work_item_id`),
  KEY `FK53608075EDFFCCCD` (`assignee`),
  KEY `FK53608075486634B7` (`assigned_scope`),
  KEY `FK53608075A5FB1B1` (`owner`),
  KEY `SPT_IDXA6919D21F9F21D96` (`assigned_scope_path`(255)),
  CONSTRAINT `FK53608075486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK53608075A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK53608075EDFFCCCD` FOREIGN KEY (`assignee`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK53608075FCF09A9D` FOREIGN KEY (`work_item_id`) REFERENCES `spt_work_item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_remediation_item`
--

LOCK TABLES `spt_remediation_item` WRITE;
/*!40000 ALTER TABLE `spt_remediation_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_remediation_item` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:25
