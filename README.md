# Bank_backend

#Schema for Customer table
CREATE TABLE Customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id VARCHAR(20) NOT NULL UNIQUE,
    account_no VARCHAR(20) NOT NULL UNIQUE,
    customer_name VARCHAR(200) NOT NULL,
    Address VARCHAR(100) NOT NULL,
    Phone BIGINT NOT NULL,
    email VARCHAR(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    pan_no VARCHAR(12) NOT NULL,
    account_type VARCHAR(20) NOT NULL
);

#Schema for Account table
CREATE TABLE Account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_no VARCHAR(20) NOT NULL UNIQUE,
    balance DOUBLE NOT NULL,
    account_type VARCHAR(15) NOT NULL,
    customer_id INT NOT NULL,
    CONSTRAINT fk_account_customer
        FOREIGN KEY (customer_id) REFERENCES Customer(id)
        ON DELETE CASCADE
);



#Schema for Transaction Table
CREATE TABLE Transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_no VARCHAR(20) NOT NULL,
    transaction_type VARCHAR(10) NOT NULL,
    amount DOUBLE NOT NULL,
    timestamp DATETIME NOT NULL
);
