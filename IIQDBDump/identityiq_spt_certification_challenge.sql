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
-- Table structure for table `spt_certification_challenge`
--

DROP TABLE IF EXISTS `spt_certification_challenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_certification_challenge` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `email_template` varchar(255) DEFAULT NULL,
  `comments` longtext,
  `expiration` datetime(6) DEFAULT NULL,
  `work_item` varchar(255) DEFAULT NULL,
  `completion_state` varchar(255) DEFAULT NULL,
  `completion_comments` longtext,
  `completion_user` varchar(128) DEFAULT NULL,
  `actor_name` varchar(128) DEFAULT NULL,
  `actor_display_name` varchar(128) DEFAULT NULL,
  `acting_work_item` varchar(255) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `challenged` bit(1) DEFAULT NULL,
  `decision` varchar(255) DEFAULT NULL,
  `decision_comments` longtext,
  `decider_name` varchar(255) DEFAULT NULL,
  `challenge_decision_expired` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCFF77896486634B7` (`assigned_scope`),
  KEY `FKCFF77896A5FB1B1` (`owner`),
  KEY `SPT_IDXFB512F02CB48A798` (`assigned_scope_path`(255)),
  CONSTRAINT `FKCFF77896486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKCFF77896A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_certification_challenge`
--

LOCK TABLES `spt_certification_challenge` WRITE;
/*!40000 ALTER TABLE `spt_certification_challenge` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_certification_challenge` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:31
