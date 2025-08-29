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
-- Table structure for table `spt_full_text_index`
--

DROP TABLE IF EXISTS `spt_full_text_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_full_text_index` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `iiqlock` varchar(128) DEFAULT NULL,
  `last_refresh` bigint(20) DEFAULT NULL,
  `attributes` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_full_text_index`
--

LOCK TABLES `spt_full_text_index` WRITE;
/*!40000 ALTER TABLE `spt_full_text_index` DISABLE KEYS */;
INSERT INTO `spt_full_text_index` VALUES ('4028ab1063f427af0163f428e7710133',1528809711473,1752226850091,'BundleManagedAttribute',NULL,NULL,1752226850078,'<Attributes>\n  <Map>\n    <entry key=\"classes\" value=\"BundleManagedAttribute\"/>\n    <entry key=\"fields\">\n      <value>\n        <List>\n          <FullTextField indexed=\"true\" name=\"objectClass\" stored=\"true\"/>\n          <FullTextField name=\"id\" stored=\"true\"/>\n          <FullTextField analyzed=\"true\" indexed=\"true\" name=\"name\"/>\n          <FullTextField analyzed=\"true\" indexed=\"true\" name=\"displayableName\"/>\n          <FullTextField analyzed=\"true\" indexed=\"true\" name=\"description\"/>\n          <FullTextField indexed=\"true\" name=\"assignedScope.path\"/>\n          <FullTextField indexed=\"true\" name=\"type\"/>\n          <FullTextField indexed=\"true\" name=\"owner.id\"/>\n          <FullTextField indexed=\"true\" name=\"owner.name\"/>\n          <FullTextField name=\"owner.displayName\" stored=\"true\"/>\n          <FullTextField name=\"defaultDescription\" stored=\"true\"/>\n          <FullTextField ignored=\"true\" name=\"disabled\"/>\n          <FullTextField name=\"riskScoreWeight\" stored=\"true\"/>\n          <FullTextField indexed=\"true\" name=\"application.id\"/>\n          <FullTextField analyzed=\"true\" indexed=\"true\" name=\"application.name\"/>\n          <FullTextField indexed=\"true\" name=\"application.icon\"/>\n          <FullTextField analyzed=\"true\" indexed=\"true\" name=\"application.description\"/>\n          <FullTextField indexed=\"true\" name=\"application.assignedScope.path\"/>\n          <FullTextField indexed=\"true\" name=\"application.owner.id\"/>\n          <FullTextField indexed=\"true\" name=\"attribute\"/>\n          <FullTextField indexed=\"true\" name=\"value\"/>\n          <FullTextField indexed=\"true\" name=\"requestable\"/>\n        </List>\n      </value>\n    </entry>\n    <entry key=\"includeTargets\" value=\"true\"/>\n    <entry key=\"indexer\" value=\"BundleManagedAttributeIndexer\"/>\n  </Map>\n</Attributes>\n');
/*!40000 ALTER TABLE `spt_full_text_index` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:04
