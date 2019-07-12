CREATE TABLE `images` (
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`image_blob` LONGBLOB,
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`created_by` VARCHAR(50) NOT NULL,
	`updated_by` VARCHAR(50)
)ENGINE=InnoDB;

CREATE TABLE `organisations`(
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`application_id` BIGINT(11) NOT NULL,
	`code` VARCHAR(50) NOT NULL,
	`referral_code` VARCHAR(50),
	`parent_referral_code` VARCHAR(50),
	`name` VARCHAR(50) NOT NULL,
	`description` VARCHAR(150),
	`motto` VARCHAR(100),
	`image_url` VARCHAR(150),
	`image_id` BIGINT(11),
	`active` TINYINT(1) DEFAULT '1',
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`version` INT(11) DEFAULT '0',
	`created_by` VARCHAR(50) NOT NULL,
	`updated_by` VARCHAR(50)
)ENGINE=InnoDB;

CREATE TABLE `applications`(
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`app_code` VARCHAR(20) NOT NULL,
	`app_name` VARCHAR(50) NOT NULL,
	`app_description` VARCHAR(100),
	`app_version` VARCHAR(20),
	`image_url` VARCHAR(150),
	`image_id` BIGINT(11),
	`active` TINYINT(1) DEFAULT '0',
	`latest` TINYINT(1) DEFAULT '0',
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`version` INT(11) DEFAULT '0',
	`created_by` VARCHAR(50) NOT NULL,
	`updated_by` VARCHAR(50)
)ENGINE=InnoDB;

--
--CREATE TABLE `applications_organisations`(
--	`id` BIGINT(11) auto_increment PRIMARY KEY,
--	`application_id` BIGINT(11) NOT NULL,
--	`organisation_id` BIGINT(11) NOT NULL,
--	`created_at` DATETIME DEFAULT NULL,
--	`updated_at` DATETIME DEFAULT NULL,
--	`active` TINYINT(1) DEFAULT '1',
--	`created_by` VARCHAR(50) NOT NULL,
--	`updated_by` VARCHAR(50)
--)ENGINE=InnoDB;

CREATE TABLE `addresses` (
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`phone_number` VARCHAR(50) NOT NULL,
	`address` VARCHAR(200) DEFAULT NULL,
	`address2` VARCHAR(200) DEFAULT NULL,
	`city` VARCHAR(50) DEFAULT NULL,
	`state` VARCHAR(50) DEFAULT NULL,
	`country` VARCHAR(50) DEFAULT NULL,
	`location` LONGBLOB DEFAULT NULL,
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`created_by` VARCHAR(50) NOT NULL,
	`updated_by` VARCHAR(50)
)ENGINE=InnoDB;

CREATE TABLE `users` (
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`address_id` BIGINT(11) NOT NULL,
	`username` VARCHAR(50) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
	`salt` VARCHAR(255) NOT NULL,
	`email_address` VARCHAR(50),
	`first_name` VARCHAR(50),
	`last_name` VARCHAR(50),
	`other_name` VARCHAR(50),
	`image_url` VARCHAR(200),
	`email_verified` TINYINT(1) DEFAULT '0',
	`phone_verified` TINYINT(1) DEFAULT '0',
	`email_verified_date` DATETIME,
	`phone_verified_date` DATETIME,
	`phone_verification_code` VARCHAR(10) DEFAULT NULL,
	`email_verification_code` VARCHAR(10) DEFAULT NULL,
	`reset_password_code` VARCHAR(10) DEFAULT NULL,
	`reset_password_request_date` DATETIME DEFAULT NULL, 
	`reset_password_date` DATETIME DEFAULT NULL,
	`verification_expired_date` DATETIME DEFAULT NULL, 
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`deleted` TINYINT(1) DEFAULT '0',
	`version` INT(11) DEFAULT '0',
	`active` TINYINT(1) DEFAULT '0',
	`created_by` VARCHAR(50) NOT NULL,
	`updated_by` VARCHAR(50)
)ENGINE=InnoDB;

