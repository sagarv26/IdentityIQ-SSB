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
-- Table structure for table `spt_profile`
--

DROP TABLE IF EXISTS `spt_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_profile` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `bundle_id` varchar(32) DEFAULT NULL,
  `disabled` bit(1) DEFAULT NULL,
  `account_type` varchar(128) DEFAULT NULL,
  `application` varchar(32) DEFAULT NULL,
  `attributes` longtext,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6BFE472139D71460` (`application`),
  KEY `FK6BFE472122D068BA` (`bundle_id`),
  KEY `FK6BFE4721486634B7` (`assigned_scope`),
  KEY `FK6BFE4721A5FB1B1` (`owner`),
  KEY `spt_profile_assignedscopepath` (`assigned_scope_path`(255)),
  CONSTRAINT `FK6BFE472122D068BA` FOREIGN KEY (`bundle_id`) REFERENCES `spt_bundle` (`id`),
  CONSTRAINT `FK6BFE472139D71460` FOREIGN KEY (`application`) REFERENCES `spt_application` (`id`),
  CONSTRAINT `FK6BFE4721486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK6BFE4721A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_profile`
--

LOCK TABLES `spt_profile` WRITE;
/*!40000 ALTER TABLE `spt_profile` DISABLE KEYS */;
INSERT INTO `spt_profile` VALUES ('a9fe0bbd92d11bbd8192d1b7a6ae00ca',1730095326894,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd92d11bbd8192d1b7a6ae00c9',_binary '\0',NULL,'a9fe0bbd90c41bed8190d46ece020648',' ',0),('a9fe0bbd92d11bbd8192d1b7a6ae00cb',1730095326894,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd92d11bbd8192d1b7a6ae00c9',_binary '\0',NULL,'a9fe0bbd90c41bed8190d45e9a7705ef',' ',1),('a9fedb6691bb1afa8191bbc7d30e00b9',1725432320782,1727934913000,NULL,NULL,NULL,NULL,NULL,'a9fedb6691bb1afa8191bbc4a07b00b0',_binary '\0',NULL,'a9fe0bbd90c41bed8190d46ece020648',' ',0),('a9fedb66924113af819250f259c5031b',1727934912965,NULL,NULL,NULL,NULL,NULL,NULL,'a9fedb6691bb1afa8191bbc4a07b00b0',_binary '\0',NULL,'a9fe0bbd90c41bed8190d45e9a7705ef',' ',1),('a9fedb66924113af8192569b193c0465',1728029858108,NULL,NULL,NULL,NULL,NULL,NULL,'a9fedb66924113af8192569b193c0464',_binary '\0',NULL,'a9fe0bbd90c41bed8190d45e9a7705ef',' ',0),('a9fedb66924113af8192569b193d0466',1728029858109,NULL,NULL,NULL,NULL,NULL,NULL,'a9fedb66924113af8192569b193c0464',_binary '\0',NULL,'a9fe0bbd90c41bed8190d46ece020648',' ',1);
/*!40000 ALTER TABLE `spt_profile` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:26
