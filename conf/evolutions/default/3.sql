# Customers schema

# --- !Ups
CREATE TABLE Customers (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE Customers;
