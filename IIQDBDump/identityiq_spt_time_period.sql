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
-- Table structure for table `spt_time_period`
--

DROP TABLE IF EXISTS `spt_time_period`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_time_period` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `classifier` varchar(255) DEFAULT NULL,
  `init_parameters` longtext,
  PRIMARY KEY (`id`),
  KEY `FK49F210EB486634B7` (`assigned_scope`),
  KEY `FK49F210EBA5FB1B1` (`owner`),
  KEY `SPT_IDX1E683C17685A4D02` (`assigned_scope_path`(255)),
  CONSTRAINT `FK49F210EB486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK49F210EBA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_time_period`
--

LOCK TABLES `spt_time_period` WRITE;
/*!40000 ALTER TABLE `spt_time_period` DISABLE KEYS */;
INSERT INTO `spt_time_period` VALUES ('4028ab1063f427af0163f428d9b5010e',1528809707957,1617705333660,NULL,NULL,NULL,'first_quarter','DateRange','<Attributes>\n  <Map>\n    <entry key=\"endDate\">\n      <value>\n        <Date>1167503400000</Date>\n      </value>\n    </entry>\n    <entry key=\"startDate\">\n      <value>\n        <Date>1159641000000</Date>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n'),('4028ab1063f427af0163f428d9f7010f',1528809708023,1617705333716,NULL,NULL,NULL,'second_quarter','DateRange','<Attributes>\n  <Map>\n    <entry key=\"endDate\">\n      <value>\n        <Date>1175279400000</Date>\n      </value>\n    </entry>\n    <entry key=\"startDate\">\n      <value>\n        <Date>1167589800000</Date>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n'),('4028ab1063f427af0163f428db1e0110',1528809708318,1617705333782,NULL,NULL,NULL,'third_quarter','DateRange','<Attributes>\n  <Map>\n    <entry key=\"endDate\">\n      <value>\n        <Date>1183141800000</Date>\n      </value>\n    </entry>\n    <entry key=\"startDate\">\n      <value>\n        <Date>1175365800000</Date>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n'),('4028ab1063f427af0163f428dbdd0111',1528809708509,1617705333842,NULL,NULL,NULL,'fourth_quarter','DateRange','<Attributes>\n  <Map>\n    <entry key=\"endDate\">\n      <value>\n        <Date>1191090600000</Date>\n      </value>\n    </entry>\n    <entry key=\"startDate\">\n      <value>\n        <Date>1183228200000</Date>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n'),('4028ab1063f427af0163f428dc550112',1528809708629,1617705333912,NULL,NULL,NULL,'holidays','DateSet','<Attributes>\n  <Map>\n    <entry key=\"dates\">\n      <value>\n        <List>\n          <Date>1167589800000</Date>\n          <Date>1168799400000</Date>\n          <Date>1171823400000</Date>\n          <Date>1180290600000</Date>\n          <Date>1183487400000</Date>\n          <Date>1188757800000</Date>\n          <Date>1191781800000</Date>\n          <Date>1194805800000</Date>\n          <Date>1195669800000</Date>\n          <Date>1198521000000</Date>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n'),('4028ab1063f427af0163f428dcdd0113',1528809708765,1617705333983,NULL,NULL,NULL,'weekdays','DaysOfWeek','<Attributes>\n  <Map>\n    <entry key=\"days\">\n      <value>\n        <List>\n          <String>Monday</String>\n          <String>Tuesday</String>\n          <String>Wednesday</String>\n          <String>Thursday</String>\n          <String>Friday</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n'),('4028ab1063f427af0163f428dcfc0114',1528809708796,1617705334079,NULL,NULL,NULL,'weekends','DaysOfWeek','<Attributes>\n  <Map>\n    <entry key=\"days\">\n      <value>\n        <List>\n          <String>Saturday</String>\n          <String>Sunday</String>\n        </List>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n'),('4028ab1063f427af0163f428dd290115',1528809708841,1617705334134,NULL,NULL,NULL,'office_hours','TimeOfDayRange','<Attributes>\n  <Map>\n    <entry key=\"endTime\">\n      <value>\n        <Date>41400000</Date>\n      </value>\n    </entry>\n    <entry key=\"startTime\">\n      <value>\n        <Date>12600000</Date>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n'),('4028ab1063f427af0163f428dd5b0116',1528809708891,1617705334207,NULL,NULL,NULL,'non_office_hours','TimeOfDayRange','<Attributes>\n  <Map>\n    <entry key=\"endTime\">\n      <value>\n        <Date>12600000</Date>\n      </value>\n    </entry>\n    <entry key=\"startTime\">\n      <value>\n        <Date>41400000</Date>\n      </value>\n    </entry>\n  </Map>\n</Attributes>\n');
/*!40000 ALTER TABLE `spt_time_period` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:46
