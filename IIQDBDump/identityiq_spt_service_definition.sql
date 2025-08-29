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
-- Table structure for table `spt_service_definition`
--

DROP TABLE IF EXISTS `spt_service_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_service_definition` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `executor` varchar(255) DEFAULT NULL,
  `exec_interval` int(11) DEFAULT NULL,
  `hosts` varchar(1024) DEFAULT NULL,
  `attributes` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_service_definition`
--

LOCK TABLES `spt_service_definition` WRITE;
/*!40000 ALTER TABLE `spt_service_definition` DISABLE KEYS */;
INSERT INTO `spt_service_definition` VALUES ('297e8b3d78ab76ff0178ab789fef000f',1617784512495,NULL,'Reanimator','\n      Service definition for the task/request re-animator service\n    ',NULL,60,'global',' '),('297e8b3d78ab76ff0178ab78a4570017',1617784513623,NULL,'Monitoring','\n      Service definition for the Monitoring service.  The \"monitoringConfig\"\n      attribute entry serves as the default monitoring configuration for a Server if the\n      Server does not have any monitoring configuration.\n    ',NULL,300,'global','<Attributes>\n  <Map>\n    <entry key=\"monitoringConfig\">\n      <value>\n        <Map>\n          <entry key=\"monitoringStatistics\">\n            <value>\n              <List>\n                <String>cpu</String>\n                <String>quartzThreads</String>\n                <String>requestProcessorThreads</String>\n                <String>databaseResponseTime</String>\n                <String>memoryUsage</String>\n                <String>memoryUsagePercentage</String>\n              </List>\n            </value>\n          </entry>\n          <entry key=\"retentionPeriodDays\">\n            <value>\n              <Integer>7</Integer>\n            </value>\n          </entry>\n        </Map>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n'),('4028ab1063f427af0163f428e7c80134',1528809711560,1617705339046,'SMListener','\nService definition for the SM interceptor.  Configure this\nby adding an \"applications\" attribute whose value is a csv of Application names\nthat will be sending interception events.  Be sure you also configure \nthe ResourceEvent service to have a host.  \n    ',NULL,0,NULL,' '),('4028ab1063f427af0163f428e7f50135',1528809711605,1617705339108,'ResourceEvent','\nService definition for the ResourceEvent processor.  The attributes\nmap contains the refresh options passed to the Identitizer.  You can set\n\"noRefresh\" to \"true\" to disable refresh.  This can run independent\nof SMListener and does not need to be on the same host.  But if you\'re\nrunning SMListener, you need to be running this somewhere as well.\n    ',NULL,0,NULL,'<Attributes>\n  <Map>\n    <entry key=\"correlateEntitlements\" value=\"true\"/>\n    <entry key=\"promoteAttributes\" value=\"true\"/>\n  </Map>\n</Attributes>\n'),('4028ab1063f427af0163f428e8190136',1528809711642,1617705339188,'Heartbeat','\nService definition for the Heartbeat service.  There is no configuration\nand you normally always have this enabled globally.\n    ',NULL,0,'global',' '),('4028ab1063f427af0163f428e83e0137',1528809711679,1617705339241,'Task','\nService definition for the Task scheduler service.  \n    ',NULL,0,'global',' '),('4028ab1063f427af0163f428e8610138',1528809711713,1617705339296,'Request','\nService definition for the Request processor service.\n    ',NULL,0,'global',' '),('4028ab1063f427af0163f428e8810139',1528809711746,1617705339348,'PluginSync','\n      Service definition for the plugins sync service.\n    ',NULL,60,'global',' '),('4028ab1063fdfb6d0163fdfc4d8e0014',1528974560654,1617706124365,'FullText','\nService definition for refreshing full text indexes.  The executionMode may be either \"scheduled\" or \"automatic\".  The default if not specified is scheduled.\n    ',NULL,3600,'global','<Attributes>\n  <Map>\n    <entry key=\"executionMode\" value=\"automatic\"/>\n  </Map>\n</Attributes>\n'),('a9fe0bbd94fd12518194fdd4758e0039',1739425346958,NULL,'TaskLauncher','\n      Service definition for the SSD Server-Specific Task Launcher service.\n    ','sailpoint.services.standard.tasklauncher.TaskLauncherService',30,'global',' '),('a9fe0bbd94fd12518194fdd47d110042',1739425348881,NULL,'PluginImporter','\n      Service definition for the SSB plugin importer service.\n    ','sailpoint.services.standard.pluginimporter.PluginImporterService',86400,'global',' '),('a9fe0bbd97a01e0b8197ab42de850953',1750924975774,NULL,'SupportPluginService','\n			Service definition for the Support Plugin service.\n		','sailpoint.support.plugin.server.SupportPluginRequestService',60,'global','<Attributes>\n  <Map>\n    <entry key=\"pluginName\" value=\"SupportPlugin\"/>\n  </Map>\n</Attributes>\n');
/*!40000 ALTER TABLE `spt_service_definition` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:45
