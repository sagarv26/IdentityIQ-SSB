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
-- Table structure for table `spt_profile_constraints`
--

DROP TABLE IF EXISTS `spt_profile_constraints`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_profile_constraints` (
  `profile` varchar(32) NOT NULL,
  `elt` longtext,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`profile`,`idx`),
  KEY `FKEFD7A218B236FD12` (`profile`),
  CONSTRAINT `FKEFD7A218B236FD12` FOREIGN KEY (`profile`) REFERENCES `spt_profile` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_profile_constraints`
--

LOCK TABLES `spt_profile_constraints` WRITE;
/*!40000 ALTER TABLE `spt_profile_constraints` DISABLE KEYS */;
INSERT INTO `spt_profile_constraints` VALUES ('a9fe0bbd92d11bbd8192d1b7a6ae00ca','Group.containsAll({\"User\"})',0),('a9fe0bbd92d11bbd8192d1b7a6ae00cb','roles == \"User\"',0),('a9fedb6691bb1afa8191bbc7d30e00b9','Group.containsAll({\"Admin\"})',0),('a9fedb66924113af819250f259c5031b','roles == \"Admin\"',0),('a9fedb66924113af8192569b193c0465','roles == \"Developer\"',0),('a9fedb66924113af8192569b193d0466','Group.containsAll({\"Developer\"})',0);
/*!40000 ALTER TABLE `spt_profile_constraints` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:53
