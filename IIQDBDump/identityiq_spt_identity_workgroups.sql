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
-- Table structure for table `spt_identity_workgroups`
--

DROP TABLE IF EXISTS `spt_identity_workgroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_identity_workgroups` (
  `identity_id` varchar(32) NOT NULL,
  `workgroup` varchar(32) NOT NULL,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`identity_id`,`idx`),
  KEY `FKFBDE3BBE56651F3A` (`identity_id`),
  KEY `FKFBDE3BBE457BB10C` (`workgroup`),
  CONSTRAINT `FKFBDE3BBE457BB10C` FOREIGN KEY (`workgroup`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FKFBDE3BBE56651F3A` FOREIGN KEY (`identity_id`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_identity_workgroups`
--

LOCK TABLES `spt_identity_workgroups` WRITE;
/*!40000 ALTER TABLE `spt_identity_workgroups` DISABLE KEYS */;
INSERT INTO `spt_identity_workgroups` VALUES ('a9fe0bbd90c41bed8190d472dc1d0658','a9fe0bbd92d11bbd8192dc7ed85008fd',0),('a9fe0bbd94f8111b8194f91f2270008e','a9fe0bbd957e12ac81957e9579700004',0),('a9fedb66920d105081920de9fced0020','a9fe0bbd957e12ac81957e9579700004',1),('a9fe0bbd94f8111b8194f91f2270008e','a9fe0bbd957e12ac81958444999a0005',1),('a9fedb66920d105081920de9fced0020','a9fe0bbd957e12ac81958444999a0005',2),('a9fedb66920d105081920de9fced0020','a9fe0bbd957e12ac819584449c9b0006',3),('a9fe0bbd90c41bed8190d472dd2a065c','a9fedb6691bb1afa8191bbcfba9000cf',0),('a9fedb66920d105081920de9fced0020','a9fedb66924113af819256a3264b0476',0),('a9fedb66920d105081920ee9f8d4022c','a9fedb66924113af819256a3264b0476',0),('a9fe0bbd90c41bed8190d472da380654','a9fedb66924113af819256a426b00478',0);
/*!40000 ALTER TABLE `spt_identity_workgroups` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:23
