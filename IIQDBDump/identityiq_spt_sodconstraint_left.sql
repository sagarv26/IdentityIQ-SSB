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
-- Table structure for table `spt_sodconstraint_left`
--

DROP TABLE IF EXISTS `spt_sodconstraint_left`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_sodconstraint_left` (
  `sodconstraint` varchar(32) NOT NULL,
  `businessrole` varchar(32) NOT NULL,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`sodconstraint`,`idx`),
  KEY `FKCCC28E29AEB984AA` (`sodconstraint`),
  KEY `FKCCC28E2952F56EF8` (`businessrole`),
  CONSTRAINT `FKCCC28E2952F56EF8` FOREIGN KEY (`businessrole`) REFERENCES `spt_bundle` (`id`),
  CONSTRAINT `FKCCC28E29AEB984AA` FOREIGN KEY (`sodconstraint`) REFERENCES `spt_sodconstraint` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_sodconstraint_left`
--

LOCK TABLES `spt_sodconstraint_left` WRITE;
/*!40000 ALTER TABLE `spt_sodconstraint_left` DISABLE KEYS */;
INSERT INTO `spt_sodconstraint_left` VALUES ('a9fe0bbd92d11bbd8192d1bc58f00112','a9fe0bbd92d11bbd8192d1b6e5b900c7',1),('a9fe0bbd92d11bbd8192d1bc58f00112','a9fedb6691bb1afa8191bbc9f7a800be',0);
/*!40000 ALTER TABLE `spt_sodconstraint_left` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:13
