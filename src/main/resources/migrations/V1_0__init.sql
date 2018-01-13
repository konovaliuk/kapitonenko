SET FOREIGN_KEY_CHECKS = 0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

DROP TABLE IF EXISTS `companies`;
CREATE TABLE IF NOT EXISTS `companies` (
  `id`                 INT(11)                 NOT NULL AUTO_INCREMENT,
  `pn_number`          CHAR(12)
                       COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name`        VARCHAR(255)
                       COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key_name`    VARCHAR(255)
                       COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key_address` VARCHAR(255)
                       COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `cashboxes`;
CREATE TABLE IF NOT EXISTS `cashboxes` (
  `id`         INT(11)                 NOT NULL AUTO_INCREMENT,
  `fn_number`  CHAR(10)
               COLLATE utf8_unicode_ci NOT NULL,
  `zn_number`  CHAR(12)
               COLLATE utf8_unicode_ci NOT NULL,
  `make`       VARCHAR(32)
               COLLATE utf8_unicode_ci NOT NULL,
  `deleted_at` DATETIME                NULL,
  `deleted_by` INT(11)                 NULL,
  PRIMARY KEY (`id`),
  KEY `FK_cashbox_deleted_by` (`deleted_by`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `payment_types`;
CREATE TABLE IF NOT EXISTS `payment_types` (
  `id`          INT(11)                 NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name` VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key`  VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `products`;
CREATE TABLE IF NOT EXISTS `products` (
  `id`              INT(11)        NOT NULL AUTO_INCREMENT,
  `unit_id`         INT(11)        NOT NULL,
  `price`           DECIMAL(10, 2) NOT NULL,
  `tax_category_id` INT(11)        NOT NULL,
  `quantity`        DECIMAL(10, 3) NOT NULL,
  `created_at`      DATETIME       NOT NULL,
  `created_by`      INT(11)        NOT NULL,
  `deleted_at`      DATETIME       NULL,
  `deleted_by`      INT(11)        NULL,
  PRIMARY KEY (`id`),
  KEY `FK_products_units` (`unit_id`),
  KEY `FK_products_tax` (`tax_category_id`),
  KEY `FK_products_created_by` (`created_by`),
  KEY `FK_products_deleted_by` (`deleted_by`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `product_locale`;
CREATE TABLE IF NOT EXISTS `product_locale` (
  `id`             INT(11)                 NOT NULL AUTO_INCREMENT,
  `product_id`     INT(11)                 NOT NULL,
  `locale_id`      INT(11)                 NOT NULL,
  `property_name`  VARCHAR(255)
                   COLLATE utf8_unicode_ci NOT NULL,
  `property_value` VARCHAR(255)
                   COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_product_locale_products` (`product_id`),
  KEY `FK_product_locale_locale` (`locale_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `receipts`;
CREATE TABLE IF NOT EXISTS `receipts` (
  `id`              INT(11)    NOT NULL AUTO_INCREMENT,
  `cashbox_id`      INT(11)    NOT NULL,
  `payment_type_id` INT(11)    NOT NULL,
  `receipt_type_id` INT(11)    NOT NULL,
  `cancelled`       TINYINT(1) NOT NULL DEFAULT '0',
  `created_at`      DATETIME   NOT NULL,
  `created_by`      INT(11)    NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_receipt_payment_type` (`payment_type_id`),
  KEY `FK_receipt_receipt_types` (`receipt_type_id`),
  KEY `FK_receipt_created_by` (`created_by`),
  KEY `FK_receipt_cashbox` (`cashbox_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `receipt_products`;
CREATE TABLE IF NOT EXISTS `receipt_products` (
  `id`         INT(11)        NOT NULL AUTO_INCREMENT,
  `receipt_id` INT(11)        NOT NULL,
  `product_id` INT(11)        NOT NULL,
  `quantity`   DECIMAL(10, 3) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_receipt_product_receipt` (`receipt_id`),
  KEY `FK_receipt_product_product` (`product_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `receipt_types`;
CREATE TABLE IF NOT EXISTS `receipt_types` (
  `id`          INT(11)                 NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name` VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key`  VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `tax_categories`;
CREATE TABLE IF NOT EXISTS `tax_categories` (
  `id`          INT(11)                 NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name` VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key`  VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  `rate`        DECIMAL(3, 1)           NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `units`;
CREATE TABLE IF NOT EXISTS `units` (
  `id`          INT(11)                 NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name` VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key`  VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id`            INT(11)                 NOT NULL AUTO_INCREMENT,
  `user_role_id`  INT(11)                 NOT NULL,
  `username`      VARCHAR(32)
                  COLLATE utf8_unicode_ci NOT NULL,
  `password_hash` VARCHAR(255)
                  COLLATE utf8_unicode_ci NOT NULL,
  `active`        TINYINT(1)              NOT NULL DEFAULT '0',
  `created_at`    DATETIME                NOT NULL,
  `deleted_at`    DATETIME                NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `FK_users_user_roles` (`user_role_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE IF NOT EXISTS `user_roles` (
  `id`          INT(11)                 NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  `bundle_name` VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  `bundle_key`  VARCHAR(255)
                COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `z_reports`;
CREATE TABLE IF NOT EXISTS `z_reports` (
  `id`           INT(11)        NOT NULL AUTO_INCREMENT,
  `cashbox_id`   INT(11)        NOT NULL,
  `cash_balance` DECIMAL(10, 2) NOT NULL,
  `created_at`   DATETIME       NOT NULL,
  `created_by`   INT(11)        NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_z_report_cashbox` (`cashbox_id`),
  KEY `FK_z_report_created_by` (`created_by`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

DROP TABLE IF EXISTS `locale`;
CREATE TABLE IF NOT EXISTS `locale` (
  `id`   INT(11)                 NOT NULL AUTO_INCREMENT,
  `name` CHAR(5)
         COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

ALTER TABLE `product_locale`
  ADD CONSTRAINT `FK_product_locale_locale` FOREIGN KEY (`locale_id`) REFERENCES `locale` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_product_locale_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `cashboxes`
  ADD CONSTRAINT `FK_cashbox_deleted_by` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `products`
  ADD CONSTRAINT `FK_products_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_products_deleted_by` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_products_tax` FOREIGN KEY (`tax_category_id`) REFERENCES `tax_categories` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_products_units` FOREIGN KEY (`unit_id`) REFERENCES `units` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `receipts`
  ADD CONSTRAINT `FK_receipt_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_receipt_cashbox` FOREIGN KEY (`cashbox_id`) REFERENCES `cashboxes` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_receipt_payment_type` FOREIGN KEY (`payment_type_id`) REFERENCES `payment_types` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_receipt_receipt_types` FOREIGN KEY (`receipt_type_id`) REFERENCES `receipt_types` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `receipt_products`
  ADD CONSTRAINT `FK_receipt_product_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_receipt_product_receipt` FOREIGN KEY (`receipt_id`) REFERENCES `receipts` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `users`
  ADD CONSTRAINT `FK_users_user_roles` FOREIGN KEY (`user_role_id`) REFERENCES `user_roles` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `z_reports`
  ADD CONSTRAINT `FK_z_report_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_z_report_cashbox` FOREIGN KEY (`cashbox_id`) REFERENCES `cashboxes` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

SET FOREIGN_KEY_CHECKS = 1;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
