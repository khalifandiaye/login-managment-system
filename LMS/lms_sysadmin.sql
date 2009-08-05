-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.45-community-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema lms
--

CREATE DATABASE IF NOT EXISTS lms;
USE lms;

--
-- Definition of table `action`
--

DROP TABLE IF EXISTS `action`;
CREATE TABLE `action` (
  `id` bigint(20) NOT NULL auto_increment,
  `action` varchar(255) default NULL,
  `name` varchar(255) default NULL,
  `rule` varchar(255) default NULL,
  `sort` bigint(20) NOT NULL,
  `state` varchar(255) default NULL,
  `target` varchar(255) default NULL,
  `role_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK74946A56480689D4` (`role_id`),
  CONSTRAINT `FK74946A56480689D4` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `action`
--

/*!40000 ALTER TABLE `action` DISABLE KEYS */;
INSERT INTO `action` (`id`,`action`,`name`,`rule`,`sort`,`state`,`target`,`role_id`) VALUES 
 (1,'.*','lms admin','ACCEPT',0,'.*','.*',1);
/*!40000 ALTER TABLE `action` ENABLE KEYS */;


--
-- Definition of table `application`
--

DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `application`
--

/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` (`id`,`name`) VALUES 
 (1,'LoginManagmentSystem');
/*!40000 ALTER TABLE `application` ENABLE KEYS */;


--
-- Definition of table `groups`
--

DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `application_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK7FA2C5F4923512A0` (`application_id`),
  CONSTRAINT `FK7FA2C5F4923512A0` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `groups`
--

/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` (`id`,`name`,`application_id`) VALUES 
 (1,'admin',1);
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;


--
-- Definition of table `groups_role`
--

DROP TABLE IF EXISTS `groups_role`;
CREATE TABLE `groups_role` (
  `role_id` bigint(20) NOT NULL,
  `groups_id` bigint(20) NOT NULL,
  KEY `FK62B56401E1991694` (`groups_id`),
  KEY `FK62B56401480689D4` (`role_id`),
  CONSTRAINT `FK62B56401480689D4` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK62B56401E1991694` FOREIGN KEY (`groups_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `groups_role`
--

/*!40000 ALTER TABLE `groups_role` DISABLE KEYS */;
INSERT INTO `groups_role` (`role_id`,`groups_id`) VALUES 
 (1,1);
/*!40000 ALTER TABLE `groups_role` ENABLE KEYS */;


--
-- Definition of table `groups_user`
--

DROP TABLE IF EXISTS `groups_user`;
CREATE TABLE `groups_user` (
  `groups_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  KEY `FK62B6CF56E1991694` (`groups_id`),
  KEY `FK62B6CF56ED314DB4` (`user_id`),
  CONSTRAINT `FK62B6CF56ED314DB4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK62B6CF56E1991694` FOREIGN KEY (`groups_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `groups_user`
--

/*!40000 ALTER TABLE `groups_user` DISABLE KEYS */;
INSERT INTO `groups_user` (`groups_id`,`user_id`) VALUES 
 (1,1);
/*!40000 ALTER TABLE `groups_user` ENABLE KEYS */;


--
-- Definition of table `organisation`
--

DROP TABLE IF EXISTS `organisation`;
CREATE TABLE `organisation` (
  `id` bigint(20) NOT NULL auto_increment,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `fax` varchar(255) default NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) default NULL,
  `state` varchar(255) default NULL,
  `street` varchar(255) default NULL,
  `streetnr` varchar(255) default NULL,
  `url` tinyblob,
  `washstore` text,
  `zip` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `organisation`
--

/*!40000 ALTER TABLE `organisation` DISABLE KEYS */;
/*!40000 ALTER TABLE `organisation` ENABLE KEYS */;


--
-- Definition of table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `sort` bigint(20) NOT NULL,
  `application_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK26F496923512A0` (`application_id`),
  CONSTRAINT `FK26F496923512A0` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `role`
--

/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`id`,`name`,`sort`,`application_id`) VALUES 
 (1,'admins',0,1);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;


--
-- Definition of table `role_user`
--

DROP TABLE IF EXISTS `role_user`;
CREATE TABLE `role_user` (
  `role_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  KEY `FK1407FDF4480689D4` (`role_id`),
  KEY `FK1407FDF4ED314DB4` (`user_id`),
  CONSTRAINT `FK1407FDF4ED314DB4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK1407FDF4480689D4` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `role_user`
--

/*!40000 ALTER TABLE `role_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_user` ENABLE KEYS */;


--
-- Definition of table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL auto_increment,
  `activ` bit(1) NOT NULL,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  `fax` varchar(255) default NULL,
  `firstname` varchar(255) default NULL,
  `language` varchar(255) default NULL,
  `lastlogin` datetime default NULL,
  `lastlogout` datetime default NULL,
  `logincounter` bigint(20) NOT NULL,
  `loginname` varchar(255) NOT NULL,
  `mobile` varchar(255) default NULL,
  `password` varchar(255) NOT NULL,
  `phonepriv` varchar(255) default NULL,
  `phonework` varchar(255) default NULL,
  `state` varchar(255) default NULL,
  `street` varchar(255) default NULL,
  `streetnr` varchar(255) default NULL,
  `surename` varchar(255) default NULL,
  `template` bit(1) NOT NULL,
  `washstore` text,
  `zip` varchar(255) default NULL,
  `organisation_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `loginname` (`loginname`),
  KEY `FK285FEB42678554` (`organisation_id`),
  CONSTRAINT `FK285FEB42678554` FOREIGN KEY (`organisation_id`) REFERENCES `organisation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`,`activ`,`city`,`country`,`email`,`fax`,`firstname`,`language`,`lastlogin`,`lastlogout`,`logincounter`,`loginname`,`mobile`,`password`,`phonepriv`,`phonework`,`state`,`street`,`streetnr`,`surename`,`template`,`washstore`,`zip`,`organisation_id`) VALUES 
 (1,0x01,NULL,'de_DE',NULL,NULL,'Admin','de',NULL,NULL,0,'admin',NULL,'wrLRIqHU',NULL,NULL,NULL,NULL,NULL,'Admin',0x00,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
