
CREATE TABLE `roleimporterexample` (
  `function` text,
  `Bundle.name` text,
  `Identity.name` text,
  `Bundle.owner` text,
  `Bundle.type` text,
  `Bundle.description` text,
  `ManagedAttribute.value` text,
  `ManagedAttribute.application` text,
  `ManagedAttribute.attribute` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `roleimporterexample` VALUES ('create','Test IT Role 1','','spadmin','it','Test Role 1','','','');
INSERT INTO `roleimporterexample` VALUES ('update','Existing IT Role 2','','spadmin','it','Update Existing Role 1','','','');
INSERT INTO `roleimporterexample` VALUES ('update','Existing IT Role 3','','spadmin','it','Update Existing Role 2','','','');
INSERT INTO `roleimporterexample` VALUES ('create','Test Business Role 1','','spadmin','business','Test Role 2','','','');
INSERT INTO `roleimporterexample` VALUES ('delete','Delete IT Role 1','','','','','','','');
INSERT INTO `roleimporterexample` VALUES ('connect','Test Business Role 1','spadmin','','','','','','');
INSERT INTO `roleimporterexample` VALUES ('disconnect','Test Business Role 1','chrisc','','','','','','');
INSERT INTO `roleimporterexample` VALUES ('addEntitlement','Test IT Role 1','','','','','cn=Test_Group_1,ou=groups,dc=sailpoint,dc=com','LDAP GenericImport TEST','groups');
INSERT INTO `roleimporterexample` VALUES ('addEntitlement','Test IT Role 1','','','','','cn=Test_Group_2,ou=groups,dc=sailpoint,dc=com','LDAP GenericImport TEST','groups');
INSERT INTO `roleimporterexample` VALUES ('addEntitlement','Test IT Role 1','','','','','cn=Test_Group_3,ou=groups,dc=sailpoint,dc=com','LDAP GenericImport TEST','groups');
INSERT INTO `roleimporterexample` VALUES ('removeEntitlement','Existing IT Role 2','','','','','cn=Test_Group_2,ou=groups,dc=sailpoint,dc=com','LDAP GenericImport TEST','groups');

