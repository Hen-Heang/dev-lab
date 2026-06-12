-- =====================================================
-- General Store Admin + JWT Security (Clean Version)
-- PostgreSQL
-- =====================================================

-- 1️⃣ Drop old tables (safe reset)

DROP TABLE IF EXISTS tb_user_role;
DROP TABLE IF EXISTS tb_user;
DROP TABLE IF EXISTS tb_role;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;

-- =====================================================
-- 2️⃣ ROLE TABLE
-- =====================================================

CREATE TABLE tb_role (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(50) NOT NULL UNIQUE
);

-- =====================================================
-- 3️⃣ USER TABLE (General purpose)
-- =====================================================

CREATE TABLE tb_user (
                         id SERIAL PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         full_name VARCHAR(100),
                         phone VARCHAR(20),
                         is_verified BOOLEAN DEFAULT TRUE,
                         is_active BOOLEAN DEFAULT TRUE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 4️⃣ USER ↔ ROLE (Many-to-Many)
-- =====================================================

CREATE TABLE tb_user_role (
                              user_id INTEGER REFERENCES tb_user(id) ON DELETE CASCADE,
                              role_id INTEGER REFERENCES tb_role(id) ON DELETE CASCADE,
                              PRIMARY KEY (user_id, role_id)
);

-- =====================================================
-- 5️⃣ CATEGORY TABLE
-- =====================================================

CREATE TABLE category (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 6️⃣ PRODUCT TABLE
-- =====================================================

CREATE TABLE product (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(200) NOT NULL,
                         price INTEGER NOT NULL DEFAULT 0,
                         stock INTEGER NOT NULL DEFAULT 0,
                         category_id INTEGER NOT NULL REFERENCES category(id),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 7️⃣ INSERT ROLES
-- =====================================================

INSERT INTO tb_role (name) VALUES
                               ('ROLE_ADMIN'),
                               ('ROLE_USER'),
                               ('ROLE_MANAGER');

-- =====================================================
-- 8️⃣ INSERT USERS
-- Password: password123 (BCrypt strength=10)
-- Hash: $2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.
-- =====================================================

INSERT INTO tb_user (email, password, full_name, phone)
VALUES
    ('admin@email.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.',
     'Admin User',
     '010-0000-0000'),

    ('user@email.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.',
     'Normal User',
     '010-1111-1111');

-- =====================================================
-- 9️⃣ ASSIGN ROLES
-- =====================================================

-- Admin → ROLE_ADMIN
INSERT INTO tb_user_role (user_id, role_id)
VALUES (
           (SELECT id FROM tb_user WHERE email='admin@email.com'),
           (SELECT id FROM tb_role WHERE name='ROLE_ADMIN')
       );

-- User → ROLE_USER
INSERT INTO tb_user_role (user_id, role_id)
VALUES (
           (SELECT id FROM tb_user WHERE email='user@email.com'),
           (SELECT id FROM tb_role WHERE name='ROLE_USER')
       );

-- =====================================================
-- 🔟 INSERT SAMPLE CATEGORIES
-- =====================================================

INSERT INTO category (name) VALUES
                                ('Electronics'),
                                ('Clothing'),
                                ('Food'),
                                ('Furniture'),
                                ('Books');

-- =====================================================
-- 11️⃣ INSERT SAMPLE PRODUCTS
-- =====================================================

INSERT INTO product (name, price, stock, category_id) VALUES
                                                          ('Laptop', 1500000, 50, 1),
                                                          ('Smartphone', 1200000, 100, 1),
                                                          ('T-Shirt', 29000, 300, 2),
                                                          ('Jeans', 59000, 150, 2),
                                                          ('Ramen Box', 12000, 1000, 3),
                                                          ('Coffee Beans', 25000, 100, 3),
                                                          ('Office Chair', 150000, 40, 4),
                                                          ('Java Programming', 32000, 100, 5);

-- =====================================================
-- 12️⃣ VERIFY DATA
-- =====================================================

SELECT * FROM tb_role;
SELECT * FROM tb_user;
SELECT * FROM tb_user_role;

SELECT p.*, c.name AS category_name
FROM product p
         LEFT JOIN category c ON p.category_id = c.id
ORDER BY p.id;
