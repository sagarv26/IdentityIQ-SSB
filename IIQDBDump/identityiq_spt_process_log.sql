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
-- Table structure for table `spt_process_log`
--

DROP TABLE IF EXISTS `spt_process_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_process_log` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `process_name` varchar(128) DEFAULT NULL,
  `case_id` varchar(128) DEFAULT NULL,
  `workflow_case_name` varchar(450) DEFAULT NULL,
  `launcher` varchar(128) DEFAULT NULL,
  `case_status` varchar(128) DEFAULT NULL,
  `step_name` varchar(128) DEFAULT NULL,
  `approval_name` varchar(128) DEFAULT NULL,
  `owner_name` varchar(128) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `step_duration` int(11) DEFAULT NULL,
  `escalations` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `spt_process_log_approval_name` (`approval_name`),
  KEY `spt_process_log_process_name` (`process_name`),
  KEY `spt_process_log_owner_name` (`owner_name`),
  KEY `spt_process_log_step_name` (`step_name`),
  KEY `spt_process_log_case_id` (`case_id`),
  KEY `spt_process_log_case_status` (`case_status`),
  KEY `spt_process_log_wf_case_name` (`workflow_case_name`(255)),
  KEY `FK28FB62EC486634B7` (`assigned_scope`),
  KEY `FK28FB62ECA5FB1B1` (`owner`),
  KEY `SPT_IDXE2B6FD83726D2C4` (`assigned_scope_path`(255)),
  CONSTRAINT `FK28FB62EC486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK28FB62ECA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_process_log`
--

LOCK TABLES `spt_process_log` WRITE;
/*!40000 ALTER TABLE `spt_process_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_process_log` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:37
