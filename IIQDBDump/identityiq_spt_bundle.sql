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
-- Table structure for table `spt_bundle`
--

DROP TABLE IF EXISTS `spt_bundle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_bundle` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `extended1` varchar(450) DEFAULT NULL,
  `extended2` varchar(450) DEFAULT NULL,
  `extended3` varchar(450) DEFAULT NULL,
  `extended4` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `display_name` varchar(128) DEFAULT NULL,
  `displayable_name` varchar(128) DEFAULT NULL,
  `disabled` bit(1) DEFAULT NULL,
  `risk_score_weight` int(11) DEFAULT NULL,
  `activity_config` longtext,
  `mining_statistics` longtext,
  `attributes` longtext,
  `type` varchar(128) DEFAULT NULL,
  `join_rule` varchar(32) DEFAULT NULL,
  `pending_workflow` varchar(32) DEFAULT NULL,
  `role_index` varchar(32) DEFAULT NULL,
  `selector` longtext,
  `provisioning_plan` longtext,
  `templates` longtext,
  `provisioning_forms` longtext,
  `or_profiles` bit(1) DEFAULT NULL,
  `activation_date` bigint(20) DEFAULT NULL,
  `deactivation_date` bigint(20) DEFAULT NULL,
  `scorecard` varchar(32) DEFAULT NULL,
  `pending_delete` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `spt_bundle_type` (`type`),
  KEY `spt_bundle_dispname_ci` (`displayable_name`),
  KEY `spt_bundle_extended1_ci` (`extended1`(255)),
  KEY `spt_bundle_disabled` (`disabled`),
  KEY `FKFC45E40ABF46222D` (`join_rule`),
  KEY `FKFC45E40ABD5A5736` (`pending_workflow`),
  KEY `FKFC45E40ACC129F2E` (`scorecard`),
  KEY `FKFC45E40AF7616785` (`role_index`),
  KEY `FKFC45E40A486634B7` (`assigned_scope`),
  KEY `FKFC45E40AA5FB1B1` (`owner`),
  KEY `spt_bundle_modified` (`modified`),
  KEY `spt_bundle_created` (`created`),
  KEY `spt_bundle_assignedscopepath` (`assigned_scope_path`(255)),
  CONSTRAINT `FKFC45E40A486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKFC45E40AA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FKFC45E40ABD5A5736` FOREIGN KEY (`pending_workflow`) REFERENCES `spt_workflow_case` (`id`),
  CONSTRAINT `FKFC45E40ABF46222D` FOREIGN KEY (`join_rule`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FKFC45E40ACC129F2E` FOREIGN KEY (`scorecard`) REFERENCES `spt_role_scorecard` (`id`),
  CONSTRAINT `FKFC45E40AF7616785` FOREIGN KEY (`role_index`) REFERENCES `spt_role_index` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_bundle`
--

LOCK TABLES `spt_bundle` WRITE;
/*!40000 ALTER TABLE `spt_bundle` DISABLE KEYS */;
INSERT INTO `spt_bundle` VALUES ('a9fe0bbd92d11bbd8192d1b6e5b900c7',1730095277498,1730180939528,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,NULL,NULL,NULL,NULL,'User','User','User',_binary '\0',0,' ',' ','<Attributes>\n  <Map>\n    <entry key=\"accountSelectorRules\"/>\n    <entry key=\"allowDuplicateAccounts\" value=\"false\"/>\n    <entry key=\"allowMultipleAssignments\" value=\"false\"/>\n    <entry key=\"mergeTemplates\" value=\"false\"/>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','business',NULL,NULL,NULL,' ',' ',' ',' ',_binary '\0',NULL,NULL,NULL,_binary '\0'),('a9fe0bbd92d11bbd8192d1b7a6ae00c9',1730095326894,1744699892267,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,NULL,NULL,NULL,NULL,'User IT','User IT','User IT',_binary '\0',0,' ',' ','<Attributes>\n  <Map>\n    <entry key=\"accountSelectorRules\"/>\n    <entry key=\"allowDuplicateAccounts\" value=\"false\"/>\n    <entry key=\"allowMultipleAssignments\" value=\"false\"/>\n    <entry key=\"mergeTemplates\" value=\"false\"/>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','it',NULL,NULL,NULL,' ',' ',' ',' ',_binary '\0',NULL,NULL,NULL,_binary '\0'),('a9fe0bbd95ea1ee681963837a00f0fd1',1744699891849,NULL,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,NULL,NULL,NULL,NULL,'Contractor Birthright Role','Contractor Birthright Role','Contractor Birthright Role',_binary '\0',0,' ',' ','<Attributes>\n  <Map>\n    <entry key=\"accountSelectorRules\"/>\n    <entry key=\"allowDuplicateAccounts\" value=\"false\"/>\n    <entry key=\"allowMultipleAssignments\" value=\"false\"/>\n    <entry key=\"mergeTemplates\" value=\"false\"/>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','business',NULL,NULL,NULL,'<IdentitySelector>\n  <MatchExpression and=\"true\">\n    <MatchTerm name=\"employeeType\" type=\"Entitlement\" value=\"contractor\"/>\n    <MatchTerm name=\"status\" type=\"Entitlement\" value=\"Active\"/>\n  </MatchExpression>\n</IdentitySelector>\n',' ',' ',' ',_binary '\0',NULL,NULL,NULL,_binary '\0'),('a9fedb6691bb1afa8191bbc4a07b00b0',1725432111228,1730112674314,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,NULL,NULL,NULL,NULL,'Admin IT','Admin IT','Admin IT',_binary '\0',0,' ',' ','<Attributes>\n  <Map>\n    <entry key=\"accountSelectorRules\"/>\n    <entry key=\"allowDuplicateAccounts\" value=\"false\"/>\n    <entry key=\"allowMultipleAssignments\" value=\"false\"/>\n    <entry key=\"mergeTemplates\" value=\"true\"/>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','it',NULL,NULL,NULL,' ',' ',' ',' ',_binary '\0',NULL,NULL,NULL,_binary '\0'),('a9fedb6691bb1afa8191bbc938ce00bb',1725432412366,1728029858123,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,NULL,NULL,NULL,NULL,'IT Role','IT Role','IT Role',_binary '\0',0,' ',' ','<Attributes>\n  <Map>\n    <entry key=\"accountSelectorRules\"/>\n    <entry key=\"allowDuplicateAccounts\" value=\"false\"/>\n    <entry key=\"allowMultipleAssignments\" value=\"false\"/>\n    <entry key=\"mergeTemplates\" value=\"false\"/>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','organizational',NULL,NULL,NULL,' ',' ',' ',' ',_binary '\0',NULL,NULL,NULL,_binary '\0'),('a9fedb6691bb1afa8191bbc9f7a800be',1725432461225,1725432651200,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,NULL,NULL,NULL,NULL,'Admin','Admin','Admin',_binary '\0',0,' ',' ','<Attributes>\n  <Map>\n    <entry key=\"accountSelectorRules\"/>\n    <entry key=\"allowDuplicateAccounts\" value=\"false\"/>\n    <entry key=\"allowMultipleAssignments\" value=\"false\"/>\n    <entry key=\"mergeTemplates\" value=\"false\"/>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','business',NULL,NULL,NULL,' ',' ',' ',' ',_binary '\0',NULL,NULL,NULL,_binary '\0'),('a9fedb6691bb1afa8191bbca3a2300c0',1725432478243,1730095366231,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,NULL,NULL,NULL,NULL,'Business Role','Business Role','Business Role',_binary '\0',0,' ',' ','<Attributes>\n  <Map>\n    <entry key=\"accountSelectorRules\"/>\n    <entry key=\"allowDuplicateAccounts\" value=\"false\"/>\n    <entry key=\"allowMultipleAssignments\" value=\"false\"/>\n    <entry key=\"mergeTemplates\" value=\"false\"/>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','organizational',NULL,NULL,NULL,' ',' ',' ',' ',_binary '\0',NULL,NULL,NULL,_binary '\0'),('a9fedb66924113af8192569506840458',1728029460100,1728029958766,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,NULL,NULL,NULL,NULL,'Org Role','Org Role','Org Role',_binary '\0',0,' ',' ','<Attributes>\n  <Map>\n    <entry key=\"accountSelectorRules\"/>\n    <entry key=\"allowDuplicateAccounts\" value=\"false\"/>\n    <entry key=\"allowMultipleAssignments\" value=\"false\"/>\n    <entry key=\"mergeTemplates\" value=\"false\"/>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','organizational',NULL,NULL,NULL,' ',' ',' ',' ',_binary '\0',NULL,NULL,NULL,_binary '\0'),('a9fedb66924113af8192569770f10460',1728029618418,1728029958766,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,NULL,NULL,NULL,NULL,'Business Role Demo','Business Role Demo','Business Role Demo',_binary '\0',0,' ',' ','<Attributes>\n  <Map>\n    <entry key=\"accountSelectorRules\"/>\n    <entry key=\"allowDuplicateAccounts\" value=\"false\"/>\n    <entry key=\"allowMultipleAssignments\" value=\"false\"/>\n    <entry key=\"mergeTemplates\" value=\"false\"/>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','business',NULL,NULL,NULL,'<IdentitySelector>\n  <MatchExpression and=\"true\">\n    <MatchTerm name=\"inactive\" type=\"Entitlement\" value=\"false\"/>\n    <MatchTerm name=\"employeeType\" type=\"Entitlement\" value=\"Employee\"/>\n  </MatchExpression>\n</IdentitySelector>\n',' ',' ',' ',_binary '\0',NULL,NULL,NULL,_binary '\0'),('a9fedb66924113af8192569b193c0464',1728029858108,1728029958766,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,NULL,NULL,NULL,NULL,'IT Role Demo','IT Role Demo','IT Role Demo',_binary '\0',0,' ',' ','<Attributes>\n  <Map>\n    <entry key=\"accountSelectorRules\"/>\n    <entry key=\"allowDuplicateAccounts\" value=\"false\"/>\n    <entry key=\"allowMultipleAssignments\" value=\"false\"/>\n    <entry key=\"mergeTemplates\" value=\"false\"/>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','it',NULL,NULL,NULL,' ',' ',' ',' ',_binary '\0',NULL,NULL,NULL,_binary '\0');
/*!40000 ALTER TABLE `spt_bundle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:58
