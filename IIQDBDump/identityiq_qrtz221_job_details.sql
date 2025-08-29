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
-- Table structure for table `qrtz221_job_details`
--

DROP TABLE IF EXISTS `qrtz221_job_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz221_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `idx_qrtz_j_req_recovery` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `idx_qrtz_j_grp` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz221_job_details`
--

LOCK TABLES `qrtz221_job_details` WRITE;
/*!40000 ALTER TABLE `qrtz221_job_details` DISABLE KEYS */;
INSERT INTO `qrtz221_job_details` VALUES ('QuartzScheduler','48418cac388c4d028020ce256f0ca59e','DEFAULT','Immediate task runner','sailpoint.scheduler.JobAdapter','1','0','1','0',_binary '#\r\n#Wed Mar 12 10:34:43 IST 2025\r\nlocale=en_US\r\ntimezone=Asia/Calcutta\r\nlauncher=spadmin\r\nexecutor=a9fedb6691791889819179c01a3b00ce\r\n'),('QuartzScheduler','Application Status','DEFAULT','Application Status Report ','sailpoint.scheduler.JobAdapter','1','0','1','0',_binary '#\r\n#Fri Dec 27 10:43:00 IST 2024\r\nTaskSchedule.host=\r\nnextActualFireTime=1737621720000\r\nlauncher=spadmin\r\nexecutor=4028ab10671c24ba01671c4c14920005\r\n'),('QuartzScheduler','c2a60fce40e04f33b44c6cc098162ebe','DEFAULT','Immediate task runner','sailpoint.scheduler.JobAdapter','1','0','1','0',_binary '#\r\n#Wed Mar 12 10:34:42 IST 2025\r\nlocale=en_US\r\ntimezone=Asia/Calcutta\r\nlauncher=spadmin\r\nexecutor=a9fedb6691791889819179c01a3b00ce\r\n'),('QuartzScheduler','Check expired mitigations daily','DEFAULT','Check for expired mitigations every day at midnight.','sailpoint.scheduler.JobAdapter','1','0','1','0',_binary '#\r\n#Tue Apr 01 09:31:55 IST 2025\r\nnextActualFireTime=1743532200000\r\nexecutor=Check Expired Mitigations\r\n'),('QuartzScheduler','Check expired work items daily','DEFAULT','Check for expired work items every day at midnight.','sailpoint.scheduler.JobAdapter','1','0','1','0',_binary '#\r\n#Tue Apr 01 09:31:54 IST 2025\r\nnextActualFireTime=1743532200000\r\nexecutor=Check Expired Work Items\r\n'),('QuartzScheduler','Check sunset requests for notifications daily','DEFAULT','Check for Roles and Entitlements that are about to expire and send a notification every day at midnight','sailpoint.scheduler.JobAdapter','1','0','1','0',_binary '#\r\n#Sat Jul 05 08:29:30 IST 2025\r\nnextActualFireTime=1751740200000\r\nexecutor=Check Sunset Requests\r\n'),('QuartzScheduler','d465d4c6e6b54c7c8fd867a849f2237d','DEFAULT','Immediate task runner','sailpoint.scheduler.JobAdapter','1','0','1','0',_binary '#\r\n#Fri Oct 25 10:47:51 IST 2024\r\nlocale=en_US\r\ntimezone=Asia/Calcutta\r\nlauncher=spadmin\r\nexecutor=a9fe0bbd92b71d768192c21ba1dd050b\r\n'),('QuartzScheduler','Manager Certification [DATE] [10/30/24 1:37 PM]','DEFAULT','Global Manager Access Review','sailpoint.scheduler.JobAdapter','1','0','1','0',_binary '#\r\n#Wed Oct 30 13:37:45 IST 2024\r\ncertificationDefinitionId=Manager Certification [10/30/24 1\\:37 PM]\r\nresultName=Manager Certification [DATE] [10/30/24 1\\:37 PM]\r\nlauncher=spadmin\r\nexecutor=4028ab1063f427af0163f42923e701bf\r\n'),('QuartzScheduler','Perform Identity Request Maintenance','DEFAULT','Perform Identity Request maintenance every day at 2 AM.','sailpoint.scheduler.JobAdapter','1','0','1','0',_binary '#\r\n#Tue Apr 01 09:31:52 IST 2025\r\nnextActualFireTime=1743539400000\r\nexecutor=Perform Identity Request Maintenance\r\n'),('QuartzScheduler','Perform maintenance','DEFAULT','Perform maintenance every five minutes.','sailpoint.scheduler.JobAdapter','1','0','1','0',_binary '#\r\n#Thu May 15 10:44:34 IST 2025\r\nnextActualFireTime=1747286100000\r\nexecutor=Perform Maintenance\r\n');
/*!40000 ALTER TABLE `qrtz221_job_details` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:50
