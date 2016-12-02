# Customers seed

# --- !Ups
INSERT INTO Customers
  (name)
VALUES
  ("Budi"),
  ("Ibu Budi"),
  ("Ayah Budi");
# --- !Downs
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE Customers;
SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE Customers AUTO_INCREMENT = 1;
