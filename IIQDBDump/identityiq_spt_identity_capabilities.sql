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
-- Table structure for table `spt_identity_capabilities`
--

DROP TABLE IF EXISTS `spt_identity_capabilities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_identity_capabilities` (
  `identity_id` varchar(32) NOT NULL,
  `capability_id` varchar(32) NOT NULL,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`identity_id`,`idx`),
  KEY `FK2258790F56651F3A` (`identity_id`),
  KEY `FK2258790FA526F8FA` (`capability_id`),
  CONSTRAINT `FK2258790F56651F3A` FOREIGN KEY (`identity_id`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK2258790FA526F8FA` FOREIGN KEY (`capability_id`) REFERENCES `spt_capability` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_identity_capabilities`
--

LOCK TABLES `spt_identity_capabilities` WRITE;
/*!40000 ALTER TABLE `spt_identity_capabilities` DISABLE KEYS */;
INSERT INTO `spt_identity_capabilities` VALUES ('a9fe0bbd90c41bed8190d472dd2a065c','297e8b3d78ab76ff0178ab7796200006',0),('4028ab1063f427af0163f428d1ca0105','4028ab1063f427af0163f428956200c1',0),('a9fedb66924113af819256a3264b0476','4028ab1063f427af0163f428992700c6',0),('a9fe0bbd90c41bed8190d472dc1d0658','4028ab1063f427af0163f4289e5800d4',0),('a9fe0bbd92d11bbd8192dc7ed85008fd','4028ab1063fdfb6d0163fdfc81070056',0);
/*!40000 ALTER TABLE `spt_identity_capabilities` ENABLE KEYS */;
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
