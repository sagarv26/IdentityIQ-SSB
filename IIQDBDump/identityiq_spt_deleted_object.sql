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
-- Table structure for table `spt_deleted_object`
--

DROP TABLE IF EXISTS `spt_deleted_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_deleted_object` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `uuid` varchar(128) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `native_identity` varchar(322) NOT NULL,
  `last_refresh` bigint(20) DEFAULT NULL,
  `object_type` varchar(128) DEFAULT NULL,
  `application` varchar(32) DEFAULT NULL,
  `attributes` longtext,
  PRIMARY KEY (`id`),
  KEY `spt_delObj_lastRefresh` (`last_refresh`),
  KEY `spt_delObj_nativeIdentity_ci` (`native_identity`(255)),
  KEY `spt_delObj_objectType_ci` (`object_type`),
  KEY `spt_delObj_name_ci` (`name`),
  KEY `FKA08C7DAD39D71460` (`application`),
  KEY `FKA08C7DAD486634B7` (`assigned_scope`),
  KEY `FKA08C7DADA5FB1B1` (`owner`),
  KEY `spt_uuidcompositedelobj` (`application`,`uuid`),
  KEY `spt_appidcompositedelobj` (`application`,`native_identity`(223)),
  KEY `SPT_IDX7EDDBC591F6A3A06` (`assigned_scope_path`(255)),
  CONSTRAINT `FKA08C7DAD39D71460` FOREIGN KEY (`application`) REFERENCES `spt_application` (`id`),
  CONSTRAINT `FKA08C7DAD486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKA08C7DADA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_deleted_object`
--

LOCK TABLES `spt_deleted_object` WRITE;
/*!40000 ALTER TABLE `spt_deleted_object` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_deleted_object` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:17