CREATE TABLE `users_organisations` (
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`user_id` BIGINT(11) NOT NULL,
	`organisation_id` BIGINT(11) NOT NULL,
	`application_id` BIGINT(11) NOT NULL,
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`active` TINYINT(1) DEFAULT '1'
)ENGINE=InnoDB;

CREATE TABLE `roles`(
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`application_id` BIGINT(11) NOT NULL,
	`role_name` VARCHAR(50) NOT NULL,
	`description` VARCHAR(200) NOT NULL,
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`active` TINYINT(1) DEFAULT '1',
	`created_by` VARCHAR(50) NOT NULL,
	`updated_by` VARCHAR(50)
)ENGINE=InnoDB;

CREATE TABLE `permissions`(
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`application_id` BIGINT(11) NOT NULL,
	`permission_name` VARCHAR(50) NOT NULL,
	`description` VARCHAR(200) NOT NULL,
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`active` TINYINT(1) DEFAULT '1',
	`created_by` VARCHAR(50) NOT NULL,
	`updated_by` VARCHAR(50)
)ENGINE=InnoDB; 

CREATE TABLE `users_roles`(
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`user_id` BIGINT(11) NOT NULL,
	`role_id` BIGINT(11) NOT NULL,
	`organisation_id` BIGINT(11) NOT NULL,
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`active` TINYINT(1) DEFAULT '1'
)ENGINE=InnoDB;

CREATE TABLE `roles_permissions`(
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`role_id` BIGINT(11) NOT NULL,
	`permission_id` BIGINT(11) NOT NULL,
	`organisation_id` BIGINT(11) NOT NULL,
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`active` TINYINT(1) DEFAULT '1'
)ENGINE=InnoDB; 

CREATE TABLE `users_permissions`(
	`id` BIGINT(11) auto_increment PRIMARY KEY,
	`user_id` BIGINT(11) NOT NULL,
	`permission_id` BIGINT(11) NOT NULL,
	`organisation_id` BIGINT(11) NOT NULL,
	`created_at` DATETIME DEFAULT NULL,
	`updated_at` DATETIME DEFAULT NULL,
	`active` TINYINT(1) DEFAULT '1'
)ENGINE=InnoDB;

ALTER TABLE users
ADD CONSTRAINT FK_users_addresses
FOREIGN KEY (address_id) REFERENCES addresses(id);

ALTER TABLE organisations
ADD CONSTRAINT FK_organisations_images
FOREIGN KEY (image_id) REFERENCES images(id);

ALTER TABLE organisations
ADD CONSTRAINT FK_applications_organisations
FOREIGN KEY (application_id) REFERENCES applications(id);

ALTER TABLE applications
ADD CONSTRAINT FK_applications_images
FOREIGN KEY (image_id) REFERENCES images(id);

ALTER TABLE users_organisations
ADD CONSTRAINT FK_users_organisations
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE users_organisations
ADD CONSTRAINT FK_organisations_users
FOREIGN KEY (organisation_id) REFERENCES organisations(id);

ALTER TABLE users_organisations
ADD CONSTRAINT FK_applications_users
FOREIGN KEY (application_id) REFERENCES applications(id);

ALTER TABLE roles
ADD CONSTRAINT FK_roles_applications
FOREIGN KEY (application_id) REFERENCES applications(id);

ALTER TABLE permissions
ADD CONSTRAINT FK_permissions_applications
FOREIGN KEY (application_id) REFERENCES applications(id);

ALTER TABLE roles_permissions
ADD CONSTRAINT FK_roles_permissions
FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE roles_permissions
ADD CONSTRAINT FK_permissions_roles
FOREIGN KEY (permission_id) REFERENCES permissions(id);

ALTER TABLE roles_permissions
ADD CONSTRAINT FK_permissions_roles_organisation
FOREIGN KEY (organisation_id) REFERENCES organisations(id);

ALTER TABLE users_permissions
ADD CONSTRAINT FK_users_permissions
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE users_permissions
ADD CONSTRAINT FK_permissions_users
FOREIGN KEY (permission_id) REFERENCES permissions(id);

