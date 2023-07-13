-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 13, 2023 at 08:35 AM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `food_manager`
--

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` char(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `name`) VALUES
(1, 'Rau củ'),
(2, 'Thịt'),
(3, 'Cá'),
(4, 'Trái cây'),
(5, 'Đồ uống'),
(6, 'Đồ ăn vặt');

-- --------------------------------------------------------

--
-- Table structure for table `cook`
--

CREATE TABLE `cook` (
  `id` int(10) UNSIGNED NOT NULL,
  `quantity` int(11) NOT NULL,
  `foodName` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `timeToCook` smallint(6) NOT NULL,
  `idUser` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cook`
--

INSERT INTO `cook` (`id`, `quantity`, `foodName`, `date`, `timeToCook`, `idUser`) VALUES
(1, 2, 'Cơm chiên', '2023-06-23', 0, 1),
(2, 1, 'Bún chả', '2023-06-23', 1, 2),
(3, 3, 'Canh chua cá', '2023-06-23', 0, 1),
(4, 2, 'Thịt kho tàu', '2023-06-23', 0, 2);

-- --------------------------------------------------------

--
-- Table structure for table `group`
--

CREATE TABLE `group` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `desc` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `group`
--

INSERT INTO `group` (`id`, `name`, `desc`) VALUES
(1, 'Gia đình A', 'nhà ngoại'),
(2, 'Gia đình B', 'nhà nội'),
(3, 'Gia đình C', 'nhà bác');

-- --------------------------------------------------------

--
-- Table structure for table `marketitem`
--

CREATE TABLE `marketitem` (
  `id` int(10) UNSIGNED NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit` char(255) DEFAULT NULL,
  `name` char(255) DEFAULT NULL,
  `type` smallint(6) DEFAULT NULL,
  `idGroup` int(10) UNSIGNED NOT NULL,
  `dayToBuy` date NOT NULL,
  `idUser` int(10) UNSIGNED DEFAULT NULL,
  `expirationTime` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `marketitem`
--

INSERT INTO `marketitem` (`id`, `quantity`, `unit`, `name`, `type`, `idGroup`, `dayToBuy`, `idUser`, `expirationTime`, `state`) VALUES
(1, 2, 'củ', 'Cà rốt', 1, 1, '2023-07-12', 1, 1, 2),
(2, 1, 'miếng', 'Thịt bò', 1, 1, '2023-07-11', NULL, 2, 0),
(3, 3, 'quả', 'Táo', 1, 1, '2023-07-12', NULL, 3, 0),
(5, 1, 'hộp', 'Sữa tươi', 1, 2, '2023-07-11', NULL, 2, 0),
(6, 3, 'cái', 'Chuối', 1, 2, '2023-07-12', NULL, 5, 0),
(7, 1, 'chai', 'Nước mắm', 1, 2, '2023-07-11', NULL, 7, 0),
(8, 2, 'gói', 'Bánh quy', 0, 3, '2023-07-12', NULL, 6, 0),
(9, 1, 'kg', 'Tôm', 1, 3, '2023-07-12', NULL, 10, 0),
(13, 1, 'củ', 'Cà tím', 1, 1, '2023-07-12', NULL, NULL, 0),
(14, 1, 'quả', 'Cà chua', 1, 1, '2023-07-12', NULL, NULL, 0),
(15, 2, 'mớ', 'Rau muống', 1, 1, '2023-07-12', NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `member`
--

CREATE TABLE `member` (
  `id` int(10) UNSIGNED NOT NULL,
  `idUser` int(10) UNSIGNED NOT NULL,
  `idGroup` int(10) UNSIGNED NOT NULL,
  `isLeader` smallint(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `member`
--

INSERT INTO `member` (`id`, `idUser`, `idGroup`, `isLeader`) VALUES
(1, 1, 1, 1),
(2, 2, 1, 0),
(3, 3, 1, 0),
(4, 4, 2, 1),
(5, 5, 2, 0),
(6, 1, 3, 0),
(7, 2, 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `recipe`
--

CREATE TABLE `recipe` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `desc` varchar(255) NOT NULL,
  `idUser` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `recipe`
--

INSERT INTO `recipe` (`id`, `name`, `desc`, `idUser`) VALUES
(1, 'Cá chưng', 'Món ăn truyền thống Việt Nam', 1),
(2, 'Bánh bao', 'Món ăn của Trung Quốc được giới trẻ yêu thích', 2);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(10) UNSIGNED NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` smallint(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `role`) VALUES
(1, 'q', '1', 1),
(2, 'Ngoc', 'abcdef', 1),
(3, 'Tuan', 'qwerty', 0),
(4, 'Hieu', '123abc', 0),
(5, 'Thinh', 'xyz789', 0),
(6, 'DUONG', 'abcdef', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `cook`
--
ALTER TABLE `cook`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cook_iduser_foreign` (`idUser`);

--
-- Indexes for table `group`
--
ALTER TABLE `group`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `marketitem`
--
ALTER TABLE `marketitem`
  ADD PRIMARY KEY (`id`),
  ADD KEY `marketitem_idgroup_foreign` (`idGroup`),
  ADD KEY `marketitem_iduser_foreign` (`idUser`);

--
-- Indexes for table `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`id`),
  ADD KEY `member_idgroup_foreign` (`idGroup`),
  ADD KEY `member_iduser_foreign` (`idUser`);

--
-- Indexes for table `recipe`
--
ALTER TABLE `recipe`
  ADD PRIMARY KEY (`id`),
  ADD KEY `recipe_iduser_foreign` (`idUser`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `cook`
--
ALTER TABLE `cook`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `group`
--
ALTER TABLE `group`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `marketitem`
--
ALTER TABLE `marketitem`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `member`
--
ALTER TABLE `member`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `recipe`
--
ALTER TABLE `recipe`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cook`
--
ALTER TABLE `cook`
  ADD CONSTRAINT `cook_iduser_foreign` FOREIGN KEY (`idUser`) REFERENCES `user` (`id`);

--
-- Constraints for table `marketitem`
--
ALTER TABLE `marketitem`
  ADD CONSTRAINT `marketitem_idgroup_foreign` FOREIGN KEY (`idGroup`) REFERENCES `group` (`id`),
  ADD CONSTRAINT `marketitem_iduser_foreign` FOREIGN KEY (`idUser`) REFERENCES `user` (`id`);

--
-- Constraints for table `member`
--
ALTER TABLE `member`
  ADD CONSTRAINT `member_idgroup_foreign` FOREIGN KEY (`idGroup`) REFERENCES `group` (`id`),
  ADD CONSTRAINT `member_iduser_foreign` FOREIGN KEY (`idUser`) REFERENCES `user` (`id`);

--
-- Constraints for table `recipe`
--
ALTER TABLE `recipe`
  ADD CONSTRAINT `recipe_iduser_foreign` FOREIGN KEY (`idUser`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
