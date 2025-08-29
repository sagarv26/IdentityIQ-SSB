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
-- Table structure for table `spt_identity_assigned_roles`
--

DROP TABLE IF EXISTS `spt_identity_assigned_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_identity_assigned_roles` (
  `identity_id` varchar(32) NOT NULL,
  `bundle` varchar(32) NOT NULL,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`identity_id`,`idx`),
  KEY `FK559F642556651F3A` (`identity_id`),
  KEY `FK559F642528E03F44` (`bundle`),
  CONSTRAINT `FK559F642528E03F44` FOREIGN KEY (`bundle`) REFERENCES `spt_bundle` (`id`),
  CONSTRAINT `FK559F642556651F3A` FOREIGN KEY (`identity_id`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_identity_assigned_roles`
--

LOCK TABLES `spt_identity_assigned_roles` WRITE;
/*!40000 ALTER TABLE `spt_identity_assigned_roles` DISABLE KEYS */;
INSERT INTO `spt_identity_assigned_roles` VALUES ('a9fe0bbd90c41bed8190d472dba30656','a9fe0bbd92d11bbd8192d1b6e5b900c7',0),('a9fe0bbd90c41bed8190d472dcb6065a','a9fe0bbd92d11bbd8192d1b6e5b900c7',0),('a9fe0bbd90c41bed8190d472dcb6065a','a9fedb6691bb1afa8191bbc9f7a800be',1),('a9fe0bbd957e1ec68195839457b7021e','a9fedb66924113af8192569770f10460',0),('a9fe0bbd957e1ec6819583a1072f0243','a9fedb66924113af8192569770f10460',0),('a9fe0bbd957e1ec6819583aa9109026f','a9fedb66924113af8192569770f10460',0);
/*!40000 ALTER TABLE `spt_identity_assigned_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:58
