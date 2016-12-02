# Products schema

# --- !Ups
CREATE TABLE Products (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  price DOUBLE UNSIGNED NOT NULL,
  quantity INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE Products;
