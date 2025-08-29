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
-- Table structure for table `spt_role_index`
--

DROP TABLE IF EXISTS `spt_role_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_role_index` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `incomplete` bit(1) DEFAULT NULL,
  `composite_score` int(11) DEFAULT NULL,
  `attributes` longtext,
  `items` longtext,
  `bundle` varchar(32) DEFAULT NULL,
  `assigned_count` int(11) DEFAULT NULL,
  `detected_count` int(11) DEFAULT NULL,
  `associated_to_role` bit(1) DEFAULT NULL,
  `last_certified_membership` bigint(20) DEFAULT NULL,
  `last_certified_composition` bigint(20) DEFAULT NULL,
  `last_assigned` bigint(20) DEFAULT NULL,
  `entitlement_count` int(11) DEFAULT NULL,
  `entitlement_count_inheritance` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `role_index_cscore` (`composite_score`),
  KEY `FKF99E0B5128E03F44` (`bundle`),
  KEY `FKF99E0B51486634B7` (`assigned_scope`),
  KEY `FKF99E0B51A5FB1B1` (`owner`),
  KEY `SPT_IDXAEACA8FDA84AB44E` (`assigned_scope_path`(255)),
  CONSTRAINT `FKF99E0B5128E03F44` FOREIGN KEY (`bundle`) REFERENCES `spt_bundle` (`id`),
  CONSTRAINT `FKF99E0B51486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKF99E0B51A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_role_index`
--

LOCK TABLES `spt_role_index` WRITE;
/*!40000 ALTER TABLE `spt_role_index` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_role_index` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:23
