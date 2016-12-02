# Orders schema

# --- !Ups
CREATE TABLE Orders (
  id INT(11) NOT NULL AUTO_INCREMENT,
  customerId INT(11) NOT NULL,
  status ENUM('NEW', 'HOLD', 'VERIFIED', 'SHIPPED', 'CANCELED') DEFAULT 'NEW',
  isSubmitted TINYINT(1) NOT NULL DEFAULT 0,
  isPaid TINYINT(1) NOT NULL DEFAULT 0,
  couponId INT(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (customerId) REFERENCES Customers(id) ON DELETE NO ACTION
);

# --- !Downs
DROP TABLE Orders;
