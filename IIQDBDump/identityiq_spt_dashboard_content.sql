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
-- Table structure for table `spt_dashboard_content`
--

DROP TABLE IF EXISTS `spt_dashboard_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_dashboard_content` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `required` bit(1) DEFAULT NULL,
  `region_size` varchar(255) DEFAULT NULL,
  `source_task_id` varchar(128) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `parent` varchar(32) DEFAULT NULL,
  `arguments` longtext,
  `enabling_attributes` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `spt_dashboard_content_task` (`source_task_id`),
  KEY `spt_dashboard_type` (`type`),
  KEY `FKC4B33946486634B7` (`assigned_scope`),
  KEY `FKC4B33946A5FB1B1` (`owner`),
  KEY `FKC4B33946B513AA2F` (`parent`),
  KEY `SPT_IDX85C023B24A735CF8` (`assigned_scope_path`(255)),
  CONSTRAINT `FKC4B33946486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKC4B33946A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FKC4B33946B513AA2F` FOREIGN KEY (`parent`) REFERENCES `spt_dashboard_content` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_dashboard_content`
--

LOCK TABLES `spt_dashboard_content` WRITE;
/*!40000 ALTER TABLE `spt_dashboard_content` DISABLE KEYS */;
INSERT INTO `spt_dashboard_content` VALUES ('4028ab1063f427af0163f428c95400ec',1528809703764,1617705329290,NULL,NULL,NULL,'Inbox','dash_description_inbox','dash_title_inbox','dashboard/contentInbox.xhtml',_binary '\0','0',NULL,'My',NULL,' ',' '),('4028ab1063f427af0163f428c98d00ed',1528809703821,1617705329371,NULL,NULL,NULL,'Outbox','dash_description_outbox','dash_title_outbox','dashboard/contentOutbox.xhtml',_binary '\0','0',NULL,'My',NULL,' ',' '),('4028ab1063f427af0163f428c9b500ee',1528809703861,1617705329430,NULL,NULL,NULL,'Risk Score Chart','dash_description_risk_score_chart','dash_title_risk_score_chart','dashboard/chart/chartRiskScores.xhtml',_binary '\0','0',NULL,'Compliance',NULL,'<Attributes>\n  <Map>\n    <entry key=\"chartDateRange\" value=\"Current\"/>\n    <entry key=\"chartIs3d\" value=\"true\"/>\n    <entry key=\"chartOrientation\" value=\"vertical\"/>\n    <entry key=\"chartType\" value=\"bar\"/>\n  </Map>\n</Attributes>\n',' '),('4028ab1063f427af0163f428ca6500ef',1528809704039,1617705329498,NULL,NULL,NULL,'Application Risk Score Chart','dash_description_app_risk_score_chart','dash_title_app_risk_score_chart','dashboard/chart/chartApplicationRiskScores.xhtml',_binary '\0','0',NULL,'Compliance',NULL,'<Attributes>\n  <Map>\n    <entry key=\"chartDateRange\" value=\"Current\"/>\n    <entry key=\"chartIs3d\" value=\"true\"/>\n    <entry key=\"chartOrientation\" value=\"vertical\"/>\n    <entry key=\"chartType\" value=\"pie\"/>\n  </Map>\n</Attributes>\n',' '),('4028ab1063f427af0163f428caff00f0',1528809704191,1617705329563,NULL,NULL,NULL,'Policy Violation Chart','dash_description_policy_violation_chart','dash_title_policy_violation_chart','dashboard/chart/chartPolicyViolations.xhtml',_binary '\0','0',NULL,'Compliance',NULL,'<Attributes>\n  <Map>\n    <entry key=\"chartDateRange\" value=\"Current\"/>\n    <entry key=\"chartIs3d\" value=\"true\"/>\n    <entry key=\"chartOrientation\" value=\"vertical\"/>\n    <entry key=\"chartType\" value=\"bar\"/>\n  </Map>\n</Attributes>\n',' '),('4028ab1063f427af0163f428cb9100f1',1528809704338,1617705329681,NULL,NULL,NULL,'My Certifications','dash_description_my_cert','dash_title_my_cert','dashboard/contentMyCertifications.xhtml',_binary '\0','0',NULL,'My',NULL,'<Attributes>\n  <Map>\n    <entry key=\"chartIs3d\" value=\"true\"/>\n    <entry key=\"chartOrientation\" value=\"horizontal\"/>\n    <entry key=\"chartType\" value=\"pie\"/>\n    <entry key=\"viewDetails\" value=\"false\"/>\n  </Map>\n</Attributes>\n',' '),('4028ab1063f427af0163f428cbc100f2',1528809704385,1617705329761,NULL,NULL,NULL,'Access Review Owner Status','dash_description_cert_owner_status','dash_title_cert_owner_status','dashboard/contentOwnerCerts.xhtml',_binary '\0','0',NULL,'My',NULL,' ',' '),('4028ab1063f427af0163f428cbeb00f3',1528809704427,1617705329858,NULL,NULL,NULL,'Access Review Owner Status By Group','dash_description_cert_owner_group_status','dash_title_cert_owner_group_status','dashboard/contentOwnerGroupCerts.xhtml',_binary '\0','0',NULL,'Compliance',NULL,' ',' '),('4028ab1063f427af0163f428cc2a00f4',1528809704491,1617705329918,NULL,NULL,NULL,'Application Access Review Status','dash_description_app_cert_status','dash_title_app_cert_status','dashboard/contentApplicationCertPercent.xhtml',_binary '\0','0',NULL,'Compliance',NULL,' ',' '),('4028ab1063f427af0163f428cc6300f5',1528809704548,1617705330011,NULL,NULL,NULL,'Group Access Review Status','dash_description_group_cert_status','dash_title_group_cert_status','dashboard/contentGroupCerts.xhtml',_binary '\0','0',NULL,'Compliance',NULL,' ',' '),('4028ab1063f427af0163f428cc9e00f6',1528809704607,1617705330073,NULL,NULL,NULL,'Policy Violation Status','dash_description_policy_violation_status','dash_title_policy_violation_status','dashboard/contentPolicyViolations.xhtml',_binary '\0','0',NULL,'My',NULL,' ',' '),('4028ab1063f427af0163f428ccd000f7',1528809704656,1617705330162,NULL,NULL,NULL,'Access Review Completion Chart','dash_description_cert_comp_chart','dash_title_cert_comp_chart','dashboard/chart/chartCertificationCompletion.xhtml',_binary '\0','0',NULL,'Compliance',NULL,'<Attributes>\n  <Map>\n    <entry key=\"chartDateRange\" value=\"Current\"/>\n    <entry key=\"chartIs3d\" value=\"true\"/>\n    <entry key=\"chartOrientation\" value=\"vertical\"/>\n    <entry key=\"chartType\" value=\"bar\"/>\n  </Map>\n</Attributes>\n',' '),('4028ab1063f427af0163f428cd0700f8',1528809704711,1617705330253,NULL,NULL,NULL,'Access Review Decision Chart','dash_description_cert_decision_chart','dash_title_cert_decision_chart','dashboard/chart/chartCertificationDecisions.xhtml',_binary '\0','0',NULL,'Compliance',NULL,'<Attributes>\n  <Map>\n    <entry key=\"chartDateRange\" value=\"Current\"/>\n    <entry key=\"chartIs3d\" value=\"true\"/>\n    <entry key=\"chartOrientation\" value=\"vertical\"/>\n    <entry key=\"chartType\" value=\"bar\"/>\n  </Map>\n</Attributes>\n',' '),('4028ab1063f427af0163f428cd4200f9',1528809704770,1617705330341,NULL,NULL,NULL,'Application Status','dash_description_app_status','dash_title_app_status','dashboard/contentApplicationStatus.xhtml',_binary '\0','0',NULL,'Compliance',NULL,' ',' '),('4028ab1063f427af0163f428cd7200fa',1528809704818,1617705330485,NULL,NULL,NULL,'Signoff Status','dash_description_signoff_status','dash_title_signoff_status','dashboard/contentSignoffStatus.xhtml',_binary '\0','0',NULL,'Compliance',NULL,' ',' '),('4028ab1063f427af0163f428cd9e00fb',1528809704862,1617705330546,NULL,NULL,NULL,'Access Review Completion Status','dash_description_cert_completion_status','dash_title_cert_completion_status','dashboard/contentCertificationCompletionStatus.xhtml',_binary '\0','0',NULL,'Compliance',NULL,' ',' '),('4028ab1063f427af0163f428cdc600fc',1528809704902,1617784528726,NULL,NULL,NULL,'Online Tutorials','dash_description_online_tutorials','dash_title_online_tutorials','dashboard/contentFlashBasedTraining.xhtml',_binary '\0','0',NULL,'My',NULL,'<Attributes>\n  <Map>\n    <entry key=\"tutorials\">\n      <value>\n        <List>\n          <Map>\n            <entry key=\"description_key\" value=\"help_tutorial_access_review_identity_description\"/>\n            <entry key=\"page\" value=\"manager_access_review/manager_access_review/index.html\"/>\n            <entry key=\"title_key\" value=\"help_tutorial_access_review_identity\"/>\n          </Map>\n          <Map>\n            <entry key=\"description_key\" value=\"help_tutorial_access_review_entitlement_description\"/>\n            <entry key=\"page\" value=\"entitlement_owner_access_review/entitlement_owner_access_review/index.html\"/>\n            <entry key=\"title_key\" value=\"help_tutorial_access_review_entitlement\"/>\n          </Map>\n          <Map>\n            <entry key=\"description_key\" value=\"help_tutorial_home_page_overview_description\"/>\n            <entry key=\"page\" value=\"home_page/home_page/index.html\"/>\n            <entry key=\"title_key\" value=\"help_tutorial_home_page_overview\"/>\n          </Map>\n          <Map>\n            <entry key=\"description_key\" value=\"help_tutorial_lifecycle_manager_overview_description\"/>\n            <entry key=\"page\" value=\"lcm_overview/lcm_overview/index.html\"/>\n            <entry key=\"title_key\" value=\"help_tutorial_lifecycle_manager_overview\"/>\n          </Map>\n          <Map>\n            <entry key=\"description_key\" value=\"help_tutorial_individual_access_request_description\"/>\n            <entry key=\"page\" value=\"access_request/access_request/index.html\"/>\n            <entry key=\"title_key\" value=\"help_tutorial_individual_access_request\"/>\n          </Map>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n',' '),('4028ab1063f427af0163f428cf8c0101',1528809705356,1617705331007,NULL,NULL,NULL,'Certifications','dash_description_certs','dash_title_certs','dashboard/contentCertGroups.xhtml',_binary '\0','2',NULL,'Compliance',NULL,' ',' '),('4028ab1063fdfb6d0163fdfc41300001',1528974557501,1617706121495,NULL,NULL,NULL,'Access Request Status','dash_desc_lcm_access_request','dash_title_lcm_access_request','dashboard/contentAccessRequest.xhtml',_binary '\0','1',NULL,'Lifecycle',NULL,' ',' '),('4028ab1063fdfb6d0163fdfc417f0002',1528974557567,1617706121659,NULL,NULL,NULL,'My Access Requests','dash_desc_lcm_my_requests','dash_title_lcm_my_requests','dashboard/contentMyRequests.xhtml',_binary '\0','1',NULL,'My',NULL,' ',' ');
/*!40000 ALTER TABLE `spt_dashboard_content` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:04
