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
-- Table structure for table `spt_request_definition`
--

DROP TABLE IF EXISTS `spt_request_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_request_definition` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(4000) DEFAULT NULL,
  `executor` varchar(255) DEFAULT NULL,
  `form_path` varchar(128) DEFAULT NULL,
  `template` bit(1) DEFAULT NULL,
  `hidden` bit(1) DEFAULT NULL,
  `result_expiration` int(11) DEFAULT NULL,
  `progress_interval` int(11) DEFAULT NULL,
  `sub_type` varchar(128) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `progress_mode` varchar(255) DEFAULT NULL,
  `arguments` longtext,
  `parent` varchar(32) DEFAULT NULL,
  `retry_max` int(11) DEFAULT NULL,
  `retry_interval` int(11) DEFAULT NULL,
  `sig_description` longtext,
  `return_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FKF976608B486634B7` (`assigned_scope`),
  KEY `FKF976608BA5FB1B1` (`owner`),
  KEY `FKF976608B319F1FAC` (`parent`),
  KEY `SPT_IDX9393E3B78D0A4442` (`assigned_scope_path`(255)),
  CONSTRAINT `FKF976608B319F1FAC` FOREIGN KEY (`parent`) REFERENCES `spt_request_definition` (`id`),
  CONSTRAINT `FKF976608B486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKF976608BA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_request_definition`
--

LOCK TABLES `spt_request_definition` WRITE;
/*!40000 ALTER TABLE `spt_request_definition` DISABLE KEYS */;
INSERT INTO `spt_request_definition` VALUES ('297e8b3d78ab76ff0178ab78ac85001a',1617784515717,NULL,NULL,NULL,NULL,'Certification Builder',NULL,'sailpoint.request.CertificationBuilderExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"maxThreads\" value=\"4\"/>\n  </Map>\n</Attributes>\n',NULL,0,0,NULL,NULL),('297e8b3d78ab76ff0178ab78acc3001b',1617784515779,NULL,NULL,NULL,NULL,'Application Status Request',NULL,'sailpoint.request.ApplicationStatusRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,'Event',NULL,' ',NULL,0,0,NULL,NULL),('4028ab1063f427af0163f428e0b80120',1528809709753,1617705336037,NULL,NULL,NULL,'Integration Request',NULL,'sailpoint.request.IntegrationRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,' ',NULL,20,0,NULL,NULL),('4028ab1063f427af0163f428e0f20121',1528809709810,1617705336266,NULL,NULL,NULL,'Service Request',NULL,'sailpoint.request.ServiceRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"hostSpecific\" value=\"true\"/>\n  </Map>\n</Attributes>\n',NULL,0,0,NULL,NULL),('4028ab1063f427af0163f428e12e0122',1528809709870,1729747636500,NULL,NULL,NULL,'Email Request',NULL,'sailpoint.request.EmailRequestExecutor',NULL,_binary '\0',_binary '\0',7,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"maxQueue\" value=\"10\"/>\n    <entry key=\"requestProcessorNoInterrupt\" value=\"true\"/>\n  </Map>\n</Attributes>\n',NULL,20,0,NULL,NULL),('4028ab1063f427af0163f428e1ac0123',1528809709997,1617705336389,NULL,NULL,NULL,'Certification Refresh Request',NULL,'sailpoint.request.CertificationRefreshRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,' ',NULL,20,0,NULL,NULL),('4028ab1063f427af0163f428e1d00124',1528809710032,1617705336497,NULL,NULL,NULL,'Identity Trigger Request',NULL,'sailpoint.request.IdentityTriggerRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,' ',NULL,20,0,NULL,NULL),('4028ab1063f427af0163f428e2320125',1528809710131,1617705336732,NULL,NULL,NULL,'Workflow Request',NULL,'sailpoint.request.WorkflowRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,'Event',NULL,' ',NULL,0,0,NULL,NULL),('4028ab1063f427af0163f428e2b10126',1528809710258,1617705337127,NULL,NULL,NULL,'Aggregate Request',NULL,'sailpoint.request.AggregateRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,' ',NULL,20,0,NULL,NULL),('4028ab1063f427af0163f428e2e90127',1528809710313,1617705337204,NULL,NULL,NULL,'Aggregate Partition',NULL,'sailpoint.request.AggregationRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"maxThreads\" value=\"1\"/>\n  </Map>\n</Attributes>\n',NULL,20,0,NULL,NULL),('4028ab1063f427af0163f428e31a0128',1528809710363,1617705337294,NULL,NULL,NULL,'Manager Certification Generation Partition',NULL,'sailpoint.request.ManagerCertificationPartitionRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"errorAction\" value=\"terminate\"/>\n    <entry key=\"maxThreads\" value=\"1\"/>\n    <entry key=\"orphanAction\" value=\"delete\"/>\n  </Map>\n</Attributes>\n',NULL,20,0,NULL,NULL),('4028ab1063f427af0163f428e34a0129',1528809710411,1617705337371,NULL,NULL,NULL,'Identity Refresh Partition',NULL,'sailpoint.request.IdentityRefreshRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"maxThreads\" value=\"1\"/>\n  </Map>\n</Attributes>\n',NULL,20,0,NULL,NULL),('4028ab1063f427af0163f428e37f012a',1528809710464,1617705337452,NULL,NULL,NULL,'Role Propagation Partition',NULL,'sailpoint.request.RolePropagationRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"maxThreads\" value=\"1\"/>\n  </Map>\n</Attributes>\n',NULL,0,0,NULL,NULL),('4028ab1063f427af0163f428e3af012b',1528809710512,1617705337538,NULL,NULL,NULL,'Partitioned Alert Processing',NULL,'sailpoint.request.AlertProcessorRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"maxThreads\" value=\"1\"/>\n  </Map>\n</Attributes>\n',NULL,20,0,NULL,NULL),('4028ab1063f427af0163f428e3d6012c',1528809710550,1617705337616,NULL,NULL,NULL,'Task Execute',NULL,'sailpoint.request.TaskExecuteExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"hostSpecific\" value=\"true\"/>\n    <entry key=\"maxThreads\" value=\"5\"/>\n  </Map>\n</Attributes>\n',NULL,0,0,NULL,NULL),('4028ab1063f427af0163f428e409012d',1528809710601,1617705337681,NULL,NULL,NULL,'Task Command',NULL,'sailpoint.request.TaskCommandExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"hostSpecific\" value=\"true\"/>\n    <entry key=\"maxThreads\" value=\"-1\"/>\n  </Map>\n</Attributes>\n',NULL,0,0,NULL,NULL),('4028ab1063f427af0163f428e431012e',1528809710641,1617705337739,NULL,NULL,NULL,'Terminate Task',NULL,'sailpoint.request.TerminateRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"hostSpecific\" value=\"true\"/>\n  </Map>\n</Attributes>\n',NULL,0,0,NULL,NULL),('4028ab1063f427af0163f428e461012f',1528809710689,1617705337798,NULL,NULL,NULL,'Rule Request',NULL,'sailpoint.request.RuleRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"maxThreads\" value=\"-1\"/>\n  </Map>\n</Attributes>\n',NULL,0,0,NULL,NULL),('a9fe0bbd78ab1f7e8178abe0f6dd0009',1617791350493,NULL,NULL,NULL,NULL,'System Maintenance Pruner Partition',NULL,'sailpoint.request.PrunerRequestExecutor',NULL,_binary '\0',_binary '\0',0,0,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"maxThreads\" value=\"4\"/>\n  </Map>\n</Attributes>\n',NULL,0,0,NULL,NULL);
/*!40000 ALTER TABLE `spt_request_definition` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:46
