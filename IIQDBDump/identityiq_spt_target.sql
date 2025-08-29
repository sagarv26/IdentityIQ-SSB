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
-- Table structure for table `spt_target`
--

DROP TABLE IF EXISTS `spt_target`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_target` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `extended1` varchar(255) DEFAULT NULL,
  `name` varchar(512) DEFAULT NULL,
  `native_owner_id` varchar(128) DEFAULT NULL,
  `target_source` varchar(32) DEFAULT NULL,
  `application` varchar(32) DEFAULT NULL,
  `target_host` varchar(1024) DEFAULT NULL,
  `display_name` varchar(400) DEFAULT NULL,
  `full_path` longtext,
  `unique_name_hash` varchar(128) DEFAULT NULL,
  `attributes` longtext,
  `native_object_id` varchar(322) DEFAULT NULL,
  `parent` varchar(32) DEFAULT NULL,
  `target_size` bigint(20) DEFAULT NULL,
  `last_aggregation` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `spt_target_native_obj_id` (`native_object_id`(255)),
  KEY `spt_target_last_agg` (`last_aggregation`),
  KEY `spt_target_unique_name_hash` (`unique_name_hash`),
  KEY `spt_target_extended1_ci` (`extended1`),
  KEY `FK19E5251939D71460` (`application`),
  KEY `FK19E525192F001D5` (`target_source`),
  KEY `FK19E52519486634B7` (`assigned_scope`),
  KEY `FK19E52519A5FB1B1` (`owner`),
  KEY `FK19E525195D4B587B` (`parent`),
  KEY `spt_target_assignedscopepath` (`assigned_scope_path`(255)),
  CONSTRAINT `FK19E525192F001D5` FOREIGN KEY (`target_source`) REFERENCES `spt_target_source` (`id`),
  CONSTRAINT `FK19E5251939D71460` FOREIGN KEY (`application`) REFERENCES `spt_application` (`id`),
  CONSTRAINT `FK19E52519486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK19E525195D4B587B` FOREIGN KEY (`parent`) REFERENCES `spt_target` (`id`),
  CONSTRAINT `FK19E52519A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_target`
--

LOCK TABLES `spt_target` WRITE;
/*!40000 ALTER TABLE `spt_target` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_target` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:42
