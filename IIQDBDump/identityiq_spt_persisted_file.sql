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
-- Table structure for table `spt_persisted_file`
--

DROP TABLE IF EXISTS `spt_persisted_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_persisted_file` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(256) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `content_type` varchar(128) DEFAULT NULL,
  `content_length` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCEBAA850486634B7` (`assigned_scope`),
  KEY `FKCEBAA850A5FB1B1` (`owner`),
  KEY `SPT_IDXA511A43C73CC4C8C` (`assigned_scope_path`(255)),
  CONSTRAINT `FKCEBAA850486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKCEBAA850A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_persisted_file`
--

LOCK TABLES `spt_persisted_file` WRITE;
/*!40000 ALTER TABLE `spt_persisted_file` DISABLE KEYS */;
INSERT INTO `spt_persisted_file` VALUES ('4028ab10671c24ba01671c4c189c0007',1542368008348,1542368008969,NULL,NULL,NULL,'App Report.csv',NULL,'application/CSV',0),('4028ab10671c24ba01671c4c32f2000a',1542368015090,NULL,NULL,NULL,NULL,'App Report.pdf',NULL,'application/pdf',0),('4028ab1068cc9d780168daec6e4c0096',1549861154380,1549861155030,NULL,NULL,NULL,'App Report.csv',NULL,'application/CSV',0),('4028ab1068cc9d780168daec8d3d0099',1549861162301,NULL,NULL,NULL,NULL,'App Report.pdf',NULL,'application/pdf',0),('a9fe0bbd92b71d768192b7b694550020',1729659049045,1729659049758,NULL,NULL,NULL,'App status report 1.csv',NULL,'application/CSV',0),('a9fe0bbd92b71d768192b7b6a6a60023',1729659053734,NULL,NULL,NULL,NULL,'App status report 1.pdf',NULL,'application/pdf',0),('a9fe0bbd92b71d768192b7f2906b0090',1729662980204,1729662980320,NULL,NULL,NULL,'uncorrelated acct.csv',NULL,'application/CSV',0),('a9fe0bbd92b71d768192b7f29d8b0093',1729662983563,NULL,NULL,NULL,NULL,'uncorrelated acct.pdf',NULL,'application/pdf',0),('a9fe0bbd92b71d768192b885543a0177',1729672598586,1729672598768,NULL,NULL,NULL,'App report 1.csv',NULL,'application/CSV',0),('a9fe0bbd92b71d768192b8855feb017a',1729672601579,NULL,NULL,NULL,NULL,'App report 1.pdf',NULL,'application/pdf',0),('a9fe0bbd92b71d768192b88a3d9f0184',1729672920479,1729672920863,NULL,NULL,NULL,'App Report.csv',NULL,'application/CSV',0),('a9fe0bbd92b71d768192b88a4c7b0187',1729672924283,NULL,NULL,NULL,NULL,'App Report.pdf',NULL,'application/pdf',0),('a9fe0bbd92b71d768192bd19969b031f',1729749423772,NULL,NULL,NULL,NULL,'AR Detail 1.csv',NULL,'application/CSV',0),('a9fe0bbd92b71d768192bd2721100349',1729750311185,NULL,NULL,NULL,NULL,'AR Details 1.csv',NULL,'application/CSV',0),('a9fe0bbd92b71d768192bd9375a403d1',1729757410724,1729757411149,NULL,NULL,NULL,'Access Request Details 1.csv',NULL,'application/CSV',0),('a9fe0bbd92b71d768192bd93841b03d4',1729757414427,NULL,NULL,NULL,NULL,'Access Request Details 1.pdf',NULL,'application/pdf',0),('a9fe0bbd92b71d768192bd95ae8003d8',1729757556353,1729757556575,NULL,NULL,NULL,'Audit Detail Report 1.csv',NULL,'application/CSV',0),('a9fe0bbd92b71d768192bd95bc2e03db',1729757559854,NULL,NULL,NULL,NULL,'Audit Detail Report 1.pdf',NULL,'application/pdf',0),('a9fe0bbd92b71d768192c305705305f2',1729848766547,1729848766854,NULL,NULL,NULL,'Test User Details Report.csv',NULL,'application/CSV',0),('a9fe0bbd92b71d768192c3057d5e05f5',1729848769886,NULL,NULL,NULL,NULL,'Test User Details Report.pdf',NULL,'application/pdf',0),('a9fe0bbd939010d7819390417d61000a',1733292031330,1733292035296,NULL,NULL,NULL,'App Report.csv',NULL,'application/CSV',0),('a9fe0bbd939010d78193904230dc0013',1733292077276,NULL,NULL,NULL,NULL,'App Report.pdf',NULL,'application/pdf',0),('a9fe0bbd94ab18298194ab58c19b000b',1738041508251,1738041511042,NULL,NULL,NULL,'App Report.csv',NULL,'application/CSV',0),('a9fe0bbd94ab18298194ab5948630014',1738041542757,NULL,NULL,NULL,NULL,'App Report.pdf',NULL,'application/pdf',0),('a9fe0bbd957e1ec681957e5f61ae000b',1741581935022,NULL,NULL,NULL,NULL,'App Report.csv',NULL,'application/CSV',0),('a9fe0bbd957e1ec681957e5fe5a10014',1741581968801,NULL,NULL,NULL,NULL,'App Report.pdf',NULL,'application/pdf',0),('a9fe0bbd95cb121e8195cbc2f08a000d',1742880305290,NULL,NULL,NULL,NULL,'App Report.csv',NULL,'application/CSV',0),('a9fe0bbd95cb121e8195cbc34dc10015',1742880329153,NULL,NULL,NULL,NULL,'App Report.pdf',NULL,'application/pdf',0),('a9fe0bbd965b1be5819661cfaefd0332',1745397722901,NULL,NULL,NULL,NULL,'App Report.csv',NULL,'application/CSV',0),('a9fe0bbd965b1be5819662753ebc0335',1745408573116,NULL,NULL,NULL,NULL,'App Report.pdf',NULL,'application/pdf',0),('a9fe0bbd97a01e0b8197a0becb45000c',1750748547909,NULL,NULL,NULL,NULL,'App Report.csv',NULL,'application/CSV',0),('a9fe0bbd97a01e0b8197a0bf40010014',1750748577794,NULL,NULL,NULL,NULL,'App Report.pdf',NULL,'application/pdf',0),('a9fe0bbd97a01e0b8197ab42e0080954',1750924976149,1750924977254,NULL,NULL,NULL,'SupportDataCollector.2.2.0.zip',NULL,'application/zip',667772);
/*!40000 ALTER TABLE `spt_persisted_file` ENABLE KEYS */;
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
