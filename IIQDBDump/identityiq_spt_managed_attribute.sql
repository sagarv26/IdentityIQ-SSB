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
-- Table structure for table `spt_managed_attribute`
--

DROP TABLE IF EXISTS `spt_managed_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_managed_attribute` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `extended1` varchar(450) DEFAULT NULL,
  `extended2` varchar(450) DEFAULT NULL,
  `extended3` varchar(450) DEFAULT NULL,
  `purview` varchar(128) DEFAULT NULL,
  `application` varchar(32) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `aggregated` bit(1) DEFAULT NULL,
  `attribute` varchar(322) DEFAULT NULL,
  `value` varchar(450) DEFAULT NULL,
  `hash` varchar(128) NOT NULL,
  `display_name` varchar(450) DEFAULT NULL,
  `displayable_name` varchar(450) DEFAULT NULL,
  `uuid` varchar(128) DEFAULT NULL,
  `attributes` longtext,
  `requestable` bit(1) DEFAULT NULL,
  `uncorrelated` bit(1) DEFAULT NULL,
  `last_refresh` bigint(20) DEFAULT NULL,
  `last_target_aggregation` bigint(20) DEFAULT NULL,
  `key1` varchar(128) DEFAULT NULL,
  `key2` varchar(128) DEFAULT NULL,
  `key3` varchar(128) DEFAULT NULL,
  `key4` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hash` (`hash`),
  KEY `spt_managed_attr_aggregated` (`aggregated`),
  KEY `spt_managed_attr_type` (`type`),
  KEY `spt_managed_attr_requestable` (`requestable`),
  KEY `spt_managed_attr_extended1_ci` (`extended1`(255)),
  KEY `spt_managed_attr_dispname_ci` (`displayable_name`(255)),
  KEY `spt_managed_attr_extended2_ci` (`extended2`(255)),
  KEY `spt_managed_attr_value_ci` (`value`(255)),
  KEY `spt_ma_key2_ci` (`key2`),
  KEY `spt_ma_key1_ci` (`key1`),
  KEY `spt_ma_key3_ci` (`key3`),
  KEY `spt_managed_attr_extended3_ci` (`extended3`(255)),
  KEY `spt_managed_attr_uuid_ci` (`uuid`),
  KEY `spt_managed_attr_last_tgt_agg` (`last_target_aggregation`),
  KEY `spt_ma_key4_ci` (`key4`),
  KEY `spt_managed_attr_attr_ci` (`attribute`(255)),
  KEY `FKF5F1417439D71460` (`application`),
  KEY `FKF5F14174486634B7` (`assigned_scope`),
  KEY `FKF5F14174A5FB1B1` (`owner`),
  KEY `spt_managed_created` (`created`),
  KEY `spt_managed_comp` (`application`,`type`(32),`attribute`(50),`value`(141)),
  KEY `spt_managed_modified` (`modified`),
  KEY `SPT_IDX6B29BC60611AFDD4` (`assigned_scope_path`(255)),
  CONSTRAINT `FKF5F1417439D71460` FOREIGN KEY (`application`) REFERENCES `spt_application` (`id`),
  CONSTRAINT `FKF5F14174486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKF5F14174A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_managed_attribute`
--

LOCK TABLES `spt_managed_attribute` WRITE;
/*!40000 ALTER TABLE `spt_managed_attribute` DISABLE KEYS */;
INSERT INTO `spt_managed_attribute` VALUES ('a9fe0bbd957e1ec6819583a108450245',1741670123589,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd957e1ec68195839d91180238','Entitlement',_binary '\0','group','Approver','d356ed3d91bf33cd030707c574fd555de2f219f6',NULL,'Approver',NULL,NULL,_binary '',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL),('a9fe0bbd957e1ec6819583a10aa40249',1741670124197,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd957e1ec68195839d91180238','Entitlement',_binary '\0','group','Tester','b4e8785564eff28d23883f68280277c775e49298',NULL,'Tester',NULL,NULL,_binary '',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL),('a9fe0bbd957e1ec6819583aa919b0271',1741670748571,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd957e1ec6819583a6892e0263','Entitlement',_binary '\0','group','Developer','98a4e9357bfceec01df6fd7dc3f85cefc604fd31',NULL,'Developer',NULL,NULL,_binary '\0',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL),('a9fe0bbd965b1be58196b3af7baa24c6',1746771344306,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd965b1be58196b3a7b67a24ab','group',_binary '\0','group','admin','028d9f85a32a37beefb97a9db680d01d34f83707',NULL,'admin',NULL,NULL,_binary '',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL),('a9fe0bbd965b1be58196b3af85af24ca',1746771346863,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd965b1be58196b3a7b67a24ab','group',_binary '\0','group','dev','9d6933772169ec03d00cb16b0137d7c645bf55d2',NULL,'dev',NULL,NULL,_binary '',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL),('a9fe0bbd965b1be58196b3af8a9824ce',1746771348121,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd965b1be58196b3a7b67a24ab','group',_binary '\0','group','tester','01411a0966b72293416ffa66a52bbde414a36db1',NULL,'tester',NULL,NULL,_binary '',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL),('a9fedb66917918898191799df82c0096',1724322281517,1733376397760,'a9fe0bbd90c41bed8190d472dc1d0658',NULL,NULL,NULL,NULL,NULL,NULL,'297e8b3d76432f3201764cc2356c002b','group',_binary '','groups','cn=admins,ou=group,o=Company','fe8ad6d38cc64a2505827a4301991cfb9406c895','admins','admins',NULL,'<Attributes>\n  <Map>\n    <entry key=\"IIQDisabled\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"cn\" value=\"admins\"/>\n    <entry key=\"description\" value=\"Administrators\"/>\n    <entry key=\"owner\" value=\"uid=rdra001,ou=user,o=Company\"/>\n    <entry key=\"sysDescriptions\">\n      <value>\n        <Map>\n          <entry key=\"en_US\"/>\n        </Map>\n      </value>\n    </entry>\n    <entry key=\"uniqueMember\">\n      <value>\n        <List>\n          <String>uid=rdra001,ou=users,o=Company</String>\n          <String>uid=aaa0011,ou=user,o=Company</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n',_binary '',_binary '\0',1725430219666,NULL,NULL,NULL,NULL,NULL),('a9fedb66924113af819250e8b6b702f3',1727934281405,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d45e9a7705ef','Entitlement',_binary '\0','roles','Admin','9fbe3b59cc60363484691a9e3f3b47bc0de1c99b',NULL,'Admin',NULL,NULL,_binary '',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL),('a9fedb66924113af819250e8ba9e02f5',1727934282398,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d45e9a7705ef','Entitlement',_binary '\0','roles','Developer','2ef539d022be0e50a977a2dd9c8c8e0bd2e59b9c',NULL,'Developer',NULL,NULL,_binary '',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL),('a9fedb66924113af819250e8be0502f7',1727934283276,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d45e9a7705ef','Entitlement',_binary '\0','roles','User','41aeb26e4f6d12e2c948d2b9de93000037f45a0d',NULL,'User',NULL,NULL,_binary '',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL),('a9fedb66924113af819250f0358b030e',1727934772619,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648','Entitlement',_binary '\0','Group','User','96361f7240f9cdb494a9179c0970ba14dd703589',NULL,'User',NULL,NULL,_binary '',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL),('a9fedb66924113af819250f03e480311',1727934774856,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648','Entitlement',_binary '\0','Group','Admin','01c71f8b2b7a8cefc248811330d5f6e877d3ffce',NULL,'Admin',NULL,NULL,_binary '',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL),('a9fedb66924113af819250f0435a0312',1727934776155,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648','Entitlement',_binary '\0','Group','Developer','9550e1187b47689d09f46d33890348d35a63a455',NULL,'Developer',NULL,NULL,_binary '',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `spt_managed_attribute` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:28
