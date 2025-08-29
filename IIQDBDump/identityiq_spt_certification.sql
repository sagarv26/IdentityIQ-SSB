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
-- Table structure for table `spt_certification`
--

DROP TABLE IF EXISTS `spt_certification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spt_certification` (
  `id` varchar(32) NOT NULL,
  `created` bigint(20) DEFAULT NULL,
  `modified` bigint(20) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `assigned_scope` varchar(32) DEFAULT NULL,
  `assigned_scope_path` varchar(450) DEFAULT NULL,
  `attributes` longtext,
  `iiqlock` varchar(128) DEFAULT NULL,
  `name` varchar(256) DEFAULT NULL,
  `short_name` varchar(255) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `complete` bit(1) DEFAULT NULL,
  `complete_hierarchy` bit(1) DEFAULT NULL,
  `signed` bigint(20) DEFAULT NULL,
  `approver_rule` varchar(512) DEFAULT NULL,
  `finished` bigint(20) DEFAULT NULL,
  `expiration` bigint(20) DEFAULT NULL,
  `automatic_closing_date` bigint(20) DEFAULT NULL,
  `application_id` varchar(255) DEFAULT NULL,
  `manager` varchar(255) DEFAULT NULL,
  `group_definition` varchar(512) DEFAULT NULL,
  `group_definition_id` varchar(128) DEFAULT NULL,
  `group_definition_name` varchar(255) DEFAULT NULL,
  `comments` longtext,
  `error` longtext,
  `entities_to_refresh` longtext,
  `commands` longtext,
  `activated` bigint(20) DEFAULT NULL,
  `total_entities` int(11) DEFAULT NULL,
  `excluded_entities` int(11) DEFAULT NULL,
  `completed_entities` int(11) DEFAULT NULL,
  `delegated_entities` int(11) DEFAULT NULL,
  `percent_complete` int(11) DEFAULT NULL,
  `certified_entities` int(11) DEFAULT NULL,
  `cert_req_entities` int(11) DEFAULT NULL,
  `overdue_entities` int(11) DEFAULT NULL,
  `total_items` int(11) DEFAULT NULL,
  `excluded_items` int(11) DEFAULT NULL,
  `completed_items` int(11) DEFAULT NULL,
  `delegated_items` int(11) DEFAULT NULL,
  `item_percent_complete` int(11) DEFAULT NULL,
  `certified_items` int(11) DEFAULT NULL,
  `cert_req_items` int(11) DEFAULT NULL,
  `overdue_items` int(11) DEFAULT NULL,
  `remediations_kicked_off` int(11) DEFAULT NULL,
  `remediations_completed` int(11) DEFAULT NULL,
  `total_violations` int(11) NOT NULL,
  `violations_allowed` int(11) NOT NULL,
  `violations_remediated` int(11) NOT NULL,
  `violations_acknowledged` int(11) NOT NULL,
  `total_roles` int(11) NOT NULL,
  `roles_approved` int(11) NOT NULL,
  `roles_allowed` int(11) NOT NULL,
  `roles_remediated` int(11) NOT NULL,
  `total_exceptions` int(11) NOT NULL,
  `exceptions_approved` int(11) NOT NULL,
  `exceptions_allowed` int(11) NOT NULL,
  `exceptions_remediated` int(11) NOT NULL,
  `total_grp_perms` int(11) NOT NULL,
  `grp_perms_approved` int(11) NOT NULL,
  `grp_perms_remediated` int(11) NOT NULL,
  `total_grp_memberships` int(11) NOT NULL,
  `grp_memberships_approved` int(11) NOT NULL,
  `grp_memberships_remediated` int(11) NOT NULL,
  `total_accounts` int(11) NOT NULL,
  `accounts_approved` int(11) NOT NULL,
  `accounts_allowed` int(11) NOT NULL,
  `accounts_remediated` int(11) NOT NULL,
  `total_profiles` int(11) NOT NULL,
  `profiles_approved` int(11) NOT NULL,
  `profiles_remediated` int(11) NOT NULL,
  `total_scopes` int(11) NOT NULL,
  `scopes_approved` int(11) NOT NULL,
  `scopes_remediated` int(11) NOT NULL,
  `total_capabilities` int(11) NOT NULL,
  `capabilities_approved` int(11) NOT NULL,
  `capabilities_remediated` int(11) NOT NULL,
  `total_permits` int(11) NOT NULL,
  `permits_approved` int(11) NOT NULL,
  `permits_remediated` int(11) NOT NULL,
  `total_requirements` int(11) NOT NULL,
  `requirements_approved` int(11) NOT NULL,
  `requirements_remediated` int(11) NOT NULL,
  `total_hierarchies` int(11) NOT NULL,
  `hierarchies_approved` int(11) NOT NULL,
  `hierarchies_remediated` int(11) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `task_schedule_id` varchar(255) DEFAULT NULL,
  `trigger_id` varchar(128) DEFAULT NULL,
  `certification_definition_id` varchar(128) DEFAULT NULL,
  `phase` varchar(255) DEFAULT NULL,
  `next_phase_transition` bigint(20) DEFAULT NULL,
  `phase_config` longtext,
  `process_revokes_immediately` bit(1) DEFAULT NULL,
  `next_remediation_scan` bigint(20) DEFAULT NULL,
  `entitlement_granularity` varchar(255) DEFAULT NULL,
  `bulk_reassignment` bit(1) DEFAULT NULL,
  `continuous` bit(1) DEFAULT NULL,
  `continuous_config` longtext,
  `next_cert_required_scan` bigint(20) DEFAULT NULL,
  `next_overdue_scan` bigint(20) DEFAULT NULL,
  `exclude_inactive` bit(1) DEFAULT NULL,
  `parent` varchar(32) DEFAULT NULL,
  `immutable` bit(1) DEFAULT NULL,
  `electronically_signed` bit(1) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  `self_cert_reassignment` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `spt_cert_nxt_phs_tran` (`next_phase_transition`),
  KEY `spt_cert_electronic_signed` (`electronically_signed`),
  KEY `nxt_overdue_scan` (`next_overdue_scan`),
  KEY `spt_certification_finished` (`finished`),
  KEY `spt_cert_exclude_inactive` (`exclude_inactive`),
  KEY `spt_cert_auto_close_date` (`automatic_closing_date`),
  KEY `spt_cert_percent_complete` (`percent_complete`),
  KEY `nxt_cert_req_scan` (`next_cert_required_scan`),
  KEY `spt_certification_signed` (`signed`),
  KEY `spt_cert_nextRemediationScan` (`next_remediation_scan`),
  KEY `FK4E6F1832486634B7` (`assigned_scope`),
  KEY `FK4E6F1832A5FB1B1` (`owner`),
  KEY `FK4E6F18323733F724` (`parent`),
  KEY `SPT_IDX90929F9EDF01B7D0` (`assigned_scope_path`(255)),
  KEY `spt_cert_group_id_ci` (`group_definition_id`),
  KEY `spt_cert_type_ci` (`type`),
  KEY `spt_cert_phase_ci` (`phase`),
  KEY `spt_cert_group_name_ci` (`group_definition_name`),
  KEY `spt_cert_short_name_ci` (`short_name`),
  KEY `spt_cert_application_ci` (`application_id`),
  KEY `spt_cert_cert_def_id_ci` (`certification_definition_id`),
  KEY `spt_certification_name_ci` (`name`),
  KEY `spt_cert_trigger_id_ci` (`trigger_id`),
  KEY `spt_cert_manager_ci` (`manager`),
  KEY `spt_cert_task_sched_id_ci` (`task_schedule_id`),
  CONSTRAINT `FK4E6F18323733F724` FOREIGN KEY (`parent`) REFERENCES `spt_certification` (`id`),
  CONSTRAINT `FK4E6F1832486634B7` FOREIGN KEY (`assigned_scope`) REFERENCES `spt_scope` (`id`),
  CONSTRAINT `FK4E6F1832A5FB1B1` FOREIGN KEY (`owner`) REFERENCES `spt_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spt_certification`
--

LOCK TABLES `spt_certification` WRITE;
/*!40000 ALTER TABLE `spt_certification` DISABLE KEYS */;
INSERT INTO `spt_certification` VALUES ('a9fe0bbd92d11bbd8192d1c8d1070153',1730096451848,1735464212882,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Manager Access Review for Lisa Desrochers','Access Review for Lisa Desrochers',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,1732774855103,NULL,NULL,'2020007',NULL,NULL,NULL,NULL,NULL,' ',' ',1730096455103,6,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,3,0,0,0,4,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'Manager','Manager Certification [DATE] [10/28/24 11:50 AM]',NULL,'a9fe0bbd92d11bbd8192d1c8cb050150','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '\0',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd92d11bbd8192d1cb23230177',1730096603940,1735464214050,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Application Owner Access Review for HR System','HR System Access Review',NULL,'spadmin',_binary '',_binary '\0',1730096624842,NULL,1730096701786,1732775005175,NULL,'a9fe0bbd90c41bed8190d45e9a7705ef',NULL,NULL,NULL,NULL,NULL,NULL,' ',' ',1730096605175,1,0,1,0,100,0,0,0,1,0,1,0,100,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'ApplicationOwner','Application Owner Certification [DATE] [10/28/24 11:53 AM]',NULL,'a9fe0bbd92d11bbd8192d1cb1cf30174','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd92d11bbd8192d1cd87bc01a7',1730096760765,1735464214632,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Manager Access Review for Lisa Desrochers','Access Review for Lisa Desrochers',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,1732775164211,NULL,NULL,'2020007',NULL,NULL,NULL,NULL,NULL,' ',' ',1730096764211,5,1,0,0,0,0,0,0,15,2,0,0,0,0,0,0,0,0,2,0,0,0,3,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'Manager','Manager Certification [DATE] [10/28/24 11:52 AM]',NULL,'a9fe0bbd92d11bbd8192d1ca9c3f0173','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd92d11bbd8192d6b3bdb50345',1730178956725,1735464215827,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Global Role Membership Access Review for Lisa Desrochers','Global Role Membership Access Review for Lisa Desrochers',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,1732857358299,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,' ',' ',1730178958299,3,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'BusinessRoleMembership','Role Membership Certification [DATE] [10/29/24 10:45 AM]',NULL,'a9fe0bbd92d11bbd8192d6b3a63d0342','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '\0',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd92d11bbd8192d6b624b10363',1730179114162,1735464216590,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\" value=\"false\"/>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n  </Map>\n</Attributes>\n',NULL,'Targeted Access Review for 2020007','Access Review for 2020007',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,1732857518304,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,' ',' ',1730179118304,5,0,0,0,0,0,0,0,14,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'Focused',NULL,NULL,'a9fe0bbd92d11bbd8192d6b5fdf30351','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd92d11bbd8192d6bd2fb0038c',1730179575729,1735464217543,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Manager Access Review for Lisa Desrochers','Access Review for Lisa Desrochers',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,1732857977904,NULL,NULL,'2020007',NULL,NULL,NULL,NULL,NULL,' ',' ',1730179577904,6,0,0,0,0,0,0,0,21,0,0,0,0,0,0,0,0,0,5,0,0,0,5,0,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'Manager','Manager Certification [DATE] [10/29/24 10:56 AM]',NULL,'a9fe0bbd92d11bbd8192d6bd2a800389','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '\0',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd92d11bbd8192dc7211340817',1730275316020,1733292038367,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Manager Access Review for Lisa Desrochers','Access Review for Lisa Desrochers',NULL,'spadmin',_binary '',_binary '\0',1730275422194,NULL,1730275506820,1731139355858,NULL,NULL,'2020007',NULL,NULL,NULL,NULL,NULL,' ',' ',1730275355858,5,1,5,0,100,0,0,0,18,4,18,0,100,0,0,0,0,0,4,4,0,0,3,3,0,0,11,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'Manager','Manager Certification [DATE] [10/30/24 1:31 PM]',NULL,'a9fe0bbd92d11bbd8192dc720c2b0814','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Staged\"/>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"10\" scale=\"Day\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"10\" scale=\"Day\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd92d11bbd8192dc749a280851',1730275482152,1735550891696,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Application Owner Access Review for Contractor System Employee','Contractor System Employee Access Review',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,1732953884599,NULL,'a9fe0bbd90c41bed8190d46ece020648',NULL,NULL,NULL,NULL,NULL,NULL,' ',' ',1730275484599,5,0,0,0,0,0,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'ApplicationOwner','Application Owner Certification [DATE] [10/30/24 1:34 PM]',NULL,'a9fe0bbd92d11bbd8192dc749416084e','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '\0',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd92d11bbd8192dc74a9b30864',1730275486131,1735550893843,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Application Owner Access Review for HR System','HR System Access Review',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,1732953886725,NULL,'a9fe0bbd90c41bed8190d45e9a7705ef',NULL,NULL,NULL,NULL,NULL,NULL,' ',' ',1730275486725,3,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'ApplicationOwner','Application Owner Certification [DATE] [10/30/24 1:34 PM]',NULL,'a9fe0bbd92d11bbd8192dc749416084e','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '\0',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd92d11bbd8192dc7b4aeb08b4',1730275920619,1739343461306,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Manager Access Review for Lisa Desrochers','Access Review for Lisa Desrochers',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,1737801806758,NULL,NULL,'2020007',NULL,NULL,NULL,NULL,NULL,' ',' ',1736937806758,5,1,0,0,0,0,0,0,18,4,0,0,0,0,0,0,0,0,4,0,0,0,3,0,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'Manager','Manager Certification [DATE] [10/30/24 1:37 PM]',NULL,'a9fe0bbd92d11bbd8192dc77655e08aa','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Staged\"/>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"10\" scale=\"Day\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"10\" scale=\"Day\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd94f8111b8194f8f341040011',1739343479046,1739343521202,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Manager Access Review for Lisa Desrochers','Access Review for Lisa Desrochers',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL,'2020007',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,3,0,0,0,0,0,0,13,7,0,0,0,0,0,0,0,0,1,0,0,0,3,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'Manager','Manager Certification [DATE] [10/30/24 1:37 PM]',NULL,'a9fe0bbd92d11bbd8192dc77655e08aa','Staged',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Staged\"/>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"10\" scale=\"Day\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"10\" scale=\"Day\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',NULL,NULL,NULL,_binary '',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd957e1ec68195838f575001f5',1741668964189,1747112627196,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Application Owner Access Review for Contractor System Employee','Contractor System Employee Access Review',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,1744347371025,NULL,'a9fe0bbd90c41bed8190d46ece020648',NULL,NULL,NULL,NULL,NULL,NULL,' ',' ',1741668971025,7,0,0,0,0,0,0,0,14,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'ApplicationOwner','Application Owner Certification [DATE] [3/11/25 10:26 AM]',NULL,'a9fe0bbd957e1ec68195838f510d01f2','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '\0',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd957e1ec6819583a2f0420253',1741670248515,1747112627978,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Application Owner Access Review for PeopleSoft Employee System','PeopleSoft Employee System Access Review',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,1744348649843,NULL,'a9fe0bbd957e1ec68195839d91180238',NULL,NULL,NULL,NULL,NULL,NULL,' ',' ',1741670249843,2,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'ApplicationOwner','Application Owner Certification [DATE] [3/11/25 10:47 AM]',NULL,'a9fe0bbd957e1ec6819583a2ea1d0250','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '\0',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd957e1ec68195844f43e00317',1741681542112,1747112630147,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Application Owner Access Review for PeopleSoft Employee System','PeopleSoft Employee System Access Review',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,1744359943107,NULL,'a9fe0bbd957e1ec68195839d91180238',NULL,NULL,NULL,NULL,NULL,NULL,' ',' ',1741681543107,2,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'ApplicationOwner','Application Owner Certification [DATE] [3/11/25 1:55 PM]',NULL,'a9fe0bbd957e1ec68195844f3d890314','End',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"1\" scale=\"Month\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',' ',NULL,NULL,_binary '\0',NULL,_binary '\0',_binary '\0',NULL,_binary '\0'),('a9fe0bbd965b1be5819685c0e6a10c1d',1746000733857,1746000755588,NULL,NULL,NULL,'<Attributes>\n  <Map>\n    <entry key=\"autoSignOffWhenNothingToCertify\" value=\"false\"/>\n    <entry key=\"automateSignOffOnReassignment\" value=\"false\"/>\n    <entry key=\"certificationDelegationReview\" value=\"false\"/>\n    <entry key=\"notifyRemediation\">\n      <value>\n        <Boolean></Boolean>\n      </value>\n    </entry>\n    <entry key=\"requireReassignmentCompletion\" value=\"true\"/>\n    <entry key=\"suppressEmailWhenNothingToCertify\" value=\"false\"/>\n  </Map>\n</Attributes>\n',NULL,'Manager Access Review for Lisa Desrochers','Access Review for Lisa Desrochers',NULL,'spadmin',_binary '\0',_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL,'2020007',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,9,3,0,0,0,0,0,0,19,7,0,0,0,0,0,0,0,0,1,0,0,0,6,0,0,0,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'Manager','Manager Certification [DATE] [10/30/24 1:37 PM]',NULL,'a9fe0bbd92d11bbd8192dc77655e08aa','Staged',NULL,'<List>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Staged\"/>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Active\">\n    <Duration>\n      <TimeDuration amount=\"10\" scale=\"Day\"/>\n    </Duration>\n  </CertificationPhaseConfig>\n  <CertificationPhaseConfig enabled=\"true\" phase=\"Remediation\">\n    <Duration>\n      <TimeDuration amount=\"10\" scale=\"Day\"/>\n    </Duration>\n    <NotificationConfig>\n      <Configs>\n        <ReminderConfig before=\"true\" emailTemplateName=\"Work Item Reminder\" millis=\"1209600000\" once=\"true\"/>\n        <EscalationConfig before=\"true\" emailTemplateName=\"Work Item Escalation\" maxReminders=\"5\" millis=\"604800000\"/>\n      </Configs>\n    </NotificationConfig>\n  </CertificationPhaseConfig>\n</List>\n',_binary '\0',NULL,'Value',_binary '\0',_binary '\0',NULL,NULL,NULL,_binary '',NULL,_binary '\0',_binary '\0',NULL,_binary '\0');
/*!40000 ALTER TABLE `spt_certification` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-18 12:49:55
