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
-- Table structure for table `spt_batch_request_item`
--

DROP TABLE IF EXISTS `spt_batch_request_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_batch_request_item` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `request_data` varchar(4000) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `message` varchar(4000) DEFAULT NULL,
  `error_message` longtext,
  `result` varchar(255) DEFAULT NULL,
  `identity_request_id` varchar(255) DEFAULT NULL,
  `target_identity_id` varchar(255) DEFAULT NULL,
  `batch_request_id` varchar(32) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9118CB302C200325` (`batch_request_id`),
  KEY `FK9118CB30486634B7` (`assigned_scope`),
  KEY `FK9118CB30A5FB1B1` (`owner`),
  KEY `SPT_IDX6200CF1CF3199A4C` (`assigned_scope_path`(255)),
  CONSTRAINT `FK9118CB302C200325` FOREIGN KEY (`batch_request_id`) REFERENCES `spt_batch_request` (`id`),
  CONSTRAINT `FK9118CB30486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK9118CB30A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_batch_request_item`
--

LOCK TABLES `spt_batch_request_item` WRITE;
/*!40000 ALTER TABLE `spt_batch_request_item` DISABLE KEYS */;
INSERT INTO `spt_batch_request_item` VALUES ('a9fe0bbd92d11bbd8192dbe5c4a00703',1730266121376,1730266128246,NULL,NULL,NULL,'ModifyIdentity,2020005,Donna.Guertin@sweinc.com','Finished',NULL,' ','Success',NULL,'a9fe0bbd90c41bed8190d472da380654','a9fe0bbd92d11bbd8192dbe5c04c0701',0),('a9fe0bbd92d11bbd8192dbe5c4b70704',1730266121399,1730266132429,NULL,NULL,NULL,'ModifyIdentity,2020007,Lisa.Desrochers@sweinc.com','Finished',NULL,' ','Success',NULL,'a9fe0bbd90c41bed8190d472dc1d0658','a9fe0bbd92d11bbd8192dbe5c04c0701',1),('a9fe0bbd92d11bbd8192dbe6699e071a',1730266163614,1730266175587,NULL,NULL,NULL,'ModifyIdentity,2020005,Donna.Guertin@sweinc.com','Finished',NULL,' ','Success','0000000070','a9fe0bbd90c41bed8190d472da380654','a9fe0bbd92d11bbd8192dbe665ab0718',1),('a9fe0bbd92d11bbd8192dbe669a4071b',1730266163620,1730266168976,NULL,NULL,NULL,'ModifyIdentity,2020007,Lisa.Desrochers@sweinc.com','Finished',NULL,' ','Success','0000000069','a9fe0bbd90c41bed8190d472dc1d0658','a9fe0bbd92d11bbd8192dbe665ab0718',0),('a9fe0bbd92d11bbd8192dc7c296308d4',1730275977571,1730275994260,NULL,NULL,NULL,'ModifyIdentity,2020010,Alan.McRae@sweinc.com','Finished',NULL,' ','Success','0000000072','a9fe0bbd90c41bed8190d472ddcd065e','a9fe0bbd92d11bbd8192dc7c241708d2',1),('a9fe0bbd92d11bbd8192dc7c296c08d5',1730275977580,1730275983946,NULL,NULL,NULL,'ModifyIdentity,2020008,Tom.Rubyck@sweinc.com','Finished',NULL,' ','Success','0000000071','a9fe0bbd90c41bed8190d472dcb6065a','a9fe0bbd92d11bbd8192dc7c241708d2',0);
/*!40000 ALTER TABLE `spt_batch_request_item` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:07
