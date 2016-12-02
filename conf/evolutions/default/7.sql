# PaymentProff schema

# --- !Ups
CREATE TABLE PaymentProff (
  id INT(11) NOT NULL AUTO_INCREMENT,
  amount INT(11) NOT NULL,
  note VARCHAR(255) NOT NULL,
  paymentDate DATETIME DEFAULT CURRENT_TIMESTAMP, 
  PRIMARY KEY (id),
  FOREIGN KEY (orderId) REFERENCES Orders(id) ON DELETE CASCADE,
);

# --- !Downs
DROP TABLE PaymentProff;
