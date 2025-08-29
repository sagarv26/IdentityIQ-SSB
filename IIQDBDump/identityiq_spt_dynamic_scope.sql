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
-- Table structure for table `spt_dynamic_scope`
--

DROP TABLE IF EXISTS `spt_dynamic_scope`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_dynamic_scope` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `selector` longtext,
  `allow_all` bit(1) DEFAULT NULL,
  `population_request_authority` longtext,
  `role_request_control` varchar(32) DEFAULT NULL,
  `application_request_control` varchar(32) DEFAULT NULL,
  `managed_attr_request_control` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FKA73F59CC11B2F20` (`role_request_control`),
  KEY `FKA73F59CCE677873B` (`managed_attr_request_control`),
  KEY `FKA73F59CC486634B7` (`assigned_scope`),
  KEY `FKA73F59CC8A8BFFA` (`application_request_control`),
  KEY `FKA73F59CCA5FB1B1` (`owner`),
  KEY `SPT_IDX50B36EB8F7F2C884` (`assigned_scope_path`(255)),
  CONSTRAINT `FKA73F59CC11B2F20` FOREIGN KEY (`role_request_control`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FKA73F59CC486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKA73F59CC8A8BFFA` FOREIGN KEY (`application_request_control`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FKA73F59CCA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FKA73F59CCE677873B` FOREIGN KEY (`managed_attr_request_control`) REFERENCES `spt_rule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_dynamic_scope`
--

LOCK TABLES `spt_dynamic_scope` WRITE;
/*!40000 ALTER TABLE `spt_dynamic_scope` DISABLE KEYS */;
INSERT INTO `spt_dynamic_scope` VALUES ('4028ab1063f427af0163f428e8c2013a',1528809711811,1617705339491,NULL,NULL,NULL,'Everyone',NULL,' ',_binary '',' ',NULL,NULL,NULL),('4028ab1063fdfb6d0163fdfc4e0e0015',1528974560782,1617706124482,NULL,NULL,NULL,'Self Service',NULL,' ',_binary '',' ','4028ab1063f427af0163f42915d801a3','4028ab1063f427af0163f42915d801a3','4028ab1063f427af0163f429186001a7'),('4028ab1063fdfb6d0163fdfc4e820016',1528974560899,1617706124624,NULL,NULL,NULL,'Manager',NULL,'<IdentitySelector>\n  <MatchExpression>\n    <MatchTerm name=\"managerStatus\" value=\"true\"/>\n  </MatchExpression>\n</IdentitySelector>\n',_binary '\0','<PopulationRequestAuthority>\n  <MatchConfig enableSubordinateControl=\"true\" maxHierarchyDepth=\"10\" subordinateOption=\"directOrIndirect\"/>\n</PopulationRequestAuthority>\n','4028ab1063f427af0163f42915d801a3','4028ab1063f427af0163f42915d801a3','4028ab1063f427af0163f429186001a7'),('4028ab1063fdfb6d0163fdfc4f000017',1528974561024,1617706124918,NULL,NULL,NULL,'Help Desk',NULL,'<IdentitySelector>\n  <MatchExpression>\n    <MatchTerm name=\"capabilities\" value=\"HelpDesk\"/>\n  </MatchExpression>\n</IdentitySelector>\n',_binary '\0','<PopulationRequestAuthority allowAll=\"true\"/>\n','4028ab1063f427af0163f42915d801a3','4028ab1063f427af0163f42915d801a3','4028ab1063f427af0163f429186001a7'),('a9fe0bbd92f5112a8192f65979b000ee',1730709911984,1730710139027,NULL,NULL,NULL,'Test DynamicScope','','<IdentitySelector>\n  <MatchExpression>\n    <MatchTerm name=\"city\" type=\"Entitlement\" value=\"Bengaluru\"/>\n  </MatchExpression>\n</IdentitySelector>\n',_binary '\0','<PopulationRequestAuthority>\n  <MatchConfig enableAttributeControl=\"true\">\n    <IdentityAttributeFilterControl displayName=\"EmployeeType\" name=\"employeeType\"/>\n  </MatchConfig>\n</PopulationRequestAuthority>\n',NULL,NULL,NULL),('a9fe0bbd97a01e0b8197ab42d057094f',1750924972146,NULL,NULL,NULL,NULL,'SystemAdministrator',NULL,' ',_binary '\0',' ',NULL,NULL,NULL),('a9fedb66924113af81926fc5d220084a',1728452088352,NULL,NULL,NULL,NULL,'User Mainenance',NULL,' ',_binary '\0',' ',NULL,NULL,NULL);
/*!40000 ALTER TABLE `spt_dynamic_scope` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:52
