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
-- Table structure for table `spt_scope`
--

DROP TABLE IF EXISTS `spt_scope`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_scope` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `display_name` varchar(128) DEFAULT NULL,
  `parent_id` varchar(32) DEFAULT NULL,
  `manually_created` bit(1) DEFAULT NULL,
  `dormant` bit(1) DEFAULT NULL,
  `path` varchar(450) DEFAULT NULL,
  `dirty` bit(1) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `scope_disp_name_ci` (`display_name`),
  KEY `scope_dirty` (`dirty`),
  KEY `scope_name_ci` (`name`),
  KEY `scope_path` (`path`(255)),
  KEY `FKAE33F9CC486634B7` (`assigned_scope`),
  KEY `FKAE33F9CC35F348E4` (`parent_id`),
  KEY `FKAE33F9CCA5FB1B1` (`owner`),
  KEY `spt_scope_assignedscopepath` (`assigned_scope_path`(255)),
  CONSTRAINT `FKAE33F9CC35F348E4` FOREIGN KEY (`parent_id`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKAE33F9CC486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKAE33F9CCA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_scope`
--

LOCK TABLES `spt_scope` WRITE;
/*!40000 ALTER TABLE `spt_scope` DISABLE KEYS */;
INSERT INTO `spt_scope` VALUES ('4028ab1068cc9d780168cd5aa9dd002d',1549633497565,1549633497566,NULL,'4028ab1068cc9d780168cd5aa9dd002d','4028ab1068cc9d780168cd5aa9dd002d','Test','Test',NULL,_binary '\0',_binary '\0','4028ab1068cc9d780168cd5aa9dd002d',_binary '\0',NULL);
/*!40000 ALTER TABLE `spt_scope` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:44
