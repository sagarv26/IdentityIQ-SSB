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
-- Table structure for table `spt_work_item_config`
--

DROP TABLE IF EXISTS `spt_work_item_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_work_item_config` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `description_template` varchar(1024) DEFAULT NULL,
  `disabled` bit(1) DEFAULT NULL,
  `no_work_item` bit(1) DEFAULT NULL,
  `parent` varchar(32) DEFAULT NULL,
  `owner_rule` varchar(32) DEFAULT NULL,
  `hours_till_escalation` int(11) DEFAULT NULL,
  `hours_between_reminders` int(11) DEFAULT NULL,
  `max_reminders` int(11) DEFAULT NULL,
  `notification_email` varchar(32) DEFAULT NULL,
  `reminder_email` varchar(32) DEFAULT NULL,
  `escalation_email` varchar(32) DEFAULT NULL,
  `escalation_rule` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC86AF748F36F8B85` (`reminder_email`),
  KEY `FKC86AF748C98DBFA2` (`escalation_rule`),
  KEY `FKC86AF748FDF11A44` (`owner_rule`),
  KEY `FKC86AF7487EAF553E` (`notification_email`),
  KEY `FKC86AF74884EC4F68` (`escalation_email`),
  KEY `FKC86AF748486634B7` (`assigned_scope`),
  KEY `FKC86AF748A5FB1B1` (`owner`),
  KEY `FKC86AF7482E3B7910` (`parent`),
  KEY `SPT_IDXB999253482041C7C` (`assigned_scope_path`(255)),
  CONSTRAINT `FKC86AF7482E3B7910` FOREIGN KEY (`parent`) REFERENCES `spt_work_item_config` (`id`),
  CONSTRAINT `FKC86AF748486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKC86AF7487EAF553E` FOREIGN KEY (`notification_email`) REFERENCES `spt_email_template` (`id`),
  CONSTRAINT `FKC86AF74884EC4F68` FOREIGN KEY (`escalation_email`) REFERENCES `spt_email_template` (`id`),
  CONSTRAINT `FKC86AF748A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FKC86AF748C98DBFA2` FOREIGN KEY (`escalation_rule`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FKC86AF748F36F8B85` FOREIGN KEY (`reminder_email`) REFERENCES `spt_email_template` (`id`),
  CONSTRAINT `FKC86AF748FDF11A44` FOREIGN KEY (`owner_rule`) REFERENCES `spt_rule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_work_item_config`
--

LOCK TABLES `spt_work_item_config` WRITE;
/*!40000 ALTER TABLE `spt_work_item_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_work_item_config` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:41