ALTER TABLE users_permissions
ADD CONSTRAINT FK_permissions_users_organisation
FOREIGN KEY (organisation_id) REFERENCES organisations(id);

ALTER TABLE users_roles
ADD CONSTRAINT FK_users_roles
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE users_roles
ADD CONSTRAINT FK_roles_users
FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE users_roles
ADD CONSTRAINT FK_roles_users_organisation
FOREIGN KEY (organisation_id) REFERENCES organisations(id);

ALTER TABLE users ADD UNIQUE INDEX idx_users_username_email (username, email_address);
ALTER TABLE addresses ADD UNIQUE INDEX idx_addresses_phone_number (phone_number);
ALTER TABLE organisations ADD UNIQUE INDEX idx_organisations (name);
ALTER TABLE roles ADD UNIQUE INDEX idx_roles_name (role_name);
ALTER TABLE permissions ADD UNIQUE INDEX idx_permissions_name (permission_name);
ALTER TABLE applications ADD UNIQUE INDEX idx_applications (app_name);
ALTER TABLE users_organisations ADD UNIQUE INDEX idx_users_organisations (user_id, organisation_id, application_id);
ALTER TABLE roles_permissions ADD UNIQUE INDEX idx_roles_permissions (role_id, permission_id, organisation_id);
ALTER TABLE users_permissions ADD UNIQUE INDEX idx_users_permissions (user_id, permission_id, organisation_id);
ALTER TABLE users_roles ADD UNIQUE INDEX idx_users_roles (user_id, role_id, organisation_id);

INSERT INTO `applications` (`id`, `app_code`, `app_name`, `app_description`, `app_version`, `active`, `created_at`, `created_by`)
VALUES (1, 'CJK2233448', 'Hotels and Catering Services System', 'Managing of hotels and catering services', '1.0.0', 1, NOW(), 'system');

INSERT INTO `organisations` (`id`, `code`, `application_id`, referral_code, `name`, `description`, `created_at`, `created_by`)
VALUES (1, '123456', 1, 'ABT9856FG', 'Youngprime Solution Ventures', 'Simplifying processes with software', NOW(), 'system');

INSERT INTO `addresses` (`id`, `phone_number`, `created_at`, `created_by`)
VALUES (1, '2348030000008', NOW(), 'admin');

--password=hashPassword("password01")

INSERT INTO `users` (`id`, `first_name`, `last_name`, `image_url`, `email_address`, `address_id`, `username`, `password`, `salt`, `phone_verified`, `email_verified`, `created_at`, `created_by`, `active`)
VALUES (1, 'Murtadha', 'Ali', 'http://res.cloudinary.com/boripe/image/upload/v1560089039/serve90/99654-name.jpg', 'admin@email.com', '1', 'admin', 'onbmOP+X9HJ0T6e8oZysVI6o6QzO3oYNjGKDABGzr8nuE0KB5rpU57v2D353FjcYgnFhn5b9bU2QpZMR7EEgwQ==', 'BCipJy/ulE2KNHlnJSpN3c9Sj5sgBatei5BTJRIr5F8=', 1, 1, NOW(), 'system', 1);

INSERT INTO `users_organisations` (`id`, `user_id`, `organisation_id`, `application_id`, `created_at`)
VALUES (1, '1', '1', '1', NOW());

INSERT INTO `roles` (`id`, `application_id`, `role_name`, `description`, `created_at`, `created_by`) 
VALUES (1, 1, 'superadmin', 'Super administrator', NOW(), 'system');

INSERT INTO `roles` (`id`, `application_id`,`role_name`, `description`, `created_at`, `created_by`) 
VALUES (2, 1, 'admin', 'Administrator', NOW(), 'system');

INSERT INTO `roles` (`id`, `application_id`,`role_name`, `description`, `created_at`, `created_by`) 
VALUES (3, 1, 'client', 'Client', NOW(), 'system');

