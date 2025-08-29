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
-- Table structure for table `spt_scorecard`
--

DROP TABLE IF EXISTS `spt_scorecard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_scorecard` (
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
  `identity_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `identity_scorecard_cscore` (`composite_score`),
  KEY `FK2062601A56651F3A` (`identity_id`),
  KEY `FK2062601A486634B7` (`assigned_scope`),
  KEY `FK2062601AA5FB1B1` (`owner`),
  KEY `SPT_IDX8F4ABD86AFAD1DA0` (`assigned_scope_path`(255)),
  CONSTRAINT `FK2062601A486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK2062601A56651F3A` FOREIGN KEY (`identity_id`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK2062601AA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_scorecard`
--

LOCK TABLES `spt_scorecard` WRITE;
/*!40000 ALTER TABLE `spt_scorecard` DISABLE KEYS */;
INSERT INTO `spt_scorecard` VALUES ('a9fe0bbd7af6130b817b100829f607db',1628061575670,1730112649553,NULL,NULL,NULL,_binary '\0',0,' ',' ',0,0,0,0,0,0,0,0,0,0,0,0,'4028ab1063f427af0163f428d1ca0105'),('a9fe0bbd90c41bed8190d474eae00664',1721551350496,1730266142632,NULL,NULL,NULL,_binary '\0',0,' ',' ',0,0,0,0,0,0,0,0,0,0,0,0,'a9fe0bbd90c41bed8190d472da380654'),('a9fe0bbd90c41bed8190d474ebf20665',1721551350770,1730180939528,NULL,NULL,NULL,_binary '\0',0,' ',' ',0,0,0,0,0,0,0,0,0,0,0,0,'a9fe0bbd90c41bed8190d472dba30656'),('a9fe0bbd90c41bed8190d474ec8d0666',1721551350925,1730266148755,NULL,NULL,NULL,_binary '\0',0,' ',' ',0,0,0,0,0,0,0,0,0,0,0,0,'a9fe0bbd90c41bed8190d472dc1d0658'),('a9fe0bbd90c41bed8190d474ed670667',1721551351143,1721564602822,NULL,NULL,NULL,_binary '\0',0,' ',' ',0,0,0,0,0,0,0,0,0,0,0,0,'a9fe0bbd90c41bed8190d472dcb6065a'),('a9fe0bbd90c41bed8190d474edfa0668',1721551351290,1722151730087,NULL,NULL,NULL,_binary '\0',0,' ',' ',0,0,0,0,0,0,0,0,0,0,0,0,'a9fe0bbd90c41bed8190d472dd2a065c'),('a9fe0bbd90c41bed8190d474ee8d0669',1721551351437,NULL,NULL,NULL,NULL,_binary '\0',0,NULL,' ',0,0,0,0,0,0,0,0,0,0,0,0,'a9fe0bbd90c41bed8190d472ddcd065e'),('a9fedb6691bb1afa8191bbcd7adf00ce',1725432691423,1730112674314,NULL,NULL,NULL,_binary '\0',250,' ','<List>\n  <ScoreItem compositePercentage=\"100\" displayName=\"score_def_certificationAge_name\" score=\"1000\" scorePercentage=\"100\" type=\"score_def_certificationAge_shortname\">\n    <TargetMessage>\n      <Message key=\"cert_scorer_target_id_not_certified\" type=\"Info\"/>\n    </TargetMessage>\n  </ScoreItem>\n</List>\n',0,0,1,1,0,0,1000,0,0,0,0,0,'a9fe0bbd90c41bed8190d472dd2a065c'),('ff8080816403081601640dfd8f320504',1529243078450,NULL,NULL,NULL,NULL,_binary '\0',0,NULL,' ',0,0,0,0,0,0,0,0,0,0,0,0,'4028ab1063f427af0163f428d1ca0105');
/*!40000 ALTER TABLE `spt_scorecard` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:43
