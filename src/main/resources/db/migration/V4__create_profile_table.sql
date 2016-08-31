CREATE TABLE `fitness`.`profile` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `version` INT NOT NULL,
  `gender` ENUM('M', 'F') NOT NULL,
  `age` INT NULL,
  `height` FLOAT NULL,
  `weight` FLOAT NULL,
  `photo` VARCHAR(45) NULL,
  `user_id` INT NULL,
PRIMARY KEY (`id`),
INDEX `fk_user_id_idx` (`user_id` ASC),
CONSTRAINT `fk_user_id`
FOREIGN KEY (`user_id`)
REFERENCES `fitness`.`users` (`id`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);

