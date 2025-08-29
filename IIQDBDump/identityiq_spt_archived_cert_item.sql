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
-- Table structure for table `spt_archived_cert_item`
--

DROP TABLE IF EXISTS `spt_archived_cert_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_archived_cert_item` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `sub_type` varchar(255) DEFAULT NULL,
  `item_id` varchar(128) DEFAULT NULL,
  `exception_application` varchar(128) DEFAULT NULL,
  `exception_attribute_name` varchar(255) DEFAULT NULL,
  `exception_attribute_value` varchar(2048) DEFAULT NULL,
  `exception_permission_target` varchar(255) DEFAULT NULL,
  `exception_permission_right` varchar(255) DEFAULT NULL,
  `exception_native_identity` varchar(322) DEFAULT NULL,
  `constraint_name` varchar(2000) DEFAULT NULL,
  `policy` varchar(256) DEFAULT NULL,
  `bundle` varchar(255) DEFAULT NULL,
  `violation_summary` varchar(256) DEFAULT NULL,
  `entitlements` longtext,
  `parent_id` varchar(32) DEFAULT NULL,
  `target_display_name` varchar(255) DEFAULT NULL,
  `target_name` varchar(255) DEFAULT NULL,
  `target_id` varchar(255) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `spt_arch_cert_item_tdisplay` (`target_display_name`),
  KEY `spt_arch_cert_item_tname` (`target_name`),
  KEY `spt_arch_item_app` (`exception_application`),
  KEY `spt_arch_item_policy` (`policy`(255)),
  KEY `spt_arch_cert_item_type` (`type`),
  KEY `spt_arch_item_native_id` (`exception_native_identity`(255)),
  KEY `spt_arch_item_bundle` (`bundle`),
  KEY `FK764147B9486634B7` (`assigned_scope`),
  KEY `FK764147B9BAC8DC8B` (`parent_id`),
  KEY `FK764147B9A5FB1B1` (`owner`),
  KEY `SPT_IDXE4B09B655AF1E31E` (`assigned_scope_path`(255)),
  CONSTRAINT `FK764147B9486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK764147B9A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK764147B9BAC8DC8B` FOREIGN KEY (`parent_id`) REFERENCES `spt_archived_cert_entity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_archived_cert_item`
--

