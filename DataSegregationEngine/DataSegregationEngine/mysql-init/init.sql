-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS segregationdb;

-- Create app user
CREATE USER IF NOT EXISTS 'appuser'@'%' IDENTIFIED BY 'app123';

-- Grant privileges
GRANT ALL PRIVILEGES ON segregationdb.* TO 'appuser'@'%';
FLUSH PRIVILEGES;
