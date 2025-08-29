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
-- Table structure for table `spt_dashboard_reference`
--

DROP TABLE IF EXISTS `spt_dashboard_reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_dashboard_reference` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `identity_dashboard_id` varchar(32) DEFAULT NULL,
  `content_id` varchar(32) DEFAULT NULL,
  `region` varchar(128) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `minimized` bit(1) DEFAULT NULL,
  `arguments` longtext,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK45E944D82D6026` (`content_id`),
  KEY `FK45E944D8486634B7` (`assigned_scope`),
  KEY `FK45E944D8A5FB1B1` (`owner`),
  KEY `FK45E944D8878775BD` (`identity_dashboard_id`),
  KEY `SPT_IDX2D52EC448BE739C` (`assigned_scope_path`(255)),
  CONSTRAINT `FK45E944D82D6026` FOREIGN KEY (`content_id`) REFERENCES `spt_dashboard_content` (`id`),
  CONSTRAINT `FK45E944D8486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK45E944D8878775BD` FOREIGN KEY (`identity_dashboard_id`) REFERENCES `spt_identity_dashboard` (`id`),
  CONSTRAINT `FK45E944D8A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_dashboard_reference`
--

LOCK TABLES `spt_dashboard_reference` WRITE;
/*!40000 ALTER TABLE `spt_dashboard_reference` DISABLE KEYS */;
INSERT INTO `spt_dashboard_reference` VALUES ('297e8b3d78a6bfa20178a6c068200001',1617705330732,NULL,NULL,NULL,NULL,'4028ab1063f427af0163f428ce2100fd','4028ab1063f427af0163f428c95400ec','Medium1',1,_binary '\0',' ',0),('297e8b3d78a6bfa20178a6c068360002',1617705330742,NULL,NULL,NULL,NULL,'4028ab1063f427af0163f428ce2100fd','4028ab1063f427af0163f428c98d00ed','Medium2',2,_binary '\0',' ',1),('297e8b3d78a6bfa20178a6cc79fe000c',1617706121726,NULL,NULL,NULL,NULL,'4028ab1063fdfb6d0163fdfc41e00003','4028ab1063fdfb6d0163fdfc41300001','XtraLarge1',1,_binary '\0',' ',0),('297e8b3d78a6bfa20178a6cc7a7d000d',1617706121853,NULL,NULL,NULL,NULL,'4028ab1063fdfb6d0163fdfc42300005','4028ab1063f427af0163f428c95400ec','Medium1',1,_binary '\0',' ',0),('297e8b3d78a6bfa20178a6cc7a7e000e',1617706121854,NULL,NULL,NULL,NULL,'4028ab1063fdfb6d0163fdfc42300005','4028ab1063f427af0163f428c98d00ed','Medium2',2,_binary '\0',' ',1),('4028ab1063f427af0163f428ce2100fe',1528809704993,NULL,NULL,NULL,NULL,NULL,'4028ab1063f427af0163f428c95400ec','Medium1',1,_binary '\0',' ',NULL),('4028ab1063f427af0163f428ce2100ff',1528809704994,NULL,NULL,NULL,NULL,NULL,'4028ab1063f427af0163f428c98d00ed','Medium2',2,_binary '\0',' ',NULL),('4028ab1063fdfb6d0163fdfc41e00004',1528974557664,NULL,NULL,NULL,NULL,NULL,'4028ab1063fdfb6d0163fdfc41300001','XtraLarge1',1,_binary '\0',' ',NULL),('4028ab1063fdfb6d0163fdfc42300006',1528974557744,NULL,NULL,NULL,NULL,NULL,'4028ab1063f427af0163f428c95400ec','Medium1',1,_binary '\0',' ',NULL),('4028ab1063fdfb6d0163fdfc42320007',1528974557746,NULL,NULL,NULL,NULL,NULL,'4028ab1063f427af0163f428c98d00ed','Medium2',2,_binary '\0',' ',NULL);
/*!40000 ALTER TABLE `spt_dashboard_reference` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:20
