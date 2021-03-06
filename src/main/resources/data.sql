DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS OPERAND;
CREATE TABLE USER (
    username VARCHAR(250) NOT NULL PRIMARY KEY,
    password VARCHAR(250) NOT NULL
);
CREATE TABLE OPERAND (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(256) NOT NULL,
    session_id VARCHAR(256) NOT NULL,
    value NUMERIC NOT NULL,
    used BOOL
);
CREATE TABLE OPERATION (
   id INT AUTO_INCREMENT PRIMARY KEY,
   username VARCHAR(256) NOT NULL,
   session_id VARCHAR(256) NOT NULL,
   operation INT NOT NULL,
   operands VARCHAR(256) NOT NULL,
   date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   error VARCHAR(256)
);
INSERT INTO USER (username, password)
VALUES ('testuser', '$2a$10$lTAWFTyWYm3CzPnn16ujiOUFZ1eM3WyIVMot0FX6vhkVgOeCQA5ym');
SELECT * FROM USER;