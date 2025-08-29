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
-- Table structure for table `spt_certification_groups`
--

DROP TABLE IF EXISTS `spt_certification_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_certification_groups` (
  `certification_id` varchar(32) NOT NULL,
  `group_id` varchar(32) NOT NULL,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`certification_id`,`idx`),
  KEY `FK248E8281F6578B00` (`group_id`),
  KEY `FK248E8281DB59193A` (`certification_id`),
  CONSTRAINT `FK248E8281DB59193A` FOREIGN KEY (`certification_id`) REFERENCES `spt_certification` (`id`),
  CONSTRAINT `FK248E8281F6578B00` FOREIGN KEY (`group_id`) REFERENCES `spt_certification_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_certification_groups`
--

LOCK TABLES `spt_certification_groups` WRITE;
/*!40000 ALTER TABLE `spt_certification_groups` DISABLE KEYS */;
INSERT INTO `spt_certification_groups` VALUES ('a9fe0bbd92d11bbd8192d1c8d1070153','a9fe0bbd92d11bbd8192d1c8d0420152',0),('a9fe0bbd92d11bbd8192d1cb23230177','a9fe0bbd92d11bbd8192d1cb211f0176',0),('a9fe0bbd92d11bbd8192d1cd87bc01a7','a9fe0bbd92d11bbd8192d1cd86dc01a6',0),('a9fe0bbd92d11bbd8192d6b3bdb50345','a9fe0bbd92d11bbd8192d6b3b12c0344',0),('a9fe0bbd92d11bbd8192d6b624b10363','a9fe0bbd92d11bbd8192d6b601a30353',0),('a9fe0bbd92d11bbd8192d6bd2fb0038c','a9fe0bbd92d11bbd8192d6bd2e05038b',0),('a9fe0bbd92d11bbd8192dc7211340817','a9fe0bbd92d11bbd8192dc7210120816',0),('a9fe0bbd92d11bbd8192dc749a280851','a9fe0bbd92d11bbd8192dc7498d00850',0),('a9fe0bbd92d11bbd8192dc74a9b30864','a9fe0bbd92d11bbd8192dc7498d00850',0),('a9fe0bbd92d11bbd8192dc7b4aeb08b4','a9fe0bbd92d11bbd8192dc7b4a5908b3',0),('a9fe0bbd94f8111b8194f8f341040011','a9fe0bbd94f8111b8194f8f2f2cf0010',0),('a9fe0bbd957e1ec68195838f575001f5','a9fe0bbd957e1ec68195838f538e01f4',0),('a9fe0bbd957e1ec6819583a2f0420253','a9fe0bbd957e1ec6819583a2ee0a0252',0),('a9fe0bbd957e1ec68195844f43e00317','a9fe0bbd957e1ec68195844f41800316',0),('a9fe0bbd965b1be5819685c0e6a10c1d','a9fe0bbd965b1be5819685c0e00d0c1c',0);
/*!40000 ALTER TABLE `spt_certification_groups` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:36
