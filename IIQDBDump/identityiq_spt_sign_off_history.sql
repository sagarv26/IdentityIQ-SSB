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
-- Table structure for table `spt_sign_off_history`
--

DROP TABLE IF EXISTS `spt_sign_off_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_sign_off_history` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `sign_date` bigint(20) DEFAULT NULL,
  `signer_id` varchar(128) DEFAULT NULL,
  `signer_name` varchar(128) DEFAULT NULL,
  `signer_display_name` varchar(128) DEFAULT NULL,
  `application` varchar(128) DEFAULT NULL,
  `account` varchar(128) DEFAULT NULL,
  `text` longtext,
  `electronic_sign` bit(1) DEFAULT NULL,
  `certification_id` varchar(32) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sign_off_history_signer_id` (`signer_id`),
  KEY `spt_sign_off_history_esig` (`electronic_sign`),
  KEY `FK2BDCCBCADB59193A` (`certification_id`),
  KEY `FK2BDCCBCA486634B7` (`assigned_scope`),
  KEY `FK2BDCCBCAA5FB1B1` (`owner`),
  KEY `SPT_IDXC439D3638206900` (`assigned_scope_path`(255)),
  CONSTRAINT `FK2BDCCBCA486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK2BDCCBCAA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK2BDCCBCADB59193A` FOREIGN KEY (`certification_id`) REFERENCES `spt_certification` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_sign_off_history`
--

LOCK TABLES `spt_sign_off_history` WRITE;
/*!40000 ALTER TABLE `spt_sign_off_history` DISABLE KEYS */;
INSERT INTO `spt_sign_off_history` VALUES ('a9fe0bbd92d11bbd8192d1cb74fe017e',1730096624894,NULL,NULL,NULL,NULL,1730096624841,'4028ab1063f427af0163f428d1ca0105','spadmin','The Administrator',NULL,NULL,NULL,_binary '\0','a9fe0bbd92d11bbd8192d1cb23230177',0),('a9fe0bbd92d11bbd8192dc73b012084d',1730275422226,NULL,NULL,NULL,NULL,1730275422163,'4028ab1063f427af0163f428d1ca0105','spadmin','The Administrator',NULL,NULL,NULL,_binary '\0','a9fe0bbd92d11bbd8192dc7211340817',0);
/*!40000 ALTER TABLE `spt_sign_off_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:29
