-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Hôte : mysql
-- Généré le :  lun. 03 juin 2019 à 13:14
-- Version du serveur :  5.7.26
-- Version de PHP :  7.2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `mysql-chillout`
--
CREATE DATABASE IF NOT EXISTS `mysql-chillout` DEFAULT CHARACTER SET utf8  COLLATE utf8_general_ci ;
USE `mysql-chillout`;

-- --------------------------------------------------------

--
-- Structure de la table `Drink`
--

DROP TABLE IF EXISTS `Drink`;
CREATE TABLE `Drink` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `volume` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `rankingValue` int(11) NOT NULL,
  `nbRanking` int(11) NOT NULL,
  `isArchived` tinyint(1) NOT NULL,
  `picture` varchar(1024) DEFAULT NULL,
  `price` double(8,2) NOT NULL,
  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `Beer`
--

DROP TABLE IF EXISTS `Beer`;
CREATE TABLE `Beer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provenance` varchar(256) NOT NULL,
  `alcool` varchar(128) NOT NULL,
  `idx_drink` int(11) NOT NULL,
  FOREIGN KEY(`idx_drink`) REFERENCES Drink(`id`),
  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `SoftDrink`
--

DROP TABLE IF EXISTS `SoftDrink`;
CREATE TABLE `SoftDrink` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idx_drink` int(11) NOT NULL,
  FOREIGN KEY(`idx_drink`) REFERENCES Drink(`id`),
  PRIMARY KEY(`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `BlackList`
--

DROP TABLE IF EXISTS `BlackList`;
CREATE TABLE `BlackList` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mac` varchar(20) NOT NULL,
  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `Chat`
--

DROP TABLE IF EXISTS `Chat`;
CREATE TABLE `Chat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chatId` varchar(32) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `DrinkBlackList`
--

DROP TABLE IF EXISTS `DrinkBlackList`;
CREATE TABLE `DrinkBlackList` (
  `idx_drink` int(11) NOT NULL,
  `idx_blackList` int(11) NOT NULL,
  FOREIGN KEY(`idx_drink`) REFERENCES Drink(`id`),
  FOREIGN KEY(`idx_blackList`) REFERENCES BlackList(`id`),
  PRIMARY KEY(`idx_drink`, `idx_blackList`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Staff`
--

DROP TABLE IF EXISTS `Staff`;
CREATE TABLE `Staff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(1024) CHARACTER SET utf8 COLLATE utf8_estonian_ci NOT NULL,
  `pseudo` varchar(64) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY(`id`)

) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Storage`
--

DROP TABLE IF EXISTS `Storage`;
CREATE TABLE `Storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `StorageDrink`
--

DROP TABLE IF EXISTS `StorageDrink`;
CREATE TABLE `StorageDrink` (
  `idx_drink` int(11) NOT NULL,
  `idx_storage` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  FOREIGN KEY(`idx_drink`) REFERENCES Drink(`id`),
  FOREIGN KEY(`idx_storage`) REFERENCES Storage(`id`),
  PRIMARY KEY(`idx_drink`, `idx_storage`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `Drink` (`id`, `volume`, `name`, `description`, `rankingValue`, `nbRanking`, `isArchived`, `picture`, `price`)
VALUES
(NULL, '33', 'Punk IPA', 'Greate IPA with pale color', '4.9', '245', '0', 'https://proxy.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.beerhawk.co.uk%2Fmedia%2Fcatalog%2Fproduct%2Fb%2Fr%2Fbrewdog_punkipa_2.jpg&f=1', '3'),
(NULL, '33', 'Hazy Jane', 'Juste a awesome NEIPA', '5', '2044', '0', 'https://proxy.duckduckgo.com/iu/?u=https%3A%2F%2Fproducts1.imgix.drizly.com%2Fci-brewdog-hazy-jane-ipa-583f8d5af4f88cb9.png%3Fauto%3Dformat%252Ccompress%26fm%3Djpeg%26q%3D20&f=1', '3'),
(NULL, '50', 'Club Mate', 'Softdrink made with mate tea', '4.5', '199', '0', 'https://proxy.duckduckgo.com/iu/?u=http%3A%2F%2Fswiatshishy.pl%2F2043-home_default%2Fclub-mate-05l.jpg&f=1', '2')
;

INSERT INTO `Beer` (`id`, `provenance`, `alcool`, `idx_drink`)
VALUES
(NULL, 'Brewdog - Scotland', '5.6', '1'),
(NULL, 'Brewdog - Scotland', '7.2', '2')
;

INSERT INTO `SoftDrink` (`id`, `idx_drink`)
VALUES
(NULL, '3');
--
-- Index pour les tables déchargées
--

--
-- Index pour la table `Beer`
--
-- ALTER TABLE `Beer`
--  ADD UNIQUE KEY `idx_drink` (`idx_drink`);

--
-- Index pour la table `DrinkBlackList`
--
-- ALTER TABLE `DrinkBlackList`
--  ADD KEY `ref_blackList` (`idx_blackList`),
--  ADD KEY `ref_drinks` (`idx_drink`);

--
-- Index pour la table `SoftDrink`
--
-- ALTER TABLE `SoftDrink`
--  ADD UNIQUE KEY `idx_drink` (`idx_drink`);

--
-- Index pour la table `StorageDrink`
--
-- ALTER TABLE `StorageDrink`
--  ADD KEY `ref_drink` (`idx_drink`),
--  ADD KEY `ref_storage` (`idx_storage`,`idx_drink`) USING BTREE;

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `Beer`
--
-- ALTER TABLE `Beer`
--  ADD CONSTRAINT `beer_gen_drink` FOREIGN KEY (`idx_drink`) REFERENCES `Drink` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Contraintes pour la table `DrinkBlackList`
--
-- ALTER TABLE `DrinkBlackList`
--  ADD CONSTRAINT `ref_blackList` FOREIGN KEY (`idx_blackList`) REFERENCES `BlackList` (`id`);

--
-- Contraintes pour la table `Drink`
--
-- ALTER TABLE `Drink`
--  ADD CONSTRAINT `Drink_ibfk_1` FOREIGN KEY (`id`) REFERENCES `SoftDrink` (`id`);

--
-- Contraintes pour la table `SoftDrink`
--
-- ALTER TABLE `SoftDrink`
--  ADD CONSTRAINT `softDring_gen_drink` FOREIGN KEY (`idx_drink`) REFERENCES `Drink` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Contraintes pour la table `StorageDrink`
--
-- ALTER TABLE `StorageDrink`
--  ADD CONSTRAINT `ref_drink` FOREIGN KEY (`idx_drink`) REFERENCES `Drink` (`id`),
--  ADD CONSTRAINT `ref_storage` FOREIGN KEY (`idx_storage`) REFERENCES `Storage` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
