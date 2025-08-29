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
-- Table structure for table `spt_group_definition`
--

DROP TABLE IF EXISTS `spt_group_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_group_definition` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `filter` longtext,
  `last_refresh` bigint(20) DEFAULT NULL,
  `null_group` bit(1) DEFAULT NULL,
  `indexed` bit(1) DEFAULT NULL,
  `private` bit(1) DEFAULT NULL,
  `factory` varchar(32) DEFAULT NULL,
  `group_index` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK21F3C89BFA54B4D5` (`factory`),
  KEY `FK21F3C89B486634B7` (`assigned_scope`),
  KEY `FK21F3C89B1CE09EE5` (`group_index`),
  KEY `FK21F3C89BA5FB1B1` (`owner`),
  KEY `SPT_IDX892D67C7AB213062` (`assigned_scope_path`(255)),
  CONSTRAINT `FK21F3C89B1CE09EE5` FOREIGN KEY (`group_index`) REFERENCES `spt_group_index` (`id`),
  CONSTRAINT `FK21F3C89B486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK21F3C89BA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK21F3C89BFA54B4D5` FOREIGN KEY (`factory`) REFERENCES `spt_group_factory` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_group_definition`
--

LOCK TABLES `spt_group_definition` WRITE;
/*!40000 ALTER TABLE `spt_group_definition` DISABLE KEYS */;
INSERT INTO `spt_group_definition` VALUES ('a9fe0bbd92b71d768192c302956505d1',1729848579429,1729848665265,NULL,NULL,NULL,'No Test group','Test group','<Filter operation=\"ISNULL\" property=\"employeeType\"/>\n',NULL,_binary '',_binary '',_binary '\0','a9fe0bbd92b71d768192c3020ab105cf','a9fe0bbd92b71d768192c303e4b005e7'),('a9fe0bbd92b71d768192c30295f205d2',1729848579571,1729848666202,NULL,NULL,NULL,'Contractor','Test group','<Filter operation=\"EQ\" property=\"employeeType\" value=\"Contractor\"/>\n',NULL,_binary '\0',_binary '',_binary '\0','a9fe0bbd92b71d768192c3020ab105cf','a9fe0bbd92b71d768192c303e85905e8'),('a9fe0bbd92b71d768192c303e14105e1',1729848664385,1729848666362,NULL,NULL,NULL,'false','Test Inactive group','<Filter operation=\"EQ\" property=\"inactive\">\n  <Value>\n    <Boolean></Boolean>\n  </Value>\n</Filter>\n',NULL,_binary '\0',_binary '',_binary '\0','a9fe0bbd92b71d768192c303357105d7','a9fe0bbd92b71d768192c303e8f805e9'),('a9fe0bbd92b71d768192c303e17605e2',1729848664438,1729848666566,NULL,NULL,NULL,'true','Test Inactive group','<Filter operation=\"EQ\" property=\"inactive\">\n  <Value>\n    <Boolean>true</Boolean>\n  </Value>\n</Filter>\n',NULL,_binary '\0',_binary '',_binary '\0','a9fe0bbd92b71d768192c303357105d7','a9fe0bbd92b71d768192c303e9c505ea'),('a9fe0bbd92b71d768192c303e20c05e3',1729848664588,1729848666616,NULL,NULL,NULL,'No Test role group','Test role group','<Filter operation=\"ISEMPTY\" property=\"assignedRoles\"/>\n',NULL,_binary '',_binary '',_binary '\0','a9fe0bbd92b71d768192c30368b305de','a9fe0bbd92b71d768192c303e9f705eb'),('a9fe0bbd92b71d768192c303e28e05e4',1729848664718,1729848666684,NULL,NULL,NULL,'No Test manager group','Test manager group','<Filter operation=\"ISNULL\" property=\"manager\"/>\n',NULL,_binary '',_binary '',_binary '\0','a9fe0bbd92b71d768192c303a22305df','a9fe0bbd92b71d768192c303ea3b05ec'),('a9fe0bbd92b71d768192c303e32105e5',1729848664865,1729848666770,NULL,NULL,NULL,'2020007','Test manager group','<Filter operation=\"EQ\" property=\"manager.name\" value=\"2020007\"/>\n',NULL,_binary '\0',_binary '',_binary '\0','a9fe0bbd92b71d768192c303a22305df','a9fe0bbd92b71d768192c303ea8f05ed'),('a9fe0bbd92b71d768192c303e37205e6',1729848664946,1729848666814,NULL,NULL,NULL,'rdra000','Test manager group','<Filter operation=\"EQ\" property=\"manager.name\" value=\"rdra000\"/>\n',NULL,_binary '\0',_binary '',_binary '\0','a9fe0bbd92b71d768192c303a22305df','a9fe0bbd92b71d768192c303eabd05ee'),('a9fedb66924113af81927aa765141390',1728634643732,1729848666969,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Active Contractors','','<CompositeFilter operation=\"AND\">\n  <Filter operation=\"EQ\" property=\"inactive\">\n    <Value>\n      <Boolean></Boolean>\n    </Value>\n  </Filter>\n  <Filter matchMode=\"START\" operation=\"LIKE\" property=\"employeeType\" value=\"Contractor\"/>\n</CompositeFilter>\n',NULL,_binary '\0',_binary '',_binary '',NULL,'a9fe0bbd92b71d768192c303eb5805ef');
/*!40000 ALTER TABLE `spt_group_definition` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:33
