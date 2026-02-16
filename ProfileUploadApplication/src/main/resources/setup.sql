CREATE DATABASE IF NOT EXISTS profiledb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE profiledb;
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    profile_image_path VARCHAR(500)
);
INSERT INTO users (name) VALUES ('Nitin') ON DUPLICATE KEY UPDATE name=name;
SELECT 'Database setup complete! Test user created with ID=1' as status;
