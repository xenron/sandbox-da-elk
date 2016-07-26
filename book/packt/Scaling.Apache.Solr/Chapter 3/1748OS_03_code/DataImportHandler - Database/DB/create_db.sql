DROP TABLE IF EXISTS `deptdb`.`employee`;
DROP TABLE IF EXISTS `deptdb`.`department`;
CREATE  TABLE `deptdb`.`department` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `department_code` VARCHAR(45) NOT NULL ,
  `department_name` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`id`) );

CREATE  TABLE `deptdb`.`employee` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `employee_name` VARCHAR(200) NOT NULL ,
  `gender` VARCHAR(1) NOT NULL DEFAULT 'M' ,
  `address` VARCHAR(100) NULL ,
  `dept_id` INT NULL ,
  PRIMARY KEY (`id`) );

ALTER TABLE `deptdb`.`employee` 
  ADD CONSTRAINT `dept_ref`
  FOREIGN KEY (`dept_id` )
  REFERENCES `deptdb`.`department` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `dept_ref_idx` (`dept_id` ASC) ;

## Data Addition

INSERT INTO `deptdb`.`department` (`department_code`, `department_name`) VALUES ('IT', 'InformationTechnology');
INSERT INTO `deptdb`.`department` (`department_code`, `department_name`) VALUES ('CIVIL', 'Civil Engineering');
INSERT INTO `deptdb`.`employee` (`emp_id`, `employee_name`, `gender`, `employeecol`, `dept_id`) VALUES ('1', 'Hrishikesh', 'M', 'Mulund East', '1');
INSERT INTO `deptdb`.`employee` (`employee_name`, `gender`, `address`, `dept_id`) VALUES ('Sachin', 'M', 'Mumbai', '1');
INSERT INTO `deptdb`.`employee` (`employee_name`, `gender`, `address`) VALUES ('John', 'M', 'Michigan');

############
## Use it for delta Updates
############
INSERT INTO `deptdb`.`department` (`id`, `department_code`, `department_name`) VALUES ('55', 'Civil', 'Civil Engineering');
UPDATE `deptdb`.`department` SET `department_code`='CSE' WHERE `id`='1';

