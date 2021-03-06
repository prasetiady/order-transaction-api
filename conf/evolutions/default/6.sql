# ShippingAddresses schema

# --- !Ups
CREATE TABLE ShippingAddresses (
  id INT(11) NOT NULL AUTO_INCREMENT,
  orderId INT(11) NOT NULL,
  address VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  phoneNumber VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (orderId) REFERENCES Orders(id) ON DELETE CASCADE
);

# --- !Downs
DROP TABLE ShippingAddresses;
