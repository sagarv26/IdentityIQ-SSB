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
-- Table structure for table `spt_arch_cert_item_apps`
--

DROP TABLE IF EXISTS `spt_arch_cert_item_apps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_arch_cert_item_apps` (
  `arch_cert_item_id` varchar(32) NOT NULL,
  `application_name` varchar(255) DEFAULT NULL,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`arch_cert_item_id`,`idx`),
  KEY `FKFBD89444D6D1B4E0` (`arch_cert_item_id`),
  KEY `spt_arch_cert_item_apps_name` (`application_name`),
  CONSTRAINT `FKFBD89444D6D1B4E0` FOREIGN KEY (`arch_cert_item_id`) REFERENCES `spt_archived_cert_item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_arch_cert_item_apps`
--

LOCK TABLES `spt_arch_cert_item_apps` WRITE;
/*!40000 ALTER TABLE `spt_arch_cert_item_apps` DISABLE KEYS */;
INSERT INTO `spt_arch_cert_item_apps` VALUES ('a9fe0bbd92d11bbd8192dc721770081d','Contractor System Employee',0),('a9fe0bbd92d11bbd8192dc7b4d1908b8','Contractor System Employee',0),('a9fe0bbd94f8111b8194f8f3a2830015','Contractor System Employee',0),('a9fe0bbd94f8111b8194f8f3a707001a','Contractor System Employee',0),('a9fe0bbd94f8111b8194f8f3b5500027','Contractor System Employee',0),('a9fe0bbd94f8111b8194f8f3b5500028','Contractor System Employee',0),('a9fe0bbd965b1be5819685c102ac0c20','Contractor System Employee',0),('a9fe0bbd965b1be5819685c10df90c24','Contractor System Employee',0),('a9fe0bbd965b1be5819685c11aa00c31','Contractor System Employee',0),('a9fe0bbd965b1be5819685c11afe0c32','Contractor System Employee',0),('a9fe0bbd92d11bbd8192dc721770081e','HR System',0),('a9fe0bbd92d11bbd8192dc7b4d1a08b9','HR System',0),('a9fe0bbd94f8111b8194f8f3a707001b','HR System',0),('a9fe0bbd965b1be5819685c10dfa0c25','HR System',0);
/*!40000 ALTER TABLE `spt_arch_cert_item_apps` ENABLE KEYS */;
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
