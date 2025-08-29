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
-- Table structure for table `spt_certification_group`
--

DROP TABLE IF EXISTS `spt_certification_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_certification_group` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(256) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `attributes` longtext,
  `total_certifications` int(11) DEFAULT NULL,
  `percent_complete` int(11) DEFAULT NULL,
  `completed_certifications` int(11) DEFAULT NULL,
  `certification_definition` varchar(32) DEFAULT NULL,
  `messages` longtext,
  PRIMARY KEY (`id`),
  KEY `spt_cert_grp_perc_comp` (`percent_complete`),
  KEY `spt_cert_group_status` (`status`),
  KEY `spt_cert_group_type` (`type`),
  KEY `FK11B2043263178D65` (`certification_definition`),
  KEY `FK11B20432486634B7` (`assigned_scope`),
  KEY `FK11B20432A5FB1B1` (`owner`),
  KEY `SPT_IDXD9728B9EEB248FD0` (`assigned_scope_path`(255)),
  CONSTRAINT `FK11B20432486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK11B2043263178D65` FOREIGN KEY (`certification_definition`) REFERENCES `spt_certification_definition` (`id`),
  CONSTRAINT `FK11B20432A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_certification_group`
--

LOCK TABLES `spt_certification_group` WRITE;
/*!40000 ALTER TABLE `spt_certification_group` DISABLE KEYS */;
INSERT INTO `spt_certification_group` VALUES ('a9fe0bbd92d11bbd8192d1c8d0420152',1730096451650,1730096458474,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Manager Certification [10/28/24 11:50:51 AM IST]','Certification','Active',' ',1,0,0,'a9fe0bbd92d11bbd8192d1c8cb050150',' '),('a9fe0bbd92d11bbd8192d1cb211f0176',1730096603423,1730096701914,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Application Owner Certification [10/28/24 11:53:23 AM IST]','Certification','Complete',' ',1,100,1,'a9fe0bbd92d11bbd8192d1cb1cf30174',' '),('a9fe0bbd92d11bbd8192d1cd86dc01a6',1730096760540,1730096765395,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Manager Certification [10/28/24 11:56:00 AM IST]','Certification','Active','<Attributes>\n  <Map>\n    <entry key=\"scheduleFrequency\" value=\"Once\"/>\n  </Map>\n</Attributes>\n',1,0,0,'a9fe0bbd92d11bbd8192d1ca9c3f0173',' '),('a9fe0bbd92d11bbd8192d6a1a63d0322',1730177771069,1730177771624,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Entitlement Owner Certification [10/29/24 10:26:11 AM IST]','Certification','Complete',' ',0,100,0,'a9fe0bbd92d11bbd8192d6a1a1e40320','<List>\n  <Message key=\"warn_nothing_to_certify\" type=\"Warn\">\n    <Parameters>\n      <Message key=\"entitlements_lcase\" type=\"Info\"/>\n    </Parameters>\n  </Message>\n</List>\n'),('a9fe0bbd92d11bbd8192d6a2751a0325',1730177824027,1730177824504,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Entitlement Owner Certification [10/29/24 10:27:04 AM IST]','Certification','Complete',' ',0,100,0,'a9fe0bbd92d11bbd8192d6a2722e0323','<List>\n  <Message key=\"warn_nothing_to_certify\" type=\"Warn\">\n    <Parameters>\n      <Message key=\"entitlements_lcase\" type=\"Info\"/>\n    </Parameters>\n  </Message>\n</List>\n'),('a9fe0bbd92d11bbd8192d6b3b12c0344',1730178953516,1730178959371,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Role Membership Certification [10/29/24 10:45:53 AM IST]','Certification','Active',' ',1,0,0,'a9fe0bbd92d11bbd8192d6b3a63d0342',' '),('a9fe0bbd92d11bbd8192d6b601a30353',1730179105187,1730179120427,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Targeted Certification [10/29/24 10:48:25 AM IST]','Certification','Active',' ',1,0,0,'a9fe0bbd92d11bbd8192d6b5fdf30351',' '),('a9fe0bbd92d11bbd8192d6bd2e05038b',1730179575301,1730179580018,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Manager Certification [10/29/24 10:56:15 AM IST]','Certification','Active',' ',1,0,0,'a9fe0bbd92d11bbd8192d6bd2a800389',' '),('a9fe0bbd92d11bbd8192dc7210120816',1730275315730,1730275506914,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Manager Certification [10/30/24 1:31:55 PM IST]','Certification','Complete',' ',1,100,1,'a9fe0bbd92d11bbd8192dc720c2b0814',' '),('a9fe0bbd92d11bbd8192dc7498d00850',1730275481808,1730275489518,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Application Owner Certification [10/30/24 1:34:41 PM IST]','Certification','Active',' ',2,0,0,'a9fe0bbd92d11bbd8192dc749416084e',' '),('a9fe0bbd92d11bbd8192dc7b4a5908b3',1730275920473,1736937812199,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Manager Certification [10/30/24 1:42:00 PM IST]','Certification','Active','<Attributes>\n  <Map>\n    <entry key=\"scheduleFrequency\" value=\"Quarterly\"/>\n  </Map>\n</Attributes>\n',1,0,0,'a9fe0bbd92d11bbd8192dc77655e08aa',' '),('a9fe0bbd94f8111b8194f8f2f2cf0010',1739343459024,1739343522530,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Manager Certification [2/12/25 12:27:36 PM IST]','Certification','Staged','<Attributes>\n  <Map>\n    <entry key=\"scheduleFrequency\" value=\"Quarterly\"/>\n  </Map>\n</Attributes>\n',1,0,0,'a9fe0bbd92d11bbd8192dc77655e08aa',' '),('a9fe0bbd957e1ec68195838b8df501e4',1741668716021,1741668716521,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Entitlement Owner Certification [3/11/25 10:21:55 AM IST]','Certification','Complete',' ',0,100,0,'a9fe0bbd957e1ec68195838b83fc01e2','<List>\n  <Message key=\"warn_nothing_to_certify\" type=\"Warn\">\n    <Parameters>\n      <Message key=\"entitlements_lcase\" type=\"Info\"/>\n    </Parameters>\n  </Message>\n</List>\n'),('a9fe0bbd957e1ec68195838bfeb701e7',1741668744887,1741668745147,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Entitlement Owner Certification [3/11/25 10:22:24 AM IST]','Certification','Complete',' ',0,100,0,'a9fe0bbd957e1ec68195838bfc5501e5','<List>\n  <Message key=\"warn_nothing_to_certify\" type=\"Warn\">\n    <Parameters>\n      <Message key=\"entitlements_lcase\" type=\"Info\"/>\n    </Parameters>\n  </Message>\n</List>\n'),('a9fe0bbd957e1ec68195838e9a8701f1',1741668915847,1741668916044,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Application Owner Certification [3/11/25 10:25:15 AM IST]','Certification','Complete',' ',0,100,0,'a9fe0bbd957e1ec68195838e95e701ef','<List>\n  <Message key=\"warn_nothing_to_certify\" type=\"Warn\">\n    <Parameters>\n      <Message key=\"users_lcase\" type=\"Info\"/>\n    </Parameters>\n  </Message>\n  <Message key=\"warn_no_entitlements_for_app\" type=\"Warn\">\n    <Parameters>\n      <String>PeopleSoft Employee System</String>\n    </Parameters>\n  </Message>\n</List>\n'),('a9fe0bbd957e1ec68195838f538e01f4',1741668963216,1741668976242,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Application Owner Certification [3/11/25 10:26:03 AM IST]','Certification','Active',' ',1,0,0,'a9fe0bbd957e1ec68195838f510d01f2',' '),('a9fe0bbd957e1ec681958394d6030223',1741669324291,1741669327089,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Application Owner Certification [3/11/25 10:32:04 AM IST]','Certification','Complete',' ',0,100,0,'a9fe0bbd957e1ec681958394d1f30221','<List>\n  <Message key=\"nothing_to_certify\" type=\"Warn\">\n    <Parameters>\n      <String>Application Owner Access Review for PeopleSoft Employee System</String>\n      <Message key=\"users_lcase\" type=\"Info\"/>\n    </Parameters>\n  </Message>\n</List>\n'),('a9fe0bbd957e1ec6819583963e770228',1741669416567,1741669418326,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Application Owner Certification [3/11/25 10:33:36 AM IST]','Certification','Complete',' ',0,100,0,'a9fe0bbd957e1ec68195839639fb0226','<List>\n  <Message key=\"nothing_to_certify\" type=\"Warn\">\n    <Parameters>\n      <String>Application Owner Access Review for PeopleSoft Employee System</String>\n      <Message key=\"users_lcase\" type=\"Info\"/>\n    </Parameters>\n  </Message>\n</List>\n'),('a9fe0bbd957e1ec6819583a2ee0a0252',1741670247946,1741670252692,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Application Owner Certification [3/11/25 10:47:27 AM IST]','Certification','Active',' ',1,0,0,'a9fe0bbd957e1ec6819583a2ea1d0250',' '),('a9fe0bbd957e1ec6819583ab659b0276',1741670802843,1741670803120,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Application Owner Certification [3/11/25 10:56:42 AM IST]','Certification','Complete',' ',0,100,0,'a9fe0bbd957e1ec6819583ab622a0274','<List>\n  <Message key=\"warn_nothing_to_certify\" type=\"Warn\">\n    <Parameters>\n      <Message key=\"users_lcase\" type=\"Info\"/>\n    </Parameters>\n  </Message>\n  <Message key=\"warn_no_entitlements_for_app\" type=\"Warn\">\n    <Parameters>\n      <String>App 1</String>\n    </Parameters>\n  </Message>\n</List>\n'),('a9fe0bbd957e1ec68195844f41800316',1741681541504,1741681544953,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Application Owner Certification [3/11/25 1:55:41 PM IST]','Certification','Active',' ',1,0,0,'a9fe0bbd957e1ec68195844f3d890314',' '),('a9fe0bbd957e1ec68195844fd83c0320',1741681580092,1741681580398,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Application Owner Certification [3/11/25 1:56:20 PM IST]','Certification','Complete',' ',0,100,0,'a9fe0bbd957e1ec68195844fd352031e','<List>\n  <Message key=\"warn_nothing_to_certify\" type=\"Warn\">\n    <Parameters>\n      <Message key=\"users_lcase\" type=\"Info\"/>\n    </Parameters>\n  </Message>\n  <Message key=\"warn_no_entitlements_for_app\" type=\"Warn\">\n    <Parameters>\n      <String>App 1</String>\n    </Parameters>\n  </Message>\n</List>\n'),('a9fe0bbd965b1be5819685c0e00d0c1c',1746000732173,1746000756455,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'Manager Certification [4/30/25 1:42:06 PM IST]','Certification','Staged','<Attributes>\n  <Map>\n    <entry key=\"scheduleFrequency\" value=\"Quarterly\"/>\n  </Map>\n</Attributes>\n',1,0,0,'a9fe0bbd92d11bbd8192dc77655e08aa',' ');
/*!40000 ALTER TABLE `spt_certification_group` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:45
