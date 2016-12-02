# Coupons schema

# --- !Ups
CREATE TABLE Coupons (
  id INT(11) NOT NULL AUTO_INCREMENT,
  code VARCHAR(255) NOT NULL,
  quantity INT(11) UNSIGNED NOT NULL,
  validFrom DATETIME NOT NULL,
  validUntil DATETIME NOT NULL,
  discountAmount DOUBLE UNSIGNED NOT NULL,
  discountType ENUM('PERCENTAGE', 'DECIMAL') DEFAULT 'PERCENTAGE',
  PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE Coupons;
