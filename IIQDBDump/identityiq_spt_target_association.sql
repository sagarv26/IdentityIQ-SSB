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
-- Table structure for table `spt_target_association`
--

DROP TABLE IF EXISTS `spt_target_association`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_target_association` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `object_id` varchar(32) DEFAULT NULL,
  `type` varchar(8) DEFAULT NULL,
  `hierarchy` varchar(512) DEFAULT NULL,
  `flattened` bit(1) DEFAULT NULL,
  `application_name` varchar(128) DEFAULT NULL,
  `target_type` varchar(128) DEFAULT NULL,
  `target_name` varchar(255) DEFAULT NULL,
  `target_id` varchar(32) DEFAULT NULL,
  `rights` varchar(512) DEFAULT NULL,
  `inherited` bit(1) DEFAULT NULL,
  `effective` int(11) DEFAULT NULL,
  `deny_permission` bit(1) DEFAULT NULL,
  `last_aggregation` bigint(20) DEFAULT NULL,
  `attributes` longtext,
  PRIMARY KEY (`id`),
  KEY `spt_target_assoc_id` (`object_id`),
  KEY `spt_target_assoc_targ_name_ci` (`target_name`),
  KEY `spt_target_assoc_last_agg` (`last_aggregation`),
  KEY `FK7AD6825B486634B7` (`assigned_scope`),
  KEY `FK7AD6825B68039A5A` (`target_id`),
  KEY `FK7AD6825BA5FB1B1` (`owner`),
  KEY `SPT_IDX8DFD31878D3B3E2` (`assigned_scope_path`(255)),
  CONSTRAINT `FK7AD6825B486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK7AD6825B68039A5A` FOREIGN KEY (`target_id`) REFERENCES `spt_target` (`id`),
  CONSTRAINT `FK7AD6825BA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_target_association`
--

LOCK TABLES `spt_target_association` WRITE;
/*!40000 ALTER TABLE `spt_target_association` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_target_association` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:51
