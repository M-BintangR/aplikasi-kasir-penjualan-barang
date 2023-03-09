/*
Navicat MySQL Data Transfer

Source Server         : Bintang Database
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : db_kasir_barang

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2023-03-10 07:29:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tbl_barang`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_barang`;
CREATE TABLE `tbl_barang` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kode_barang` char(6) NOT NULL,
  `nama_barang` varchar(35) NOT NULL,
  `harga_barang` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of tbl_barang
-- ----------------------------
INSERT INTO `tbl_barang` VALUES ('3', '100001', 'Mebel Kursi', '1000000');
INSERT INTO `tbl_barang` VALUES ('4', '100002', 'Mebel Meja ', '1500000');

-- ----------------------------
-- Table structure for `tbl_pembeli`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_pembeli`;
CREATE TABLE `tbl_pembeli` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama_pembeli` varchar(35) NOT NULL,
  `kode_barang` char(6) NOT NULL,
  `jumlah_barang` int(11) NOT NULL,
  `harga_satuan` int(11) NOT NULL,
  `diskon` varchar(10) NOT NULL,
  `member` enum('tidak','ya') NOT NULL,
  `total_harga` double NOT NULL,
  `tanggal_beli` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of tbl_pembeli
-- ----------------------------

-- ----------------------------
-- Table structure for `tbl_users`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_users`;
CREATE TABLE `tbl_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(35) NOT NULL,
  `password` varchar(35) NOT NULL,
  `level` enum('admin','petugas') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of tbl_users
-- ----------------------------
INSERT INTO `tbl_users` VALUES ('1', 'Bintang', 'adminadmin', 'admin');
INSERT INTO `tbl_users` VALUES ('2', 'Bintang Petugas', 'adminadmin', 'petugas');
