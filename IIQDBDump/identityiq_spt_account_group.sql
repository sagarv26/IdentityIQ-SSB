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
-- Table structure for table `spt_account_group`
--

DROP TABLE IF EXISTS `spt_account_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_account_group` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `native_identity` varchar(322) DEFAULT NULL,
  `reference_attribute` varchar(128) DEFAULT NULL,
  `member_attribute` varchar(128) DEFAULT NULL,
  `last_refresh` bigint(20) DEFAULT NULL,
  `last_target_aggregation` bigint(20) DEFAULT NULL,
  `uncorrelated` bit(1) DEFAULT NULL,
  `application` varchar(32) DEFAULT NULL,
  `attributes` longtext,
  `key1` varchar(128) DEFAULT NULL,
  `key2` varchar(128) DEFAULT NULL,
  `key3` varchar(128) DEFAULT NULL,
  `key4` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `spt_actgroup_attr` (`reference_attribute`),
  KEY `spt_actgroup_key4_ci` (`key4`),
  KEY `spt_actgroup_name_csi` (`name`),
  KEY `spt_actgroup_key2_ci` (`key2`),
  KEY `spt_actgroup_key3_ci` (`key3`),
  KEY `spt_actgroup_key1_ci` (`key1`),
  KEY `spt_actgroup_lastAggregation` (`last_target_aggregation`),
  KEY `spt_actgroup_native_ci` (`native_identity`(255)),
  KEY `FK54D3916539D71460` (`application`),
  KEY `FK54D39165486634B7` (`assigned_scope`),
  KEY `FK54D39165A5FB1B1` (`owner`),
  KEY `SPT_IDXC71C52111BEFE376` (`assigned_scope_path`(255)),
  CONSTRAINT `FK54D3916539D71460` FOREIGN KEY (`application`) REFERENCES `spt_application` (`id`),
  CONSTRAINT `FK54D39165486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK54D39165A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_account_group`
--

LOCK TABLES `spt_account_group` WRITE;
/*!40000 ALTER TABLE `spt_account_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_account_group` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:34
