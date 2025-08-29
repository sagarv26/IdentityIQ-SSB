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
-- Table structure for table `spt_capability`
--

DROP TABLE IF EXISTS `spt_capability`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_capability` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `display_name` varchar(128) DEFAULT NULL,
  `applies_to_analyzer` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK5E9BD4A0486634B7` (`assigned_scope`),
  KEY `FK5E9BD4A0A5FB1B1` (`owner`),
  KEY `SPT_IDXECBE5C8C4B5A312C` (`assigned_scope_path`(255)),
  CONSTRAINT `FK5E9BD4A0486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK5E9BD4A0A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_capability`
--

LOCK TABLES `spt_capability` WRITE;
/*!40000 ALTER TABLE `spt_capability` DISABLE KEYS */;
INSERT INTO `spt_capability` VALUES ('297e8b3d78ab76ff0178ab7796200006',1617784444449,1730112674313,NULL,NULL,NULL,'ViewAdminConsole','capability_desc_view_admin_console','capability_view_admin_console',_binary '\0'),('4028ab1063f427af0163f428956200c1',1528809690467,1730112649553,NULL,NULL,NULL,'SystemAdministrator','capability_desc_system_administrator','capability_system_administrator',_binary '\0'),('4028ab1063f427af0163f428960c00c2',1528809690636,1617791284761,NULL,NULL,NULL,'ApplicationAdministrator','capability_desc_application_administrator','capability_application_administrator',_binary '\0'),('4028ab1063f427af0163f42897c000c3',1528809691072,1617791284892,NULL,NULL,NULL,'WorkItemAdministrator','capability_desc_workitem_administrator','capability_workitem_administrator',_binary '\0'),('4028ab1063f427af0163f428982d00c4',1528809691183,1617791285144,NULL,NULL,NULL,'IdentityRequestAdministrator','capability_desc_identity_request_administrator','capability_identity_request_administrator',_binary '\0'),('4028ab1063f427af0163f42898d200c5',1528809691346,1617791285237,NULL,NULL,NULL,'PasswordAdministrator','capability_desc_password_administrator','capability_password_administrator',_binary '\0'),('4028ab1063f427af0163f428992700c6',1528809691431,1617791285528,NULL,NULL,NULL,'WorkgroupAdministrator','capability_desc_workgroup_administrator','capability_workgroup_administrator',_binary '\0'),('4028ab1063f427af0163f428996b00c7',1528809691499,1617791285671,NULL,NULL,NULL,'ITRoleAdministrator','capability_desc_it_role_administrator','capability_it_role_administrator',_binary '\0'),('4028ab1063f427af0163f42899aa00c8',1528809691562,1617791285813,NULL,NULL,NULL,'EntitlementRoleAdministrator','capability_desc_entitlement_role_administrator','capability_entitlement_role_administrator',_binary '\0'),('4028ab1063f427af0163f42899de00c9',1528809691614,1617791285899,NULL,NULL,NULL,'BusinessRoleAdministrator','capability_desc_business_role_administrator','capability_business_role_administrator',_binary '\0'),('4028ab1063f427af0163f4289a2a00ca',1528809691690,1617791285998,NULL,NULL,NULL,'OrganizationalRoleAdministrator','capability_desc_organization_role_administrator','capability_organizational_role_administrator',_binary '\0'),('4028ab1063f427af0163f4289a7d00cb',1528809691773,1617791286102,NULL,NULL,NULL,'RoleAdministrator','capability_desc_role_administrator','capability_role_administrator',_binary '\0'),('4028ab1063f427af0163f4289af900cc',1528809691897,1617791286202,NULL,NULL,NULL,'PolicyAdministrator','capability_desc_policy_administrator','capability_policy_administrator',_binary '\0'),('4028ab1063f427af0163f4289bb600cd',1528809692086,1617791286417,NULL,NULL,NULL,'ComplianceOfficer','capability_desc_compliance_officer','capability_compliance_officer',_binary '\0'),('4028ab1063f427af0163f4289c8600ce',1528809692295,1617791286627,NULL,NULL,NULL,'Auditor','capability_desc_auditor','capability_auditor',_binary '\0'),('4028ab1063f427af0163f4289ce100cf',1528809692386,1617791286734,NULL,NULL,NULL,'SignOffAdministrator','capability_desc_sign_off_administrator','capability_sign_off_administrator',_binary '\0'),('4028ab1063f427af0163f4289d0a00d0',1528809692426,1617791286782,NULL,NULL,NULL,'TaskResultsViewer','capability_desc_task_results_viewer','capability_task_results_viewer',_binary '\0'),('4028ab1063f427af0163f4289d8000d1',1528809692544,1617791286984,NULL,NULL,NULL,'IdentityAdministrator','capability_desc_identity_administrator','capability_identity_administrator',_binary '\0'),('4028ab1063f427af0163f4289dc900d2',1528809692617,1617791287196,NULL,NULL,NULL,'IdentityCorrelationAdministrator','capability_desc_identity_correlation_administrator','capability_identity_correlation_administrator',_binary '\0'),('4028ab1063f427af0163f4289e1a00d3',1528809692698,1617791287499,NULL,NULL,NULL,'CertificationAdministrator','capability_desc_certification_administrator','capability_certification_administrator',_binary '\0'),('4028ab1063f427af0163f4289e5800d4',1528809692761,1617791288491,NULL,NULL,NULL,'SCIMExecutor','capability_desc_scim_executor','capability_scim_executor',_binary '\0'),('4028ab1063f427af0163f4289e9b00d5',1528809692827,1617791287779,NULL,NULL,NULL,'WebServicesExecutor','capability_desc_ws_executor','capability_ws_executor',_binary '\0'),('4028ab1063f427af0163f4289f0000d6',1528809692928,1617791287927,NULL,NULL,NULL,'RuleAdministrator','capability_desc_rule_administrator','capability_rule_administrator',_binary '\0'),('4028ab1063f427af0163f4289f2d00d7',1528809692973,1617791287978,NULL,NULL,NULL,'HelpDesk','capability_desc_help_desk','capability_help_desk',_binary '\0'),('4028ab1063f427af0163f4289f5d00d8',1528809693021,1617791288063,NULL,NULL,NULL,'AccessManager','capability_access_manager_desc','capability_access_manager',_binary '\0'),('4028ab1063f427af0163f4289fa200d9',1528809693090,1617791288145,NULL,NULL,NULL,'ManagedAttributePropertyAdmin','capability_desc_managed_attribute_property_admin','capability_managed_attribute_property_admin',_binary '\0'),('4028ab1063f427af0163f4289fd500da',1528809693141,1617791288211,NULL,NULL,NULL,'ManagedAttributeProvisioningAdmin','capability_desc_managed_attribute_provisioning_admin','capability_managed_attribute_provisioning_admin',_binary '\0'),('4028ab1063f427af0163f4289ffc00db',1528809693180,1617791288275,NULL,NULL,NULL,'SyslogAdministrator','capability_desc_managed_attribute_provisioning_admin','capability_syslog_admin',_binary '\0'),('4028ab1063f427af0163f428a02b00dc',1528809693227,1617791288340,NULL,NULL,NULL,'FormAdministrator','capability_desc_form_admin','capability_form_admin',_binary '\0'),('4028ab1063f427af0163f428a09a00dd',1528809693338,1617791288390,NULL,NULL,NULL,'PluginAdministrator','capability_desc_plugin_admin','capability_plugin_admin',_binary '\0'),('4028ab1063f427af0163f428a2fc00de',1528809693949,1617791288561,NULL,NULL,NULL,'AlertAdministrator','capability_desc_alert_admin','capability_alert_admin',_binary '\0'),('4028ab1063f427af0163f428a3c800df',1528809694152,1617791288713,NULL,NULL,NULL,'FullAccessAdminConsole','capability_desc_full_admin_console','capability_full_admin_console',_binary '\0'),('4028ab1063fdfb6d0163fdfc81070056',1528974573831,1617791357467,NULL,NULL,NULL,'BatchRequestAdministrator','capability_desc_batch_request_administrator','capability_batch_request_administrator',_binary '\0');
/*!40000 ALTER TABLE `spt_capability` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:52
