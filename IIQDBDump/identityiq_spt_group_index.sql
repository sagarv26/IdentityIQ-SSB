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
-- Table structure for table `spt_group_index`
--

DROP TABLE IF EXISTS `spt_group_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_group_index` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `incomplete` bit(1) DEFAULT NULL,
  `composite_score` int(11) DEFAULT NULL,
  `attributes` longtext,
  `items` longtext,
  `business_role_score` int(11) DEFAULT NULL,
  `raw_business_role_score` int(11) DEFAULT NULL,
  `entitlement_score` int(11) DEFAULT NULL,
  `raw_entitlement_score` int(11) DEFAULT NULL,
  `policy_score` int(11) DEFAULT NULL,
  `raw_policy_score` int(11) DEFAULT NULL,
  `certification_score` int(11) DEFAULT NULL,
  `total_violations` int(11) DEFAULT NULL,
  `total_remediations` int(11) DEFAULT NULL,
  `total_delegations` int(11) DEFAULT NULL,
  `total_mitigations` int(11) DEFAULT NULL,
  `total_approvals` int(11) DEFAULT NULL,
  `definition` varchar(32) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `member_count` int(11) DEFAULT NULL,
  `band_count` int(11) DEFAULT NULL,
  `band1` int(11) DEFAULT NULL,
  `band2` int(11) DEFAULT NULL,
  `band3` int(11) DEFAULT NULL,
  `band4` int(11) DEFAULT NULL,
  `band5` int(11) DEFAULT NULL,
  `band6` int(11) DEFAULT NULL,
  `band7` int(11) DEFAULT NULL,
  `band8` int(11) DEFAULT NULL,
  `band9` int(11) DEFAULT NULL,
  `band10` int(11) DEFAULT NULL,
  `certifications_due` int(11) DEFAULT NULL,
  `certifications_on_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `group_index_cscore` (`composite_score`),
  KEY `FK5E03A88AF7729445` (`definition`),
  KEY `FK5E03A88A486634B7` (`assigned_scope`),
  KEY `FK5E03A88AA5FB1B1` (`owner`),
  KEY `SPT_IDXECB4C9F64AB87280` (`assigned_scope_path`(255)),
  CONSTRAINT `FK5E03A88A486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK5E03A88AA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK5E03A88AF7729445` FOREIGN KEY (`definition`) REFERENCES `spt_group_definition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_group_index`
--

LOCK TABLES `spt_group_index` WRITE;
/*!40000 ALTER TABLE `spt_group_index` DISABLE KEYS */;
INSERT INTO `spt_group_index` VALUES ('a9fe0bbd92b71d768192c303e4b005e7',1729848665264,NULL,NULL,NULL,NULL,_binary '\0',0,' ',' ',0,0,0,0,0,0,0,0,0,0,0,0,'a9fe0bbd92b71d768192c302956505d1','No Test group',3,3,3,0,0,0,0,0,0,0,0,0,0,0),('a9fe0bbd92b71d768192c303e85905e8',1729848666201,NULL,NULL,NULL,NULL,_binary '\0',27,' ',' ',0,0,0,0,0,0,111,0,0,0,0,0,'a9fe0bbd92b71d768192c30295f205d2','Contractor',9,3,9,0,0,0,0,0,0,0,0,0,0,0),('a9fe0bbd92b71d768192c303e8f805e9',1729848666360,NULL,NULL,NULL,NULL,_binary '\0',25,' ',' ',0,0,0,0,0,0,100,0,0,0,0,0,'a9fe0bbd92b71d768192c303e14105e1','false',10,3,10,0,0,0,0,0,0,0,0,0,0,0),('a9fe0bbd92b71d768192c303e9c505ea',1729848666565,NULL,NULL,NULL,NULL,_binary '\0',0,' ',' ',0,0,0,0,0,0,0,0,0,0,0,0,'a9fe0bbd92b71d768192c303e17605e2','true',2,3,2,0,0,0,0,0,0,0,0,0,0,0),('a9fe0bbd92b71d768192c303e9f705eb',1729848666615,NULL,NULL,NULL,NULL,_binary '\0',20,' ',' ',0,0,0,0,0,0,83,0,0,0,0,0,'a9fe0bbd92b71d768192c303e20c05e3','No Test role group',12,3,12,0,0,0,0,0,0,0,0,0,0,0),('a9fe0bbd92b71d768192c303ea3b05ec',1729848666683,NULL,NULL,NULL,NULL,_binary '\0',0,' ',' ',0,0,0,0,0,0,0,0,0,0,0,0,'a9fe0bbd92b71d768192c303e28e05e4','No Test manager group',3,3,3,0,0,0,0,0,0,0,0,0,0,0),('a9fe0bbd92b71d768192c303ea8f05ed',1729848666768,NULL,NULL,NULL,NULL,_binary '\0',31,' ',' ',0,0,0,0,0,0,125,0,0,0,0,0,'a9fe0bbd92b71d768192c303e32105e5','2020007',8,3,8,0,0,0,0,0,0,0,0,0,0,0),('a9fe0bbd92b71d768192c303eabd05ee',1729848666813,NULL,NULL,NULL,NULL,_binary '\0',0,' ',' ',0,0,0,0,0,0,0,0,0,0,0,0,'a9fe0bbd92b71d768192c303e37205e6','rdra000',1,3,1,0,0,0,0,0,0,0,0,0,0,0),('a9fe0bbd92b71d768192c303eb5805ef',1729848666968,NULL,NULL,NULL,NULL,_binary '\0',35,' ',' ',0,0,0,0,0,0,142,0,0,0,0,0,'a9fedb66924113af81927aa765141390','Active Contractors',7,3,7,0,0,0,0,0,0,0,0,0,0,0);
/*!40000 ALTER TABLE `spt_group_index` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:21
