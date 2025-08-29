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
-- Table structure for table `spt_authentication_question`
--

DROP TABLE IF EXISTS `spt_authentication_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_authentication_question` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `question` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE3609F45486634B7` (`assigned_scope`),
  KEY `FKE3609F45A5FB1B1` (`owner`),
  KEY `SPT_IDX4875A7F12BD64736` (`assigned_scope_path`(255)),
  CONSTRAINT `FKE3609F45486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKE3609F45A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_authentication_question`
--

LOCK TABLES `spt_authentication_question` WRITE;
/*!40000 ALTER TABLE `spt_authentication_question` DISABLE KEYS */;
INSERT INTO `spt_authentication_question` VALUES ('4028ab1063fdfb6d0163fdfc49430008',1528974559556,1617706123173,NULL,NULL,NULL,'auth_question_mothers_maiden_name'),('4028ab1063fdfb6d0163fdfc49690009',1528974559593,1617706123236,NULL,NULL,NULL,'auth_question_favorite_color'),('4028ab1063fdfb6d0163fdfc498d000a',1528974559629,1617706123286,NULL,NULL,NULL,'auth_question_first_street'),('4028ab1063fdfb6d0163fdfc49af000b',1528974559664,1617706123342,NULL,NULL,NULL,'auth_question_favorite_pets_name'),('4028ab1063fdfb6d0163fdfc49e0000c',1528974559712,1617706123396,NULL,NULL,NULL,'auth_question_childhood_best_friend'),('4028ab1063fdfb6d0163fdfc4a0b000d',1528974559755,1617706123447,NULL,NULL,NULL,'auth_question_maternal_grandmother_firstname'),('4028ab1063fdfb6d0163fdfc4a3a000e',1528974559802,1617706123494,NULL,NULL,NULL,'auth_question_favorite_author');
/*!40000 ALTER TABLE `spt_authentication_question` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:44
