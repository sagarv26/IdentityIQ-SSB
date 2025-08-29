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
-- Table structure for table `spt_monitoring_statistic`
--

DROP TABLE IF EXISTS `spt_monitoring_statistic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_monitoring_statistic` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `display_name` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `value` varchar(4000) DEFAULT NULL,
  `value_type` varchar(128) DEFAULT NULL,
  `type` varchar(128) DEFAULT NULL,
  `attributes` longtext,
  `template` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK9B2F43A1A5FB1B1` (`owner`),
  KEY `FK9B2F43A1486634B7` (`assigned_scope`),
  KEY `SPT_IDXF89E6D4D93CDB0EE` (`assigned_scope_path`(255)),
  CONSTRAINT `FK9B2F43A1486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK9B2F43A1A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_monitoring_statistic`
--

LOCK TABLES `spt_monitoring_statistic` WRITE;
/*!40000 ALTER TABLE `spt_monitoring_statistic` DISABLE KEYS */;
INSERT INTO `spt_monitoring_statistic` VALUES ('297e8b3d78ab76ff0178ab78a2560010',1617784513110,NULL,NULL,NULL,NULL,'applicationResponseTime','monitor_stat_label_application_response_time',NULL,'call:getApplicationResponseTime','long','Application','<Attributes>\n  <Map>\n    <entry key=\"referencedObject\" value=\"ValidApplicationName\"/>\n    <entry key=\"referencedObjectType\" value=\"Application\"/>\n  </Map>\n</Attributes>\n',_binary ''),('297e8b3d78ab76ff0178ab78a2970011',1617784513175,NULL,NULL,NULL,NULL,'cpu','monitor_stat_label_cpu',NULL,'call:getCpuUsage','double','Server','<Attributes>\n  <Map>\n    <entry key=\"valueRenderer\" value=\"percentage\"/>\n  </Map>\n</Attributes>\n',_binary '\0'),('297e8b3d78ab76ff0178ab78a2df0012',1617784513247,NULL,NULL,NULL,NULL,'quartzThreads','monitor_stat_label_quartz_threads',NULL,'call:getQuartzThreads','int','Server',' ',_binary '\0'),('297e8b3d78ab76ff0178ab78a35d0013',1617784513373,NULL,NULL,NULL,NULL,'requestProcessorThreads','monitor_stat_label_request_proc_threads',NULL,'call:getRequestProcessorThreads','int','Server',' ',_binary '\0'),('297e8b3d78ab76ff0178ab78a3a20014',1617784513442,NULL,NULL,NULL,NULL,'databaseResponseTime','monitor_stat_label_database_response_time',NULL,'call:getDatabaseResponseTime','long','Server','<Attributes>\n  <Map>\n    <entry key=\"valueRenderer\" value=\"milliseconds\"/>\n  </Map>\n</Attributes>\n',_binary '\0'),('297e8b3d78ab76ff0178ab78a3f40015',1617784513524,NULL,NULL,NULL,NULL,'memoryUsage','monitor_stat_label_memory_usage','The amount of memory (in bytes) actively in use by the application server\'s Java virtual machine','call:getUsedMemory','long','JVM','<Attributes>\n  <Map>\n    <entry key=\"valueRenderer\" value=\"megabytes\"/>\n  </Map>\n</Attributes>\n',_binary '\0'),('297e8b3d78ab76ff0178ab78a42b0016',1617784513579,NULL,NULL,NULL,NULL,'memoryUsagePercentage','monitor_stat_label_memory_usage_percentage','The percentage of the maximum allowed memory which is actively in use by the application server\'s Java virtual machine','call:getMemoryUsagePercentage','double','JVM','<Attributes>\n  <Map>\n    <entry key=\"valueRenderer\" value=\"percentage\"/>\n  </Map>\n</Attributes>\n',_binary '\0');
/*!40000 ALTER TABLE `spt_monitoring_statistic` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:59
