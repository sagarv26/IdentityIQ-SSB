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
-- Table structure for table `spt_entitlement_group`
--

DROP TABLE IF EXISTS `spt_entitlement_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_entitlement_group` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `application` varchar(32) DEFAULT NULL,
  `instance` varchar(128) DEFAULT NULL,
  `native_identity` varchar(322) DEFAULT NULL,
  `display_name` varchar(128) DEFAULT NULL,
  `account_only` bit(1) NOT NULL,
  `attributes` longtext,
  `identity_id` varchar(32) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK13D2B86556651F3A` (`identity_id`),
  KEY `FK13D2B86539D71460` (`application`),
  KEY `FK13D2B865486634B7` (`assigned_scope`),
  KEY `FK13D2B865A5FB1B1` (`owner`),
  KEY `SPT_IDX593FB9116D127176` (`assigned_scope_path`(255)),
  CONSTRAINT `FK13D2B86539D71460` FOREIGN KEY (`application`) REFERENCES `spt_application` (`id`),
  CONSTRAINT `FK13D2B865486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK13D2B86556651F3A` FOREIGN KEY (`identity_id`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK13D2B865A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_entitlement_group`
--

LOCK TABLES `spt_entitlement_group` WRITE;
/*!40000 ALTER TABLE `spt_entitlement_group` DISABLE KEYS */;
INSERT INTO `spt_entitlement_group` VALUES ('a9fe0bbd92d11bbd8192d6984b6002ee',1730177157985,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d45e9a7705ef',NULL,'???','???',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"roles\" value=\"Admin\"/>\n  </Map>\n</Attributes>\n','a9fe0bbd90c41bed8190d472dcb6065a',0),('a9fe0bbd92d11bbd8192dbb0cff606a0',1730262650870,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648',NULL,'2020006','2020006',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"Group\">\n      <value>\n        <List>\n          <String>User</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','a9fe0bbd90c41bed8190d472dba30656',0),('a9fe0bbd92d11bbd8192dbb0cff706a1',1730262650871,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d45e9a7705ef',NULL,'???','???',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"roles\" value=\"Admin\"/>\n  </Map>\n</Attributes>\n','a9fe0bbd90c41bed8190d472dba30656',1),('a9fe0bbd92d11bbd8192f592e65509d6',1730696898133,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648',NULL,'2020005','2020005',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"Group\">\n      <value>\n        <List>\n          <String>User</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','a9fe0bbd90c41bed8190d472da380654',0),('a9fe0bbd94f8111b8194f92093b10093',1739346449329,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648',NULL,'2020015','2020015',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"Group\">\n      <value>\n        <List>\n          <String>Admin</String>\n          <String>Developer</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','a9fe0bbd94f8111b8194f91f2270008e',0),('a9fe0bbd957e1ec6819583a1417c024c',1741670138236,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd957e1ec68195839d91180238',NULL,'2020020','2020020',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"group\">\n      <value>\n        <List>\n          <String>Tester</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','a9fe0bbd957e1ec68195839457b7021e',0),('a9fe0bbd957e1ec6819583a148c7024e',1741670140103,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd957e1ec68195839d91180238',NULL,'2020021','2020021',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"group\">\n      <value>\n        <List>\n          <String>Approver</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','a9fe0bbd957e1ec6819583a1072f0243',0),('a9fe0bbd957e1ec6819588be41fd04a7',1741755924989,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648',NULL,'2020007','2020007',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"Group\">\n      <value>\n        <List>\n          <String>User</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','a9fe0bbd90c41bed8190d472dc1d0658',0),('a9fe0bbd957e1ec6819588be5b7504aa',1741755931509,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648',NULL,'2020009','2020009',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"Group\">\n      <value>\n        <List>\n          <String>Admin</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','a9fe0bbd90c41bed8190d472dd2a065c',0),('a9fe0bbd957e1ec6819588be5b7604ab',1741755931510,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d45e9a7705ef',NULL,'aaa0011','aaa0011',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"roles\" value=\"Developer\"/>\n  </Map>\n</Attributes>\n','a9fe0bbd90c41bed8190d472dd2a065c',1),('a9fe0bbd957e1ec6819588be691504ae',1741755934997,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d45e9a7705ef',NULL,'rdra000','rdra000',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"roles\" value=\"Admin\"/>\n  </Map>\n</Attributes>\n','a9fe0bbd90c41bed8190d55d948d07ad',0),('a9fe0bbd957e1ec6819588be92ac04b4',1741755945644,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648',NULL,'2020012','2020012',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"Group\">\n      <value>\n        <List>\n          <String>Admin</String>\n          <String>Developer</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','a9fedb66920d105081920de9fced0020',0),('a9fe0bbd957e1ec6819588be989804b6',1741755947161,1747113980372,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648',NULL,'2020013','2020013',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"Group\">\n      <value>\n        <List>\n          <String>Admin</String>\n          <String>Developer</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','a9fedb66920d105081920df70a4b0085',0),('a9fe0bbd95ea1ee6819613d314180918',1744089322520,1744089565889,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648',NULL,'20250001','20250001',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"Group\">\n      <value>\n        <List>\n          <String>User</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','a9fe0bbd95ea1ee6819613c4723108f1',0),('a9fedb66924113af819274efbfa40a52',1728538722212,1730892026042,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d46ece020648',NULL,'2020014','2020014',_binary '\0','<Attributes>\n  <Map>\n    <entry key=\"Group\">\n      <value>\n        <List>\n          <String>Admin</String>\n          <String>Developer</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n','a9fedb66920d105081920ee9f8d4022c',0);
/*!40000 ALTER TABLE `spt_entitlement_group` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:23
