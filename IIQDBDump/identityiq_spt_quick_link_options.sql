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
-- Table structure for table `spt_quick_link_options`
--

DROP TABLE IF EXISTS `spt_quick_link_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_quick_link_options` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `allow_bulk` bit(1) DEFAULT NULL,
  `allow_other` bit(1) DEFAULT NULL,
  `allow_self` bit(1) DEFAULT NULL,
  `options` longtext,
  `dynamic_scope` varchar(32) NOT NULL,
  `quick_link` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8C93F7F329E4F453` (`quick_link`),
  KEY `FK8C93F7F3E5B001E9` (`dynamic_scope`),
  KEY `FK8C93F7F3A5FB1B1` (`owner`),
  CONSTRAINT `FK8C93F7F329E4F453` FOREIGN KEY (`quick_link`) REFERENCES `spt_quick_link` (`id`),
  CONSTRAINT `FK8C93F7F3A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK8C93F7F3E5B001E9` FOREIGN KEY (`dynamic_scope`) REFERENCES `spt_dynamic_scope` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_quick_link_options`
--

LOCK TABLES `spt_quick_link_options` WRITE;
/*!40000 ALTER TABLE `spt_quick_link_options` DISABLE KEYS */;
INSERT INTO `spt_quick_link_options` VALUES ('297e8b3d78a6bfa20178a6c08b880003',1617705339784,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063f427af0163f428e8c2013a','4028ab1063f427af0163f428e936013b'),('297e8b3d78a6bfa20178a6c08bf90004',1617705339897,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063f427af0163f428e8c2013a','4028ab1063f427af0163f428e9d6013d'),('297e8b3d78a6bfa20178a6c08d490006',1617705340234,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063f427af0163f428e8c2013a','4028ab1063f427af0163f428ed370141'),('297e8b3d78a6bfa20178a6c08dd30007',1617705340372,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063f427af0163f428e8c2013a','4028ab1063f427af0163f428ed7f0143'),('297e8b3d78a6bfa20178a6c08ea70008',1617705340583,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063f427af0163f428e8c2013a','4028ab1063f427af0163f428edd90145'),('297e8b3d78a6bfa20178a6c08f380009',1617705340728,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063f427af0163f428e8c2013a','4028ab1063f427af0163f428ee2b0147'),('297e8b3d78a6bfa20178a6c08fa5000a',1617705340837,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063f427af0163f428e8c2013a','4028ab1063f427af0163f428ee680149'),('297e8b3d78a6bfa20178a6c09014000b',1617705340948,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063f427af0163f428e8c2013a','4028ab1063f427af0163f428eeaa014b'),('297e8b3d78a6bfa20178a6cc8784000f',1617706125188,NULL,NULL,_binary '\0',_binary '\0',_binary '','<Attributes>\n  <Map>\n    <entry key=\"allowRequestEntitlements\" value=\"true\"/>\n    <entry key=\"allowRequestEntitlementsAdditionalAccountRequests\" value=\"false\"/>\n    <entry key=\"allowRequestEntitlementsRemove\" value=\"true\"/>\n    <entry key=\"allowRequestEntitlementsShowPopulation\" value=\"true\"/>\n    <entry key=\"allowRequestRoles\" value=\"true\"/>\n    <entry key=\"allowRequestRolesAdditionalAccountRequests\" value=\"false\"/>\n    <entry key=\"allowRequestRolesRemove\" value=\"true\"/>\n    <entry key=\"allowRequestRolesShowPopulation\" value=\"true\"/>\n  </Map>\n</Attributes>\n','4028ab1063fdfb6d0163fdfc4e0e0015','4028ab1063fdfb6d0163fdfc4f850018'),('297e8b3d78a6bfa20178a6cc87850010',1617706125189,NULL,NULL,_binary '',_binary '\0',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"allowRequestEntitlements\" value=\"true\"/>\n    <entry key=\"allowRequestEntitlementsAdditionalAccountRequests\" value=\"false\"/>\n    <entry key=\"allowRequestEntitlementsRemove\" value=\"true\"/>\n    <entry key=\"allowRequestEntitlementsShowPopulation\" value=\"true\"/>\n    <entry key=\"allowRequestRoles\" value=\"true\"/>\n    <entry key=\"allowRequestRolesAdditionalAccountRequests\" value=\"false\"/>\n    <entry key=\"allowRequestRolesRemove\" value=\"true\"/>\n    <entry key=\"allowRequestRolesShowPopulation\" value=\"true\"/>\n  </Map>\n</Attributes>\n','4028ab1063fdfb6d0163fdfc4e820016','4028ab1063fdfb6d0163fdfc4f850018'),('297e8b3d78a6bfa20178a6cc87860011',1617706125190,NULL,NULL,_binary '',_binary '\0',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"allowRequestEntitlements\" value=\"true\"/>\n    <entry key=\"allowRequestEntitlementsAdditionalAccountRequests\" value=\"false\"/>\n    <entry key=\"allowRequestEntitlementsRemove\" value=\"true\"/>\n    <entry key=\"allowRequestEntitlementsShowPopulation\" value=\"true\"/>\n    <entry key=\"allowRequestRoles\" value=\"true\"/>\n    <entry key=\"allowRequestRolesAdditionalAccountRequests\" value=\"false\"/>\n    <entry key=\"allowRequestRolesRemove\" value=\"true\"/>\n    <entry key=\"allowRequestRolesShowPopulation\" value=\"true\"/>\n  </Map>\n</Attributes>\n','4028ab1063fdfb6d0163fdfc4f000017','4028ab1063fdfb6d0163fdfc4f850018'),('297e8b3d78a6bfa20178a6cc89310012',1617706125617,NULL,NULL,_binary '\0',_binary '\0',_binary '','<Attributes>\n  <Map>\n    <entry key=\"allowAccountOnlyRequests\" value=\"false\"/>\n    <entry key=\"allowManageAccountsAdditionalAccountRequests\" value=\"false\"/>\n    <entry key=\"allowManageExistingAccounts\" value=\"true\"/>\n  </Map>\n</Attributes>\n','4028ab1063fdfb6d0163fdfc4e0e0015','4028ab1063fdfb6d0163fdfc50a4001c'),('297e8b3d78a6bfa20178a6cc89320013',1617706125618,NULL,NULL,_binary '\0',_binary '',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"allowAccountOnlyRequests\" value=\"false\"/>\n    <entry key=\"allowManageAccountsAdditionalAccountRequests\" value=\"false\"/>\n    <entry key=\"allowManageExistingAccounts\" value=\"true\"/>\n  </Map>\n</Attributes>\n','4028ab1063fdfb6d0163fdfc4e820016','4028ab1063fdfb6d0163fdfc50a4001c'),('297e8b3d78a6bfa20178a6cc89330014',1617706125619,NULL,NULL,_binary '\0',_binary '',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"allowAccountOnlyRequests\" value=\"false\"/>\n    <entry key=\"allowManageAccountsAdditionalAccountRequests\" value=\"false\"/>\n    <entry key=\"allowManageExistingAccounts\" value=\"true\"/>\n  </Map>\n</Attributes>\n','4028ab1063fdfb6d0163fdfc4f000017','4028ab1063fdfb6d0163fdfc50a4001c'),('297e8b3d78a6bfa20178a6cc8a300015',1617706125872,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063fdfb6d0163fdfc4e0e0015','4028ab1063fdfb6d0163fdfc50f70020'),('297e8b3d78a6bfa20178a6cc8a310016',1617706125873,NULL,NULL,_binary '\0',_binary '',_binary '\0',' ','4028ab1063fdfb6d0163fdfc4e820016','4028ab1063fdfb6d0163fdfc50f70020'),('297e8b3d78a6bfa20178a6cc8a320017',1617706125874,NULL,NULL,_binary '\0',_binary '',_binary '\0',' ','4028ab1063fdfb6d0163fdfc4f000017','4028ab1063fdfb6d0163fdfc50f70020'),('297e8b3d78a6bfa20178a6cc8a890018',1617706125961,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063fdfb6d0163fdfc4e0e0015','4028ab1063fdfb6d0163fdfc51310024'),('297e8b3d78a6bfa20178a6cc8b070019',1617706126087,1728453549969,NULL,_binary '\0',_binary '',_binary '\0',' ','4028ab1063fdfb6d0163fdfc4e820016','4028ab1063fdfb6d0163fdfc51610026'),('297e8b3d78a6bfa20178a6cc8b08001a',1617706126088,1728453549969,NULL,_binary '\0',_binary '',_binary '\0',' ','4028ab1063fdfb6d0163fdfc4f000017','4028ab1063fdfb6d0163fdfc51610026'),('297e8b3d78a6bfa20178a6cc8bce001b',1617706126286,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063fdfb6d0163fdfc4e0e0015','4028ab1063fdfb6d0163fdfc519a0029'),('297e8b3d78a6bfa20178a6cc8bcf001c',1617706126287,NULL,NULL,_binary '\0',_binary '',_binary '\0',' ','4028ab1063fdfb6d0163fdfc4e820016','4028ab1063fdfb6d0163fdfc519a0029'),('297e8b3d78a6bfa20178a6cc8bd0001d',1617706126288,NULL,NULL,_binary '\0',_binary '',_binary '\0',' ','4028ab1063fdfb6d0163fdfc4f000017','4028ab1063fdfb6d0163fdfc519a0029'),('297e8b3d78a6bfa20178a6cc8cca001e',1617706126538,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063fdfb6d0163fdfc4e0e0015','4028ab1063fdfb6d0163fdfc5201002d'),('297e8b3d78a6bfa20178a6cc8ccb001f',1617706126539,NULL,NULL,_binary '\0',_binary '',_binary '\0',' ','4028ab1063fdfb6d0163fdfc4e820016','4028ab1063fdfb6d0163fdfc5201002d'),('297e8b3d78a6bfa20178a6cc8ccc0020',1617706126540,NULL,NULL,_binary '\0',_binary '',_binary '\0',' ','4028ab1063fdfb6d0163fdfc4f000017','4028ab1063fdfb6d0163fdfc5201002d'),('a9fe0bbd78ab1f7e8178abe0f5160007',1617791350038,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063f427af0163f428e8c2013a','4028ab1063f427af0163f428ecbc013f'),('a9fe0bbd94fd12518194fdd394e2000d',1739425289442,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','a9fedb66924113af81926fc5d220084a','a9fedb66924113af81926fc5d2cc084b'),('a9fe0bbd94fd12518194fdd39690000e',1739425289873,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','a9fedb66924113af81926fc5d220084a','a9fe0bbd94591a788194592c9bdf0051'),('a9fe0bbd95ea1ee68196139f6fc208ab',1744085938114,1744086153973,NULL,_binary '\0',_binary '\0',_binary '',' ','a9fedb66924113af81926fc5d220084a','a9fe0bbd95ea1ee68196139f6fad08aa'),('a9fe0bbd95ea1ee6819613ccc3b30908',1744088908723,1744088914708,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063f427af0163f428e8c2013a','a9fe0bbd95ea1ee6819613ccc3b20907'),('a9fe0bbd97a01e0b8197ab42d1420951',1750924972354,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','a9fe0bbd97a01e0b8197ab42d057094f','a9fe0bbd97a01e0b8197ab42d1340950'),('a9fedb66924113af81926fdc1f91089b',1728453549969,NULL,NULL,_binary '\0',_binary '\0',_binary '',' ','4028ab1063fdfb6d0163fdfc4e0e0015','4028ab1063fdfb6d0163fdfc51610026');
/*!40000 ALTER TABLE `spt_quick_link_options` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:11
