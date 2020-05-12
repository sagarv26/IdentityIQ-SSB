CREATE TABLE `workgroup_import` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `workgroup_name` varchar(45) DEFAULT NULL,
  `workgroup_owner` varchar(45) DEFAULT NULL,
  `workgroup_descripton` varchar(45) DEFAULT NULL,
  `workgroup_email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO `workgroup_import` VALUES (1,'Workgroup_A','spadmin','This is workgroup A','workgroupa@sailpoint.com');
INSERT INTO `workgroup_import` VALUES (2,'Workgroup_B','spadmin','This is workgroup B','workgroupb@sailpoint.com');