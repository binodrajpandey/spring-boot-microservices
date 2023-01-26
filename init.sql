CREATE DATABASE IF NOT EXISTS `order-service`;
CREATE DATABASE IF NOT EXISTS `inventory-service`;
CREATE DATABASE IF NOT EXISTS `jwt_security`;

-- create user password.
CREATE USER username@'localhost' IDENTIFIED BY 'password';
GRANT ALL ON *.* TO username@'localhost';
