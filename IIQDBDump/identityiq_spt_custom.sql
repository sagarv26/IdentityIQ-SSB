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
-- Table structure for table `spt_custom`
--

DROP TABLE IF EXISTS `spt_custom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_custom` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `attributes` longtext,
  PRIMARY KEY (`id`),
  KEY `spt_custom_name_csi` (`name`),
  KEY `FKFDFD3EF9486634B7` (`assigned_scope`),
  KEY `FKFDFD3EF9A5FB1B1` (`owner`),
  KEY `spt_custom_assignedscopepath` (`assigned_scope_path`(255)),
  CONSTRAINT `FKFDFD3EF9486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKFDFD3EF9A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_custom`
--

LOCK TABLES `spt_custom` WRITE;
/*!40000 ALTER TABLE `spt_custom` DISABLE KEYS */;
INSERT INTO `spt_custom` VALUES ('a9fe0bbd90c41bed8190d451226305d5',1721549005427,1745393007451,NULL,NULL,NULL,'UID Custom Object',NULL,'<Attributes>\n  <Map>\n    <entry key=\"prefix\" value=\"aaa\"/>\n    <entry key=\"suffix\" value=\"0005\"/>\n  </Map>\n</Attributes>\n'),('a9fe0bbd92f5112a81930566313d06be',1730962403645,1739425273683,NULL,NULL,NULL,'Application Custom Object',NULL,'<Attributes>\n  <Map>\n    <entry key=\"ApplicationList\">\n      <value>\n        <List>\n          <String>LDAP</String>\n          <String>HR System</String>\n          <String>Contractor System Employee</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n');
/*!40000 ALTER TABLE `spt_custom` ENABLE KEYS */;
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
