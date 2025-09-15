Create Database
CREATE DATABASE sweDB;

Create Admin User with Full Privileges
-- Create admin user with password
CREATE USER 'adminUser'@'localhost' IDENTIFIED BY '@dminUser!123';

-- Grant read, write, delete (all privileges) on the new database
GRANT ALL PRIVILEGES ON sweDB.* TO 'adminUser'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;

Notes:
Replace 'StrongPassword123!' with a secure password.
If you want the admin to connect from anywhere, replace 'localhost' with '%'.


Switch to the New Database
USE sweDB;

Create User Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,  -- unique row identifier
    username VARCHAR(50) NOT NULL UNIQUE, -- login name
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    employeenumber VARCHAR(20) UNIQUE NOT NULL,
    manager VARCHAR(50), -- could also be a foreign key to another user
    status VARCHAR(50) NOT NULL,
    usertype VARCHAR(50) NOT NULL,
    useraccess VARCHAR(50) NOT NULL
);

INSERT INTO `users` (`username`, `firstname`, `lastname`, `name`, `manager`, `employeenumber`, `status`, `usertype`, `useraccess`) VALUES
('testUser10', 'Test', 'User10', 'Test User1', '20250001', '20250010', 'ACTIVE', 'User', 'Admin'),
('testUser11', 'Test', 'User11', 'Test User2', '20250001', '20250011', 'ACTIVE', 'User', 'Admin');
COMMIT;

