-- Cake Schema

# --- !Ups

CREATE TABLE cake (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE,
  price NUMERIC NOT NULL,
  on_sale BOOLEAN NOT NULL
);

CREATE TABLE supplier (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE coffee (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE,
  price NUMERIC NOT NULL,
  on_sale BOOLEAN NOT NULL,
  supplier_id INT NOT NULL,
  FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

INSERT INTO supplier (name) VALUES ('GOLD'), ('BEANIES');

INSERT INTO coffee (name, price, on_sale, supplier_id) VALUES
  ('G1', 0.25, false, (SELECT id FROM supplier WHERE name = 'GOLD')),
  ('B1', 0.25, false, (SELECT id FROM supplier WHERE name = 'BEANIES'));

# --- !Downs

DROP TABLE coffee;
DROP TABLE supplier;
DROP TABLE cake;

