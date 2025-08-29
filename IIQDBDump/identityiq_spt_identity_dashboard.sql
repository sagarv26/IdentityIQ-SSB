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
-- Table structure for table `spt_identity_dashboard`
--

DROP TABLE IF EXISTS `spt_identity_dashboard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_identity_dashboard` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `identity_id` varchar(32) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `layout` varchar(32) DEFAULT NULL,
  `arguments` longtext,
  PRIMARY KEY (`id`),
  KEY `spt_identity_dashboard_type` (`type`),
  KEY `FK6732A7DB56651F3A` (`identity_id`),
  KEY `FK6732A7DB68DCB7C8` (`layout`),
  KEY `FK6732A7DB486634B7` (`assigned_scope`),
  KEY `FK6732A7DBA5FB1B1` (`owner`),
  KEY `SPT_IDX10AAF70777DD9EE2` (`assigned_scope_path`(255)),
  CONSTRAINT `FK6732A7DB486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK6732A7DB56651F3A` FOREIGN KEY (`identity_id`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK6732A7DB68DCB7C8` FOREIGN KEY (`layout`) REFERENCES `spt_dashboard_layout` (`id`),
  CONSTRAINT `FK6732A7DBA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_identity_dashboard`
--

LOCK TABLES `spt_identity_dashboard` WRITE;
/*!40000 ALTER TABLE `spt_identity_dashboard` DISABLE KEYS */;
INSERT INTO `spt_identity_dashboard` VALUES ('4028ab1063f427af0163f428ce2100fd',1528809704993,1617705330742,NULL,NULL,NULL,'dashboardDefault',NULL,NULL,'My','4028ab1063f427af0163f428c90700eb',' '),('4028ab1063f427af0163f428cf570100',1528809705304,1617705330924,NULL,NULL,NULL,'dashboardComplianceDefault',NULL,NULL,'Compliance','4028ab1063f427af0163f428c90700eb',' '),('4028ab1063fdfb6d0163fdfc41e00003',1528974557664,1617706121726,NULL,NULL,NULL,'dashboardLifecycleDefault',NULL,NULL,'Lifecycle','4028ab1063f427af0163f428c82100e8',' '),('4028ab1063fdfb6d0163fdfc42300005',1528974557744,1617706121854,NULL,NULL,NULL,'dashboardDefaultLCM',NULL,NULL,'My','4028ab1063f427af0163f428c85700e9',' ');
/*!40000 ALTER TABLE `spt_identity_dashboard` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:05
