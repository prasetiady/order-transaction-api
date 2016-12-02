# PaymentProffs schema

# --- !Ups
CREATE TABLE PaymentProffs (
  id INT(11) NOT NULL AUTO_INCREMENT,
  orderId INT(11) NOT NULL,
  amount DOUBLE UNSIGNED NOT NULL,
  note VARCHAR(255) NOT NULL,
  paymentDate DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (orderId) REFERENCES Orders(id) ON DELETE CASCADE
);

# --- !Downs
DROP TABLE PaymentProffs;
