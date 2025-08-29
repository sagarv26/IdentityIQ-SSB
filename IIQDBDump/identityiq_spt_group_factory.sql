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
-- Table structure for table `spt_group_factory`
--

DROP TABLE IF EXISTS `spt_group_factory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_group_factory` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `factory_attribute` varchar(255) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `last_refresh` bigint(20) DEFAULT NULL,
  `group_owner_rule` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK36D2A2C252F9C404` (`group_owner_rule`),
  KEY `FK36D2A2C2486634B7` (`assigned_scope`),
  KEY `FK36D2A2C2A5FB1B1` (`owner`),
  KEY `SPT_IDXCEBEA62E59148F0` (`assigned_scope_path`(255)),
  CONSTRAINT `FK36D2A2C2486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK36D2A2C252F9C404` FOREIGN KEY (`group_owner_rule`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FK36D2A2C2A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_group_factory`
--

LOCK TABLES `spt_group_factory` WRITE;
/*!40000 ALTER TABLE `spt_group_factory` DISABLE KEYS */;
INSERT INTO `spt_group_factory` VALUES ('a9fe0bbd92b71d768192c3020ab105cf',1729848543921,1729848664332,NULL,NULL,NULL,'Test group','','employeeType',_binary '',1729848664330,NULL),('a9fe0bbd92b71d768192c303357105d7',1729848620401,1729848664503,NULL,NULL,NULL,'Test Inactive group','Test Inactive group','inactive',_binary '',1729848664502,NULL),('a9fe0bbd92b71d768192c30368b305de',1729848633523,1729848664645,NULL,NULL,NULL,'Test role group','Test role group','assignedRoles',_binary '',1729848664644,NULL),('a9fe0bbd92b71d768192c303a22305df',1729848648228,1729848664991,NULL,NULL,NULL,'Test manager group','Test manager group','manager',_binary '',1729848664990,NULL);
/*!40000 ALTER TABLE `spt_group_factory` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:59
