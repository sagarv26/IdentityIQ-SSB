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
-- Table structure for table `spt_request`
--

DROP TABLE IF EXISTS `spt_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_request` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `stack` longtext,
  `attributes` longtext,
  `launcher` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `launched` bigint(20) DEFAULT NULL,
  `progress` varchar(255) DEFAULT NULL,
  `percent_complete` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `messages` longtext,
  `completed` bigint(20) DEFAULT NULL,
  `expiration` bigint(20) DEFAULT NULL,
  `name` varchar(450) DEFAULT NULL,
  `definition` varchar(32) DEFAULT NULL,
  `task_result` varchar(32) DEFAULT NULL,
  `phase` int(11) DEFAULT NULL,
  `dependent_phase` int(11) DEFAULT NULL,
  `next_launch` bigint(20) DEFAULT NULL,
  `retry_count` int(11) DEFAULT NULL,
  `retry_interval` int(11) DEFAULT NULL,
  `string1` varchar(2048) DEFAULT NULL,
  `completion_status` varchar(255) DEFAULT NULL,
  `live` bit(1) DEFAULT b'0',
  `notification_needed` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `spt_request_name` (`name`(255)),
  KEY `spt_request_expiration` (`expiration`),
  KEY `spt_request_compl_status` (`completion_status`),
  KEY `spt_request_phase` (`phase`),
  KEY `spt_request_nextLaunch` (`next_launch`),
  KEY `spt_request_depPhase` (`dependent_phase`),
  KEY `FKBFBEB0073EE0F059` (`task_result`),
  KEY `FKBFBEB007307D4C55` (`definition`),
  KEY `FKBFBEB007486634B7` (`assigned_scope`),
  KEY `FKBFBEB007A5FB1B1` (`owner`),
  KEY `spt_request_completed` (`completed`),
  KEY `spt_request_id_composite` (`completed`,`next_launch`,`launched`),
  KEY `spt_request_host` (`host`),
  KEY `spt_request_launched` (`launched`),
  KEY `spt_request_assignedscopepath` (`assigned_scope_path`(255)),
  KEY `spt_request_notif_needed` (`notification_needed`),
  CONSTRAINT `FKBFBEB007307D4C55` FOREIGN KEY (`definition`) REFERENCES `spt_request_definition` (`id`),
  CONSTRAINT `FKBFBEB0073EE0F059` FOREIGN KEY (`task_result`) REFERENCES `spt_task_result` (`id`),
  CONSTRAINT `FKBFBEB007486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKBFBEB007A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_request`
--

LOCK TABLES `spt_request` WRITE;
/*!40000 ALTER TABLE `spt_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_request` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:55
