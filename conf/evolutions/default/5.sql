# LineItems schema

# --- !Ups
CREATE TABLE LineItems (
  id INT(11) NOT NULL AUTO_INCREMENT,
  orderId INT(11) NOT NULL,
  productId INT(11) NOT NULL,
  productName VARCHAR(255) NOT NULL,
  productPrice DOUBLE UNSIGNED NOT NULL,
  quantity INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (orderId) REFERENCES Orders(id) ON DELETE CASCADE,
  FOREIGN KEY (productId) REFERENCES Products(id) ON DELETE NO ACTION
);

# --- !Downs
DROP TABLE LineItems;