INSERT INTO `roles` (`id`, `application_id`,`role_name`, `description`, `created_at`, `created_by`) 
VALUES (4, 1, 'staff', 'Staff', NOW(), 'system');

INSERT INTO `roles` (`id`, `application_id`,`role_name`, `description`, `created_at`, `created_by`) 
VALUES (5, 1, 'customer', 'Customer', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (2, 1, 'roles:list', 'View all roles', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (3, 1, 'roles:view', 'View a role', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (4, 1, 'roles:updateRole', 'Update role', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (5, 1, 'roles:createRole', 'Create role', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (6, 1, 'roles:deleteRole', 'Delete role', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (7, 1, 'roles:addUsersToRoles', 'Add users to roles', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (8, 1, 'roles:addPermissionsToRoles', 'Add permissions to roles', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (9, 1, 'roles:removeUsersFromRoles', 'Remove users from roles', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (10, 1, 'roles:removeRolesFromUsers', 'Remove roles from users', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (11, 1, 'roles:removePermissionsFromRoles', 'Remove permissions from roles', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (12, 1, 'roles:updateUsersRoles', 'Update users roles', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (13, 1, 'roles:updateUsersPermissions', 'Update users permissions', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (14, 1, 'roles:updateRolesPermissions', 'Update roles permissions', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (15, 1, 'roles:findUserPermissionsByUserId', 'Find user permissions', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (16, 1, 'roles:findUserRolesByUserId', 'Find user roles', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (17, 1, 'roles:findUsersByRoleName', 'Find users by role', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (18, 1, 'roles:findUsersByRoleId', 'Find users by role V2', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (19, 1, 'roles:findPermissionsByRoleId', 'Find permissions by role V2', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (20, 1, 'roles:findRoleNotSuper', 'Find roles except superadmin', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (21, 1, 'users:findUsersByOrganisation', 'Find users by organisation', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (22, 1, 'users:list', 'View all users', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (23, 1, 'users:view', 'View a user details', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (24, 1, 'users:findUserByEmailOrUsername', 'Find user by email or username', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (25, 1, 'roles:addPermissionsToUsers', 'Add permissions to users', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (26, 1, 'roles:removePermissionsFromUsers', 'Remove permissions from users', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (27, 1, 'permissions:findPermissionNotSuper', 'Find Permissions', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (28, 1, 'users:findUserDetails', 'View User details and permissions', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (29, 1, 'users:inviteUser', 'Ability to invite users', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (30, 1, 'users:findInvitedUsers', 'Find invited users', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (31, 1, 'users:resendInviteToUser', 'Resend invite to user', NOW(), 'system');

INSERT INTO `permissions` (`id`, `application_id`,`permission_name`, `description`, `created_at`, `created_by`) 
VALUES (32, 1, 'users:deleteInvitedUser', 'Delete invited user', NOW(), 'system');

INSERT INTO `users_roles` (`id`, `user_id`, `role_id`, `organisation_id`, `created_at`)
VALUES (1, '1', '1', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (1, '2', '3', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (2, '2', '4', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (3, '2', '5', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (4, '2', '7', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (5, '2', '8', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (6, '2', '9', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (7, '2', '10', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (8, '2', '11', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (9, '2', '12', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (10, '2', '13', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (11, '2', '14', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (12, '2', '15', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (13, '2', '16', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (14, '2', '17', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (15, '2', '18', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (16, '2', '19', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (17, '2', '20', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (18, '2', '21', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (19, '3', '15', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (20, '3', '16', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (21, '3', '19', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (22, '4', '15', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (23, '4', '16', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (24, '4', '19', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (25, '5', '15', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (26, '5', '16', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (27, '5', '19', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (28, '2', '23', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (29, '3', '23', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (30, '4', '23', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (31, '5', '23', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (32, '2', '24', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (33, '2', '25', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (34, '2', '26', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (35, '2', '27', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (36, '2', '28', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (37, '2', '29', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (38, '2', '30', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (39, '2', '31', '1', NOW());

INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `organisation_id`, `created_at`)
VALUES (40, '2', '32', '1', NOW());


