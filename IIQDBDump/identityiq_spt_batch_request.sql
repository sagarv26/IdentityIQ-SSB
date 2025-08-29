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
-- Table structure for table `spt_batch_request`
--

DROP TABLE IF EXISTS `spt_batch_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_batch_request` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `header` varchar(4000) DEFAULT NULL,
  `run_date` bigint(20) DEFAULT NULL,
  `completed_date` bigint(20) DEFAULT NULL,
  `record_count` int(11) DEFAULT NULL,
  `completed_count` int(11) DEFAULT NULL,
  `error_count` int(11) DEFAULT NULL,
  `invalid_count` int(11) DEFAULT NULL,
  `message` varchar(4000) DEFAULT NULL,
  `error_message` longtext,
  `file_contents` longtext,
  `status` varchar(255) DEFAULT NULL,
  `run_config` longtext,
  PRIMARY KEY (`id`),
  KEY `FKA7055A02486634B7` (`assigned_scope`),
  KEY `FKA7055A02A5FB1B1` (`owner`),
  KEY `SPT_IDX8CEA0D6E33EF6770` (`assigned_scope_path`(255)),
  CONSTRAINT `FKA7055A02486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FKA7055A02A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_batch_request`
--

LOCK TABLES `spt_batch_request` WRITE;
/*!40000 ALTER TABLE `spt_batch_request` DISABLE KEYS */;
INSERT INTO `spt_batch_request` VALUES ('a9fe0bbd92d11bbd8192dbe5c04c0701',1730266120267,1730266132465,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'batch-request.csv','operation, identityname, email',1730266120302,1730266132464,2,2,0,0,NULL,' ','operation,identityName,email\nModifyIdentity,2020005,Donna.Guertin@sweinc.com\nModifyIdentity,2020007,Lisa.Desrochers@sweinc.com','Executed','<Attributes>\n  <Map>\n    <entry key=\"generateIdentityRequests\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"handleExistingCreate\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"ignoreErrors\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"policyScheme\" value=\"none\"/>\n    <entry key=\"runDate\">\n      <value>\n        <Date>1730266080000</Date>\n      </value>\n    </entry>\n    <entry key=\"runNow\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"skipManualWorkItems\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"skipProvisioningForms\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"stopNumber\">\n      <value>\n        <Integer>0</Integer>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n'),('a9fe0bbd92d11bbd8192dbe665ab0718',1730266162602,1730266175685,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'batch-request.csv','operation, identityname, email',1730266162621,1730266175684,2,2,0,0,NULL,' ','operation,identityName,email\nModifyIdentity,2020005,Donna.Guertin@sweinc.com\nModifyIdentity,2020007,Lisa.Desrochers@sweinc.com','Executed','<Attributes>\n  <Map>\n    <entry key=\"generateIdentityRequests\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"handleExistingCreate\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"ignoreErrors\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"policyScheme\" value=\"none\"/>\n    <entry key=\"runDate\">\n      <value>\n        <Date>1730266140000</Date>\n      </value>\n    </entry>\n    <entry key=\"runNow\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"skipManualWorkItems\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"skipProvisioningForms\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"stopNumber\">\n      <value>\n        <Integer>0</Integer>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n'),('a9fe0bbd92d11bbd8192dc7c241708d2',1730275976214,1730275994395,'4028ab1063f427af0163f428d1ca0105',NULL,NULL,'batch-request.csv','operation, identityname, email',1730275976250,1730275994394,2,2,0,0,NULL,' ','operation,identityName,email\nModifyIdentity,2020010,Alan.McRae@sweinc.com\nModifyIdentity,2020008,Tom.Rubyck@sweinc.com','Executed','<Attributes>\n  <Map>\n    <entry key=\"generateIdentityRequests\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"handleExistingCreate\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"ignoreErrors\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"policyScheme\" value=\"none\"/>\n    <entry key=\"runDate\">\n      <value>\n        <Date>1730275920000</Date>\n      </value>\n    </entry>\n    <entry key=\"runNow\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"skipManualWorkItems\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"skipProvisioningForms\">\n      <value>\n        <Boolean>true</Boolean>\n      </value>\n    </entry>\n    <entry key=\"stopNumber\">\n      <value>\n        <Integer>0</Integer>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n');
/*!40000 ALTER TABLE `spt_batch_request` ENABLE KEYS */;
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
