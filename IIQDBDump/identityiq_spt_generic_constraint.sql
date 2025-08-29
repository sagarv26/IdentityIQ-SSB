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
-- Table structure for table `spt_generic_constraint`
--

DROP TABLE IF EXISTS `spt_generic_constraint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_generic_constraint` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(2000) DEFAULT NULL,
  `description` varchar(4000) DEFAULT NULL,
  `policy` varchar(32) DEFAULT NULL,
  `violation_owner_type` varchar(255) DEFAULT NULL,
  `violation_owner` varchar(32) DEFAULT NULL,
  `violation_owner_rule` varchar(32) DEFAULT NULL,
  `compensating_control` longtext,
  `disabled` bit(1) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `remediation_advice` longtext,
  `violation_summary` longtext,
  `arguments` longtext,
  `selectors` longtext,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1A3C4CCD2E02D59E` (`violation_owner_rule`),
  KEY `FK1A3C4CCD57FD28A4` (`policy`),
  KEY `FK1A3C4CCD486634B7` (`assigned_scope`),
  KEY `FK1A3C4CCDA5FB1B1` (`owner`),
  KEY `FK1A3C4CCD16E8C617` (`violation_owner`),
  KEY `SPT_IDX52403791F605046` (`assigned_scope_path`(255)),
  CONSTRAINT `FK1A3C4CCD16E8C617` FOREIGN KEY (`violation_owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK1A3C4CCD2E02D59E` FOREIGN KEY (`violation_owner_rule`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FK1A3C4CCD486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK1A3C4CCD57FD28A4` FOREIGN KEY (`policy`) REFERENCES `spt_policy` (`id`),
  CONSTRAINT `FK1A3C4CCDA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_generic_constraint`
--

LOCK TABLES `spt_generic_constraint` WRITE;
/*!40000 ALTER TABLE `spt_generic_constraint` DISABLE KEYS */;
INSERT INTO `spt_generic_constraint` VALUES ('a9fe0bbd92d11bbd8192d1948de60025',1730093026790,1730181557658,NULL,NULL,NULL,'new rule',NULL,'a9fe0bbd92d11bbd8192d1948de60024','None',NULL,NULL,NULL,_binary '',0,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"taskResultId\" value=\"a9fe0bbd92d11bbd8192d1a021560054\"/>\n  </Map>\n</Attributes>\n','<List>\n  <IdentitySelector>\n    <MatchExpression>\n      <MatchTerm name=\"Group\" type=\"Entitlement\" value=\"Admin\">\n        <ApplicationRef>\n          <Reference class=\"sailpoint.object.Application\" id=\"a9fe0bbd90c41bed8190d46ece020648\" name=\"Contractor System Employee\"/>\n        </ApplicationRef>\n      </MatchTerm>\n    </MatchExpression>\n  </IdentitySelector>\n  <IdentitySelector>\n    <MatchExpression>\n      <MatchTerm name=\"Group\" type=\"Entitlement\" value=\"User\">\n        <ApplicationRef>\n          <Reference class=\"sailpoint.object.Application\" id=\"a9fe0bbd90c41bed8190d46ece020648\" name=\"Contractor System Employee\"/>\n        </ApplicationRef>\n      </MatchTerm>\n    </MatchExpression>\n  </IdentitySelector>\n</List>\n',0),('a9fe0bbd92d11bbd8192d1b43de500b7',1730095103461,1730181557658,NULL,NULL,NULL,'new rule',NULL,'a9fe0bbd92d11bbd8192d1948de60024','None',NULL,NULL,NULL,_binary '\0',0,NULL,NULL,' ','<List>\n  <IdentitySelector>\n    <MatchExpression>\n      <MatchTerm name=\"Group\" type=\"Entitlement\" value=\"User\">\n        <ApplicationRef>\n          <Reference class=\"sailpoint.object.Application\" id=\"a9fe0bbd90c41bed8190d46ece020648\" name=\"Contractor System Employee\"/>\n        </ApplicationRef>\n      </MatchTerm>\n    </MatchExpression>\n  </IdentitySelector>\n  <IdentitySelector>\n    <MatchExpression>\n      <MatchTerm name=\"Group\" type=\"Entitlement\" value=\"Admin\">\n        <ApplicationRef>\n          <Reference class=\"sailpoint.object.Application\" id=\"a9fe0bbd90c41bed8190d46ece020648\" name=\"Contractor System Employee\"/>\n        </ApplicationRef>\n      </MatchTerm>\n    </MatchExpression>\n  </IdentitySelector>\n</List>\n',1),('a9fe0bbd92d11bbd8192d6ccf83703c1',1730180610103,1730185318598,NULL,NULL,NULL,'Requires LDAP account - Please create LDAP Account before submitting request',NULL,'a9fe0bbd92d11bbd8192d6ccf83603c0','None',NULL,NULL,'Requires LDAP account',_binary '\0',0,'Please create LDAP Account before submitting request',NULL,' ','<List>\n  <IdentitySelector>\n    <RuleRef>\n      <Reference class=\"sailpoint.object.Rule\" id=\"a9fe0bbd92d11bbd8192d6cbc74003bf\" name=\"Require LDAP Account Policy Rule\"/>\n    </RuleRef>\n  </IdentitySelector>\n</List>\n',0);
/*!40000 ALTER TABLE `spt_generic_constraint` ENABLE KEYS */;
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
