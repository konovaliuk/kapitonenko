SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS `cashregister` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `cashregister`;

DROP TABLE IF EXISTS `companies`;
CREATE TABLE IF NOT EXISTS `companies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pn_number` char(12) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key_address` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `cashboxes`;
CREATE TABLE IF NOT EXISTS `cashboxes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fn_number` char(10) COLLATE utf8_unicode_ci NOT NULL,
  `zn_number` char(12) COLLATE utf8_unicode_ci NOT NULL,
  `make` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `deleted_at` datetime NULL,
  `deleted_by` int(11) NULL,
  PRIMARY KEY (`id`),
  KEY `FK_cashbox_deleted_by` (`deleted_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `payment_types`;
CREATE TABLE IF NOT EXISTS `payment_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `products`;
CREATE TABLE IF NOT EXISTS `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unit_id` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `tax_category_id` int(11) NOT NULL,
  `quantity` decimal(10,3) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `deleted_at` datetime NULL,
  `deleted_by` int(11) NULL,
  PRIMARY KEY (`id`),
  KEY `FK_products_units` (`unit_id`),
  KEY `FK_products_tax` (`tax_category_id`),
  KEY `FK_products_created_by` (`created_by`),
  KEY `FK_products_deleted_by` (`deleted_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `product_locale`;
CREATE TABLE IF NOT EXISTS `product_locale` (
  `id`             int(11)                              NOT NULL AUTO_INCREMENT,
  `product_id`     int(11)                              NOT NULL,
  `locale`         INT(11)                              NOT NULL,
  `property_name`  varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `property_value` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_product_locale_products` (`product_id`),
  KEY `FK_product_locale_locale` (`locale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `receipts`;
CREATE TABLE IF NOT EXISTS `receipts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cashbox_id` int(11) NOT NULL,
  `payment_type_id` int(11) NOT NULL,
  `receipt_type_id` int(11) NOT NULL,
  `cancelled` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_receipt_payment_type` (`payment_type_id`),
  KEY `FK_receipt_receipt_types` (`receipt_type_id`),
  KEY `FK_receipt_created_by` (`created_by`),
  KEY `FK_receipt_cashbox` (`cashbox_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `receipt_products`;
CREATE TABLE IF NOT EXISTS `receipt_products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `receipt_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` decimal(10,3) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_receipt_product_receipt` (`receipt_id`),
  KEY `FK_receipt_product_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `receipt_types`;
CREATE TABLE IF NOT EXISTS `receipt_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `tax_categories`;
CREATE TABLE IF NOT EXISTS `tax_categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `rate`  DECIMAL(3,1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `units`;
CREATE TABLE IF NOT EXISTS `units` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_role_id` int(11) NOT NULL,
  `username` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL,
  `deleted_at` datetime NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `FK_users_user_roles` (`user_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE IF NOT EXISTS `user_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `z_reports`;
CREATE TABLE IF NOT EXISTS `z_reports` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cashbox_id` int(11) NOT NULL,
  `last_receipt_id` int(11) NOT NULL,
  `cash_balance` decimal(10,2) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_z_report_receipt` (`last_receipt_id`),
  KEY `FK_z_report_cashbox` (`cashbox_id`),
  KEY `FK_z_report_created_by` (`created_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `locale`;
CREATE TABLE IF NOT EXISTS `locale` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(5) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `product_locale`
  ADD CONSTRAINT `FK_product_locale_locale` FOREIGN KEY (`locale`) REFERENCES `locale` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_product_locale_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE `cashboxes`
  ADD CONSTRAINT `FK_cashbox_deleted_by` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `products`
  ADD CONSTRAINT `FK_products_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_products_deleted_by` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_products_tax` FOREIGN KEY (`tax_category_id`) REFERENCES `tax_categories` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_products_units` FOREIGN KEY (`unit_id`) REFERENCES `units` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `receipts`
  ADD CONSTRAINT `FK_receipt_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_receipt_cashbox` FOREIGN KEY (`cashbox_id`) REFERENCES `cashboxes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_receipt_payment_type` FOREIGN KEY (`payment_type_id`) REFERENCES `payment_types` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_receipt_receipt_types` FOREIGN KEY (`receipt_type_id`) REFERENCES `receipt_types` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `receipt_products`
  ADD CONSTRAINT `FK_receipt_product_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_receipt_product_receipt` FOREIGN KEY (`receipt_id`) REFERENCES `receipts` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE `users`
  ADD CONSTRAINT `FK_users_user_roles` FOREIGN KEY (`user_role_id`) REFERENCES `user_roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `z_reports`
  ADD CONSTRAINT `FK_z_report_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_z_report_cashbox` FOREIGN KEY (`cashbox_id`) REFERENCES `cashboxes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_z_report_receipt` FOREIGN KEY (`last_receipt_id`) REFERENCES `receipts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

SET FOREIGN_KEY_CHECKS=1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
