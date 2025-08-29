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
-- Table structure for table `spt_server`
--

DROP TABLE IF EXISTS `spt_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_server` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `heartbeat` bigint(20) DEFAULT NULL,
  `inactive` bit(1) DEFAULT NULL,
  `attributes` longtext,
  `extended1` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `spt_server_extended1_ci` (`extended1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_server`
--

LOCK TABLES `spt_server` WRITE;
/*!40000 ALTER TABLE `spt_server` DISABLE KEYS */;
INSERT INTO `spt_server` VALUES ('4028ab1063f42da50163f42dd5e80001',1528810034672,1752260525672,'DESKTOP-PNSAQP0',1752260525663,_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"applicationStatus\"/>\n    <entry key=\"cpuUsage\" value=\"7.53\"/>\n    <entry key=\"databaseResponseTime\" value=\"4\"/>\n    <entry key=\"lastStatisticUpdate\">\n      <value>\n        <Date>1752260260981</Date>\n      </value>\n    </entry>\n    <entry key=\"memoryUsage\" value=\"1337229968\"/>\n    <entry key=\"memoryUsagePercentage\" value=\"23.3\"/>\n    <entry key=\"moduleStatus\"/>\n    <entry key=\"requestServiceStarted\" value=\"true\"/>\n    <entry key=\"requestThreads\">\n      <value>\n        <Integer>0</Integer>\n      </value>\n    </entry>\n    <entry key=\"taskThreads\">\n      <value>\n        <Integer>0</Integer>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n',NULL);
/*!40000 ALTER TABLE `spt_server` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:45