LOCK TABLES `spt_archived_cert_item` WRITE;
/*!40000 ALTER TABLE `spt_archived_cert_item` DISABLE KEYS */;
INSERT INTO `spt_archived_cert_item` VALUES ('a9fe0bbd92d11bbd8192d1cd8bd101aa',1730096761809,NULL,NULL,NULL,NULL,'Bundle','AssignedRole','fb24e133d3d942d1a1cd43d88e0d5c86',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'User',NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020006\" nativeIdentity=\"2020006\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\">\n          <value>\n            <Set>\n              <String>User</String>\n            </Set>\n          </value>\n        </entry>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd92d11bbd8192d1cd8bcd01a9','User','User','a9fe0bbd92d11bbd8192d1b6e5b900c7',0),('a9fe0bbd92d11bbd8192d1cd8bd101ab',1730096761809,NULL,NULL,NULL,NULL,'PolicyViolation',NULL,'ba24b57fb00641b9b765fa668b784d24',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Role Rule',' ','a9fe0bbd92d11bbd8192d1cd8bcd01a9',NULL,NULL,NULL,1),('a9fe0bbd92d11bbd8192dc721770081c',1730275317616,NULL,NULL,NULL,NULL,'Bundle','AssignedRole','b4fe3a14e72f44958ae2949f6282aa4c',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'User',NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020006\" nativeIdentity=\"2020006\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\">\n          <value>\n            <Set>\n              <String>User</String>\n            </Set>\n          </value>\n        </entry>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd92d11bbd8192dc72176c081b','User','User','a9fe0bbd92d11bbd8192d1b6e5b900c7',0),('a9fe0bbd92d11bbd8192dc721770081d',1730275317616,NULL,NULL,NULL,NULL,'Exception',NULL,'1ff6028982d1423aa3a0dea99c810b48','Contractor System Employee','Group','User',NULL,NULL,'2020006',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020006\" nativeIdentity=\"2020006\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\" value=\"User\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd92d11bbd8192dc72176c081b',NULL,NULL,NULL,1),('a9fe0bbd92d11bbd8192dc721770081e',1730275317616,NULL,NULL,NULL,NULL,'Exception',NULL,'00e44ca3a40045e4aafed8745224cbba','HR System','roles','Admin',NULL,NULL,'???',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"HR System\" displayName=\"???\" nativeIdentity=\"???\">\n    <Attributes>\n      <Map>\n        <entry key=\"roles\" value=\"Admin\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd92d11bbd8192dc72176c081b',NULL,NULL,NULL,2),('a9fe0bbd92d11bbd8192dc721771081f',1730275317617,NULL,NULL,NULL,NULL,'PolicyViolation',NULL,'bbeff9f977604fdb92ad8bf582fcd34f',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Role Rule',' ','a9fe0bbd92d11bbd8192dc72176c081b',NULL,NULL,NULL,3),('a9fe0bbd92d11bbd8192dc7b4d1908b7',1730275921177,NULL,NULL,NULL,NULL,'Bundle','AssignedRole','c5dc71ff241247a7a9776b48c11d8e03',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'User',NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020006\" nativeIdentity=\"2020006\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\">\n          <value>\n            <Set>\n              <String>User</String>\n            </Set>\n          </value>\n        </entry>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd92d11bbd8192dc7b4d1508b6','User','User','a9fe0bbd92d11bbd8192d1b6e5b900c7',0),('a9fe0bbd92d11bbd8192dc7b4d1908b8',1730275921177,NULL,NULL,NULL,NULL,'Exception',NULL,'29dca0568dcf4368807803f5688cf05d','Contractor System Employee','Group','User',NULL,NULL,'2020006',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020006\" nativeIdentity=\"2020006\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\" value=\"User\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd92d11bbd8192dc7b4d1508b6',NULL,NULL,NULL,1),('a9fe0bbd92d11bbd8192dc7b4d1a08b9',1730275921178,NULL,NULL,NULL,NULL,'Exception',NULL,'3a6a85fe192c47b6bc8d22aec152b104','HR System','roles','Admin',NULL,NULL,'???',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"HR System\" displayName=\"???\" nativeIdentity=\"???\">\n    <Attributes>\n      <Map>\n        <entry key=\"roles\" value=\"Admin\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd92d11bbd8192dc7b4d1508b6',NULL,NULL,NULL,2),('a9fe0bbd92d11bbd8192dc7b4d1a08ba',1730275921178,NULL,NULL,NULL,NULL,'PolicyViolation',NULL,'0156624bb7a64812ab852050a85b0927',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Role Rule',' ','a9fe0bbd92d11bbd8192dc7b4d1508b6',NULL,NULL,NULL,3),('a9fe0bbd94f8111b8194f8f3a2830015',1739343504003,NULL,NULL,NULL,NULL,'Exception',NULL,'b846ec81a8c046e4a25029dfa94d410d','Contractor System Employee','Group','User',NULL,NULL,'2020005',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020005\" nativeIdentity=\"2020005\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\" value=\"User\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd94f8111b8194f8f3a2830014',NULL,NULL,NULL,0),('a9fe0bbd94f8111b8194f8f3a7070019',1739343505159,NULL,NULL,NULL,NULL,'Bundle','AssignedRole','4ce6faf1580a43efabad7ea5a54ec050',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'User',NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020006\" nativeIdentity=\"2020006\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\">\n          <value>\n            <Set>\n              <String>User</String>\n            </Set>\n          </value>\n        </entry>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd94f8111b8194f8f3a6f80018','User','User','a9fe0bbd92d11bbd8192d1b6e5b900c7',0),('a9fe0bbd94f8111b8194f8f3a707001a',1739343505159,NULL,NULL,NULL,NULL,'Exception',NULL,'df4d6c362b7b46be95d8ccb81c09848b','Contractor System Employee','Group','User',NULL,NULL,'2020006',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020006\" nativeIdentity=\"2020006\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\" value=\"User\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd94f8111b8194f8f3a6f80018',NULL,NULL,NULL,1),('a9fe0bbd94f8111b8194f8f3a707001b',1739343505159,NULL,NULL,NULL,NULL,'Exception',NULL,'88b7d4fb591f4231b6efb7881f35e5db','HR System','roles','Admin',NULL,NULL,'???',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"HR System\" displayName=\"???\" nativeIdentity=\"???\">\n    <Attributes>\n      <Map>\n        <entry key=\"roles\" value=\"Admin\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd94f8111b8194f8f3a6f80018',NULL,NULL,NULL,2),('a9fe0bbd94f8111b8194f8f3a707001c',1739343505159,NULL,NULL,NULL,NULL,'PolicyViolation',NULL,'7b3d6352659d4281adb1b2d340b99023',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Role Rule',' ','a9fe0bbd94f8111b8194f8f3a6f80018',NULL,NULL,NULL,3),('a9fe0bbd94f8111b8194f8f3b5500027',1739343508816,NULL,NULL,NULL,NULL,'Exception',NULL,'e4a6f1aa328b47738d9cc5ec471a9324','Contractor System Employee','Group','Admin',NULL,NULL,'2020014',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020014\" nativeIdentity=\"2020014\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\" value=\"Admin\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd94f8111b8194f8f3b5400026',NULL,NULL,NULL,0),('a9fe0bbd94f8111b8194f8f3b5500028',1739343508816,NULL,NULL,NULL,NULL,'Exception',NULL,'f704e89b3bb84b27a3681db205e29b87','Contractor System Employee','Group','Developer',NULL,NULL,'2020014',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020014\" nativeIdentity=\"2020014\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\" value=\"Developer\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd94f8111b8194f8f3b5400026',NULL,NULL,NULL,1),('a9fe0bbd965b1be5819685c102ac0c20',1746000741036,NULL,NULL,NULL,NULL,'Exception',NULL,'20865f3359694a6bb68ea036f2797d4f','Contractor System Employee','Group','User',NULL,NULL,'2020005',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020005\" nativeIdentity=\"2020005\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\" value=\"User\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd965b1be5819685c101ef0c1f',NULL,NULL,NULL,0),('a9fe0bbd965b1be5819685c10df90c23',1746000743929,NULL,NULL,NULL,NULL,'Bundle','AssignedRole','c29bec86c4dd4395b14a180487f0e620',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'User',NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020006\" nativeIdentity=\"2020006\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\">\n          <value>\n            <Set>\n              <String>User</String>\n            </Set>\n          </value>\n        </entry>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd965b1be5819685c10d470c22','User','User','a9fe0bbd92d11bbd8192d1b6e5b900c7',0),('a9fe0bbd965b1be5819685c10df90c24',1746000743930,NULL,NULL,NULL,NULL,'Exception',NULL,'978d4bc412574c80be6a70bec141a8ea','Contractor System Employee','Group','User',NULL,NULL,'2020006',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020006\" nativeIdentity=\"2020006\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\" value=\"User\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd965b1be5819685c10d470c22',NULL,NULL,NULL,1),('a9fe0bbd965b1be5819685c10dfa0c25',1746000743930,NULL,NULL,NULL,NULL,'Exception',NULL,'02a07b534c0744bcbd790f00ad0a5226','HR System','roles','Admin',NULL,NULL,'???',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"HR System\" displayName=\"???\" nativeIdentity=\"???\">\n    <Attributes>\n      <Map>\n        <entry key=\"roles\" value=\"Admin\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd965b1be5819685c10d470c22',NULL,NULL,NULL,2),('a9fe0bbd965b1be5819685c10dfa0c26',1746000743930,NULL,NULL,NULL,NULL,'PolicyViolation',NULL,'5ab60d2e320945cba0f008a540a1995e',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Role Rule',' ','a9fe0bbd965b1be5819685c10d470c22',NULL,NULL,NULL,3),('a9fe0bbd965b1be5819685c11aa00c31',1746000747262,NULL,NULL,NULL,NULL,'Exception',NULL,'b64d7531a9e64755b816cc20c528464a','Contractor System Employee','Group','Admin',NULL,NULL,'2020014',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020014\" nativeIdentity=\"2020014\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\" value=\"Admin\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd965b1be5819685c11a9e0c30',NULL,NULL,NULL,0),('a9fe0bbd965b1be5819685c11afe0c32',1746000747262,NULL,NULL,NULL,NULL,'Exception',NULL,'fb2d728415664f60b212890656d00971','Contractor System Employee','Group','Developer',NULL,NULL,'2020014',NULL,NULL,NULL,NULL,'<List>\n  <EntitlementSnapshot application=\"Contractor System Employee\" displayName=\"2020014\" nativeIdentity=\"2020014\">\n    <Attributes>\n      <Map>\n        <entry key=\"Group\" value=\"Developer\"/>\n      </Map>\n    </Attributes>\n  </EntitlementSnapshot>\n</List>\n','a9fe0bbd965b1be5819685c11a9e0c30',NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `spt_archived_cert_item` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:12
