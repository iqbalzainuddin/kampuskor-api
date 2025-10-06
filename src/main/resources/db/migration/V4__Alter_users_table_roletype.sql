-- Normalize existing lowercase role_type values to uppercase
UPDATE users
SET role_type = UPPER(role_type);

-- Change role_type column from CHAR(1) to ENUM('A', 'I', 'S')
ALTER TABLE users
MODIFY COLUMN role_type ENUM('A', 'I', 'S') NOT NULL DEFAULT 'S';
