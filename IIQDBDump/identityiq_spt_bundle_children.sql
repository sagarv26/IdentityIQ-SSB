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
-- Table structure for table `spt_bundle_children`
--

DROP TABLE IF EXISTS `spt_bundle_children`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_bundle_children` (
  `bundle` varchar(32) NOT NULL,
  `child` varchar(32) NOT NULL,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`bundle`,`idx`),
  KEY `FK5D48969480A503DE` (`child`),
  KEY `FK5D48969428E03F44` (`bundle`),
  CONSTRAINT `FK5D48969428E03F44` FOREIGN KEY (`bundle`) REFERENCES `spt_bundle` (`id`),
  CONSTRAINT `FK5D48969480A503DE` FOREIGN KEY (`child`) REFERENCES `spt_bundle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_bundle_children`
--

LOCK TABLES `spt_bundle_children` WRITE;
/*!40000 ALTER TABLE `spt_bundle_children` DISABLE KEYS */;
INSERT INTO `spt_bundle_children` VALUES ('a9fedb6691bb1afa8191bbc4a07b00b0','a9fedb6691bb1afa8191bbc938ce00bb',0),('a9fedb66924113af8192569b193c0464','a9fedb6691bb1afa8191bbc938ce00bb',0),('a9fe0bbd92d11bbd8192d1b6e5b900c7','a9fedb6691bb1afa8191bbca3a2300c0',0),('a9fedb6691bb1afa8191bbc9f7a800be','a9fedb6691bb1afa8191bbca3a2300c0',0),('a9fedb66924113af8192569770f10460','a9fedb66924113af8192569506840458',0);
/*!40000 ALTER TABLE `spt_bundle_children` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:08
