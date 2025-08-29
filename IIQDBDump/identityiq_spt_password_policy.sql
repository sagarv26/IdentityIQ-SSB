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
-- Table structure for table `spt_password_policy`
--

DROP TABLE IF EXISTS `spt_password_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_password_policy` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `password_constraints` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK479B98CEA5FB1B1` (`owner`),
  CONSTRAINT `FK479B98CEA5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_password_policy`
--

LOCK TABLES `spt_password_policy` WRITE;
/*!40000 ALTER TABLE `spt_password_policy` DISABLE KEYS */;
INSERT INTO `spt_password_policy` VALUES ('4028ab1063f427af0163f428a48a00e0',1528809694346,1739425289023,NULL,'RACF Default Password Policy','','<Map>\n  <entry key=\"checkPasswordsAgainstAccountAttributes\">\n    <value>\n      <Boolean></Boolean>\n    </value>\n  </entry>\n  <entry key=\"checkPasswordsAgainstDictionary\">\n    <value>\n      <Boolean></Boolean>\n    </value>\n  </entry>\n  <entry key=\"checkPasswordsAgainstIdentityAttributes\">\n    <value>\n      <Boolean></Boolean>\n    </value>\n  </entry>\n  <entry key=\"passwordHistory\"/>\n  <entry key=\"passwordMaxLength\" value=\"8\"/>\n  <entry key=\"passwordMinAlpha\" value=\"4\"/>\n  <entry key=\"passwordMinLength\" value=\"8\"/>\n  <entry key=\"passwordMinLower\"/>\n  <entry key=\"passwordMinNumeric\" value=\"4\"/>\n  <entry key=\"passwordMinSpecial\"/>\n  <entry key=\"passwordMinUpper\"/>\n</Map>\n'),('a9fe0bbd940617a18194069b5e65002f',1735277633125,1739425288891,NULL,'AD Password Policy',NULL,'<Map>\n  <entry key=\"checkCaseSensitive\">\n    <value>\n      <Boolean></Boolean>\n    </value>\n  </entry>\n  <entry key=\"checkPasswordAgainstAccountID\">\n    <value>\n      <Boolean>true</Boolean>\n    </value>\n  </entry>\n  <entry key=\"checkPasswordAgainstDisplayName\">\n    <value>\n      <Boolean></Boolean>\n    </value>\n  </entry>\n  <entry key=\"checkPasswordTriviality\">\n    <value>\n      <Boolean>true</Boolean>\n    </value>\n  </entry>\n  <entry key=\"checkPasswordsAgainstAccountAttributes\">\n    <value>\n      <Boolean></Boolean>\n    </value>\n  </entry>\n  <entry key=\"checkPasswordsAgainstDictionary\">\n    <value>\n      <Boolean>true</Boolean>\n    </value>\n  </entry>\n  <entry key=\"checkPasswordsAgainstIdentityAttributes\">\n    <value>\n      <Boolean>true</Boolean>\n    </value>\n  </entry>\n  <entry key=\"minAccountIDUniqueChars\"/>\n  <entry key=\"minDisplayNameUniqueChars\"/>\n  <entry key=\"minHistoryUniqueChars\"/>\n  <entry key=\"passwordHistory\" value=\"10\"/>\n  <entry key=\"passwordMaxLength\" value=\"64\"/>\n  <entry key=\"passwordMinAlpha\" value=\"6\"/>\n  <entry key=\"passwordMinCharType\"/>\n  <entry key=\"passwordMinLength\" value=\"15\"/>\n  <entry key=\"passwordMinLower\" value=\"1\"/>\n  <entry key=\"passwordMinNumeric\" value=\"1\"/>\n  <entry key=\"passwordMinSpecial\" value=\"0\"/>\n  <entry key=\"passwordMinUpper\" value=\"1\"/>\n  <entry key=\"passwordRepeatedChar\" value=\"2\"/>\n</Map>\n');
/*!40000 ALTER TABLE `spt_password_policy` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:50:16
