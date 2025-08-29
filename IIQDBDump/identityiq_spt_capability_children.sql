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
-- Table structure for table `spt_capability_children`
--

DROP TABLE IF EXISTS `spt_capability_children`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_capability_children` (
  `capability_id` varchar(32) NOT NULL,
  `child_id` varchar(32) NOT NULL,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`capability_id`,`idx`),
  KEY `FKC7A8EEBEA526F8FA` (`capability_id`),
  KEY `FKC7A8EEBEC4BCFA76` (`child_id`),
  CONSTRAINT `FKC7A8EEBEA526F8FA` FOREIGN KEY (`capability_id`) REFERENCES `spt_capability` (`id`),
  CONSTRAINT `FKC7A8EEBEC4BCFA76` FOREIGN KEY (`child_id`) REFERENCES `spt_capability` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_capability_children`
--

LOCK TABLES `spt_capability_children` WRITE;
/*!40000 ALTER TABLE `spt_capability_children` DISABLE KEYS */;
INSERT INTO `spt_capability_children` VALUES ('4028ab1063f427af0163f4289d8000d1','4028ab1063f427af0163f42898d200c5',0),('4028ab1063f427af0163f4289a7d00cb','4028ab1063f427af0163f428996b00c7',2),('4028ab1063f427af0163f4289a7d00cb','4028ab1063f427af0163f42899de00c9',1),('4028ab1063f427af0163f4289a7d00cb','4028ab1063f427af0163f4289a2a00ca',0),('4028ab1063f427af0163f4289e9b00d5','4028ab1063f427af0163f4289e5800d4',0);
/*!40000 ALTER TABLE `spt_capability_children` ENABLE KEYS */;
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
