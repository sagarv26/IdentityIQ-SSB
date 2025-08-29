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
-- Table structure for table `spt_widget`
--

DROP TABLE IF EXISTS `spt_widget`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_widget` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `title` varchar(128) DEFAULT NULL,
  `selector` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK1F6E0DCC486634B7` (`assigned_scope`),
  KEY `FK1F6E0DCCA5FB1B1` (`owner`),
  KEY `spt_widget_assignedscopepath` (`assigned_scope_path`(255)),
  CONSTRAINT `FK1F6E0DCC486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK1F6E0DCCA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_widget`
--

LOCK TABLES `spt_widget` WRITE;
/*!40000 ALTER TABLE `spt_widget` DISABLE KEYS */;
INSERT INTO `spt_widget` VALUES ('4028ab1063f427af0163f428eee7014d',1528809713384,1617705341161,NULL,NULL,NULL,'Violations','widget_violations',' '),('4028ab1063f427af0163f428ef10014e',1528809713424,1617705341245,NULL,NULL,NULL,'MyAccessReviews','widget_my_access_reviews',' '),('4028ab1063f427af0163f428ef3c014f',1528809713468,1617705341324,NULL,NULL,NULL,'CertificationCampaigns','widget_certification_campaigns','<IdentitySelector>\n  <MatchExpression>\n    <MatchTerm name=\"rights\" value=\"CertificationCampaignsWidget\"/>\n  </MatchExpression>\n</IdentitySelector>\n'),('4028ab1063f427af0163f428efca0150',1528809713610,1617705341370,NULL,NULL,NULL,'TopFiveRisks','widget_top_five_risks','<IdentitySelector>\n  <MatchExpression>\n    <MatchTerm name=\"rights\" value=\"ViewRiskScoreChart\"/>\n    <MatchTerm name=\"rights\" value=\"ViewApplicationRiskScoreChart\"/>\n  </MatchExpression>\n</IdentitySelector>\n'),('4028ab1063fdfb6d0163fdfc52740031',1528974561908,1617706126785,NULL,NULL,NULL,'Approvals','widget_approvals',' '),('4028ab1063fdfb6d0163fdfc52d50032',1528974562005,1617706126834,NULL,NULL,NULL,'Forms','widget_forms',' '),('4028ab1063fdfb6d0163fdfc53130033',1528974562067,1617706126875,NULL,NULL,NULL,'DirectReports','widget_direct_reports','<IdentitySelector>\n  <MatchExpression>\n    <MatchTerm name=\"managerStatus\" value=\"true\"/>\n  </MatchExpression>\n</IdentitySelector>\n');
/*!40000 ALTER TABLE `spt_widget` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:24
