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
-- Table structure for table `spt_certifiers`
--

DROP TABLE IF EXISTS `spt_certifiers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_certifiers` (
  `certification_id` varchar(32) NOT NULL,
  `certifier` varchar(255) DEFAULT NULL,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`certification_id`,`idx`),
  KEY `FK784C89A6DB59193A` (`certification_id`),
  KEY `spt_certification_certifiers` (`certifier`),
  CONSTRAINT `FK784C89A6DB59193A` FOREIGN KEY (`certification_id`) REFERENCES `spt_certification` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_certifiers`
--

LOCK TABLES `spt_certifiers` WRITE;
/*!40000 ALTER TABLE `spt_certifiers` DISABLE KEYS */;
INSERT INTO `spt_certifiers` VALUES ('a9fe0bbd92d11bbd8192d1c8d1070153','2020007',0),('a9fe0bbd92d11bbd8192d1cd87bc01a7','2020007',0),('a9fe0bbd92d11bbd8192d6b3bdb50345','2020007',0),('a9fe0bbd92d11bbd8192d6b624b10363','2020007',0),('a9fe0bbd92d11bbd8192d6bd2fb0038c','2020007',0),('a9fe0bbd92d11bbd8192dc7211340817','2020007',0),('a9fe0bbd92d11bbd8192dc7b4aeb08b4','2020007',0),('a9fe0bbd94f8111b8194f8f341040011','2020007',0),('a9fe0bbd965b1be5819685c0e6a10c1d','2020007',0),('a9fe0bbd92d11bbd8192d1cb23230177','spadmin',0),('a9fe0bbd92d11bbd8192dc749a280851','spadmin',0),('a9fe0bbd92d11bbd8192dc74a9b30864','spadmin',0),('a9fe0bbd957e1ec68195838f575001f5','spadmin',0),('a9fe0bbd957e1ec6819583a2f0420253','spadmin',0),('a9fe0bbd957e1ec68195844f43e00317','spadmin',0);
/*!40000 ALTER TABLE `spt_certifiers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:56
