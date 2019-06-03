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

-- --------------------------------------------------------

--
-- Structure de la table `Beer`
--

DROP TABLE IF EXISTS `Beer`;
CREATE TABLE `Beer` (
  `id` int(11) NOT NULL,
  `provenance` varchar(256) NOT NULL,
  `alcool` varchar(128) NOT NULL,
  `idx_drink` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `BlackList`
--

DROP TABLE IF EXISTS `BlackList`;
CREATE TABLE `BlackList` (
  `id` int(11) NOT NULL,
  `mac` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `chat`
--

DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat` (
  `id` int(11) NOT NULL,
  `chatId` varchar(32) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `drikBlackList`
--

DROP TABLE IF EXISTS `drikBlackList`;
CREATE TABLE `drikBlackList` (
  `idx_drink` int(11) NOT NULL,
  `idx_blackList` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Drink`
--

DROP TABLE IF EXISTS `Drink`;
CREATE TABLE `Drink` (
  `id` int(11) NOT NULL,
  `volume` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `rankingValue` int(11) NOT NULL,
  `nbRanking` int(11) NOT NULL,
  `isArchived` tinyint(1) NOT NULL,
  `picture` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `SoftDrink`
--

DROP TABLE IF EXISTS `SoftDrink`;
CREATE TABLE `SoftDrink` (
  `id` int(11) NOT NULL,
  `idx_beer` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `Staff`
--

DROP TABLE IF EXISTS `Staff`;
CREATE TABLE `Staff` (
  `id` int(11) NOT NULL,
  `password` varchar(1024) CHARACTER SET utf8 COLLATE utf8_estonian_ci NOT NULL,
  `pseudo` varchar(64) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Storage`
--

DROP TABLE IF EXISTS `Storage`;
CREATE TABLE `Storage` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `StorageDrink`
--

DROP TABLE IF EXISTS `StorageDrink`;
CREATE TABLE `StorageDrink` (
  `idx_drink` int(11) NOT NULL,
  `idx_storage` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `Beer`
--
ALTER TABLE `Beer`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_drink` (`idx_drink`);

--
-- Index pour la table `BlackList`
--
ALTER TABLE `BlackList`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `chat`
--
ALTER TABLE `chat`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `drikBlackList`
--
ALTER TABLE `drikBlackList`
  ADD KEY `ref_blackList` (`idx_blackList`),
  ADD KEY `ref_drinks` (`idx_drink`);

--
-- Index pour la table `Drink`
--
ALTER TABLE `Drink`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `SoftDrink`
--
ALTER TABLE `SoftDrink`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_beer` (`idx_beer`);

--
-- Index pour la table `Staff`
--
ALTER TABLE `Staff`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `Storage`
--
ALTER TABLE `Storage`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `StorageDrink`
--
ALTER TABLE `StorageDrink`
  ADD KEY `ref_drink` (`idx_drink`),
  ADD KEY `ref_storage` (`idx_storage`,`idx_drink`) USING BTREE;

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `Staff`
--
ALTER TABLE `Staff`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `Beer`
--
ALTER TABLE `Beer`
  ADD CONSTRAINT `beer_gen_drink` FOREIGN KEY (`idx_drink`) REFERENCES `Drink` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Contraintes pour la table `drikBlackList`
--
ALTER TABLE `drikBlackList`
  ADD CONSTRAINT `ref_blackList` FOREIGN KEY (`idx_blackList`) REFERENCES `BlackList` (`id`);

--
-- Contraintes pour la table `Drink`
--
ALTER TABLE `Drink`
  ADD CONSTRAINT `Drink_ibfk_1` FOREIGN KEY (`id`) REFERENCES `SoftDrink` (`id`);

--
-- Contraintes pour la table `SoftDrink`
--
ALTER TABLE `SoftDrink`
  ADD CONSTRAINT `softDring_gen_drink` FOREIGN KEY (`idx_beer`) REFERENCES `Drink` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Contraintes pour la table `StorageDrink`
--
ALTER TABLE `StorageDrink`
  ADD CONSTRAINT `ref_drink` FOREIGN KEY (`idx_drink`) REFERENCES `Drink` (`id`),
  ADD CONSTRAINT `ref_storage` FOREIGN KEY (`idx_storage`) REFERENCES `Storage` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
