CREATE DATABASE IF NOT EXISTS projectfunding;
USE projectfunding;

DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects` (
    `pId` INT AUTO_INCREMENT,
    `email` VARCHAR(50),
    `paymentInfo` VARCHAR(50),
    `name` VARCHAR(50),
    `desc` VARCHAR(100),
    `current` DOUBLE,
    `target` DOUBLE,
    `endDate` DATE,
    `status` VARCHAR(10),
    PRIMARY KEY (`pId`)
);

DROP TABLE IF EXISTS `rewards`;
CREATE TABLE `rewards` (
    `pId` INT,
    `rId` INT AUTO_INCREMENT,
    `tier` DOUBLE,
    `reward` VARCHAR(50),
    PRIMARY KEY (`pId`, `rId`)
);

DROP TABLE IF EXISTS `donations`;
CREATE TABLE `donations` (
    `dId` INT AUTO_INCREMENT,
    `pId` INT,
    `email` VARCHAR(50),
    `paymentInfo` VARCHAR(50),
    `amount` DOUBLE,
    PRIMARY KEY (`dId`, `pId`)
);