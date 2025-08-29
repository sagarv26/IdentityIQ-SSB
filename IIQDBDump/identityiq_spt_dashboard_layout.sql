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
-- Table structure for table `spt_dashboard_layout`
--

DROP TABLE IF EXISTS `spt_dashboard_layout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_dashboard_layout` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `regions` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK9914A8BD486634B7` (`assigned_scope`),
  KEY `FK9914A8BDA5FB1B1` (`owner`),
  KEY `SPT_IDXDE774369778BEC26` (`assigned_scope_path`(255)),
  CONSTRAINT `FK9914A8BD486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK9914A8BDA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_dashboard_layout`
--

LOCK TABLES `spt_dashboard_layout` WRITE;
/*!40000 ALTER TABLE `spt_dashboard_layout` DISABLE KEYS */;
INSERT INTO `spt_dashboard_layout` VALUES ('4028ab1063f427af0163f428c82100e8',1528809703457,1617705329032,NULL,NULL,NULL,'oneCol','OneCol','<List>\n  <String>XtraLarge1</String>\n</List>\n'),('4028ab1063f427af0163f428c85700e9',1528809703511,1617705329101,NULL,NULL,NULL,'twoColEvenTop','TwoColEvenTop','<List>\n  <String>XtraLarge1</String>\n  <String>Medium1</String>\n  <String>Medium2</String>\n</List>\n'),('4028ab1063f427af0163f428c88200ea',1528809703554,1617705329162,NULL,NULL,NULL,'twoColEvenBottom','TwoColEvenBottom','<List>\n  <String>Medium1</String>\n  <String>Medium2</String>\n  <String>XtraLarge1</String>\n</List>\n'),('4028ab1063f427af0163f428c90700eb',1528809703687,1617705329208,NULL,NULL,NULL,'twoColEven','TwoColEven','<List>\n  <String>Medium1</String>\n  <String>Medium2</String>\n</List>\n');
/*!40000 ALTER TABLE `spt_dashboard_layout` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:33
