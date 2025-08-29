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
-- Table structure for table `spt_identity_entitlement`
--

DROP TABLE IF EXISTS `spt_identity_entitlement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_identity_entitlement` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `start_date` bigint(20) DEFAULT NULL,
  `end_date` bigint(20) DEFAULT NULL,
  `attributes` longtext,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(450) DEFAULT NULL,
  `annotation` varchar(450) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `native_identity` varchar(450) DEFAULT NULL,
  `instance` varchar(128) DEFAULT NULL,
  `application` varchar(32) DEFAULT NULL,
  `identity_id` varchar(32) NOT NULL,
  `aggregation_state` varchar(255) DEFAULT NULL,
  `source` varchar(64) DEFAULT NULL,
  `assigned` bit(1) DEFAULT NULL,
  `allowed` bit(1) DEFAULT NULL,
  `granted_by_role` bit(1) DEFAULT NULL,
  `assigner` varchar(128) DEFAULT NULL,
  `assignment_id` varchar(64) DEFAULT NULL,
  `assignment_note` varchar(1024) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `request_item` varchar(32) DEFAULT NULL,
  `pending_request_item` varchar(32) DEFAULT NULL,
  `certification_item` varchar(32) DEFAULT NULL,
  `pending_certification_item` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `spt_identity_ent_assgnid` (`assignment_id`),
  KEY `spt_identity_ent_source_ci` (`source`),
  KEY `spt_identity_ent_ag_state` (`aggregation_state`),
  KEY `spt_identity_ent_role_granted` (`granted_by_role`),
  KEY `spt_identity_ent_assigned` (`assigned`),
  KEY `spt_identity_ent_allowed` (`allowed`),
  KEY `spt_identity_ent_value_ci` (`value`(255)),
  KEY `spt_identity_ent_nativeid_ci` (`native_identity`(255)),
  KEY `spt_identity_ent_name_ci` (`name`),
  KEY `spt_identity_ent_instance_ci` (`instance`),
  KEY `spt_identity_ent_type` (`type`),
  KEY `FK1134F4B456651F3A` (`identity_id`),
  KEY `FK1134F4B439D71460` (`application`),
  KEY `FK1134F4B47AEC327` (`request_item`),
  KEY `FK1134F4B4D9C563CD` (`pending_certification_item`),
  KEY `FK1134F4B484ACD425` (`certification_item`),
  KEY `FK1134F4B4FFB630CF` (`pending_request_item`),
  KEY `FK1134F4B4A5FB1B1` (`owner`),
  KEY `spt_identity_entitlement_comp` (`identity_id`,`application`,`native_identity`(175),`instance`(16)),
  KEY `spt_ident_entit_comp_name` (`identity_id`,`name`(223)),
  CONSTRAINT `FK1134F4B439D71460` FOREIGN KEY (`application`) REFERENCES `spt_application` (`id`),
  CONSTRAINT `FK1134F4B456651F3A` FOREIGN KEY (`identity_id`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK1134F4B47AEC327` FOREIGN KEY (`request_item`) REFERENCES `spt_identity_request_item` (`id`),
  CONSTRAINT `FK1134F4B484ACD425` FOREIGN KEY (`certification_item`) REFERENCES `spt_certification_item` (`id`),
  CONSTRAINT `FK1134F4B4A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK1134F4B4D9C563CD` FOREIGN KEY (`pending_certification_item`) REFERENCES `spt_certification_item` (`id`),
  CONSTRAINT `FK1134F4B4FFB630CF` FOREIGN KEY (`pending_request_item`) REFERENCES `spt_identity_request_item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_identity_entitlement`
--

LOCK TABLES `spt_identity_entitlement` WRITE;
/*!40000 ALTER TABLE `spt_identity_entitlement` DISABLE KEYS */;
INSERT INTO `spt_identity_entitlement` VALUES ('a9fe0bbd92d11bbd8192d196fe5b002e',1730093186651,NULL,NULL,NULL,NULL,NULL,'Group','User',NULL,'2020007','2020007',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fe0bbd90c41bed8190d472dc1d0658','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fe0bbd92d11bbd8192d19ccc920046',1730093567122,1746000748971,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"sourceDetectedRoles\" value=\"User IT\"/>\n  </Map>\n</Attributes>\n','Group','User',NULL,'2020012','2020012',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fedb66920d105081920de9fced0020','Connected','Aggregation',_binary '\0',_binary '\0',_binary '',NULL,NULL,NULL,'Entitlement',NULL,NULL,'a9fe0bbd92d11bbd8192dc72195b082b','a9fe0bbd965b1be5819685c11dd90c41'),('a9fe0bbd92d11bbd8192d1aaf0a00092',1730094493856,1741756010020,NULL,NULL,NULL,NULL,'Group','User',NULL,'2020014','2020014',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fedb66920d105081920ee9f8d4022c','Disconnected','LCM',_binary '\0',_binary '\0',_binary '\0','The Administrator','6528de3fbc084b11bca690f1ce288744',NULL,'Entitlement',NULL,'a9fe0bbd92d11bbd8192d1aae87e008f',NULL,NULL),('a9fe0bbd92d11bbd8192d1ac99ff00a6',1730094602752,1741756004909,NULL,NULL,NULL,NULL,'Group','User',NULL,'2020009','2020009',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fe0bbd90c41bed8190d472dd2a065c','Disconnected','LCM',_binary '\0',_binary '\0',_binary '\0','The Administrator','b0e6308ed3f6487a92bf804f57ead538',NULL,'Entitlement',NULL,'a9fe0bbd92d11bbd8192d1ac8b3a00a2',NULL,NULL),('a9fe0bbd92d11bbd8192d1b8cd3400d6',1730095402292,1730179576654,NULL,NULL,NULL,NULL,'assignedRoles','User',NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d472dba30656','Disconnected','LCM',_binary '\0',_binary '\0',_binary '\0','spadmin','76c6a8c0e0854f949813c5bcde03a8ec',NULL,'Entitlement','a9fe0bbd92d11bbd8192d1b8c75700d1',NULL,NULL,'a9fe0bbd92d11bbd8192d6bd3332038f'),('a9fe0bbd92d11bbd8192d1bb6c910106',1730095574161,1746000748635,NULL,NULL,NULL,NULL,'assignedRoles','User',NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d472dcb6065a','Disconnected','LCM',_binary '\0',_binary '\0',_binary '\0','spadmin','03ee009dc1204f6a9d53b2fa42428f29',NULL,'Entitlement','a9fe0bbd92d11bbd8192d1bb5a3a0100',NULL,'a9fe0bbd92d11bbd8192dc7219560821','a9fe0bbd965b1be5819685c11d110c39'),('a9fe0bbd92d11bbd8192d1be04660122',1730095744103,1746000748635,NULL,NULL,NULL,NULL,'assignedRoles','Admin',NULL,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d472dcb6065a','Disconnected','LCM',_binary '\0',_binary '\0',_binary '\0','spadmin','5e808e2dc8974f4e9b69d1ffd1dc6f4b',NULL,'Entitlement','a9fe0bbd92d11bbd8192d1bdfdd2011d',NULL,'a9fe0bbd92d11bbd8192dc7219560822','a9fe0bbd965b1be5819685c11d3c0c3a'),('a9fe0bbd92d11bbd8192d1be047b0123',1730095744123,1746000748635,NULL,NULL,NULL,NULL,'roles','Admin',NULL,'???','???',NULL,'a9fe0bbd90c41bed8190d45e9a7705ef','a9fe0bbd90c41bed8190d472dcb6065a','Connected','LCM',_binary '\0',_binary '\0',_binary '\0','The Administrator',NULL,NULL,'Entitlement','a9fe0bbd92d11bbd8192d1be04110120',NULL,'a9fe0bbd92d11bbd8192dc7219570823','a9fe0bbd965b1be5819685c11d9c0c3b'),('a9fe0bbd92d11bbd8192d6d27cb803f2',1730180971704,1730275486434,NULL,NULL,NULL,NULL,'roles','Admin',NULL,'???','???',NULL,'a9fe0bbd90c41bed8190d45e9a7705ef','a9fe0bbd90c41bed8190d472dba30656','Connected','LCM',_binary '',_binary '\0',_binary '\0','The Administrator','3bc6a53d69e843d1834da1054281b558',NULL,'Entitlement','a9fe0bbd92d11bbd8192d6d2766c03ef',NULL,NULL,'a9fe0bbd92d11bbd8192dc74aad80866'),('a9fe0bbd92d11bbd8192d6dbab5b041b',1730181573468,1741756003371,NULL,NULL,NULL,NULL,'Group','Developer',NULL,'2020006','2020006',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fe0bbd90c41bed8190d472dba30656','Disconnected','LCM',_binary '\0',_binary '\0',_binary '\0','The Administrator','89428182af964fa58e38edf3da5147a2',NULL,'Entitlement',NULL,'a9fe0bbd92d11bbd8192d6dba5660418',NULL,NULL),('a9fe0bbd92d11bbd8192d6f9ac4a04a5',1730183539787,1741756003371,NULL,NULL,NULL,NULL,'Group','Admin',NULL,'2020006','2020006',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fe0bbd90c41bed8190d472dba30656','Disconnected','LCM',_binary '\0',_binary '\0',_binary '\0','The Administrator','f3f8e67710e045a3a326221f94262a8f',NULL,'Entitlement',NULL,'a9fe0bbd92d11bbd8192d6f9a74404a2',NULL,NULL),('a9fe0bbd94f8111b8194f91f23a30090',1739346355107,1746000749002,NULL,NULL,NULL,NULL,'Group','Admin',NULL,'2020015','2020015',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fe0bbd94f8111b8194f91f2270008e','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,'a9fe0bbd965b1be5819685c11e170c48'),('a9fe0bbd94f8111b8194f91f23a50091',1739346355110,1746000749002,NULL,NULL,NULL,NULL,'Group','Developer',NULL,'2020015','2020015',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fe0bbd94f8111b8194f91f2270008e','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,'a9fe0bbd965b1be5819685c11e180c49'),('a9fe0bbd957e1ec6819583a109470246',1741670123847,1746000749061,NULL,NULL,NULL,NULL,'group','Approver',NULL,'2020021','2020021',NULL,'a9fe0bbd957e1ec68195839d91180238','a9fe0bbd957e1ec6819583a1072f0243','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,'a9fe0bbd965b1be5819685c11e3f0c4f'),('a9fe0bbd957e1ec6819583a10b03024a',1741670124291,1746000749021,NULL,NULL,NULL,NULL,'group','Tester',NULL,'2020020','2020020',NULL,'a9fe0bbd957e1ec68195839d91180238','a9fe0bbd957e1ec68195839457b7021e','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,'a9fe0bbd965b1be5819685c11e370c4c'),('a9fe0bbd957e1ec6819583a143c7024d',1741670138823,1746000749003,NULL,NULL,NULL,NULL,'assignedRoles','Business Role Demo',NULL,NULL,NULL,NULL,NULL,'a9fe0bbd957e1ec68195839457b7021e',NULL,'Rule',_binary '\0',_binary '\0',_binary '\0',NULL,'7c5ed5c0f59d4a8a96ebafdb2de93e77',NULL,NULL,NULL,NULL,NULL,'a9fe0bbd965b1be5819685c11e360c4b'),('a9fe0bbd957e1ec6819583a14a17024f',1741670140439,1746000749061,NULL,NULL,NULL,NULL,'assignedRoles','Business Role Demo',NULL,NULL,NULL,NULL,NULL,'a9fe0bbd957e1ec6819583a1072f0243',NULL,'Rule',_binary '\0',_binary '\0',_binary '\0',NULL,'8390d70682da4563a111864bc70613ab',NULL,NULL,NULL,NULL,NULL,'a9fe0bbd965b1be5819685c11e380c4e'),('a9fe0bbd957e1ec6819583aaf00e0273',1741670772750,1746000749063,NULL,NULL,NULL,NULL,'assignedRoles','Business Role Demo',NULL,NULL,NULL,NULL,NULL,'a9fe0bbd957e1ec6819583aa9109026f',NULL,'Rule',_binary '\0',_binary '\0',_binary '\0',NULL,'fb6ed92eebf046cfad88bb8fc8669fbe',NULL,NULL,NULL,NULL,NULL,'a9fe0bbd965b1be5819685c11e410c51'),('a9fe0bbd957e1ec6819588be925e04b3',1741755945566,1746000748971,NULL,NULL,NULL,NULL,'detectedRoles','User IT',NULL,NULL,NULL,NULL,NULL,'a9fedb66920d105081920de9fced0020',NULL,NULL,_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a9fe0bbd965b1be5819685c11dd90c41'),('a9fe0bbd95ea1ee6819613c4756508f3',1744088364389,1746000749064,NULL,NULL,NULL,NULL,'Group','User',NULL,'20250001','20250001',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fe0bbd95ea1ee6819613c4723108f1','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,'a9fe0bbd965b1be5819685c11e410c53'),('a9fe0bbd965b1be58196a497414c1945',1746518098270,NULL,NULL,NULL,NULL,NULL,'group','admin',NULL,'2020021','2020021',NULL,'a9fe0bbd940617a1819411d5df7801da','a9fe0bbd957e1ec6819583a1072f0243','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fe0bbd965b1be58196a49745c81948',1746518099400,NULL,NULL,NULL,NULL,NULL,'group','dev',NULL,'2020022','2020022',NULL,'a9fe0bbd940617a1819411d5df7801da','a9fe0bbd965b1be58196a49742831946','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fe0bbd965b1be58196a497473e194b',1746518099774,NULL,NULL,NULL,NULL,NULL,'group','tester',NULL,'2020023','2020023',NULL,'a9fe0bbd940617a1819411d5df7801da','a9fe0bbd965b1be58196a49746aa1949','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fe0bbd965b1be58196a4974a5c194d',1746518100572,NULL,NULL,NULL,NULL,NULL,'group','admin',NULL,'2020020','2020020',NULL,'a9fe0bbd940617a1819411d5df7801da','a9fe0bbd957e1ec68195839457b7021e','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fe0bbd965b1be58196a4974b7a1950',1746518100858,NULL,NULL,NULL,NULL,NULL,'group','dev',NULL,'2020024','2020024',NULL,'a9fe0bbd940617a1819411d5df7801da','a9fe0bbd965b1be58196a4974ad0194e','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fe0bbd96c816468196c82786f7004a',1747114755832,NULL,NULL,NULL,NULL,NULL,'group','admin',NULL,'aaa0012','aaa0012',NULL,'a9fe0bbd965b1be58196b3a7b67a24ab','a9fedb66920d105081920de9fced0020','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fe0bbd96c816468196c8278ac3004d',1747114756803,NULL,NULL,NULL,NULL,NULL,'group','admin',NULL,'aaa0013','aaa0013',NULL,'a9fe0bbd965b1be58196b3a7b67a24ab','a9fedb66920d105081920df70a4b0085','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fe0bbd96c816468196c8278bc5004f',1747114757061,NULL,NULL,NULL,NULL,NULL,'group','admin',NULL,'aaa0014','aaa0014',NULL,'a9fe0bbd965b1be58196b3a7b67a24ab','a9fedb66920d105081920ee9f8d4022c','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fe0bbd96c816468196c8278d240052',1747114757412,NULL,NULL,NULL,NULL,NULL,'group','Tester',NULL,'6','6',NULL,'a9fe0bbd965b1be58196b3a7b67a24ab','a9fe0bbd96c816468196c8278cc20050','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fe0bbd96c818208196c82930a30005',1747114864803,NULL,NULL,NULL,NULL,NULL,'group','User',NULL,'aaa0014','aaa0014',NULL,'a9fe0bbd965b1be58196b3a7b67a24ab','a9fe0bbd957e1ec6819583a1072f0243','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fedb6691bb1afa8191bbc297e200a7',1725431977955,1746000748968,NULL,NULL,NULL,' ','Group','Admin',NULL,'2020009','2020009',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fe0bbd90c41bed8190d472dd2a065c','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,'a9fe0bbd92d11bbd8192dc7219590826','a9fe0bbd965b1be5819685c11db70c3e'),('a9fedb66920d105081920de9fdae0022',1726810291630,1746000748971,NULL,NULL,NULL,' ','Group','Admin',NULL,'2020012','2020012',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fedb66920d105081920de9fced0020','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,'a9fe0bbd92d11bbd8192dc72195b0829','a9fe0bbd965b1be5819685c11e040c42'),('a9fedb66920d105081920de9fdaf0023',1726810291631,1746000748972,NULL,NULL,NULL,NULL,'Group','Developer',NULL,'2020012','2020012',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fedb66920d105081920de9fced0020','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,'a9fe0bbd92d11bbd8192dc72195b082a','a9fe0bbd965b1be5819685c11e050c43'),('a9fedb66920d105081920df70b420087',1726811147074,1746000748999,NULL,NULL,NULL,' ','Group','Admin',NULL,'2020013','2020013',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fedb66920d105081920df70a4b0085','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,'a9fe0bbd92d11bbd8192dc7219650831','a9fe0bbd965b1be5819685c11e070c45'),('a9fedb66920d105081920df70b430088',1726811147075,1746000748999,NULL,NULL,NULL,NULL,'Group','Developer',NULL,'2020013','2020013',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fedb66920d105081920df70a4b0085','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,'a9fe0bbd92d11bbd8192dc7219660832','a9fe0bbd965b1be5819685c11e080c46'),('a9fedb66920d105081920ee9fabd022e',1726827068093,1741668968317,NULL,NULL,NULL,' ','Group','Admin',NULL,'2020014','2020014',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fedb66920d105081920ee9f8d4022c','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,'a9fe0bbd92d11bbd8192dc7219670835','a9fe0bbd957e1ec68195838f670e0208'),('a9fedb66920d105081920ee9fabe022f',1726827068094,1741668968317,NULL,NULL,NULL,NULL,'Group','Developer',NULL,'2020014','2020014',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fedb66920d105081920ee9f8d4022c','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,'a9fe0bbd92d11bbd8192dc7219670836','a9fe0bbd957e1ec68195838f67100209'),('a9fedb66924113af819250e8b81102f4',1727934281746,NULL,NULL,NULL,NULL,NULL,'roles','Admin',NULL,'rdra000','rdra000',NULL,'a9fe0bbd90c41bed8190d45e9a7705ef','a9fe0bbd90c41bed8190d55d948d07ad','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,NULL),('a9fedb66924113af819250e8bb1102f6',1727934282514,1746000748968,NULL,NULL,NULL,NULL,'roles','Developer',NULL,'aaa0011','aaa0011',NULL,'a9fe0bbd90c41bed8190d45e9a7705ef','a9fe0bbd90c41bed8190d472dd2a065c','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,'a9fe0bbd965b1be5819685c11db80c3f'),('a9fedb66924113af819250e8be4002f8',1727934283328,1746000748971,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"sourceDetectedRoles\" value=\"User IT\"/>\n  </Map>\n</Attributes>\n','roles','User',NULL,'aaa0012','aaa0012',NULL,'a9fe0bbd90c41bed8190d45e9a7705ef','a9fedb66920d105081920de9fced0020','Connected','Aggregation',_binary '\0',_binary '\0',_binary '',NULL,NULL,NULL,'Entitlement',NULL,NULL,'a9fe0bbd92d11bbd8192dc72195b082c','a9fe0bbd965b1be5819685c11dd90c41'),('a9fedb66924113af819250f03664030f',1727934772836,1741668968256,NULL,NULL,NULL,NULL,'Group','User',NULL,'2020005','2020005',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fe0bbd90c41bed8190d472da380654','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,'a9fe0bbd957e1ec68195838f66bc01f9'),('a9fedb66924113af819250f0392d0310',1727934773549,1741668968269,NULL,NULL,NULL,' ','Group','User',NULL,'2020006','2020006',NULL,'a9fe0bbd90c41bed8190d46ece020648','a9fe0bbd90c41bed8190d472dba30656','Connected','Aggregation',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'Entitlement',NULL,NULL,NULL,'a9fe0bbd957e1ec68195838f66e001fb');
/*!40000 ALTER TABLE `spt_identity_entitlement` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:40
