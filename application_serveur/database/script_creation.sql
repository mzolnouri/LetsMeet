-- Base de données
-- INF8405 - Laboratoire 2
-- Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)


SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `inf8405` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `inf8405` ;

-- -----------------------------------------------------
-- Table `inf8405`.`groupe`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `inf8405`.`groupe` ;

CREATE TABLE IF NOT EXISTS `inf8405`.`groupe` (
  `idgroupe` INT NOT NULL,
  `nom` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idgroupe`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf8405`.`position`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `inf8405`.`position` ;

CREATE TABLE IF NOT EXISTS `inf8405`.`position` (
  `idposition` INT NOT NULL,
  `latitude` DOUBLE NOT NULL,
  `longitude` DOUBLE NOT NULL,
  `radius` DOUBLE NOT NULL,
  `position_time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`idposition`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf8405`.`utilisateur`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `inf8405`.`utilisateur` ;

CREATE TABLE IF NOT EXISTS `inf8405`.`utilisateur` (
  `idutilisateur` INT NOT NULL,
  `courriel` VARCHAR(255) NOT NULL,
  `photo` VARCHAR(1024) NULL,
  `organisateur` TINYINT(1) NOT NULL DEFAULT 0,
  `position_idposition` INT NOT NULL,
  `groupe_idgroupe` INT NOT NULL,
  PRIMARY KEY (`idutilisateur`),
  UNIQUE INDEX `courriel_UNIQUE` (`courriel` ASC),
  INDEX `fk_utilisateur_position_idx` (`position_idposition` ASC),
  INDEX `fk_utilisateur_groupe1_idx` (`groupe_idgroupe` ASC),
  CONSTRAINT `fk_utilisateur_position`
    FOREIGN KEY (`position_idposition`)
    REFERENCES `inf8405`.`position` (`idposition`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_utilisateur_groupe1`
    FOREIGN KEY (`groupe_idgroupe`)
    REFERENCES `inf8405`.`groupe` (`idgroupe`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf8405`.`preferences`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `inf8405`.`preferences` ;

CREATE TABLE IF NOT EXISTS `inf8405`.`preferences` (
  `idpreferences` INT NOT NULL,
  `endroit` VARCHAR(255) NOT NULL,
  `priorite` INT NOT NULL,
  `utilisateur_idutilisateur` INT NOT NULL,
  PRIMARY KEY (`idpreferences`),
  INDEX `fk_preferences_utilisateur1_idx` (`utilisateur_idutilisateur` ASC),
  CONSTRAINT `fk_preferences_utilisateur1`
    FOREIGN KEY (`utilisateur_idutilisateur`)
    REFERENCES `inf8405`.`utilisateur` (`idutilisateur`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf8405`.`calendrier`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `inf8405`.`calendrier` ;

CREATE TABLE IF NOT EXISTS `inf8405`.`calendrier` (
  `idcalendrier` INT NOT NULL,
  `heure_debut` TIMESTAMP NULL,
  `heure_fin` TIMESTAMP NULL,
  `activite` VARCHAR(255) NULL,
  PRIMARY KEY (`idcalendrier`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf8405`.`calendrier_has_utilisateur`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `inf8405`.`calendrier_has_utilisateur` ;

CREATE TABLE IF NOT EXISTS `inf8405`.`calendrier_has_utilisateur` (
  `calendrier_idcalendrier` INT NOT NULL,
  `utilisateur_idutilisateur` INT NOT NULL,
  PRIMARY KEY (`calendrier_idcalendrier`, `utilisateur_idutilisateur`),
  INDEX `fk_calendrier_has_utilisateur_utilisateur1_idx` (`utilisateur_idutilisateur` ASC),
  INDEX `fk_calendrier_has_utilisateur_calendrier1_idx` (`calendrier_idcalendrier` ASC),
  CONSTRAINT `fk_calendrier_has_utilisateur_calendrier1`
    FOREIGN KEY (`calendrier_idcalendrier`)
    REFERENCES `inf8405`.`calendrier` (`idcalendrier`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_calendrier_has_utilisateur_utilisateur1`
    FOREIGN KEY (`utilisateur_idutilisateur`)
    REFERENCES `inf8405`.`utilisateur` (`idutilisateur`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
