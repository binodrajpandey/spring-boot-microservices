CREATE DATABASE IF NOT EXISTS `order-service`;
CREATE DATABASE IF NOT EXISTS `inventory-service`;
CREATE DATABASE IF NOT EXISTS `jwt_security`;

-- create user password.
CREATE USER username@'localhost' IDENTIFIED BY 'password';
GRANT ALL ON *.* TO username@'localhost';

USE jwt_security;

-- Table for storing application user roles
CREATE TABLE IF NOT EXISTS app_user_role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL -- Enum values will be stored as strings
);

-- Table for storing application user permissions
CREATE TABLE IF NOT EXISTS app_user_permission (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codename VARCHAR(255) NOT NULL -- Permission codename
);

-- Join table for many-to-many relationship between roles and permissions
CREATE TABLE IF NOT EXISTS app_user_role_permission_map (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES app_user_role(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES app_user_permission(id) ON DELETE CASCADE
);

-- Table for storing clients
CREATE TABLE IF NOT EXISTS client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    contracted_services INT -- Stores contracted services as integer
);

-- Table for storing application users
CREATE TABLE IF NOT EXISTS app_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,

    client_id INT,
    role_id INT,

    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES app_user_role(id) ON DELETE SET NULL
);
