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
-- Table structure for table `spt_correlation_config`
--

DROP TABLE IF EXISTS `spt_correlation_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_correlation_config` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `attribute_assignments` longtext,
  `direct_assignments` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK3A3DBC27486634B7` (`assigned_scope`),
  KEY `FK3A3DBC27A5FB1B1` (`owner`),
  KEY `SPT_IDXB52E1053EF6BCC7A` (`assigned_scope_path`(255)),
  CONSTRAINT `FK3A3DBC27486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK3A3DBC27A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_correlation_config`
--

LOCK TABLES `spt_correlation_config` WRITE;
/*!40000 ALTER TABLE `spt_correlation_config` DISABLE KEYS */;
INSERT INTO `spt_correlation_config` VALUES ('a9fe0bbd90c41bed8190d060238203bb',1721482879874,1739425273393,NULL,NULL,NULL,'Contractor Correlation',NULL,'<List>\n  <Filter operation=\"EQ\" property=\"employeenumber\" value=\"employeenumber\"/>\n</List>\n',' '),('a9fe0bbd90c41bed8190d0aa2c6c0478',1721487731820,1739425273548,NULL,NULL,NULL,'LDAP Correlation',NULL,'<List>\n  <Filter operation=\"EQ\" property=\"employeenumber\" value=\"employeeNumber\"/>\n</List>\n',' '),('a9fe0bbd90c41bed8190d4472dc805c0',1721548352968,1739425273453,NULL,NULL,NULL,'HR System Correlation',NULL,'<List>\n  <Filter operation=\"EQ\" property=\"employeenumber\" value=\"employeenumber\"/>\n</List>\n',' '),('a9fe0bbd940617a1819406981f580028',1735277420377,1735280262744,NULL,NULL,NULL,'AD',NULL,'<List>\n  <Filter operation=\"EQ\" property=\"employeenumber\" value=\"employeeNumber\"/>\n  <Filter operation=\"EQ\" property=\"uid\" value=\"sAMAccountName\"/>\n</List>\n',' '),('a9fe0bbd957e1ec6819583859c7e01ce',1741668326526,1741668380674,NULL,NULL,NULL,'PeopleSoft Correlation',NULL,'<List>\n  <Filter operation=\"EQ\" property=\"employeenumber\" value=\"employeenumber\"/>\n</List>\n',' '),('a9fe0bbd965b1be58196a49143c51933',1746517705670,NULL,NULL,NULL,NULL,'WebApp',NULL,'<List>\n  <Filter operation=\"EQ\" property=\"employeenumber\" value=\"employeenumber\"/>\n</List>\n',' '),('a9fedb66922c14e481922d5e422200b1',1727338005026,NULL,NULL,NULL,NULL,'JDBC Demo',NULL,' ',' '),('a9fedb66922c14e481922dc578270153',1727344769064,NULL,NULL,NULL,NULL,'DelimitedFile Correlation',NULL,' ',' ');
/*!40000 ALTER TABLE `spt_correlation_config` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:49
