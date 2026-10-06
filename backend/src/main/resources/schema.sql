CREATE DATABASE IF NOT EXISTS service_booking DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE service_booking;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL,
  role ENUM('CUSTOMER', 'STAFF', 'ADMIN') NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS services (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  duration_minutes INT NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS time_slots (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  service_id BIGINT NOT NULL,
  starts_at DATETIME NOT NULL,
  ends_at DATETIME NOT NULL,
  capacity INT NOT NULL DEFAULT 1,
  booked_count INT NOT NULL DEFAULT 0,
  is_available BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_time_slots_service FOREIGN KEY (service_id) REFERENCES services(id),
  UNIQUE KEY uk_time_slots_service_start (service_id, starts_at)
);

CREATE TABLE IF NOT EXISTS orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  customer_id BIGINT NOT NULL,
  staff_id BIGINT NULL,
  service_id BIGINT NOT NULL,
  time_slot_id BIGINT NULL,
  status ENUM('PENDING', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
  scheduled_at DATETIME NOT NULL,
  total_amount DECIMAL(10, 2) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES users(id),
  CONSTRAINT fk_orders_staff FOREIGN KEY (staff_id) REFERENCES users(id),
  CONSTRAINT fk_orders_service FOREIGN KEY (service_id) REFERENCES services(id)
  ,CONSTRAINT fk_orders_time_slot FOREIGN KEY (time_slot_id) REFERENCES time_slots(id)
);

CREATE TABLE IF NOT EXISTS reviews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL UNIQUE,
  customer_id BIGINT NOT NULL,
  rating TINYINT NOT NULL,
  comment VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_reviews_order FOREIGN KEY (order_id) REFERENCES orders(id),
  CONSTRAINT fk_reviews_customer FOREIGN KEY (customer_id) REFERENCES users(id)
);
