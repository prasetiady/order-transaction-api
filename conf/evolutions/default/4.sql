# Orders schema

# --- !Ups
CREATE TABLE Orders (
  id INT(11) NOT NULL AUTO_INCREMENT,
  status ENUM('NEW', 'HOLD', 'VERIFIED', 'SHIPPED', 'CANCELED') DEFAULT 'NEW',
  isComplete TINYINT(1) NOT NULL DEFAULT 0,
  customerId INT(11) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (customerId) REFERENCES Customers(id) ON DELETE NO ACTION
);

# --- !Downs
DROP TABLE Orders;
