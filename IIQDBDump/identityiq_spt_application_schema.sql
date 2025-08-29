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
-- Table structure for table `spt_application_schema`
--

DROP TABLE IF EXISTS `spt_application_schema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_application_schema` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `object_type` varchar(255) DEFAULT NULL,
  `aggregation_type` varchar(128) DEFAULT NULL,
  `native_object_type` varchar(255) DEFAULT NULL,
  `identity_attribute` varchar(255) DEFAULT NULL,
  `display_attribute` varchar(255) DEFAULT NULL,
  `instance_attribute` varchar(255) DEFAULT NULL,
  `group_attribute` varchar(255) DEFAULT NULL,
  `hierarchy_attribute` varchar(255) DEFAULT NULL,
  `reference_attribute` varchar(255) DEFAULT NULL,
  `include_permissions` bit(1) DEFAULT NULL,
  `index_permissions` bit(1) DEFAULT NULL,
  `child_hierarchy` bit(1) DEFAULT NULL,
  `perm_remed_mod_type` varchar(255) DEFAULT NULL,
  `config` longtext,
  `features_string` varchar(512) DEFAULT NULL,
  `association_schema_name` varchar(255) DEFAULT NULL,
  `creation_rule` varchar(32) DEFAULT NULL,
  `customization_rule` varchar(32) DEFAULT NULL,
  `correlation_rule` varchar(32) DEFAULT NULL,
  `refresh_rule` varchar(32) DEFAULT NULL,
  `application` varchar(32) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  `description_attribute` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK62F93AF839D71460` (`application`),
  KEY `FK62F93AF8D9F8531C` (`refresh_rule`),
  KEY `FK62F93AF84FE65998` (`creation_rule`),
  KEY `FK62F93AF86FB29924` (`customization_rule`),
  KEY `FK62F93AF8BE1EE0D5` (`correlation_rule`),
  KEY `FK62F93AF8486634B7` (`assigned_scope`),
  KEY `FK62F93AF8A5FB1B1` (`owner`),
  KEY `SPT_IDX95FDCE46C5917DC` (`assigned_scope_path`(255)),
  CONSTRAINT `FK62F93AF839D71460` FOREIGN KEY (`application`) REFERENCES `spt_application` (`id`),
  CONSTRAINT `FK62F93AF8486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK62F93AF84FE65998` FOREIGN KEY (`creation_rule`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FK62F93AF86FB29924` FOREIGN KEY (`customization_rule`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FK62F93AF8A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK62F93AF8BE1EE0D5` FOREIGN KEY (`correlation_rule`) REFERENCES `spt_rule` (`id`),
  CONSTRAINT `FK62F93AF8D9F8531C` FOREIGN KEY (`refresh_rule`) REFERENCES `spt_rule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_application_schema`
--

LOCK TABLES `spt_application_schema` WRITE;
/*!40000 ALTER TABLE `spt_application_schema` DISABLE KEYS */;
INSERT INTO `spt_application_schema` VALUES ('a9fe0bbd7af6130b817b0ad4f4dd06c4',1627974333661,NULL,NULL,NULL,NULL,NULL,'account',NULL,'','','','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0',NULL,' ',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('a9fe0bbd7af6130b817b0ad5c8af06cb',1627974387888,NULL,NULL,NULL,NULL,NULL,'account',NULL,'account','','','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0',NULL,' ',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('a9fe0bbd957e1ec681957e8e99840069',1741585029510,NULL,NULL,NULL,NULL,NULL,'account',NULL,'inetOrgPerson','dn','cn','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0',NULL,' ',NULL,NULL,NULL,NULL,NULL,NULL,'297e8b3d76432f3201764cc2356c002b',0,NULL),('a9fe0bbd957e1ec681957e8e9987006a',1741585029511,NULL,NULL,NULL,NULL,NULL,'group',NULL,'groupOfUniqueNames','dn','cn','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0',NULL,'<Attributes>\n  <Map>\n    <entry key=\"groupMemberAttribute\" value=\"uniqueMember\"/>\n  </Map>\n</Attributes>\n','PROVISIONING',NULL,NULL,NULL,NULL,NULL,'297e8b3d76432f3201764cc2356c002b',1,''),('a9fe0bbd957e1ec681957e8e9e41006e',1741585030721,NULL,NULL,NULL,NULL,NULL,'account',NULL,'account','EmployeeID','EmployeeID','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0','None',' ',NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d45e9a7705ef',0,NULL),('a9fe0bbd957e1ec681957e8e9f2a0070',1741585030954,NULL,NULL,NULL,NULL,NULL,'account',NULL,'account','employeenumber','employeenumber','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0','None',' ',NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648',0,NULL),('a9fe0bbd957e1ec681957e8ea45e0073',1741585032287,NULL,NULL,NULL,NULL,NULL,'account',NULL,'User','distinguishedName','msDS-PrincipalName','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0',NULL,' ',NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd940617a1819406c057410083',0,NULL),('a9fe0bbd957e1ec681957e8ea4600074',1741585032288,NULL,NULL,NULL,NULL,NULL,'group',NULL,'Group','distinguishedName','msDS-PrincipalName','',NULL,'memberOf',NULL,_binary '\0',_binary '\0',_binary '\0',NULL,'<Attributes>\n  <Map>\n    <entry key=\"groupMemberAttribute\" value=\"member\"/>\n  </Map>\n</Attributes>\n','PROVISIONING, GROUPS_HAVE_MEMBERS',NULL,NULL,NULL,NULL,NULL,'a9fe0bbd940617a1819406c057410083',1,''),('a9fe0bbd957e1ec681957e8ea75e0076',1741585033054,1746517935498,NULL,NULL,NULL,NULL,'account',NULL,'user','employeenumber','employeenumber','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0',NULL,' ',NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd940617a1819411d5df7801da',0,NULL),('a9fe0bbd957e1ec681957e8ea75f0077',1741585033055,1746517935498,NULL,NULL,NULL,NULL,'group',NULL,'group','','','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0',NULL,' ',NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd940617a1819411d5df7801da',1,''),('a9fe0bbd957e1ec681957e8ea8080079',1741585033224,NULL,NULL,NULL,NULL,NULL,'account',NULL,'account','EmployeeID','EmployeeID','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0','None',' ',NULL,NULL,NULL,NULL,NULL,NULL,'a9fedb66922c14e481922d64ab4c00b9',0,NULL),('a9fe0bbd957e1ec68195839d912f023a',1741669896495,NULL,NULL,NULL,NULL,NULL,'account',NULL,'account','employeenumber','employeenumber','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0','None',' ',NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd957e1ec68195839d91180238',0,NULL),('a9fe0bbd957e1ec6819583a6892f0265',1741670484271,NULL,NULL,NULL,NULL,NULL,'account',NULL,'account','employeenumber','employeenumber','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0','None',' ',NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd957e1ec6819583a6892e0263',0,NULL),('a9fe0bbd965b1be58196b3a7b75424ad',1746770835302,1747294701682,NULL,NULL,NULL,NULL,'account',NULL,'user','id','id','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0',NULL,' ',NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd965b1be58196b3a7b67a24ab',0,NULL),('a9fe0bbd965b1be58196b3a7b76624ae',1746770835303,1747294701682,NULL,NULL,NULL,NULL,'group',NULL,'group','','','',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0',NULL,' ',NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd965b1be58196b3a7b67a24ab',1,'');
/*!40000 ALTER TABLE `spt_application_schema` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:17
