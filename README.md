# BankingManagementSyatem-JDBC
This is the project created using Java and JDBC as part of the Java Microservices training program by Edureka

Features
•User Registration & Login
•Account Creation
•Debit and Credit Transactions
•Money Transfer Between Accounts
•Balance Inquiry

Technologies Used
•Java
•JDBC (Java Database Connectivity)
•MySQL (or your preferred database system)

Note : Add below jar in the build path - 
mysql-connector-j-8.3.0

Database queries - 

CREATE DATABASE banking_system;
USE banking_system;

CREATE TABLE accounts(
account_number BIGINT NOT NULL PRIMARY KEY,
full_name VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL UNIQUE,
balance DECIMAL(10,2) NOT NULL,
security_pin CHAR(4) NOT Null);

DESCRIBE accounts;

CREATE TABLE user(
full_name VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL PRIMARY KEY, 
password VARCHAR(255) NOT NULL);

DESCRIBE user;

SELECT * FROM user;
SELECT * FROM accounts;

DELETE FROM accounts WHERE account_number = 100100100;
