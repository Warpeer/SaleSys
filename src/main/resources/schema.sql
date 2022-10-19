CREATE TABLE PersonalSubscribtions
(
    id INTEGER AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(13) NOT NULL,
    provider VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL,
    dataAmount DOUBLE NOT NULL,
    PRIMARY KEY(id)
);
CREATE TABLE employees
(
    id INTEGER AUTO_INCREMENT NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    hire_date DATETIME NOT NULL DEFAULT(current_timestamp),
    PRIMARY KEY (id)
);
CREATE TABLE admins
(
    id INTEGER AUTO_INCREMENT NOT NULL,
    employeeID INTEGER NOT NULL,
    role_date DATETIME NOT NULL DEFAULT(current_timestamp),
    FOREIGN KEY (employeeID) REFERENCES employees(id),
    PRIMARY KEY (id)
);
CREATE TABLE providers
(
  name VARCHAR(255) NOT NULL
);
