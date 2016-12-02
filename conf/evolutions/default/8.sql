# Coupons seed

# --- !Ups
INSERT INTO Coupons
  (code, quantity, validFrom, validUntil, discountAmount, discountType)
VALUES
  ("10%", 1000, NOW(), NOW() + INTERVAL 1 MONTH, 10, 'PERCENTAGE'),
  ("5%", 1000, NOW(), NOW() + INTERVAL 1 MONTH, 5, 'PERCENTAGE'),
  ("GOCENG", 1000, NOW(), NOW() + INTERVAL 1 MONTH, 5000, 'DECIMAL'),
  ("RUNS_OUT", 0, NOW(), NOW() + INTERVAL 1 MONTH, 1000, 'DECIMAL'),
  ("DUPLICATE", 1000, NOW(), NOW() + INTERVAL 1 MONTH, 1000, 'DECIMAL'),
  ("DUPLICATE", 1000, NOW() - INTERVAL 1 MONTH, NOW() - INTERVAL 1 DAY, 1000, 'DECIMAL'),
  ("EXPIRED", 1000, NOW() - INTERVAL 1 MONTH, NOW() - INTERVAL 1 DAY, 1000, 'DECIMAL');
# --- !Downs
TRUNCATE Coupons;
ALTER TABLE Coupons AUTO_INCREMENT = 1;
