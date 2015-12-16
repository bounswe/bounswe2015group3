-- phpMyAdmin SQL Dump
-- version 4.2.7
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Dec 16, 2015 at 10:29 PM
-- Server version: 5.5.41-log
-- PHP Version: 5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `cmpesocial`
--

-- --------------------------------------------------------

--
-- Table structure for table `comment_event`
--

DROP TABLE IF EXISTS `comment_event`;
CREATE TABLE IF NOT EXISTS `comment_event` (
`id` int(11) NOT NULL,
  `id_post` int(11) NOT NULL,
  `id_event` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `content` varchar(500) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=3 ;

--
-- Dumping data for table `comment_event`
--

INSERT INTO `comment_event` (`id`, `id_post`, `id_event`, `id_user`, `date`, `content`) VALUES
(1, 1, 7, 4, '2015-12-13 00:37:16', 'comment1'),
(2, 1, 7, 4, '2015-12-13 00:37:16', 'comment2');

-- --------------------------------------------------------

--
-- Table structure for table `comment_group`
--

DROP TABLE IF EXISTS `comment_group`;
CREATE TABLE IF NOT EXISTS `comment_group` (
  `id` int(11) NOT NULL,
  `id_post` int(11) NOT NULL,
  `id_group` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `content` varchar(500) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
CREATE TABLE IF NOT EXISTS `event` (
`id` int(11) NOT NULL,
  `name` varchar(200) COLLATE utf8_bin NOT NULL,
  `date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `periodic` int(11) NOT NULL DEFAULT '0',
  `date_of_creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_user` int(11) NOT NULL,
  `location` varchar(500) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `type` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `id_group` int(11) DEFAULT NULL,
  `url` varchar(1000) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=11 ;

--
-- Dumping data for table `event`
--

INSERT INTO `event` (`id`, `name`, `date`, `end_date`, `periodic`, `date_of_creation`, `id_user`, `location`, `description`, `type`, `id_group`, `url`) VALUES
(8, 'Kahve donut yemece', '2015-12-25 00:00:00', '2015-12-16 05:00:00', 0, '2015-12-14 20:41:32', 1, 'kare block', 'kahve donut sigara eşliğinde bilgi ve kültür dolu goygoy yapıcaz.', '1', NULL, NULL),
(9, 'Kahve donut yemece', '2015-12-17 00:00:00', '2015-12-16 05:00:00', 0, '2015-12-14 20:41:43', 1, 'kare block', 'kahve donut sigara eşliğinde bilgi ve kültür dolu goygoy yapıcaz.', '1', NULL, NULL),
(10, 'Çay''a bekleriiz', '2015-12-17 00:00:00', '2015-12-17 00:00:00', 0, '2015-12-14 20:44:00', 1, 'Benim ev 1.sokak fsm gişelerine yakın', 'Çay yaptım kurabiye de var muhabbeti de siz getirin ', '0', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
CREATE TABLE IF NOT EXISTS `group` (
`id` int(11) NOT NULL,
  `name` varchar(200) COLLATE utf8_bin NOT NULL,
  `date_of_creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_admin` int(11) NOT NULL,
  `type` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `description` varchar(1000) COLLATE utf8_bin NOT NULL,
  `group_url` varchar(500) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=14 ;

--
-- Dumping data for table `group`
--

INSERT INTO `group` (`id`, `name`, `date_of_creation`, `id_admin`, `type`, `description`, `group_url`) VALUES
(11, 'asdasda', '2015-12-13 00:06:55', 5, '0', '                                asdas', 'dasdasd'),
(12, 'cemin grup', '2015-12-13 01:26:17', 4, '0', '                                cemin grup', 'https://www.petfinder.com/wp-content/uploads/2012/11/140272627-grooming-needs-senior-cat-632x475.jpg'),
(13, 'test2', '2015-12-13 01:32:52', 4, '3', '                                asdas', 'https://www.petfinder.com/wp-content/uploads/2012/11/140272627-grooming-needs-senior-cat-632x475.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `post_event`
--

DROP TABLE IF EXISTS `post_event`;
CREATE TABLE IF NOT EXISTS `post_event` (
`id` int(11) NOT NULL,
  `id_event` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `content` varchar(500) COLLATE utf8_bin NOT NULL,
  `content_url` varchar(1000) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=3 ;

--
-- Dumping data for table `post_event`
--

INSERT INTO `post_event` (`id`, `id_event`, `id_user`, `date`, `content`, `content_url`) VALUES
(1, 7, 4, '2015-12-13 00:36:37', 'post1', 'post1'),
(2, 7, 4, '2015-12-13 00:36:37', 'post2', 'post2');

-- --------------------------------------------------------

--
-- Table structure for table `post_group`
--

DROP TABLE IF EXISTS `post_group`;
CREATE TABLE IF NOT EXISTS `post_group` (
`id` int(11) NOT NULL,
  `id_group` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `post_text` varchar(1000) COLLATE utf8_bin NOT NULL,
  `post_url` varchar(200) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=4 ;

--
-- Dumping data for table `post_group`
--

INSERT INTO `post_group` (`id`, `id_group`, `id_user`, `date`, `post_text`, `post_url`) VALUES
(1, 12, 4, '2015-12-13 01:27:03', 'asdsadsa', '�imdilik bo� ge�'),
(2, 12, 4, '2015-12-13 01:27:16', 'jjj', '�imdilik bo� ge�'),
(3, 13, 4, '2015-12-13 01:33:08', 'deneme1 2', '�imdilik bo� ge�');

-- --------------------------------------------------------

--
-- Table structure for table `tag_event`
--

DROP TABLE IF EXISTS `tag_event`;
CREATE TABLE IF NOT EXISTS `tag_event` (
  `id_event` int(11) NOT NULL,
  `tag` varchar(100) COLLATE utf8_bin NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `tag_event`
--

INSERT INTO `tag_event` (`id_event`, `tag`, `date`) VALUES
(9, 'donut', '2015-12-25 20:44:41'),
(9, 'kahve', '2015-12-14 20:44:41'),
(10, 'kahve', '2015-12-14 20:44:41');

-- --------------------------------------------------------

--
-- Table structure for table `tag_group`
--

DROP TABLE IF EXISTS `tag_group`;
CREATE TABLE IF NOT EXISTS `tag_group` (
  `id_group` int(11) NOT NULL,
  `tag` varchar(100) COLLATE utf8_bin NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `tag_user`
--

DROP TABLE IF EXISTS `tag_user`;
CREATE TABLE IF NOT EXISTS `tag_user` (
  `id_user` int(11) NOT NULL,
  `tag` varchar(100) COLLATE utf8_bin NOT NULL,
  `hidden` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `tag_user`
--

INSERT INTO `tag_user` (`id_user`, `tag`, `hidden`) VALUES
(1, 'donut', 0),
(1, 'kahve', 0),
(2, 'kahve', 0);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
`id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `surname` varchar(128) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `email` varchar(128) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `password` char(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `date_of_creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `profile_pic_link` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(20) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=5 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `name`, `surname`, `email`, `password`, `date_of_creation`, `profile_pic_link`, `type`) VALUES
(1, 'halil', 'taskesen', 'halil@boun.edu.tr', '1234', '2015-11-03 18:05:27', NULL, '0'),
(2, 'otrivine', 'afacaaacan', 'umut@boun.edu.tr', '81dc9bdb52d04dc20036dbd8313ed055', '2015-11-03 18:24:11', NULL, '0'),
(3, 'test', 'test', 'test@test.com', '81dc9bdb52d04dc20036dbd8313ed055', '2015-11-10 12:54:11', NULL, '0'),
(4, 'cem', 'cem', 'cem', '67e34ed173dcf2b555855f3408d5e664', '2015-12-12 23:08:20', NULL, '1');

-- --------------------------------------------------------

--
-- Table structure for table `user_event`
--

DROP TABLE IF EXISTS `user_event`;
CREATE TABLE IF NOT EXISTS `user_event` (
  `id_event` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `user_event`
--

INSERT INTO `user_event` (`id_event`, `id_user`, `status`) VALUES
(8, 1, 1),
(9, 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_group`
--

DROP TABLE IF EXISTS `user_group`;
CREATE TABLE IF NOT EXISTS `user_group` (
  `id_user` int(11) NOT NULL,
  `id_group` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `user_group`
--

INSERT INTO `user_group` (`id_user`, `id_group`, `status`) VALUES
(4, 12, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comment_event`
--
ALTER TABLE `comment_event`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `event`
--
ALTER TABLE `event`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `group`
--
ALTER TABLE `group`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `post_event`
--
ALTER TABLE `post_event`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `post_group`
--
ALTER TABLE `post_group`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tag_event`
--
ALTER TABLE `tag_event`
 ADD PRIMARY KEY (`id_event`,`tag`);

--
-- Indexes for table `tag_group`
--
ALTER TABLE `tag_group`
 ADD PRIMARY KEY (`id_group`,`tag`);

--
-- Indexes for table `tag_user`
--
ALTER TABLE `tag_user`
 ADD PRIMARY KEY (`id_user`,`tag`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_event`
--
ALTER TABLE `user_event`
 ADD PRIMARY KEY (`id_user`,`id_event`), ADD KEY `fkevent` (`id_event`);

--
-- Indexes for table `user_group`
--
ALTER TABLE `user_group`
 ADD PRIMARY KEY (`id_user`,`id_group`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `comment_event`
--
ALTER TABLE `comment_event`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `event`
--
ALTER TABLE `event`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `group`
--
ALTER TABLE `group`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT for table `post_event`
--
ALTER TABLE `post_event`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `post_group`
--
ALTER TABLE `post_group`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `user_event`
--
ALTER TABLE `user_event`
ADD CONSTRAINT `fkevent` FOREIGN KEY (`id_event`) REFERENCES `event` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fkuser` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

DELIMITER $$
--
-- Events
--
DROP EVENT `annually_events`$$
CREATE DEFINER=`root`@`localhost` EVENT `annually_events` ON SCHEDULE EVERY 5 MINUTE STARTS '2015-11-28 17:38:24' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE `event`  SET `event`.`date` = DATE_ADD(`event`.`date`,INTERVAL 1 YEAR) , `event`.`end_date` = DATE_ADD(`event`.`end_date`,INTERVAL 1 YEAR) WHERE `event`.`end_date`< NOW() AND `event`.`periodic` = 4$$

DROP EVENT `monthly_event`$$
CREATE DEFINER=`root`@`localhost` EVENT `monthly_event` ON SCHEDULE EVERY 5 MINUTE STARTS '2015-11-28 17:37:49' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE `event`  SET `event`.`date` = DATE_ADD(`event`.`date`,INTERVAL 1 MONTH) , `event`.`end_date` = DATE_ADD(`event`.`end_date`,INTERVAL 1 MONTH) WHERE `event`.`end_date`< NOW() AND `event`.`periodic` = 3$$

DROP EVENT `weekly`$$
CREATE DEFINER=`root`@`localhost` EVENT `weekly` ON SCHEDULE EVERY 5 MINUTE STARTS '2015-11-28 17:37:12' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE `event`  SET `event`.`date` = DATE_ADD(`event`.`date`,INTERVAL 1 WEEK), `event`.`end_date` = DATE_ADD(`event`.`end_date`,INTERVAL 1 WEEK)  WHERE `event`.`end_date`< NOW() AND `event`.`periodic` = 2$$

DROP EVENT `daily_event`$$
CREATE DEFINER=`root`@`localhost` EVENT `daily_event` ON SCHEDULE EVERY 5 MINUTE STARTS '2015-11-28 17:36:21' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE `event`  SET `event`.`date` = DATE_ADD(`event`.`date`,INTERVAL 1 DAY) , `event`.`end_date` = DATE_ADD(`event`.`end_date`,INTERVAL 1 DAY)  WHERE `event`.`end_date`< NOW() AND `event`.`periodic` = 1$$

DELIMITER ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
