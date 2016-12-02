# Products seed

# --- !Ups
INSERT INTO Products
  (name, price, quantity)
VALUES
  ("Product 1", 10000, 10000),
  ("Product 2", 20000, 10000),
  ("Product 3", 30000, 10000),
  ("Limited Product 1", 10000, 1),
  ("Limited Product 2", 10000, 2),
  ("Limited Product 3", 10000, 3),
  ("Limited Product 4", 10000, 4),
  ("Limited Product 5", 10000, 5),
  ("Runs Out Product", 10000, 0);
# --- !Downs
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE Products;
SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE Customers AUTO_INCREMENT = 1;
