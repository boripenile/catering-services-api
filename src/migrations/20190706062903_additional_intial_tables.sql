CREATE TABLE `organisations_users_requests` (
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`code` VARCHAR(10) DEFAULT NULL,
	`organisation_id` BIGINT(11) NOT NULL,
	`organisation_code` VARCHAR(15) NOT NULL,
	`organisation_name` VARCHAR(100) NOT NULL,
	`user_id` BIGINT(11) NOT NULL,
	`user_email_address` VARCHAR(100) NOT NULL,
	`user_fullname` VARCHAR(100) NOT NULL,
	`title` VARCHAR(50) NOT NULL,
	`details` VARCHAR(255) NOT NULL,
	`information` VARCHAR(255) DEFAULT NULL,
	`status` ENUM ('pending', 'approved', 'rejected') DEFAULT 'pending',
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`created_by` VARCHAR(50) NOT NULL,
	`updated_by` VARCHAR(50) DEFAULT NULL,
	`version` INT(11) DEFAULT '0'
)ENGINE=InnoDB;

ALTER TABLE organisations_users_requests
ADD CONSTRAINT FK_organisations_users_requests_org
FOREIGN KEY (organisation_id) REFERENCES organisations(id);

ALTER TABLE organisations_users_requests
ADD CONSTRAINT FK_organisations_users_requests_user
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE organisations_users_requests
ADD UNIQUE INDEX idx_organisation_title_user (organisation_id, title, user_id, created_by); 
