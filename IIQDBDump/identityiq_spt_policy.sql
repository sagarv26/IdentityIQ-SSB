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
-- Table structure for table `spt_policy`
--

DROP TABLE IF EXISTS `spt_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_policy` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `template` bit(1) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `type_key` varchar(255) DEFAULT NULL,
  `executor` varchar(255) DEFAULT NULL,
  `config_page` varchar(255) DEFAULT NULL,
  `certification_actions` varchar(255) DEFAULT NULL,
  `violation_owner_type` varchar(255) DEFAULT NULL,
  `violation_owner` varchar(32) DEFAULT NULL,
  `violation_owner_rule` varchar(32) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `arguments` longtext,
  `signature` longtext,
  `alert` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK13D458BA2E02D59E` (`violation_owner_rule`),
  KEY `FK13D458BA486634B7` (`assigned_scope`),
  KEY `FK13D458BAA5FB1B1` (`owner`),
  KEY `FK13D458BA16E8C617` (`violation_owner`),
  KEY `spt_policy_assignedscopepath` (`assigned_scope_path`(255)),
  CONSTRAINT `FK13D458BA16E8C617` FOREIGN KEY (`violation_owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK13D458BA2E02D59E` FOREIGN KEY (`violation_owner_rule`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FK13D458BA486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK13D458BAA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_policy`
--

LOCK TABLES `spt_policy` WRITE;
/*!40000 ALTER TABLE `spt_policy` DISABLE KEYS */;
INSERT INTO `spt_policy` VALUES ('297e8b3d78ab76ff0178ab78ac0b0019',1617784515595,NULL,NULL,NULL,NULL,'Effective Entitlement SOD Template',_binary '','EffectiveEntitlementSOD','policy_type_effective_entitlement_sod','sailpoint.policy.EffectiveEntitlementSODPolicyExecutor','entitlementPolicy.xhtml','Remediated,Mitigated,Delegated','Manager',NULL,NULL,'Inactive',' ',' ',' '),('4028ab1063f427af0163f428de760119',1528809709174,1617705334779,NULL,NULL,NULL,'SOD Template',_binary '','SOD','policy_type_sod','sailpoint.policy.SODPolicyExecutor','sodpolicy.xhtml','Remediated,Mitigated,Delegated','Manager',NULL,NULL,'Inactive',' ',' ',' '),('4028ab1063f427af0163f428def3011a',1528809709299,1617705334945,NULL,NULL,NULL,'Entitlement SOD Template',_binary '','EntitlementSOD','policy_type_entitlement_sod','sailpoint.policy.EntitlementSODPolicyExecutor','entitlementPolicy.xhtml','Remediated,Mitigated,Delegated','Manager',NULL,NULL,'Inactive',' ',' ',' '),('4028ab1063f427af0163f428df69011b',1528809709418,1617705335095,NULL,NULL,NULL,'Activity Template',_binary '','Activity','policy_type_activity','sailpoint.policy.ActivityPolicyExecutor','activitypolicy.xhtml','Acknowledged,Mitigated,Delegated','Manager',NULL,NULL,'Inactive',' ',' ',' '),('4028ab1063f427af0163f428dfcc011c',1528809709519,1617705335240,NULL,NULL,NULL,'Account Template',_binary '','Account','policy_type_account','sailpoint.policy.AccountPolicyExecutor','genericpolicy.xhtml','Mitigated,Delegated','Manager',NULL,NULL,'Inactive',' ',' ',' '),('4028ab1063f427af0163f428e043011e',1528809709635,1617705335409,NULL,NULL,NULL,'Risk Template',_binary '','Risk','policy_type_risk','sailpoint.policy.RiskPolicyExecutor','genericpolicy.xhtml','Mitigated,Delegated','Manager',NULL,NULL,'Inactive','<Attributes>\n  <Map>\n    <entry key=\"compositeThreshold\" value=\"1000\"/>\n  </Map>\n</Attributes>\n','<Signature>\n  <Inputs>\n    <Argument helpKey=\"policy_composite_threshold\" name=\"compositeThreshold\" type=\"int\">\n      <Prompt>risk_template_composite_score_threshold</Prompt>\n    </Argument>\n  </Inputs>\n</Signature>\n',' '),('4028ab1063f427af0163f428e084011f',1528809709701,1617705335729,NULL,NULL,NULL,'Advanced Template',_binary '','Advanced','policy_type_advanced','sailpoint.policy.GenericPolicyExecutor','advancedPolicy.xhtml','Mitigated,Delegated','Manager',NULL,NULL,'Inactive',' ',' ',' '),('a9fe0bbd92d11bbd8192d1948de60024',1730093026790,1730181557658,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Entitlement SOD',_binary '\0','EntitlementSOD','policy_type_entitlement_sod','sailpoint.policy.EntitlementSODPolicyExecutor','entitlementPolicy.xhtml','Remediated,Mitigated,Delegated','Identity','a9fe0bbd90c41bed8190d472dc1d0658',NULL,'Inactive','<Attributes>\n  <Map>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n    <entry key=\"taskResultId\" value=\"a9fe0bbd92d11bbd8192d19a18a70039\"/>\n  </Map>\n</Attributes>\n',' ','<PolicyAlert disabled=\"true\" escalationStyle=\"none\"/>\n'),('a9fe0bbd92d11bbd8192d1bc58f00111',1730095634672,1730095714545,NULL,NULL,NULL,'Role SOD',_binary '\0','SOD','policy_type_sod','sailpoint.policy.SODPolicyExecutor','sodpolicy.xhtml','Remediated,Mitigated,Delegated','Manager',NULL,NULL,'Active','<Attributes>\n  <Map>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n',' ','<PolicyAlert disabled=\"true\" escalationStyle=\"none\"/>\n'),('a9fe0bbd92d11bbd8192d6ccf83603c0',1730180610103,1730185318598,NULL,NULL,NULL,'LDAP Account',_binary '\0','Advanced','policy_type_advanced','sailpoint.policy.GenericPolicyExecutor','advancedPolicy.xhtml','Mitigated,Delegated','Manager',NULL,NULL,'Active','<Attributes>\n  <Map>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n',' ','<PolicyAlert disabled=\"true\" escalationStyle=\"none\"/>\n');
/*!40000 ALTER TABLE `spt_policy` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:39
