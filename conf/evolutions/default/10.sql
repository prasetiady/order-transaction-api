# Customers seed

# --- !Ups
INSERT INTO Customers
  (name)
VALUES
  ("Budi"),
  ("Ibu Budi"),
  ("Ayah Budi");
# --- !Downs
TRUNCATE Customers;
ALTER TABLE Customers AUTO_INCREMENT = 1;
