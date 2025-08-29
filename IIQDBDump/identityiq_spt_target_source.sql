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
-- Table structure for table `spt_target_source`
--

DROP TABLE IF EXISTS `spt_target_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_target_source` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `collector` varchar(255) DEFAULT NULL,
  `last_refresh` bigint(20) DEFAULT NULL,
  `configuration` longtext,
  `correlation_rule` varchar(32) DEFAULT NULL,
  `creation_rule` varchar(32) DEFAULT NULL,
  `refresh_rule` varchar(32) DEFAULT NULL,
  `transformation_rule` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6F50201D9F8531C` (`refresh_rule`),
  KEY `FK6F502014FE65998` (`creation_rule`),
  KEY `FK6F50201B854BFAE` (`transformation_rule`),
  KEY `FK6F50201BE1EE0D5` (`correlation_rule`),
  KEY `FK6F50201486634B7` (`assigned_scope`),
  KEY `FK6F50201A5FB1B1` (`owner`),
  KEY `SPT_IDX719553AD788A55AE` (`assigned_scope_path`(255)),
  CONSTRAINT `FK6F50201486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK6F502014FE65998` FOREIGN KEY (`creation_rule`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FK6F50201A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK6F50201B854BFAE` FOREIGN KEY (`transformation_rule`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FK6F50201BE1EE0D5` FOREIGN KEY (`correlation_rule`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FK6F50201D9F8531C` FOREIGN KEY (`refresh_rule`) REFERENCES `spt_rule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_target_source`
--

LOCK TABLES `spt_target_source` WRITE;
/*!40000 ALTER TABLE `spt_target_source` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_target_source` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:42
