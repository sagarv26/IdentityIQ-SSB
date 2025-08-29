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
-- Table structure for table `spt_integration_config`
--

DROP TABLE IF EXISTS `spt_integration_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_integration_config` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(4000) DEFAULT NULL,
  `executor` varchar(255) DEFAULT NULL,
  `exec_style` varchar(255) DEFAULT NULL,
  `role_sync_style` varchar(255) DEFAULT NULL,
  `template` bit(1) DEFAULT NULL,
  `signature` longtext,
  `attributes` longtext,
  `plan_initializer` varchar(32) DEFAULT NULL,
  `resources` longtext,
  `application_id` varchar(32) DEFAULT NULL,
  `role_sync_filter` longtext,
  `container_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK12CC3B95907AB97A` (`application_id`),
  KEY `FK12CC3B95AAEC2008` (`plan_initializer`),
  KEY `FK12CC3B95FAA8585B` (`container_id`),
  KEY `FK12CC3B95486634B7` (`assigned_scope`),
  KEY `FK12CC3B95A5FB1B1` (`owner`),
  KEY `spt_integration_conf_created` (`created`),
  KEY `spt_integration_conf_modified` (`modified`),
  KEY `SPT_IDXABF0D041BEBD0BD6` (`assigned_scope_path`(255)),
  CONSTRAINT `FK12CC3B95486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK12CC3B95907AB97A` FOREIGN KEY (`application_id`) REFERENCES `spt_application` (`id`),
  CONSTRAINT `FK12CC3B95A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK12CC3B95AAEC2008` FOREIGN KEY (`plan_initializer`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FK12CC3B95FAA8585B` FOREIGN KEY (`container_id`) REFERENCES `spt_bundle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_integration_config`
--

LOCK TABLES `spt_integration_config` WRITE;
/*!40000 ALTER TABLE `spt_integration_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `spt_integration_config` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:30
