-- phpMyAdmin SQL Dump
-- version 4.4.10
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Jan 07, 2016 at 05:14 PM
-- Server version: 5.5.42
-- PHP Version: 5.6.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `cmpesocial`
--

-- --------------------------------------------------------

--
-- Table structure for table `comment_event`
--

CREATE TABLE `comment_event` (
  `id` int(11) NOT NULL,
  `id_post` int(11) NOT NULL,
  `id_event` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `content` varchar(500) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `comment_group`
--

CREATE TABLE `comment_group` (
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

CREATE TABLE `event` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Triggers `event`
--
DELIMITER $$
CREATE TRIGGER `admin_going` AFTER INSERT ON `event`
 FOR EACH ROW INSERT INTO user_event(id_user,id_event,`status`) VALUES(NEW.id_user,NEW.id,1)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `group`
--

CREATE TABLE `group` (
  `id` int(11) NOT NULL,
  `name` varchar(200) COLLATE utf8_bin NOT NULL,
  `date_of_creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_admin` int(11) NOT NULL,
  `type` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `description` varchar(1000) COLLATE utf8_bin NOT NULL,
  `group_url` varchar(500) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Triggers `group`
--
DELIMITER $$
CREATE TRIGGER `admin_group` AFTER INSERT ON `group`
 FOR EACH ROW INSERT INTO user_group(id_user,id_group,`status`) VALUES(NEW.id_admin,NEW.id,1)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `post_event`
--

CREATE TABLE `post_event` (
  `id` int(11) NOT NULL,
  `id_event` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `content` varchar(500) COLLATE utf8_bin NOT NULL,
  `content_url` varchar(1000) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `post_group`
--

CREATE TABLE `post_group` (
  `id` int(11) NOT NULL,
  `id_group` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `post_text` varchar(1000) COLLATE utf8_bin NOT NULL,
  `post_url` varchar(200) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `tag_event`
--

CREATE TABLE `tag_event` (
  `id_event` int(11) NOT NULL,
  `tag` varchar(100) COLLATE utf8_bin NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `tag_group`
--

CREATE TABLE `tag_group` (
  `id_group` int(11) NOT NULL,
  `tag` varchar(100) COLLATE utf8_bin NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `tag_user`
--

CREATE TABLE `tag_user` (
  `id_user` int(11) NOT NULL,
  `tag` varchar(100) COLLATE utf8_bin NOT NULL,
  `hidden` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `surname` varchar(128) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `email` varchar(128) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `password` char(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `date_of_creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `profile_pic_link` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(20) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_event`
--

CREATE TABLE `user_event` (
  `id_event` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `user_group`
--

CREATE TABLE `user_group` (
  `id_user` int(11) NOT NULL,
  `id_group` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  ADD PRIMARY KEY (`id_user`,`id_event`),
  ADD KEY `fkevent` (`id_event`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `event`
--
ALTER TABLE `event`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `group`
--
ALTER TABLE `group`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `post_event`
--
ALTER TABLE `post_event`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `post_group`
--
ALTER TABLE `post_group`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
DELIMITER $$
--
-- Events
--
CREATE DEFINER=`root`@`localhost` EVENT `daily periodic` ON SCHEDULE EVERY 5 MINUTE STARTS '2015-11-23 22:47:15' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE `event`  SET `event`.`date` = DATE_ADD(`event`.`date`,INTERVAL 1 DAY) , `event`.`end_date` = DATE_ADD(`event`.`end_date`,INTERVAL 1 DAY)  WHERE `event`.`end_date`< NOW() AND `event`.`periodic` = 1$$

CREATE DEFINER=`root`@`localhost` EVENT `weekly` ON SCHEDULE EVERY 1 MINUTE STARTS '2015-11-23 22:48:19' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE `event`  SET `event`.`date` = DATE_ADD(`event`.`date`,INTERVAL 1 WEEK), `event`.`end_date` = DATE_ADD(`event`.`end_date`,INTERVAL 1 WEEK)  WHERE `event`.`end_date`< NOW() AND `event`.`periodic` = 2$$

CREATE DEFINER=`root`@`localhost` EVENT `monthly` ON SCHEDULE EVERY 1 MINUTE STARTS '2015-11-23 22:48:57' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE `event`  SET `event`.`date` = DATE_ADD(`event`.`date`,INTERVAL 1 MONTH) , `event`.`end_date` = DATE_ADD(`event`.`end_date`,INTERVAL 1 MONTH) WHERE `event`.`end_date`< NOW() AND `event`.`periodic` = 3$$

CREATE DEFINER=`root`@`localhost` EVENT `annually` ON SCHEDULE EVERY 1 MINUTE STARTS '2015-11-23 22:52:51' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE `event`  SET `event`.`date` = DATE_ADD(`event`.`date`,INTERVAL 1 YEAR) , `event`.`end_date` = DATE_ADD(`event`.`end_date`,INTERVAL 1 YEAR) WHERE `event`.`end_date`< NOW() AND `event`.`periodic` = 4$$

DELIMITER ;
