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
-- Table structure for table `spt_provisioning_request`
--

DROP TABLE IF EXISTS `spt_provisioning_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_provisioning_request` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `identity_id` varchar(32) DEFAULT NULL,
  `target` varchar(128) DEFAULT NULL,
  `requester` varchar(128) DEFAULT NULL,
  `expiration` bigint(20) DEFAULT NULL,
  `provisioning_plan` longtext,
  PRIMARY KEY (`id`),
  KEY `spt_provreq_expiration` (`expiration`),
  KEY `FK604114C556651F3A` (`identity_id`),
  KEY `FK604114C5486634B7` (`assigned_scope`),
  KEY `FK604114C5A5FB1B1` (`owner`),
  KEY `SPT_IDX133BD716174D236` (`assigned_scope_path`(255)),
  CONSTRAINT `FK604114C5486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK604114C556651F3A` FOREIGN KEY (`identity_id`) REFERENCES `spt_identity` (`id`),
  CONSTRAINT `FK604114C5A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_provisioning_request`
--

LOCK TABLES `spt_provisioning_request` WRITE;
/*!40000 ALTER TABLE `spt_provisioning_request` DISABLE KEYS */;
INSERT INTO `spt_provisioning_request` VALUES ('a9fe0bbd92d11bbd8192d1bc6b170113',1730095639319,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d472dcb6065a',NULL,'spadmin',1732687639318,'<ProvisioningPlan trackingId=\"ea5fa66c6c114e9fbb5b23eb7b3161f0\">\n  <AccountRequest application=\"Contractor System Employee\" nativeIdentity=\"2020008\" op=\"Modify\">\n    <AttributeRequest name=\"Group\" op=\"Add\" value=\"User\"/>\n  </AccountRequest>\n  <Attributes>\n    <Map>\n      <entry key=\"identityRequestId\" value=\"0000000055\"/>\n      <entry key=\"requester\" value=\"spadmin\"/>\n      <entry key=\"source\" value=\"LCM\"/>\n    </Map>\n  </Attributes>\n  <Requesters>\n    <Reference class=\"sailpoint.object.Identity\" id=\"4028ab1063f427af0163f428d1ca0105\" name=\"spadmin\"/>\n  </Requesters>\n</ProvisioningPlan>\n'),('a9fe0bbd92d11bbd8192d1be3ebf012b',1730095759039,NULL,NULL,NULL,NULL,'a9fe0bbd90c41bed8190d472dcb6065a',NULL,'spadmin',1732687759038,'<ProvisioningPlan trackingId=\"60492f61b2d54cdc8cfb6314f5aa67dc\">\n  <AccountRequest application=\"Contractor System Employee\" nativeIdentity=\"2020008\" op=\"Modify\">\n    <AttributeRequest name=\"Group\" op=\"Add\" value=\"Admin\"/>\n  </AccountRequest>\n  <Attributes>\n    <Map>\n      <entry key=\"identityRequestId\" value=\"0000000056\"/>\n      <entry key=\"requester\" value=\"spadmin\"/>\n      <entry key=\"source\" value=\"LCM\"/>\n    </Map>\n  </Attributes>\n  <Requesters>\n    <Reference class=\"sailpoint.object.Identity\" id=\"4028ab1063f427af0163f428d1ca0105\" name=\"spadmin\"/>\n  </Requesters>\n</ProvisioningPlan>\n');
/*!40000 ALTER TABLE `spt_provisioning_request` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:31
